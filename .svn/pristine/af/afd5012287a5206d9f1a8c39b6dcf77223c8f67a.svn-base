package com.nordicpeak.flowengine.queries.fixedalternativesquery;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.string.StringUtils;

import com.nordicpeak.flowengine.interfaces.ExportAlternativeID;
import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

public abstract class FixedAlternativesBaseQuery extends BaseQuery implements FixedAlternativesQuery {

	private static final long serialVersionUID = 7987991587750437624L;

	private Map<Integer, Integer> alternativeConversionMap;

	@Override
	public Map<Integer, Integer> getAlternativeConversionMap() {

		return alternativeConversionMap;
	}

	public void setAlternativeConversionMap(Map<Integer, Integer> alternativeConversionMap) {

		this.alternativeConversionMap = alternativeConversionMap;
	}

	public void toXSD(Document doc, int maxOccurs) {

		toXSD(doc, 1, maxOccurs);
	}

	public void toXSD(Document doc, int minOccurs, int maxOccurs) {

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

		doc.getDocumentElement().appendChild(complexTypeElement);

		if (this.getAlternatives() != null) {

			
			if (this instanceof ExportAlternativeID &&  ((ExportAlternativeID)this).isExportAlternativeID()) {
				
				Element complexValueIDTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
				complexValueIDTypeElement.setAttribute("name", "Alternative");
				complexValueIDTypeElement.setAttribute("type", getXSDTypeName() + "Alternative");
				complexValueIDTypeElement.setAttribute("minOccurs", Integer.toString(minOccurs));
				complexValueIDTypeElement.setAttribute("maxOccurs", Integer.toString(maxOccurs));

				sequenceElement.appendChild(complexValueIDTypeElement);

				Element complexValueIDTypeAlternativeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexType");
				complexValueIDTypeAlternativeElement.setAttribute("name", getXSDTypeName() + "Alternative");

				Element sequenceAlternativeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:sequence");
				Element alternativeIDElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
				alternativeIDElement.setAttribute("name", "ID");
				alternativeIDElement.setAttribute("type", getXSDTypeName() + "AlternativeID");

				alternativeIDElement.setAttribute("minOccurs", "0");
				alternativeIDElement.setAttribute("maxOccurs", "1");

				sequenceAlternativeElement.appendChild(alternativeIDElement);

				Element alternativeValueElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
				alternativeValueElement.setAttribute("name", "Value");
				alternativeValueElement.setAttribute("type", getXSDTypeName() + "AlternativeValue");

				alternativeValueElement.setAttribute("minOccurs", "0");
				alternativeValueElement.setAttribute("maxOccurs", "1");

				sequenceAlternativeElement.appendChild(alternativeValueElement);

				complexValueIDTypeAlternativeElement.appendChild(sequenceAlternativeElement);

				doc.getDocumentElement().appendChild(complexValueIDTypeAlternativeElement);

				Element simpleAlternativeTypeIDElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:simpleType");
				simpleAlternativeTypeIDElement.setAttribute("name", getXSDTypeName() + "AlternativeID");

				Element simpleAlternativeTypeIDRestrictionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:restriction");
				simpleAlternativeTypeIDRestrictionElement.setAttribute("base", "xs:string");
				simpleAlternativeTypeIDElement.appendChild(simpleAlternativeTypeIDRestrictionElement);

				for (ImmutableAlternative alternative : this.getAlternatives()) {

					Element enumerationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:enumeration");

					enumerationElement.setAttribute("value", String.valueOf(alternative.getAlternativeID()));

					simpleAlternativeTypeIDRestrictionElement.appendChild(enumerationElement);
				}

				simpleAlternativeTypeIDElement.appendChild(simpleAlternativeTypeIDRestrictionElement);

				Element simpleAlternativeTypeValueElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:simpleType");
				simpleAlternativeTypeValueElement.setAttribute("name", getXSDTypeName() + "AlternativeValue");

				Element simpleAlternativeTypeValueRestrictionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:restriction");
				simpleAlternativeTypeValueRestrictionElement.setAttribute("base", "xs:string");
				simpleAlternativeTypeValueElement.appendChild(simpleAlternativeTypeValueRestrictionElement);

				for (ImmutableAlternative alternative : this.getAlternatives()) {

					Element enumerationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:enumeration");

					if (!StringUtils.isEmpty(alternative.getExportXMLValue())) {

						enumerationElement.setAttribute("value", alternative.getExportXMLValue());

					} else {
						enumerationElement.setAttribute("value", alternative.getName());
					}

					simpleAlternativeTypeValueRestrictionElement.appendChild(enumerationElement);
				}

				simpleAlternativeTypeValueElement.appendChild(simpleAlternativeTypeValueRestrictionElement);

				if (getFreeTextAlternative() != null) {

					Element enumerationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:enumeration");
					enumerationElement.setAttribute("value", getFreeTextAlternative());
					simpleAlternativeTypeValueRestrictionElement.appendChild(enumerationElement);

					Element freeTextElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
					freeTextElement.setAttribute("name", "TextAlternative");
					freeTextElement.setAttribute("type", "xs:string");
					freeTextElement.setAttribute("minOccurs", "0");
					freeTextElement.setAttribute("maxOccurs", "1");
					sequenceElement.appendChild(freeTextElement);
				}

				doc.getDocumentElement().appendChild(simpleAlternativeTypeIDElement);
				doc.getDocumentElement().appendChild(simpleAlternativeTypeValueElement);
				
			} else {
				
				Element valueElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
				valueElement.setAttribute("name", "Value");
				valueElement.setAttribute("type", getXSDTypeName() + "Alternative");

				valueElement.setAttribute("minOccurs", Integer.toString(minOccurs));
				valueElement.setAttribute("maxOccurs", Integer.toString(maxOccurs));

				sequenceElement.appendChild(valueElement);

				Element simpleTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:simpleType");
				simpleTypeElement.setAttribute("name", getXSDTypeName() + "Alternative");

				Element restrictionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:restriction");
				restrictionElement.setAttribute("base", "xs:string");
				simpleTypeElement.appendChild(restrictionElement);

				for (ImmutableAlternative alternative : this.getAlternatives()) {

					Element enumerationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:enumeration");

					if (!StringUtils.isEmpty(alternative.getExportXMLValue())) {

						enumerationElement.setAttribute("value", alternative.getExportXMLValue());

					} else {
						enumerationElement.setAttribute("value", alternative.getName());
					}

					restrictionElement.appendChild(enumerationElement);
				}

				if (getFreeTextAlternative() != null) {

					Element enumerationElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:enumeration");
					enumerationElement.setAttribute("value", getFreeTextAlternative());
					restrictionElement.appendChild(enumerationElement);

					Element freeTextElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
					freeTextElement.setAttribute("name", "TextAlternative");
					freeTextElement.setAttribute("type", "xs:string");
					freeTextElement.setAttribute("minOccurs", "0");
					freeTextElement.setAttribute("maxOccurs", "1");
					sequenceElement.appendChild(freeTextElement);
				}

				doc.getDocumentElement().appendChild(simpleTypeElement);
			}
		}
	}
}
