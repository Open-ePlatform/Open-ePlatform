package com.nordicpeak.flowengine.queries.stopquery;

import java.lang.reflect.Field;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;

@Table(name = "stop_query_instances")
@XMLElement
public class StopQueryInstance extends BaseQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(StopQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private StopQuery query;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + " (queryInstanceID=" + queryInstanceID + ")";
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		return element;
	}

	
	@Override
	public StopQuery getQuery() {
	
		return query;
	}

	
	public void setQuery(StopQuery query) {
	
		this.query = query;
	}

}
