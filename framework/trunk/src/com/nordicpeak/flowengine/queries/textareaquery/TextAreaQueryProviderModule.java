package com.nordicpeak.flowengine.queries.textareaquery;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.TooLongContentValidationError;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class TextAreaQueryProviderModule extends BaseQueryProviderModule<TextAreaQueryInstance> implements BaseQueryCRUDCallback {

	private static final int DEFAULT_MAX_LENGTH = 65536;

	private AnnotatedDAO<TextAreaQuery> queryDAO;
	private AnnotatedDAO<TextAreaQueryInstance> queryInstanceDAO;

	private TextAreaQueryCRUD queryCRUD;

	private QueryParameterFactory<TextAreaQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<TextAreaQueryInstance, Integer> queryInstanceIDParamFactory;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, TextAreaQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(TextAreaQuery.class);
		queryInstanceDAO = daoFactory.getDAO(TextAreaQueryInstance.class);

		queryCRUD = new TextAreaQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<TextAreaQuery>(TextAreaQuery.class), "TextAreaQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		TextAreaQuery query = new TextAreaQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		TextAreaQuery query = new TextAreaQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor) throws SQLException {

		TextAreaQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		TextAreaQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		TextAreaQueryInstance queryInstance;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new TextAreaQueryInstance();

		} else {

			queryInstance = getQueryInstance(descriptor.getQueryInstanceID());

			if (queryInstance == null) {

				return null;
			}
		}

		queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

		if(queryInstance.getQuery() == null){

			return null;
		}

		if(req != null){

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}
		
		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		//If this is a new query instance copy the default values
		if(descriptor.getQueryInstanceID() == null){

			queryInstance.copyQueryValues();
		}

		return queryInstance;
	}

	private TextAreaQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<TextAreaQuery> query = new HighLevelQuery<TextAreaQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private TextAreaQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<TextAreaQuery> query = new HighLevelQuery<TextAreaQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private TextAreaQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<TextAreaQueryInstance> query = new HighLevelQuery<TextAreaQueryInstance>(TextAreaQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(TextAreaQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		if(queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())){

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		}else{

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}

	@Override
	public void populate(TextAreaQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		String value = req.getParameter("q" + queryInstance.getQuery().getQueryID() + "_value");

		if(StringUtils.isEmpty(value)){

			if(!allowPartialPopulation && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED){

				throw new ValidationException(new ValidationError("RequiredField"));
			}

			queryInstance.reset(attributeHandler);
			queryInstance.getQueryInstanceDescriptor().setPopulated(false);
			return;
		}

		value = value.trim();

		if((queryInstance.getQuery().getMaxLength() == null && value.length() > DEFAULT_MAX_LENGTH) || (queryInstance.getQuery().getMaxLength() != null && value.length() > queryInstance.getQuery().getMaxLength())){

			throw new ValidationException(new TooLongContentValidationError(value.length(),queryInstance.getQuery().getMaxLength() != null ? queryInstance.getQuery().getMaxLength() : DEFAULT_MAX_LENGTH));

		}

		queryInstance.setValue(value);
		queryInstance.getQueryInstanceDescriptor().setPopulated(true);
		
		if(queryInstance.getQuery().isSetAsAttribute()){
			
			attributeHandler.setAttribute(queryInstance.getQuery().getAttributeName(), value);
		}
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		TextAreaQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		TextAreaQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

		if (queryInstance == null) {

			return false;
		}

		this.queryInstanceDAO.delete(queryInstance, transactionHandler);

		return true;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler) throws SQLException {

		TextAreaQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		queryDAO.add(query, transactionHandler, null);
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, TextAreaQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if(queryInstance.getQuery().getDescription() != null){

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler)));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<TextAreaQueryInstance> getQueryInstanceClass() {

		return TextAreaQueryInstance.class;
	}
}
