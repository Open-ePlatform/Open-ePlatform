package com.nordicpeak.flowengine.queries.basemapquery;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.base64.Base64;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryUtils;
import com.vividsolutions.jts.geom.Geometry;

public abstract class BaseMapQueryInstance<MapQueryType extends BaseMapQuery> extends BaseQueryInstance {

	private static final long serialVersionUID = -243016174337892278L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged
	@XMLElement
	private String propertyUnitDesignation;

	@DAOManaged
	@XMLElement
	private Integer propertyUnitNumber;

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
	private Blob firstMapImage;

	@DAOManaged
	private Integer firstMapImageDpi;

	@DAOManaged
	private Integer firstMapImageScale;

	@DAOManaged
	private String firstMapImageLayout;

	@DAOManaged
	private String firstMapImageFormat;

	@DAOManaged
	private Blob secondMapImage;

	@DAOManaged
	private Integer secondMapImageDpi;

	@DAOManaged
	private Integer secondMapImageScale;

	@DAOManaged
	private String secondMapImageLayout;

	@DAOManaged
	private String secondMapImageFormat;

	@DAOManaged
	private Blob thirdMapImage;

	@DAOManaged
	private Integer thirdMapImageDpi;

	@DAOManaged
	private Integer thirdMapImageScale;

	@DAOManaged
	private String thirdMapImageLayout;

	@DAOManaged
	private String thirdMapImageFormat;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	public String getPropertyUnitDesignation() {

		return propertyUnitDesignation;
	}

	public void setPropertyUnitDesignation(String propertyUnitDesignation) {

		this.propertyUnitDesignation = propertyUnitDesignation;
	}

	public Integer getPropertyUnitNumber() {

		return propertyUnitNumber;
	}

	public void setPropertyUnitNumber(Integer propertyUnitNumber) {

		this.propertyUnitNumber = propertyUnitNumber;
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

	public Blob getFirstMapImage() {

		return firstMapImage;
	}

	public void setFirstMapImage(Blob firstMapImage) {

		this.firstMapImage = firstMapImage;
	}

	public Blob getSecondMapImage() {

		return secondMapImage;
	}

	public void setSecondMapImage(Blob secondMapImage) {

		this.secondMapImage = secondMapImage;
	}

	public Blob getThirdMapImage() {

		return thirdMapImage;
	}

	public void setThirdMapImage(Blob thirdMapImage) {

		this.thirdMapImage = thirdMapImage;
	}

	public String getVisibleBaseLayer() {

		return visibleBaseLayer;
	}

	public void setVisibleBaseLayer(String visibleBaseLayer) {

		this.visibleBaseLayer = visibleBaseLayer;
	}

	public void copyQueryValues() {

	}

	public abstract void setQuery(MapQueryType query);

	@Override
	public abstract MapQueryType getQuery();

	public abstract List<Geometry> getPrintableGeometries();

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		this.propertyUnitDesignation = null;
		this.setPropertyUnitNumber(null);
		this.extent = null;
		this.epsg = null;
		this.visibleBaseLayer = null;

		super.reset(attributeHandler);
	}

	@Override
	public String toString() {

		return "BaseMapQueryInstance [queryInstanceID=" + queryInstanceID + ", propertyUnitNumber=" + propertyUnitNumber + "]";
	}

	protected final Object writeReplace() {

		try {

			if (firstMapImage != null) {
				firstMapImage = new SerialBlob(firstMapImage);
			}

			if (secondMapImage != null) {
				secondMapImage = new SerialBlob(secondMapImage);
			}

			if (thirdMapImage != null) {
				thirdMapImage = new SerialBlob(thirdMapImage);
			}

		} catch (SerialException e) {

		} catch (SQLException e) {

		}

		return this;

	}

	@Override
	public QueryRequestProcessor getQueryRequestProcessor(HttpServletRequest req, User user, User poster, URIParser uriParser, QueryHandler queryHandler) throws Exception {

		return BaseQueryUtils.getGenericQueryInstanceProvider(this.getClass(), queryHandler, queryInstanceDescriptor.getQueryDescriptor().getQueryTypeID()).getQueryRequestProcessor(this, req, user, poster, uriParser);
	}


	public Integer getFirstMapImageDpi() {

		return firstMapImageDpi;
	}


	public void setFirstMapImageDpi(Integer firstMapImageDpi) {

		this.firstMapImageDpi = firstMapImageDpi;
	}


	public Integer getFirstMapImageScale() {

		return firstMapImageScale;
	}


	public void setFirstMapImageScale(Integer firstMapImageScale) {

		this.firstMapImageScale = firstMapImageScale;
	}


	public String getFirstMapImageLayout() {

		return firstMapImageLayout;
	}


