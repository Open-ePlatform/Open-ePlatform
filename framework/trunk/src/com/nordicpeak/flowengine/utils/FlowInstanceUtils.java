package com.nordicpeak.flowengine.utils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.interfaces.ContactQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class FlowInstanceUtils {
	
	public static String getSubmitterName(ImmutableFlowInstance flowInstance, String anonymousUserName) {
		
		String submitterName = getSubmitterName(flowInstance);
		
		if (submitterName == null) {
			
			return anonymousUserName;
		}
		
		return submitterName;
	}
	
	public static String getSubmitterName(ImmutableFlowInstance flowInstance) {
		
		if (flowInstance.getAttributeHandler().isSet("organizationName")) {
			
			String orgName = flowInstance.getAttributeHandler().getString("organizationName");
			
			if (flowInstance.getPoster() != null) {
				
				return orgName + " (" + getNameFromUser(flowInstance.getPoster()) + ")";
				
			} else if (flowInstance.getAttributeHandler().isSet("firstname")) {
				
				return orgName + " (" + getNameFromAttributes(flowInstance.getAttributeHandler()) + ")";
			}
			
			return orgName;
			
		} else if (flowInstance.getPoster() != null) {
			
			return getNameFromUser(flowInstance.getPoster());
			
		} else if (flowInstance.getAttributeHandler().isSet("firstname")) {
			
			return getNameFromAttributes(flowInstance.getAttributeHandler());
		}
		
		return null;
	}
	
	private static String getNameFromUser(User user) {
		
		return user.getFirstname() + " " + user.getLastname();
	}
	
	private static String getNameFromAttributes(AttributeHandler attributeHandler) {
		
		String firstName = attributeHandler.getString("firstname");
		String lastname = attributeHandler.getString("lastname");
		
		return firstName + " " + lastname;
	}
	
	public static FlowInstanceEvent getLatestPDFEvent(FlowInstance flowInstance) {
		
		if (CollectionUtils.isEmpty(flowInstance.getEvents())) {
			
			return null;
		}
		
		for (int i = flowInstance.getEvents().size() - 1; i > -1; i--) {
			
			FlowInstanceEvent event = flowInstance.getEvents().get(i);
			
			if (Boolean.TRUE.equals(event.getAttributeHandler().getBoolean("pdf"))) {
				
				return event;
			}
		}
		
		return null;
	}
	
	public static ImmutableFlowInstanceEvent getLatestSubmitEvent(ImmutableFlowInstance flowInstance){
		
		if (CollectionUtils.isEmpty(flowInstance.getEvents())) {
			
			return null;
		}
		
		for (int i = flowInstance.getEvents().size() - 1; i > -1; i--) {
			
			ImmutableFlowInstanceEvent event = flowInstance.getEvents().get(i);
			
			if (event.getEventType() == EventType.SUBMITTED) {
				
				return event;
			}
		}
		
		return null;
	}	
	
	// Used when saving flow instance on submit and multipart signing complete
	public static void setContactAttributes(FlowInstanceManager flowInstanceManager, MutableAttributeHandler attributeHandler) {
		
		List<ContactQueryInstance> contactQueryInstances = flowInstanceManager.getQueries(ContactQueryInstance.class);
		
		if (!CollectionUtils.isEmpty(contactQueryInstances)) {
			
			for (ContactQueryInstance contactQueryInstance : contactQueryInstances) {
				
				if (contactQueryInstance.getQueryInstanceDescriptor().isPopulated()) {
					
					Contact contact = contactQueryInstance.getContact();
					
					if (contact != null) {
						
						attributeHandler.setAttribute("firstname", contact.getFirstname());
						attributeHandler.setAttribute("lastname", contact.getLastname());
						attributeHandler.setAttribute("address", contact.getAddress());
						attributeHandler.setAttribute("zipCode", contact.getZipCode());
						attributeHandler.setAttribute("postalAddress", contact.getPostalAddress());
						attributeHandler.setAttribute("email", contact.getEmail());
						attributeHandler.setAttribute("phone", contact.getPhone());
						attributeHandler.setAttribute("mobilePhone", contact.getMobilePhone());
						attributeHandler.setAttribute("citizenIdentifier", contact.getCitizenIdentifier());
						
						attributeHandler.setAttribute("contactBySMS", Boolean.toString(contact.isContactBySMS()));
						
						attributeHandler.setAttribute("organizationName", contact.getOrganizationName());
						attributeHandler.setAttribute("organizationNumber", contact.getOrganizationNumber());
						
						return;
					}
				}
			}
			
			attributeHandler.removeAttribute("firstname");
			attributeHandler.removeAttribute("lastname");
			attributeHandler.removeAttribute("address");
			attributeHandler.removeAttribute("zipCode");
			attributeHandler.removeAttribute("postalAddress");
			attributeHandler.removeAttribute("email");
			attributeHandler.removeAttribute("phone");
			attributeHandler.removeAttribute("mobilePhone");
			attributeHandler.removeAttribute("citizenIdentifier");
			
			attributeHandler.removeAttribute("organizationName");
			attributeHandler.removeAttribute("organizationNumber");
			
			attributeHandler.removeAttribute("contactBySMS");
		}
	}
	
	public static void setDescriptionAttribute(MutableAttributeHandler attributeHandler) {
		
		if(!attributeHandler.isSet(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE) || attributeHandler.isSet(Constants.FLOW_INSTANCE_DESCRIPTION_GENERATED_ATTRIBUTE)){
			
			Map<String,String> attributeMap = attributeHandler.getAttributeMap();
			
			for(Entry<String,String> entry : attributeMap.entrySet()){
				
				if(entry.getKey().startsWith(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE)){
					
					attributeHandler.setAttribute(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE, entry.getValue());
					attributeHandler.setAttribute(Constants.FLOW_INSTANCE_DESCRIPTION_GENERATED_ATTRIBUTE, true);
					
					return;
				}
			}
			
			attributeHandler.removeAttribute(Constants.FLOW_INSTANCE_DESCRIPTION_GENERATED_ATTRIBUTE);
		}
	}
}
