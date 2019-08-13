package com.nordicpeak.flowengine;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.date.PooledSimpleDateFormat;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.events.SubmitEvent;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.XMLProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;


public class XMLProviderModule extends AnnotatedForegroundModule implements XMLProvider {

	public static final RelationQuery EVENT_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstanceEvent.ATTRIBUTES_RELATION);

	private static final PooledSimpleDateFormat DATE_TIME_FORMATTER = new PooledSimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

	@ModuleSetting(allowsNull=true)
	@TextAreaSettingDescriptor(name="Supported actionID's", description="The action ID's which will trigger export XML to be generated and stored when a submit event is detected")
	private List<String> supportedActionIDs;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "XML dir", description = "The directory where XML files be stored ")
	protected String xmlDir;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Force UTF-8 encoding", description="Forces UTF-8 encoding to be used instead of the system default encoding.")
	protected boolean forceUTF;
	
	@InstanceManagerDependency(required = true)
	protected QueryHandler queryHandler;
	
	@InstanceManagerDependency(required = true)
	protected FlowBrowserModule browserModule;

	private FlowEngineDAOFactory daoFactory;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if(!systemInterface.getInstanceHandler().addInstance(XMLProvider.class, this)){

			throw new RuntimeException("Unable to register module " + this.moduleDescriptor + " in global instance handler using key " + XMLProvider.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(XMLProvider.class, this);

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
	}

	@EventListener(channel=FlowInstanceManager.class, priority = 40)
	public void processEvent(SubmitEvent event, EventSource eventSource) throws SQLException{

		if(this.supportedActionIDs == null || xmlDir == null){

			log.warn("Module " + this.moduleDescriptor + " not properly configured, refusing to generate export XML for flow instance " + event.getFlowInstanceManager().getFlowInstance());
		}

		if(event.getEvent().getEventType() != EventType.SUBMITTED || event.getActionID() == null || !supportedActionIDs.contains(event.getActionID())){

			return;
		}

		ImmutableFlowInstance flowInstance = event.getFlowInstanceManager().getFlowInstance();

		log.info("Generating export XML for flow instance " + flowInstance);

		try{
			generateXML(flowInstance, event.getFlowInstanceManager(), event.getEvent(), event.getEvent().getAdded(), getFile(flowInstance.getFlowInstanceID(), event.getEvent().getEventID()));

			event.getEvent().getAttributeHandler().setAttribute("xml", "true");
			daoFactory.getFlowInstanceEventDAO().update(event.getEvent(), EVENT_ATTRIBUTE_RELATION_QUERY, 10);

		}catch(Exception e){

			log.error("Error generating export XML for flow instance " + flowInstance + " submitted by user " + event.getEvent().getPoster(), e);
		}
	}

	@Override
	public void generateXML(ImmutableFlowInstance flowInstance, FlowInstanceManager flowInstanceManager, FlowInstanceEvent event, Timestamp lastSubmitted) throws Exception {
		
		generateXML(flowInstance, flowInstanceManager, event, lastSubmitted, getFile(flowInstance.getFlowInstanceID(), event.getEventID()));
		
		event.getAttributeHandler().setAttribute("xml", "true");
		daoFactory.getFlowInstanceEventDAO().update(event, EVENT_ATTRIBUTE_RELATION_QUERY);
	}
	
	@Override
	public void generateXML(ImmutableFlowInstance flowInstance, FlowInstanceManager flowInstanceManager, FlowInstanceEvent event, Timestamp lastSubmitted, File outputFile) throws Exception {
		
		Document doc = XMLUtils.createDomDocument();
		
		Element flowInstanceElement = doc.createElement("FlowInstance");
		doc.appendChild(flowInstanceElement);
		
		flowInstanceElement.setAttribute("xmlns", "http://www.oeplatform.org/version/2.0/schemas/flowinstance");
		flowInstanceElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		flowInstanceElement.setAttribute("xsi:schemaLocation", "http://www.oeplatform.org/version/2.0/schemas/flowinstance schema-" + flowInstanceManager.getFlowID() + ".xsd");
		
		Element headerElement = XMLUtils.appendNewElement(doc, flowInstanceElement, "Header");
		
		Element flowElement = XMLUtils.appendNewElement(doc, headerElement, "Flow");
		XMLUtils.appendNewElement(doc, flowElement, "FamilyID", flowInstance.getFlow().getFlowFamily().getFlowFamilyID());
		XMLUtils.appendNewElement(doc, flowElement, "Version", flowInstance.getFlow().getVersion());
		XMLUtils.appendNewElement(doc, flowElement, "FlowID", flowInstance.getFlow().getFlowID());
		XMLUtils.appendNewCDATAElement(doc, flowElement, "Name", flowInstance.getFlow().getName());
		
		XMLUtils.appendNewElement(doc, headerElement, "FlowInstanceID", flowInstance.getFlowInstanceID());
		
		Element statusElement = XMLUtils.appendNewElement(doc, headerElement, "Status");
		XMLUtils.appendNewElement(doc, statusElement, "ID", flowInstance.getStatus().getStatusID());
		XMLUtils.appendNewCDATAElement(doc, statusElement, "Name", flowInstance.getStatus().getName());
		
		appendUser(flowInstance.getPoster(), "Poster", doc, headerElement);
		
		if (flowInstance.getOwners() != null) {
			
			for (User owner : flowInstance.getOwners()) {
				
				appendUser(owner, "Owner", doc, headerElement);
			}
		}
		
		XMLUtils.appendNewElement(doc, headerElement, "Posted", DATE_TIME_FORMATTER.format(flowInstance.getAdded()));
		
		if (flowInstance.getUpdated() != null) {
			
			appendUser(flowInstance.getEditor(), "Editor", doc, headerElement);
			
			XMLUtils.appendNewElement(doc, headerElement, "Updated", DATE_TIME_FORMATTER.format(flowInstance.getUpdated()));
		}
		
		XMLUtils.appendNewElement(doc, headerElement, "FirstSubmitted", DATE_TIME_FORMATTER.format(flowInstance.getFirstSubmitted()));
		
		XMLUtils.appendNewElement(doc, headerElement, "LastSubmitted", DATE_TIME_FORMATTER.format(lastSubmitted));
		
		Element valuesElement = XMLUtils.appendNewElement(doc, flowInstanceElement, "Values");
		
		List<Element> queryElements = flowInstanceManager.getExportXMLElements(doc, queryHandler);
		
		if (queryElements != null) {
			
			for (Element queryElement : queryElements) {
				
				valuesElement.appendChild(queryElement);
			}
		}
		
		if (event != null) {
			
			List<? extends ImmutableFlowInstanceEvent> flowInstanceEvents = browserModule.getFlowInstanceEvents((FlowInstance) flowInstance);
			List<ImmutableFlowInstanceEvent> signEvents = null;
			
			String signingSessionID = event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION);
			
			if (!StringUtils.isEmpty(signingSessionID)) {
				
				signEvents = new ArrayList<ImmutableFlowInstanceEvent>(flowInstanceEvents.size());
				
				for (ImmutableFlowInstanceEvent flowInstanceEvent : flowInstanceEvents) {
					
					if (flowInstanceEvent.getEventType() == EventType.SIGNED
						&& signingSessionID.equals(flowInstanceEvent.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION))
						&& !Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNED_PDF.equals(flowInstanceEvent.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT))
					) {
						signEvents.add(flowInstanceEvent);
					}
				}
				
				if (CollectionUtils.isEmpty(signEvents)) {
					
					log.warn("Signing session ID set on " + event + " but no matching sign events found for " + flowInstance);
				}
			}
			
			if (!CollectionUtils.isEmpty(signEvents)) {
				
				Element signEventsElement = XMLUtils.appendNewElement(doc, flowInstanceElement, "SigningEvents");
				
				for (ImmutableFlowInstanceEvent signEvent : signEvents) {
					
					Element signElement = XMLUtils.appendNewElement(doc, signEventsElement, "SignEvent");
					
					XMLUtils.appendNewElement(doc, signElement, "SignedChecksum", signEvent.getAttributeHandler().getString("signingChecksum"));
					XMLUtils.appendNewElement(doc, signElement, "Date", DATE_TIME_FORMATTER.format(signEvent.getAdded()));
					
					if (signEvent.getPoster() != null) {
						
						appendUser(signEvent.getPoster(), "Signer", doc, signElement);
						
					} else {
						
						appendUserFromAttributes(signEvent, "Signer", doc, signElement);
					}
				}
			}
		}
		
		outputFile.getParentFile().mkdirs();
		
		if (forceUTF) {
			
			XMLUtils.writeXMLFile(doc, outputFile, true, "UTF-8", "1.1");
			
		} else {
			
			XMLUtils.writeXMLFile(doc, outputFile, true, systemInterface.getEncoding());
		}
	}

	@EventListener(channel=FlowInstance.class)
	public void processEvent(CRUDEvent<FlowInstance> event, EventSource source) {

		if(source.isLocal()){

			if(event.getAction() == CRUDAction.DELETE){

				for(FlowInstance flowInstance : event.getBeans()){

					File instanceDir = new File(xmlDir + File.separator + flowInstance.getFlowInstanceID());

					if(!instanceDir.exists()){

						continue;
					}

					log.info("Deleting XML files for flow instance " + flowInstance);

					FileUtils.deleteFiles(instanceDir, null, true);

					instanceDir.delete();
				}
			}
		}
	}
	
	private void appendUser(User user, String elementName, Document doc, Element headerElement) {

		if(user != null){

			Element userElement = XMLUtils.appendNewElement(doc, headerElement, elementName);
			XMLUtils.appendNewCDATAElement(doc, userElement, "Firstname", user.getFirstname());
			XMLUtils.appendNewCDATAElement(doc, userElement, "Lastname", user.getLastname());
			XMLUtils.appendNewCDATAElement(doc, userElement, "Email", user.getEmail());
			XMLUtils.appendNewElement(doc, userElement, "ID", user.getUserID());
		}
	}

	private void appendUserFromAttributes(ImmutableFlowInstanceEvent event, String elementName, Document doc, Element targetElement) {

		Element userElement = XMLUtils.appendNewElement(doc, targetElement, elementName);
		
		XMLUtils.appendNewCDATAElement(doc, userElement, "Firstname", event.getAttributeHandler().getString("firstname"));
		XMLUtils.appendNewCDATAElement(doc, userElement, "Lastname", event.getAttributeHandler().getString("lastname"));
		XMLUtils.appendNewElement(doc, userElement, "ID", event.getAttributeHandler().getString(Constants.USER_CITIZEN_IDENTIFIER_ATTRIBUTE));
	}
	
	@Override
	public File getXML(Integer flowInstanceID, Integer eventID) {

		File file = getFile(flowInstanceID, eventID);

		if(file.exists()){

			return file;
		}

		return null;
	}

	private File getFile(Integer flowInstanceID, Integer eventID) {

		return new File(xmlDir + File.separator + flowInstanceID + File.separator + eventID + ".xml");
	}
	
	@Override
	public String getEncoding() {
		
		return forceUTF ? "UTF-8" : systemInterface.getEncoding();
	}
}
