package com.nordicpeak.flowengine.queries.contactdetailquery;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.interfaces.CitizenIdentifierQuery;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

@Table(name = "contact_detail_queries")
@XMLElement
public class ContactDetailQuery extends BaseQuery implements CitizenIdentifierQuery {
	
	
	private static final long serialVersionUID = -842191226937409416L;
	private static final EnumPopulator<ContactDetailQueryField> CONTACT_FIELD_POPULATOR = new EnumPopulator<ContactDetailQueryField>(ContactDetailQueryField.class);
	private static final EnumPopulator<ContactDetailQueryFieldUpdate> CONTACT_FIELD_UPDATE_POPULATOR = new EnumPopulator<ContactDetailQueryFieldUpdate>(ContactDetailQueryFieldUpdate.class);
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean allowSMS;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean useOfficalAddress;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireAtLeastOneContactWay;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ContactDetailQueryField fieldCitizenID = ContactDetailQueryField.HIDDEN;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ContactDetailQueryField fieldName = ContactDetailQueryField.REQUIRED;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ContactDetailQueryField fieldAddress = ContactDetailQueryField.VISIBLE;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ContactDetailQueryField fieldPhone = ContactDetailQueryField.VISIBLE;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ContactDetailQueryField fieldMobilePhone = ContactDetailQueryField.VISIBLE;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ContactDetailQueryField fieldEmail = ContactDetailQueryField.VISIBLE;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ContactDetailQueryField fieldCareOf = ContactDetailQueryField.HIDDEN;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ContactDetailQueryFieldUpdate fieldUpdate = ContactDetailQueryFieldUpdate.ALWAYS;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<ContactDetailQueryInstance> instances;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean managerUpdateAccess;

	public static long getSerialversionuid() {
		
		return serialVersionUID;
	}
	
	@Override
	public Integer getQueryID() {
		
		return queryID;
	}
	
	public List<ContactDetailQueryInstance> getInstances() {
		
		return instances;
	}
	
	public void setInstances(List<ContactDetailQueryInstance> instances) {
		
		this.instances = instances;
	}
	
	public void setQueryID(int queryID) {
		
		this.queryID = queryID;
	}
	
	public boolean isAllowSMSNotification() {
		return allowSMS;
	}
	
	public void setAllowSMSNotification(boolean allowSMS) {
		this.allowSMS = allowSMS;
	}
	
	@Override
	public String toString() {
		
		if (this.queryDescriptor != null) {
			
			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}
		
		return "ContactChannelQuery (queryID: " + queryID + ")";
	}
	
	@Override
	public String getXSDTypeName() {
		
		return "ContactChannelQuery" + queryID;
	}
	
