package com.nordicpeak.flowengine.queries.textfieldquery;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.formatvalidation.FormatValidationHandler;
import com.nordicpeak.flowengine.formatvalidation.FormatValidator;
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

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
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
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringFormatValidator;
import se.unlogic.standardutils.validation.TooLongContentValidationError;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;

public class TextFieldQueryProviderModule extends BaseQueryProviderModule<TextFieldQueryInstance> implements BaseQueryCRUDCallback, SystemStartupListener {

	@XSLVariable(prefix = "java.")
	protected String fieldLayoutNewLine = "This variable should be set by your stylesheet";

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
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		TextFieldQuery query = new TextFieldQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

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
	public Query getQuery(MutableQueryDescriptor descriptor) throws SQLException {

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

	private TextFieldQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<TextFieldQuery> query = new HighLevelQuery<TextFieldQuery>(TextFieldQuery.TEXT_FIELDS_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

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
	public void save(TextFieldQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);
		}
	}

	@Override
	public void populate(TextFieldQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

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
			if(textField.isDisabled()){
				
				TextFieldValue fieldValue = queryInstance.getTextFieldValue(textField.getTextFieldID());
				
				if(fieldValue != null){
					
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

				if(formatValidator == null){
					
					log.warn("Unable to get format validator " + textField.getFormatValidator() + " for field " + textField);
					
				}else if (!formatValidator.validateFormat(value)) {

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
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler) throws SQLException {

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

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler)));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<TextFieldQueryInstance> getQueryInstanceClass() {

		return TextFieldQueryInstance.class;
	}
}