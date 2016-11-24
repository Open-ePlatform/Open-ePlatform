package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.enums.ContentType;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class NewEventsBackgroundModule extends AnnotatedBackgroundModule {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Number of events", description = "The number of flow instance events to show", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected int nrOfEvents = 5;

	@InstanceManagerDependency
	private UserFlowInstanceModule userFlowInstanceModule;

	@SuppressWarnings("unchecked")
	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		Document doc = this.createDocument(req, uriParser, user);

		HttpSession session = req.getSession();

		Timestamp lastEventUpdate = (Timestamp) session.getAttribute("LastFlowInstanceEventUpdate");

		Timestamp currentTime = TimeUtils.getCurrentTimestamp();

		if (lastEventUpdate == null || (currentTime.getTime() - lastEventUpdate.getTime()) > 60 * 1000) {

			List<FlowInstance> flowInstances = this.getChangedFlowInstances(user);

			XMLUtils.append(doc, doc.getDocumentElement(), flowInstances);

			session.setAttribute("FlowInstanceEvents", flowInstances);

			session.setAttribute("LastFlowInstanceEventUpdate", currentTime);

		} else {

			XMLUtils.append(doc, doc.getDocumentElement(), (List<FlowInstance>) session.getAttribute("FlowInstanceEvents"));

		}

		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "nrOfEvents", nrOfEvents);

		if (userFlowInstanceModule != null) {
			
			XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "userFlowInstanceModuleAlias", userFlowInstanceModule.getFullAlias());
		}

		return new SimpleBackgroundModuleResponse(doc);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(XMLUtils.createElement("contextpath", req.getContextPath(), doc));
		doc.appendChild(document);
		return doc;
	}

	protected List<FlowInstance> getChangedFlowInstances(User user) throws SQLException {

		if(userFlowInstanceModule != null){
			
			List<FlowInstance> flowInstances = userFlowInstanceModule.getFlowInstances(user, false, false);
			
			if (!CollectionUtils.isEmpty(flowInstances)) {
				
				List<FlowInstance> changedFlowInstances = new ArrayList<FlowInstance>();
				
				for (FlowInstance flowInstance : flowInstances) {
					
					Status status = flowInstance.getStatus();
					
					if (status.getContentType() == ContentType.SUBMITTED || status.getContentType() == ContentType.IN_PROGRESS || status.getContentType() == ContentType.WAITING_FOR_COMPLETION) {
						
						List<FlowInstanceEvent> events = userFlowInstanceModule.getNewFlowInstanceEvents(flowInstance, user);
						
						if (events != null) {
							
							flowInstance.setEvents(userFlowInstanceModule.getNewFlowInstanceEvents(flowInstance, user));
							
							changedFlowInstances.add(flowInstance);
							
						}
						
					}
					
				}
				
				return changedFlowInstances;
				
			}
		}

		return null;

	}

}
