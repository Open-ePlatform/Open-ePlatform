package com.nordicpeak.flowengine.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import se.unlogic.standardutils.beans.Named;

public class NameUtils {

	public static List<String> getNameList(Collection<? extends Named> beans){
		
		List<String> names = new ArrayList<String>(beans.size());
		
		for(Named bean : beans){
			
			names.add(bean.getName());
		}
		
		return names;
	}
}
