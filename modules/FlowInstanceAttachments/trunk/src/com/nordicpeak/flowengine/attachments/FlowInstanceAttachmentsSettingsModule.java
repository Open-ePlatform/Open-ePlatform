package com.nordicpeak.flowengine.attachments;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
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
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.attachments.beans.FlowInstanceAttachmentsSettings;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowAdminExtensionShowView;
import com.nordicpeak.flowengine.interfaces.FlowAdminExtensionViewProvider;

public class FlowInstanceAttachmentsSettingsModule extends AnnotatedForegroundModule implements ViewFragmentModule<ForegroundModuleDescriptor>, FlowAdminExtensionViewProvider {

	private static final String FLOW_NOT_FOUND = "FlowNotFound";


	@XSLVariable(prefix = "java.")
	private String adminExtensionViewTitle = "not set";

	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this extension provider compared to other providers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int priority = 0;
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Temp dir", description = "Directory for temporary files. Should be on the same filesystem as the file store for best performance. If not set system default temp directory will be used")
	protected String tempDir;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmententXML;

	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	
	private AnnotatedDAOWrapper<FlowInstanceAttachmentsSettings, Integer> attachmentSettingsDAOWrapper;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmentXML;

	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;
	
	protected FlowAdminModule flowAdminModule;
	
	@Override
	protected void moduleConfigured() throws Exception {
		
		super.moduleConfigured();
		
		ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.WARN);
		
		if (!StringUtils.isEmpty(tempDir) && !FileUtils.isReadable(tempDir)) {
			
			log.error("Filestore not found/readable");
		}
		
