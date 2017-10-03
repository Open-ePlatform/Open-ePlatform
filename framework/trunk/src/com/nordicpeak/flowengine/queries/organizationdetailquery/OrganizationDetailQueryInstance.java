package com.nordicpeak.flowengine.queries.organizationdetailquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
import com.nordicpeak.flowengine.interfaces.ColumnExportableQueryInstance;
import com.nordicpeak.flowengine.interfaces.ContactQueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;

@Table(name = "organization_detail_query_instances")
@XMLElement
public class OrganizationDetailQueryInstance extends BaseQueryInstance implements StringValueQueryInstance, ColumnExportableQueryInstance, ContactQueryInstance {

	private static final long serialVersionUID = -2166602898244004279L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(OrganizationDetailQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private OrganizationDetailQuery query;

	@DAOManaged
	@XMLElement
	private String name;

	@DAOManaged
	@XMLElement
	private String organizationNumber;

	@DAOManaged
	private String citizenIdentifier;

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
	private String firstname;

	@DAOManaged
	@XMLElement
	private String lastname;

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
	private Integer organizationID;

	@DAOManaged
	@XMLElement
	private boolean persistOrganization;

	@XMLElement
	private boolean isUserOrganizationsMutableWithAccess;

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getOrganizationNumber() {

		return organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {

		this.organizationNumber = organizationNumber;
	}

	public String getAddress() {

		return address;
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

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public OrganizationDetailQuery getQuery() {

		return query;
	}

	public void setQuery(OrganizationDetailQuery query) {

		this.query = query;
	}

	public Integer getOrganizationID() {

		return organizationID;
	}

	public void setOrganizationID(Integer organizationID) {

		this.organizationID = organizationID;
	}

	public boolean isPersistOrganization() {

		return persistOrganization;
	}

	public void setPersistOrganization(boolean persistOrganization) {

		this.persistOrganization = persistOrganization;
	}

	public boolean isPopulated() {

		if (StringUtils.isEmpty(name) && StringUtils.isEmpty(organizationNumber) && StringUtils.isEmpty(address) && StringUtils.isEmpty(zipCode) && StringUtils.isEmpty(postalAddress) && StringUtils.isEmpty(phone) && StringUtils.isEmpty(email) && StringUtils.isEmpty(mobilePhone)) {

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

		this.name = null;
		this.organizationNumber = null;
		this.address = null;
		this.zipCode = null;
		this.postalAddress = null;
		this.phone = null;
		this.email = null;
		this.mobilePhone = null;
		this.contactBySMS = false;
		this.organizationID = null;

		reset(attributeHandler);
	}

	@Override
	public String toString() {

		return "OrganizationDetailQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		XMLUtils.appendNewCDATAElement(doc, element, "OrganizationName", name);
		XMLUtils.appendNewCDATAElement(doc, element, "OrganizationNumber", organizationNumber);
		XMLUtils.appendNewCDATAElement(doc, element, "Address", address);
		XMLUtils.appendNewCDATAElement(doc, element, "ZipCode", zipCode);
		XMLUtils.appendNewCDATAElement(doc, element, "PostalAddress", postalAddress);
		XMLUtils.appendNewCDATAElement(doc, element, "Firstname", firstname);
		XMLUtils.appendNewCDATAElement(doc, element, "Lastname", lastname);
		XMLUtils.appendNewCDATAElement(doc, element, "Phone", phone);
		XMLUtils.appendNewCDATAElement(doc, element, "Email", email);
		XMLUtils.appendNewCDATAElement(doc, element, "MobilePhone", mobilePhone);
		XMLUtils.appendNewCDATAElement(doc, element, "ContactBySMS", contactBySMS);

		return element;
	}

	@Override
	public String getStringValue() {

		StringBuilder builder = new StringBuilder();

		builder.append(name);

		builder.append("\n" + organizationNumber);

		builder.append("\n" + firstname + " " + lastname);

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

	@Override
	public List<String> getColumnLabels(QueryHandler queryHandler) {

		OrganizationDetailQueryProviderModule queryProvider = queryHandler.getQueryProvider(getQueryInstanceDescriptor().getQueryDescriptor().getQueryTypeID(), OrganizationDetailQueryProviderModule.class);

		List<String> labels = new ArrayList<String>();

		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportOrganizationName());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportOrganizationNumber());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportContactFirstName());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportContactLastName());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportAddress());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportZipCode());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportPostalAddress());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportPhone());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportEmail());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportMobilePhone());

		return labels;
	}

	@Override
	public List<String> getColumnValues() {

		List<String> values = new ArrayList<String>();

		values.add(getName());
		values.add(getOrganizationNumber());
		values.add(getFirstname());
		values.add(getLastname());
		values.add(getAddress());
		values.add(getZipCode());
		values.add(getPostalAddress());
		values.add(getPhone());
		values.add(getEmail());
		values.add(getMobilePhone());

		return values;
	}

	public boolean isUserOrganizationsMutableWithAccess() {

		return isUserOrganizationsMutableWithAccess;
	}

	public void setUserOrganizationsMutableWithAccess(boolean isUserOrganizationsMutableWithAccess) {

		this.isUserOrganizationsMutableWithAccess = isUserOrganizationsMutableWithAccess;
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
			contact.setOrganizationName(getName());
			contact.setOrganizationNumber(getOrganizationNumber());

			contact.setContactBySMS(isContactBySMS());
			contact.setContactByEmail(true);
			contact.setCitizenIdentifier(citizenIdentifier);

			return contact;
		}

		return null;
	}

	public String getCitizenIdentifier() {

		return citizenIdentifier;
	}

	public void setCitizenIdentifier(String citizenIdentifier) {

		this.citizenIdentifier = citizenIdentifier;
	}

	@Override
	public boolean isPersist() {
		return isPersistOrganization();
	}

}
