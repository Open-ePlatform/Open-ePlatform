package com.nordicpeak.flowengine.listeners;

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.ElementableListener;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.validationerrors.QueryExportValidationError;


public class QueryDescriptorElementableListener implements ElementableListener<QueryDescriptor> {

	protected final Logger log = Logger.getLogger(getClass());
	
	protected final QueryHandler queryHandler;
	protected final List<ValidationError> validationErrors;
	
	public QueryDescriptorElementableListener(QueryHandler queryHandler, List<ValidationError> validationErrors) {

		super();
		this.queryHandler = queryHandler;
		this.validationErrors = validationErrors;
	}

	@Override
	public void elementGenerated(Document doc, Element element, QueryDescriptor queryDescriptor) {

		try {
			Query query = queryHandler.getQuery(queryDescriptor);
			
			element.appendChild(query.toXML(doc));
			
		} catch (Exception e) {

			log.error("Error exporting query " + queryDescriptor, e);
			
			validationErrors.add(new QueryExportValidationError(queryDescriptor));
		}
	}
}
