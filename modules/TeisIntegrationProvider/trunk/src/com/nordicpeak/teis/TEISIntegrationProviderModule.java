package com.nordicpeak.teis;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.SubmitEvent;
import com.nordicpeak.flowengine.interfaces.FlowAdminExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.teis.beans.IntegrationConfiguration;
import com.nordicpeak.teis.beans.IntegrationLog;
import com.nordicpeak.teis.beans.QueuedEvent;
import com.nordicpeak.teis.enums.QueuedEventType;


public class TEISIntegrationProviderModule extends AnnotatedForegroundModule implements FlowAdminExtensionViewProvider, ViewFragmentModule<ForegroundModuleDescriptor>{

	@ModuleSetting(allowsNull=true)
	@TextAreaSettingDescriptor(name="Supported actionID's", description="The action ID's which will trigger the integration")
	private List<String> supportedActionIDs;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="TEIS URL", description="The URL to the web service interface of TEIS", required=true)
	private String teisURL;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="User", description="The username to use for the TEIS connection", required=true)
	private String user;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Password", description="The password to use for the TEIS connection", required=true)
	private String password;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Service ID", description="The service ID for the TEIS connection", required=true)
	private String serviceID;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Sender operator", description="The sender operator to use", required=true)
	private String senderOperator;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Sender address", description="The sender address to use", required=true)
	private String senderAddress;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Retry delay", description="The retry delay in seconds between attempts to send events regarding a flow instance to TEIS", required=true)
	private Integer retryDelay = 300;	
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Retry count", description="The maximum retry count before sending of events for a given flow instance are paused", required=true)
	private Integer retryCount = 10;		
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name="Disable SSL check", description="Disables the check if the used SSL certificate is valid, use with caution.")
	private boolean disableSSLCertCheck;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmententXML;	
	
	@InstanceManagerDependency(required=true)
	private FlowAdminModule flowAdminModule;
	
	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	
	private AnnotatedDAO<IntegrationConfiguration> configurationDAO;
	private AnnotatedDAO<IntegrationLog> logDAO;
	private AnnotatedDAO<QueuedEvent> queueDAO;
	
	private QueryParameterFactory<IntegrationConfiguration, Integer> flowIDParamFactory;
	
	protected boolean configured;
	
	@Override
	protected void moduleConfigured() throws Exception {

		this.configured = ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.WARN);
		
		this.viewFragmentTransformer = new ModuleViewFragmentTransformer<>(sectionInterface.getForegroundModuleXSLTCache(), this, systemInterface.getEncoding());

		this.viewFragmentTransformer.setDebugXML(debugFragmententXML);		
	}
	
	@Override
	public void unload() throws Exception {

		super.unload();
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, TEISIntegrationProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);
		
		configurationDAO = daoFactory.getDAO(IntegrationConfiguration.class);
		logDAO = daoFactory.getDAO(IntegrationLog.class);
		queueDAO = daoFactory.getDAO(QueuedEvent.class);
		
		flowIDParamFactory = configurationDAO.getParamFactory("flowID", Integer.class);
	}	
	
	@EventListener(channel=FlowInstanceManager.class, priority = 255)
	public void processEvent(SubmitEvent event, EventSource source) throws SQLException {

		if(!configured){
			
			log.warn("Module " + moduleDescriptor + " is not properly configured");
		}
		
		if(source.isLocal()){

			if(event.getEvent().getEventType() != EventType.SUBMITTED || event.getActionID() == null || !supportedActionIDs.contains(event.getActionID())){

				return;
			}

			IntegrationConfiguration configuration = getConfiguration(event.getFlowInstanceManager().getFlowInstance().getFlow());
			
			if(configuration == null || !configuration.isEnabled()){
				
				return;
			}
			
			log.info("Queueing event for flow instance " + event.getFlowInstanceManager().getFlowInstance());
			
			QueuedEvent queuedEvent = new QueuedEvent();
			
			queuedEvent.setEventID(event.getEvent().getEventID());
			queuedEvent.setEventType(QueuedEventType.FLOW_INSTANCE);
			queuedEvent.setFlowID(event.getFlowInstanceManager().getFlowInstance().getFlow().getFlowID());
			queuedEvent.setFlowInstanceID(event.getFlowInstanceManager().getFlowInstance().getFlowInstanceID());
			queuedEvent.setQueued(TimeUtils.getCurrentTimestamp());
			
			queueDAO.add(queuedEvent);
		}
	}

	@EventListener(channel=FlowInstanceManager.class)
	public void processEvent(ExternalMessageAddedEvent event, EventSource source) throws SQLException {
		
		IntegrationConfiguration configuration = getConfiguration(event.getFlowInstance());
		
		log.info("Queueing event for external message " + event.getExternalMessage() + " belonging to flow instance " + event.getExternalMessage().getFlowInstance());
		
		QueuedEvent queuedEvent = new QueuedEvent();
		
		queuedEvent.setEventID(event.getEvent().getEventID());
		queuedEvent.setEventType(QueuedEventType.MESSAGE);
		queuedEvent.setFlowID(configuration.getFlowID());
		queuedEvent.setFlowInstanceID(event.getExternalMessage().getFlowInstance().getFlowInstanceID());
		queuedEvent.setQueued(TimeUtils.getCurrentTimestamp());
		
		queueDAO.add(queuedEvent);
	}
	
	private IntegrationConfiguration getConfiguration(FlowInstance flowInstance) throws SQLException {

		if(flowInstance.getFlow() != null){
			
			return getConfiguration(flowInstance.getFlow());
		}
		
		flowInstance = flowAdminModule.getFlowInstance(flowInstance.getFlowInstanceID(), null, FlowInstance.FLOW_RELATION);
		
		if(flowInstance != null){
			
			return getConfiguration(flowInstance.getFlow());
		}
		
		return null;
	}

	private IntegrationConfiguration getConfiguration(ImmutableFlow flow) throws SQLException {

		HighLevelQuery<IntegrationConfiguration> query = new HighLevelQuery<>();
		
		query.addParameter(flowIDParamFactory.getParameter(flow.getFlowID()));
		
		return configurationDAO.get(query);
	}

	@Override
	public int getPriority() {

		//TODO add module setting
		
		return 10;
	}

	@Override
	public ViewFragment getShowView(Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {

		if (!AccessUtils.checkAccess(user, this.moduleDescriptor)) {

			return null;
		}

		Document doc = createDocument(req, uriParser, user);

		Element showViewElement = doc.createElement("ShowView");
		doc.getDocumentElement().appendChild(showViewElement);

		XMLUtils.append(doc, showViewElement, getConfiguration(flow));

		showViewElement.appendChild(flow.toXML(doc));

		return viewFragmentTransformer.createViewFragment(doc);
	}

	@Override
	public String getExtensionViewTitle() {

		return null;
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
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(document);

		return doc;
	}
	
	//Configure flow
	
	//View queue
	
	//View log (paged?)
	
	//View paused
	
	//Delete flow
	
	//Delete single event
}
