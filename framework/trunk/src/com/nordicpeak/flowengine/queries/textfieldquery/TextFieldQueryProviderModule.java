package com.nordicpeak.flowengine.queries.textfieldquery;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
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
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.standardutils.validation.TooLongContentValidationError;
import se.unlogic.standardutils.validation.TooShortContentValidationError;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.beans.QueryResponse;
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
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.queries.textfieldquery.api.TextFieldAPIRequestException;
import com.nordicpeak.flowengine.queries.textfieldquery.api.TextFieldQueryEndpoint;
import com.nordicpeak.flowengine.queries.textfieldquery.api.TextFieldQueryEndpointAdminModule;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class TextFieldQueryProviderModule extends BaseQueryProviderModule<TextFieldQueryInstance> implements BaseQueryCRUDCallback, SystemStartupListener {

	@XSLVariable(prefix = "java.")
	protected String fieldLayoutNewLine = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String fieldLayoutNewLineFullWidth = "This variable should be set by your stylesheet";

	@XSLVariable(prefix = "java.")
	protected String fieldLayoutFloat = "This variable should be set by your stylesheet";

	private static final RelationQuery SAVE_QUERY_INSTANCE_RELATION_QUERY = new RelationQuery(TextFieldQueryInstance.VALUES_RELATION);

	private AnnotatedDAO<TextFieldQuery> queryDAO;
	private AnnotatedDAO<TextFieldQueryInstance> queryInstanceDAO;

	private TextFieldQueryCRUD queryCRUD;
	private TextFieldCRUD textFieldCRUD;

	private QueryParameterFactory<TextFieldQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<TextFieldQueryInstance, Integer> queryInstanceIDParamFactory;

	@InstanceManagerDependency(required=true)
	private FormatValidationHandler formatValidationHandler;
	
	@InstanceManagerDependency
	private TextFieldQueryEndpointAdminModule apiModule;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		systemInterface.addSystemStartupListener(this);
	}
	
	@Override
	public void systemStarted() {

		if(formatValidationHandler == null){
			
			log.error("No format validation handler found in instance handler.");
		}
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, TextFieldQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(TextFieldQuery.class);
		queryInstanceDAO = daoFactory.getDAO(TextFieldQueryInstance.class);

		queryCRUD = new TextFieldQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<TextFieldQuery>(TextFieldQuery.class), "TextFieldQuery", "query", null, this);
		textFieldCRUD = new TextFieldCRUD(daoFactory.getDAO(TextField.class).getWrapper(Integer.class), new AnnotatedRequestPopulator<TextField>(TextField.class), "TextField", "textfield", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		TextFieldQuery query = new TextFieldQuery();

		query.setLayout(FieldLayout.FLOAT);

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap, QueryContentFilter contentFilter) throws Throwable {

		TextFieldQuery query = new TextFieldQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));
		
		contentFilter.filterHTML(query);

		List<Integer> oldTextFieldIDs = new ArrayList<Integer>();

		if (query.getFields() != null) {

			for (TextField textField : query.getFields()) {

				oldTextFieldIDs.add(textField.getTextFieldID());
				
				textField.setTextFieldID(null);
			}

		}
		
		this.queryDAO.add(query, transactionHandler, null);

		if (query.getFields() != null) {

			Map<Integer, Integer> textFieldConversionMap = new HashMap<Integer, Integer>(oldTextFieldIDs.size());

			int index = 0;

			for (Integer oldTextFieldID : oldTextFieldIDs) {

				textFieldConversionMap.put(oldTextFieldID, query.getFields().get(index).getTextFieldID());

				index++;
			}

			query.setTextFieldConversionMap(textFieldConversionMap);

		}

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

		TextFieldQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		TextFieldQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		TextFieldQueryInstance queryInstance;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new TextFieldQueryInstance();
			
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

		if(descriptor.getQueryInstanceID() == null){
			
			queryInstance.setDefaultValues();
		}
		
		if (req != null) {

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}
		
		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		return queryInstance;
	}

	private TextFieldQuery getQuery(Integer queryID, Field... extraRelations) throws SQLException {

		HighLevelQuery<TextFieldQuery> query = new HighLevelQuery<TextFieldQuery>(TextFieldQuery.TEXT_FIELDS_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));
		
		if (extraRelations != null) {
			
			query.addRelations(extraRelations);
		}

		return queryDAO.get(query);
	}

	private TextFieldQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<TextFieldQuery> query = new HighLevelQuery<TextFieldQuery>(TextFieldQuery.TEXT_FIELDS_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private TextFieldQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<TextFieldQueryInstance> query = new HighLevelQuery<TextFieldQueryInstance>(TextFieldQueryInstance.VALUES_RELATION, TextFieldValue.TEXT_FIELD_RELATION, TextFieldQueryInstance.QUERY_RELATION, TextFieldQuery.TEXT_FIELDS_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(TextFieldQueryInstance queryInstance, TransactionHandler transactionHandler, InstanceRequestMetadata requestMetadata) throws Throwable {

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);
		}
	}
	
	@Override
	protected void appendQueryInstance(TextFieldQueryInstance queryInstance, Document doc, Element targetElement, AttributeHandler attributeHandler) {
		
		super.appendQueryInstance(queryInstance, doc, targetElement, attributeHandler);
		
		if (queryInstance.getQuery().isLockOnOwnershipTransfer() && attributeHandler.getPrimitiveBoolean("OwnershipTransfered")) {
			
			XMLUtils.appendNewElement(doc, targetElement, "Locked", "true");
		}
	}
	
	@Override
	public QueryResponse getFormHTML(TextFieldQueryInstance queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, InstanceRequestMetadata requestMetadata, AttributeHandler attributeHandler) throws Throwable {

		TextFieldQuery query = queryInstance.getQuery();

		if (query.getEndpoint() != null) {
			
			List<ValidationError> apiErrors = getFromAPI(queryInstance, poster, user, requestMetadata, attributeHandler);

			if ((apiErrors != null || validationErrors != null) && validationErrors instanceof AbstractList) { // Fix for unmodifiable list
				validationErrors = new ArrayList<>(validationErrors);
			}

			if (apiErrors != null) {

				for (ValidationError error : apiErrors) {

					if (validationErrors == null || !validationErrors.contains(error)) {
						
						validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, error);
					}
				}

			} else if (apiErrors == null && validationErrors != null) { // Remove populate validation error if it worked just now

				Iterator<ValidationError> it = validationErrors.iterator();

				while (it.hasNext()) {

					if ("APIRequestException".equals(it.next().getMessageKey())) {

						it.remove();
						break;
					}
				}
			}
		}

		return super.getFormHTML(queryInstance, req, user, poster, validationErrors, enableAjaxPosting, queryRequestURL, requestMetadata, attributeHandler);
	}
	
	private List<ValidationError> getFromAPI(TextFieldQueryInstance queryInstance, User poster, User currentUser, InstanceRequestMetadata requestMetadata, AttributeHandler attributeHandler) {

		List<ValidationError> validationErrors = null;
		TextFieldQuery query = queryInstance.getQuery();
		
		if (!CollectionUtils.isEmpty(query.getFields())) {

			if (apiModule == null) {

				validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("APIRequestException"));

			} else {

				String endpointURL = apiModule.getEndpointURL(query.getEndpoint(), poster, currentUser, requestMetadata, attributeHandler);
				
				if (queryInstance.getLastUsedEndpointURL() == null || !queryInstance.getLastUsedEndpointURL().equals(endpointURL)) {

					try {
						Map<String, String> valuesFromAPI = apiModule.getAPIFieldValues(endpointURL, query.getEndpoint(), currentUser);
						
						if (!CollectionUtils.isEmpty(valuesFromAPI)) {

							for (TextField textField : query.getFields()) {

								if (!StringUtils.isEmpty(textField.getEndpointField())) {

									String valueFromAPI = valuesFromAPI.get(textField.getEndpointField());

									queryInstance.setFieldValue(textField.getTextFieldID(), valueFromAPI, (MutableAttributeHandler) attributeHandler);
								}
							}
						}

						queryInstance.setInitialized(true);
						queryInstance.setLastUsedEndpointURL(endpointURL);

					} catch (TextFieldAPIRequestException e) {

						validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("APIRequestException"));
					}
					
				} else {
					
					queryInstance.setInitialized(true);
				}
			}

		} else {

			queryInstance.setInitialized(true);
		}
		
		return validationErrors;
	}
	
	@Override
	public void populate(TextFieldQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, InstanceRequestMetadata requestMetadata) throws ValidationException {

		TextFieldQuery query = queryInstance.getQuery();
		
		if (query.isLockForManagerUpdate() && requestMetadata.isManager()) {
			return;
		}
		
		if (query.getEndpoint() != null && !queryInstance.isInitialized()) {
			
			if (allowPartialPopulation) {
				
				return;
				
			} else {
				
				throw new ValidationException(new ValidationError("APIRequestException"));
			}
		}

		if (query.isLockOnOwnershipTransfer() && attributeHandler.getPrimitiveBoolean("OwnershipTransfered")) {
			return;
		}

		List<TextField> textFields = queryInstance.getQuery().getFields();

		if (CollectionUtils.isEmpty(textFields)) {

			//If the parent query doesn't have any fields then there is no population to do
			queryInstance.reset(attributeHandler);
			return;
		}

		List<ValidationError> validationErrors = new ArrayList<ValidationError>(queryInstance.getQuery().getFields().size());
		

		List<TextFieldValue> textFieldValues = new ArrayList<TextFieldValue>(queryInstance.getQuery().getFields().size());

		for (TextField textField : textFields) {

			//If the field is disabled use the previous value if there is any
			if (textField.isDisabled()) {

				TextFieldValue fieldValue = queryInstance.getTextFieldValue(textField.getTextFieldID());

				if (fieldValue != null) {

					textFieldValues.add(fieldValue);
				}

				continue;
			}

			String value = req.getParameter("q" + queryInstance.getQuery().getQueryID() + "_field" + textField.getTextFieldID());

			if (StringUtils.isEmpty(value)) {

				if (!allowPartialPopulation && textField.isRequired() && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED) {

					validationErrors.add(new ValidationError(textField.getTextFieldID().toString(), ValidationErrorType.RequiredField));
				}

				continue;
			}

			value = value.trim();

			if (textField.getMinContentLength() != null && value.length() < textField.getMinContentLength()) {

				validationErrors.add(new TooShortContentValidationError(textField.getTextFieldID().toString(), value.length(), textField.getMinContentLength()));
				continue;
			}

			Integer maxLength = textField.getMaxContentLength();

			if (maxLength == null || maxLength > 255) {

				maxLength = 255;
			}

			if (value.length() > maxLength) {

				validationErrors.add(new TooLongContentValidationError(textField.getTextFieldID().toString(), value.length(), maxLength));
				continue;
			}

			if (textField.getFormatValidator() != null) {

				StringFormatValidator formatValidator = getStringFormatValidator(textField);

				if (formatValidator == null) {

					log.warn("Unable to get format validator " + textField.getFormatValidator() + " for field " + textField);

				} else if (!formatValidator.validateFormat(value)) {

					validationErrors.add(new InvalidFormatValidationError(textField.getTextFieldID().toString(), textField.getInvalidFormatMessage()));
					continue;
				}
			}

			textFieldValues.add(new TextFieldValue(textField, value));
		}

		if (!validationErrors.isEmpty()) {

			throw new ValidationException(validationErrors);
		}

		//Clear attributes
		for (TextField textField : textFields) {

			if (textField.isSetAsAttribute()) {

				attributeHandler.removeAttribute(textField.getAttributeName());
			}
		}

		if (textFieldValues.isEmpty()) {

			queryInstance.setValues(null);
			queryInstance.getQueryInstanceDescriptor().setPopulated(false);

		} else {

			queryInstance.setValues(textFieldValues);
			queryInstance.getQueryInstanceDescriptor().setPopulated(true);

			//Set attributes
			for (TextFieldValue textFieldValue : textFieldValues) {

				if (textFieldValue.getTextField().isSetAsAttribute()) {

					attributeHandler.setAttribute(textFieldValue.getTextField().getAttributeName(), textFieldValue.getValue());
				}
			}
		}
	}

	private FormatValidator getStringFormatValidator(TextField textField) {

		if(formatValidationHandler != null){
			
			return formatValidationHandler.getFormatValidator(textField.getFormatValidator());
		}
		
		return null;
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.show(req, res, user, uriParser);
	}

	@WebPublic(alias = "updatequery")
	public ForegroundModuleResponse updateQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(alias = "addtextfield")
	public ForegroundModuleResponse addTextField(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		TextFieldQuery query = this.queryCRUD.getRequestedBean(req, res, user, uriParser, null);

		if (query != null) {

			this.checkUpdateQueryAccess(user, query);

			req.setAttribute("TextFieldQuery", query);

			return textFieldCRUD.add(req, res, user, uriParser);

		}

		throw new URINotFoundException(uriParser);
	}

	@WebPublic(alias = "updatetextfield")
	public ForegroundModuleResponse updateTextField(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return textFieldCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(alias = "deletetextfield")
	public ForegroundModuleResponse deleteTextField(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return textFieldCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic(alias = "sorttextfields")
	public ForegroundModuleResponse sortTextFields(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return queryCRUD.sort(req, res, user, uriParser);
	}
	
	@WebPublic(alias = "selectendpoint")
	public ForegroundModuleResponse selectEndpoint(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		TextFieldQuery query = queryCRUD.getRequestedBean(req, res, user, uriParser, "UPDATE");
		
		if (query != null && !CollectionUtils.isEmpty(query.getFields()) && apiModule != null) {
			
			checkUpdateQueryAccess(user, query);
			
			List<ValidationError> validationErrors = null;
			
			if (req.getMethod().equalsIgnoreCase("POST")) {
				
				validationErrors = new ArrayList<ValidationError>();

				Integer endpointID = ValidationUtils.validateParameter("endpointID", req, false, IntegerPopulator.getPopulator(), validationErrors);
				
				if (endpointID == null) {
					
					query.setEndpoint(null);
					
				} else {
					
					TextFieldQueryEndpoint endpoint = apiModule.getEndpoint(endpointID);

					if (endpoint == null) {
						
						validationErrors.add(new ValidationError("endpointID", ValidationErrorType.InvalidFormat));
						
					} else {

						query.setEndpoint(endpoint);
						
						boolean anyEndpointFieldsSelected = false;
						
						for (TextField queryField : query.getFields()) {
							
							String fieldName = "endpointField-" + endpoint.getEndpointID() + "-" + queryField.getTextFieldID();

							String endpointField = ValidationUtils.validateParameter(fieldName, req, false, null, 255, StringPopulator.getPopulator(), validationErrors);

							if (endpointField != null && endpoint.getFields() != null && !endpoint.getFields().contains(endpointField)) {

								validationErrors.add(new ValidationError(fieldName, ValidationErrorType.InvalidFormat));
								anyEndpointFieldsSelected = true; // Suppress nothing selected error
								
							} else {

								queryField.setEndpointField(endpointField);

								if (endpointField != null) {

									anyEndpointFieldsSelected = true;
								}
							}
						}
						
						if (!anyEndpointFieldsSelected) {
							
							validationErrors.add(new ValidationError("NoEndpointFieldsSelected"));
						}
					}
				}
				
				if (validationErrors.isEmpty()) {
					
					queryDAO.update(query);
					
					return queryCRUD.beanUpdated(query, req, res, user, uriParser);
				}
			}
			
			Document doc = createDocument(req, user);
			Element selectEndpointElement = XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "SelectTextFieldQueryEndpoint");

			XMLUtils.append(doc, selectEndpointElement, query);

			XMLUtils.append(doc, selectEndpointElement, "Endpoints", apiModule.getEndpoints());

			if (!CollectionUtils.isEmpty(validationErrors)) {

				XMLUtils.append(doc, selectEndpointElement, new ValidationException(validationErrors));

				selectEndpointElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
		}
		
		throw new URINotFoundException(uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		TextFieldQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		TextFieldQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

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

	public String getFieldLayoutName(FieldLayout layout) {

		if (layout.equals(FieldLayout.FLOAT)) {
			return fieldLayoutFloat;
		} else if (layout.equals(FieldLayout.NEW_LINE)) {
			return fieldLayoutNewLine;
		} else if (layout.equals(FieldLayout.NEW_LINE_FULL_WIDTH)) {
			return fieldLayoutNewLineFullWidth;
		}

		return layout.toString();
	}

	public void checkUpdateQueryAccess(User user, TextFieldQuery query) throws AccessDeniedException, SQLException {

		flowAdminModule.checkFlowStructureManipulationAccess(user, (Flow) query.getQueryDescriptor().getStep().getFlow());
	}

	public void redirectToQueryConfig(TextFieldQuery query, HttpServletRequest req, HttpServletResponse res) throws IOException {

		res.sendRedirect(req.getContextPath() + this.getFullAlias() + "/config/" + query.getQueryID());
	}

	public List<FormatValidator> getFormatValidators() {

		if(formatValidationHandler != null){
			
			return formatValidationHandler.getFormatValidators();
		}
		
		return null;
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws SQLException {

		TextFieldQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		if (query.getFields() != null) {

			for (TextField textField : query.getFields()) {

				textField.setTextFieldID(null);
			}
		}

		this.queryDAO.add(query, transactionHandler, new RelationQuery(TextFieldQuery.TEXT_FIELDS_RELATION));
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, TextFieldQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler), systemInterface.getEncoding()));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<TextFieldQueryInstance> getQueryInstanceClass() {

		return TextFieldQueryInstance.class;
	}
	
	public TextFieldQueryEndpointAdminModule getAPIModule() {
		
		return apiModule;
	}
	
}
