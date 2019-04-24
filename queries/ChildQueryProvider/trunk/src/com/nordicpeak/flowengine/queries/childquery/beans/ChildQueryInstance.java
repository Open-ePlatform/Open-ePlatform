package com.nordicpeak.flowengine.queries.childquery.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;
import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.interfaces.CitizenIdentifierQueryInstance;
import com.nordicpeak.flowengine.interfaces.ColumnExportableQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.MultiSignQueryinstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.childquery.ChildQueryProviderModule;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;

@Table(name = "child_query_instances")
@XMLElement
public class ChildQueryInstance extends BaseQueryInstance implements StringValueQueryInstance, MultiSignQueryinstance, FixedAlternativesQueryInstance, ColumnExportableQueryInstance, CitizenIdentifierQueryInstance {
	
	private static final long serialVersionUID = -7761759005604863873L;
	
	public static final Field QUERY_RELATION = ReflectionUtils.getField(ChildQueryInstance.class, "query");
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;
	
	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private ChildQuery query;
	
	@DAOManaged
	@XMLElement
	private String citizenIdentifier;
	
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
	private String zipcode;
	
	@DAOManaged
	@XMLElement
	private String postalAddress;
	
	@DAOManaged
	private String municipalityCode;
	
	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@XMLElement(name = "Guardians")
	private List<StoredGuardian> storedGuardians;
	
	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@XMLElement(fixCase = true)
	private List<ChildAttribute> childAttributes;
	
	@XMLElement
	private boolean hasChildrenUnderSecrecy;
	
	private Map<String, StoredChild> children;
	
	private ChildRelationProviderException fetchChildrenException;
	
	private boolean testChild;
	
	public Integer getQueryInstanceID() {
		
		return queryInstanceID;
	}
	
	public void setQueryInstanceID(Integer queryInstanceID) {
		
		this.queryInstanceID = queryInstanceID;
	}
	
	@Override
	public ChildQuery getQuery() {
		
		return query;
	}
	
	public void setQuery(ChildQuery query) {
		
		this.query = query;
	}
	
	@Override
	public String getCitizenIdentifier() {
		
		return citizenIdentifier;
	}
	
	@Override
	public boolean isTestCitizenIdentifier() {
		return testChild;
	}

	public boolean isTestChild() {
		return testChild;
	}

	public void setTestChild(boolean testChild) {
		this.testChild = testChild;
	}