		viewFragmentTransformer = new ModuleViewFragmentTransformer<>(sectionInterface.getForegroundModuleXSLTCache(), this, systemInterface.getEncoding());
		viewFragmentTransformer.setDebugXML(debugFragmententXML);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowInstanceAttachmentsModule.class.getName(), new XMLDBScriptProvider(FlowInstanceAttachmentsModule.class.getResourceAsStream("DB script.xml")));
		
		if (upgradeResult.isUpgrade()) {
			
			log.info(upgradeResult.toString());
		}
		
		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface);
		attachmentSettingsDAOWrapper = daoFactory.getDAO(FlowInstanceAttachmentsSettings.class).getWrapper(Integer.class);
	}
	
	@Override
	public FlowAdminExtensionShowView getShowView(Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerException, SQLException {
		
		Document doc = createDocument(req, uriParser);
		
		Element showViewElement = doc.createElement("ShowSettings");
		doc.getDocumentElement().appendChild(showViewElement);
		
		XMLUtils.appendNewElement(doc, showViewElement, "FullAlias", getFullAlias());
		showViewElement.appendChild(flow.toXML(doc));
		
	

			XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ModuleURI", req.getContextPath() + getFullAlias());
			XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "StaticContentURL", getStaticContentURL());
		
		
		FlowInstanceAttachmentsSettings settings = attachmentSettingsDAOWrapper.get(flow.getFlowFamily().getFlowFamilyID());

		boolean enabled = false;
		
		if(settings != null) {
		
			XMLUtils.append(doc, showViewElement, settings);
			enabled = true;
		}
		
		return new FlowAdminExtensionShowView(viewFragmentTransformer.createViewFragment(doc, true), enabled);
	}
	
	/**
	 * Metod som populerar bild för att visa inställningar
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse showUpdateSettings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		Flow flow = flowAdminModule.getRequestedFlow(req, user, uriParser);
		
		if (flow == null) {
			
			return flowAdminModule.list(req, res, user, uriParser, new ValidationError(FLOW_NOT_FOUND));
		} 
		
		Document doc = createDocument(req, uriParser);
		
		Element settingsElement = doc.createElement("ShowUpdateSettings");
		doc.getDocumentElement().appendChild(settingsElement);
		
		settingsElement.appendChild(flow.toXML(doc));
		
		FlowInstanceAttachmentsSettings settings = attachmentSettingsDAOWrapper.get(flow.getFlowFamily().getFlowFamilyID());
		
		//populera settings om det saknas (förutom ID) och använd dessa värden
		//i bilden
		if(settings == null)
		{
			settings = new FlowInstanceAttachmentsSettings();
			settings.setModuleEnabled(false);
			settings.setEmailEnabled(false);
			settings.setSmsEnabled(false);
		}
		
		XMLUtils.append(doc, settingsElement, settings);
		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ModuleURI", req.getContextPath() + getFullAlias());
		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "StaticContentURL", getStaticContentURL());
		
		
		
		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateSettings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
				
		Flow flow = flowAdminModule.getRequestedFlow(req, user, uriParser);
		
		if (flow == null) {
			
			return flowAdminModule.list(req, res, user, uriParser, new ValidationError(FLOW_NOT_FOUND));
		}
		
		//uppdatera basen
		updateSettings(req, flow);
		
		flowAdminModule.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#addflowinstanceasmanagersettings");
		
		return null;
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteSettings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		if(!HTTPUtils.isPost(req)) {
			
			throw new AccessDeniedException("Delete requests using method " + req.getMethod() + " are not allowed.");
		}
		
		Flow flow = flowAdminModule.getRequestedFlow(req, user, uriParser);
		
		if (flow == null) {
			
			return flowAdminModule.list(req, res, user, uriParser, new ValidationError(FLOW_NOT_FOUND));
			
		}
		
		FlowInstanceAttachmentsSettings settings = attachmentSettingsDAOWrapper.get(flow.getFlowFamily().getFlowFamilyID());
		
		if (settings != null) {
			
			log.info("User " + user + " deleting attachmentsettings for flow " + flow);
			attachmentSettingsDAOWrapper.delete(settings);
			
			
		} else {
			
			log.warn("User " + user + " trying to delete attachmentsettings for flow " + flow + " which has no settings");
		}
		
		flowAdminModule.redirectToMethod(req, res, "/showflow/" + flow.getFlowID() + "#addflowinstanceasmanagersettings");
		
		return null;
	}

	private void updateSettings(HttpServletRequest req, Flow flow) throws SQLException {

		String[] moduleEnabledParameter = req.getParameterValues("moduleEnabled");
		String[] emailDisabledParameter = req.getParameterValues("emailEnabled");
		String[] smsDisabledParameter = req.getParameterValues("smsEnabled");
		
		boolean moduleEnabled = moduleEnabledParameter != null ? moduleEnabledParameter[0].equals("true") : false;
		boolean emailDisabled = emailDisabledParameter != null ? emailDisabledParameter[0].equals("true") : false;
		boolean smsDisabled = smsDisabledParameter != null ? smsDisabledParameter[0].equals("true") : false;
		
		FlowInstanceAttachmentsSettings settings = attachmentSettingsDAOWrapper.get(flow.getFlowFamily().getFlowFamilyID());
		
		if(settings == null)
		{
			settings = new FlowInstanceAttachmentsSettings();
			populateSettings(flow, moduleEnabled, emailDisabled, smsDisabled, settings);
			attachmentSettingsDAOWrapper.add(settings);
		}
		else
		{
			populateSettings(flow, moduleEnabled, emailDisabled, smsDisabled, settings);
			attachmentSettingsDAOWrapper.update(settings);
		}
	}

	private void populateSettings(Flow flow, boolean moduleEnabled, boolean emailDisabled, boolean smsDisabled, FlowInstanceAttachmentsSettings settings) {

		settings.setFlowFamilyID(flow.getFlowFamily().getFlowFamilyID());
		settings.setModuleEnabled(moduleEnabled);
		settings.setEmailEnabled(emailDisabled);
		settings.setSmsEnabled(smsDisabled);
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
	public int getModuleID() {

		return moduleDescriptor.getModuleID();
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

	public Document createDocument(HttpServletRequest req, URIParser uriParser) {

		Document doc = XMLUtils.createDomDocument();

		Element documentElement = doc.createElement("Document");

		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(documentElement);

		return doc;
	}

	public String getTitlePrefix() {

		return moduleDescriptor.getName();
	}

	public ModuleViewFragmentTransformer<ForegroundModuleDescriptor> getViewFragmentTransformer() {

		return viewFragmentTransformer;
	}

	public String getStaticContentURL() {

		return staticContentModule.getModuleContentURL(moduleDescriptor);
	}
	
	@InstanceManagerDependency(required = true)
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) {
		
		if (flowAdminModule == null && this.flowAdminModule != null) {
			
			this.flowAdminModule.removeExtensionViewProvider(this);
		}
		
		this.flowAdminModule = flowAdminModule;
		
		if (this.flowAdminModule != null) {
			
			this.flowAdminModule.addExtensionViewProvider(this);
		}
	}

}

