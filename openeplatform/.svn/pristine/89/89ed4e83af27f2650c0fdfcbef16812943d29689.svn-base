package com.nordicpeak.flowengine.queries.fileinfoquery;

import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.interfaces.NonColumnExportableQueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryUtils;

@Table(name = "fileinfo_query_instances")
@XMLElement
public class FileInfoQueryInstance extends BaseQueryInstance implements NonColumnExportableQueryInstance{

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(FileInfoQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private FileInfoQuery query;

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
	public QueryRequestProcessor getQueryRequestProcessor(HttpServletRequest req, User user, User poster, URIParser uriParser, QueryHandler queryHandler, InstanceRequestMetadata requestMetadata) throws Exception {

		return BaseQueryUtils.getGenericQueryInstanceProvider(this.getClass(), queryHandler, queryInstanceDescriptor.getQueryDescriptor().getQueryTypeID()).getQueryRequestProcessor(this, req, user, poster, uriParser, requestMetadata);
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		return element;
	}

	
	@Override
	public FileInfoQuery getQuery() {
	
		return query;
	}

	
	public void setQuery(FileInfoQuery query) {
	
		this.query = query;
	}

}
