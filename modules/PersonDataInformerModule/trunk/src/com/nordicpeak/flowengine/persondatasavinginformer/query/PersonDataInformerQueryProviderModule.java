package com.nordicpeak.flowengine.persondatasavinginformer.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.persondatasavinginformer.PersonDataInformerModule;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.FlowFamilyInformerSetting;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.populators.BooleanPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

public class PersonDataInformerQueryProviderModule extends BaseQueryProviderModule<PersonDataInformerQueryInstance> implements BaseQueryCRUDCallback {
	
	@XSLVariable(prefix = "java.")
	protected String alternativeName = "This variable should be set by your stylesheet";
	
	private AnnotatedDAO<PersonDataInformerQuery> queryDAO;
	private AnnotatedDAO<PersonDataInformerQueryInstance> queryInstanceDAO;
	
	private PersonDataInformerQueryCRUD queryCRUD;
	
	private QueryParameterFactory<PersonDataInformerQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<PersonDataInformerQueryInstance, Integer> queryInstanceIDParamFactory;
	
	private PersonDataInformerQueryAlternative alternative;
	
	@InstanceManagerDependency(required = true)
	protected PersonDataInformerModule personDataInformerModule;
	
	@Override
	protected void moduleConfigured() throws Exception {
		
		super.moduleConfigured();
		
		ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.ERROR);
		
