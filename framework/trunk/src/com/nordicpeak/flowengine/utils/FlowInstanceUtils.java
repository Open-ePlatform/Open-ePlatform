package com.nordicpeak.flowengine.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.ReverseListIterator;
import se.unlogic.standardutils.string.StringUtils;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ContactQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.InvoiceLine;
import com.nordicpeak.flowengine.interfaces.PaymentQuery;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class FlowInstanceUtils {
	
	public static String getSubmitterName(ImmutableFlowInstance flowInstance, String anonymousUserName) {
		
		String submitterName = getSubmitterName(flowInstance);
		
		if (submitterName == null) {
			
			return anonymousUserName;
		}
		
		return submitterName;
	}
	
	/**
	 * Uses poster before attributes because if the poster was logged in then they can not change the attribute name anyway.
	 * And we are only interested in name.
	 * @param flowInstance
	 * @return
	 */
	public static String getSubmitterName(ImmutableFlowInstance flowInstance) {
		
		StringBuilder builder = new StringBuilder();
		
		boolean hasOrganization = flowInstance.getAttributeHandler().isSet("organizationName");
		
		if (hasOrganization) {
			
			builder.append(flowInstance.getAttributeHandler().getString("organizationName"));
			builder.append(" (");
		}
		
		ImmutableFlowInstanceEvent submitEvent = getLatestSubmitEvent(flowInstance);
		
		if (submitEvent != null && submitEvent.getPoster() != null) {
			
			builder.append(getNameFromUser(submitEvent.getPoster()));
			
		} else if (flowInstance.getPoster() != null) {
			
			builder.append(getNameFromUser(flowInstance.getPoster()));
			
		} else if (flowInstance.getAttributeHandler().isSet("firstname")) {
			
			builder.append(getNameFromAttributes(flowInstance.getAttributeHandler()));
		}
		
		if (hasOrganization) {
			builder.append(")");
		}
		
		if (builder.length() > 0) {
			
			return builder.toString();
		}
		
		return null;
	}
	
	public static String getSubmitterCitizenID(ImmutableFlowInstance flowInstance) {
		
		AttributeHandler attributeHandler = null;
		ImmutableFlowInstanceEvent submitEvent = getLatestSubmitEvent(flowInstance);
		
		if (submitEvent != null && submitEvent.getPoster() != null) {
			
			attributeHandler = submitEvent.getPoster().getAttributeHandler();
			
		} else if (flowInstance.getPoster() != null) {
			
			attributeHandler = flowInstance.getPoster().getAttributeHandler();
			
		} else {
			
			attributeHandler = flowInstance.getAttributeHandler();
		}
		
		if (attributeHandler != null && attributeHandler.isSet("citizenIdentifier")) {
			
			return attributeHandler.getString("citizenIdentifier");
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
	
	public static ImmutableFlowInstanceEvent getLatestSubmitEvent(ImmutableFlowInstance flowInstance) {
		
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
	
	public static ImmutableFlowInstanceEvent getLastestPaymentEvent(ImmutableFlowInstance flowInstance, boolean skipImmediateSubmitEvent) {
		
		return getLastestPaymentEvent(flowInstance.getEvents(), skipImmediateSubmitEvent);
	}
	
	public static ImmutableFlowInstanceEvent getLastestPaymentEvent(List<? extends ImmutableFlowInstanceEvent> events, boolean skipImmediateSubmitEvent) {
		
		if (events != null) {
			
			boolean firstSubmitEvent = true;
			
			for (ImmutableFlowInstanceEvent event : new ReverseListIterator<ImmutableFlowInstanceEvent>(events)) {
				
				if (event.getEventType() == EventType.SIGNED) {
					
					continue;
					
				} else if (event.getEventType() == EventType.PAYED) {
					
					return event;
					
				} else if (event.getEventType() == EventType.SUBMITTED && skipImmediateSubmitEvent && firstSubmitEvent) {
					
					firstSubmitEvent = false;
					continue;
					
				} else { // Other event types, stop
					
					break;
				}
			}
		}
		
		return null;
	}
	
	public static ImmutableFlowInstanceEvent getLastestSigningPDFEvent(ImmutableFlowInstance flowInstance) {
		
		return getLastestSigningPDFEvent(flowInstance.getEvents());
	}
	
	public static ImmutableFlowInstanceEvent getLastestSigningPDFEvent(List<? extends ImmutableFlowInstanceEvent> events) {
		
		if (events != null) {
			
			for (ImmutableFlowInstanceEvent event : new ReverseListIterator<ImmutableFlowInstanceEvent>(events)) {
				
				if ((event.getEventType() == EventType.SIGNED || event.getEventType() == EventType.SIGNING_SKIPPED)
				    && Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNING_PDF.equals(event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT))) {
					
					return event;
					
				} else if (event.getEventType() == EventType.UPDATED) {
					
					break;
				}
			}
		}
		
		return null;
	}
	
	// Used when saving flow instance on submit and multipart signing complete
	public static void setContactAttributes(FlowInstanceManager flowInstanceManager, MutableAttributeHandler flowInstanceAttributeHandler) {
		
		List<ContactQueryInstance> contactQueryInstances = flowInstanceManager.getQueries(ContactQueryInstance.class);
		
		if (!CollectionUtils.isEmpty(contactQueryInstances)) {
			
			for (ContactQueryInstance contactQueryInstance : contactQueryInstances) {
				
				if (contactQueryInstance.getQueryInstanceDescriptor().isPopulated() && !contactQueryInstance.updatedPoster()) {
					
					Contact contact = contactQueryInstance.getContact();
					
					if (contact != null) {
						
						flowInstanceAttributeHandler.setAttribute("contactInfoAttributes", "true");
						
						flowInstanceAttributeHandler.setAttribute("firstname", contact.getFirstname());
						flowInstanceAttributeHandler.setAttribute("lastname", contact.getLastname());
						flowInstanceAttributeHandler.setAttribute("address", contact.getAddress());
						flowInstanceAttributeHandler.setAttribute("zipCode", contact.getZipCode());
						flowInstanceAttributeHandler.setAttribute("postalAddress", contact.getPostalAddress());
						flowInstanceAttributeHandler.setAttribute("email", contact.getEmail());
						flowInstanceAttributeHandler.setAttribute("phone", contact.getPhone());
						flowInstanceAttributeHandler.setAttribute("mobilePhone", contact.getMobilePhone());
						flowInstanceAttributeHandler.setAttribute("citizenIdentifier", contact.getCitizenIdentifier());
						
						flowInstanceAttributeHandler.setAttribute("contactBySMS", Boolean.toString(contact.isContactBySMS()));
						flowInstanceAttributeHandler.setAttribute("contactByEmail", Boolean.toString(contact.isContactByEmail()));
						
						flowInstanceAttributeHandler.setAttribute("organizationName", contact.getOrganizationName());
						flowInstanceAttributeHandler.setAttribute("organizationNumber", contact.getOrganizationNumber());
						
						return;
					}
				}
			}
			
			flowInstanceAttributeHandler.removeAttribute("contactInfoAttributes");
			
			flowInstanceAttributeHandler.removeAttribute("firstname");
			flowInstanceAttributeHandler.removeAttribute("lastname");
			flowInstanceAttributeHandler.removeAttribute("address");
			flowInstanceAttributeHandler.removeAttribute("zipCode");
			flowInstanceAttributeHandler.removeAttribute("postalAddress");
			flowInstanceAttributeHandler.removeAttribute("email");
			flowInstanceAttributeHandler.removeAttribute("phone");
			flowInstanceAttributeHandler.removeAttribute("mobilePhone");
			flowInstanceAttributeHandler.removeAttribute("citizenIdentifier");
			
			flowInstanceAttributeHandler.removeAttribute("organizationName");
			flowInstanceAttributeHandler.removeAttribute("organizationNumber");
			
			flowInstanceAttributeHandler.removeAttribute("contactBySMS");
			flowInstanceAttributeHandler.removeAttribute("contactByEmail");
		}
	}
	
	public static void setDescriptions(FlowInstance flowInstance) {
		
		String genericDescription = null;
		
		if(flowInstance.getFlow().getUserDescriptionTemplate() != null) {
			
			String userDescription = AttributeTagUtils.replaceTags(flowInstance.getFlow().getUserDescriptionTemplate(), flowInstance.getAttributeHandler());
			
			if(!StringUtils.isEmpty(userDescription)) {
				
				flowInstance.setUserDescription(userDescription);
				
			}else {
			
				flowInstance.setUserDescription(null);
			}
		
		}else {
			
			genericDescription = getGenericDescription(flowInstance.getAttributeHandler());
			
			flowInstance.setUserDescription(genericDescription);
		}
		
		if(flowInstance.getFlow().getManagerDescriptionTemplate() != null) {
			
			String managerDescription = AttributeTagUtils.replaceTags(flowInstance.getFlow().getManagerDescriptionTemplate(), flowInstance.getAttributeHandler());
			
			if(!StringUtils.isEmpty(managerDescription)) {
			
				flowInstance.setManagerDescription(managerDescription);
				
			}else {
				
				flowInstance.setManagerDescription(null);
			}
		
		}else {
			
			if(genericDescription == null) {
				
				genericDescription = getGenericDescription(flowInstance.getAttributeHandler());
			}
			
			flowInstance.setManagerDescription(genericDescription);
		}
	}
	
	public static String getGenericDescription(AttributeHandler attributeHandler) {
		
		String description = attributeHandler.getString(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE);
		
		if(description != null) {
			
			return description;
		}
		
		Map<String, String> attributeMap = attributeHandler.getAttributeMap();
		
		if (!CollectionUtils.isEmpty(attributeMap)) {
			
			ArrayList<String> descriptionAttributes = new ArrayList<String>(attributeMap.size());
			
			for (String attributeName : attributeMap.keySet()) {
				
				if (attributeName.startsWith(Constants.FLOW_INSTANCE_DESCRIPTION_ATTRIBUTE)) {
					
					descriptionAttributes.add(attributeName);
				}
			}
			
			if (!descriptionAttributes.isEmpty()) {
				
				Collections.sort(descriptionAttributes);
				
				return attributeHandler.getString(descriptionAttributes.get(descriptionAttributes.size() - 1));
			}
		}
		
		return null;
	}
	
	public static String getManagersString(List<User> managers, List<Group> managerGroups) {
		
		return getManagersString(new StringBuilder(), managers, managerGroups).toString();
	}
	
	public static StringBuilder getManagersString(StringBuilder stringBuilder, List<User> managers, List<Group> managerGroups) {
	
		return getManagersString(stringBuilder, managers, managerGroups, true, false);
	}
	
	public static StringBuilder getManagersString(StringBuilder stringBuilder, List<User> managers, List<Group> managerGroups, boolean names, boolean usernames) {
		
		if (managers != null) {
			
			for (User selectedManager : managers) {
				
				if (stringBuilder.length() > 0) {
					
					stringBuilder.append(", ");
				}
				
				if (names) {
					stringBuilder.append(selectedManager.getFirstname());
					stringBuilder.append(" ");
					stringBuilder.append(selectedManager.getLastname());
				}
				
				if (usernames) {

					if (names) {
						stringBuilder.append(" (");
					}
					
					stringBuilder.append(selectedManager.getUsername());

					if (names) {
						stringBuilder.append(")");
					}
				}
			}
		}

		appendManagerGroupStrings(stringBuilder, managerGroups);
		
		return stringBuilder;
	}
	
	public static void appendManagerGroupStrings(StringBuilder stringBuilder, List<Group> managerGroups) {
		
		if (managerGroups != null) {
			
			for (Group selectedManagerGroup : managerGroups) {
				
				if (stringBuilder.length() > 0) {
					
					stringBuilder.append(", ");
				}
				
				stringBuilder.append(selectedManagerGroup.getName());
			}
		}
	}
	
	public static List<Contact> getContacts(ImmutableFlowInstance flowInstance) {
		
		List<Contact> contacts = new ArrayList<Contact>(2);
		
		Contact flowInstanceContact = getContactFromFlowInstanceAttributes(flowInstance.getAttributeHandler());
		
		if (flowInstanceContact != null) {
			
			contacts.add(flowInstanceContact);
		}
		
		if (!CollectionUtils.isEmpty(flowInstance.getOwners())) {
			
			for (User owner : flowInstance.getOwners()) {
				
				Contact ownerContact = getContactForUser(owner);
				addUniqueContact(contacts, ownerContact);
			}
		}
		
		if (contacts.isEmpty()) {
			return null;
		}
		
		return contacts;
	}
	
	public static Contact getContactFromFlowInstanceAttributes(AttributeHandler attributeHandler) {
		
		if (attributeHandler.isSet("contactInfoAttributes") || attributeHandler.isSet("email") || attributeHandler.isSet("mobilePhone")) {
			
			Contact contact = new Contact();
			
			contact.setFirstname(attributeHandler.getString("firstname"));
			contact.setLastname(attributeHandler.getString("lastname"));
			contact.setEmail(attributeHandler.getString("email"));
			contact.setPhone(attributeHandler.getString("phone"));
			contact.setMobilePhone(attributeHandler.getString("mobilePhone"));
			contact.setAddress(attributeHandler.getString("address"));
			contact.setZipCode(attributeHandler.getString("zipCode"));
			contact.setPostalAddress(attributeHandler.getString("postalAddress"));
			contact.setCareOf(attributeHandler.getString("careOf"));
			contact.setCitizenIdentifier(attributeHandler.getString("citizenIdentifier"));
			contact.setOrganizationNumber(attributeHandler.getString("organizationNumber"));
			contact.setContactBySMS(attributeHandler.getPrimitiveBoolean("contactBySMS"));
			
			if (attributeHandler.isSet("contactByEmail")) {
				
				contact.setContactByEmail(attributeHandler.getPrimitiveBoolean("contactByEmail"));
				
			} else {
				
				contact.setContactByEmail(true);
			}
			
			return contact;
		}
		
		return null;
	}
	
	public static Contact getContactForUser(User user) {
		
		if (user != null) {
			
			Contact contact = new Contact();
			
			contact.setFirstname(user.getFirstname());
			contact.setLastname(user.getLastname());
			contact.setEmail(user.getEmail());
			
			AttributeHandler attributeHandler = user.getAttributeHandler();
			
			if (attributeHandler != null) {
				
				contact.setPhone(attributeHandler.getString("phone"));
				contact.setMobilePhone(attributeHandler.getString("mobilePhone"));
				contact.setAddress(attributeHandler.getString("address"));
				contact.setZipCode(attributeHandler.getString("zipCode"));
				contact.setPostalAddress(attributeHandler.getString("postalAddress"));
				contact.setCareOf(attributeHandler.getString("careOf"));
				contact.setCitizenIdentifier(attributeHandler.getString("citizenIdentifier"));
			}
			
			contact.setContactBySMS(attributeHandler.getPrimitiveBoolean("contactBySMS"));
			
			if (attributeHandler.isSet("contactByEmail")) {
				
				contact.setContactByEmail(attributeHandler.getPrimitiveBoolean("contactByEmail"));
				
			} else {
				
				contact.setContactByEmail(true);
			}
			
			return contact;
		}
		
		return null;
	}
	
	/**
	 * Uses attributes first because address might be different.
	 * @param flowInstance
	 * @return
	 */
	public static Contact getPosterContact(ImmutableFlowInstance flowInstance) {
		
		Contact flowInstanceContact = getContactFromFlowInstanceAttributes(flowInstance.getAttributeHandler());
		
		if (flowInstanceContact != null) {
			
			return flowInstanceContact;
		}
		
		ImmutableFlowInstanceEvent latestSubmitEvent = FlowInstanceUtils.getLatestSubmitEvent(flowInstance);
		
		if (latestSubmitEvent != null && latestSubmitEvent.getPoster() != null) {
			
			Contact posterContact = getContactForUser(latestSubmitEvent.getPoster());
			
			if (posterContact != null) {
				
				return posterContact;
			}
		}
		
		return getContactForUser(flowInstance.getPoster());
	}
	
	public static void addUniqueContact(List<Contact> contacts, Contact contact) {
		
		for (Contact existingContact : contacts) {
			
			if (existingContact.getCitizenIdentifier() != null && contact.getCitizenIdentifier() != null) {
				
				if (existingContact.getCitizenIdentifier().equals(contact.getCitizenIdentifier())) {
					
					return;
				}
				
				continue;
			}
			
			if (existingContact.getEmail() != null && contact.getEmail() != null) {
				
				if (existingContact.getEmail().equalsIgnoreCase(contact.getEmail())) {
					
					return;
				}
				
				continue;
			}
			
			if (existingContact.getMobilePhone() != null && contact.getMobilePhone() != null) {
				
				String phone1 = existingContact.getMobilePhone().replaceAll("\\+", "00").replaceAll("[^0-9]+", "");
				String phone2 = contact.getMobilePhone().replaceAll("\\+", "00").replaceAll("[^0-9]+", "");
				
				if (phone1.equals(phone2)) {
					
					return;
				}
				
				continue;
			}
		}
		
		contacts.add(contact);
	}

	public static List<InvoiceLine> getPaymentInvoiceLines(FlowInstanceManager instanceManager) {
		
		List<PaymentQuery> paymentQueries = instanceManager.getQueries(PaymentQuery.class);
		
		if (paymentQueries != null) {
			
			List<InvoiceLine> invoiceLines = new ArrayList<InvoiceLine>();
			
			for (PaymentQuery paymentQuery : paymentQueries) {
				
				if (paymentQuery.getQueryInstanceDescriptor().getQueryState() != QueryState.HIDDEN) {
					
					List<? extends InvoiceLine> queryInvoiceLines = paymentQuery.getInvoiceLines();
					
					if (!CollectionUtils.isEmpty(queryInvoiceLines)) {

						invoiceLines.addAll(queryInvoiceLines);
					}
				}
			}
			
			if (invoiceLines.size() > 0) {
				
				return invoiceLines;
			}
		}
		
		return null;
	}

	public static BigDecimal getPaymentInvoiceLinesSum(List<InvoiceLine> invoiceLines) {

		BigDecimal totalSum = BigDecimal.ZERO;

		if (!CollectionUtils.isEmpty(invoiceLines)) {

			for (InvoiceLine invoiceLine : invoiceLines) {

				totalSum = totalSum.add(invoiceLine.getQuantity().multiply(invoiceLine.getUnitPrice()));
			}
			
			if (totalSum.scale() > 2) {
				totalSum = totalSum.setScale(2, RoundingMode.UP);
			}
		}

		return totalSum;
	}

	public static boolean isNewExternalMessagesAllowed(FlowInstance flowInstance, Status status) {

		if (flowInstance == null || status == null) {

			return false;
		}

		if (!flowInstance.isExternalMessagesEnabled()) {

			return false;
		}

		boolean isChangingStatus = !status.equals(flowInstance.getStatus());

		if (!isChangingStatus && status.getNewExternalMessagesAllowedDays() != null && flowInstance.getLastStatusChange() != null) {

			LocalDateTime lastStatusChange = flowInstance.getLastStatusChange().toLocalDateTime();
			LocalDateTime lastAllowedDate = lastStatusChange.plusDays(status.getNewExternalMessagesAllowedDays());

			if (LocalDateTime.now().isAfter(lastAllowedDate)) {

				return false;
			}
		}

		return !status.isNewExternalMessagesDisallowed();
	}
	
	public static boolean isOwnersContactable(FlowInstance flowInstance) {

		boolean contactable = false;
		List<Contact> contacts = getContacts(flowInstance);

		if (contacts != null) {

			for (Contact contact : contacts) {

				if ((contact.getEmail() != null && contact.isContactByEmail()) || (contact.getMobilePhone() != null && contact.isContactBySMS())) {

					contactable = true;
					break;
				}
			}
		}

		return contactable;
	}
}
