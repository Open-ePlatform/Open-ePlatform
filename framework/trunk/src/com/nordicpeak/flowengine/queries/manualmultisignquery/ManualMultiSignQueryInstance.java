package com.nordicpeak.flowengine.queries.manualmultisignquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.interfaces.CitizenIdentifierQueryInstance;
import com.nordicpeak.flowengine.interfaces.ColumnExportableQueryInstance;
import com.nordicpeak.flowengine.interfaces.MultiSignQueryinstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.checkboxquery.CheckboxQueryInstance;

@Table(name = "manual_multi_sign_query_instances")
@XMLElement
public class ManualMultiSignQueryInstance extends BaseQueryInstance implements MultiSignQueryinstance, ColumnExportableQueryInstance, CitizenIdentifierQueryInstance {

	private static final long serialVersionUID = 2847121037559137804L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(CheckboxQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private ManualMultiSignQuery query;

	@DAOManaged
	@XMLElement
	private String firstname;

	@DAOManaged
	@XMLElement
	private String lastname;

	@DAOManaged
	@XMLElement
	private String email;
	
	@DAOManaged
	@XMLElement
	private String mobilePhone;

	@DAOManaged
	@XMLElement
	private String socialSecurityNumber;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		this.firstname = null;
		this.lastname = null;
		this.email = null;
		this.mobilePhone = null;
		this.socialSecurityNumber = null;
		
		if (query.isSetAsAttribute()) {
			
			resetAttribute(attributeHandler);
		}

		super.reset(attributeHandler);
	}

	public void resetAttribute(MutableAttributeHandler attributeHandler) {
		
		attributeHandler.removeAttribute(query.getAttributeName() + ".firstname");
		attributeHandler.removeAttribute(query.getAttributeName() + ".lastname");
		attributeHandler.removeAttribute(query.getAttributeName() + ".email");
		attributeHandler.removeAttribute(query.getAttributeName() + ".mobilePhone");
		attributeHandler.removeAttribute(query.getAttributeName() + ".citizenIdentifier");
	}
	
	public void setAttribute(MutableAttributeHandler attributeHandler) {
		
		attributeHandler.setAttribute(query.getAttributeName() + ".firstname", firstname);
		attributeHandler.setAttribute(query.getAttributeName() + ".lastname", lastname);
		attributeHandler.setAttribute(query.getAttributeName() + ".email", email);
		attributeHandler.setAttribute(query.getAttributeName() + ".mobilePhone", mobilePhone);
		attributeHandler.setAttribute(query.getAttributeName() + ".citizenIdentifier", socialSecurityNumber);
	}

	public void copyQueryValues() {

	}

	@Override
	public String toString() {

		return "ManualMultiSignQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}

	@Override
	public ManualMultiSignQuery getQuery() {

		return query;
	}

	public void setQuery(ManualMultiSignQuery query) {

		this.query = query;
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		XMLUtils.appendNewCDATAElement(doc, element, "Firstname", firstname);
		XMLUtils.appendNewCDATAElement(doc, element, "Lastname", lastname);
		XMLUtils.appendNewCDATAElement(doc, element, "Email", email);
		XMLUtils.appendNewCDATAElement(doc, element, "MobilePhone", mobilePhone);
		XMLUtils.appendNewCDATAElement(doc, element, "SocialSecurityNumber", socialSecurityNumber);

		return element;
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

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public String getSocialSecurityNumber() {

		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {

		this.socialSecurityNumber = socialSecurityNumber;
	}

	@Override
	public String getCitizenIdentifier() {

		return getSocialSecurityNumber();
	}
	
	@Override
	public List<? extends SigningParty> getSigningParties() {

		if (getQueryInstanceDescriptor().isPopulated()) {
		
			return Collections.singletonList(new SigningParty(firstname, lastname, email, mobilePhone, socialSecurityNumber, getQuery().isSetMultipartsAsOwners()));
			
		} else {
			
			return null;
		}
	}

	
	public String getMobilePhone() {
	
		return mobilePhone;
	}

	
	public void setMobilePhone(String mobilePhone) {
	
		this.mobilePhone = mobilePhone;
	}

	@Override
	public List<String> getColumnLabels(QueryHandler queryHandler) {

		ManualMultiSignQueryProviderModule queryProvider = queryHandler.getQueryProvider(getQueryInstanceDescriptor().getQueryDescriptor().getQueryTypeID(), ManualMultiSignQueryProviderModule.class);

		List<String> labels = new ArrayList<String>();

		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportFirstName());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportLastName());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportEmail());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportMobilePhone());
		labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + queryProvider.getExportSocialSecurityNumber());

		return labels;
	}

	@Override
	public List<String> getColumnValues() {

		List<String> values = new ArrayList<String>();
		
		values.add(getFirstname());
		values.add(getLastname());
		values.add(getEmail());
		values.add(getMobilePhone());
		values.add(getSocialSecurityNumber());

		return values;
	}

	@Override
	public boolean isTestCitizenIdentifier() {

		return false;
	}

}
