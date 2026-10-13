package com.nordicpeak.flowengine.queries.treequery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;
import com.nordicpeak.flowengine.queries.treequery.beans.TreeQuery;
import com.nordicpeak.treequerydataprovider.TreeDataProvider;

public class TreeQueryCRUD extends BaseQueryCRUD<TreeQuery, TreeQueryProviderModule> {

	protected AnnotatedDAOWrapper<TreeQuery, Integer> queryDAO;

	public TreeQueryCRUD(AnnotatedDAOWrapper<TreeQuery, Integer> queryDAO, BeanRequestPopulator<TreeQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, TreeQueryProviderModule callback) {

		super(TreeQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected void appendUpdateFormData(TreeQuery query, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendUpdateFormData(query, doc, updateTypeElement, user, req, uriParser);

		Element providersElement = XMLUtils.appendNewElement(doc, updateTypeElement, "Providers");

		for (TreeDataProvider provider : callback.getTreeProviders()) {

			Element providerElement = XMLUtils.appendNewElement(doc, providersElement, "Provider");

			XMLUtils.appendNewElement(doc, providerElement, "Name", provider.getTreeName());
			XMLUtils.appendNewElement(doc, providerElement, "ID", provider.getTreeID());
		}

		if (!StringUtils.isEmpty(query.getProviderIdentifier()) && callback.getTreeProvider(query.getProviderIdentifier()) == null) {

			Element providerElement = XMLUtils.appendNewElement(doc, providersElement, "Provider");

			XMLUtils.appendNewElement(doc, providerElement, "Name", callback.getMissingTreeProvider());
			XMLUtils.appendNewElement(doc, providerElement, "ID", query.getProviderIdentifier());
		}
	}

	@Override
	protected TreeQuery populateFromUpdateRequest(TreeQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		TreeQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}

}
