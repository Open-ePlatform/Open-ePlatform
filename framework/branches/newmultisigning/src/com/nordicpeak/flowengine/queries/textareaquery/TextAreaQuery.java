package com.nordicpeak.flowengine.queries.textareaquery;

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
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

@Table(name = "text_area_queries")
@XMLElement
public class TextAreaQuery extends BaseQuery {
	
	private static final long serialVersionUID = -842191226937409416L;
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer maxLength;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer rows;
	
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
	private boolean hideDescriptionInPDF;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideTitle;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean lockOnOwnershipTransfer;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<TextAreaQueryInstance> instances;
	
	public static long getSerialversionuid() {
		
		return serialVersionUID;
	}
	
	@Override
	public Integer getQueryID() {
		
		return queryID;
	}
	
	public List<TextAreaQueryInstance> getInstances() {
		
		return instances;
	}
	
	public void setInstances(List<TextAreaQueryInstance> instances) {
		
		this.instances = instances;
	}
	
	public void setQueryID(Integer queryID) {
		
		this.queryID = queryID;
	}
	
	@Override
	public String toString() {
		
		if (this.queryDescriptor != null) {
			
			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}
		
		return "TextAreaQuery (queryID: " + queryID + ")";
	}
	
	public Integer getMaxLength() {
		
		return maxLength;
	}
	
	public void setMaxLength(Integer maxLength) {
		
		this.maxLength = maxLength;
	}
	
	public Integer getRows() {
		
		return rows;
	}
	
	public void setRows(Integer rows) {
		
		this.rows = rows;
	}
	
	@Override
	public String getXSDTypeName() {
		
		return "TextAreaQuery" + queryID;
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
		
		Element valueElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		valueElement.setAttribute("name", "Value");
		valueElement.setAttribute("minOccurs", "1");
		valueElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(valueElement);
		
		Element simpleTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:simpleType");
		valueElement.appendChild(simpleTypeElement);
		
		Element restrictionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:restriction");
		restrictionElement.setAttribute("base", "xs:string");
		simpleTypeElement.appendChild(restrictionElement);
		
		Element maxLengthElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:maxLength");
		
		if (maxLength != null) {
			
			maxLengthElement.setAttribute("value", maxLength.toString());
			
		} else {
			
			maxLengthElement.setAttribute("value", "65535");
		}
		
		restrictionElement.appendChild(maxLengthElement);
		
		doc.getDocumentElement().appendChild(complexTypeElement);
	}
	
	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		
		maxLength = XMLValidationUtils.validateParameter("maxLength", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		
		rows = XMLValidationUtils.validateParameter("rows", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		
		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		if (attributeName != null) {
			
			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
		}
		
		hideDescriptionInPDF = xmlParser.getPrimitiveBoolean("hideDescriptionInPDF");
		hideTitle = xmlParser.getPrimitiveBoolean("hideTitle");
		lockOnOwnershipTransfer = xmlParser.getPrimitiveBoolean("lockOnOwnershipTransfer");
		
		if (!errors.isEmpty()) {
			
			throw new ValidationException(errors);
		}
		
	}
	
	public boolean isLockOnOwnershipTransfer() {
		return lockOnOwnershipTransfer;
	}
	
	public void setLockOnOwnershipTransfer(boolean lockOnOwnershipTransfer) {
		this.lockOnOwnershipTransfer = lockOnOwnershipTransfer;
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

	
	public boolean isHideDescriptionInPDF() {
	
		return hideDescriptionInPDF;
	}

	
	public void setHideDescriptionInPDF(boolean hideDescriptionInPDF) {
	
		this.hideDescriptionInPDF = hideDescriptionInPDF;
	}

	
	public boolean isHideTitle() {
	
		return hideTitle;
	}

	
	public void setHideTitle(boolean hideTitle) {
	
		this.hideTitle = hideTitle;
	}
}
