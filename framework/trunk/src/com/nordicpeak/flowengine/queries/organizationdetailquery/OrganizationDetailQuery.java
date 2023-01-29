package com.nordicpeak.flowengine.queries.organizationdetailquery;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.RequiredIfSet;
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

import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

@Table(name = "organization_detail_queries")
@XMLElement
public class OrganizationDetailQuery extends BaseQuery {

	private static final long serialVersionUID = 2716884146368159522L;
	
	private static final EnumPopulator<OrganizationDetailQueryField> ORGANIZATION_FIELD_POPULATOR = new EnumPopulator<OrganizationDetailQueryField>(OrganizationDetailQueryField.class);

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideNotificationChannelSettings;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean allowSMS;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireEmailOrMobile;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean validateZipCode;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private OrganizationDetailQueryField fieldAddress = OrganizationDetailQueryField.VISIBLE;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private OrganizationDetailQueryField fieldEmail = OrganizationDetailQueryField.REQUIRED;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private OrganizationDetailQueryField fieldMobilePhone = OrganizationDetailQueryField.VISIBLE;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private OrganizationDetailQueryField fieldPhone = OrganizationDetailQueryField.VISIBLE;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean setAsAttribute;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@RequiredIfSet(paramNames = "setAsAttribute")
	@XMLElement
	private String attributeName;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<OrganizationDetailQueryInstance> instances;

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	public List<OrganizationDetailQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<OrganizationDetailQueryInstance> instances) {

		this.instances = instances;
	}

	public void setQueryID(int queryID) {

		this.queryID = queryID;
	}

	public boolean isAllowSMS() {

		return allowSMS;
	}

	public void setAllowSMS(boolean allowSMS) {

		this.allowSMS = allowSMS;
	}
	
	public boolean isHideNotificationChannelSettings() {

		return hideNotificationChannelSettings;
	}

	public void setHideNotificationChannelSettings(boolean hideNotificationChannelSettings) {

		this.hideNotificationChannelSettings = hideNotificationChannelSettings;
	}

	@Override
	public String toString() {

		if (this.queryDescriptor != null) {

			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}

		return "OrganizationDetailQuery (queryID: " + queryID + ")";
	}

	@Override
	public String getXSDTypeName() {

		return "OrganizationDetailQuery" + queryID;
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

		appendFieldDefenition("OrganizationName", true, doc, sequenceElement);
		appendFieldDefenition("OrganizationNumber", true, doc, sequenceElement);
		appendFieldDefenition("CitizenIdentifier", false, doc, sequenceElement);

		if (fieldAddress != OrganizationDetailQueryField.HIDDEN) {
			
			appendFieldDefenition("Address", fieldAddress == OrganizationDetailQueryField.REQUIRED, doc, sequenceElement);
			appendFieldDefenition("ZipCode", fieldAddress == OrganizationDetailQueryField.REQUIRED, doc, sequenceElement);
			appendFieldDefenition("PostalAddress", fieldAddress == OrganizationDetailQueryField.REQUIRED, doc, sequenceElement);
		}

		appendFieldDefenition("Firstname", true, doc, sequenceElement);
		appendFieldDefenition("Lastname", true, doc, sequenceElement);
		

		if (fieldPhone != OrganizationDetailQueryField.HIDDEN) {
			
			appendFieldDefenition("Phone", fieldPhone == OrganizationDetailQueryField.REQUIRED, doc, sequenceElement);
		}

		if (fieldEmail != OrganizationDetailQueryField.HIDDEN) {
			
			appendFieldDefenition("Email", fieldEmail == OrganizationDetailQueryField.REQUIRED, doc, sequenceElement);
		}

		if (fieldMobilePhone != OrganizationDetailQueryField.HIDDEN) {
			
			appendFieldDefenition("MobilePhone", fieldMobilePhone == OrganizationDetailQueryField.REQUIRED, doc, sequenceElement);
		}

		Element smsElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		smsElement.setAttribute("name", "ContactBySMS");
		smsElement.setAttribute("type", "xs:boolean");
		smsElement.setAttribute("minOccurs", "1");
		smsElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(smsElement);

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

		hideNotificationChannelSettings = xmlParser.getPrimitiveBoolean("hideNotificationChannelSettings");
		allowSMS = xmlParser.getPrimitiveBoolean("allowSMS");
		requireEmailOrMobile = xmlParser.getPrimitiveBoolean("requireEmailOrMobile");
		validateZipCode = xmlParser.getPrimitiveBoolean("validateZipCode");
		
		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		if (attributeName != null) {

			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
		}		
		
		fieldAddress = XMLValidationUtils.validateParameter("fieldAddress", xmlParser, false, 1, 8, ORGANIZATION_FIELD_POPULATOR, errors);
		
		fieldEmail = XMLValidationUtils.validateParameter("fieldEmail", xmlParser, false, 1, 8, ORGANIZATION_FIELD_POPULATOR, errors);
		
		fieldMobilePhone = XMLValidationUtils.validateParameter("fieldMobilePhone", xmlParser, false, 1, 8, ORGANIZATION_FIELD_POPULATOR, errors);
		
		fieldPhone = XMLValidationUtils.validateParameter("fieldPhone", xmlParser, false, 1, 8, ORGANIZATION_FIELD_POPULATOR, errors);
		
		//Fallback if importing older version
		if(fieldAddress == null) {
			
			// Old setting
			if(xmlParser.getPrimitiveBoolean("requireAddress")) {
				
				fieldAddress = OrganizationDetailQueryField.REQUIRED;
						
			} else {
				
				fieldAddress = OrganizationDetailQueryField.VISIBLE;
			}
		}
		
		//Fallback if importing older version
		if(fieldEmail == null) {
			
			if(allowSMS) {
				
				fieldEmail = OrganizationDetailQueryField.VISIBLE;
				requireEmailOrMobile = true;
						
			} else {
				
				fieldEmail = OrganizationDetailQueryField.REQUIRED;
				requireEmailOrMobile = false;
			}
		}

		//Fallback if importing older version
		if(fieldMobilePhone == null) {
			
			fieldMobilePhone = OrganizationDetailQueryField.VISIBLE;
		}

		//Fallback if importing older version
		if(fieldPhone == null) {
			
			fieldPhone = OrganizationDetailQueryField.VISIBLE;
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

	}

	public boolean isValidateZipCode() {
		
		return validateZipCode;
	}

	public boolean isRequireEmailOrMobile() {
		return requireEmailOrMobile;
	}

	public void setRequireEmailOrMobile(boolean requireEmailOrMobile) {
		
		this.requireEmailOrMobile = requireEmailOrMobile;
	}

	public OrganizationDetailQueryField getFieldAddress() {
		
		return fieldAddress;
	}

	public void setFieldAddress(OrganizationDetailQueryField fieldAddress) {
		
		this.fieldAddress = fieldAddress;
	}

	public OrganizationDetailQueryField getFieldEmail() {
		
		return fieldEmail;
	}

	public void setFieldEmail(OrganizationDetailQueryField fieldEmail) {
		
		this.fieldEmail = fieldEmail;
	}

	public OrganizationDetailQueryField getFieldMobilePhone() {
		
		return fieldMobilePhone;
	}

	public void setFieldMobilePhone(OrganizationDetailQueryField fieldMobilePhone) {
		
		this.fieldMobilePhone = fieldMobilePhone;
	}

	public OrganizationDetailQueryField getFieldPhone() {
		
		return fieldPhone;
	}

	public void setFieldPhone(OrganizationDetailQueryField fieldPhone) {
		
		this.fieldPhone = fieldPhone;
	}

	public void setValidateZipCode(boolean validateZipCode) {
		
		this.validateZipCode = validateZipCode;
	}
	
	public boolean isSetAsAttribute() {

		return setAsAttribute;
	}

	public void setSetAsAttribute(boolean setAsAttribute) {

		this.setAsAttribute = setAsAttribute;
	}

	public String getAttributeName() {

		return attributeName;
	}

	public void setAttributeName(String attributeName) {

		this.attributeName = attributeName;
	}

}
