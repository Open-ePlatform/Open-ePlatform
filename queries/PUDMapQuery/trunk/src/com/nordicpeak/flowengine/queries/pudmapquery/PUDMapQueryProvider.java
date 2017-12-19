package com.nordicpeak.flowengine.queries.pudmapquery;

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
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQueryProviderModule;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

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

		if(queryInstance.getPropertyUnitDesignation() != null) {
			
			String xCoordinate = req.getParameter("q" + queryID + "_xCoordinate");
			String yCoordinate = req.getParameter("q" + queryID + "_yCoordinate");
			
			if(!NumberUtils.isDouble(xCoordinate) || !NumberUtils.isDouble(yCoordinate)) {
				
				throw new ValidationException(new ValidationError("InCompleteMapQuerySubmit"));
				
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
			
			queryInstance.getQueryInstanceDescriptor().setPopulated(true);
			
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
