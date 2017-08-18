package com.nordicpeak.flowengine.persondatasavinginformer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.csv.CSVRow;
import se.unlogic.standardutils.csv.CSVWriter;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xsl.XSLVariableReader;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.beans.ExtensionView;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.interfaces.FlowAdminExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowBrowserExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowFamily;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.FlowFamilyInformerSetting;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerDataAlternative;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerReasonAlternative;

public class PersonDataInformerModule extends AnnotatedForegroundModule implements FlowAdminExtensionViewProvider, ViewFragmentModule<ForegroundModuleDescriptor>, FlowBrowserExtensionViewProvider {
	
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

	@XSLVariable(prefix = "java.")
	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Default complaint description", description="The descripion for how users are to complain about personal data being stored", required = true)
	private String defaultComplaintDescription;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this extension provider compared to other providers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int priority = 0;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmententXML;
	
	private AnnotatedDAO<FlowFamilyInformerSetting> settingsDAO;
	private AnnotatedDAOWrapper<InformerDataAlternative, Integer> dataAlternativesDAOWrapper;
	private AnnotatedDAOWrapper<InformerReasonAlternative, Integer> reasonAlternativesDAOWrapper;
	
	private QueryParameterFactory<FlowFamilyInformerSetting, Integer> flowFamilyIDParamFactory;
	
	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;
	
	private FlowBrowserModule flowBrowserModule;
	
