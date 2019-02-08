package com.nordicpeak.flowengine.persondatasavinginformer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleViewFragment;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.csv.CSVRow;
import se.unlogic.standardutils.csv.CSVWriter;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.enums.EnumUtils;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xsl.XSLVariableReader;
import se.unlogic.standardutils.xsl.XSLVariableReaderRenamer;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.beans.ExtensionView;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.interfaces.FlowAdminFragmentExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowBrowserExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.FlowFamilyInformerSetting;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerDataAlternative;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerDataSettingStorage;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerReasonAlternative;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerStandardText;
import com.nordicpeak.flowengine.persondatasavinginformer.enums.StorageType;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class PersonDataInformerModule extends AnnotatedForegroundModule implements FlowAdminFragmentExtensionViewProvider, ViewFragmentModule<ForegroundModuleDescriptor>, FlowBrowserExtensionViewProvider {

	private static final AnnotatedRequestPopulator<FlowFamilyInformerSetting> POPULATOR = new AnnotatedRequestPopulator<FlowFamilyInformerSetting>(FlowFamilyInformerSetting.class);

	@XSLVariable(prefix = "java.")
	private String adminExtensionViewTitle = "Person data informer settings";

	@XSLVariable(prefix = "java.")
	private String browserExtensionViewTitle = "Person data informer settings";

	@XSLVariable(name = "i18n.Column.FlowName")
	private String columnFlowName = "";

	@XSLVariable(name = "i18n.Column.FlowType")
	private String columnFlowType = "";

	@XSLVariable(name = "i18n.Column.PersonData")
	private String columnPersonData = "";

	@XSLVariable(name = "i18n.Reasons")
	private String columnReasons = "";

	@XSLVariable(name = "i18n.YearsSaved")
	private String columnYearsSaved = "";

	@XSLVariable(name = "i18n.Reason")
	private String columnReason = "";

	@XSLVariable(name = "i18n.Accountable")
	private String columnAccountable = "";

	@XSLVariable(name = "i18n.ExtraInformation")
	private String columnExtraInformation = "";

	@XSLVariable(name = "i18n.YearsSaved.Infinite")
	private String yearsSavedInfinite = "";

	@XSLVariable(name = "i18n.YearsSaved.Years")
	private String yearsSavedYears = "";

	@XSLVariable(name = "i18n.YearsSaved.Months")
	private String yearsSavedMonths = "";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this extension provider compared to other providers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int priority = 0;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmententXML;

	private AnnotatedDAO<FlowFamilyInformerSetting> settingsDAO;
	private AnnotatedDAOWrapper<InformerDataAlternative, Integer> dataAlternativesDAOWrapper;
	private AnnotatedDAOWrapper<InformerReasonAlternative, Integer> reasonAlternativesDAOWrapper;
	private AnnotatedDAO<InformerStandardText> standardTextsDAO;

	private QueryParameterFactory<FlowFamilyInformerSetting, Integer> flowFamilyIDParamFactory;

	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;

	@InstanceManagerDependency
	protected SiteProfileHandler profileHandler;

	private FlowBrowserModule flowBrowserModule;

	private FlowAdminModule flowAdminModule;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;

	private List<ScriptTag> updateGlobalScriptTags;

	private String defaultComplaintDescription = "";
	private String defaultReasonDescription = "";
	private String defaultStorageDescription = "";
	private String defaultExtraInformationDescription = "";
	private String defaultConfirmationText = "";

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getForegroundModuleXSLTCache(), this, sectionInterface.getSystemInterface().getEncoding());

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(PersonDataInformerModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + PersonDataInformerModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@InstanceManagerDependency(required = true)
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) {

		if (flowAdminModule == null && this.flowAdminModule != null) {

			this.flowAdminModule.removeFragmentExtensionViewProvider(this);
			this.flowAdminModule.removeFlowBrowserExtensionViewProvider(this);
		}

		this.flowAdminModule = flowAdminModule;

		if (this.flowAdminModule != null) {

			this.flowAdminModule.addFragmentExtensionViewProvider(this);
			this.flowAdminModule.addFlowBrowserExtensionViewProvider(this);
		}
	}

	@InstanceManagerDependency(required = true)
	public void setFlowBrowserModule(FlowBrowserModule flowBrowserModule) {

		if (flowBrowserModule == null && this.flowBrowserModule != null) {

			this.flowBrowserModule.removeExtensionViewProvider(this);
		}

		this.flowBrowserModule = flowBrowserModule;

		if (this.flowBrowserModule != null) {

			this.flowBrowserModule.addExtensionViewProvider(this);
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(PersonDataInformerModule.class, this);

		if (flowAdminModule != null) {

			setFlowAdminModule(null);
		}

		if (flowBrowserModule != null) {

			setFlowBrowserModule(null);
		}

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, PersonDataInformerModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		settingsDAO = daoFactory.getDAO(FlowFamilyInformerSetting.class);

		flowFamilyIDParamFactory = settingsDAO.getParamFactory("flowFamilyID", Integer.class);

		dataAlternativesDAOWrapper = daoFactory.getDAO(InformerDataAlternative.class).getWrapper(Integer.class);
		reasonAlternativesDAOWrapper = daoFactory.getDAO(InformerReasonAlternative.class).getWrapper(Integer.class);
		standardTextsDAO = daoFactory.getDAO(InformerStandardText.class);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		viewFragmentTransformer.setDebugXML(debugFragmententXML);

		XSLVariableReader variableReader = ModuleUtils.getXSLVariableReader(moduleDescriptor, sectionInterface.getSystemInterface());

		if (variableReader != null) {

			Map<String, String> map = new HashMap<String, String>();
			map.put("globalscripts", "updateglobalscripts");
			updateGlobalScriptTags = ModuleUtils.getGlobalScripts(new XSLVariableReaderRenamer(variableReader, map));
		}

		cacheStandardTexts();
	}

	@Override
	public ViewFragment getShowView(String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {

		if (!AccessUtils.checkRecursiveModuleAccess(user, moduleDescriptor, systemInterface)) {
			return null;
		}

		Document doc = createDocument(req, uriParser, user);

		Element showViewElement = doc.createElement("ShowSettings");
		doc.getDocumentElement().appendChild(showViewElement);

		showViewElement.appendChild(flow.toXML(doc));
		XMLUtils.appendNewElement(doc, showViewElement, "extensionRequestURL", extensionRequestURL);

		FlowFamilyInformerSetting informerSettings = getInformerSetting(flow.getFlowFamily());

		if (informerSettings != null) {

			TextTagReplacer.replaceTextTags(informerSettings, getCurrentSiteProfile(req, user, uriParser, flow.getFlowFamily()));
		}

		XMLUtils.append(doc, showViewElement, informerSettings);

		return viewFragmentTransformer.createViewFragment(doc);
	}

	public SiteProfile getCurrentSiteProfile(HttpServletRequest req, User user, URIParser uriParser, ImmutableFlowFamily flowFamily) {

		if (profileHandler != null) {

			SiteProfile profile = profileHandler.getCurrentProfile(user, req, uriParser);

			if (profile != null && flowFamily != null) {

				SiteProfile subProfile = profileHandler.getMatchingSubProfile(profile.getProfileID(), "flowfamily-" + flowFamily.getFlowFamilyID());

				if (subProfile != null) {

					return subProfile;
				}
			}

			return profile;
		}

		return null;
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		XMLUtils.appendNewElement(doc, document, "ModuleURI", req.getContextPath() + getFullAlias());
		XMLUtils.appendNewElement(doc, document, "StaticContentURL", staticContentModule.getModuleContentURL(moduleDescriptor));

		doc.appendChild(document);

		return doc;
	}

	public FlowFamilyInformerSetting getInformerSetting(ImmutableFlowFamily flowFamily) throws SQLException {

		HighLevelQuery<FlowFamilyInformerSetting> query = new HighLevelQuery<FlowFamilyInformerSetting>(FlowFamilyInformerSetting.DATA_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.REASON_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.STORAGE_SETTINGS_RELATION);

		query.addParameter(flowFamilyIDParamFactory.getParameter(flowFamily.getFlowFamilyID()));

		return settingsDAO.get(query);
	}

	public List<FlowFamilyInformerSetting> getInformerSettings(Collection<Integer> flowFamilyIDs) throws SQLException {

		HighLevelQuery<FlowFamilyInformerSetting> query = new HighLevelQuery<FlowFamilyInformerSetting>(FlowFamilyInformerSetting.DATA_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.REASON_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.STORAGE_SETTINGS_RELATION);

		query.addParameter(flowFamilyIDParamFactory.getWhereInParameter(flowFamilyIDs));

		return settingsDAO.getAll(query);
	}

	@Override
	public String getExtensionViewTitle() {

		return adminExtensionViewTitle;
	}

	@Override
	public String getExtensionViewLinkName() {

		return "informersettings";
	}

	@Override
	public int getPriority() {

		return priority;
	}

	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	@Override
	public List<LinkTag> getLinkTags() {

		return links;
	}

	@Override
	public List<ScriptTag> getScriptTags() {

		return scripts;
	}

	@Override
	public ViewFragment processRequest(String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		String method = uriParser.get(4);

		if ("updateflowsettings".equals(method)) {

			return updateFlowSettings(extensionRequestURL, flow, req, user, uriParser);

		} else if ("deleteflowsettings".equals(method)) {

			return deleteFlowSettings(flow, req, user, uriParser);
		}

		throw new URINotFoundException(uriParser);
	}

	public ViewFragment updateFlowSettings(String extensionRequestURL, Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		FlowFamilyInformerSetting informerSettings = getInformerSetting(flow.getFlowFamily());

		List<InformerDataAlternative> allDataAlternatives = dataAlternativesDAOWrapper.getAll();
		List<InformerReasonAlternative> allReasonAlternatives = reasonAlternativesDAOWrapper.getAll();

		List<ValidationError> validationErrors = null;

		if (req.getMethod().equalsIgnoreCase("POST")) {

			validationErrors = new ArrayList<ValidationError>();

			boolean isNewSetting = false;

			if (informerSettings == null) {

				informerSettings = new FlowFamilyInformerSetting();
				informerSettings.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());

				isNewSetting = true;
			}

			if (req.getParameter("usesPersonData") != null) {

				if (!isNewSetting) { // Fix for blanking standard text fields before populate
					informerSettings.setComplaintDescription(null);
					informerSettings.setExtraInformation(null);
					informerSettings.setExtraInformationStorage(null);
					informerSettings.setReason(null);
					informerSettings.setConfirmationText(null);
				}

				try {
					POPULATOR.populate(informerSettings, req);

					if (!informerSettings.isOverrideComplaintDescription()) {

						informerSettings.setComplaintDescription(null);
					}

					List<InformerDataAlternative> selectedDataAlternatives = new ArrayList<InformerDataAlternative>(allDataAlternatives);
					List<InformerReasonAlternative> selectedReasonAlternatives = new ArrayList<InformerReasonAlternative>(allReasonAlternatives);

					if (!CollectionUtils.isEmpty(selectedDataAlternatives)) {
						for (Iterator<InformerDataAlternative> it = selectedDataAlternatives.iterator(); it.hasNext();) {

							InformerDataAlternative alternative = it.next();

							if (req.getParameter("data_alternative_" + alternative.getAlternativeID()) == null) {
								it.remove();
							}
						}

						if (CollectionUtils.isEmpty(selectedDataAlternatives)) {

							validationErrors.add(new ValidationError("dataAlternatives", ValidationErrorType.RequiredField));
						}
					}

					if (!CollectionUtils.isEmpty(selectedReasonAlternatives)) {
						for (Iterator<InformerReasonAlternative> it = selectedReasonAlternatives.iterator(); it.hasNext();) {

							InformerReasonAlternative alternative = it.next();

							if (req.getParameter("reason_alternative_" + alternative.getAlternativeID()) == null) {
								it.remove();
							}
						}

						if (CollectionUtils.isEmpty(selectedReasonAlternatives)) {

							validationErrors.add(new ValidationError("reasonAlternatives", ValidationErrorType.RequiredField));
						}
					}

					Integer storageCounter = NumberUtils.toInt(req.getParameter("storageCounter"));

					if (storageCounter == null) {
						validationErrors.add(new ValidationError("NoStorageCounter"));
					} else {
						informerSettings.setStorageSettings(new ArrayList<InformerDataSettingStorage>());

						boolean requiresDescription = false;
						int typeCount = 0;

						for (int i = 1; i <= storageCounter; i++) {
							StorageType storageType = EnumUtils.toEnum(StorageType.class, req.getParameter("storageType-" + i));

							if (storageType != null) {
								typeCount++;
							}

							if (typeCount > 1) {
								requiresDescription = true;
								break;
							}
						}

						int preStorageValidationErrors = validationErrors.size();

						for (int i = 1; i <= storageCounter; i++) {
							StorageType storageType = EnumUtils.toEnum(StorageType.class, req.getParameter("storageType-" + i));

							if (storageType != null) {
								
								InformerDataSettingStorage storageSetting = new InformerDataSettingStorage();
								storageSetting.setStorageType(storageType);

								if (requiresDescription || storageType == StorageType.CUSTOM) {
									
									storageSetting.setDescription(ValidationUtils.validateParameter("storageDescription-" + i, req, true, 1, 255, StringPopulator.getPopulator(), validationErrors));
								}
								
								if (storageType == StorageType.YEAR || storageType == StorageType.MONTH) {

									storageSetting.setPeriod(ValidationUtils.validateParameter("storagePeriod-" + i, req, true, PositiveStringIntegerPopulator.getPopulator(), validationErrors));
								}

								informerSettings.getStorageSettings().add(storageSetting);
							}
						}

						if (CollectionUtils.isEmpty(informerSettings.getStorageSettings()) && preStorageValidationErrors == validationErrors.size()) {
							validationErrors.add(new ValidationError("NoStorageSettings"));
						}
					}

					String ownerName = ValidationUtils.validateParameter("ownerName", req, false, 0, 255, validationErrors);
					String ownerEmail = ValidationUtils.validateParameter("ownerEmail", req, false, 0, 255, EmailPopulator.getPopulator(), validationErrors);

					if (CollectionUtils.isEmpty(validationErrors)) {

						informerSettings.setDataAlternatives(selectedDataAlternatives);
						informerSettings.setReasonAlternatives(selectedReasonAlternatives);

						log.info("User " + user + " updated person data informer settings for flow " + flow);

						settingsDAO.addOrUpdate(informerSettings, new RelationQuery(FlowFamilyInformerSetting.DATA_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.REASON_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.STORAGE_SETTINGS_RELATION));

						flow.getFlowFamily().setOwnerName(ownerName);
						flow.getFlowFamily().setOwnerEmail(ownerEmail);

						flowAdminModule.getDAOFactory().getFlowFamilyDAO().update(flow.getFlowFamily());

						systemInterface.getEventHandler().sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(CRUDAction.UPDATE, flow.getFlowFamily()), EventTarget.ALL);

						flowAdminModule.addFlowFamilyEvent(flowAdminModule.getEventFlowUpdatedMessage(), flow.getFlowFamily(), user);

						return null;
					}

				} catch (ValidationException e) {

					validationErrors.addAll(e.getErrors());
				}
				
			} else {
				
				if (!isNewSetting) {
					settingsDAO.delete(informerSettings);

					flowAdminModule.addFlowFamilyEvent(flowAdminModule.getEventFlowUpdatedMessage(), flow.getFlowFamily(), user);
				}

				return null;
			}
		}

		log.info("User " + user + " requesting update person data informer settings form for flow " + flow);

		Document doc = createDocument(req, uriParser, user);

		Element settingsElement = doc.createElement("UpdateSettings");
		doc.getDocumentElement().appendChild(settingsElement);

		settingsElement.appendChild(flow.toXML(doc));
		XMLUtils.appendNewElement(doc, settingsElement, "extensionRequestURL", extensionRequestURL);

		XMLUtils.append(doc, settingsElement, informerSettings);
		XMLUtils.append(doc, settingsElement, "DataAlternatives", allDataAlternatives);
		XMLUtils.append(doc, settingsElement, "ReasonAlternatives", allReasonAlternatives);

		if (defaultComplaintDescription != null) {
			XMLUtils.appendNewElement(doc, settingsElement, "DefaultComplaintDescription", defaultComplaintDescription);
		}

		if (defaultReasonDescription != null) {
			XMLUtils.appendNewElement(doc, settingsElement, "DefaultReasonDescription", defaultReasonDescription);
		}

		if (defaultStorageDescription != null) {
			XMLUtils.appendNewElement(doc, settingsElement, "DefaultStorageDescription", defaultStorageDescription);
		}

		if (defaultExtraInformationDescription != null) {
			XMLUtils.appendNewElement(doc, settingsElement, "DefaultExtraInformationDescription", defaultExtraInformationDescription);
		}

		if (defaultConfirmationText != null) {
			XMLUtils.appendNewElement(doc, settingsElement, "DefaultConfirmationText", defaultConfirmationText);
		}

		if (flow.getFlowFamily().getOwnerName() != null) {
			XMLUtils.appendNewElement(doc, settingsElement, "OwnerName", flow.getFlowFamily().getOwnerName());
		}

		if (flow.getFlowFamily().getOwnerEmail() != null) {
			XMLUtils.appendNewElement(doc, settingsElement, "OwnerEmail", flow.getFlowFamily().getOwnerEmail());
		}

		if (validationErrors != null) {

			XMLUtils.append(doc, settingsElement, "ValidationErrors", validationErrors);
			settingsElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		SimpleViewFragment viewFragment = (SimpleViewFragment) viewFragmentTransformer.createViewFragment(doc);
		return new SimpleViewFragment(viewFragment.getHTML(), viewFragment.getDebugXML(), updateGlobalScriptTags, viewFragment.getLinks());
	}

	public ViewFragment deleteFlowSettings(Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		FlowFamilyInformerSetting informerSettings = getInformerSetting(flow.getFlowFamily());

		if (informerSettings != null) {

			log.info("User " + user + " deleting person data informer settings for flow " + flow);

			settingsDAO.delete(informerSettings);

			flowAdminModule.addFlowFamilyEvent(flowAdminModule.getEventFlowUpdatedMessage(), flow.getFlowFamily(), user);

		} else {

			log.warn("User " + user + " trying to delete person data informer settings for flow " + flow + " which has no settings");
		}

		return null;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		log.info("User " + user + " listing flows with person data");

		Document doc = createDocument(req, uriParser, user);

		Element listFlowsElement = doc.createElement("ListFlows");

		doc.getDocumentElement().appendChild(listFlowsElement);

		Collection<Flow> flows = flowBrowserModule.getAccessFilteredLatestPublishedFlowVersions(user);

		if (flows != null) {

			Element flowsElement = XMLUtils.appendNewElement(doc, listFlowsElement, "Flows");

			for (Flow flow : flows) {

				FlowFamilyInformerSetting informerSetting = getInformerSetting(flow.getFlowFamily());

				if (informerSetting != null) {

					Element flowElement = flow.toXML(doc);
					flowElement.appendChild(informerSetting.toXML(doc));
					flowsElement.appendChild(flowElement);

				}

			}

		}

		XMLUtils.append(doc, listFlowsElement, "DataAlternatives", dataAlternativesDAOWrapper.getAll());
		XMLUtils.append(doc, listFlowsElement, "ReasonAlternatives", reasonAlternativesDAOWrapper.getAll());

		XMLUtils.appendNewElement(doc, listFlowsElement, "FlowBrowserFullAlias", flowBrowserModule.getFullAlias());
		XMLUtils.appendNewElement(doc, listFlowsElement, "FlowAdminFullAlias", flowAdminModule.getFullAlias());

		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
	}

	@WebPublic(alias = "icon")
	public ForegroundModuleResponse getFlowIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, AccessDeniedException, ModuleConfigurationException, SQLException, IOException {

		return flowAdminModule.getFlowIcon(req, res, user, uriParser);
	}

	@WebPublic()
	public ForegroundModuleResponse export(HttpServletRequest req, final HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " exporting person data settings");

		String[] familyParams = req.getParameterValues("flowFamilyID");

		List<FlowFamilyInformerSetting> settings = null;

		if (familyParams != null) {

			List<Integer> flowFamilyIDs = new ArrayList<Integer>(familyParams.length);

			for (String flowFamilyIDParam : familyParams) {

				Integer familyID = NumberUtils.toInt(flowFamilyIDParam);

				if (familyID != null) {
					flowFamilyIDs.add(familyID);
				}
			}

			if (!CollectionUtils.isEmpty(flowFamilyIDs)) {

				settings = getInformerSettings(flowFamilyIDs);
			}
		}

		res.setHeader("Content-Disposition", "attachment;filename=\"" + "Personuppgiftshantering " + DateUtils.DATE_FORMATTER.format(new Date()) + ".csv" + "\"");
		res.setContentType("text/csv");

		CSVWriter csvWriter = null;

		try {
			csvWriter = new CSVWriter(res.getWriter(), ";", 8);

			CSVRow titleRow = csvWriter.createRow();

			int columnIndex = 0;

			titleRow.setCellValue(columnIndex++, columnFlowName);
			titleRow.setCellValue(columnIndex++, columnFlowType);
			titleRow.setCellValue(columnIndex++, columnPersonData);
			titleRow.setCellValue(columnIndex++, columnReasons);
			titleRow.setCellValue(columnIndex++, columnYearsSaved);
			titleRow.setCellValue(columnIndex++, columnAccountable);
			titleRow.setCellValue(columnIndex++, columnReason);
			titleRow.setCellValue(columnIndex++, columnExtraInformation);

			csvWriter.writeRow(titleRow);

			if (!CollectionUtils.isEmpty(settings)) {

				for (FlowFamilyInformerSetting setting : settings) {

					CSVRow currentRow = csvWriter.createRow();
					columnIndex = 0;

					Flow flow = flowBrowserModule.getLatestPublishedFlowVersion(setting.getFlowFamilyID());

					if (!flow.isHideFromOverview() && AccessUtils.checkAccess(user, flow.getFlowType().getUserAccessInterface())) {

						currentRow.setCellValue(columnIndex++, flow.getName());
						currentRow.setCellValue(columnIndex++, flow.getFlowType().getName());

						if (setting.getDataAlternatives() != null) {

							currentRow.setCellValue(columnIndex++, StringUtils.toCommaSeparatedString(setting.getDataAlternatives()));

						} else {

							currentRow.setCellValue(columnIndex++, "");
						}

						if (setting.getReasonAlternatives() != null) {

							currentRow.setCellValue(columnIndex++, StringUtils.toCommaSeparatedString(setting.getReasonAlternatives()));

						} else {

							currentRow.setCellValue(columnIndex++, "");
						}

						StringBuilder yearsSavedString = new StringBuilder();

						int storageSettingIndex = 1;

						for (InformerDataSettingStorage storageSetting : setting.getStorageSettings()) {
							yearsSavedString.append(storageSetting.getDescription());
							yearsSavedString.append(": ");

							if (storageSetting.getStorageType() == StorageType.INFINITY) {
								yearsSavedString.append(yearsSavedInfinite);
								
							} else if (storageSetting.getStorageType() == StorageType.CUSTOM) {
								
							} else {
								yearsSavedString.append(storageSetting.getPeriod());
								yearsSavedString.append(" ");

								if (storageSetting.getStorageType() == StorageType.YEAR) {
									yearsSavedString.append(yearsSavedYears);
									
								} else {
									yearsSavedString.append(yearsSavedMonths);
								}
							}

							if (storageSettingIndex < setting.getStorageSettings().size()) {
								yearsSavedString.append(", ");
							}

							storageSettingIndex++;
						}

						currentRow.setCellValue(columnIndex++, yearsSavedString.toString());
						currentRow.setCellValue(columnIndex++, flow.getFlowFamily().getOwnerName());
						currentRow.setCellValue(columnIndex++, setting.getReason());
						currentRow.setCellValue(columnIndex++, setting.getExtraInformation());

						csvWriter.writeRow(currentRow);

					}

				}
			}

		} finally {

			CloseUtils.close(csvWriter);
		}

		return null;
	}

	@EventListener(channel = InformerStandardText.class)
	public void processStandardTextsEvent(CRUDEvent<InformerStandardText> event, EventSource eventSource) throws SQLException {

		setStandardText(event.getBeans().get(0));
	}

	private void cacheStandardTexts() throws SQLException {

		List<InformerStandardText> standardTexts = standardTextsDAO.getAll();

		if (!CollectionUtils.isEmpty(standardTexts)) {
			for (InformerStandardText text : standardTexts) {
				setStandardText(text);
			}
		}
	}

	private void setStandardText(InformerStandardText text) {

		if (text.getName().equals("defaultComplaintDescription")) {
			defaultComplaintDescription = text.getValue();
		} else if (text.getName().equals("defaultReasonDescription")) {
			defaultReasonDescription = text.getValue();
		} else if (text.getName().equals("defaultStorageDescription")) {
			defaultStorageDescription = text.getValue();
		} else if (text.getName().equals("defaultExtraInformationDescription")) {
			defaultExtraInformationDescription = text.getValue();
		} else if (text.getName().equals("defaultConfirmationText")) {
			defaultConfirmationText = text.getValue();
		}
	}

	@Override
	public ExtensionView getFlowOverviewExtensionView(Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {

		FlowFamilyInformerSetting setting = getInformerSetting(flow.getFlowFamily());

		if (setting != null) {

			Document doc = createDocument(req, uriParser, user);
			Element extensionElement = doc.createElement("FlowOverviewExtension");
			doc.getDocumentElement().appendChild(extensionElement);

			SiteProfile profile = getCurrentSiteProfile(req, user, uriParser, flow.getFlowFamily());

			TextTagReplacer.replaceTextTags(setting, profile);

			extensionElement.appendChild(setting.toXML(doc));

			if (defaultComplaintDescription != null) {
				if (profile != null) {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultComplaintDescription", TextTagReplacer.replaceTextTags(defaultComplaintDescription, profile.getSettingHandler()));
				} else {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultComplaintDescription", defaultComplaintDescription);
				}
			}

			if (defaultReasonDescription != null) {
				if (profile != null) {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultReasonDescription", TextTagReplacer.replaceTextTags(defaultReasonDescription, profile.getSettingHandler()));
				} else {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultReasonDescription", defaultReasonDescription);
				}
			}

			if (defaultStorageDescription != null) {
				if (profile != null) {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultStorageDescription", TextTagReplacer.replaceTextTags(defaultStorageDescription, profile.getSettingHandler()));
				} else {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultStorageDescription", defaultStorageDescription);
				}
			}

			if (defaultExtraInformationDescription != null) {
				if (profile != null) {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultExtraInformationDescription", TextTagReplacer.replaceTextTags(defaultExtraInformationDescription, profile.getSettingHandler()));
				} else {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultExtraInformationDescription", defaultExtraInformationDescription);
				}
			}
			
			if (defaultConfirmationText != null) {
				if (profile != null) {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultConfirmationText", TextTagReplacer.replaceTextTags(defaultConfirmationText, profile.getSettingHandler()));
				} else {
					XMLUtils.appendNewElement(doc, extensionElement, "DefaultConfirmationText", defaultConfirmationText);
				}
			}

			return new ExtensionView(browserExtensionViewTitle, viewFragmentTransformer.createViewFragment(doc), "left-owner");
		}

		return null;
	}

	public String getDefaultReason() {

		return defaultReasonDescription;
	}

	public String getDefaultComplaintDescription() {

		return defaultComplaintDescription;
	}

	public String getDefaultExtraInformation() {

		return defaultExtraInformationDescription;
	}

	public String getDefaultExtraInformationStorage() {

		return defaultStorageDescription;
	}
	
	public String getDefaultConfirmationText() {

		return defaultConfirmationText;
	}

	@Override
	public int getModuleID() {

		return moduleDescriptor.getModuleID();
	}
}