package com.nordicpeak.flowengine.queries.pudquery;

import java.lang.reflect.Field;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;

@Table(name = "pud_query_instances")
@XMLElement
public class PUDQueryInstance extends BaseQueryInstance implements FixedAlternativesQueryInstance, StringValueQueryInstance {

	private static final long serialVersionUID = -405719839906745735L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(PUDQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private PUDQuery query;

	@DAOManaged
	@XMLElement
	private String propertyUnitDesignation;
	
	@DAOManaged
	@XMLElement
	private String address;

	@DAOManaged
	@XMLElement
	private Integer propertyUnitNumber;
	
	@DAOManaged
	@XMLElement
	private String propertyObjectIdentity;

	public Integer getQueryInstanceID() {
		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {
		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public PUDQuery getQuery() {
		return query;
	}

	public void setQuery(PUDQuery query) {
		this.query = query;
	}

	public String getPropertyUnitDesignation() {
		return propertyUnitDesignation;
	}

	public void setPropertyUnitDesignation(String propertyUnitDesignation) {
		this.propertyUnitDesignation = propertyUnitDesignation;
	}

	public Integer getPropertyUnitNumber() {
		return propertyUnitNumber;
	}
	
	public void setPropertyUnitNumber(Integer propertyUnitNumber) {
		this.propertyUnitNumber = propertyUnitNumber;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		propertyUnitDesignation = null;
		propertyUnitNumber = null;
		propertyObjectIdentity = null;
		address = null;

		if (query.isSetAsAttribute()) {
			
			resetAttribute(attributeHandler);
		}
		
		super.reset(attributeHandler);
	}

	public void resetAttribute(MutableAttributeHandler attributeHandler){
		
		attributeHandler.removeAttribute(query.getAttributeName());
		attributeHandler.removeAttribute(query.getAttributeName() + ".address");
		attributeHandler.removeAttribute(query.getAttributeName() + ".propertyUnitDesignation");
		attributeHandler.removeAttribute(query.getAttributeName() + ".propertyUnitDesignationNoMunicipality");
		attributeHandler.removeAttribute(query.getAttributeName() + ".propertyUnitNumber");
		attributeHandler.removeAttribute(query.getAttributeName() + ".propertyObjectIdentity");
	}
	
	public void setAttribute(MutableAttributeHandler attributeHandler) {

		if (query.isUseAddressAsResult()) {
		
			attributeHandler.setAttribute(query.getAttributeName(), address);
			
		} else {
			
			attributeHandler.setAttribute(query.getAttributeName(), propertyUnitDesignation);
		}
		
		attributeHandler.setAttribute(query.getAttributeName() + ".address", address);
		attributeHandler.setAttribute(query.getAttributeName() + ".propertyUnitNumber", propertyUnitNumber);
		attributeHandler.setAttribute(query.getAttributeName() + ".propertyObjectIdentity", propertyObjectIdentity);
		
		if (!StringUtils.isEmpty(propertyUnitDesignation)) {
			
			attributeHandler.setAttribute(query.getAttributeName() + ".propertyUnitDesignation", propertyUnitDesignation);
			
			String unitDesignation = propertyUnitDesignation;
			
			int firstSpace = unitDesignation.indexOf(" ");
			
			if (firstSpace != -1) {
				unitDesignation = unitDesignation.substring(firstSpace + 1);
			}
			
			attributeHandler.setAttribute(query.getAttributeName() + ".propertyUnitDesignationNoMunicipality", unitDesignation);
		}
	}
	
	@Override
	public String toString() {

		return "PUDQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}

	public void copyQueryValues() {}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		if (query.isUseAddressAsResult()) {
			
			XMLUtils.appendNewCDATAElement(doc, element, "Address", address);
			
		} else {
			
			XMLUtils.appendNewCDATAElement(doc, element, "PropertyUnitDesignation", propertyUnitDesignation);
			XMLUtils.appendNewElement(doc, element, "PropertyUnitNumber", propertyUnitNumber);
		}
		
		XMLUtils.appendNewElement(doc, element, "PropertyObjectIdentity", propertyObjectIdentity);

		return element;
	}

	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {
		
		List<PUDAlternative> alternatives = null;
		
		if (queryInstanceDescriptor.isPopulated()) {
			
			alternatives = CollectionUtils.addAndInstantiateIfNeeded(alternatives, query.getIsPopulatedAlternative());
			
		} else {
			
			alternatives = CollectionUtils.addAndInstantiateIfNeeded(alternatives, query.getIsNotPopulatedAlternative());
		}
		
		return alternatives;
	}
	
	@Override
	public String getFreeTextAlternativeValue() {
		return null;
	}

	public void setPropertyObjectIdentity(String propertyObjectIdentity) {
		this.propertyObjectIdentity = propertyObjectIdentity;
	}

	@Override
	public String getStringValue() {

		if(propertyUnitDesignation != null) {
			
			return propertyUnitDesignation;
		}
		
		return address;
	}

}
