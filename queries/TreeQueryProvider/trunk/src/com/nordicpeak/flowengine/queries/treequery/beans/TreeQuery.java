package com.nordicpeak.flowengine.queries.treequery.beans;

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

@Table(name = "tree_queries")
@XMLElement
public class TreeQuery extends BaseQuery {

	private static final long serialVersionUID = -842191226937409429L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@DAOManaged
	@WebPopulate(maxLength = 255, required = true)
	@XMLElement
	private String providerIdentifier;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean onlyAllowSelectingLeafs;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<TreeQueryInstance> instances;

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	public List<TreeQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<TreeQueryInstance> instances) {

		this.instances = instances;
	}

	public boolean isOnlyAllowSelectingLeafs() {

		return onlyAllowSelectingLeafs;
	}

	public void setOnlyAllowSelectingLeafs(boolean onlyAllowSelectingLeafs) {

		this.onlyAllowSelectingLeafs = onlyAllowSelectingLeafs;
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

		doc.getDocumentElement().appendChild(complexTypeElement);
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		providerIdentifier = XMLValidationUtils.validateParameter("providerIdentifier", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		onlyAllowSelectingLeafs = xmlParser.getPrimitiveBoolean("onlyAllowSelectingLeafs");

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

	}

	public String getProviderIdentifier() {

		return providerIdentifier;
	}

	public void setProviderIdentifier(String providerIdentifier) {

		this.providerIdentifier = providerIdentifier;
	}

}
