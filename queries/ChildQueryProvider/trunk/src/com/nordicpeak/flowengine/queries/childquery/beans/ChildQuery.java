package com.nordicpeak.flowengine.queries.childquery.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.FCKContent;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.Templated;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;
import se.unlogic.webutils.annotations.URLRewrite;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.interfaces.CitizenIdentifierQuery;
import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.MultiSignQuery;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQuery;

@Table(name = "child_queries")
@XMLElement
public class ChildQuery extends BaseQuery implements FixedAlternativesQuery, MultiSignQuery, CitizenIdentifierQuery {

	private static final long serialVersionUID = -842191226937409429L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@FCKContent
	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement(cdata = true)
	private String otherGuardiansDescription;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean showAddress;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean showGuardianAddress;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean alwaysShowOtherGuardians;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideSSNForOtherGuardians;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean useMultipartSigning;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean skipMultipartSigningIfSameAddress;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean setMultipartsAsOwners;

	@DAOManaged
	@XMLElement
	private boolean requireGuardianEmail;

	@DAOManaged
	@XMLElement
	private boolean requireGuardianPhone;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireGuardianContactInfoVerification;

	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer minAge;

	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer maxAge;

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
	@WebPopulate
	@XMLElement
	private boolean setSecondGuardianAsAttribute;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@RequiredIfSet(paramNames = "setSecondGuardianAsAttribute")
	@XMLElement
	private String secondGuardianAttributeName;

	@DAOManaged
	@XMLElement
	private String filterEndpoint;

	@TextTagReplace
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String emptyFilterDescription;

	@TextTagReplace
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String filteredChildrenDescription;

	@TextTagReplace
	@DAOManaged
	@Templated
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String communicationErrorDescription;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@XMLElement(fixCase = true)
	private List<SelectedChildAttribute> selectedChildAttributes;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<ChildQueryInstance> instances;

	private ImmutableAlternative childSelectedAlternative;

	private ImmutableAlternative singleGuardianAlternative;

	private ImmutableAlternative multiGuardianAlternative;

	public boolean isShowAddress() {

		return showAddress;
	}

	public void setShowAddress(boolean showAddress) {

		this.showAddress = showAddress;
	}

	public boolean isShowGuardianAddress() {

		return showGuardianAddress;
	}

	public void setShowGuardianAddress(boolean showGuardianAddress) {

		this.showGuardianAddress = showGuardianAddress;
	}

	public boolean isAlwaysShowOtherGuardians() {

		return alwaysShowOtherGuardians;
	}

	public void setAlwaysShowOtherGuardians(boolean alwaysShowOtherGuardians) {

		this.alwaysShowOtherGuardians = alwaysShowOtherGuardians;
	}

	public boolean isHideSSNForOtherGuardians() {

		return hideSSNForOtherGuardians;
	}

	public void setHideSSNForOtherGuardians(boolean hideSSNForOtherGuardians) {

		this.hideSSNForOtherGuardians = hideSSNForOtherGuardians;
	}

	public boolean isRequireGuardianEmail() {

		return requireGuardianEmail;
	}

	public void setRequireGuardianEmail(boolean requireGuardianEmail) {

		this.requireGuardianEmail = requireGuardianEmail;
	}

	public boolean isRequireGuardianPhone() {

		return requireGuardianPhone;
	}

	public void setRequireGuardianPhone(boolean requireGuardianPhone) {

		this.requireGuardianPhone = requireGuardianPhone;
	}

	public boolean isUseMultipartSigning() {

		return useMultipartSigning;
	}

	public void setUseMultipartSigning(boolean multipartSigning) {

		this.useMultipartSigning = multipartSigning;
	}

	public boolean isRequireGuardianContactInfoVerification() {

		return requireGuardianContactInfoVerification;
	}

	public void setRequireGuardianContactInfoVerification(boolean requireGuardianContactInfoVerification) {

		this.requireGuardianContactInfoVerification = requireGuardianContactInfoVerification;
	}

	public Integer getMinAge() {

		return minAge;
	}

	public void setMinAge(Integer minAge) {

		this.minAge = minAge;
	}

	public Integer getMaxAge() {

		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {

		this.maxAge = maxAge;
	}

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	public List<ChildQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<ChildQueryInstance> instances) {

		this.instances = instances;
	}

	public void setQueryID(int queryID) {

		this.queryID = queryID;
	}

	public void setQueryID(Integer queryID) {

		this.queryID = queryID;
	}

	public String getOtherGuardiansDescription() {

		return otherGuardiansDescription;
	}

	public void setOtherGuardiansDescription(String otherGuardiansDescription) {

		this.otherGuardiansDescription = otherGuardiansDescription;
	}

	public boolean isSetMultipartsAsOwners() {

		return setMultipartsAsOwners;
	}

	public void setSetMultipartsAsOwners(boolean setMultipartsAsOwners) {

		this.setMultipartsAsOwners = setMultipartsAsOwners;
	}

