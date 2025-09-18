package com.nordicpeak.flowengine.queries.stopquery;

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

@Table(name = "stop_queries")
@XMLElement
public class StopQuery extends BaseQuery {

	private static final long serialVersionUID = 8732607760189948382L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean displayValidationError;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<StopQueryInstance> instances;

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	public List<StopQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<StopQueryInstance> instances) {

		this.instances = instances;
	}

	public void setQueryID(int queryID) {

		this.queryID = queryID;
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

		doc.getDocumentElement().appendChild(complexTypeElement);
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);

		displayValidationError = xmlParser.getPrimitiveBoolean("displayValidationError");
		
		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
	}

	
	public boolean isDisplayValidationError() {
	
		return displayValidationError;
	}

	
	public void setDisplayValidationError(boolean displayValidationError) {
	
		this.displayValidationError = displayValidationError;
	}
}
