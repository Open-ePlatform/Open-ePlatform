package com.nordicpeak.flowengine.queries.basemapquery;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;
import se.unlogic.webutils.annotations.URLRewrite;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

public abstract class BaseMapQuery extends BaseQuery {

	private static final long serialVersionUID = 2230363617580102516L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 1000)
	@XMLElement
	private String startInstruction;

	@Override
	public Integer getQueryID() {
		return queryID;
	}

	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}

	public String getStartInstruction() {
		return startInstruction;
	}

	public void setStartInstruction(String startInstruction) {
		this.startInstruction = startInstruction;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		startInstruction = XMLValidationUtils.validateParameter("startInstruction", xmlParser, false, 1, 1000, StringPopulator.getPopulator(), errors);

		if(!errors.isEmpty()){

			throw new ValidationException(errors);
		}

	}

	protected void appendBaseFieldDefenitions(Document doc, Element sequenceElement) {

		appendFieldDefenition("PropertyUnitDesignation", "xs:string", false, doc, sequenceElement);
		appendFieldDefenition("PropertyUnitNumber", "xs:positiveInteger", false, doc, sequenceElement);
		appendFieldDefenition("Extent", "xs:string", false, doc, sequenceElement);
		appendFieldDefenition("EPSG", "xs:string", false, doc, sequenceElement);

		appendFieldDefenition("FirstMapImage", "xs:string", false, doc, sequenceElement);
		appendFieldDefenition("FirstMapImageDpi", "xs:positiveInteger", false, doc, sequenceElement);
		appendFieldDefenition("FirstMapImageScale", "xs:positiveInteger", false, doc, sequenceElement);
		appendFieldDefenition("FirstMapImageLayout", "xs:string", false, doc, sequenceElement);
		appendFieldDefenition("FirstMapImageFormat", "xs:string", false, doc, sequenceElement);

		appendFieldDefenition("SecondMapImage", "xs:string", false, doc, sequenceElement);
		appendFieldDefenition("SecondMapImageDpi", "xs:positiveInteger", false, doc, sequenceElement);
		appendFieldDefenition("SecondMapImageScale", "xs:positiveInteger", false, doc, sequenceElement);
		appendFieldDefenition("SecondMapImageLayout", "xs:string", false, doc, sequenceElement);
		appendFieldDefenition("SecondMapImageFormat", "xs:string", false, doc, sequenceElement);

		appendFieldDefenition("ThirdMapImage", "xs:string", false, doc, sequenceElement);
		appendFieldDefenition("ThirdMapImageDpi", "xs:positiveInteger", false, doc, sequenceElement);
		appendFieldDefenition("ThirdMapImageScale", "xs:positiveInteger", false, doc, sequenceElement);
		appendFieldDefenition("ThirdMapImageLayout", "xs:string", false, doc, sequenceElement);
		appendFieldDefenition("ThirdMapImageFormat", "xs:string", false, doc, sequenceElement);
	}

	protected void appendFieldDefenition(String name, String type, boolean required, Document doc, Element sequenceElement) {

		Element fieldElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:element");
		fieldElement.setAttribute("name", name);
		fieldElement.setAttribute("type", "xs:string");
		fieldElement.setAttribute("minOccurs", required ? "1" : "0");
		fieldElement.setAttribute("maxOccurs", "1");

		sequenceElement.appendChild(fieldElement);
	}
}
