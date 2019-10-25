package com.nordicpeak.flowengine.listeners;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.crud.FragmentLinkScriptFilter;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.interfaces.ExternalMessageExtensionProvider;

public class ExternalMessageExtensionElementableListener implements ElementableListener<ExternalMessage> {

	private static final Logger log = Logger.getLogger(ExternalMessageExtensionElementableListener.class);

	private final List<ExternalMessageExtensionProvider> externalMessageExtensionProviders;
	private final FlowInstance flowInstance;
	private final HttpServletRequest req;
	private final User user;
	private final URIParser uriParser;
	private final boolean manager;

	public ExternalMessageExtensionElementableListener(List<ExternalMessageExtensionProvider> externalMessageExtensionProviders, FlowInstance flowInstance, HttpServletRequest req, User user, URIParser uriParser, boolean manager) {

		this.externalMessageExtensionProviders = externalMessageExtensionProviders;
		this.flowInstance = flowInstance;
		this.req = req;
		this.user = user;
		this.uriParser = uriParser;
		this.manager = manager;
	}

	@Override
	public void elementGenerated(Document doc, Element element, ExternalMessage externalMessage) {

		if (!CollectionUtils.isEmpty(externalMessageExtensionProviders)) {

			externalMessage.setFlowInstance(flowInstance);

			Element pluginFragments = doc.createElement("ViewFragmentExtension");

			for (ExternalMessageExtensionProvider extensionProvider : externalMessageExtensionProviders) {

				try {

					ViewFragment extensionSettings = extensionProvider.getViewFragment(externalMessage, req, user, uriParser, manager);

					if (extensionSettings != null) {

						pluginFragments.appendChild(extensionSettings.toXML(doc));
						FragmentLinkScriptFilter.addViewFragment(extensionSettings, req);
					}

				} catch (Exception e) {

					log.error("Error getting show view fragment for extension provider " + extensionProvider + " for user " + user, e);
				}
			}

			if (pluginFragments.hasChildNodes()) {

				element.appendChild(pluginFragments);
			}
		}
	}

}