	private FlowAdminModule flowAdminModule;
	
	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	
	private List<ScriptTag> updateScriptTags;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getModuleXSLTCache(), this, sectionInterface.getSystemInterface().getEncoding());
		
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(PersonDataInformerModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + PersonDataInformerModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}
	
	@InstanceManagerDependency(required = true)
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) {
		
		if (flowAdminModule == null && this.flowAdminModule != null) {
			
			this.flowAdminModule.removeExtensionViewProvider(this);
			this.flowAdminModule.removeFlowBrowserExtensionViewProvider(this);
		}
		
		this.flowAdminModule = flowAdminModule;
		
		if (this.flowAdminModule != null) {
			
			this.flowAdminModule.addExtensionViewProvider(this);
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
	}
	
	@Override
	protected void moduleConfigured() throws Exception {
		
		viewFragmentTransformer.setDebugXML(debugFragmententXML);
		
		XSLVariableReader variableReader = ModuleUtils.getXSLVariableReader(moduleDescriptor, sectionInterface.getSystemInterface());

		if (variableReader != null) {
			
			updateScriptTags = ModuleUtils.getGlobalScripts(new XSLVariableReaderRenamer(variableReader, "updateglobalscripts"));
		}
	}
	
	@Override
	public ViewFragment getShowView(Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {
		
		Document doc = createDocument(req, uriParser, user);
		
		Element showViewElement = doc.createElement("ShowSettings");
		doc.getDocumentElement().appendChild(showViewElement);
		
		showViewElement.appendChild(flow.toXML(doc));
		
		FlowFamilyInformerSetting informerSettings = getInformerSetting(flow.getFlowFamily());
		XMLUtils.append(doc, showViewElement, informerSettings);
		
		return viewFragmentTransformer.createViewFragment(doc);
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
		
		HighLevelQuery<FlowFamilyInformerSetting> query = new HighLevelQuery<FlowFamilyInformerSetting>(FlowFamilyInformerSetting.DATA_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.REASON_ALTERNATIVES_RELATION);
		
		query.addParameter(flowFamilyIDParamFactory.getParameter(flowFamily.getFlowFamilyID()));
		
		return settingsDAO.get(query);
	}
	
	public List<FlowFamilyInformerSetting> getInformerSettings() throws SQLException {
		
		HighLevelQuery<FlowFamilyInformerSetting> query = new HighLevelQuery<FlowFamilyInformerSetting>(FlowFamilyInformerSetting.DATA_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.REASON_ALTERNATIVES_RELATION);
		
		return settingsDAO.getAll(query);
	}
	
	public List<FlowFamilyInformerSetting> getInformerSettings(Collection<Integer> flowFamilyIDs) throws SQLException {
		
		HighLevelQuery<FlowFamilyInformerSetting> query = new HighLevelQuery<FlowFamilyInformerSetting>(FlowFamilyInformerSetting.DATA_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.REASON_ALTERNATIVES_RELATION);
		
		query.addParameter(flowFamilyIDParamFactory.getWhereInParameter(flowFamilyIDs));
		
		return settingsDAO.getAll(query);
	}
	
	@Override
	public String getExtensionViewTitle() {
		return adminExtensionViewTitle;
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
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateFlowSettings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		Flow flow = flowAdminModule.getRequestedFlow(req, user, uriParser);
		
		if (flow == null) {
			
			return flowAdminModule.list(req, res, user, uriParser, new ValidationError("FlowNotFound"));
			
		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {
			
			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}
		
		FlowFamilyInformerSetting informerSettings = getInformerSetting(flow.getFlowFamily());
		
		List<InformerDataAlternative> allDataAlternatives = dataAlternativesDAOWrapper.getAll();
		List<InformerReasonAlternative> allReasonAlternatives = reasonAlternativesDAOWrapper.getAll();
		
		List<ValidationError> validationErrors = null;
		
		if (req.getMethod().equalsIgnoreCase("POST")) {
			
			validationErrors = new ArrayList<ValidationError>();
			
			if (informerSettings == null) {
				
				informerSettings = new FlowFamilyInformerSetting();
				informerSettings.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());
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
					
					if (CollectionUtils.isEmpty(selectedDataAlternatives)) {
						
						validationErrors.add(new ValidationError("reasonAlternatives", ValidationErrorType.RequiredField));
					}
				}
				
				if (CollectionUtils.isEmpty(validationErrors)) {
					
					informerSettings.setDataAlternatives(selectedDataAlternatives);
					informerSettings.setReasonAlternatives(selectedReasonAlternatives);
					
					log.info("User " + user + " updated person data informer settings for flow " + flow);
					
					HighLevelQuery<FlowFamilyInformerSetting> query = new HighLevelQuery<FlowFamilyInformerSetting>(FlowFamilyInformerSetting.DATA_ALTERNATIVES_RELATION, FlowFamilyInformerSetting.REASON_ALTERNATIVES_RELATION);
					
					settingsDAO.addOrUpdate(informerSettings, query);
					
					flowAdminModule.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#informersettings");
					return null;
				}
				
			} catch (ValidationException e) {
				
				validationErrors.addAll(e.getErrors());
			}
		}
		
		log.info("User " + user + " requesting update person data informer settings form for flow " + flow);
		
		Document doc = createDocument(req, uriParser, user);
		
		Element settingsElement = doc.createElement("UpdateSettings");
		doc.getDocumentElement().appendChild(settingsElement);
		
		settingsElement.appendChild(flow.toXML(doc));
		
		XMLUtils.append(doc, settingsElement, informerSettings);
		XMLUtils.append(doc, settingsElement, "DataAlternatives", allDataAlternatives);
		XMLUtils.append(doc, settingsElement, "ReasonAlternatives", allReasonAlternatives);
		XMLUtils.appendNewElement(doc, settingsElement, "DefaultComplaintDescription", defaultComplaintDescription);
		
		if (validationErrors != null) {
			
			XMLUtils.append(doc, settingsElement, "ValidationErrors", validationErrors);
			settingsElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}
		
		SimpleForegroundModuleResponse response = new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
		response.addScripts(updateScriptTags);
		
		return response;
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteFlowSettings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		Flow flow = flowAdminModule.getRequestedFlow(req, user, uriParser);
		
		if (flow == null) {
			
			return flowAdminModule.list(req, res, user, uriParser, new ValidationError("FlowNotFound"));
			
		} else if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface())) {
			
			throw new AccessDeniedException("User does not have access to flow type " + flow.getFlowType());
		}
		
		FlowFamilyInformerSetting paymentSettings = getInformerSetting(flow.getFlowFamily());
		
		if (paymentSettings != null) {
			
			log.info("User " + user + " deleting person data informer settings for flow " + flow);
			
			settingsDAO.delete(paymentSettings);
			
		} else {
			
			log.warn("User " + user + " trying to deleting person data informer settings for flow " + flow + " which has no settings");
		}
		
		flowAdminModule.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#informersettings");
		
		return null;
	}
	
	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {
		
		log.info("User " + user + " listing flows with person data");
		
		Document doc = createDocument(req, uriParser, user);
		
		Element listFlowsElement = doc.createElement("ListFlows");
		
		doc.getDocumentElement().appendChild(listFlowsElement);
		
		List<FlowFamilyInformerSetting> flowFamilyInformerSettings = getInformerSettings();
		
		if (!CollectionUtils.isEmpty(flowFamilyInformerSettings)) {
			
			XMLUtils.append(doc, listFlowsElement, "Settings", flowFamilyInformerSettings);
			
			List<Flow> flows = new ArrayList<Flow>(flowFamilyInformerSettings.size());
			
			for (FlowFamilyInformerSetting setting : flowFamilyInformerSettings) {
				
				FlowFamily flowFamily = flowAdminModule.getFlowFamily(setting.getFlowFamilyID());
				
				if (flowFamily != null) {
					
					Flow flow = flowAdminModule.getLatestFlowVersion(flowFamily);
					
					if (flow != null) {
						
						flows.add(flow);
					}
				}
			}
			
			XMLUtils.append(doc, listFlowsElement, "Flows", flows);
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
					
					Flow flow = flowAdminModule.getLatestFlowVersion(flowAdminModule.getFlowFamily(setting.getFlowFamilyID()));

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
					
					currentRow.setCellValue(columnIndex++, setting.getYearsSaved() != null ? setting.getYearsSaved().toString() : yearsSavedInfinite);
					currentRow.setCellValue(columnIndex++, flow.getFlowFamily().getOwnerName());
					currentRow.setCellValue(columnIndex++, setting.getReason());
					currentRow.setCellValue(columnIndex++, setting.getExtraInformation());
					
					csvWriter.writeRow(currentRow);
				}
			}
			
		} finally {
			
			CloseUtils.close(csvWriter);
		}
		
		return null;
	}

	@Override
	public ExtensionView getFlowOverviewExtensionView(Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {
		
		FlowFamilyInformerSetting setting = getInformerSetting(flow.getFlowFamily());
		
		if (setting != null) {
			
			Document doc = createDocument(req, uriParser, user);
			Element extensionElement = doc.createElement("FlowOverviewExtension");
			doc.getDocumentElement().appendChild(extensionElement);
			
			extensionElement.appendChild(setting.toXML(doc));
			XMLUtils.appendNewElement(doc, extensionElement, "DefaultComplaintDescription", defaultComplaintDescription);
			
			return new ExtensionView(browserExtensionViewTitle, viewFragmentTransformer.createViewFragment(doc), "left-owner");
		}
		
		return null;
	}
	
}
