package com.nordicpeak.flowengine.attachments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.interfaces.FlowInstanceOverviewExtensionProvider;

public class FlowInstanceOverviewExtensionProviderWrapper implements FlowInstanceOverviewExtensionProvider {
	
	
	private final boolean manager;
	private final FlowInstanceAttachmentsModule flowInstanceAttachmentsModule;
	
	public FlowInstanceOverviewExtensionProviderWrapper(boolean manager, FlowInstanceAttachmentsModule flowInstanceAttachmentsModule) {
		super();
		this.manager = manager;
		this.flowInstanceAttachmentsModule = flowInstanceAttachmentsModule;
	}
	
	@Override
	public ExtensionLink getOverviewTabHeaderExtensionLink(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user) throws Exception {
		return flowInstanceAttachmentsModule.getOverviewTabHeaderExtensionLink(flowInstance, req, uriParser, user);
	}
	
	@Override
	public ViewFragment getOverviewTabContentsViewFragment(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user) throws Exception {
		return flowInstanceAttachmentsModule.getOverviewTabContentsViewFragment(flowInstance, req, uriParser, user, manager);
	}
	
	@Override
	public ForegroundModuleResponse processOverviewExtensionRequest(FlowInstance flowInstance, HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user) throws Exception {
		return flowInstanceAttachmentsModule.processOverviewExtensionRequest(flowInstance, req, res, uriParser, user, manager);
	}
	
	@Override
	public String getOverviewExtensionProviderID() {
		return flowInstanceAttachmentsModule.getOverviewExtensionProviderID();
	}
	
}
