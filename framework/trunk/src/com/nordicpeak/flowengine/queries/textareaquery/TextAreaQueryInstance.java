package com.nordicpeak.flowengine.queries.textareaquery;

import java.lang.reflect.Field;
import java.util.Arrays;
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
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.SearchableQueryInstance;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;

@Table(name = "text_area_query_instances")
@XMLElement
public class TextAreaQueryInstance extends BaseQueryInstance implements StringValueQueryInstance, SearchableQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(TextAreaQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private TextAreaQuery query;

	@DAOManaged
	@XMLElement
	private String value;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public TextAreaQuery getQuery() {

		return query;
	}

	public void setQuery(TextAreaQuery query) {

		this.query = query;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		this.value = null;
		
		if (query.isSetAsAttribute()) {
			
			resetAttribute(attributeHandler);
		}
		
		super.reset(attributeHandler);
	}

	public void resetAttribute(MutableAttributeHandler attributeHandler){
		
		attributeHandler.removeAttribute(query.getAttributeName());
	}
	
	public void setAttribute(MutableAttributeHandler attributeHandler) {

		attributeHandler.setAttribute(query.getAttributeName(), value);
	}
	
	public void copyQueryValues() {

	}

	@Override
	public String toString() {

		return "TextAreaQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}

	public String getValue() {

		return value;
	}

	public void setValue(String value) {

		this.value = value;
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		XMLUtils.appendNewCDATAElement(doc, element, "Value", value);

		return element;
	}

	@Override
	public String getStringValue() {

		return value;
	}
	
	@Override
	public List<String> getSearchableValues() {
		
		if(!StringUtils.isEmpty(value) && query.isSearchable()) {
			return Arrays.asList(value);
		}
		
		return null;
	}
}