		alternative = new PersonDataInformerQueryAlternative(alternativeName);
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, PersonDataInformerModule.class.getName(), new XMLDBScriptProvider(PersonDataInformerModule.class.getResourceAsStream("DB script.xml")));
		
		if (upgradeResult.isUpgrade()) {
			
			log.info(upgradeResult.toString());
		}
		
		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);
		
		queryDAO = daoFactory.getDAO(PersonDataInformerQuery.class);
		queryInstanceDAO = daoFactory.getDAO(PersonDataInformerQueryInstance.class);
		
		queryCRUD = new PersonDataInformerQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<PersonDataInformerQuery>(PersonDataInformerQuery.class), "PersonDataInformerQuery", "query", null, this);
		
		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}
	
	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {
		
		PersonDataInformerQuery query = new PersonDataInformerQuery();
		
		query.setQueryID(descriptor.getQueryID());
		
		this.queryDAO.add(query, transactionHandler, null);
		
		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());
		
		query.setAlternative(alternative);
		
		return query;
	}
	
	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {
		
		PersonDataInformerQuery query = new PersonDataInformerQuery();
		
		query.setQueryID(descriptor.getQueryID());
		
		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));
		
		this.queryDAO.add(query, transactionHandler, null);
		
		return query;
	}
	
	@Override
	public Query getQuery(MutableQueryDescriptor descriptor) throws SQLException {
		
		PersonDataInformerQuery query = this.getQuery(descriptor.getQueryID());
		
		if (query == null) {
			
			return null;
		}
		
		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());
		
		query.setAlternative(alternative);
		
		return query;
	}
	
	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {
		
		PersonDataInformerQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);
		
		if (query == null) {
			
			return null;
		}
		
		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());
		
		query.setAlternative(alternative);
		
		return query;
	}
	
	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {
		
		PersonDataInformerQueryInstance queryInstance = null;
		
		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {
			
			queryInstance = new PersonDataInformerQueryInstance();
			
		} else {
			
			queryInstance = getQueryInstance(descriptor.getQueryInstanceID());
			
			if (queryInstance == null) {
				
				return null;
			}
		}
		
		queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));
		
		if (queryInstance.getQuery() == null) {
			
			return null;
		}
		
		queryInstance.getQuery().setAlternative(alternative);
		
		FlowFamilyInformerSetting informerSetting = personDataInformerModule.getInformerSetting(descriptor.getQueryDescriptor().getStep().getFlow().getFlowFamily());
		
		if (informerSetting != null) {
			if (informerSetting.getReason() == null && personDataInformerModule.getDefaultReason() != null) {
				informerSetting.setReason(JTidyUtils.getXHTML(personDataInformerModule.getDefaultReason()));
			}
			
			if (informerSetting.getExtraInformation() == null && personDataInformerModule.getDefaultExtraInformation() != null) {
				informerSetting.setExtraInformation(JTidyUtils.getXHTML(personDataInformerModule.getDefaultExtraInformation()));
			}
			
			if (informerSetting.getExtraInformationStorage() == null && personDataInformerModule.getDefaultExtraInformationStorage() != null) {
				informerSetting.setExtraInformationStorage(JTidyUtils.getXHTML(personDataInformerModule.getDefaultExtraInformationStorage()));
			}
			
			TextTagReplacer.replaceTextTags(informerSetting, instanceMetadata.getSiteProfile());
			
			queryInstance.getQuery().setFamilyInformerSettings(informerSetting);
		}
		
		if (req != null) {
			
			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);
			
			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}
		
		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());
		
		queryInstance.set(descriptor);
		
		//If this is a new query instance copy the default values
		if (descriptor.getQueryInstanceID() == null) {
			
			queryInstance.defaultQueryValues();
		}
		
		return queryInstance;
	}
	
	private PersonDataInformerQuery getQuery(Integer queryID) throws SQLException {
		
		HighLevelQuery<PersonDataInformerQuery> query = new HighLevelQuery<PersonDataInformerQuery>();
		
		query.addParameter(queryIDParamFactory.getParameter(queryID));
		
		return queryDAO.get(query);
	}
	
	private PersonDataInformerQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {
		
		HighLevelQuery<PersonDataInformerQuery> query = new HighLevelQuery<PersonDataInformerQuery>();
		
		query.addParameter(queryIDParamFactory.getParameter(queryID));
		
		return queryDAO.get(query, transactionHandler);
	}
	
	private PersonDataInformerQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {
		
		HighLevelQuery<PersonDataInformerQueryInstance> query = new HighLevelQuery<PersonDataInformerQueryInstance>();
		
		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));
		
		return queryInstanceDAO.get(query);
	}
	
	@Override
	public void save(PersonDataInformerQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {
		
		//Check if the query instance has an ID set and if the ID of the descriptor has changed
		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {
			
			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());
			
			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);
			
		} else {
			
			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}
	
	@Override
	public void populate(PersonDataInformerQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		Boolean accepted = ValidationUtils.validateParameter("q" + queryInstance.getQuery().getQueryID() + "_accept", req, false, BooleanPopulator.getPopulator(), validationErrors);
		
		if (accepted == null) {
			accepted = false;
		}
		
		//If partial population is allowed and the user has not selected any alternatives, skip validation
		if (allowPartialPopulation) {
			
			queryInstance.setAccepted(accepted);
			queryInstance.getQueryInstanceDescriptor().setPopulated(accepted);
			
			return;
		}
		
		//Check if this query is required or if the user has selected any alternatives anyway
		if (queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED && !accepted) {
			
			validationErrors.add(new ValidationError("RequiredQuery"));
		}
		
		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}
		
		queryInstance.setAccepted(accepted);
		queryInstance.getQueryInstanceDescriptor().setPopulated(accepted);
	}
	
	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		return this.queryCRUD.update(req, res, user, uriParser);
	}
	
	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {
		
		PersonDataInformerQuery query = getQuery(descriptor.getQueryID());
		
		if (query == null) {
			
			return false;
		}
		
		this.queryDAO.delete(query, transactionHandler);
		
		return true;
	}
	
	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {
		
		PersonDataInformerQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());
		
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
		
		PersonDataInformerQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);
		
		query.setQueryID(copyQueryDescriptor.getQueryID());
		
		this.queryDAO.add(query, transactionHandler, null);
	}
	
	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, PersonDataInformerQueryInstance queryInstance, AttributeHandler attributeHandler) {
		
		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);
		
		if (queryInstance.getQuery().getDescription() != null) {
			
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler)));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}
	
	@Override
	protected Class<PersonDataInformerQueryInstance> getQueryInstanceClass() {
		
		return PersonDataInformerQueryInstance.class;
	}
	
}
