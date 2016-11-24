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
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

@Table(name = "contact_detail_queries")
@XMLElement
public class ContactDetailQuery extends BaseQuery {

	private static final long serialVersionUID = -842191226937409416L;

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
	private boolean requireAddress;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireEmail;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requirePhone;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireMobilePhone;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<ContactDetailQueryInstance> instances;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean showSocialSecurityNumberField;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean disableProfileUpdate;

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

	public boolean isAllowSMS() {
		return allowSMS;
	}

	public void setAllowSMS(boolean allowSMS) {
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

		Element complexTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:complexType");
		complexTypeElement.setAttribute("name", getXSDTypeName());

		Element complexContentElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:complexContent");
		complexTypeElement.appendChild(complexContentElement);

		Element extensionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:extension");
		extensionElement.setAttribute("base", "Query");
		complexContentElement.appendChild(extensionElement);

		Element sequenceElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:sequence");
		extensionElement.appendChild(sequenceElement);

		Element nameElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:element");
		nameElement.setAttribute("name", "Name");
		nameElement.setAttribute("type", "xs:string");
		nameElement.setAttribute("minOccurs", "1");
		nameElement.setAttribute("maxOccurs", "1");
		nameElement.setAttribute("fixed", queryDescriptor.getName());
		sequenceElement.appendChild(nameElement);

		appendFieldDefenition("Firstname", true, doc, sequenceElement);
		appendFieldDefenition("Lastname", true, doc, sequenceElement);
		appendFieldDefenition("Address", requireAddress, doc, sequenceElement);
		appendFieldDefenition("ZipCode", requireAddress, doc, sequenceElement);
		appendFieldDefenition("PostalAddress", requireAddress, doc, sequenceElement);
		appendFieldDefenition("Phone", false, doc, sequenceElement);
		appendFieldDefenition("Email", true, doc, sequenceElement);
		appendFieldDefenition("MobilePhone", false, doc, sequenceElement);
		appendFieldDefenition("SocialSecurityNumber", showSocialSecurityNumberField, doc, sequenceElement);

		Element smsElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:element");
		smsElement.setAttribute("name", "ContactBySMS");
		smsElement.setAttribute("type", "xs:boolean");
		smsElement.setAttribute("minOccurs", "1");
		smsElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(smsElement);

		doc.getDocumentElement().appendChild(complexTypeElement);
	}

	private void appendFieldDefenition(String name, boolean required, Document doc, Element sequenceElement) {

		Element fieldElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:element");
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
		requireAddress = xmlParser.getPrimitiveBoolean("requireAddress");
		showSocialSecurityNumberField = xmlParser.getPrimitiveBoolean("showSocialSecurityNumberField");
		disableProfileUpdate = xmlParser.getPrimitiveBoolean("disableProfileUpdate");
		
		if(!errors.isEmpty()){

			throw new ValidationException(errors);
		}

	}
	
	public boolean requiresAddress() {
	
		return requireAddress;
	}
	
	public void setRequireAddress(boolean requireAddress) {
	
		this.requireAddress = requireAddress;
	}

	
	public boolean isShowSocialSecurityNumberField() {
	
		return showSocialSecurityNumberField;
	}

	
	public void setShowSocialSecurityNumberField(boolean showSocialSecurityNumberField) {
	
		this.showSocialSecurityNumberField = showSocialSecurityNumberField;
	}

	
	public boolean requiresPhone() {
	
		return requirePhone;
	}

	
	public void setRequirePhone(boolean requirePhone) {
	
		this.requirePhone = requirePhone;
	}

	
	public boolean requiresMobilePhone() {
	
		return requireMobilePhone;
	}

	
	public void setRequireMobilePhone(boolean requireMobilePhone) {
	
		this.requireMobilePhone = requireMobilePhone;
	}

	
	public boolean requiresEmail() {
	
		return requireEmail;
	}

	
	public void setRequireEmail(boolean requireEmail) {
	
		this.requireEmail = requireEmail;
	}

	
	public boolean usesOfficalAddress() {
	
		return useOfficalAddress;
	}

	
	public void setUseOfficalAddress(boolean useOfficalAddress) {
	
		this.useOfficalAddress = useOfficalAddress;
	}

	
	public boolean isDisableProfileUpdate() {
	
		return disableProfileUpdate;
	}

	
	public void setDisableProfileUpdate(boolean disableProfileUpdate) {
	
		this.disableProfileUpdate = disableProfileUpdate;
	}
}
