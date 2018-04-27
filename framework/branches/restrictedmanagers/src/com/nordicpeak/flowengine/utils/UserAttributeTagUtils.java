package com.nordicpeak.flowengine.utils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;


public class UserAttributeTagUtils {

	private static final Pattern USER_ATTRIBUTE_TAG_PATTERN = Pattern.compile("(\\$user.attribute)[{](.*?)}");
	
	protected static Set<String> getAttributeTags(String text) {

		Matcher matcher = USER_ATTRIBUTE_TAG_PATTERN.matcher(text);
	    
		Set<String> attributeTags = null;
		
		while (matcher.find()) {
	        
			String tag = matcher.group(2);
			
			attributeTags = CollectionUtils.addAndInstantiateIfNeeded(attributeTags, tag);
		}
		
		return attributeTags;
	}
	
	public static String replaceTags(String text, User user){
		
		Set<String> tags = getAttributeTags(text);
		
		if(tags == null){
			
			return text;
		}
		
		return replaceTags(text, user.getAttributeHandler(), tags);
	}
	
	public static String replaceTags(String text, AttributeHandler attributeHandler, Set<String> tags){
		
		for(String tag : tags){
			
			String value = attributeHandler.getString(tag);
			
			if(value == null){
				
				value = "";
			}
			
			text = text.replace("$user.attribute{" + tag + "}", value);
		}
		
		return text;
	}
}
