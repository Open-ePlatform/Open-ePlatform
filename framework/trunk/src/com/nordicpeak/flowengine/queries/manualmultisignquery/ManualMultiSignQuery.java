package com.nordicpeak.flowengine.queries.manualmultisignquery;

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
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.interfaces.MultiSignQuery;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

@Table(name = "manual_multi_sign_queries")
@XMLElement
public class ManualMultiSignQuery extends BaseQuery implements MultiSignQuery {

	private static final long serialVersionUID = 3734201104262524858L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;
	
	//TODO remove?
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideCitizenIdetifierInPDF;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean setMultipartsAsOwners;
	
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
	private List<ManualMultiSignQueryInstance> instances;

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	public void setQueryID(int queryID) {

		this.queryID = queryID;
	}
	
	public boolean isSetMultipartsAsOwners() {
		return setMultipartsAsOwners;
	}

	public void setSetMultipartsAsOwners(boolean setMultipartsAsOwners) {
		this.setMultipartsAsOwners = setMultipartsAsOwners;
	}

	@Override
	public String toString() {

		if(this.queryDescriptor != null) {

			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}

		return "ManualMultiSignQuery (queryID: " + queryID + ")";
	}

	@Override
	public String getXSDTypeName() {

		return "ManualMultiSignQuery" + queryID;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		setMultipartsAsOwners = xmlParser.getPrimitiveBoolean("setMultipartsAsOwners");
		
		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		if(attributeName != null){
			
			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
		}

		if(!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
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
		appendFieldDefenition("Email", false, doc, sequenceElement);
		appendFieldDefenition("MobilePhone", false, doc, sequenceElement);
		appendFieldDefenition("SocialSecurityNumber", true, doc, sequenceElement);

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

	public List<ManualMultiSignQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<ManualMultiSignQueryInstance> instances) {

		this.instances = instances;
	}

	public void setQueryID(Integer queryID) {

		this.queryID = queryID;
	}

	@Override
	public boolean requiresMultipartSigning() {

		return true;
	}
}
