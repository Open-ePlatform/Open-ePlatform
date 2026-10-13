package com.nordicpeak.flowengine.queries.radiobuttonquery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.validationerrors.InvalidFormatValidationError;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.formatvalidation.FormatValidationHandler;
import com.nordicpeak.flowengine.formatvalidation.FormatValidator;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryContentFilter;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.populators.FreeTextAlternativePopulator;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativeQueryUtils;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class RadioButtonQueryProviderModule extends BaseQueryProviderModule<RadioButtonQueryInstance> implements BaseQueryCRUDCallback {

	@XSLVariable(prefix="java.")
	private String countText = "Count";

	@XSLVariable(prefix="java.")
	private String alternativesText = "Alternative";

	private AnnotatedDAO<RadioButtonQuery> queryDAO;
	private AnnotatedDAO<RadioButtonQueryInstance> queryInstanceDAO;

	private RadioButtonQueryCRUD queryCRUD;

	private QueryParameterFactory<RadioButtonQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<RadioButtonQueryInstance, Integer> queryInstanceIDParamFactory;
	
	@InstanceManagerDependency(required = true)
	private FormatValidationHandler formatValidationHandler;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, RadioButtonQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(RadioButtonQuery.class);
		queryInstanceDAO = daoFactory.getDAO(RadioButtonQueryInstance.class);

		queryCRUD = new RadioButtonQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<RadioButtonQuery>(RadioButtonQuery.class), "RadioButtonQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		RadioButtonQuery query = new RadioButtonQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap, QueryContentFilter contentFilter) throws Throwable {

		RadioButtonQuery query = new RadioButtonQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));
		
		contentFilter.filterHTML(query);

		List<Integer> oldAlternativeIDs = FixedAlternativeQueryUtils.getAlternativeIDs(query);

		FixedAlternativeQueryUtils.clearAlternativeIDs(query.getAlternatives());

		this.queryDAO.add(query, transactionHandler, null);

		query.setAlternativeConversionMap(FixedAlternativeQueryUtils.getAlternativeConversionMap(query.getAlternatives(), oldAlternativeIDs));

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

		RadioButtonQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		RadioButtonQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		RadioButtonQueryInstance queryInstance;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new RadioButtonQueryInstance();

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

	private RadioButtonQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<RadioButtonQuery> query = new HighLevelQuery<RadioButtonQuery>(RadioButtonQuery.ALTERNATIVES_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private RadioButtonQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<RadioButtonQuery> query = new HighLevelQuery<RadioButtonQuery>(RadioButtonQuery.ALTERNATIVES_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private RadioButtonQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<RadioButtonQueryInstance> query = new HighLevelQuery<RadioButtonQueryInstance>(RadioButtonQueryInstance.ALTERNATIVE_RELATION, RadioButtonQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(RadioButtonQueryInstance queryInstance, TransactionHandler transactionHandler, InstanceRequestMetadata requestMetadata) throws Throwable {

		if(queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())){

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		}else{

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}
	
	@Override
	public void populate(RadioButtonQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, InstanceRequestMetadata requestMetadata) throws ValidationException {

		RadioButtonQuery query = queryInstance.getQuery();
		
		if (query.isLockForManagerUpdate() && requestMetadata.isManager()) {
			return;
		}
		if (query.isLockForOwnerUpdate() && requestMetadata.flowInstanceIsSubmitted() && !requestMetadata.isManager()) {
			return;
		}
		
		if (queryInstance.isLocked()) {
			return;
		}
		
		List<RadioButtonAlternative> availableAlternatives = query.getAlternatives();

		if (CollectionUtils.isEmpty(availableAlternatives)) {

			//If the parent query doesn't have any alternatives then there is no population to do
			queryInstance.reset(attributeHandler);
			return;
		}

		Integer alternativeID = NumberUtils.toInt(req.getParameter("q" + query.getQueryID() + "_alternative"));

		boolean alternativeSelected = false;

		RadioButtonAlternative selectedAlternative = null;

		if (alternativeID != null) {

			for (RadioButtonAlternative alternative : availableAlternatives) {

				if (alternative.getAlternativeID().equals(alternativeID)) {

					selectedAlternative = alternative;
					alternativeSelected = true;
					break;
				}
			}
		}

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String freeTextAlternativeValue = null;
		
		if (query.getFreeTextAlternative() != null && !alternativeSelected) {
			
			freeTextAlternativeValue = FreeTextAlternativePopulator.populate(query.getQueryID(), "_alternative", req, validationErrors);

			if (freeTextAlternativeValue != null) {
				
				validateFreeTextValue(freeTextAlternativeValue, queryInstance, validationErrors);
				
				if (!validationErrors.isEmpty() && !ValidationUtils.containsValidationErrorWithMessageKey("FreeTextAlternativeValueRequired", validationErrors)) {
					
					allowPartialPopulation = false;
				}
				
				alternativeSelected = true;
			}
		}
		
		//If partial population is allowed, skip validation
		if (allowPartialPopulation) {

			queryInstance.setAlternative(selectedAlternative);
			queryInstance.setFreeTextAlternativeValue(freeTextAlternativeValue);
			queryInstance.getQueryInstanceDescriptor().setPopulated(alternativeSelected);
			
			if (query.isSetAsAttribute()) {
				
				queryInstance.resetAttribute(attributeHandler);
				queryInstance.setAttribute(attributeHandler);
			}
			
			return;
		}

		//Check if this query is required and if the user has selected any alternative
		if (queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED && !alternativeSelected) {

			validationErrors.add(new ValidationError("RequiredQuery"));
		}

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		queryInstance.setFreeTextAlternativeValue(freeTextAlternativeValue);
		queryInstance.setAlternative(selectedAlternative);
		queryInstance.getQueryInstanceDescriptor().setPopulated(alternativeSelected);
		
		if (query.isSetAsAttribute()) {
			
			queryInstance.resetAttribute(attributeHandler);
			queryInstance.setAttribute(attributeHandler);
		}
	}
	
	private void validateFreeTextValue(String freeTextAlternativeValue, RadioButtonQueryInstance queryInstance, List<ValidationError> validationErrors) {

		if (queryInstance.getQuery().getFormatValidator() != null) {
			StringFormatValidator formatValidator = getStringFormatValidator(queryInstance.getQuery());

			if (formatValidator == null) {
				log.warn("Unable to get format validator " + queryInstance.getQuery().getFormatValidator() + " for query " + queryInstance.getQuery());

			} else if (!formatValidator.validateFormat(freeTextAlternativeValue)) {

				validationErrors.add(new InvalidFormatValidationError(queryInstance.getQuery().getQueryID() + "_freeTextAlternativeValue", queryInstance.getQuery().getInvalidFormatMessage()));
			}
		}
	}

	private StringFormatValidator getStringFormatValidator(RadioButtonQuery query) {

		if (formatValidationHandler != null) {
			
			return formatValidationHandler.getFormatValidator(query.getFormatValidator());
		}
		
		return null;
	}
	
	public List<FormatValidator> getFormatValidators() {

		if (formatValidationHandler != null) {

			return formatValidationHandler.getFormatValidators();
		}

		return null;
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		RadioButtonQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		RadioButtonQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

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
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws SQLException {

		RadioButtonQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		if(query.getAlternatives() != null){

			for(RadioButtonAlternative alternative : query.getAlternatives()){

				alternative.setAlternativeID(null);
			}
		}

		this.queryDAO.add(query, transactionHandler, new RelationQuery(RadioButtonQuery.ALTERNATIVES_RELATION));
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, RadioButtonQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if(queryInstance.getQuery().getDescription() != null){

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler), systemInterface.getEncoding()));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<RadioButtonQueryInstance> getQueryInstanceClass() {

		return RadioButtonQueryInstance.class;
	}

}
