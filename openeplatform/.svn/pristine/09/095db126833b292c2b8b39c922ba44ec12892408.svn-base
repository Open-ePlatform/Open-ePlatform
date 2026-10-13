package com.nordicpeak.flowengine.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.unlogic.hierarchy.core.interfaces.settings.SettingHandler;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.html.HTMLUtils;
import se.unlogic.standardutils.reflection.ReflectionUtils;

import com.nordicpeak.flowengine.annotations.TextTagReplace;

public class TextTagReplacer {

	private static final Pattern TEXT_TAG_PATTERN = Pattern.compile("(\\$)[{](.*?)}");
	
	public static String replaceTextTags(String text, SettingHandler settingHandler) {
		
		Matcher matcher = TEXT_TAG_PATTERN.matcher(text);
	    
		while (matcher.find()) {
	        
			String tag = matcher.group(2);
			
			String value = settingHandler.getString(HTMLUtils.unEscapeHTML(tag));
			
			if(value == null) {
				value = "";				
			}
			
			text = text.replace("${" + tag + "}", value);
			
		}
		
		return text;
	}

	public static void replaceTextTags(Object bean, SiteProfile siteProfile) {

		if(siteProfile != null) {
		
			SettingHandler settingHandler = siteProfile.getSettingHandler();
			
			try {
	
				List<Field> fields = getAnnotatedFields(bean.getClass());
	
				for (Field field : fields) {
	
					Object value = field.get(bean);
	
					if (value != null) {
	
						if (value instanceof String) {
	
							field.set(bean, replaceTextTags((String) value, settingHandler));
	
						}
						
					}
				}
				
			} catch (IllegalArgumentException e) {
	
				throw new RuntimeException(e);
	
			} catch (IllegalAccessException e) {
	
				throw new RuntimeException(e);
			}
		
		}

	}

	public static List<Field> getAnnotatedFields(Class<?> clazz) {

		List<Field> fields = ReflectionUtils.getFields(clazz);

		Iterator<Field> iterator = fields.iterator();

		Field field;

		while (iterator.hasNext()) {

			field = iterator.next();

			if (!field.isAnnotationPresent(TextTagReplace.class)) {

				iterator.remove();

			} else if (!field.getType().equals(String.class) || Modifier.isFinal(field.getModifiers())) {

				throw new RuntimeException("Error parsing field " + field.getName() + " in " + clazz + ". Only non final String fields can be annotated with @TextTagReplace annotation.");
			}

			ReflectionUtils.fixFieldAccess(field);
		}

		return fields;
	}
	
	public static boolean hasTextTags(Object bean) {
		
		try {

			List<Field> fields = getAnnotatedFields(bean.getClass());

			for (Field field : fields) {

				Object value = field.get(bean);

				if (value != null) {

					if (value instanceof String) {

						Matcher matcher = TEXT_TAG_PATTERN.matcher((String) value);

						if(matcher.find()) {
							
							return true;
						}
						
					}
					
				}
				
			}
			
			return false;
			
		} catch (IllegalArgumentException e) {

			throw new RuntimeException(e);

		} catch (IllegalAccessException e) {

			throw new RuntimeException(e);
		}
		
	}

}
