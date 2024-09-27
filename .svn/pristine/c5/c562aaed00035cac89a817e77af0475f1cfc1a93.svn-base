package com.nordicpeak.flowengine.populators;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.utils.crud.BeanIDParser;
import se.unlogic.webutils.http.URIParser;


public class MessageTemplateBeanIDParser implements BeanIDParser<Integer> {

	private static final MessageTemplateBeanIDParser INSTANCE = new MessageTemplateBeanIDParser();
	
	private MessageTemplateBeanIDParser(){}
	
	@Override
	public Integer getBeanID(URIParser uriParser, HttpServletRequest req, String getMode) {

		return uriParser.getInt(3);
	}

	public static BeanIDParser<Integer> getInstance() {

		return INSTANCE;
	}
}
