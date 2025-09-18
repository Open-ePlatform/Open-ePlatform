package com.nordicpeak.flowengine.queries.pudmapquery;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQueryInstance;
import com.vividsolutions.jts.geom.Geometry;

@Table(name = "pud_map_query_instances")
@XMLElement
public class PUDMapQueryInstance extends BaseMapQueryInstance<PUDMapQuery> {

	private static final long serialVersionUID = -6796307158661195070L;

	@DAOManaged
	@XMLElement
	private Double xCoordinate;

	@DAOManaged
	@XMLElement
	private Double yCoordinate;
	
	@DAOManaged
	@XMLElement
	private String propertyObjectIdentity;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private PUDMapQuery query;

	private Geometry geometry;

	public Double getXCoordinate() {
		return xCoordinate;
	}

	public void setXCoordinate(Double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public Double getYCoordinate() {
		return yCoordinate;
	}

	public void setYCoordinate(Double yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	@Override
	public void setQuery(PUDMapQuery query) {

		this.query = query;
	}

	@Override
	public PUDMapQuery getQuery() {

		return this.query;
	}

	public void setPrintableGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getPropertyObjectIdentity() {
		return propertyObjectIdentity;
	}

	public void setPropertyObjectIdentity(String propertyObjectIdentity) {
		this.propertyObjectIdentity = propertyObjectIdentity;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		this.xCoordinate = null;
		this.yCoordinate = null;
		
		if (query.isSetAsAttribute()) {
			
			resetAttribute(attributeHandler);
		}

		super.reset(attributeHandler);
	}
	
	public void resetAttribute(MutableAttributeHandler attributeHandler){
		
		attributeHandler.removeAttribute(query.getAttributeName() + ".propertyObjectIdentity");
		attributeHandler.removeAttribute(query.getAttributeName() + ".propertyUnitDesignation");
	}
	
	public void setAttribute(MutableAttributeHandler attributeHandler) {

		attributeHandler.setAttribute(query.getAttributeName() + ".propertyObjectIdentity", propertyObjectIdentity);
		
		if (!StringUtils.isEmpty(getPropertyUnitDesignation())) {
			
			attributeHandler.setAttribute(query.getAttributeName() + ".propertyUnitDesignation", getPropertyUnitDesignation());
			
			String unitDesignation = getPropertyUnitDesignation();
			
			int firstSpace = unitDesignation.indexOf(" ");
			
			if (firstSpace != -1) {
				unitDesignation = unitDesignation.substring(firstSpace + 1);
			}
			
			attributeHandler.setAttribute(query.getAttributeName() + ".propertyUnitDesignationNoMunicipality", unitDesignation);
		}
	}

	@Override
	public List<Geometry> getPrintableGeometries() {

		return Arrays.asList(geometry);
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		XMLUtils.appendNewElement(doc, element, "XCoordinate", xCoordinate);
		XMLUtils.appendNewElement(doc, element, "YCoordinate", yCoordinate);
		
		XMLUtils.appendNewElement(doc, element, "PropertyObjectIdentity", propertyObjectIdentity);
		XMLUtils.appendNewElement(doc, element, "PropertyUnitDesignation", getPropertyUnitDesignation());

		return element;
	}
}
