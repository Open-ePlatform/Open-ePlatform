package com.nordicpeak.flowengine.queries.multigeometrymapquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQueryInstance;

@Table(name = "multi_geometry_map_query_instances")
@XMLElement
public class MultiGeometryMapQueryInstance extends BaseMapQueryInstance<MultiGeometryMapQuery> {

	private static final long serialVersionUID = -6796307158661195070L;

	public static final Field GEOMETRIES_RELATION = ReflectionUtils.getField(MultiGeometryMapQueryInstance.class, "geometries");
	
	@DAOManaged
	@XMLElement
	private String propertyUnitGeometry;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<Geometry> geometries;
	
	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private MultiGeometryMapQuery query;

	private com.vividsolutions.jts.geom.Geometry printableGeometry;
	
	public String getPropertyUnitGeometry() {
		return propertyUnitGeometry;
	}

	public void setPropertyUnitGeometry(String propertyUnitGeometry) {
		this.propertyUnitGeometry = propertyUnitGeometry;
	}

	public List<Geometry> getGeometries() {
		return geometries;
	}

	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}
	
	public com.vividsolutions.jts.geom.Geometry getPrintablePUDGeometry() {
		
		return printableGeometry;
	}
	
	public void setPrintablePUDGeometry(com.vividsolutions.jts.geom.Geometry geometry) {
	
		this.printableGeometry = geometry;
	}

	@Override
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
	public void setQuery(MultiGeometryMapQuery query) {
		
		this.query = query;
	}

	@Override
	public MultiGeometryMapQuery getQuery() {
		
		return this.query;
	}
	
	@Override
	public void reset() {

		this.propertyUnitGeometry = null;
		this.geometries = null;

		super.reset();
	}

}
