package com.nordicpeak.flowengine.queries.pudquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.RequiredIfNotSet;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQuery;

@Table(name = "pud_query_queries")
@XMLElement
public class PUDQuery extends BaseQuery implements FixedAlternativesQuery {

	private static final long serialVersionUID = -1356231798937567819L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;
	
	@DAOManaged
	@OneToMany(autoAdd = true, autoUpdate = true, autoGet = true)
	@SimplifiedRelation(table = "pud_query_allowed_search_services", remoteValueColumnName = "service")
	@WebPopulate(maxLength = 15, paramName = "allowedSearchService")
	@RequiredIfNotSet(paramNames = "useAddressAsResult")
	@XMLElement(fixCase = true, childName = "allowedSearchService")
	private List<SearchService> allowedSearchServices;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean useAddressAsResult;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean setAsAttribute;
	
	@DAOManaged
	@WebPopulate(maxLength=255)
	@RequiredIfSet(paramNames = "setAsAttribute")
	@XMLElement
	private String attributeName;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<PUDQueryInstance> instances;
	
	private PUDAlternative isPopulatedAlternative;
	private PUDAlternative isNotPopulatedAlternative;

	public List<SearchService> getAllowedSearchServices() {
		return allowedSearchServices;
	}

	public void setAllowedSearchServices(List<SearchService> allowedSearchServices) {
		this.allowedSearchServices = allowedSearchServices;
	}

	public List<PUDQueryInstance> getInstances() {
		return instances;
	}

	public void setInstances(List<PUDQueryInstance> instances) {
		this.instances = instances;
	}

	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}

	@Override
	public Integer getQueryID() {
		return this.queryID;
	}

	@Override
	public String toString() {

		if (this.queryDescriptor != null) {

			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}

		return "PUDQuery (queryID: " + queryID + ")";
	}

	@Override
	public String getXSDTypeName() {

		return "PUDQuery" + this.getQueryID();
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

		if (useAddressAsResult) {
			
			Element addressElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
			addressElement.setAttribute("name", "Address");
			addressElement.setAttribute("type", "xs:string");
			addressElement.setAttribute("minOccurs", "0");
			addressElement.setAttribute("maxOccurs", "1");
			sequenceElement.appendChild(addressElement);
			
		} else {
			
			Element propertyUnitDesignationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
			propertyUnitDesignationElement.setAttribute("name", "PropertyUnitDesignation");
			propertyUnitDesignationElement.setAttribute("type", "xs:string");
			propertyUnitDesignationElement.setAttribute("minOccurs", "0");
			propertyUnitDesignationElement.setAttribute("maxOccurs", "1");
			sequenceElement.appendChild(propertyUnitDesignationElement);
			
			Element propertyUnitNumberElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
			propertyUnitNumberElement.setAttribute("name", "PropertyUnitNumber");
			propertyUnitNumberElement.setAttribute("type", "xs:positiveInteger");
			propertyUnitNumberElement.setAttribute("minOccurs", "0");
			propertyUnitNumberElement.setAttribute("maxOccurs", "1");
			sequenceElement.appendChild(propertyUnitNumberElement);
			
		}

		doc.getDocumentElement().appendChild(complexTypeElement);
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		useAddressAsResult = xmlParser.getPrimitiveBoolean("useAddressAsResult");
		
		XMLParser serviceParser = xmlParser.getNode("AllowedSearchServices");
		
		if (serviceParser != null) {
			
			allowedSearchServices = XMLValidationUtils.validateParameters("allowedSearchService", serviceParser, false, PUDQueryCRUD.SEARCH_SERVICE_POPULATOR, errors);
		}
		
		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		if (attributeName != null) {
			
			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
		}
		
		if (!errors.isEmpty()) {
			
			throw new ValidationException(errors);
		}
		
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
	
	public boolean isUseAddressAsResult() {
		return useAddressAsResult;
	}
	
	public void setUseAddressAsResult(boolean useAddressAsResult) {
		this.useAddressAsResult = useAddressAsResult;
	}

	public PUDAlternative getIsPopulatedAlternative() {
		return isPopulatedAlternative;
	}

	public PUDAlternative getIsNotPopulatedAlternative() {
		return isNotPopulatedAlternative;
	}
	
	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {
		
		return CollectionUtils.getList(isPopulatedAlternative, isNotPopulatedAlternative);
	}
	
	@Override
	public String getFreeTextAlternative() {
		return null;
	}
	
	@Override
	public Map<Integer, Integer> getAlternativeConversionMap() {
		return null;
	}
	
	public void setIsPopulatedAlternative(PUDAlternative isPopulatedAlternative) {
		this.isPopulatedAlternative = isPopulatedAlternative;
	}
	
	public void setIsNotPopulatedAlternative(PUDAlternative isNotPopulatedAlternative) {
		this.isNotPopulatedAlternative = isNotPopulatedAlternative;
	}
	
}
