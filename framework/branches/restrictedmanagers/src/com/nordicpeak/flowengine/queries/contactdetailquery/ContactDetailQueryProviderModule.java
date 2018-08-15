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
import com.nordicpeak.flowengine.utils.CitizenIdentifierUtils;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.foregroundmodules.userprofile.events.UserUpdatedEvent;
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

public class ContactDetailQueryProviderModule extends BaseQueryProviderModule<ContactDetailQueryInstance> implements BaseQueryCRUDCallback {
	
	
	private static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();
	private static final SwedishSocialSecurity12DigitsWithoutMinusPopulator SOCIAL_SECURITY_NUMBER_POPULATOR = new SwedishSocialSecurity12DigitsWithoutMinusPopulator();
	
	@XSLVariable(prefix = "java.")
	private String exportFirstName;
	
	@XSLVariable(prefix = "java.")
	private String exportLastName;
	
	@XSLVariable(prefix = "java.")
	private String exportCareOf;
	
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
	private String exportCitizenID;
	
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
		
		if (req != null) {
			
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
		ContactDetailQuery query = queryInstance.getQuery();
		
		boolean queryRequired = queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED;
		boolean fieldsRequired = queryRequired && !allowPartialPopulation;
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		String qParam = "q" + query.getQueryID() + "_";
		
		String firstname = null;
		String lastname = null;
		String citizenID = null;
		String phone = null;
		String mobilePhone = null;
		String email = null;
		String careOf = null;
		String address = null;
		String zipCode = null;
		String postalAddress = null;
		boolean officalAddress = false;
		boolean contactBySMS = false;
		
		if (query.isAllowSMSNotification()) {
			
			if (query.getFieldMobilePhone() == ContactDetailQueryField.REQUIRED && query.getFieldEmail() == ContactDetailQueryField.HIDDEN) {
				
				contactBySMS = true;
				
			} else {
				
				contactBySMS = req.getParameter(qParam + "contactBySMS") != null;
			}
		}
		
		if (query.getFieldName() != ContactDetailQueryField.HIDDEN) {
			
			if (poster != null) {
				
				if (requestMetadata.isManager()) {
					
					firstname = queryInstance.getFirstname();
					lastname = queryInstance.getLastname();
					
				} else {
					
					firstname = poster.getFirstname();
					lastname = poster.getLastname();
				}
				
			} else {
				
				boolean required = fieldsRequired && query.getFieldName() == ContactDetailQueryField.REQUIRED;
				firstname = ValidationUtils.validateParameter(qParam + "firstname", req, required, 0, 255, stringPopulator, validationErrors);
				lastname = ValidationUtils.validateParameter(qParam + "lastname", req, required, 0, 255, stringPopulator, validationErrors);
			}
		}
		
		if (query.getFieldCitizenID() != ContactDetailQueryField.HIDDEN) {
			
			if (poster == null) {
				
				boolean required = fieldsRequired && query.getFieldCitizenID() == ContactDetailQueryField.REQUIRED;
				citizenID = ValidationUtils.validateParameter(qParam + "citizenID", req, required, SOCIAL_SECURITY_NUMBER_POPULATOR, validationErrors);
				
			} else if (requestMetadata.isManager()) {
				
				citizenID = queryInstance.getCitizenID();
				
			} else {
				
				if (poster.getAttributeHandler() != null) {
					
					citizenID = CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster);
				}
				
				if (query.getFieldCitizenID() == ContactDetailQueryField.REQUIRED && StringUtils.isEmpty(citizenID)) {
					
					validationErrors.add(new ValidationError("RequiresCitizenIDOnUser"));
				}
			}
		}
		
