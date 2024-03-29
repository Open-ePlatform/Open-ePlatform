package com.nordicpeak.flowengine.queries.childquery;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkProvider;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.MySQLRowLimiter;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.date.PooledSimpleDateFormat;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.populators.StringSwedishPhoneNumberPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xsl.XSLVariableReader;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.childrelationprovider.Child;
import com.nordicpeak.childrelationprovider.ChildRelationProvider;
import com.nordicpeak.childrelationprovider.ChildrenResponse;
import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;
import com.nordicpeak.childrelationprovider.exceptions.CommunicationException;
import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.SimpleImmutableAlternative;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MultiSigningQueryProvider;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryContentFilter;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQueryInstance;
import com.nordicpeak.flowengine.queries.childquery.beans.StoredChild;
import com.nordicpeak.flowengine.queries.childquery.beans.StoredGuardian;
import com.nordicpeak.flowengine.queries.childquery.filterapi.FilterAPIChild;
import com.nordicpeak.flowengine.queries.childquery.filterapi.IncompleteFilterAPIDataException;
import com.nordicpeak.flowengine.queries.childquery.interfaces.ChildQueryFilterEndpoint;
import com.nordicpeak.flowengine.queries.childquery.interfaces.ChildQueryFilterProvider;
import com.nordicpeak.flowengine.utils.CitizenIdentifierUtils;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class ChildQueryProviderModule extends BaseQueryProviderModule<ChildQueryInstance> implements BaseQueryCRUDCallback, MultiSigningQueryProvider, ExtensionLinkProvider {

	private static final String SESSION_TEST_CHILDREN = "childQueryTestChildren";

	public static final PooledSimpleDateFormat CITIZEN_ID_DATE_FORMATTER = new PooledSimpleDateFormat("yyyyMMdd");

	private static final String GET_OTHER_PARTIES_SQL = "SELECT child_query_guardians.queryInstanceID FROM child_query_guardians\n" + "INNER JOIN child_query_instances ON child_query_guardians.queryInstanceID = child_query_instances.queryInstanceID\n" + "INNER JOIN child_queries ON child_query_instances.queryID = child_queries.queryID\n" + "WHERE child_queries.useMultipartSigning = true AND child_query_guardians.poster = false AND child_query_guardians.citizenIdentifier = ?;";

	@XSLVariable(prefix = "java.")
	protected String testChildrenMenuName = "This variable should be set by your stylesheet";

	@XSLVariable(prefix = "java.")
	protected String childSelectedAlternativeName = "This variable should be set by your stylesheet";

	@XSLVariable(prefix = "java.")
	protected String singleGuardianAlternativeName = "This variable should be set by your stylesheet";

	@XSLVariable(prefix = "java.")
	protected String multiGuardianAlternativeName = "This variable should be set by your stylesheet";

	@XSLVariable(prefix = "java.")
	protected String exportChildCitizenName = "This variable should be set by your stylesheet";

	@XSLVariable(prefix = "java.")
	protected String exportChildCitizenFirstName = "This variable should be set by your stylesheet";

	@XSLVariable(prefix = "java.")
	protected String exportChildCitizenLastName = "This variable should be set by your stylesheet";

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

	protected String communicationErrorDescription;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User phone attribute", description = "Attribute to get phone from", required = true)
	private String phoneAttribute = "mobilePhone";

	@InstanceManagerDependency(required = true)
	private ChildRelationProvider childRelationProvider;

	protected StaticContentModule staticContentModule;

	private AnnotatedDAO<ChildQuery> queryDAO;
	private AnnotatedDAO<ChildQueryInstance> queryInstanceDAO;
	private AnnotatedDAOWrapper<QueryDescriptor, Integer> queryDescriptorDAOWrapper;

	private ChildQueryCRUD queryCRUD;

	private QueryParameterFactory<ChildQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<ChildQuery, String> queryFilterEndpointNameParamFactory;
	private QueryParameterFactory<ChildQueryInstance, Integer> queryInstanceIDParamFactory;

	private ImmutableAlternative childSelectedAlternative;
	private ImmutableAlternative singleGuardianAlternative;
	private ImmutableAlternative multiGuardianAlternative;

	protected ExtensionLink flowListExtensionLink;

	protected CopyOnWriteArrayList<ChildQueryFilterProvider> filterProviders = new CopyOnWriteArrayList<>();

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(ChildQueryProviderModule.class, this)) {

			throw new RuntimeException("Unable to register module " + moduleDescriptor + " in global instance handler using key " + ChildQueryProviderModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.ERROR);

		childSelectedAlternative = new SimpleImmutableAlternative(childSelectedAlternativeName, 1, 1);
		singleGuardianAlternative = new SimpleImmutableAlternative(singleGuardianAlternativeName, 2, 2);
		multiGuardianAlternative = new SimpleImmutableAlternative(multiGuardianAlternativeName, 3, 3);
	}

	@Override
	protected XSLVariableReader parseQueryXSLStyleSheet(URL styleSheetURL) {

		XSLVariableReader variableReader = super.parseQueryXSLStyleSheet(styleSheetURL);

		if (variableReader != null) {
			try {
				communicationErrorDescription = variableReader.getValue("i18n.Error.Provider.CommunicationError");

			} catch (Exception e) {

				log.error("Unable to get i18n.Error.Provider.CommunicationError from query style sheet " + queryStyleSheet, e);
			}
		}

		return variableReader;
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

		queryCRUD = new ChildQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<>(ChildQuery.class), "ChildQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryFilterEndpointNameParamFactory = queryDAO.getParamFactory("filterEndpoint", String.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);

		FlowEngineDAOFactory flowDAOFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		queryDescriptorDAOWrapper = flowDAOFactory.getQueryDescriptorDAO().getWrapper(Integer.class);
		queryDescriptorDAOWrapper.addRelations(QueryDescriptor.STEP_RELATION, Step.FLOW_RELATION, Flow.FLOW_TYPE_RELATION, Flow.CATEGORY_RELATION);
		queryDescriptorDAOWrapper.setUseRelationsOnGet(true);
	}

	@InstanceManagerDependency(required = true)
	public void setStaticContentModule(StaticContentModule staticContentModule) {

		generateExtensionLinks(staticContentModule);

		this.staticContentModule = staticContentModule;
	}

	@Override
	@InstanceManagerDependency
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) throws SQLException {

		if (flowAdminModule != null) {

			flowAdminModule.addFlowListExtensionLinkProvider(this);

		} else if (this.flowAdminModule != null) {

			this.flowAdminModule.removeFlowListExtensionLinkProvider(this);
		}

		super.setFlowAdminModule(flowAdminModule);
	}

	@Override
	public void unload() throws Exception {

		if (flowAdminModule != null) {

			flowAdminModule.removeFlowListExtensionLinkProvider(this);
		}

		systemInterface.getInstanceHandler().removeInstance(ChildQueryProviderModule.class, this);

		super.unload();
	}

	private void generateExtensionLinks(StaticContentModule staticContentModule) {

		if (staticContentModule != null) {

			flowListExtensionLink = new ExtensionLink(testChildrenMenuName, systemInterface.getContextPath() + getFullAlias() + "/testchildren", staticContentModule.getModuleContentURL(moduleDescriptor) + "/pics/group.png", "bottom-right");

		} else {

			flowListExtensionLink = null;
		}
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		ChildQuery query = new ChildQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setChildSelectedAlternative(childSelectedAlternative);
		query.setSingleGuardianAlternative(singleGuardianAlternative);
		query.setMultiGuardianAlternative(multiGuardianAlternative);

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap, QueryContentFilter contentFilter) throws Throwable {

		ChildQuery query = new ChildQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		contentFilter.filterHTML(query);

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

		ChildQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setChildSelectedAlternative(childSelectedAlternative);
		query.setSingleGuardianAlternative(singleGuardianAlternative);
		query.setMultiGuardianAlternative(multiGuardianAlternative);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ChildQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setChildSelectedAlternative(childSelectedAlternative);
		query.setSingleGuardianAlternative(singleGuardianAlternative);
		query.setMultiGuardianAlternative(multiGuardianAlternative);

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

		queryInstance.getQuery().setChildSelectedAlternative(childSelectedAlternative);
		queryInstance.getQuery().setSingleGuardianAlternative(singleGuardianAlternative);
		queryInstance.getQuery().setMultiGuardianAlternative(multiGuardianAlternative);

		if (req != null) {

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}

		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		//If this is a new query instance copy the default values
		if (descriptor.getQueryInstanceID() == null) {

			//Only do if query is visible
			if (poster != null && descriptor.getQueryState() != QueryState.HIDDEN) {

				queryInstance.setChildren(getChildrenWithGuardians(queryInstance, poster, null, null));
			}

		} else {

			if (queryInstance.getGuardians() != null) {

				for (StoredGuardian guardian : queryInstance.getGuardians()) {

					if (guardian.isPoster()) {

						for (StoredGuardian guardian2 : queryInstance.getGuardians()) {

							if (guardian2 != guardian && guardian2.addressEquals(guardian)) {

								guardian2.setSameAddressAsPoster(true);
							}
						}

						break;
					}
				}
			}
		}

		return queryInstance;
	}

	@Override
	public QueryResponse getFormHTML(ChildQueryInstance queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, InstanceRequestMetadata requestMetadata, AttributeHandler attributeHandler) throws Throwable {

		if (queryInstance.getChildren() == null) {

			queryInstance.setChildren(getChildrenWithGuardians(queryInstance, poster, requestMetadata, attributeHandler));
		}

		return super.getFormHTML(queryInstance, req, user, poster, validationErrors, enableAjaxPosting, queryRequestURL, requestMetadata, attributeHandler);
	}

	@Override
	protected void appendQueryInstance(ChildQueryInstance queryInstance, Document doc, Element targetElement, AttributeHandler attributeHandler) {

		super.appendQueryInstance(queryInstance, doc, targetElement, attributeHandler);

		ChildQuery query = queryInstance.getQuery();

		if (query.getFilterEndpointName() != null) {

			ChildQueryFilterEndpoint filterEndpoint = getFilterEndpoint(query.getFilterEndpointName());

			if (filterEndpoint != null) {

				targetElement.appendChild(filterEndpoint.toXML(doc));
			}
		}
	}

	private ChildQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<ChildQuery> query = new HighLevelQuery<>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private ChildQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<ChildQuery> query = new HighLevelQuery<>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private ChildQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<ChildQueryInstance> query = new HighLevelQuery<>(ChildQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(ChildQueryInstance queryInstance, TransactionHandler transactionHandler, InstanceRequestMetadata requestMetadata) throws Throwable {

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}

	@Override
	public void populate(ChildQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, InstanceRequestMetadata requestMetadata) throws ValidationException {

		Integer queryID = queryInstance.getQuery().getQueryID();

		List<ValidationError> validationErrors = new ArrayList<>();

		if (requestMetadata != null && requestMetadata.isManager() && queryInstance.getCitizenIdentifier() != null) {

			populateGuardians(queryInstance.getGuardians(), queryInstance, req, poster, allowPartialPopulation, requestMetadata, validationErrors);

			if (!validationErrors.isEmpty()) {
				throw new ValidationException(validationErrors);
			}

			queryInstance.getQueryInstanceDescriptor().setPopulated(true);
			queryInstance.setAttributes(attributeHandler);

			return;
		}

		String childCitizenIdentifier = ValidationUtils.validateParameter("q" + queryID + "_child", req, false, StringPopulator.getPopulator(), validationErrors);

		if (childCitizenIdentifier == null && queryInstance.getQueryInstanceDescriptor().getQueryState() != QueryState.VISIBLE_REQUIRED) {
			queryInstance.reset(attributeHandler);
			return;
		}

		if (!allowPartialPopulation && childCitizenIdentifier == null) {
			validationErrors.add(new ValidationError("Required"));
		}

		StoredChild selectedChild = null;

		if (childCitizenIdentifier != null) {

			if (!CollectionUtils.isEmpty(queryInstance.getChildren())) {
				selectedChild = queryInstance.getChildren().get(childCitizenIdentifier);
			}

			if (selectedChild == null || (queryInstance.getQuery().getMinAge() != null && selectedChild.getAge() < queryInstance.getQuery().getMinAge()) || (queryInstance.getQuery().getMaxAge() != null && selectedChild.getAge() > queryInstance.getQuery().getMaxAge())) {
				validationErrors.add(new ValidationError("InvalidFormat"));
			}
		}

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		if (selectedChild != null) {

			List<StoredGuardian> storedGuardians = populateGuardians(selectedChild.getGuardians(), queryInstance, req, poster, allowPartialPopulation, requestMetadata, validationErrors);

			if (!validationErrors.isEmpty()) {
				throw new ValidationException(validationErrors);
			}

			queryInstance.setCitizenIdentifier(selectedChild.getCitizenIdentifier());
			queryInstance.setFirstname(selectedChild.getFirstname());
			queryInstance.setLastname(selectedChild.getLastname());
			queryInstance.setAddress(selectedChild.getAddress());
			queryInstance.setZipcode(selectedChild.getZipcode());
			queryInstance.setPostalAddress(selectedChild.getPostalAddress());
			queryInstance.setMunicipalityCode(selectedChild.getMunicipalityCode());
			queryInstance.setAddressUUID(selectedChild.getAddressUUID());
			queryInstance.setTestChild(selectedChild.isTestChild());
			queryInstance.setChildAttributes(selectedChild.getAttributes());

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

	private List<StoredGuardian> populateGuardians(List<StoredGuardian> storedGuardians, ChildQueryInstance queryInstance, HttpServletRequest req, User poster, boolean allowPartialPopulation, RequestMetadata requestMetadata, List<ValidationError> validationErrors) {

		ChildQuery query = queryInstance.getQuery();
		Integer queryID = query.getQueryID();
		String posterCitizienIdentifier = CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster);

		if (query.isUseMultipartSigning()) {

			for (StoredGuardian storedGuardian : storedGuardians) {

				if (storedGuardian.getCitizenIdentifier() == null) {

					validationErrors.add(new ValidationError("Provider.IncompleteData"));

				} else if (storedGuardian.isPoster()) {

					storedGuardian.setEmail(poster.getEmail());
						storedGuardian.setPhone(poster.getAttributeHandler().getString(phoneAttribute));

				} else {

					if (query.isSkipMultipartSigningIfSameAddress() && storedGuardian.isSameAddressAsPoster()) {
						continue;
					}

					String emailID = "q" + queryID + "_guardian_" + storedGuardian.getCitizenIdentifier() + "_email";
					String phoneID = "q" + queryID + "_guardian_" + storedGuardian.getCitizenIdentifier() + "_phone";

					String email = ValidationUtils.validateParameter(emailID, req, query.isRequireGuardianEmail() && !allowPartialPopulation, 0, 255 , EmailPopulator.getPopulator(), validationErrors);
					String phone = ValidationUtils.validateParameter(phoneID, req, query.isRequireGuardianPhone() && !allowPartialPopulation, StringSwedishPhoneNumberPopulator.getPopulator(), validationErrors);

					storedGuardian.setEmail(email);
					storedGuardian.setPhone(phone);

					if (!allowPartialPopulation && !query.isRequireGuardianEmail() && !query.isRequireGuardianPhone() && storedGuardian.getEmail() == null && storedGuardian.getPhone() == null && StringUtils.isEmpty(req.getParameter(emailID)) && StringUtils.isEmpty(req.getParameter(phoneID))) {

						validationErrors.add(new ValidationError("EmailOrPhoneRequired"));

					} else if (query.isRequireGuardianContactInfoVerification() && !requestMetadata.isManager()) {

						String emailID2 = "q" + queryID + "_guardian_" + storedGuardian.getCitizenIdentifier() + "_email2";
						String phoneID2 = "q" + queryID + "_guardian_" + storedGuardian.getCitizenIdentifier() + "_phone2";

						String email2 = ValidationUtils.validateParameter(emailID2, req, query.isRequireGuardianEmail() && !allowPartialPopulation, 0, 255, EmailPopulator.getPopulator(), validationErrors);
						String phone2 = ValidationUtils.validateParameter(phoneID2, req, query.isRequireGuardianPhone() && !allowPartialPopulation, StringSwedishPhoneNumberPopulator.getPopulator(), validationErrors);

						if (((email == null && email2 != null) || (email != null && !email.equals(email2))) && !ValidationUtils.containsValidationErrorForField(emailID2, validationErrors)) {

							validationErrors.add(new ValidationError("EmailVerificationMismatch", "", emailID2));
						}

						if (((phone == null && phone2 != null) || (phone != null && !phone.equals(phone2))) && !ValidationUtils.containsValidationErrorForField(phoneID2, validationErrors)) {

							validationErrors.add(new ValidationError("PhoneVerificationMismatch", "", phoneID2));
						}
					}
				}
			}

		} else if (query.isAlwaysShowOtherGuardians()) {

			if(posterCitizienIdentifier != null) {
			
				for (StoredGuardian storedGuardian : storedGuardians) {
	
					storedGuardian.setPoster(posterCitizienIdentifier.equals(storedGuardian.getCitizenIdentifier()));
				}
			}
		}

		return storedGuardians;
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
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws SQLException {

		ChildQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		queryDAO.add(query, transactionHandler, null);
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, ChildQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler), systemInterface.getEncoding()));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<ChildQueryInstance> getQueryInstanceClass() {

		return ChildQueryInstance.class;
	}

	private Map<String, StoredChild> getChildrenWithGuardians(ChildQueryInstance queryInstance, User poster, RequestMetadata requestMetadata, AttributeHandler attributeHandler) {

		ChildQuery query = queryInstance.getQuery();

		if (poster != null) {

			if ((requestMetadata == null || !requestMetadata.isManager()) && SessionUtils.getAttribute(SESSION_TEST_CHILDREN, poster.getSession()) != null) {

				queryInstance.setHasChildrenUnderSecrecy(false);

				@SuppressWarnings("unchecked")
				Map<String, StoredChild> storedChildMap = (Map<String, StoredChild>) SessionUtils.getAttribute(SESSION_TEST_CHILDREN, poster.getSession());

				setGuardianPosterAndSameAddress(CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster), storedChildMap);

				return storedChildMap;
			}

			if (requestMetadata != null && requestMetadata.isManager() && queryInstance.getCitizenIdentifier() != null) {
				return null;
			}

			String citizenIdentifier = CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster);

			if (citizenIdentifier != null) {

				if (childRelationProvider == null) {

					log.error("Missing required instance manager dependency: childRelationProvider");
					queryInstance.setFetchChildrenException(new CommunicationException("Missing required instance manager dependency: childRelationProvider"));
					return null;
				}

				log.info("Getting children information for user " + poster);

				queryInstance.setFilteredChildrenText(null);
				queryInstance.setAgeFilteredChildren(false);

				try {
					ChildrenResponse childrenResponse = childRelationProvider.getChildren(citizenIdentifier, true, query.isUseMultipartSigning());

					if (childrenResponse != null) {

						queryInstance.setHasChildrenUnderSecrecy(childrenResponse.hasChildrenUnderSecrecy());

						Map<String, StoredChild> storedChildMap = new LinkedHashMap<>();
						Map<String, Child> navetChildMap = childrenResponse.getChildren();

						if (query.getFilterEndpointName() != null) {

							ChildQueryFilterEndpoint filterEndpoint = getFilterEndpoint(query.getFilterEndpointName());

							if (filterEndpoint == null) {

								log.warn("Missing required filter endpoint: " + query.getFilterEndpointName());
								queryInstance.setFetchChildrenException(new CommunicationException("Missing required filter endpoint: " + query.getFilterEndpointName()));
								return null;
							}

							if (query.getMinAge() != null || query.getMaxAge() != null) {
								for (Iterator<Child> it = navetChildMap.values().iterator(); it.hasNext();) {

									Child child = it.next();

									int age = StoredChild.getAge(child.getCitizenIdentifier());

									if ((query.getMinAge() != null && age < query.getMinAge()) || (query.getMaxAge() != null && age > query.getMaxAge())) {

										log.info("Removing child " + child + " before filter endpoint due to age " + age + " outside limits " + query.getMinAge() + "-" + query.getMaxAge());
										it.remove();
										queryInstance.setAgeFilteredChildren(true);
									}
								}
							}

							ImmutableFlow flow = queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getStep().getFlow();

							Map<String, FilterAPIChild> filterChildren = filterEndpoint.getChildren(navetChildMap, poster, citizenIdentifier, flow, attributeHandler);

							if (filterChildren == null) {

								queryInstance.setFetchChildrenException(new CommunicationException("Got no response from filtering API: " + filterEndpoint));
								return null;

							} else if (filterChildren.isEmpty()) {

								queryInstance.setFetchChildrenException(new IncompleteFilterAPIDataException("Empty response from filtering API: " + filterEndpoint));
								return null;
							}

							StringBuilder filteredChildrenNames = null;

							if (query.getFilteredChildrenDescription() != null) {
								filteredChildrenNames = new StringBuilder();
							}

							for (Entry<String, Child> entry : navetChildMap.entrySet()) {

								FilterAPIChild filterChild = filterChildren.get(entry.getKey());

								if (filterChild == null) {

									if (filteredChildrenNames != null) {

										if (filteredChildrenNames.length() > 0) {
											filteredChildrenNames.append(", ");
										}

										filteredChildrenNames.append(entry.getValue().getFirstname() + " " + entry.getValue().getLastname());
									}

									continue;
								}

								StoredChild child = new StoredChild(entry.getValue());
								child.setAttributes(filterChild.getFields());

								storedChildMap.put(entry.getKey(), child);
							}

							if (filteredChildrenNames != null && filteredChildrenNames.length() > 0) {
								queryInstance.setFilteredChildrenText(query.getFilteredChildrenDescription().replace("$filteredChildren", filteredChildrenNames.toString()));
							}

							if (storedChildMap.isEmpty()) {

								queryInstance.setFetchChildrenException(new IncompleteFilterAPIDataException("No remaining children after filtering API: " + filterEndpoint));
								return null;
							}

						} else {

							for (Entry<String, Child> entry : navetChildMap.entrySet()) {

								storedChildMap.put(entry.getKey(), new StoredChild(entry.getValue()));
							}
						}

						setGuardianPosterAndSameAddress(citizenIdentifier, storedChildMap);

						return storedChildMap;

					} else {

						queryInstance.setHasChildrenUnderSecrecy(false);
					}

				} catch (ChildRelationProviderException e) {

					queryInstance.setFetchChildrenException(e);
				}
			}
		}

		return null;
	}

	private void setGuardianPosterAndSameAddress(String posterCitizenIdentifier, Map<String, StoredChild> storedChildMap) {

		for (StoredChild child : storedChildMap.values()) {

			if(child.getGuardians() == null) {
				
				log.warn("No guardians found for child " + child);
				
				continue;
			}
			
			for (StoredGuardian guardian : child.getGuardians()) {

				if (posterCitizenIdentifier != null && posterCitizenIdentifier.equals(guardian.getCitizenIdentifier())) {

					guardian.setPoster(true);

					for (StoredGuardian guardian2 : child.getGuardians()) {

						if (guardian2 != guardian && guardian2.addressEquals(guardian)) {

							guardian2.setSameAddressAsPoster(true);
						}
					}

					break;
				}
			}
		}
	}

	@Override
	public Document createDocument(HttpServletRequest req, User poster) {

		Document doc = super.createDocument(req, poster);

		if (poster != null) {

			Element userElement = poster.toXML(doc);

			doc.getDocumentElement().appendChild(userElement);

			if (poster.getAttributeHandler() != null) {

				XMLUtils.appendNewElement(doc, userElement, "SocialSecurityNumber", CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(poster));
			}
		}

		return doc;
	}

	@Override
	public List<Integer> getQueryInstanceIDs(String citizenIdentifier) throws SQLException {

		ArrayListQuery<Integer> query = new ArrayListQuery<>(queryInstanceDAO.getDataSource(), GET_OTHER_PARTIES_SQL, IntegerPopulator.getPopulator());

		query.setString(1, citizenIdentifier);

		return query.executeQuery();
	}

	public List<String> getExportLabels(ChildQuery query) {

		List<String> labels = new ArrayList<>();

		labels.add(exportChildCitizenName);
		labels.add(exportChildCitizenIdentifier);

		if (query.isShowAddress()) {

			labels.add(exportChildAdress);
			labels.add(exportChildPostalAdress);
			labels.add(exportChildZipCode);
		}

		labels.add(exportOtherGuardianName);

		if (!query.isHideSSNForOtherGuardians()) {
			labels.add(exportOtherGuardianCitizenIdentifier);
		}

		labels.add(exportOtherGuardianEmail);
		labels.add(exportOtherGuardianPhone);

		if (query.isShowGuardianAddress()) {

			labels.add(exportOtherGuardianAdress);
			labels.add(exportOtherGuardianPostalAdress);
			labels.add(exportOtherGuardianZipCode);
		}

		return labels;
	}

	@Override
	public ExtensionLink getExtensionLink(User user) {

		if (hasRequiredDependencies) {

			return flowListExtensionLink;
		}

		return null;
	}

	@Override
	public AccessInterface getAccessInterface() {

		return moduleDescriptor;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse testChildren(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (flowAdminModule == null || !AccessUtils.checkAccess(user, flowAdminModule.getAccessInterface())) {

			throw new AccessDeniedException("Access to toggling of test children denied");
		}

		if (req.getMethod().equalsIgnoreCase("POST")) {

			if ("true".equals(req.getParameter("enable"))) {

				log.info("User " + user + " enabling test children");

				String userCitizenID = CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(user);
				StoredGuardian guardian = new StoredGuardian(user.getFirstname(), user.getLastname(), userCitizenID);

				Map<String, StoredChild> children = new LinkedHashMap<>();

				for (int i = 0; i < 18; i++) {

					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					calendar.add(Calendar.YEAR, -i);

					StoredChild child = new StoredChild("Testbarn", i + "�r", CITIZEN_ID_DATE_FORMATTER.format(calendar.getTime()) + "TEST");
					child.setTestChild(true);
					child.setMunicipalityCode("4321");
					child.setAddress("Testgatan 1");
					child.setZipcode("123 45");
					child.setPostalAddress("Testk�ping");
					child.setGuardians(Collections.singletonList(guardian));
					children.put(child.getCitizenIdentifier(), child);
				}

				SessionUtils.setAttribute(SESSION_TEST_CHILDREN, children, user.getSession());

			} else {

				log.info("User " + user + " disabling test children");

				SessionUtils.removeAttribute(SESSION_TEST_CHILDREN, user.getSession());
			}

			this.flowAdminModule.redirectToDefaultMethod(req, res);
		}

		log.info("User " + user + " viewing test children form");

		Document doc = createDocument(req, uriParser, user);

		Element settingsElement = doc.createElement("TestChildren");
		doc.getDocumentElement().appendChild(settingsElement);

		XMLUtils.appendNewElement(doc, settingsElement, "Enabled", SessionUtils.getAttribute(SESSION_TEST_CHILDREN, user.getSession()) != null);

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	public ChildQueryFilterEndpoint getFilterEndpoint(String name) {

		for (ChildQueryFilterProvider provider : filterProviders) {

			try {
				ChildQueryFilterEndpoint endpoint = provider.getEndpoint(name);

				if (endpoint != null) {
					return endpoint;
				}

			} catch (Exception e) {
				log.error("Error getting filter endpoint with name \"" + name + "\" from provider " + provider, e);
			}
		}

		return null;
	}

	public List<ChildQueryFilterEndpoint> getFilterEndpoints() {

		List<ChildQueryFilterEndpoint> endpoints = new ArrayList<>();

		for (ChildQueryFilterProvider provider : filterProviders) {

			try {
				List<? extends ChildQueryFilterEndpoint> providerEndpoints = provider.getEndpoints();

				if (providerEndpoints != null) {

					endpoints.addAll(providerEndpoints);
				}

			} catch (Exception e) {
				log.error("Error getting filter endpoints from provider " + provider, e);
			}
		}

		return endpoints;
	}

	public boolean addChildQueryFilterProvider(ChildQueryFilterProvider provider) {

		return filterProviders.add(provider);
	}

	public boolean removeChildQueryFilterProvider(ChildQueryFilterProvider provider) {

		return filterProviders.remove(provider);
	}

	public boolean hasQueriesUsingFilterEndpoint(String name) throws SQLException {
		
		HighLevelQuery<ChildQuery> query = new HighLevelQuery<>();
		query.addParameter(queryFilterEndpointNameParamFactory.getParameter(name));
		query.setRowLimiter(MySQLRowLimiter.SINGLE_ROW);
		return queryDAO.getBoolean(query);
	}
	
	public List<ChildQuery> getQueriesUsingFilterEndpoint(String name) throws SQLException {

		HighLevelQuery<ChildQuery> query = new HighLevelQuery<>();
		query.addParameter(queryFilterEndpointNameParamFactory.getParameter(name));

		List<ChildQuery> queries = queryDAO.getAll(query);

		if (queries != null) {

			for (Iterator<ChildQuery> it = queries.iterator(); it.hasNext();) {

				ChildQuery childQuery = it.next();

				QueryDescriptor queryDescriptor = queryDescriptorDAOWrapper.get(childQuery.getQueryID());

				if (queryDescriptor != null) {

					childQuery.init(queryDescriptor, getFullAlias() + "/config/" + queryDescriptor.getQueryID());

				} else {

					it.remove();
				}
			}
		}

		return queries;
	}

}
