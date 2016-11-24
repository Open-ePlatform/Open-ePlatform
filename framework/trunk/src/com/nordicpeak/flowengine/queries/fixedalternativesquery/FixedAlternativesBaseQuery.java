package com.nordicpeak.flowengine.queries.fixedalternativesquery;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;


public abstract class FixedAlternativesBaseQuery extends BaseQuery implements FixedAlternativesQuery {

	private static final long serialVersionUID = 7987991587750437624L;

	private Map<Integer,Integer> alternativeConversionMap;

	@Override
	public Map<Integer, Integer> getAlternativeConversionMap() {

		return alternativeConversionMap;
	}

	public void setAlternativeConversionMap(Map<Integer, Integer> alternativeConversionMap) {

		this.alternativeConversionMap = alternativeConversionMap;
	}

	public void toXSD(Document doc, int maxOccurs) {

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

		doc.getDocumentElement().appendChild(complexTypeElement);

		if(this.getAlternatives() != null){

			Element valueElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:element");
			valueElement.setAttribute("name", "Value");
			valueElement.setAttribute("type", getXSDTypeName() + "Alternative");

			valueElement.setAttribute("minOccurs", "1");
			valueElement.setAttribute("maxOccurs", Integer.toString(maxOccurs));

			sequenceElement.appendChild(valueElement);

			Element simpleTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:simpleType");
			simpleTypeElement.setAttribute("name", getXSDTypeName() + "Alternative");

			Element restrictionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:restriction");
			restrictionElement.setAttribute("base", "xs:string");
			simpleTypeElement.appendChild(restrictionElement);

			for(ImmutableAlternative alternative : this.getAlternatives()){

				Element enumerationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:enumeration");
				enumerationElement.setAttribute("value", alternative.getName());
				restrictionElement.appendChild(enumerationElement);
			}

			if(getFreeTextAlternative() != null){

				Element enumerationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:enumeration");
				enumerationElement.setAttribute("value", getFreeTextAlternative());
				restrictionElement.appendChild(enumerationElement);

				Element freeTextElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema","xs:element");
				freeTextElement.setAttribute("name", "TextAlternative");
				freeTextElement.setAttribute("type", "xs:string");
				freeTextElement.setAttribute("minOccurs", "0");
				freeTextElement.setAttribute("maxOccurs", "1");
				sequenceElement.appendChild(freeTextElement);
			}

			doc.getDocumentElement().appendChild(simpleTypeElement);
		}
	}

	@Override
	public List<? extends FixedAlternativesQueryInstance> getInstances(List<Integer> queryInstanceIDs, QueryHandler queryHandler) throws SQLException {

		return FixedAlternativeQueryUtils.getGenericFixedAlternativesQueryCallback(this.getClass(), queryHandler, getQueryDescriptor().getQueryTypeID()).getQueryInstances(this, queryInstanceIDs);
	}
}