		if (query.getFieldAddress() != ContactDetailQueryField.HIDDEN) {
			
			if (query.usesOfficalAddress() && poster != null && poster.getAttributeHandler() != null) {
				
				if (requestMetadata.isManager() && queryInstance.getQueryInstanceDescriptor().getQueryInstanceID() != null) { // Manager updating saved instance
					
					if (queryInstance.isOfficalAddress()) {
						
						address = queryInstance.getAddress();
						zipCode = queryInstance.getZipCode();
						postalAddress = queryInstance.getPostalAddress();
						officalAddress = true;
					}
					
				} else {
					
					AttributeHandler posterAttributeHandler = poster.getAttributeHandler();
					
					String officialCareOf = posterAttributeHandler.getString("official.careOf");
					String officialAddress = posterAttributeHandler.getString("official.address");
					String officialZipCode = posterAttributeHandler.getString("official.zipCode");
					String officialPostalAddress = posterAttributeHandler.getString("official.postalAddress");
					
					if (!StringUtils.isEmpty(officialAddress, officialZipCode, officialPostalAddress)) {
						
						careOf = officialCareOf;
						address = officialAddress;
						zipCode = officialZipCode;
						postalAddress = officialPostalAddress;
						officalAddress = true;
					}
				}
			}
			
			if (!officalAddress) {
				
				boolean required = fieldsRequired && query.getFieldAddress() == ContactDetailQueryField.REQUIRED;
				
				address = ValidationUtils.validateParameter(qParam + "address", req, required, 0, 255, stringPopulator, validationErrors);
				zipCode = ValidationUtils.validateParameter(qParam + "zipcode", req, required, 0, 10, stringPopulator, validationErrors);
				postalAddress = ValidationUtils.validateParameter(qParam + "postaladdress", req, required, 0, 255, stringPopulator, validationErrors);
				
				if (query.getFieldCareOf() != ContactDetailQueryField.HIDDEN) {
					
					boolean required2 = fieldsRequired && query.getFieldCareOf() == ContactDetailQueryField.REQUIRED;
					careOf = ValidationUtils.validateParameter(qParam + "careof", req, required2, 0, 255, stringPopulator, validationErrors);
				}
			}
		}
		
		if (query.getFieldPhone() != ContactDetailQueryField.HIDDEN) {
			
			boolean required = fieldsRequired && query.getFieldPhone() == ContactDetailQueryField.REQUIRED;
			phone = ValidationUtils.validateParameter(qParam + "phone", req, required, 0, 255, StringSwedishPhoneNumberPopulator.getPopulator(), validationErrors);
		}
		
		if (query.getFieldMobilePhone() != ContactDetailQueryField.HIDDEN) {
			
			boolean required = fieldsRequired && ((query.isAllowSMSNotification() && contactBySMS) || query.getFieldMobilePhone() == ContactDetailQueryField.REQUIRED);
			mobilePhone = ValidationUtils.validateParameter(qParam + "mobilephone", req, required, 0, 255, StringSwedishPhoneNumberPopulator.getPopulator(), validationErrors);
		}
		
		if (query.getFieldEmail() != ContactDetailQueryField.HIDDEN) {
			
			boolean required = fieldsRequired && query.getFieldEmail() == ContactDetailQueryField.REQUIRED;
			email = ValidationUtils.validateParameter(qParam + "email", req, required, 0, 255, EMAIL_POPULATOR, validationErrors);
		}
		
		// Empty for the purposes of checking if the user has entered any information where allowed. The field may still hold non user entered data.
		boolean firstnameEmpty = query.getFieldName() != ContactDetailQueryField.HIDDEN && poster == null && StringUtils.isEmpty(req.getParameter(qParam + "firstname"));
		boolean lastnameEmpty = query.getFieldName() != ContactDetailQueryField.HIDDEN && poster == null && StringUtils.isEmpty(req.getParameter(qParam + "lastname"));
		boolean citizenIDEmpty = query.getFieldCitizenID() != ContactDetailQueryField.HIDDEN && poster == null && StringUtils.isEmpty(req.getParameter(qParam + "citizenID"));
		boolean phoneEmpty = query.getFieldPhone() != ContactDetailQueryField.HIDDEN && StringUtils.isEmpty(req.getParameter(qParam + "phone"));
		boolean mobilePhoneEmpty = query.getFieldMobilePhone() != ContactDetailQueryField.HIDDEN && StringUtils.isEmpty(req.getParameter(qParam + "mobilephone"));
		boolean emailEmpty = query.getFieldEmail() != ContactDetailQueryField.HIDDEN && StringUtils.isEmpty(req.getParameter(qParam + "email"));
		boolean careOfEmpty = query.getFieldAddress() != ContactDetailQueryField.HIDDEN && query.getFieldCareOf() != ContactDetailQueryField.HIDDEN && !officalAddress && StringUtils.isEmpty(req.getParameter(qParam + "careof"));
		boolean addressEmpty = query.getFieldAddress() != ContactDetailQueryField.HIDDEN && !officalAddress && StringUtils.isEmpty(req.getParameter(qParam + "address"));
		boolean zipCodeEmpty = query.getFieldAddress() != ContactDetailQueryField.HIDDEN && !officalAddress && StringUtils.isEmpty(req.getParameter(qParam + "zipcode"));
		boolean postalAddressEmpty = query.getFieldAddress() != ContactDetailQueryField.HIDDEN && !officalAddress && StringUtils.isEmpty(req.getParameter(qParam + "postaladdress"));
		
