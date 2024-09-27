package com.nordicpeak.flowengine.queries.multigeometrymapquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;

import se.riges.lm.rmi.interfaces.IEstate;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQueryProviderModule;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

public class MultiGeometryMapQueryProvider extends BaseMapQueryProviderModule<MultiGeometryMapQuery, MultiGeometryMapQueryInstance> {

	private static final RelationQuery SAVE_QUERY_INSTANCE_RELATION_QUERY = new RelationQuery(MultiGeometryMapQueryInstance.GEOMETRIES_RELATION);
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Require geometry configs", description = "Controls whether to require geometry configs or not")
	protected boolean requireGeometryConfigs = true;
	
	private static final String CONFIGURE_QUERY_ALIAS = "config";

	protected MultiGeometryMapQueryCRUD queryCRUD;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, MultiGeometryMapQueryProvider.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		super.createDAOs(dataSource);

		queryCRUD = new MultiGeometryMapQueryCRUD(queryDAO.getWrapper(Integer.class), this);
	}

	@Override
	public void populate(MultiGeometryMapQueryInstance queryInstance, HttpServletRequest req, User user, boolean allowPartialPopulation) throws ValidationException {

		Integer queryID = queryInstance.getQuery().getQueryID();

		String propertyUnitDesignation = req.getParameter("q" + queryID + "_propertyUnitDesignation");
		String extent = req.getParameter("q" + queryID + "_extent");
		String epsg = req.getParameter("q" + queryID + "_epsg");
		String baseLayer = req.getParameter("q" + queryID + "_baseLayer");
		String [] addedGeometries = req.getParameterValues("q" + queryID + "_geometry");
		String propertyUnitGeometry = req.getParameter("q" + queryID + "_propertyUnitGeometry");

		GeometryFactory geometryFactory = new GeometryFactory();
		
		WKTReader reader = new WKTReader(geometryFactory);
		
		List<Geometry> geometries = null;
		
		if(!ArrayUtils.isEmpty(addedGeometries)) {
			
			geometries = populateGeometries(addedGeometries, reader);
		
		}
		
		if(!StringUtils.isEmpty(propertyUnitGeometry)) {
			com.vividsolutions.jts.geom.Geometry geometry = populateGeometry(reader, propertyUnitGeometry);
			queryInstance.setPrintablePUDGeometry(geometry);
		}
		
		queryInstance.setPropertyUnitGeometry(propertyUnitGeometry);
		queryInstance.setPropertyUnitDesignation(propertyUnitDesignation);
		queryInstance.setGeometries(geometries);
		queryInstance.setExtent(extent);
		queryInstance.setEpsg(epsg);
		queryInstance.setVisibleBaseLayer(baseLayer);
		
		if((StringUtils.isEmpty(propertyUnitDesignation) || CollectionUtils.isEmpty(geometries)) && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED) {

			List<ValidationError> errors = new ArrayList<ValidationError>(2);
			
			if(StringUtils.isEmpty(queryInstance.getPropertyUnitDesignation())) {
				errors.add(new ValidationError("PUDRequired"));
			}
			
			if(CollectionUtils.isEmpty(geometries)) {
				errors.add(new ValidationError("GeometryRequired"));
			}
				
			throw new ValidationException(errors);
			
		} else if(!StringUtils.isEmpty(propertyUnitDesignation) || !CollectionUtils.isEmpty(geometries)) {
			
			ValidationError error = null;
			
			if(StringUtils.isEmpty(queryInstance.getPropertyUnitDesignation())) {
				error = new ValidationError("PUDRequired");
			} else if(CollectionUtils.isEmpty(geometries)) {
				error = new ValidationError("GeometryRequired");
			}
			
			if(error != null) {
				throw new ValidationException(error);
			}
			
		} else {
			
			queryInstance.reset();
			
			return;
		}
		
		if(StringUtils.isEmpty(queryInstance.getExtent()) || StringUtils.isEmpty(queryInstance.getEpsg()) || StringUtils.isEmpty(queryInstance.getPropertyUnitGeometry())) {
			
			throw new ValidationException(new ValidationError("InCompleteMapQuerySubmit"));
			
		}
		
		if(enablePUDValidation) {
		
			IEstate estate = getPropertyUnitDesignation(propertyUnitDesignation, req, user);
		
			if(estate == null) {
				throw new ValidationException(new ValidationError("PUDNotValid"));
			}
		
			queryInstance.setPropertyUnitNumber(estate.getEstateID());
			
		}
		
		queryInstance.getQueryInstanceDescriptor().setPopulated(true);
		
		generateMapImages(queryInstance, user);
		
	}
	
	private List<Geometry> populateGeometries(String[] geometries, WKTReader reader) throws ValidationException {
		
		List<Geometry> populatedGeometries = new ArrayList<Geometry>(geometries.length);
		
		for(String object : geometries) {
			
			String [] objectParts = object.split("#");
			
			String geometryStr = objectParts[0].trim();
			
			com.vividsolutions.jts.geom.Geometry geometry = populateGeometry(reader, geometryStr);
			
			String config = null;
			
			if(objectParts.length > 1) {
				
				config = objectParts[1].trim();
				
			}
			
			populatedGeometries.add(new Geometry(geometryStr, config, geometry));
			
		}
		
		return populatedGeometries;
		
	}
	
	private com.vividsolutions.jts.geom.Geometry populateGeometry(WKTReader reader, String geometry) throws ValidationException {
		
		try {

			return reader.read(geometry);

		} catch (Exception e) {
			
			throw new ValidationException(new ValidationError("GeometryNotValid"));

		}
		
	}

	@Override
	protected Class<MultiGeometryMapQuery> getMapQueryClass() {

		return MultiGeometryMapQuery.class;
	}

	@Override
	protected Class<MultiGeometryMapQueryInstance> getMapQueryInstanceClass() {

		return MultiGeometryMapQueryInstance.class;
	}

	@WebPublic(alias = CONFIGURE_QUERY_ALIAS)
	@Override
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	protected String configureQueryAlias() {

		return CONFIGURE_QUERY_ALIAS;
	}

	@Override
	protected List<Field> getMapQueryInstanceExcludedGetRelations() {
		
		return Arrays.asList(Geometry.QUERY_INSTANCE_RELATION);
	}
	
	@Override
	protected void removeUnnecessaryRelations(MultiGeometryMapQueryInstance queryInstance) {
		
		List<Geometry> geometries = queryInstance.getGeometries();
		
		if(geometries != null) {
		
			for(Geometry geometry : geometries) {
				
				geometry.setQueryInstance(null);
				
			}
		
		}
		
	}

	@Override
	protected List<Field> getMapQueryInstanceGetRelations() {

		return Arrays.asList(MultiGeometryMapQueryInstance.GEOMETRIES_RELATION);
	}
	
	@Override
	protected RelationQuery getSaveMapQueryInstanceRelationQuery() {
		
		return SAVE_QUERY_INSTANCE_RELATION_QUERY;
	}
	
	@Override
	public Document createDocument(HttpServletRequest req, User user) {
		
		Document doc = super.createDocument(req, user);
		
		if(requireGeometryConfigs) {
			XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "requireGeometryConfigs", true);
		}
		
		return doc;
	}

}
