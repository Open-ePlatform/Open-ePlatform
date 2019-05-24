package com.nordicpeak.flowengine.queries.textfieldquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.queries.basequery.BaseQuery;
import com.nordicpeak.flowengine.queries.textfieldquery.api.TextFieldQueryEndpoint;

@Table(name = "text_field_queries")
@XMLElement
public class TextFieldQuery extends BaseQuery {

	private static final long serialVersionUID = -842191226937409416L;

	public static final Field TEXT_FIELDS_RELATION = ReflectionUtils.getField(TextFieldQuery.class, "fields");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private FieldLayout layout;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideTitle;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean lockOnOwnershipTransfer;

	@DAOManaged(dontUpdateIfNull = true)
	@OneToMany(autoUpdate = true, autoAdd = true)
	@XMLElement(fixCase = true)
	private List<TextField> fields;

	@DAOManaged(dontUpdateIfNull = true)
	@OneToMany(autoUpdate = true)
	@XMLElement
	private List<TextFieldQueryInstance> instances;

	@DAOManaged(columnName = "endpointID")
	@ManyToOne(autoAdd = true, autoGet = true, autoUpdate = true)
	@XMLElement(fixCase = true)
	private TextFieldQueryEndpoint endpoint;

	private Map<Integer, Integer> textFieldConversionMap;

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	public List<TextField> getFields() {

		return fields;
	}

	public List<TextFieldQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<TextFieldQueryInstance> instances) {

		this.instances = instances;
	}

	public TextFieldQueryEndpoint getEndpoint() {

		return endpoint;
	}

	public void setEndpoint(TextFieldQueryEndpoint endpoint) {

		this.endpoint = endpoint;
	}

	public void setQueryID(int queryID) {

		this.queryID = queryID;
	}

	public void setFields(List<TextField> alternatives) {

		this.fields = alternatives;
	}

	public boolean isHideTitle() {

		return hideTitle;
	}

	public void setHideTitle(boolean hideTitle) {

		this.hideTitle = hideTitle;
	}

	@Override
	public String toString() {

		if (this.queryDescriptor != null) {

			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}

		return "TextFieldQuery (queryID: " + queryID + ")";
	}

	public FieldLayout getLayout() {

		return layout;
	}

	public void setLayout(FieldLayout layout) {

		this.layout = layout;
	}

	public Map<Integer, Integer> getTextFieldConversionMap() {

		return textFieldConversionMap;
	}

	public void setTextFieldConversionMap(Map<Integer, Integer> textFieldConversionMap) {

		this.textFieldConversionMap = textFieldConversionMap;
	}

	@Override
	public String getXSDTypeName() {

		return "TextFieldQuery" + queryID;
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

		ArrayList<String> fieldElementNames = new ArrayList<String>(fields.size());

		if (!CollectionUtils.isEmpty(fields)) {

			for (TextField textField : fields) {

				sequenceElement.appendChild(doc.createComment(textField.getLabel()));

				String elementName;

				if (textField.getXSDElementName() != null) {

					elementName = textField.getXSDElementName();

					fieldElementNames.add(elementName);

				} else {

					elementName = generateElementName(textField.getLabel(), fieldElementNames);
				}

				Element fieldElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
				fieldElement.setAttribute("name", elementName);
				fieldElement.setAttribute("type", "xs:string");
				fieldElement.setAttribute("minOccurs", textField.isRequired() ? "1" : "0");
				fieldElement.setAttribute("maxOccurs", "1");

				sequenceElement.appendChild(fieldElement);
			}
		}

		doc.getDocumentElement().appendChild(complexTypeElement);
	}

	public static String generateElementName(String label, ArrayList<String> fieldElementNames) {

		String elementName = XMLUtils.toValidElementName(label);

		if (fieldElementNames.contains(elementName)) {

			int counter = 1;

			while (fieldElementNames.contains(elementName + counter)) {

				counter++;
			}

			elementName += counter;
		}

		fieldElementNames.add(elementName);

		return elementName;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);

		hideTitle = xmlParser.getPrimitiveBoolean("hideTitle");
		layout = XMLValidationUtils.validateParameter("layout", xmlParser, true, TextFieldCRUD.LAYOUT_POPULATOR, errors);
		lockOnOwnershipTransfer = xmlParser.getPrimitiveBoolean("lockOnOwnershipTransfer");

		List<XMLParser> xmlParsers = xmlParser.getNodes("Fields/TextField");

		if (xmlParsers != null) {

			fields = new ArrayList<TextField>(xmlParsers.size());

			for (XMLParser parser : xmlParsers) {

				TextField textField = new TextField();

				textField.populate(parser);

				textField.setQuery(this);

				fields.add(textField);

			}

		}
		//		else {
		//
		//			errors.add(new ValidationError("NoTextFieldsFound"));
		//
		//		}

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

}
