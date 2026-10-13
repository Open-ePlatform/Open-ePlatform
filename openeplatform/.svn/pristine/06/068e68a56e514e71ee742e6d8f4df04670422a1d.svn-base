package com.nordicpeak.flowengine.queries.basequery;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.FCKContent;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.hierarchy.core.utils.HTMLListAttributeTagUtils;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.webutils.annotations.URLRewrite;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.queries.DescribedQuery;

public abstract class BaseQuery extends GeneratedElementable implements Query, Serializable, DescribedQuery, XMLParserPopulateable {

	private static final long serialVersionUID = 7384942784185489658L;

	public static final Field DESCRIPTION_FIELD = ReflectionUtils.getField(BaseQuery.class, "description");

	public static final Field HELP_TEXT_FIELD = ReflectionUtils.getField(BaseQuery.class, "helpText");

	@FCKContent
	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement(cdata = true)
	protected String description;

	@FCKContent
	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement(cdata = true)
	protected String helpText;

	private Set<String> descriptionAttributeTags;

	private Set<String> helpTextAttributeTags;

	private Set<String> descriptionAttributeListTags;

	private Set<String> helpTextAttributeListTags;

	@XMLElement
	private String configURL;

	@XMLElement
	protected MutableQueryDescriptor queryDescriptor;

	public void init(MutableQueryDescriptor mutableQueryDescriptor, String configURL) {

		this.queryDescriptor = mutableQueryDescriptor;
		this.configURL = configURL;
	}

	public abstract Integer getQueryID();

	@Override
	public String getConfigAlias() {

		return configURL;
	}

	@Override
	public MutableQueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}

	@Override
	public String getDescription() {

		return description;
	}

	public String getHelpText() {
		
		return helpText;
	}	
	
	public String getDescription(AttributeHandler attributeHandler) {

		if(description != null && (hasDescriptionAttributeTags() || hasDescriptionAttributeListTags())){
			
			String tagReplacedDescription = description;
			
			if(hasDescriptionAttributeTags()) {

				tagReplacedDescription = AttributeTagUtils.replaceTags(tagReplacedDescription, attributeHandler, descriptionAttributeTags, true, false);
			}
			
			if(hasDescriptionAttributeListTags()) {
				
				tagReplacedDescription = HTMLListAttributeTagUtils.replaceTags(tagReplacedDescription, attributeHandler, descriptionAttributeListTags);
			}
			
			return tagReplacedDescription;
			
		}else{
			
			return description;
		}
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public String getHelpText(AttributeHandler attributeHandler) {

		if (helpText != null && (hasHelpTextAttributeTags() || hasHelpTextAttributeListTags())) {

			String tagReplacedHelpText = helpText;
			
			if(hasHelpTextAttributeTags()) {
				
				tagReplacedHelpText = AttributeTagUtils.replaceTags(helpText, attributeHandler, helpTextAttributeTags, true, false);
			}
			
			if(hasHelpTextAttributeListTags()) {
				
				tagReplacedHelpText = HTMLListAttributeTagUtils.replaceTags(tagReplacedHelpText, attributeHandler, helpTextAttributeListTags);
			}
			
			return tagReplacedHelpText;

		} else {

			return helpText;
		}
	}

	public void setHelpText(String helpText) {

		this.helpText = helpText;
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
		valueElement.setAttribute("type", "xs:string");
		valueElement.setAttribute("minOccurs", "0");
		valueElement.setAttribute("maxOccurs", "1");
		sequenceElement.appendChild(valueElement);

		doc.getDocumentElement().appendChild(complexTypeElement);
	}

	public void scanAttributeTags() {

		if (description != null) {

			this.descriptionAttributeTags = AttributeTagUtils.getAttributeTags(description);

			this.descriptionAttributeListTags = HTMLListAttributeTagUtils.getAttributeTags(description);
		}

		if (helpText != null) {

			this.helpTextAttributeTags = AttributeTagUtils.getAttributeTags(helpText);

			this.helpTextAttributeListTags = HTMLListAttributeTagUtils.getAttributeTags(helpText);
		}
	}

	public boolean hasDescriptionAttributeTags() {

		return descriptionAttributeTags != null;
	}

	public boolean hasHelpTextAttributeTags() {

		return helpTextAttributeTags != null;
	}

	public boolean hasDescriptionAttributeListTags() {

		return descriptionAttributeListTags != null;
	}

	public boolean hasHelpTextAttributeListTags() {

		return helpTextAttributeListTags != null;
	}
	
	public boolean hasTags() {
		
		return hasDescriptionAttributeTags() || hasHelpTextAttributeTags() || hasDescriptionAttributeListTags() || hasHelpTextAttributeListTags();
	}
}
