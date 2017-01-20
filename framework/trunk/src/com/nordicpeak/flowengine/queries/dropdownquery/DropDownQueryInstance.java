package com.nordicpeak.flowengine.queries.dropdownquery;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativeQueryUtils;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;


@Table(name = "drop_down_query_instances")
@XMLElement
public class DropDownQueryInstance extends BaseQueryInstance implements FixedAlternativesQueryInstance, StringValueQueryInstance{

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field ALTERNATIVE_RELATION = ReflectionUtils.getField(DropDownQueryInstance.class, "alternative");
	public static final Field QUERY_RELATION = ReflectionUtils.getField(DropDownQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName="queryID")
	@ManyToOne
	@XMLElement
	private DropDownQuery query;

	@DAOManaged(columnName="alternativeID")
	@ManyToOne
	@XMLElement
	private DropDownAlternative alternative;

	@DAOManaged
	@XMLElement
	private String freeTextAlternativeValue;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}


	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}


	public DropDownQuery getQuery() {

		return query;
	}


	public void setQuery(DropDownQuery query) {

		this.query = query;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		this.alternative = null;
		freeTextAlternativeValue = null;
		
		if(query.isSetAsAttribute()){

			resetAttribute(attributeHandler);
		}
		
		super.reset(attributeHandler);
	}
	
	public void setAttribute(MutableAttributeHandler attributeHandler){
		
		if(!StringUtils.isEmpty(freeTextAlternativeValue)){
			
			attributeHandler.setAttribute(query.getAttributeName(), freeTextAlternativeValue);
			
		} else if(alternative != null){
			
			attributeHandler.setAttribute(query.getAttributeName(), alternative.getName());
		}
	}
	
	public void resetAttribute(MutableAttributeHandler attributeHandler){
		
		attributeHandler.removeAttribute(query.getAttributeName());
	}

	public void copyQueryValues() {}

	@Override
	public String toString() {

		return "DropDownQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}

	public DropDownAlternative getAlternative() {

		return alternative;
	}


	public void setAlternative(DropDownAlternative alternative) {

		this.alternative = alternative;
	}

	/**
	 * @return the freeTextAlternativeValue
	 */
	@Override
	public String getFreeTextAlternativeValue() {
		return freeTextAlternativeValue;
	}


	/**
	 * @param freeTextAlternativeValue the freeTextAlternativeValue to set
	 */
	public void setFreeTextAlternativeValue(String freeTextAlternativeValue) {
		this.freeTextAlternativeValue = freeTextAlternativeValue;
	}


	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {

		if(alternative == null){

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

		if(alternative != null){

			return alternative.getName();

		}else if(freeTextAlternativeValue != null){

			return freeTextAlternativeValue;
		}

		return null;
	}
}