		if (!fieldsRequired && validationErrors.isEmpty() && firstnameEmpty && lastnameEmpty && citizenIDEmpty && phoneEmpty && mobilePhoneEmpty && emailEmpty && addressEmpty && zipCodeEmpty && postalAddressEmpty) {
			
			queryInstance.fullReset(attributeHandler);
			return;
		}
		
		// If any field has entered information then require all the required fields
		if (!allowPartialPopulation && !(firstnameEmpty && lastnameEmpty && citizenIDEmpty && phoneEmpty && mobilePhoneEmpty && emailEmpty && addressEmpty && zipCodeEmpty && postalAddressEmpty)) {
			
			if (firstnameEmpty && query.getFieldName() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "firstname", ValidationErrorType.RequiredField));
			}
			
			if (lastnameEmpty && query.getFieldName() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "lastname", ValidationErrorType.RequiredField));
			}
			
			if (citizenIDEmpty && query.getFieldCitizenID() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "citizenID", ValidationErrorType.RequiredField));
			}
			
			if (phoneEmpty && query.getFieldPhone() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "phone", ValidationErrorType.RequiredField));
			}
			
			if (mobilePhoneEmpty && query.getFieldMobilePhone() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "mobilephone", ValidationErrorType.RequiredField));
			}
			
			if (emailEmpty && query.getFieldEmail() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "email", ValidationErrorType.RequiredField));
			}
			
			if (careOfEmpty && query.getFieldCareOf() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "careof", ValidationErrorType.RequiredField));
			}
			
			if (addressEmpty && query.getFieldAddress() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "address", ValidationErrorType.RequiredField));
			}
			
			if (zipCodeEmpty && query.getFieldAddress() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "zipcode", ValidationErrorType.RequiredField));
			}
			
			if (postalAddressEmpty && query.getFieldAddress() == ContactDetailQueryField.REQUIRED) {
				validationErrors.add(new ValidationError(qParam + "postaladdress", ValidationErrorType.RequiredField));
			}
		}
		
		if (query.isRequireAtLeastOneContactWay() && query.isAllowSMSNotification() && query.getFieldMobilePhone() != ContactDetailQueryField.HIDDEN && query.getFieldEmail() == ContactDetailQueryField.VISIBLE && !allowPartialPopulation) {
			
			if ((mobilePhoneEmpty && emailEmpty) || (emailEmpty && !mobilePhoneEmpty && !contactBySMS)) {
				
				validationErrors.add(new ValidationError("NoContactChannelChoosen"));
			}
		}
		
		if (fieldsRequired && query.isAllowSMSNotification() && !contactBySMS && emailEmpty) {
			validationErrors.add(new ValidationError("NoContactChannelChoosen"));
		}
		
		if (!validationErrors.isEmpty()) {
			
			throw new ValidationException(validationErrors);
		}
		
		boolean persistUserProfile = false;
		
		if (poster != null) {
			
			if (query.getFieldUpdate() == ContactDetailQueryFieldUpdate.ALWAYS) {
				
				persistUserProfile = true;
				
			} else if (query.getFieldUpdate() == ContactDetailQueryFieldUpdate.ASK) {
				
				persistUserProfile = req.getParameter(qParam + "persistUserProfile") != null;
			}
		}
		
		queryInstance.setContactBySMS(contactBySMS);
		
		queryInstance.setFirstname(firstname);
		queryInstance.setLastname(lastname);
		
		queryInstance.setCareOf(careOf);
		queryInstance.setAddress(address);
		queryInstance.setZipCode(zipCode);
		queryInstance.setPostalAddress(postalAddress);
		
		queryInstance.setMobilePhone(mobilePhone);
		queryInstance.setEmail(email);
		queryInstance.setPhone(phone);
		queryInstance.setPersistUserProfile(persistUserProfile);
		queryInstance.setCitizenID(citizenID);
		queryInstance.getQueryInstanceDescriptor().setPopulated(queryInstance.isPopulated());
		
		if (hasUpdateUserProfileAccess(poster, user, persistUserProfile, requestMetadata, query)) {
			
			MutableUser mutableUser = (MutableUser) poster;
			
			if (query.getFieldEmail() != ContactDetailQueryField.HIDDEN && !emailEmpty) {
				
				mutableUser.setEmail(email);
			}
			
			MutableAttributeHandler userAttributeHandler = mutableUser.getAttributeHandler();
			
			if (userAttributeHandler != null) {
				
				if (query.getFieldAddress() != ContactDetailQueryField.HIDDEN) {
					
					setAttributeValue("address", address, userAttributeHandler);
					setAttributeValue("zipCode", zipCode, userAttributeHandler);
					setAttributeValue("postalAddress", postalAddress, userAttributeHandler);
					
					if (query.getFieldCareOf() != ContactDetailQueryField.HIDDEN) {
						setAttributeValue("careOf", careOf, userAttributeHandler);
					}
				}
				
				if (query.getFieldMobilePhone() != ContactDetailQueryField.HIDDEN) {
					setAttributeValue("mobilePhone", mobilePhone, userAttributeHandler);
				}
				
				if (query.getFieldPhone() != ContactDetailQueryField.HIDDEN) {
					setAttributeValue("phone", phone, userAttributeHandler);
				}
				
				setAttributeValue("contactBySMS", contactBySMS, userAttributeHandler);
			}
			
			if (!validationErrors.isEmpty()) {
				
				throw new ValidationException(validationErrors);
				
			} else {
				
				try {
					
					log.info("User " + user + " updating user profile");
					
					req.getSession(true).setAttribute("user", user);
					
					this.systemInterface.getUserHandler().updateUser(mutableUser, false, false, userAttributeHandler != null);
					
					systemInterface.getEventHandler().sendEvent(User.class, new UserUpdatedEvent(mutableUser), EventTarget.ALL);
					
				} catch (UnableToUpdateUserException e) {
					
					throw new ValidationException(new ValidationError("UnableToUpdateUser"));
				}
			}
		}
	}
	
	private boolean hasUpdateUserProfileAccess(User poster, User user, boolean persistUserProfile, RequestMetadata requestMetadata, ContactDetailQuery query) {

		if(poster != null && poster instanceof MutableUser && persistUserProfile && poster.equals(user) && !requestMetadata.isManager()) {
			
			return true;
			
		} else if(poster != null && poster instanceof MutableUser && persistUserProfile && requestMetadata.getAttributeHandler().getPrimitiveBoolean("AddFlowInstanceAsManager") && query.isManagerUpdateAccess()) {
			
			//Manager adding instance when query isManagerUpdateAccess
			return true;
			
		} else if(poster != null && poster instanceof MutableUser && persistUserProfile && requestMetadata.isManager() && query.isManagerUpdateAccess()) {
			
			//Manager is editing existing instance when isManagerUpdateAccess
			return true;
		}
				
		return false;
	}

	private void setAttributeValue(String name, Object value, MutableAttributeHandler attributeHandler) {
		
		if (value != null) {
			
			attributeHandler.setAttribute(name, value);
			
		} else {
			
			attributeHandler.removeAttribute(name);
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
			
			doc.renameNode(userElement, userElement.getNamespaceURI(), "Poster");
			doc.getDocumentElement().appendChild(userElement);
			
			if (poster.getAttributeHandler() != null) {
				
				XMLUtils.appendNewElement(doc, userElement, "citizenID", CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster));
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
	
	public String getExportCareOf() {
		
		return exportCareOf;
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
	
	public String getExportCitizenID() {
		
		return exportCitizenID;
	}
	
	@Override
	public QueryResponse getFormHTML(ContactDetailQueryInstance queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, RequestMetadata requestMetadata, AttributeHandler attributeHandler) throws TransformerConfigurationException, TransformerException {
		
		ContactDetailQuery query = queryInstance.getQuery();
		
		if (poster != null && poster.equals(user) && !requestMetadata.isManager()) { // Poster is editing
			
			// Update official address when editing saved query instance. No need to update Name and CitizenID as they are overridden in the show XSL and populate code.
			if (query.getFieldAddress() != ContactDetailQueryField.HIDDEN && query.usesOfficalAddress() && poster != null && queryInstance.getQueryInstanceDescriptor().getQueryInstanceID() != null) {
				
				queryInstance.updateOfficialAddress(poster);
			}
		}
		
		queryInstance.setMutableUserWithAccess(poster instanceof MutableUser && poster.equals(user));
		
		return super.getFormHTML(queryInstance, req, user, poster, validationErrors, enableAjaxPosting, queryRequestURL, requestMetadata, attributeHandler);
	}
	
}