	@Override
	public void toXSD(Document doc) {
		
		Element complexTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexType");
		complexTypeElement.setAttribute("name", getXSDTypeName());
		
		Element complexContentElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexContent");
		complexTypeElement.appendChild(complexContentElement);
		
		Element extensionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:extension");
		extensionElement.setAttribute("base", "Query");
		complexContentElement.appendChild(extensionElement);
		
		Element sequenceElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:sequence");
		extensionElement.appendChild(sequenceElement);
		
		Element nameElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		nameElement.setAttribute("name", "Name");
		nameElement.setAttribute("type", "xs:string");
		nameElement.setAttribute("minOccurs", "1");
		nameElement.setAttribute("maxOccurs", "1");
		nameElement.setAttribute("fixed", queryDescriptor.getName());
		sequenceElement.appendChild(nameElement);
		
		if (fieldName != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("Firstname", fieldName == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldName != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("Lastname", fieldName == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldCareOf != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("CareOf", fieldCareOf == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldAddress != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("Address", fieldAddress == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldAddress != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("ZipCode", fieldAddress == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldAddress != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("PostalAddress", fieldAddress == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldPhone != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("Phone", fieldPhone == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldEmail != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("Email", fieldEmail == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldMobilePhone != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("MobilePhone", fieldMobilePhone == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldCitizenID != ContactDetailQueryField.HIDDEN) {
			appendFieldDefenition("SocialSecurityNumber", fieldCitizenID == ContactDetailQueryField.REQUIRED, doc, sequenceElement);
		}
		
		if (fieldMobilePhone != ContactDetailQueryField.HIDDEN && allowSMS) {
			
			Element smsElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
			smsElement.setAttribute("name", "ContactBySMS");
			smsElement.setAttribute("type", "xs:boolean");
			smsElement.setAttribute("minOccurs", "1");
			smsElement.setAttribute("maxOccurs", "1");
			sequenceElement.appendChild(smsElement);
		}
		
		doc.getDocumentElement().appendChild(complexTypeElement);
	}
	
	private void appendFieldDefenition(String name, boolean required, Document doc, Element sequenceElement) {
		
		Element fieldElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		fieldElement.setAttribute("name", name);
		fieldElement.setAttribute("type", "xs:string");
		fieldElement.setAttribute("minOccurs", required ? "1" : "0");
		fieldElement.setAttribute("maxOccurs", "1");
		
		sequenceElement.appendChild(fieldElement);
	}
	
	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		
		allowSMS = xmlParser.getPrimitiveBoolean("allowSMS");
		useOfficalAddress = xmlParser.getPrimitiveBoolean("useOfficalAddress");
		requireAtLeastOneContactWay = xmlParser.getPrimitiveBoolean("requireAtLeastOneContactWay");
		
		fieldName = XMLValidationUtils.validateParameter("fieldName", xmlParser, false, 1, 8, CONTACT_FIELD_POPULATOR, errors);
		fieldAddress = XMLValidationUtils.validateParameter("fieldAddress", xmlParser, false, 1, 8, CONTACT_FIELD_POPULATOR, errors);
		fieldPhone = XMLValidationUtils.validateParameter("fieldPhone", xmlParser, false, 1, 8, CONTACT_FIELD_POPULATOR, errors);
		fieldMobilePhone = XMLValidationUtils.validateParameter("fieldMobilePhone", xmlParser, false, 1, 8, CONTACT_FIELD_POPULATOR, errors);
		fieldEmail = XMLValidationUtils.validateParameter("fieldEmail", xmlParser, false, 1, 8, CONTACT_FIELD_POPULATOR, errors);
		fieldCitizenID = XMLValidationUtils.validateParameter("fieldCitizenID", xmlParser, false, 1, 8, CONTACT_FIELD_POPULATOR, errors);
		fieldCareOf = XMLValidationUtils.validateParameter("fieldCareOf", xmlParser, false, 1, 8, CONTACT_FIELD_POPULATOR, errors);
		
		fieldUpdate = XMLValidationUtils.validateParameter("fieldUpdate", xmlParser, false, 1, 8, CONTACT_FIELD_UPDATE_POPULATOR, errors);
		managerUpdateAccess = xmlParser.getPrimitiveBoolean("managerUpdateAccess");
		
		// Fallbacks for importing old versions
		
		if (fieldName == null) {
			fieldName = ContactDetailQueryField.REQUIRED;
		}
		
		if (fieldAddress == null) {
			
			if (xmlParser.getPrimitiveBoolean("requireAddress")) {
				
				fieldAddress = ContactDetailQueryField.REQUIRED;
				
			} else {
				
				fieldAddress = ContactDetailQueryField.VISIBLE;
			}
		}
		
		if (fieldCareOf == null) {
			
			fieldCareOf = ContactDetailQueryField.HIDDEN;
		}
		
		if (fieldPhone == null) {
			
			if (xmlParser.getPrimitiveBoolean("requirePhone")) {
				
				fieldPhone = ContactDetailQueryField.REQUIRED;
				
			} else {
				
				fieldPhone = ContactDetailQueryField.VISIBLE;
			}
		}
		
		if (fieldMobilePhone == null) {
			
			if (xmlParser.getPrimitiveBoolean("requireMobilePhone")) {
				
				fieldMobilePhone = ContactDetailQueryField.REQUIRED;
				
			} else {
				
				fieldMobilePhone = ContactDetailQueryField.VISIBLE;
			}
		}
		
		if (fieldEmail == null) {
			
			if (xmlParser.getPrimitiveBoolean("requireEmail")) {
				
				fieldEmail = ContactDetailQueryField.REQUIRED;
				
			} else {
				
				fieldEmail = ContactDetailQueryField.VISIBLE;
			}
		}
		
		if (fieldCitizenID == null) {
			
			if (xmlParser.getPrimitiveBoolean("showSocialSecurityNumberField")) {
				
				fieldCitizenID = ContactDetailQueryField.VISIBLE;
				
			} else {
				
				fieldCitizenID = ContactDetailQueryField.HIDDEN;
			}
		}
		
		if (fieldUpdate == null) {
			
			if (xmlParser.getPrimitiveBoolean("disableProfileUpdate")) {
				
				fieldUpdate = ContactDetailQueryFieldUpdate.NEVER;
				
			} else {
				
				fieldUpdate = ContactDetailQueryFieldUpdate.ASK;
			}
		}
		
		if (!errors.isEmpty()) {
			
			throw new ValidationException(errors);
		}
	}
	
	public boolean usesOfficalAddress() {
		
		return useOfficalAddress;
	}
	
	public void setUseOfficalAddress(boolean useOfficalAddress) {
		
		this.useOfficalAddress = useOfficalAddress;
	}
	
	public ContactDetailQueryField getFieldCitizenID() {
		return fieldCitizenID;
	}
	
	public void setFieldCitizenID(ContactDetailQueryField fieldCitizenID) {
		this.fieldCitizenID = fieldCitizenID;
	}
	
	public ContactDetailQueryField getFieldName() {
		return fieldName;
	}
	
	public void setFieldName(ContactDetailQueryField fieldName) {
		this.fieldName = fieldName;
	}
	
	public ContactDetailQueryField getFieldAddress() {
		return fieldAddress;
	}
	
	public void setFieldAddress(ContactDetailQueryField fieldAddress) {
		this.fieldAddress = fieldAddress;
	}
	
	public ContactDetailQueryField getFieldPhone() {
		return fieldPhone;
	}
	
	public void setFieldPhone(ContactDetailQueryField fieldPhone) {
		this.fieldPhone = fieldPhone;
	}
	
	public ContactDetailQueryField getFieldMobilePhone() {
		return fieldMobilePhone;
	}
	
	public void setFieldMobilePhone(ContactDetailQueryField fieldMobilePhone) {
		this.fieldMobilePhone = fieldMobilePhone;
	}
	
	public ContactDetailQueryField getFieldEmail() {
		return fieldEmail;
	}
	
	public void setFieldEmail(ContactDetailQueryField fieldEmail) {
		this.fieldEmail = fieldEmail;
	}
	
	public ContactDetailQueryFieldUpdate getFieldUpdate() {
		return fieldUpdate;
	}
	
	public void setFieldUpdate(ContactDetailQueryFieldUpdate fieldUpdate) {
		this.fieldUpdate = fieldUpdate;
	}
	
	public boolean isRequireAtLeastOneContactWay() {
		return requireAtLeastOneContactWay;
	}
	
	public void setRequireAtLeastOneContactWay(boolean requireAtLeastOneContactWay) {
		this.requireAtLeastOneContactWay = requireAtLeastOneContactWay;
	}
	
	public ContactDetailQueryField getFieldCareOf() {
		return fieldCareOf;
	}
	
	public void setFieldCareOf(ContactDetailQueryField fieldCareOf) {
		this.fieldCareOf = fieldCareOf;
	}
	
	public boolean isManagerUpdateAccess() {
		return managerUpdateAccess;
	}
	
	public void setManagerUpdateAccess(boolean managerUpdateAccess) {
		this.managerUpdateAccess = managerUpdateAccess;
	}
}
