package com.nordicpeak.flowengine.flowapprovalmodule.populators;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.utils.crud.BeanIDParser;
import se.unlogic.webutils.http.URIParser;

public class FlowApprovalActivityGroupMessageTemplateBeanIDParser implements BeanIDParser<Integer> {

	private static final FlowApprovalActivityGroupMessageTemplateBeanIDParser INSTANCE = new FlowApprovalActivityGroupMessageTemplateBeanIDParser();
	
	private FlowApprovalActivityGroupMessageTemplateBeanIDParser(){}
	
	@Override
	public Integer getBeanID(URIParser uriParser, HttpServletRequest req, String getMode) {

		return uriParser.getInt(5);
	}

	public static BeanIDParser<Integer> getInstance() {

		return INSTANCE;
	}
}