	public void setCitizenIdentifier(String citizenIdentifier) {
		
		this.citizenIdentifier = citizenIdentifier;
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
	
	public Map<String, StoredChild> getChildren() {
		
		return children;
	}
	
	public void setChildren(Map<String, StoredChild> children) {
		
		this.children = children;
	}
	
	public List<StoredGuardian> getGuardians() {
		
		return storedGuardians;
	}
	
	public void setGuardians(List<StoredGuardian> storedGuardians) {
		
		this.storedGuardians = storedGuardians;
	}
	
	public String getAddress() {
		
		return address;
	}
	
	public void setAddress(String address) {
		
		this.address = address;
	}
	
	public String getZipcode() {
		
		return zipcode;
	}
	
	public void setZipcode(String zipcode) {
		
		this.zipcode = zipcode;
	}
	
	public String getPostalAddress() {
		
		return postalAddress;
	}
	
	public void setPostalAddress(String postalAddress) {
		
		this.postalAddress = postalAddress;
	}
	
	public boolean isHasChildrenUnderSecrecy() {
		return hasChildrenUnderSecrecy;
	}
	
	public void setHasChildrenUnderSecrecy(boolean hasChildrenUnderSecrecy) {
		this.hasChildrenUnderSecrecy = hasChildrenUnderSecrecy;
	}
	
	public void setAttributes(MutableAttributeHandler attributeHandler) {
		
		// Backward compatibility
		attributeHandler.setAttribute("childFirstname", firstname);
		attributeHandler.setAttribute("childLastname", lastname);
		attributeHandler.setAttribute("childCitizenIdentifier", citizenIdentifier);
		
		if (query.isSetAsAttribute() && query.getAttributeName() != null) {
			
			attributeHandler.setAttribute(query.getAttributeName() + ".childFirstname", firstname);
			attributeHandler.setAttribute(query.getAttributeName() + ".childLastname", lastname);
			attributeHandler.setAttribute(query.getAttributeName() + ".childCitizenIdentifier", citizenIdentifier);
			
			//TODO set child attributes
		}
	}
	
	@Override
	public void reset(MutableAttributeHandler attributeHandler) {
		
		//TODO clear child attributes
		
		citizenIdentifier = null;
		firstname = null;
		lastname = null;
		children = null;
		storedGuardians = null;
		address = null;
		zipcode = null;
		postalAddress = null;
		testChild = false;
		childAttributes = null;
		
		// Backward compatibility
		attributeHandler.removeAttribute("childFirstname");
		attributeHandler.removeAttribute("childLastname");
		attributeHandler.removeAttribute("childCitizenIdentifier");

		if (query.isSetAsAttribute() && query.getAttributeName() != null) {
			
			attributeHandler.removeAttribute(query.getAttributeName() + ".childFirstname");
			attributeHandler.removeAttribute(query.getAttributeName() + ".childLastname");
			attributeHandler.removeAttribute(query.getAttributeName() + ".childCitizenIdentifier");			
		}
		
		super.reset(attributeHandler);
	}
	
	public void defaultQueryValues() {
		
	}
	
	public ChildRelationProviderException getFetchChildrenException() {
		return fetchChildrenException;
	}
	
	public void setFetchChildrenException(ChildRelationProviderException fetchChildrenExceptions) {
		this.fetchChildrenException = fetchChildrenExceptions;
	}
	
	@Override
	public String toString() {
		
		return getClass().getSimpleName() + " (queryInstanceID=" + queryInstanceID + ")";
	}
	
	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {
		
		Element element = getBaseExportXML(doc);
		
		XMLUtils.appendNewElement(doc, element, "CitizenIdentifier", citizenIdentifier);
		XMLUtils.appendNewElement(doc, element, "Firstname", firstname);
		XMLUtils.appendNewElement(doc, element, "Lastname", lastname);
		XMLUtils.appendNewElement(doc, element, "Address", address);
		XMLUtils.appendNewElement(doc, element, "Zipcode", zipcode);
		XMLUtils.appendNewElement(doc, element, "PostalAddress", postalAddress);
		
		XMLUtils.append(doc, element, "Guardians", storedGuardians);
		
		return element;
	}
	
	@Override
	public String getStringValue() {
		
		StringBuilder builder = new StringBuilder();
		
		if (firstname != null) {
			
			builder.append(firstname);
		}
		
		if (lastname != null) {
			
			if (firstname != null) {
				
				builder.append(" ");
			}
			
			builder.append(lastname);
		}
		
		if (citizenIdentifier != null) {
			
			if (firstname != null || lastname != null) {
				
				builder.append(" ");
			}
			
			builder.append("(");
			builder.append(citizenIdentifier);
			builder.append(")");
		}
		
		return builder.toString();
	}
	
	@Override
	public List<? extends SigningParty> getSigningParties() {
		
		if (!getQuery().isUseMultipartSigning()) {
			return null;
		}
		
		if (storedGuardians == null || storedGuardians.size() == 1) {
			return null;
		}
		
		List<SigningParty> signingParties = new ArrayList<SigningParty>();
		
		for (StoredGuardian storedGuardian : storedGuardians) {
			
			if (!storedGuardian.isPoster()) {
				signingParties.add(new SigningParty(storedGuardian.getFirstname(), storedGuardian.getLastname(), storedGuardian.getEmail(), storedGuardian.getPhone(), storedGuardian.getCitizenIdentifier().toString(), getQuery().isSetMultipartsAsOwners()));
			}
		}
		
		return signingParties;
	}
	
	@Override
	public Element toXML(Document doc) {
		
		Element element = super.toXML(doc);
		
		if (!CollectionUtils.isEmpty(children)) {
			
			XMLUtils.append(doc, element, "Children", children.values());
			
		} else if (children != null) {
			
			XMLUtils.appendNewElement(doc, element, "Children");
		}
		
		if (fetchChildrenException != null) {
			
			Element fetchChildrenExceptionElement = XMLUtils.appendNewElement(doc, element, "FetchChildrenException");
			fetchChildrenExceptionElement.appendChild(fetchChildrenException.toXML(doc));
		}
		
		return element;
	}
	
	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {
		
		List<ImmutableAlternative> alternatives = null;
		
		if (citizenIdentifier != null) {
			
			alternatives = CollectionUtils.addAndInstantiateIfNeeded(alternatives, query.getChildSelectedAlternative());
		}
		
		if(storedGuardians != null){
			
			if(storedGuardians.size() == 1){
				
				alternatives = CollectionUtils.addAndInstantiateIfNeeded(alternatives, query.getSingleGuardianAlternative());
				
			}else if(storedGuardians.size() == 2){
				
				alternatives = CollectionUtils.addAndInstantiateIfNeeded(alternatives, query.getMultiGuardianAlternative());
			}
		}
		
		return alternatives;
	}
	
	@Override
	public String getFreeTextAlternativeValue() {
		
		return null;
	}
	
	@Override
	public List<String> getColumnLabels(QueryHandler queryHandler) {
		
		ChildQueryProviderModule provider = queryHandler.getQueryProvider(getQueryInstanceDescriptor().getQueryDescriptor().getQueryTypeID(), ChildQueryProviderModule.class);
		
		return provider.getExportLabels(getQuery());
	}
	
	@Override
	public List<String> getColumnValues() {

		List<String> values = new ArrayList<String>();

		values.add(firstname + " " + lastname);
		values.add(citizenIdentifier);

		if (query.isShowAddress()) {

			values.add(address);
			values.add(postalAddress);
			values.add(zipcode);
		}

		StoredGuardian otherGuardian = null;

		if (storedGuardians != null) {

			for (StoredGuardian storedGuardian : storedGuardians) {

				if (!storedGuardian.isPoster()) {
					otherGuardian = storedGuardian;
				}
			}
		}

		if (otherGuardian != null) {

			values.add(otherGuardian.getFirstname() + " " + otherGuardian.getLastname());
			
			if (!query.isHideSSNForOtherGuardians()) {
				values.add(otherGuardian.getCitizenIdentifier());
			}
			
			values.add(otherGuardian.getEmail());
			values.add(otherGuardian.getPhone());

			if (getQuery().isShowGuardianAddress()) {

				values.add(otherGuardian.getAddress());
				values.add(otherGuardian.getPostalAddress());
				values.add(otherGuardian.getZipcode());
			}

		} else {

			values.add("");
			
			if (!query.isHideSSNForOtherGuardians()) {
				values.add("");
			}
			
			values.add("");
			values.add("");

			if (getQuery().isShowGuardianAddress()) {

				values.add("");
				values.add("");
				values.add("");
			}
		}

		return values;
	}

	public String getMunicipalityCode() {

		return municipalityCode;
	}

	public void setMunicipalityCode(String municipalityCode) {

		this.municipalityCode = municipalityCode;
	}

	public List<ChildAttribute> getChildAttributes() {
		return childAttributes;
	}

	public void setChildAttributes(List<ChildAttribute> childAttributes) {
		this.childAttributes = childAttributes;
	}

}
