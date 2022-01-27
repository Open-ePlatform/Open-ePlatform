package com.nordicpeak.flowengine.persondatasavinginformer.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;

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

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.FlowFamilyInformerSetting;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesBaseQuery;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQuery;

@Table(name = "person_data_informer_queries")
@XMLElement
public class PersonDataInformerQuery extends FixedAlternativesBaseQuery implements FixedAlternativesQuery {
	
	private static final Map<Integer, Integer> ALTERNATIVE_CONVERSION_MAP = Collections.singletonMap(1, 1);
	
	private static final long serialVersionUID = -842191226937409416L;
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<PersonDataInformerQueryInstance> instances;
	
	@XMLElement(fixCase = true)
	private FlowFamilyInformerSetting familyInformerSettings;
	
	private PersonDataInformerQueryAlternative alternative;
	
	@Override
	public Integer getQueryID() {
		
		return queryID;
	}
	
	public List<PersonDataInformerQueryInstance> getInstances() {
		
		return instances;
	}
	
	public void setInstances(List<PersonDataInformerQueryInstance> instances) {
		
		this.instances = instances;
	}
	
	public void setQueryID(Integer queryID) {
		
		this.queryID = queryID;
	}
	
	@Override
	public String toString() {
		
		if (this.queryDescriptor != null) {
			
			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}
		
		return "PersonDataInformerQuery (queryID: " + queryID + ")";
	}
	
	@Override
	public String getXSDTypeName() {
		
		return "PersonDataInformerQuery" + queryID;
	}
	
	@Override
	public void toXSD(Document doc) {
		
		toXSD(doc, 1);
	}
	
	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {
		
		return Collections.singletonList(alternative);
	}
	
	@Override
	public String getFreeTextAlternative() {
		
		return null;
	}
	
	@Override
	public Map<Integer, Integer> getAlternativeConversionMap() {
		
		return ALTERNATIVE_CONVERSION_MAP;
	}
	
	public PersonDataInformerQueryAlternative getAlternative() {
		
		return alternative;
	}
	
	public void setAlternative(PersonDataInformerQueryAlternative alternative) {
		
		this.alternative = alternative;
	}
	
	public FlowFamilyInformerSetting getFamilyInformerSettings() {
		return familyInformerSettings;
	}
	
	public void setFamilyInformerSettings(FlowFamilyInformerSetting familyInformerSettings) {
		this.familyInformerSettings = familyInformerSettings;
	}
	
	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		
		if (!errors.isEmpty()) {
			
			throw new ValidationException(errors);
		}
		
	}
	
	@Override
	public boolean isExportAlternativeID() {

		return false;
	}

}
