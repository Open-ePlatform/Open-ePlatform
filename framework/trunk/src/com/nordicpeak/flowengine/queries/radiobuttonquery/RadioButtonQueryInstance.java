package com.nordicpeak.flowengine.queries.radiobuttonquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.BaseInvoiceLine;
import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.PaymentQueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativeQueryUtils;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;


@Table(name = "radio_button_query_instances")
@XMLElement
public class RadioButtonQueryInstance extends BaseQueryInstance implements FixedAlternativesQueryInstance, StringValueQueryInstance, PaymentQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field ALTERNATIVE_RELATION = ReflectionUtils.getField(RadioButtonQueryInstance.class, "alternative");
	public static final Field QUERY_RELATION = ReflectionUtils.getField(RadioButtonQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName="queryID")
	@ManyToOne
	@XMLElement
	private RadioButtonQuery query;

	@DAOManaged(columnName="alternativeID")
	@ManyToOne
	@XMLElement
	private RadioButtonAlternative alternative;

	@DAOManaged
	@XMLElement
	private String freeTextAlternativeValue;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}


	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}


	@Override
	public RadioButtonQuery getQuery() {

		return query;
	}


	public void setQuery(RadioButtonQuery query) {

		this.query = query;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {
		
		this.alternative = null;
		freeTextAlternativeValue = null;
		
		if (query.isSetAsAttribute()) {
			
			resetAttribute(attributeHandler);
		}
		
		super.reset(attributeHandler);
	}
	
	public void setAttribute(MutableAttributeHandler attributeHandler) {
		
		if (!StringUtils.isEmpty(freeTextAlternativeValue)) {
			
			attributeHandler.setAttribute(query.getAttributeName(), freeTextAlternativeValue);
			
		} else if (alternative != null) {
			
			if (!StringUtils.isEmpty(alternative.getAttributeValue())) {
				
				attributeHandler.setAttribute(query.getAttributeName(), alternative.getAttributeValue());
				
			} else {
				
				attributeHandler.setAttribute(query.getAttributeName(), alternative.getName());
			}
		}
	}
	
	public void resetAttribute(MutableAttributeHandler attributeHandler){
		
		attributeHandler.removeAttribute(query.getAttributeName());
	}

	public void copyQueryValues() {}

	@Override
	public String toString() {

		return "RadioButtonQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}

	public RadioButtonAlternative getAlternative() {

		return alternative;
	}


	public void setAlternative(RadioButtonAlternative alternative) {

		this.alternative = alternative;
	}

	@Override
	public String getFreeTextAlternativeValue() {

		return freeTextAlternativeValue;
	}

	public void setFreeTextAlternativeValue(String freeTextAlternativeValue) {

		this.freeTextAlternativeValue = freeTextAlternativeValue;
	}

	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {

		if (alternative == null) {
			
			return null;
		}

		return Collections.singletonList(alternative);
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		FixedAlternativeQueryUtils.appendExportXMLAlternatives(doc, element, this);

		return element;
	}
	
	@Override
	public String getStringValue() {

		if (alternative != null) {
			
			return alternative.getName();
			
		} else if (freeTextAlternativeValue != null) {
			
			return freeTextAlternativeValue;
		}

		return null;
	}
	
	@Override
	public List<BaseInvoiceLine> getInvoiceLines() {
		
		if (alternative != null && alternative.getPrice() != null && alternative.getPrice() > 0) {
			
			List<BaseInvoiceLine> invoiceLines = new ArrayList<BaseInvoiceLine>(1);
			invoiceLines.add(new BaseInvoiceLine(1, alternative.getPrice(), alternative.getName(), ""));
			
			return invoiceLines;
		}
		
		return null;
	}
}