	public boolean isSkipMultipartSigningIfSameAddress() {

		return skipMultipartSigningIfSameAddress;
	}

	public void setSkipMultipartSigningIfSameAddress(boolean skipMultipartSigningIfSameAddress) {

		this.skipMultipartSigningIfSameAddress = skipMultipartSigningIfSameAddress;
	}

	@Override
	public String toString() {

		if (this.queryDescriptor != null) {

			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}

		return getClass().getSimpleName() + " (queryID: " + queryID + ")";
	}

	@Override
	public String getXSDTypeName() {

		return getClass().getSimpleName() + queryID;
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

		Element citizenIdentifierElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		citizenIdentifierElement.setAttribute("name", "CitizenIdentifier");
		citizenIdentifierElement.setAttribute("type", "xs:string");
		citizenIdentifierElement.setAttribute("minOccurs", "1");
		citizenIdentifierElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(citizenIdentifierElement);

		Element firstnameElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		firstnameElement.setAttribute("name", "Firstname");
		firstnameElement.setAttribute("type", "xs:string");
		firstnameElement.setAttribute("minOccurs", "1");
		firstnameElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(firstnameElement);

		Element lastnameElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		lastnameElement.setAttribute("name", "Lastname");
		lastnameElement.setAttribute("type", "xs:string");
		lastnameElement.setAttribute("minOccurs", "1");
		lastnameElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(lastnameElement);

		Element addressElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		addressElement.setAttribute("name", "Address");
		addressElement.setAttribute("type", "xs:string");
		addressElement.setAttribute("minOccurs", "1");
		addressElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(addressElement);

		Element addressUUIDElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		addressUUIDElement.setAttribute("name", "AddressUUID");
		addressUUIDElement.setAttribute("type", "xs:string");
		addressUUIDElement.setAttribute("minOccurs", "0");
		addressUUIDElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(addressUUIDElement);

		Element zipcodeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		zipcodeElement.setAttribute("name", "Zipcode");
		zipcodeElement.setAttribute("type", "xs:string");
		zipcodeElement.setAttribute("minOccurs", "1");
		zipcodeElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(zipcodeElement);

		Element postalAddressElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		postalAddressElement.setAttribute("name", "PostalAddress");
		postalAddressElement.setAttribute("type", "xs:string");
		postalAddressElement.setAttribute("minOccurs", "1");
		postalAddressElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(postalAddressElement);

		if (useMultipartSigning || alwaysShowOtherGuardians) {

			Element guardiansElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
			guardiansElement.setAttribute("name", "Guardians");
			guardiansElement.setAttribute("type", getXSDTypeName() + "Guardians");
			guardiansElement.setAttribute("minOccurs", "1");
			guardiansElement.setAttribute("maxOccurs", "1");
			sequenceElement.appendChild(guardiansElement);

			// Guardians type
			Element guardiansComplexTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexType");
			guardiansComplexTypeElement.setAttribute("name", getXSDTypeName() + "Guardians");

			Element guardiansSequenceElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:sequence");
			guardiansComplexTypeElement.appendChild(guardiansSequenceElement);

			Element guardianElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
			guardianElement.setAttribute("name", "Guardian");
			guardianElement.setAttribute("type", getXSDTypeName() + "Guardian");
			guardianElement.setAttribute("minOccurs", "1");
			guardianElement.setAttribute("maxOccurs", "2");
			guardiansSequenceElement.appendChild(guardianElement);

			doc.getDocumentElement().appendChild(guardiansComplexTypeElement);

			// Guardian type
			Element guardianComplexTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexType");
			guardianComplexTypeElement.setAttribute("name", getXSDTypeName() + "Guardian");

			Element guardianSequenceElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:sequence");
			guardianComplexTypeElement.appendChild(guardianSequenceElement);

			addElementType(doc, guardianSequenceElement, "firstname", "xs:string");
			addElementType(doc, guardianSequenceElement, "lastname", "xs:string");
			addElementType(doc, guardianSequenceElement, "citizenIdentifier", "xs:string");
			addElementType(doc, guardianSequenceElement, "email", "xs:string", requireGuardianEmail);
			addElementType(doc, guardianSequenceElement, "phone", "xs:string", requireGuardianPhone);
			addElementType(doc, guardianSequenceElement, "AddressUUID", "xs:string", false);

			doc.getDocumentElement().appendChild(guardianComplexTypeElement);
		}

		doc.getDocumentElement().appendChild(complexTypeElement);
	}

	private void addElementType(Document doc, Element sequenceElement, String name, String type) {

		addElementType(doc, sequenceElement, name, type, true);
	}

