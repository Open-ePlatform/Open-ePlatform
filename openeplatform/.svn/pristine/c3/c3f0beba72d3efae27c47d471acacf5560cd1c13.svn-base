package com.nordicpeak.flowengine.queries.basemapquery;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.xml.XMLElement;

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
	private Blob secondMapImage;

	@DAOManaged
	private Blob thirdMapImage;

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

	public abstract MapQueryType getQuery();

	public abstract List<Geometry> getPrintableGeometries();

	@Override
	public void reset() {

		this.propertyUnitDesignation = null;
		this.setPropertyUnitNumber(null);
		this.extent = null;
		this.epsg = null;
		this.visibleBaseLayer = null;

		super.reset();
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
	public QueryRequestProcessor getQueryRequestProcessor(HttpServletRequest req, User user, QueryHandler queryHandler) throws Exception {

		return BaseQueryUtils.getGenericQueryInstanceProvider(this.getClass(), queryHandler, queryInstanceDescriptor.getQueryDescriptor().getQueryTypeID()).getQueryRequestProcessor(this, req, user);
	}

}
