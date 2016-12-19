package com.nordicpeak.flowengine.queries.generalmapquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryUtils;

@Table(name = "general_map_query_instances")
@XMLElement
public class GeneralMapQueryInstance extends BaseQueryInstance {

	private static final long serialVersionUID = 5366400543122031446L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(GeneralMapQueryInstance.class, "query");
	public static final Field MAPPRINTS_RELATION = ReflectionUtils.getField(GeneralMapQueryInstance.class, "mapPrints");
	public static final Field GEOMETRIES_RELATION = ReflectionUtils.getField(GeneralMapQueryInstance.class, "geometries");
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private GeneralMapQuery query;

	@DAOManaged
	@XMLElement
	private String extent;

	@DAOManaged
	@XMLElement
	private String epsg;

	@DAOManaged
	@XMLElement
	private String visibleBaseLayer;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<Geometry> geometries;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<GeneralMapQueryInstancePrint> mapPrints;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	public GeneralMapQuery getQuery() {

		return query;
	}

	public String getExtent() {

		return extent;
	}

	public void setExtent(String extent) {

		this.extent = extent;
	}

	public String getEpsg() {

		return epsg;
	}

	public void setEpsg(String epsg) {

		this.epsg = epsg;
	}

	public String getVisibleBaseLayer() {

		return visibleBaseLayer;
	}

	public void setVisibleBaseLayer(String visibleBaseLayer) {

		this.visibleBaseLayer = visibleBaseLayer;
	}

	public List<Geometry> getGeometries() {

		return geometries;
	}

	public void setGeometries(List<Geometry> geometries) {

		this.geometries = geometries;
	}

	public List<GeneralMapQueryInstancePrint> getMapPrints() {

		return mapPrints;
	}

	public void setMapPrints(List<GeneralMapQueryInstancePrint> mapPrints) {

		this.mapPrints = mapPrints;
	}

	public void setQuery(GeneralMapQuery query) {

		this.query = query;
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		return null;
	}

	public void copyQueryValues() {

	}

	public List<com.vividsolutions.jts.geom.Geometry> getPrintableGeometries() {

		List<com.vividsolutions.jts.geom.Geometry> printableGeometries = new ArrayList<com.vividsolutions.jts.geom.Geometry>();

		if(geometries != null) {

			for(Geometry geometry : geometries) {

				if(geometry.getPrintableGeometry() != null) {

					printableGeometries.add(geometry.getPrintableGeometry());

				}

			}
		}

		return printableGeometries;

	}
	
	@Override
	public QueryRequestProcessor getQueryRequestProcessor(HttpServletRequest req, User user, QueryHandler queryHandler) throws Exception {

		return BaseQueryUtils.getGenericQueryInstanceProvider(this.getClass(), queryHandler, queryInstanceDescriptor.getQueryDescriptor().getQueryTypeID()).getQueryRequestProcessor(this, req, user);
	}
	
}
