package com.nordicpeak.flowengine.queries.dropdownquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesBaseQuery;


@Table(name = "drop_down_queries")
@XMLElement
public class DropDownQuery extends FixedAlternativesBaseQuery {

	private static final long serialVersionUID = -842191226937409416L;

	public static final Field ALTERNATIVES_RELATION = ReflectionUtils.getField(DropDownQuery.class, "alternatives");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@TextTagReplace
	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String shortDescription;

	@DAOManaged
	@OneToMany(autoUpdate=true, autoAdd=true)
	@XMLElement(fixCase=true)
	private List<DropDownAlternative> alternatives;

	@WebPopulate(maxLength = 255)
	@DAOManaged
	@XMLElement
	private String freeTextAlternative;

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
	@OneToMany
	@XMLElement
	private List<DropDownQueryInstance> instances;

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	@Override
	public List<DropDownAlternative> getAlternatives() {

		return alternatives;
	}

	public void setAlternatives(List<DropDownAlternative> alternatives) {

		this.alternatives = alternatives;
	}

	@Override
	public String getFreeTextAlternative() {
		return freeTextAlternative;
	}

	public void setFreeTextAlternative(String freeTextAlternative) {
		this.freeTextAlternative = freeTextAlternative;
	}

	public List<DropDownQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<DropDownQueryInstance> instances) {

		this.instances = instances;
	}

	public void setQueryID(int queryID) {

		this.queryID = queryID;
	}

	public String getShortDescription() {

		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {

		this.shortDescription = shortDescription;
	}

	@Override
	public String toString() {

		if(this.queryDescriptor != null){

			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}

		return "DropDownQuery (queryID: " + queryID + ")";
	}

	@Override
	public String getXSDTypeName() {

		return "DropDownQuery" + queryID;
	}

	@Override
	public void toXSD(Document doc) {

		toXSD(doc, 1);
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		shortDescription = XMLValidationUtils.validateParameter("shortDescription", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		freeTextAlternative = XMLValidationUtils.validateParameter("freeTextAlternative", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		if (attributeName != null) {
			
			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
		}
		
		alternatives = DropDownQueryCRUD.ALTERNATIVES_POPLATOR.populate(xmlParser, errors);
		
		if(!errors.isEmpty()){

			throw new ValidationException(errors);
		}
		
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
}
