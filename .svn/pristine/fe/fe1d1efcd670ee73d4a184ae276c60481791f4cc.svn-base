package com.nordicpeak.flowengine.populators;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.utils.crud.BeanIDParser;
import se.unlogic.webutils.http.URIParser;

public class FlowAdminFragmentExtensionViewCRUDIDParser implements BeanIDParser<Integer> {

	private static final FlowAdminFragmentExtensionViewCRUDIDParser INSTANCE = new FlowAdminFragmentExtensionViewCRUDIDParser();
	
	private FlowAdminFragmentExtensionViewCRUDIDParser(){}
	
	@Override
	public Integer getBeanID(URIParser uriParser, HttpServletRequest req, String getMode) {

		return uriParser.getInt(5);
	}

	public static BeanIDParser<Integer> getInstance() {

		return INSTANCE;
	}
}
