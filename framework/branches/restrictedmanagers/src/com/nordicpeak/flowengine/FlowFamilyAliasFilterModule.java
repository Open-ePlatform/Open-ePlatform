package com.nordicpeak.flowengine;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.interfaces.FilterChain;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.FilterModuleDescriptor;
import se.unlogic.hierarchy.filtermodules.AnnotatedFilterModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.events.FlowBrowserCacheEvent;

public class FlowFamilyAliasFilterModule extends AnnotatedFilterModule {

	protected FlowBrowserModule flowBrowserModule;

	protected ConcurrentHashMap<String, Integer> aliasMap = new ConcurrentHashMap<String, Integer>();

	@Override
	public void init(FilterModuleDescriptor moduleDescriptor, SystemInterface systemInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, systemInterface, dataSource);
	}

	@Override
	public void unload() throws Exception {

		super.unload();
	}

	@InstanceManagerDependency(required = true)
	public void setSiteProfileHandler(FlowBrowserModule flowBrowserModule) {

		this.flowBrowserModule = flowBrowserModule;

		if (flowBrowserModule != null) {

			processEvent((FlowBrowserCacheEvent) null, null);

		}
	}

	@EventListener(channel=FlowBrowserModule.class)
	public void processEvent(FlowBrowserCacheEvent event, EventSource eventSource) {

		if (flowBrowserModule != null) {

			Collection<Flow> flows = flowBrowserModule.getLatestPublishedFlowVersions();

			ConcurrentHashMap<String, Integer> newAliasMap = new ConcurrentHashMap<String, Integer>();

			if (!CollectionUtils.isEmpty(flows)) {

				HashSet<FlowFamily> flowFamilies = new HashSet<FlowFamily>();

				for (Flow flow : flows) {
					flowFamilies.add(flow.getFlowFamily());
				}

				for (FlowFamily flowFamily : flowFamilies) {

					if (!CollectionUtils.isEmpty(flowFamily.getAliases())) {

						for (String alias : flowFamily.getAliases()) {

							newAliasMap.put(alias, flowFamily.getFlowFamilyID());
						}
					}
				}
			}
			
			aliasMap = newAliasMap;
		}
	}


	@Override
	public void processFilterRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FilterChain filterChain) throws TransformerException, IOException {

		if (uriParser.size() == 1) {

			if (flowBrowserModule == null) {

				log.warn("Filter module " + moduleDescriptor + " found no FlowBrowserModule, redirecting disabled");

			} else {

				String alias = uriParser.get(0);

				if (systemInterface.getRootSection().getForegroundModuleCache().getEntry(alias) == null && systemInterface.getRootSection().getSectionCache().getEntry(alias) == null) {

					Integer flowFamilyID = aliasMap.get(alias);

					if (flowFamilyID != null) {

						try {
							flowBrowserModule.redirectToMethod(req, res, "/overview/" + flowFamilyID);

						} catch (IOException e) {

							log.info("Error redirecting user " + user + " to flow overview for flowFamily " + flowFamilyID);
						}

						return;
					}
				}
			}
		}

		filterChain.doFilter(req, res, user, uriParser);
	}
}