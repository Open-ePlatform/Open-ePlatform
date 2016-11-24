package com.nordicpeak.flowengine.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.UserFavourite;
import com.nordicpeak.flowengine.comparators.UserFavouriteFlowNameComparator;

public class UserFavouriteDAO extends AnnotatedDAO<UserFavourite> {

	protected static final UserFavouriteFlowNameComparator FLOWNAME_COMPARATOR = new UserFavouriteFlowNameComparator();

	protected QueryParameterFactory<UserFavourite, User> userFavouriteUserParamFactory;

	protected QueryParameterFactory<UserFavourite, FlowFamily> userFavouriteFlowFamilyParamFactory;

	public UserFavouriteDAO(DataSource dataSource, Class<UserFavourite> beanClass, HierarchyAnnotatedDAOFactory daoFactory) {

		super(dataSource, beanClass, daoFactory, daoFactory.getQueryParameterPopulators(), daoFactory.getBeanStringPopulators());

		userFavouriteUserParamFactory = getParamFactory("user", User.class);

		userFavouriteFlowFamilyParamFactory = getParamFactory("flowFamily", FlowFamily.class);

	}

	public void add(UserFavourite bean, HttpSession session) throws SQLException {

		super.add(bean);

		session.removeAttribute("UserFavourites");
	}

	public void delete(UserFavourite bean, HttpSession session) throws SQLException {

		super.delete(bean);

		session.removeAttribute("UserFavourites");
	}

	@SuppressWarnings("unchecked")
	public List<UserFavourite> getAll(User user, HttpSession session, Map<Integer, Flow> latestPublishedFlowMap) throws SQLException {

		if (latestPublishedFlowMap == null) {

			return null;
		}

		if (session.getAttribute("UserFavourites") != null) {

			return (List<UserFavourite>) session.getAttribute("UserFavourites");

		}

		HighLevelQuery<UserFavourite> query = new HighLevelQuery<UserFavourite>();

		query.addParameter(userFavouriteUserParamFactory.getParameter(user));

		List<UserFavourite> userFavourites = getAll(query);

		if (userFavourites != null) {

			Iterator<UserFavourite> iterator = userFavourites.iterator();

			UserFavourite userFavourite;

			while (iterator.hasNext()) {

				userFavourite = iterator.next();

				Flow latestPublishedFlow = latestPublishedFlowMap.get(userFavourite.getFlowFamily().getFlowFamilyID());

				if (latestPublishedFlow == null) {

					iterator.remove();

					continue;
				}

				userFavourite.setFlowName(latestPublishedFlow.getName());
				userFavourite.setFlowEnabled(latestPublishedFlow.isEnabled());
			}

			Collections.sort(userFavourites, FLOWNAME_COMPARATOR);

			session.setAttribute("UserFavourites", userFavourites);

		} else {
			session.setAttribute("UserFavourites", new ArrayList<UserFavourite>(0));
		}

		return userFavourites;

	}

	public UserFavourite get(User user, FlowFamily flowFamily) throws SQLException {

		HighLevelQuery<UserFavourite> query = new HighLevelQuery<UserFavourite>();

		query.addParameter(userFavouriteUserParamFactory.getParameter(user));
		query.addParameter(userFavouriteFlowFamilyParamFactory.getParameter(flowFamily));

		return get(query);

	}

}
