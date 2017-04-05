package com.nordicpeak.flowengine.queries.childquery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.object.ObjectUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.populators.StringSwedishPhoneNumberPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.childrelationprovider.Child;
import com.nordicpeak.childrelationprovider.ChildRelationProvider;
import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;
import com.nordicpeak.childrelationprovider.exceptions.CommunicationException;
import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MultiSigningQueryProvider;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildAlternative;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQueryInstance;
import com.nordicpeak.flowengine.queries.childquery.beans.StoredChild;
import com.nordicpeak.flowengine.queries.childquery.beans.StoredGuardian;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class ChildQueryProviderModule extends BaseQueryProviderModule<ChildQueryInstance> implements BaseQueryCRUDCallback, MultiSigningQueryProvider {

	private static final String GET_OTHER_PARTIES_SQL = "SELECT child_query_guardians.queryInstanceID FROM child_query_guardians\n" +
			"INNER JOIN child_query_instances ON child_query_guardians.queryInstanceID = child_query_instances.queryInstanceID\n" +
			"INNER JOIN child_queries ON child_query_instances.queryID = child_queries.queryID\n" +
			"WHERE child_queries.useMultipartSigning = true AND child_query_guardians.poster = false AND child_query_guardians.citizenIdentifier = ?;";
	
	@XSLVariable(prefix = "java.")
	protected String alternativeName = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportChildCitizenName= "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportChildCitizenIdentifier = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportChildAdress = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportChildPostalAdress = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportChildZipCode = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportOtherGuardianName = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportOtherGuardianEmail = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportOtherGuardianPhone = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportOtherGuardianCitizenIdentifier = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportOtherGuardianAdress = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportOtherGuardianPostalAdress = "This variable should be set by your stylesheet";
	
	@XSLVariable(prefix = "java.")
	protected String exportOtherGuardianZipCode = "This variable should be set by your stylesheet";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User citizen identifier attribute", description = "Attribute to get citizen identifier from", required = true)
	private String citizenIdentifierAttribute = "citizenIdentifier";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User phone attribute", description = "Attribute to get phone from", required = true)
	private String phoneAttribute = "mobilePhone";

	@InstanceManagerDependency(required = true)
	private ChildRelationProvider childRelationProvider;

	private AnnotatedDAO<ChildQuery> queryDAO;
	private AnnotatedDAO<ChildQueryInstance> queryInstanceDAO;

	private ChildQueryCRUD queryCRUD;

	private QueryParameterFactory<ChildQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<ChildQueryInstance, Integer> queryInstanceIDParamFactory;

	private ChildAlternative alternative;
	
	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.ERROR);

		alternative = new ChildAlternative(alternativeName);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, ChildQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(ChildQuery.class);
		queryInstanceDAO = daoFactory.getDAO(ChildQueryInstance.class);

		queryCRUD = new ChildQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<ChildQuery>(ChildQuery.class), "ChildQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		ChildQuery query = new ChildQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setAlternative(alternative);

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ChildQuery query = new ChildQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor) throws SQLException {

		ChildQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setAlternative(alternative);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ChildQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setAlternative(alternative);

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		ChildQueryInstance queryInstance;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new ChildQueryInstance();

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

		if (req != null) {

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}

		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		//If this is a new query instance copy the default values
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance.defaultQueryValues();

			if (poster != null) {

				queryInstance.setChildren(getChildrenWithGuardians(queryInstance, poster, null));
			}
		}

		return queryInstance;
	}

	@Override
	public QueryResponse getFormHTML(ChildQueryInstance queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, RequestMetadata requestMetadata, AttributeHandler attributeHandler) throws TransformerConfigurationException, TransformerException {

		if (queryInstance.getChildren() == null) {

			queryInstance.setChildren(getChildrenWithGuardians(queryInstance, poster, requestMetadata));
		}

		return super.getFormHTML(queryInstance, req, user, poster, validationErrors, enableAjaxPosting, queryRequestURL, requestMetadata, attributeHandler);
	}

	private ChildQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<ChildQuery> query = new HighLevelQuery<ChildQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private ChildQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<ChildQuery> query = new HighLevelQuery<ChildQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private ChildQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<ChildQueryInstance> query = new HighLevelQuery<ChildQueryInstance>(ChildQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(ChildQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}

	@Override
	public void populate(ChildQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		Integer queryID = queryInstance.getQuery().getQueryID();

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String childCitizenIdentifier = ValidationUtils.validateParameter("q" + queryID + "_child", req, false, StringPopulator.getPopulator(), validationErrors);

		if (!allowPartialPopulation && childCitizenIdentifier == null) {
			validationErrors.add(new ValidationError("Required"));
		}

		StoredChild selectedChild = null;

		if (childCitizenIdentifier != null) {

			if (!CollectionUtils.isEmpty(queryInstance.getChildren())) {

				selectedChild = queryInstance.getChildren().get(childCitizenIdentifier);

			} else if (queryInstance.getChildren() == null && childCitizenIdentifier.equals(queryInstance.getCitizenIdentifier())) {

				selectedChild = new StoredChild(queryInstance.getFirstname(), queryInstance.getLastname(), queryInstance.getCitizenIdentifier());
			}

			if (selectedChild == null || (queryInstance.getQuery().getMinAge() != null && selectedChild.getAge() < queryInstance.getQuery().getMinAge()) || (queryInstance.getQuery().getMaxAge() != null && selectedChild.getAge() > queryInstance.getQuery().getMaxAge())) {
				validationErrors.add(new ValidationError("InvalidFormat"));
			}
		}

		if (queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.VISIBLE_REQUIRED && ObjectUtils.isNull(selectedChild)) {

			queryInstance.reset(attributeHandler);
			return;
		}

		if (!validationErrors.isEmpty()) {

			throw new ValidationException(validationErrors);
		}

		List<StoredGuardian> storedGuardians = null;

		if (selectedChild != null) {

			if (selectedChild.getGuardians() != null) {

				storedGuardians = selectedChild.getGuardians();

			} else {

				storedGuardians = queryInstance.getGuardians();
			}

			if (queryInstance.getQuery().isUseMultipartSigning()) {

				String posterCitizienIdentifier = user.getAttributeHandler().getString(citizenIdentifierAttribute);

				for (StoredGuardian storedGuardian : storedGuardians) {

					if (storedGuardian.getCitizenIdentifier() == null) {

						validationErrors.add(new ValidationError("SecretGuardian"));

					} else if (storedGuardian.getCitizenIdentifier().equals(posterCitizienIdentifier)) {

						storedGuardian.setPoster(true);
						storedGuardian.setEmail(user.getEmail());
						storedGuardian.setPhone(user.getAttributeHandler().getString(phoneAttribute));

					} else {

						String emailID = "q" + queryID + "_guardian_" + storedGuardian.getCitizenIdentifier() + "_email";
						String phoneID = "q" + queryID + "_guardian_" + storedGuardian.getCitizenIdentifier() + "_phone";

						String email = ValidationUtils.validateParameter(emailID, req, queryInstance.getQuery().isRequireGuardianEmail() && !allowPartialPopulation, EmailPopulator.getPopulator(), validationErrors);
						String phone = ValidationUtils.validateParameter(phoneID, req, queryInstance.getQuery().isRequireGuardianPhone() && !allowPartialPopulation, StringSwedishPhoneNumberPopulator.getPopulator(), validationErrors);

						storedGuardian.setPoster(false);
						storedGuardian.setEmail(email);
						storedGuardian.setPhone(phone);

						if (!allowPartialPopulation && !queryInstance.getQuery().isRequireGuardianEmail() && !queryInstance.getQuery().isRequireGuardianPhone() && storedGuardian.getEmail() == null && storedGuardian.getPhone() == null && StringUtils.isEmpty(req.getParameter(emailID)) && StringUtils.isEmpty(req.getParameter(phoneID))) {

							validationErrors.add(new ValidationError("EmailOrPhoneRequired"));

						} else if (queryInstance.getQuery().isRequireGuardianContactInfoVerification()) {

							String emailID2 = "q" + queryID + "_guardian_" + storedGuardian.getCitizenIdentifier() + "_email2";
							String phoneID2 = "q" + queryID + "_guardian_" + storedGuardian.getCitizenIdentifier() + "_phone2";

							String email2 = ValidationUtils.validateParameter(emailID2, req, queryInstance.getQuery().isRequireGuardianEmail() && !allowPartialPopulation, EmailPopulator.getPopulator(), validationErrors);
							String phone2 = ValidationUtils.validateParameter(phoneID2, req, queryInstance.getQuery().isRequireGuardianPhone() && !allowPartialPopulation, StringSwedishPhoneNumberPopulator.getPopulator(), validationErrors);

							if (((email == null && email2 != null) || (email != null && !email.equals(email2))) && !ValidationUtils.containsValidationErrorForField(emailID2, validationErrors)) {

								validationErrors.add(new ValidationError("EmailVerificationMismatch", "", emailID2));
							}

							if (((phone == null && phone2 != null) || (phone != null && !phone.equals(phone2))) && !ValidationUtils.containsValidationErrorForField(phoneID2, validationErrors)) {

								validationErrors.add(new ValidationError("PhoneVerificationMismatch", "", phoneID2));
							}
						}
					}
				}
			}

			if (!validationErrors.isEmpty()) {

				throw new ValidationException(validationErrors);
			}

			queryInstance.setCitizenIdentifier(selectedChild.getCitizenIdentifier());
			queryInstance.setFirstname(selectedChild.getFirstname());
			queryInstance.setLastname(selectedChild.getLastname());
			queryInstance.setAddress(selectedChild.getAddress());
			queryInstance.setZipcode(selectedChild.getZipcode());
			queryInstance.setPostalAddress(selectedChild.getPostalAddress());

			if (queryInstance.getQuery().isUseMultipartSigning() || queryInstance.getQuery().isAlwaysShowOtherGuardians()) {

				queryInstance.setGuardians(storedGuardians);
			}

			queryInstance.getQueryInstanceDescriptor().setPopulated(true);

			queryInstance.setAttributes(attributeHandler);

		} else {

			//Query not populated
			queryInstance.reset(attributeHandler);
			queryInstance.getQueryInstanceDescriptor().setPopulated(false);
		}
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		ChildQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ChildQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

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

		ChildQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		queryDAO.add(query, transactionHandler, null);
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, ChildQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler)));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<ChildQueryInstance> getQueryInstanceClass() {

		return ChildQueryInstance.class;
	}

	private Map<String, StoredChild> getChildrenWithGuardians(ChildQueryInstance queryInstance, User poster, RequestMetadata requestMetadata) {
		
		if (poster != null) {
			
			String citizenIdentifier = poster.getAttributeHandler().getString(citizenIdentifierAttribute);
			
			if (citizenIdentifier != null) {
				
				if (requestMetadata != null && requestMetadata.isManager() && queryInstance.getCitizenIdentifier() != null) {
					return null;
				}
				
				if (childRelationProvider == null) {
					
					log.error("Missing required instance manager dependency: childRelationProvider");
					queryInstance.setFetchChildrenException(new CommunicationException("Missing required instance manager dependency: childRelationProvider"));
					return null;
				}
				
				log.info("Getting children information for user " + poster);
				
				try {
					Map<String, Child> childMap = childRelationProvider.getChildrenWithGuardians(citizenIdentifier);
					
					if (childMap != null) {
						
						Map<String, StoredChild> storedChildMap = new HashMap<String, StoredChild>();
						
						for (Entry<String, Child> entry : childMap.entrySet()) {
							
							storedChildMap.put(entry.getKey(), new StoredChild(entry.getValue()));
						}
						
						return storedChildMap;
					}
					
				} catch (ChildRelationProviderException e) {
					
					queryInstance.setFetchChildrenException(e);
				}
			}
		}
		
		return null;
	}

	@Override
	public Document createDocument(HttpServletRequest req, User poster) {

		Document doc = super.createDocument(req, poster);

		if (poster != null) {

			Element userElement = poster.toXML(doc);

			doc.getDocumentElement().appendChild(userElement);

			if (poster.getAttributeHandler() != null) {

				XMLUtils.appendNewElement(doc, userElement, "SocialSecurityNumber", poster.getAttributeHandler().getString("citizenIdentifier"));
			}
		}

		return doc;
	}

	@Override
	public List<Integer> getQueryInstanceIDs(String citizenIdentifier) throws SQLException {
		
		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(queryInstanceDAO.getDataSource(), GET_OTHER_PARTIES_SQL, IntegerPopulator.getPopulator());
		
		query.setString(1, citizenIdentifier);
		
		return query.executeQuery();
	}

	public List<String> getExportLabels(ChildQuery query) {
		
		List<String> labels = new ArrayList<String>();
		
		labels.add(exportChildCitizenName);
		labels.add(exportChildCitizenIdentifier);
		
		if (query.isShowAddress()) {
			
			labels.add(exportChildAdress);
			labels.add(exportChildPostalAdress);
			labels.add(exportChildZipCode);
		}
		
		labels.add(exportOtherGuardianName);
		labels.add(exportOtherGuardianCitizenIdentifier);
		labels.add(exportOtherGuardianEmail);
		labels.add(exportOtherGuardianPhone);
		
		if (query.isShowGuardianAddress()) {
			
			labels.add(exportOtherGuardianAdress);
			labels.add(exportOtherGuardianPostalAdress);
			labels.add(exportOtherGuardianZipCode);
		}
		
		return labels;
	}
}
