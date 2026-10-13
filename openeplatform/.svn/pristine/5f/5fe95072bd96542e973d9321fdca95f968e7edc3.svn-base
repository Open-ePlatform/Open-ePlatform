package com.nordicpeak.flowengine.persondatasavinginformer.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Level;
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
import se.unlogic.hierarchy.core.utils.ModuleUtils;
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
import se.unlogic.standardutils.object.ObjectUtils;
import se.unlogic.standardutils.populators.BooleanPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryContentFilter;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.persondatasavinginformer.PersonDataInformerModule;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.FlowFamilyInformerSetting;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerDataAlternative;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerDataSettingStorage;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerReasonAlternative;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.PersonDataInformerQueryInstanceSettingStorage;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

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
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap, QueryContentFilter contentFilter) throws Throwable {

		PersonDataInformerQuery query = new PersonDataInformerQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		contentFilter.filterHTML(query);

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

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

		//@formatter:off
		boolean hasQueryInstanceValues = !ObjectUtils.isNull(
					queryInstance.getReason(), 
					queryInstance.getExtraInformation(), 
					queryInstance.getComplaintDescription(), 
					queryInstance.getExtraInformationStorage(), 
					queryInstance.getConfirmationText(), 
					queryInstance.getDataRecipient(),
					queryInstance.getSelectedDataAlternatives(),
					queryInstance.getSelectedReasonAlternatives()
				);
		//@formatter:on

		ImmutableFlowFamily flowFamily = descriptor.getQueryDescriptor().getStep().getFlow().getFlowFamily();
		FlowFamilyInformerSetting informerSetting = personDataInformerModule.getInformerSetting(flowFamily);
		
		if (informerSetting != null) {

			if (hasQueryInstanceValues) {
				
				if (queryInstance.getReason() != null) {
					informerSetting.setReason(JTidyUtils.getXHTML(queryInstance.getReason(), systemInterface.getEncoding()));
				}

				if (queryInstance.getExtraInformation() != null) {
					informerSetting.setExtraInformation(JTidyUtils.getXHTML(queryInstance.getExtraInformation(), systemInterface.getEncoding()));
				}

				if (queryInstance.getComplaintDescription() != null) {
					informerSetting.setComplaintDescription(JTidyUtils.getXHTML(queryInstance.getComplaintDescription(), systemInterface.getEncoding()));
				}

				if (queryInstance.getExtraInformationStorage() != null) {
					informerSetting.setExtraInformationStorage(JTidyUtils.getXHTML(queryInstance.getExtraInformationStorage(), systemInterface.getEncoding()));
				}

				if (queryInstance.getConfirmationText() != null) {
					informerSetting.setConfirmationText(JTidyUtils.getXHTML(queryInstance.getConfirmationText(), systemInterface.getEncoding()));
				}

				if (queryInstance.getDataRecipient() != null) {
					informerSetting.setDataRecipient(JTidyUtils.getXHTML(queryInstance.getDataRecipient(), systemInterface.getEncoding()));
				}

				TextTagReplacer.replaceTextTags(informerSetting, instanceMetadata.getSiteProfile());

				queryInstance.getQuery().setFamilyInformerSettings(informerSetting);
			} else {
				
				if (informerSetting.getReason() == null && personDataInformerModule.getDefaultReason() != null) {

					informerSetting.setReason(JTidyUtils.getXHTML(personDataInformerModule.getDefaultReason(), systemInterface.getEncoding()));

				} else if (informerSetting.getReason() != null) {

					informerSetting.setReason(JTidyUtils.getXHTML(informerSetting.getReason(), systemInterface.getEncoding()));
				}

				if (informerSetting.getExtraInformation() == null && personDataInformerModule.getDefaultExtraInformation() != null) {

					informerSetting.setExtraInformation(JTidyUtils.getXHTML(personDataInformerModule.getDefaultExtraInformation(), systemInterface.getEncoding()));

				} else if (informerSetting.getExtraInformation() != null) {

					informerSetting.setExtraInformation(JTidyUtils.getXHTML(informerSetting.getExtraInformation(), systemInterface.getEncoding()));
				}

				if (informerSetting.getExtraInformationStorage() == null && personDataInformerModule.getDefaultExtraInformationStorage() != null) {

					informerSetting.setExtraInformationStorage(JTidyUtils.getXHTML(personDataInformerModule.getDefaultExtraInformationStorage(), systemInterface.getEncoding()));

				} else if (informerSetting.getExtraInformationStorage() != null) {

					informerSetting.setExtraInformationStorage(JTidyUtils.getXHTML(informerSetting.getExtraInformationStorage(), systemInterface.getEncoding()));
				}

				if (informerSetting.getConfirmationText() == null && personDataInformerModule.getDefaultConfirmationText() != null) {

					informerSetting.setConfirmationText(JTidyUtils.getXHTML(personDataInformerModule.getDefaultConfirmationText(), systemInterface.getEncoding()));

				} else if (informerSetting.getConfirmationText() != null) {

					informerSetting.setConfirmationText(JTidyUtils.getXHTML(informerSetting.getConfirmationText(), systemInterface.getEncoding()));
				}

				if (informerSetting.getDataRecipient() != null) {

					informerSetting.setDataRecipient(JTidyUtils.getXHTML(informerSetting.getDataRecipient(), systemInterface.getEncoding()));
				}

				TextTagReplacer.replaceTextTags(informerSetting, instanceMetadata.getSiteProfile());

				queryInstance.getQuery().setFamilyInformerSettings(informerSetting);
			}
		} else {
			queryInstance.setInvalidConfiguration(true);
		}

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

		HighLevelQuery<PersonDataInformerQueryInstance> query = new HighLevelQuery<PersonDataInformerQueryInstance>(PersonDataInformerQueryInstance.SETTING_STORAGES_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(PersonDataInformerQueryInstance queryInstance, TransactionHandler transactionHandler, InstanceRequestMetadata requestMetadata) throws Throwable {

		//Check if the query instance has an ID set and if the ID of the descriptor has changed
		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());
			
			this.queryInstanceDAO.add(queryInstance, transactionHandler, new RelationQuery(PersonDataInformerQueryInstance.SETTING_STORAGES_RELATION));

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, new RelationQuery(PersonDataInformerQueryInstance.SETTING_STORAGES_RELATION));
		}
		
		// Fixes recursion related stack overflow error
		if (queryInstance.getSettingStorages() != null) {
			for (PersonDataInformerQueryInstanceSettingStorage storageSetting : queryInstance.getSettingStorages()) {
				storageSetting.setQueryInstance(null);
			}
		}
	}

	@Override
	public void populate(PersonDataInformerQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, InstanceRequestMetadata requestMetadata) throws ValidationException {

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

		if (!requestMetadata.flowInstanceIsSubmitted()) {
			try {
				FlowFamilyInformerSetting informerSetting = getInformerSetting(requestMetadata);
				FlowFamily flowFamily = flowAdminModule.getFlowFamily(requestMetadata.getFlowFamilyID());

				if (informerSetting != null && flowFamily != null) {

					if (informerSetting.getReason() != null) {
						queryInstance.setReason(informerSetting.getReason());
					} else {
						queryInstance.setReason(personDataInformerModule.getDefaultReason());
					}

					if (informerSetting.getExtraInformation() != null) {
						queryInstance.setExtraInformation(informerSetting.getExtraInformation());
					} else {
						queryInstance.setExtraInformation(personDataInformerModule.getDefaultExtraInformation());
					}

					if (informerSetting.getComplaintDescription() != null) {
						queryInstance.setComplaintDescription(informerSetting.getComplaintDescription());
					} else {
						queryInstance.setComplaintDescription(personDataInformerModule.getDefaultComplaintDescription());
					}

					if (informerSetting.getExtraInformation() != null) {
						queryInstance.setExtraInformation(informerSetting.getExtraInformation());
					} else {
						queryInstance.setExtraInformation(personDataInformerModule.getDefaultExtraInformation());
					}

					if (informerSetting.getExtraInformationStorage() != null) {
						queryInstance.setExtraInformationStorage(informerSetting.getExtraInformationStorage());
					} else {
						queryInstance.setExtraInformationStorage(personDataInformerModule.getDefaultExtraInformationStorage());
					}

					if (informerSetting.getConfirmationText() != null) {
						queryInstance.setConfirmationText(informerSetting.getConfirmationText());
					} else {
						queryInstance.setConfirmationText(personDataInformerModule.getDefaultConfirmationText());
					}

					if (informerSetting.getDataRecipient() != null) {
						queryInstance.setDataRecipient(informerSetting.getDataRecipient());
					} else {
						queryInstance.setDataRecipient(null);
					}

					if (!CollectionUtils.isEmpty(informerSetting.getDataAlternatives())) {
						List<String> selectedDataAlternatives = new ArrayList<>(informerSetting.getDataAlternatives().size());

						for (InformerDataAlternative dataAlternative : informerSetting.getDataAlternatives()) {
							selectedDataAlternatives.add(dataAlternative.getName());
						}

						queryInstance.setSelectedDataAlternatives(selectedDataAlternatives);
					} else {
						queryInstance.setSelectedDataAlternatives(null);
					}
					
					if (!CollectionUtils.isEmpty(informerSetting.getReasonAlternatives())) {
						List<String> selectedReasonAlternatives = new ArrayList<>(informerSetting.getReasonAlternatives().size());
						
						for (InformerReasonAlternative reasonAlternative : informerSetting.getReasonAlternatives()) {
							selectedReasonAlternatives.add(reasonAlternative.getName());
						}
						
						queryInstance.setSelectedReasonAlternatives(selectedReasonAlternatives);
					} else {
						queryInstance.setSelectedReasonAlternatives(null);
					}

					if (flowFamily.getOwnerName() != null) {
						queryInstance.setOwnerName(flowFamily.getOwnerName());
					} else {
						queryInstance.setOwnerName(null);
					}

					if (flowFamily.getOwnerEmail() != null) {
						queryInstance.setOwnerEmail(flowFamily.getOwnerEmail());
					} else {
						queryInstance.setOwnerEmail(null);
					}

					if (informerSetting.getStorageSettings() != null) {
						List<PersonDataInformerQueryInstanceSettingStorage> settingStorages = new ArrayList<>();

						for (InformerDataSettingStorage settingStorage : informerSetting.getStorageSettings()) {
							PersonDataInformerQueryInstanceSettingStorage instanceSettingStorage = new PersonDataInformerQueryInstanceSettingStorage();

							instanceSettingStorage.setPeriod(settingStorage.getPeriod());
							instanceSettingStorage.setStorageType(settingStorage.getStorageType());
							instanceSettingStorage.setDescription(settingStorage.getDescription());

							settingStorages.add(instanceSettingStorage);
						}

						queryInstance.setSettingStorages(settingStorages);
					}
				}
			} catch (Exception e) {
				validationErrors.add(new ValidationError("Error populating query, could not get FlowFamilyInformerSetting"));
			}
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

	private FlowFamilyInformerSetting getInformerSetting(InstanceRequestMetadata requestMetadata) throws SQLException {

		return personDataInformerModule.getInformerSetting(flowAdminModule.getFlowFamily(requestMetadata.getFlowFamilyID()));
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
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws SQLException {

		PersonDataInformerQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, PersonDataInformerQueryInstance queryInstance, AttributeHandler attributeHandler) {

		XMLGeneratorDocument generatorDocument = new XMLGeneratorDocument(doc);

		generatorDocument.addIgnoredField(FlowFamilyInformerSetting.REASON_FIELD);
		generatorDocument.addIgnoredField(FlowFamilyInformerSetting.EXTRA_INFORMATION_FIELD);
		generatorDocument.addIgnoredField(FlowFamilyInformerSetting.COMPLAINT_DESCRIPTION_FIELD);
		generatorDocument.addIgnoredField(FlowFamilyInformerSetting.EXTRA_INFORMATION_STORAGE_FIELD);
		generatorDocument.addIgnoredField(FlowFamilyInformerSetting.CONFIRMATION_TEXT_FIELD);
		generatorDocument.addIgnoredField(FlowFamilyInformerSetting.DATA_RECIPIENT_FIELD);
		
		generatorDocument.addAssignableFieldElementableListener(FlowFamilyInformerSetting.class, new FlowFamilyInformerSettingTextsListener(systemInterface.getEncoding()));
		
		super.appendPDFData(generatorDocument, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler), systemInterface.getEncoding()));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<PersonDataInformerQueryInstance> getQueryInstanceClass() {

		return PersonDataInformerQueryInstance.class;
	}

}
