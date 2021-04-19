package com.nordicpeak.flowengine.interfaces;

import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

public interface QueryContentFilter {

	/**
	 * Filter the description and help text fields on the query
	 * 
	 * @param query
	 */
	public void filterHTML(BaseQuery query);
	
	/**
	 * Filter any HTML
	 * 
	 * @param html
	 * @return
	 */
	public String filterHTML(String html);
}
