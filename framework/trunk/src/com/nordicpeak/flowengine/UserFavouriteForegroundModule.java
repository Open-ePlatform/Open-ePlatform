package com.nordicpeak.flowengine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.UserFavourite;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.dao.UserFavouriteDAO;

public class UserFavouriteForegroundModule extends AnnotatedForegroundModule {

	protected AnnotatedDAO<FlowFamily> flowFamilyDAO;

	protected UserFavouriteDAO userFavouriteDAO;

	protected QueryParameterFactory<FlowFamily, Integer> flowFamilyIDParamFactory;

	@InstanceManagerDependency(required=true)
	protected FlowBrowserModule flowBrowserModule;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		FlowEngineDAOFactory daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		flowFamilyDAO = daoFactory.getFlowFamilyDAO();

		userFavouriteDAO = daoFactory.getUserFavouriteDAO();

		flowFamilyIDParamFactory = flowFamilyDAO.getParamFactory("flowFamilyID", Integer.class);

	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return new SimpleForegroundModuleResponse("<div class=\"contentitem\">This module does not have a frontend.</div>", this.getDefaultBreadcrumb());
	}

	@WebPublic(alias = "addfavourite")
	public ForegroundModuleResponse addFavourite(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws NumberFormatException, SQLException, IOException {

		FlowFamily flowFamily;

		JsonObject jsonObject = new JsonObject(1);

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowFamily = getFlowFamily(Integer.valueOf(uriParser.get(2)))) != null) {

			UserFavourite favourite = userFavouriteDAO.get(user, flowFamily);

			if (favourite == null) {

				log.info("User " + user + " adding flow family " + flowFamily + " as favourite");

				favourite = new UserFavourite();
				favourite.setFlowFamily(flowFamily);
				favourite.setUser(user);

				userFavouriteDAO.add(favourite, req.getSession());

				jsonObject.putField("AddSuccess", "true");

			}

			appendUserFavourites(jsonObject, req, user);

		} else {

			jsonObject.putField("FlowFamilyNotFound", "true");

		}

		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

		return null;

	}

	@WebPublic(alias = "deletefavourite")
	public ForegroundModuleResponse deleteFavourite(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws NumberFormatException, SQLException, IOException {

		FlowFamily flowFamily;

		JsonObject jsonObject = new JsonObject(1);

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (flowFamily = getFlowFamily(Integer.valueOf(uriParser.get(2)))) != null) {

			UserFavourite favourite = userFavouriteDAO.get(user, flowFamily);

			if (favourite != null) {

				log.info("User " + user + " deleting flow family " + flowFamily + " from its favourites");

				userFavouriteDAO.delete(favourite, req.getSession());

				jsonObject.putField("DeleteSuccess", "true");

			}

			appendUserFavourites(jsonObject, req, user);

		} else {

			jsonObject.putField("FlowFamilyNotFound", "true");

		}

		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

		return null;

	}

	protected void appendUserFavourites(JsonObject jsonObject, HttpServletRequest req, User user) throws SQLException {

		List<UserFavourite> userFavorites = userFavouriteDAO.getAll(user, req.getSession(), flowBrowserModule.getLatestPublishedFlowVersionMap());

		if (userFavorites != null) {

			JsonArray array = new JsonArray(userFavorites.size());

			for (UserFavourite userFavourite : userFavorites) {

				array.addNode(userFavourite.toJson());

			}

			jsonObject.putField("UserFavourites", array);

		}

	}

	protected FlowFamily getFlowFamily(Integer flowFamilyID) throws SQLException {

		HighLevelQuery<FlowFamily> query = new HighLevelQuery<FlowFamily>();

		query.addParameter(flowFamilyIDParamFactory.getParameter(flowFamilyID));

		return flowFamilyDAO.get(query);

	}

}
