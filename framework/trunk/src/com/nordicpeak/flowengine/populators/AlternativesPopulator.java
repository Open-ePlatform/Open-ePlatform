package com.nordicpeak.flowengine.populators;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.NonNegativeStringIntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.interfaces.MutableAlternative;
import com.nordicpeak.flowengine.interfaces.PricedAlternative;

public class AlternativesPopulator<AlternativeType extends MutableAlternative> {
	
	private Class<AlternativeType> alternativeClass;
	
	private final String xmlParentNodeName;
	
	public AlternativesPopulator(Class<AlternativeType> alternativeClass) {
		
		this.alternativeClass = alternativeClass;
		xmlParentNodeName = "Alternatives";
	}
	
	public AlternativesPopulator(Class<AlternativeType> alternativeClass, String xmlParentNodeName) {
		
		this.alternativeClass = alternativeClass;
		this.xmlParentNodeName = xmlParentNodeName;
	}
	
	public List<AlternativeType> populate(List<AlternativeType> currentAlternatives, HttpServletRequest req, List<ValidationError> validationErrors) {
		return populate(currentAlternatives, req, 255, 255, validationErrors);
	}
	
	public List<AlternativeType> populate(List<AlternativeType> currentAlternatives, HttpServletRequest req, int maxLength, int extraMaxLength, List<ValidationError> validationErrors) {
		
		String[] alternativeIDs = req.getParameterValues("alternativeID");
		
		List<AlternativeType> alternatives = new ArrayList<AlternativeType>();
		
		if (alternativeIDs != null) {
			
			for (String alternativeID : alternativeIDs) {
				
				String name = ValidationUtils.validateParameter("alternative_" + alternativeID, req, true, 1, maxLength, validationErrors);
				
				if (name == null) {
					continue;
				}
				
				String sortOrder = req.getParameter("sortorder_" + alternativeID);
				String xmlValue = ValidationUtils.validateParameter("alternative-xml-value_" + alternativeID, req, false, 0, extraMaxLength, validationErrors);
				String attributeValue = ValidationUtils.validateParameter("alternative-attribute-value_" + alternativeID, req, false, 0, extraMaxLength, validationErrors);
				
				if (NumberUtils.isInt(sortOrder)) {
					
					AlternativeType alternative = getNewAlternativeInstance();
					
					alternative.setName(name);
					alternative.setSortIndex(NumberUtils.toInt(sortOrder));
					
					if (alternative instanceof PricedAlternative) {
						
						Integer price = ValidationUtils.validateParameter("alternativeprice_" + alternativeID, req, false, NonNegativeStringIntegerPopulator.getPopulator(), validationErrors);
						((PricedAlternative) alternative).setPrice(price);
					}
					
					extraPopulation(alternative, req, alternativeID, validationErrors);
					
					if (NumberUtils.isInt(alternativeID)) {
						
						checkForExistingAlternatives(currentAlternatives, alternative, NumberUtils.toInt(alternativeID));
					}
					
					if (!StringUtils.isEmpty(xmlValue)) {
						
						alternative.setExportXMLValue(xmlValue);
					}
					
					if (!StringUtils.isEmpty(attributeValue)) {
						
						alternative.setAttributeValue(attributeValue);
					}
					
					alternatives.add(alternative);
				}
			}
		}
		
		return alternatives;
	}
	
	protected void extraPopulation(AlternativeType alternative, HttpServletRequest req, String alternativeID, List<ValidationError> validationErrors) {}
	
	public List<AlternativeType> populate(XMLParser xmlParser, List<ValidationError> errors) throws ValidationException {
		
		List<XMLParser> xmlParsers = xmlParser.getNodes(xmlParentNodeName + "/" + alternativeClass.getSimpleName());
		
		if (!CollectionUtils.isEmpty(xmlParsers)) {
			
			List<AlternativeType> alternatives = new ArrayList<AlternativeType>();
			
			for (XMLParser parser : xmlParsers) {
				
				AlternativeType alternative = getNewAlternativeInstance();
				alternative.populate(parser);
				alternatives.add(alternative);
			}
			
			return alternatives;
		}
		
		return null;
	}
	
	protected void checkForExistingAlternatives(List<AlternativeType> currentAlternatives, AlternativeType alternative, Integer alternativeID) {
		
		if (!CollectionUtils.isEmpty(currentAlternatives)) {
			
			for (MutableAlternative queryAlternative : currentAlternatives) {
				
				if (queryAlternative.getAlternativeID().equals(alternativeID)) {
					
					alternative.setAlternativeID(alternativeID);
					break;
				}
			}
		}
	}
	
	protected AlternativeType getNewAlternativeInstance() {
		
		try {
			return alternativeClass.newInstance();
			
		} catch (InstantiationException e) {
			
			throw new RuntimeException(e);
			
		} catch (IllegalAccessException e) {
			
			throw new RuntimeException(e);
		}
	}
	
}