	private void addElementType(Document doc, Element sequenceElement, String name, String type, boolean required) {

		Element element = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		element.setAttribute("name", name);
		element.setAttribute("type", type);
		element.setAttribute("minOccurs", required ? "1" : "0");
		element.setAttribute("maxOccurs", "1");

		sequenceElement.appendChild(element);
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		otherGuardiansDescription = XMLValidationUtils.validateParameter("otherGuardiansDescription", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		useMultipartSigning = xmlParser.getPrimitiveBoolean("useMultipartSigning");
		skipMultipartSigningIfSameAddress = xmlParser.getPrimitiveBoolean("skipMultipartSigningIfSameAddress");
		alwaysShowOtherGuardians = xmlParser.getPrimitiveBoolean("alwaysShowOtherGuardians");
		hideSSNForOtherGuardians = xmlParser.getPrimitiveBoolean("hideSSNForOtherGuardians");
		showAddress = xmlParser.getPrimitiveBoolean("showAddress");
		showGuardianAddress = xmlParser.getPrimitiveBoolean("showGuardianAddress");
		requireGuardianEmail = xmlParser.getPrimitiveBoolean("requireGuardianEmail");
		requireGuardianPhone = xmlParser.getPrimitiveBoolean("requireGuardianPhone");
		requireGuardianContactInfoVerification = xmlParser.getPrimitiveBoolean("requireGuardianContactInfoVerification");
		minAge = XMLValidationUtils.validateParameter("minAge", xmlParser, false, IntegerPopulator.getPopulator(), errors);
		maxAge = XMLValidationUtils.validateParameter("maxAge", xmlParser, false, IntegerPopulator.getPopulator(), errors);
		setMultipartsAsOwners = xmlParser.getPrimitiveBoolean("setMultipartsAsOwners");
		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		secondGuardianAttributeName = XMLValidationUtils.validateParameter("secondGuardianAttributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		emptyFilterDescription = XMLValidationUtils.validateParameter("emptyFilterDescription", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		filteredChildrenDescription = XMLValidationUtils.validateParameter("filteredChildrenDescription", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		communicationErrorDescription = XMLValidationUtils.validateParameter("communicationErrorDescription", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);

		if (attributeName != null) {

			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
		}

		if (secondGuardianAttributeName != null) {

			setSecondGuardianAsAttribute = xmlParser.getPrimitiveBoolean("setSecondGuardianAsAttribute");
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
	}

	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {

		return CollectionUtils.getList(childSelectedAlternative, singleGuardianAlternative, multiGuardianAlternative);
	}

	@Override
	public String getFreeTextAlternative() {

		return null;
	}

	@Override
	public Map<Integer, Integer> getAlternativeConversionMap() {

		return null;
	}

	@Override
	public boolean requiresMultipartSigning() {

		return this.useMultipartSigning;
	}

	public ImmutableAlternative getChildSelectedAlternative() {

		return childSelectedAlternative;
	}

	public void setChildSelectedAlternative(ImmutableAlternative childSelectedAlternative) {

		this.childSelectedAlternative = childSelectedAlternative;
	}

	public ImmutableAlternative getSingleGuardianAlternative() {

		return singleGuardianAlternative;
	}

	public void setSingleGuardianAlternative(ImmutableAlternative singleGuardianAlternative) {

		this.singleGuardianAlternative = singleGuardianAlternative;
	}

	public ImmutableAlternative getMultiGuardianAlternative() {

		return multiGuardianAlternative;
	}

	public void setMultiGuardianAlternative(ImmutableAlternative multiGuardianAlternative) {

		this.multiGuardianAlternative = multiGuardianAlternative;
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

	public String getFilterEndpointName() {

		return filterEndpoint;
	}

	public boolean isSetSecondGuardianAsAttribute() {

		return setSecondGuardianAsAttribute;
	}

	public void setSetSecondGuardianAsAttribute(boolean setSecondGuardianAsAttribute) {

		this.setSecondGuardianAsAttribute = setSecondGuardianAsAttribute;
	}

	public String getSecondGuardianAttributeName() {

		return secondGuardianAttributeName;
	}

	public void setSecondGuardianAttributeName(String secondGuardianAttributeName) {

		this.secondGuardianAttributeName = secondGuardianAttributeName;
	}

	public void setFilterEndpointName(String filterEndpointName) {

		this.filterEndpoint = filterEndpointName;
	}

	public List<SelectedChildAttribute> getSelectedChildAttributes() {

		return selectedChildAttributes;
	}

	public void setSelectedChildAttributes(List<SelectedChildAttribute> selectedChildAttributes) {

		this.selectedChildAttributes = selectedChildAttributes;
	}

	public String getEmptyFilterDescription() {

		return emptyFilterDescription;
	}

	public void setEmptyFilterDescription(String emptyFilterDescription) {

		this.emptyFilterDescription = emptyFilterDescription;
	}

	public String getFilteredChildrenDescription() {

		return filteredChildrenDescription;
	}

	public void setFilteredChildrenDescription(String filteredChildrenDescription) {

		this.filteredChildrenDescription = filteredChildrenDescription;
	}

	public String getCommunicationErrorDescription() {

		return communicationErrorDescription;
	}

	public void setCommunicationErrorDescription(String communicationErrorDescription) {

		this.communicationErrorDescription = communicationErrorDescription;
	}

}
