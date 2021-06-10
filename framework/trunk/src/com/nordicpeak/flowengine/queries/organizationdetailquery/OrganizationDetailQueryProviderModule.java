package com.nordicpeak.flowengine.queries.organizationdetailquery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.populators.StringSwedishPhoneNumberPopulator;
import se.unlogic.standardutils.populators.SwedishSocialSecurityOrOrganizationNumberPopulator;
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

import com.nordicpeak.flowengine.UserOrganizationsModule;
import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.UserOrganization;
import com.nordicpeak.flowengine.enums.QueryState;
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
import com.nordicpeak.flowengine.utils.CitizenIdentifierUtils;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class OrganizationDetailQueryProviderModule extends BaseQueryProviderModule<OrganizationDetailQueryInstance> implements BaseQueryCRUDCallback {

	private static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();
	private static final SwedishSocialSecurityOrOrganizationNumberPopulator ORGANIZATION_NUMBER_POPULATOR = new SwedishSocialSecurityOrOrganizationNumberPopulator();
	
	@XSLVariable(prefix = "java.")
	private String exportOrganizationName;

	@XSLVariable(prefix = "java.")
	private String exportOrganizationNumber;

	@XSLVariable(prefix = "java.")
	private String exportContactFirstName;
	
	@XSLVariable(prefix = "java.")
	private String exportContactLastName;
	
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

	private AnnotatedDAO<OrganizationDetailQuery> queryDAO;
	private AnnotatedDAO<OrganizationDetailQueryInstance> queryInstanceDAO;

	private OrganizationDetailQueryCRUD queryCRUD;

	private QueryParameterFactory<OrganizationDetailQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<OrganizationDetailQueryInstance, Integer> queryInstanceIDParamFactory;

	@InstanceManagerDependency(required = true)
	protected UserOrganizationsModule userOrganizationsModule;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, OrganizationDetailQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(OrganizationDetailQuery.class);
		queryInstanceDAO = daoFactory.getDAO(OrganizationDetailQueryInstance.class);

		queryCRUD = new OrganizationDetailQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<OrganizationDetailQuery>(OrganizationDetailQuery.class), "OrganizationDetailQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		OrganizationDetailQuery query = new OrganizationDetailQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap, QueryContentFilter contentFilter) throws Throwable {

		OrganizationDetailQuery query = new OrganizationDetailQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));
		
		contentFilter.filterHTML(query);

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

		OrganizationDetailQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		OrganizationDetailQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		OrganizationDetailQueryInstance queryInstance;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new OrganizationDetailQueryInstance();

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

		if(req != null){

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}

		queryInstance.getQuery().scanAttributeTags();
		
		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		return queryInstance;
	}

	private OrganizationDetailQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<OrganizationDetailQuery> query = new HighLevelQuery<OrganizationDetailQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private OrganizationDetailQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<OrganizationDetailQuery> query = new HighLevelQuery<OrganizationDetailQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private OrganizationDetailQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<OrganizationDetailQueryInstance> query = new HighLevelQuery<OrganizationDetailQueryInstance>(OrganizationDetailQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(OrganizationDetailQueryInstance queryInstance, TransactionHandler transactionHandler, InstanceRequestMetadata requestMetadata) throws Throwable {

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}

	@Override
	public void populate(OrganizationDetailQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, InstanceRequestMetadata requestMetadata) throws ValidationException {

		OrganizationDetailQuery query = queryInstance.getQuery();
		
		StringPopulator stringPopulator = StringPopulator.getPopulator();
		StringSwedishPhoneNumberPopulator phonePopulator = StringSwedishPhoneNumberPopulator.getPopulator();

		List<ValidationError> errors = new ArrayList<ValidationError>();

		Integer queryID = query.getQueryID();

		boolean contactBySMS = req.getParameter("q" + queryID + "_contactBySMS") != null;
		boolean persistOrganization = req.getParameter("q" + queryID + "_persistOrganization") != null;

		boolean requireAddressFields = !allowPartialPopulation && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED && query.requiresAddress();

		String name = ValidationUtils.validateParameter("q" + queryID + "_name", req, !allowPartialPopulation, stringPopulator, errors);
		
		String organizationNumber = ValidationUtils.validateParameter("q" + queryID + "_organizationNumber", req, !allowPartialPopulation, ORGANIZATION_NUMBER_POPULATOR, errors);

		String address = ValidationUtils.validateParameter("q" + queryID + "_address", req, requireAddressFields, stringPopulator, errors);
		String zipCode = ValidationUtils.validateParameter("q" + queryID + "_zipcode", req, requireAddressFields, stringPopulator, errors);
		String postalAddress = ValidationUtils.validateParameter("q" + queryID + "_postaladdress", req, requireAddressFields, stringPopulator, errors);
		String mobilePhone = ValidationUtils.validateParameter("q" + queryID + "_mobilephone", req, contactBySMS, phonePopulator, errors);
		String email = ValidationUtils.validateParameter("q" + queryID + "_email", req, !query.isAllowSMS() && !allowPartialPopulation, EMAIL_POPULATOR, errors);
		String phone = ValidationUtils.validateParameter("q" + queryID + "_phone", req, false, phonePopulator, errors);
		Integer organizationID = ValidationUtils.validateParameter("q" + queryID + "_organization", req, false, IntegerPopulator.getPopulator(), errors);

		String firstname;
		String lastname;

		String rawEmail = req.getParameter("q" + queryID + "_email");
		String rawPhone = req.getParameter("q" + queryID + "_phone");
		String rawMobilePhone = req.getParameter("q" + queryID + "_mobilephone");
		
		if (poster != null) {

			if((queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.VISIBLE_REQUIRED || allowPartialPopulation) && StringUtils.isEmpty(address, zipCode, postalAddress, rawMobilePhone, rawPhone, rawEmail)){

				queryInstance.fullReset(attributeHandler);
				return;
			}

			firstname = poster.getFirstname();
			lastname = poster.getLastname();
			
		} else {
			
			firstname = ValidationUtils.validateParameter("q" + queryID + "_firstname", req, !allowPartialPopulation, stringPopulator, errors);
			lastname = ValidationUtils.validateParameter("q" + queryID + "_lastname", req, !allowPartialPopulation, stringPopulator, errors);

			if((queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.VISIBLE_REQUIRED || allowPartialPopulation) && StringUtils.isEmpty(address, zipCode, postalAddress, firstname, lastname, rawMobilePhone, rawPhone, rawEmail)){

				queryInstance.fullReset(attributeHandler);
				return;
			}
		}
		
		if(!requireAddressFields && !allowPartialPopulation){

			if (StringUtils.isEmpty(address) && (query.requiresAddress() || (!StringUtils.isEmpty(zipCode) || !StringUtils.isEmpty(postalAddress)))) {
				errors.add(new ValidationError("q" + queryID + "_address", ValidationErrorType.RequiredField));
			}

			if (StringUtils.isEmpty(zipCode) && (query.requiresAddress() || (!StringUtils.isEmpty(address) || !StringUtils.isEmpty(postalAddress)))) {
				errors.add(new ValidationError("q" + queryID + "_zipcode", ValidationErrorType.RequiredField));
			}

			if (StringUtils.isEmpty(postalAddress) && (query.requiresAddress() || (!StringUtils.isEmpty(address) || !StringUtils.isEmpty(zipCode)))) {
				errors.add(new ValidationError("q" + queryID + "_postaladdress", ValidationErrorType.RequiredField));
			}
		}

		this.validateFieldLength("q" + queryID + "_name", name, 255, errors);
		this.validateFieldLength("q" + queryID + "_organizationNumber", organizationNumber, 16, errors);
		this.validateFieldLength("q" + queryID + "_address", address, 255, errors);
		this.validateFieldLength("q" + queryID + "_zipcode", zipCode, 10, errors);
		this.validateFieldLength("q" + queryID + "_postaladdress", postalAddress, 255, errors);
		this.validateFieldLength("q" + queryID + "_firstname", firstname, 255, errors);
		this.validateFieldLength("q" + queryID + "_lastname", lastname, 255, errors);
		this.validateFieldLength("q" + queryID + "_mobilephone", mobilePhone, 255, errors);
		this.validateFieldLength("q" + queryID + "_email", email, 255, errors);
		this.validateFieldLength("q" + queryID + "_phone", phone, 255, errors);

		if (query.isAllowSMS() && !allowPartialPopulation && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED && !contactBySMS && StringUtils.isEmpty(rawEmail)) {
			errors.add(new ValidationError("NoContactChannelChoosen"));
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

		if (!StringUtils.isEmpty(email)) {
			if (!EmailUtils.isValidEmailAddress(email)) {
				errors.add(new ValidationError("q" + queryID + "_email", ValidationErrorType.InvalidFormat));
			}
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

		queryInstance.setContactBySMS(contactBySMS);

		queryInstance.setName(name);
		queryInstance.setOrganizationNumber(organizationNumber);
		queryInstance.setAddress(address);
		queryInstance.setZipCode(zipCode);
		queryInstance.setPostalAddress(postalAddress);
		queryInstance.setFirstname(firstname);
		queryInstance.setLastname(lastname);
		queryInstance.setMobilePhone(mobilePhone);
		queryInstance.setEmail(email);
		queryInstance.setPhone(phone);
		queryInstance.setOrganizationID(organizationID);
		queryInstance.setPersistOrganization(persistOrganization);

		queryInstance.getQueryInstanceDescriptor().setPopulated(queryInstance.isPopulated());
		
		if (poster != null && poster.equals(user)) {

			queryInstance.setCitizenIdentifier(CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster));
			
			if(persistOrganization){
			
				try {

					UserOrganization organization = populateOrganization(poster, queryInstance);

					if (organization.getOrganizationID() == null) {

						organization = userOrganizationsModule.addOrganization(poster, organization);

						queryInstance.setOrganizationID(organization.getOrganizationID());

					} else {

						userOrganizationsModule.updateOrganization(poster, organization);

					}

				} catch (SQLException e) {

					throw new ValidationException(new ValidationError("UnableToPersistOrganization"));

				} catch (AccessDeniedException e) {

					throw new ValidationException(new ValidationError("UnableToPersistOrganization"));

				}
			}
		}
		
		if (query.isSetAsAttribute()) {

			queryInstance.resetAttributes(attributeHandler);
			queryInstance.setAttributes(attributeHandler);
		}
	}

	private UserOrganization populateOrganization(User user, OrganizationDetailQueryInstance queryInstance) throws AccessDeniedException, SQLException {

		UserOrganization organization = null;

		if (queryInstance.getOrganizationID() != null) {

			organization = userOrganizationsModule.getOrganization(queryInstance.getOrganizationID());

		}

		if (organization == null) {

			organization = new UserOrganization();

		}

		organization.setName(queryInstance.getName());
		organization.setOrganizationNumber(queryInstance.getOrganizationNumber());
		organization.setAddress(queryInstance.getAddress());
		organization.setZipCode(queryInstance.getZipCode());
		organization.setPostalAddress(queryInstance.getPostalAddress());
		organization.setFirstname(queryInstance.getFirstname());
		organization.setLastname(queryInstance.getLastname());
		organization.setMobilePhone(queryInstance.getMobilePhone());
		organization.setEmail(queryInstance.getEmail());
		organization.setPhone(queryInstance.getPhone());
		organization.setContactBySMS(queryInstance.isContactBySMS());
		organization.setUser(user);

		return organization;

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

		OrganizationDetailQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		OrganizationDetailQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

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

			Element document = doc.getDocumentElement();

			document.appendChild(poster.toXML(doc));

			try {

				XMLUtils.append(doc, document, "UserOrganizations", userOrganizationsModule.getUserOrganizations(poster));

			} catch (SQLException e) {

				log.error("Unable to get user organizations for user " + poster + ", user will not be able to choose from its organizations");

			}

		}

		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws SQLException {

		OrganizationDetailQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		queryDAO.add(query, transactionHandler, null);
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, OrganizationDetailQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler), systemInterface.getEncoding()));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<OrganizationDetailQueryInstance> getQueryInstanceClass() {

		return OrganizationDetailQueryInstance.class;
	}

	public String getExportOrganizationName() {

		return exportOrganizationName;
	}

	public String getExportOrganizationNumber() {

		return exportOrganizationNumber;
	}

	public String getExportContactFirstName() {

		return exportContactFirstName;
	}
	
	public String getExportContactLastName() {

		return exportContactLastName;
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
	
	@Override
	public QueryResponse getFormHTML(OrganizationDetailQueryInstance queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, InstanceRequestMetadata requestMetadata, AttributeHandler attributeHandler) throws Throwable {
		
		if (dependencyLock != null) {

			dependencyReadLock.lock();

			try {
				if (hasRequiredDependencies) {

					checkRequiredDependencies();
				}

			} catch (ModuleConfigurationException e) {
				
				throw new RuntimeException(e);
				
			} finally {

				dependencyReadLock.unlock();
			}
		}

		queryInstance.setUserOrganizationsMutableWithAccess(poster != null && poster.equals(user));
		
		return super.getFormHTML(queryInstance, req, user, poster, validationErrors, enableAjaxPosting, queryRequestURL, requestMetadata, attributeHandler);
	}

}
