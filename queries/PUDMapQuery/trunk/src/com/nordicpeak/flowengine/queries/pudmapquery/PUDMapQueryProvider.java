package com.nordicpeak.flowengine.queries.pudmapquery;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.HTTPResponse;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.SimpleRequest;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQueryProviderModule;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

import flexjson.JSONDeserializer;

public class PUDMapQueryProvider extends BaseMapQueryProviderModule<PUDMapQuery, PUDMapQueryInstance> {

	private static final String CONFIGURE_QUERY_ALIAS = "config";
	
	protected PUDMapQueryCRUD queryCRUD;
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, PUDMapQueryProvider.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}
		
		super.createDAOs(dataSource);
		
		queryCRUD = new PUDMapQueryCRUD(queryDAO.getWrapper(Integer.class), this);
	}

	@Override
	public void populate(PUDMapQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		super.populate(queryInstance, req, user, poster, allowPartialPopulation, attributeHandler, requestMetadata);

		Integer queryID = queryInstance.getQuery().getQueryID();

		if (queryInstance.getPropertyUnitDesignation() != null) {

			String xCoordinate = req.getParameter("q" + queryID + "_xCoordinate");
			String yCoordinate = req.getParameter("q" + queryID + "_yCoordinate");

			if (!NumberUtils.isDouble(xCoordinate) || !NumberUtils.isDouble(yCoordinate)) {

				throw new ValidationException(new ValidationError("InCompleteMapQuerySubmit"));
			}
			
			String propertyObjectIdentity = null;

			if (!StringUtils.isEmpty(httpSearchCoordinateParam)) {

				try {
					SimpleRequest request = new SimpleRequest(httpSearchServiceURL + "/" + httpSearchCoordinateParam);
					request.addParameter("x", xCoordinate);
					request.addParameter("y", yCoordinate);

					HTTPResponse response = HTTPUtils.sendHTTPGetRequest(request, Charset.forName("ISO-8859-1"));

					JSONDeserializer<HashMap<String, Object>> deserializer = new JSONDeserializer<HashMap<String, Object>>();
					
					Map<String, Object> root = deserializer.deserialize(response.getValue(), HashMap.class);
					
					@SuppressWarnings("unchecked")
					Map<String, String> properties = (Map<String, String>) root.get("properties");
					
					propertyObjectIdentity = properties.get("fnr");
					
					if (propertyObjectIdentity == null || propertyObjectIdentity.length() != 36) {

						log.error("Invalid propertyObjectIdentity " + propertyObjectIdentity + " for " + queryInstance.getPropertyUnitDesignation() + " at coordinates x " + xCoordinate + ", y " + xCoordinate);
						
						throw new ValidationException(new ValidationError("ErrorLookingUpEstateID"));
					}
					
				} catch (IOException e) {
					
					log.error("Error getting propertyObjectIdentity for " + queryInstance.getPropertyUnitDesignation() + " at coordinates x " + xCoordinate + ", y " + xCoordinate, e);
					throw new ValidationException(new ValidationError("ErrorLookingUpEstateID"));
				}
			}

			GeometryFactory geometryFactory = new GeometryFactory();

			WKTReader reader = new WKTReader(geometryFactory);

			Geometry geometry = null;

			try {
				geometry = reader.read("POINT(" + xCoordinate + " " + yCoordinate + ")");

			} catch (Exception e) {

				throw new ValidationException(new ValidationError("InCompleteMapQuerySubmit"));
			}

			Double x = NumberUtils.toDouble(xCoordinate);
			Double y = NumberUtils.toDouble(yCoordinate);

			queryInstance.setXCoordinate(x);
			queryInstance.setYCoordinate(y);
			queryInstance.setPrintableGeometry(geometry);
			queryInstance.setPropertyObjectIdentity(propertyObjectIdentity);

			queryInstance.getQueryInstanceDescriptor().setPopulated(true);

			if (queryInstance.getQuery().isSetAsAttribute()) {

				queryInstance.resetAttribute(attributeHandler);
				queryInstance.setAttribute(attributeHandler);
			}

			generateMapImages(queryInstance, user);
		}
	}

	@Override
	protected Class<PUDMapQuery> getMapQueryClass() {
		
		return PUDMapQuery.class;
	}

	@Override
	protected Class<PUDMapQueryInstance> getMapQueryInstanceClass() {
		
		return PUDMapQueryInstance.class;
	}

	@WebPublic(alias=CONFIGURE_QUERY_ALIAS)
	@Override
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		return queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	protected String configureQueryAlias() {
		
		return CONFIGURE_QUERY_ALIAS;
	}

	@Override
	protected Class<PUDMapQueryInstance> getQueryInstanceClass() {

		return PUDMapQueryInstance.class;
	}

}
