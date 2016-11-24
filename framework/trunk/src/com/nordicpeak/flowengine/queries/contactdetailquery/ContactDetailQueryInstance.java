package com.nordicpeak.flowengine.queries.contactdetailquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.interfaces.ContactQueryInstance;
import com.nordicpeak.flowengine.interfaces.ExportableQueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;

@Table(name = "contact_detail_query_instances")
@XMLElement
public class ContactDetailQueryInstance extends BaseQueryInstance implements StringValueQueryInstance, ExportableQueryInstance, ContactQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static Field QUERY_RELATION = ReflectionUtils.getField(ContactDetailQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private ContactDetailQuery query;

	@DAOManaged
	@XMLElement
	private String firstname;

	@DAOManaged
	@XMLElement
	private String lastname;

	@DAOManaged
	@XMLElement
	private String address;

	@DAOManaged
	@XMLElement
	private String zipCode;

	@DAOManaged
	@XMLElement
	private String postalAddress;

	@DAOManaged
	@XMLElement
	private String phone;

	@DAOManaged
	@XMLElement
	private String email;

	@DAOManaged
	@XMLElement
	private String mobilePhone;

	@DAOManaged
	@XMLElement
	private boolean contactBySMS;

	@DAOManaged
	@XMLElement
	private boolean persistUserProfile;

	@DAOManaged
	@XMLElement
	private String socialSecurityNumber;
	
	@DAOManaged
	@XMLElement
	private boolean officalAddress;

	@XMLElement
	private boolean isMutableUserWithAccess;

	public String getAddress() {

		return address;
	}

	public String getFirstname() {

		return firstname;
	}

	public void setFirstname(String firstname) {

		this.firstname = firstname;
	}

	public String getLastname() {

		return lastname;
	}

	public void setLastname(String lastname) {

		this.lastname = lastname;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getZipCode() {

		return zipCode;
	}

	public void setZipCode(String zipCode) {

		this.zipCode = zipCode;
	}

	public String getPostalAddress() {

		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {

		this.postalAddress = postalAddress;
	}

	public String getPhone() {

		return phone;
	}

	public void setPhone(String phone) {

		this.phone = phone;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getMobilePhone() {

		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {

		this.mobilePhone = mobilePhone;
	}

	public boolean isContactBySMS() {

		return contactBySMS;
	}

	public void setContactBySMS(boolean contactBySMS) {

		this.contactBySMS = contactBySMS;
	}

	public boolean isPersistUserProfile() {

		return persistUserProfile;
	}

	public void setPersistUserProfile(boolean persistUserProfile) {

		this.persistUserProfile = persistUserProfile;
	}

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public ContactDetailQuery getQuery() {

		return query;
	}

	public void setQuery(ContactDetailQuery query) {

		this.query = query;
	}

	public boolean isMutableUserWithAccess() {

		return isMutableUserWithAccess;
	}
	
	public void setMutableUserWithAccess(boolean isMutableUserWithAccess) {
	
		this.isMutableUserWithAccess = isMutableUserWithAccess;
	}

	public boolean isPopulated() {

		if (StringUtils.isEmpty(firstname) && StringUtils.isEmpty(lastname) && StringUtils.isEmpty(address) && StringUtils.isEmpty(zipCode) && StringUtils.isEmpty(postalAddress) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(email) && StringUtils.isEmpty(mobilePhone)) {

			return false;
		}

		return true;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		//Preserve fields in case they have been prepopulated

		super.reset(attributeHandler);
	}

	public void fullReset(MutableAttributeHandler attributeHandler) {

		this.firstname = null;
		this.lastname = null;
		this.address = null;
		this.zipCode = null;
		this.postalAddress = null;
		this.phone = null;
		this.email = null;
		this.mobilePhone = null;
		this.contactBySMS = false;

		reset(attributeHandler);
	}
	
	@Override
	public String toString() {

		return "ContactDetailQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}

	public void initialize(User poster) {

		this.firstname = poster.getFirstname();
		this.lastname = poster.getLastname();
		this.email = poster.getEmail();

		AttributeHandler attributeHandler = poster.getAttributeHandler();

		if (attributeHandler != null) {

			this.address = attributeHandler.getString("address");
			this.zipCode = attributeHandler.getString("zipCode");
			this.postalAddress = attributeHandler.getString("postalAddress");
			this.mobilePhone = attributeHandler.getString("mobilePhone");
			this.phone = attributeHandler.getString("phone");

			this.contactBySMS = attributeHandler.getPrimitiveBoolean("contactBySMS");
			
			if(query.usesOfficalAddress()){
				
				String officialAddress = attributeHandler.getString("official.address");
				String officialZipCode = attributeHandler.getString("official.zipCode");
				String officialPostalAddress = attributeHandler.getString("official.postalAddress");
				
				if(!StringUtils.isEmpty(officialAddress, officialZipCode, officialPostalAddress)){
					
					this.address = officialAddress;
					this.zipCode = officialZipCode;
					this.postalAddress = officialPostalAddress;
					this.officalAddress = true;
				}
			}
		}
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		XMLUtils.appendNewCDATAElement(doc, element, "Firstname", firstname);
		XMLUtils.appendNewCDATAElement(doc, element, "Lastname", lastname);
		XMLUtils.appendNewCDATAElement(doc, element, "Address", address);
		XMLUtils.appendNewCDATAElement(doc, element, "ZipCode", zipCode);
		XMLUtils.appendNewCDATAElement(doc, element, "PostalAddress", postalAddress);
		XMLUtils.appendNewCDATAElement(doc, element, "Phone", phone);
		XMLUtils.appendNewCDATAElement(doc, element, "Email", email);
		XMLUtils.appendNewCDATAElement(doc, element, "MobilePhone", mobilePhone);
		XMLUtils.appendNewCDATAElement(doc, element, "SocialSecurityNumber", socialSecurityNumber);
		XMLUtils.appendNewCDATAElement(doc, element, "ContactBySMS", contactBySMS);

		return element;
	}

	@Override
	public String getStringValue() {

		StringBuilder builder = new StringBuilder();

		builder.append(firstname + " " + lastname);

		if (address != null) {

			builder.append("\n" + address);
		}

		if (zipCode != null && postalAddress != null) {

			builder.append("\n" + zipCode + " " + postalAddress);
		}

		if (phone != null) {

			builder.append("\n" + phone);
		}

		if (email != null) {

			builder.append("\n" + email);
		}

		return builder.toString();
	}

	public String getSocialSecurityNumber() {

		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {

		this.socialSecurityNumber = socialSecurityNumber;
	}

	@Override
	public List<String> getExportValueLabels(QueryHandler queryHandler) {

		ContactDetailQueryProviderModule queryProvider = queryHandler.getQueryProvider(getQueryInstanceDescriptor().getQueryDescriptor().getQueryTypeID(), ContactDetailQueryProviderModule.class);

		List<String> labels = new ArrayList<String>();

		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportFirstName());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportLastName());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportAddress());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportZipCode());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportPostalAddress());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportPhone());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportEmail());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportMobilePhone());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportSocialSecurityNumber());

		return labels;
	}

	@Override
	public List<String> getExportValues() {

		List<String> values = new ArrayList<String>();

		values.add(getFirstname());
		values.add(getLastname());
		values.add(getAddress());
		values.add(getZipCode());
		values.add(getPostalAddress());
		values.add(getPhone());
		values.add(getEmail());
		values.add(getMobilePhone());
		values.add(getSocialSecurityNumber());

		return values;
	}

	
	public boolean isOfficalAddress() {
	
		return officalAddress;
	}

	
	public void setOfficalAddress(boolean officalAddress) {
	
		this.officalAddress = officalAddress;
	}

	@Override
	public Contact getContact() {
		
		if (isPopulated()) {
			
			Contact contact = new Contact();
			
			contact.setFirstname(getFirstname());
			contact.setLastname(getLastname());
			contact.setEmail(getEmail());
			contact.setMobilePhone(getMobilePhone());
			contact.setPhone(getPhone());
			contact.setAddress(getAddress());
			contact.setPostalAddress(getPostalAddress());
			contact.setZipCode(getZipCode());
			contact.setCitizenIdentifier(getSocialSecurityNumber());
			
			contact.setContactBySMS(isContactBySMS());
			contact.setContactByEmail(true);
			
			return contact;
		}
		
		return null;
	}
	
}