	public void setFirstMapImageLayout(String firstMapImageLayout) {

		this.firstMapImageLayout = firstMapImageLayout;
	}


	public String getFirstMapImageFormat() {

		return firstMapImageFormat;
	}


	public void setFirstMapImageFormat(String firstMapImageFormat) {

		this.firstMapImageFormat = firstMapImageFormat;
	}


	public Integer getSecondMapImageDpi() {

		return secondMapImageDpi;
	}


	public void setSecondMapImageDpi(Integer secondMapImageDpi) {

		this.secondMapImageDpi = secondMapImageDpi;
	}


	public Integer getSecondMapImageScale() {

		return secondMapImageScale;
	}


	public void setSecondMapImageScale(Integer secondMapImageScale) {

		this.secondMapImageScale = secondMapImageScale;
	}


	public String getSecondMapImageLayout() {

		return secondMapImageLayout;
	}


	public void setSecondMapImageLayout(String secondMapImageLayout) {

		this.secondMapImageLayout = secondMapImageLayout;
	}


	public String getSecondMapImageFormat() {

		return secondMapImageFormat;
	}


	public void setSecondMapImageFormat(String secondMapImageFormat) {

		this.secondMapImageFormat = secondMapImageFormat;
	}


	public Integer getThirdMapImageDpi() {

		return thirdMapImageDpi;
	}


	public void setThirdMapImageDpi(Integer thirdMapImageDpi) {

		this.thirdMapImageDpi = thirdMapImageDpi;
	}


	public Integer getThirdMapImageScale() {

		return thirdMapImageScale;
	}


	public void setThirdMapImageScale(Integer thirdMapImageScale) {

		this.thirdMapImageScale = thirdMapImageScale;
	}


	public String getThirdMapImageLayout() {

		return thirdMapImageLayout;
	}


	public void setThirdMapImageLayout(String thirdMapImageLayout) {

		this.thirdMapImageLayout = thirdMapImageLayout;
	}


	public String getThirdMapImageFormat() {

		return thirdMapImageFormat;
	}


	public void setThirdMapImageFormat(String thirdMapImageFormat) {

		this.thirdMapImageFormat = thirdMapImageFormat;
	}

	@Override
	public Element getBaseExportXML(Document doc) throws Exception {

		Element element = super.getBaseExportXML(doc);

		XMLUtils.appendNewElement(doc, element, "PropertyUnitDesignation", propertyUnitDesignation);
		XMLUtils.appendNewElement(doc, element, "PropertyUnitNumber", propertyUnitNumber);
		XMLUtils.appendNewElement(doc, element, "Extent", extent);
		XMLUtils.appendNewElement(doc, element, "EPSG", epsg);

		if(firstMapImage != null){

			XMLUtils.appendNewElement(doc, element, "FirstMapImage", Base64.encodeBytes(firstMapImage.getBytes(1, (int)firstMapImage.length())));
			XMLUtils.appendNewElement(doc, element, "FirstMapImageDpi", firstMapImageDpi);
			XMLUtils.appendNewElement(doc, element, "FirstMapImageScale", firstMapImageScale);
			XMLUtils.appendNewElement(doc, element, "FirstMapImageLayout", firstMapImageLayout);
			XMLUtils.appendNewElement(doc, element, "FirstMapImageFormat", firstMapImageFormat);
		}

		if(secondMapImage != null){

			XMLUtils.appendNewElement(doc, element, "SecondMapImage", Base64.encodeBytes(secondMapImage.getBytes(1, (int)secondMapImage.length())));
			XMLUtils.appendNewElement(doc, element, "SecondMapImageDpi", secondMapImageDpi);
			XMLUtils.appendNewElement(doc, element, "SecondMapImageScale", secondMapImageScale);
			XMLUtils.appendNewElement(doc, element, "SecondMapImageLayout", secondMapImageLayout);
			XMLUtils.appendNewElement(doc, element, "SecondMapImageFormat", secondMapImageFormat);
		}

		if(thirdMapImage != null){

			XMLUtils.appendNewElement(doc, element, "ThirdMapImage", Base64.encodeBytes(thirdMapImage.getBytes(1, (int)thirdMapImage.length())));
			XMLUtils.appendNewElement(doc, element, "ThirdMapImageDpi", thirdMapImageDpi);
			XMLUtils.appendNewElement(doc, element, "ThirdMapImageScale", thirdMapImageScale);
			XMLUtils.appendNewElement(doc, element, "ThirdMapImageLayout", thirdMapImageLayout);
			XMLUtils.appendNewElement(doc, element, "ThirdMapImageFormat", thirdMapImageFormat);
		}

		return element;
	}

}
