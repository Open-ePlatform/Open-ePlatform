package com.nordicpeak.flowengine.queries.contactdetailquery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
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
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.populators.StringSwedishPhoneNumberPopulator;
import se.unlogic.standardutils.populators.SwedishSocialSecurity12DigitsWithoutMinusPopulator;
import se.unlogic.standardutils.string.StringUtils;
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
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.QueryResponse;
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

public class ContactDetailQueryProviderModule extends BaseQueryProviderModule<ContactDetailQueryInstance> implements BaseQueryCRUDCallback {

	private static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();
	private static final SwedishSocialSecurity12DigitsWithoutMinusPopulator SOCIAL_SECURITY_NUMBER_POPULATOR = new SwedishSocialSecurity12DigitsWithoutMinusPopulator();
	
	@XSLVariable(prefix = "java.")
	private String exportFirstName;
	
	@XSLVariable(prefix = "java.")
	private String exportLastName;

	@XSLVariable(prefix = "java.")
	private String exportAddress;

	@XSLVariable(prefix = "java.")
	private String exportZipCode;

	@XSLVariable(prefix = "java.")
	private String exportPostalAddress;

	@XSLVariable(prefix = "java.")
	private String exportPhone;

	@XSLVariable(prefix = "java.")
	private String exportEmail;

	@XSLVariable(prefix = "java.")
	private String exportMobilePhone;
	
	@XSLVariable(prefix = "java.")
	private String exportSocialSecurityNumber;

	private AnnotatedDAO<ContactDetailQuery> queryDAO;
	private AnnotatedDAO<ContactDetailQueryInstance> queryInstanceDAO;

	private ContactDetailQueryCRUD queryCRUD;

	private QueryParameterFactory<ContactDetailQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<ContactDetailQueryInstance, Integer> queryInstanceIDParamFactory;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		// Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, ContactDetailQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(ContactDetailQuery.class);
		queryInstanceDAO = daoFactory.getDAO(ContactDetailQueryInstance.class);

		queryCRUD = new ContactDetailQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<ContactDetailQuery>(ContactDetailQuery.class), "ContactDetailQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		ContactDetailQuery query = new ContactDetailQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ContactDetailQuery query = new ContactDetailQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor) throws SQLException {

		ContactDetailQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ContactDetailQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		ContactDetailQueryInstance queryInstance;

		// Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new ContactDetailQueryInstance();

			queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

			if (queryInstance.getQuery() == null) {

				return null;
			}
			
