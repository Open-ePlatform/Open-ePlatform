package com.nordicpeak.flowengine.queries.singlepolygonmapquery;

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
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQueryProviderModule;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class SinglePolygonMapQueryProvider extends BaseMapQueryProviderModule<SinglePolygonMapQuery, SinglePolygonMapQueryInstance> {

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Require polygon config", description = "Controls whether to require polygon config or not")
	protected boolean requirePolygonConfig = true;
	
	private static final String CONFIGURE_QUERY_ALIAS = "config";

	protected SinglePolygonMapQueryCRUD queryCRUD;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, SinglePolygonMapQueryProvider.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		super.createDAOs(dataSource);

		queryCRUD = new SinglePolygonMapQueryCRUD(queryDAO.getWrapper(Integer.class), this);
	}

	@Override
	public void populate(SinglePolygonMapQueryInstance queryInstance, HttpServletRequest req, User user, boolean allowPartialPopulation) throws ValidationException {

		super.populate(queryInstance, req, user, allowPartialPopulation);

		Integer queryID = queryInstance.getQuery().getQueryID();

		if (queryInstance.getPropertyUnitDesignation() != null) {

			String polygonStr = req.getParameter("q" + queryID + "_polygon");
			String polygonConfig = req.getParameter("q" + queryID + "_polygonConfig");
			
			if(StringUtils.isEmpty(polygonStr) || (requirePolygonConfig && StringUtils.isEmpty(polygonConfig))) {
				
				throw new ValidationException(new ValidationError("InCompleteMapQuerySubmit"));
			
			}
			
			GeometryFactory geometryFactory = new GeometryFactory();
			
			WKTReader reader = new WKTReader(geometryFactory);
			
			Geometry geometry = null;
			
			Polygon polygon = null;
			
			try {

				geometry = reader.read(polygonStr);
				
				polygon = (Polygon) geometry;

			} catch (Exception e) {

				throw new ValidationException(new ValidationError("PolygonNotValid"));

			}
			
			if (enablePUDValidation) {

				Point centroid = polygon.getCentroid();
				
				IEstate estate = getPropertyUnitDesignation(centroid.getX(), centroid.getY(), req, user);

				if (estate == null || !estate.getEstateName().equals(queryInstance.getPropertyUnitDesignation())) {
					
					throw new ValidationException(new ValidationError("CentroidNotMatchingPUD"));
				
				}

			}

			queryInstance.setPrintableGeometry(geometry);
			queryInstance.setPolygon(polygonStr);
			queryInstance.setPolygonConfig(polygonConfig);

			queryInstance.getQueryInstanceDescriptor().setPopulated(true);
			
			generateMapImages(queryInstance, user);

		}

	}

	@Override
	protected Class<SinglePolygonMapQuery> getMapQueryClass() {

		return SinglePolygonMapQuery.class;
	}

	@Override
	protected Class<SinglePolygonMapQueryInstance> getMapQueryInstanceClass() {

		return SinglePolygonMapQueryInstance.class;
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
	public Document createDocument(HttpServletRequest req, User user) {
		
		Document doc = super.createDocument(req, user);
		
		if(requirePolygonConfig) {
			XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "requirePolygonConfig", true);
		}
		
		return doc;
	}

}