			if (poster != null) {

				queryInstance.initialize(poster);
			}

		} else {

			queryInstance = getQueryInstance(descriptor.getQueryInstanceID());

			if (queryInstance == null) {

				return null;
			}
			
			queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

			if (queryInstance.getQuery() == null) {

				return null;
			}
		}

		if(req != null){

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}
		
		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		return queryInstance;
	}

	private ContactDetailQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<ContactDetailQuery> query = new HighLevelQuery<ContactDetailQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private ContactDetailQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<ContactDetailQuery> query = new HighLevelQuery<ContactDetailQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private ContactDetailQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<ContactDetailQueryInstance> query = new HighLevelQuery<ContactDetailQueryInstance>(ContactDetailQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(ContactDetailQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}

	@Override
	public void populate(ContactDetailQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		StringPopulator stringPopulator = StringPopulator.getPopulator();

		List<ValidationError> errors = new ArrayList<ValidationError>();

		Integer queryID = queryInstance.getQuery().getQueryID();

		boolean contactBySMS = req.getParameter("q" + queryID + "_contactBySMS") != null;
		boolean persistUserProfile = req.getParameter("q" + queryID + "_persistUserProfile") != null && !queryInstance.getQuery().isDisableProfileUpdate();

		boolean requireAddressFields = !allowPartialPopulation && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED && !queryInstance.isOfficalAddress() && queryInstance.getQuery().requiresAddress();

		String address = ValidationUtils.validateParameter("q" + queryID + "_address", req, requireAddressFields, stringPopulator, errors);
		String zipCode = ValidationUtils.validateParameter("q" + queryID + "_zipcode", req, requireAddressFields, stringPopulator, errors);
		String postalAddress = ValidationUtils.validateParameter("q" + queryID + "_postaladdress", req, requireAddressFields, stringPopulator, errors);
		String mobilePhone = ValidationUtils.validateParameter("q" + queryID + "_mobilephone", req, (contactBySMS || queryInstance.getQuery().requiresMobilePhone()) && !allowPartialPopulation, StringSwedishPhoneNumberPopulator.getPopulator(), errors);
		String email = ValidationUtils.validateParameter("q" + queryID + "_email", req, (!queryInstance.getQuery().isAllowSMS() || queryInstance.getQuery().requiresEmail()) && !allowPartialPopulation, EMAIL_POPULATOR, errors);
		String phone = ValidationUtils.validateParameter("q" + queryID + "_phone", req, queryInstance.getQuery().requiresPhone() && !allowPartialPopulation, StringSwedishPhoneNumberPopulator.getPopulator(), errors);

		String socialSecurityNumber;
		
		if(queryInstance.getQuery().isShowSocialSecurityNumberField()){
			
			if(poster == null){
				
				socialSecurityNumber = ValidationUtils.validateParameter("q" + queryID + "_socialSecurityNumber", req, !allowPartialPopulation, SOCIAL_SECURITY_NUMBER_POPULATOR, errors);
				
			}else if(poster.getAttributeHandler() != null){
				
				socialSecurityNumber = poster.getAttributeHandler().getString("citizenIdentifier");
				
			}else{
				
				socialSecurityNumber = null;
			}
			
		}else{
			
			socialSecurityNumber = null;
		}
		
		String rawEmail = req.getParameter("q" + queryID + "_email");
		String rawPhone = req.getParameter("q" + queryID + "_phone");
		String rawMobilePhone = req.getParameter("q" + queryID + "_mobilephone");
		
		String firstname;
		String lastname;

		if (poster != null) {

			if((queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.VISIBLE_REQUIRED || allowPartialPopulation) && StringUtils.isEmpty(address, zipCode, postalAddress, rawMobilePhone, rawPhone, rawEmail)){

				queryInstance.fullReset(attributeHandler);
				
				firstname = poster.getFirstname();
				lastname = poster.getLastname();
				
				return;
				
			}else{
				
				firstname = poster.getFirstname();
				lastname = poster.getLastname();
			}

			
		} else {
			
			firstname = ValidationUtils.validateParameter("q" + queryID + "_firstname", req, !allowPartialPopulation, stringPopulator, errors);
			lastname = ValidationUtils.validateParameter("q" + queryID + "_lastname", req, !allowPartialPopulation, stringPopulator, errors);

			if((queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.VISIBLE_REQUIRED || allowPartialPopulation) && StringUtils.isEmpty(address, zipCode, postalAddress, firstname, lastname, rawMobilePhone, rawPhone, rawEmail)){

				queryInstance.fullReset(attributeHandler);
				return;
			}
		}

		if(!requireAddressFields && !allowPartialPopulation  && !queryInstance.isOfficalAddress()){

			if (StringUtils.isEmpty(address) && (queryInstance.getQuery().requiresAddress() || (!StringUtils.isEmpty(zipCode) || !StringUtils.isEmpty(postalAddress)))) {
				errors.add(new ValidationError("q" + queryID + "_address", ValidationErrorType.RequiredField));
			}

			if (StringUtils.isEmpty(zipCode) && (queryInstance.getQuery().requiresAddress() || (!StringUtils.isEmpty(address) || !StringUtils.isEmpty(postalAddress)))) {
				errors.add(new ValidationError("q" + queryID + "_zipcode", ValidationErrorType.RequiredField));
			}

			if (StringUtils.isEmpty(postalAddress) && (queryInstance.getQuery().requiresAddress() || (!StringUtils.isEmpty(address) || !StringUtils.isEmpty(zipCode)))) {
				errors.add(new ValidationError("q" + queryID + "_postaladdress", ValidationErrorType.RequiredField));
			}
		}

		this.validateFieldLength("q" + queryID + "_firstname", firstname, 255, errors);
		this.validateFieldLength("q" + queryID + "_lastname", lastname, 255, errors);
		this.validateFieldLength("q" + queryID + "_address", address, 255, errors);
		this.validateFieldLength("q" + queryID + "_zipcode", zipCode, 10, errors);
		this.validateFieldLength("q" + queryID + "_postaladdress", postalAddress, 255, errors);
		this.validateFieldLength("q" + queryID + "_mobilephone", mobilePhone, 255, errors);
		this.validateFieldLength("q" + queryID + "_email", email, 255, errors);
		this.validateFieldLength("q" + queryID + "_phone", phone, 255, errors);

		if (queryInstance.getQuery().isAllowSMS() && !allowPartialPopulation && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED && !contactBySMS && StringUtils.isEmpty(rawEmail)) {
			errors.add(new ValidationError("NoContactChannelChoosen"));
		}
		
		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
		
		queryInstance.setContactBySMS(contactBySMS);

		queryInstance.setFirstname(firstname);
		queryInstance.setLastname(lastname);
		
		if(!queryInstance.isOfficalAddress()){
			
			queryInstance.setAddress(address);
			queryInstance.setZipCode(zipCode);
			queryInstance.setPostalAddress(postalAddress);
		}
		
		queryInstance.setMobilePhone(mobilePhone);
		queryInstance.setEmail(email);
		queryInstance.setPhone(phone);
		queryInstance.setPersistUserProfile(persistUserProfile);
		queryInstance.setSocialSecurityNumber(socialSecurityNumber);
		queryInstance.getQueryInstanceDescriptor().setPopulated(queryInstance.isPopulated());
		
		if (poster != null && poster instanceof MutableUser && persistUserProfile && poster.equals(user)) {

			MutableUser mutableUser = (MutableUser) poster;

			if (email != null) {

				mutableUser.setEmail(email);
			}

			MutableAttributeHandler userAttributeHandler = mutableUser.getAttributeHandler();

			if (userAttributeHandler != null) {

				setAttributeValue("address", address, userAttributeHandler);
				setAttributeValue("zipCode", zipCode, userAttributeHandler);
				setAttributeValue("postalAddress", postalAddress, userAttributeHandler);
				setAttributeValue("mobilePhone", mobilePhone, userAttributeHandler);
				setAttributeValue("phone", phone, userAttributeHandler);
				setAttributeValue("contactBySMS", contactBySMS, userAttributeHandler);
			}

			if (!errors.isEmpty()) {

				throw new ValidationException(errors);

			} else {

				try {

					log.info("User " + user + " updating user profile");

					req.getSession(true).setAttribute("user", user);

					this.systemInterface.getUserHandler().updateUser(mutableUser, false, false, userAttributeHandler != null);

				} catch (UnableToUpdateUserException e) {

					throw new ValidationException(new ValidationError("UnableToUpdateUser"));

				}

			}

		}

	}

	private void setAttributeValue(String name, Object value, MutableAttributeHandler attributeHandler) {

		if (value != null) {

			attributeHandler.setAttribute(name, value);

		} else {

			attributeHandler.removeAttribute(name);

		}

	}

	private void validateFieldLength(String fieldName, String field, Integer maxLength, List<ValidationError> errors) {

		if (field != null && field.length() > maxLength) {

			errors.add(new TooLongContentValidationError(fieldName, field.length(), maxLength));
		}

	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		ContactDetailQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ContactDetailQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

		if (queryInstance == null) {

			return false;
		}

		this.queryInstanceDAO.delete(queryInstance, transactionHandler);

		return true;
	}

	@Override
	public Document createDocument(HttpServletRequest req, User poster) {

		Document doc = super.createDocument(req, poster);

		if (poster != null) {
			
			Element userElement = poster.toXML(doc);
			
			doc.getDocumentElement().appendChild(userElement);
			
			if(poster.getAttributeHandler() != null){
				
				XMLUtils.appendNewElement(doc, userElement, "SocialSecurityNumber", poster.getAttributeHandler().getString("citizenIdentifier"));
			}
		}

		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler) throws SQLException {

		ContactDetailQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		queryDAO.add(query, transactionHandler, null);
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, ContactDetailQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler)));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<ContactDetailQueryInstance> getQueryInstanceClass() {

		return ContactDetailQueryInstance.class;
	}

	public String getExportFirstName() {

		return exportFirstName;
	}

	public String getExportLastName() {

		return exportLastName;
	}

	public String getExportAddress() {

		return exportAddress;
	}

	public String getExportZipCode() {

		return exportZipCode;
	}

	public String getExportPostalAddress() {

		return exportPostalAddress;
	}

	public String getExportPhone() {

		return exportPhone;
	}

	public String getExportEmail() {

		return exportEmail;
	}

	public String getExportMobilePhone() {

		return exportMobilePhone;
	}

	public String getExportSocialSecurityNumber() {

		return exportSocialSecurityNumber;
	}

	@Override
	public QueryResponse getFormHTML(ContactDetailQueryInstance queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, RequestMetadata requestMetadata, AttributeHandler attributeHandler) throws TransformerConfigurationException, TransformerException {

		queryInstance.setMutableUserWithAccess(poster instanceof MutableUser && poster.equals(user));
		
		return super.getFormHTML(queryInstance, req, user, poster, validationErrors, enableAjaxPosting, queryRequestURL, requestMetadata, attributeHandler);
	}

}
