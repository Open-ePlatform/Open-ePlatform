package com.nordicpeak.flowengine.queries.pudmapquery;

import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQuery;

@Table(name = "pud_map_queries")
@XMLElement
public class PUDMapQuery extends BaseMapQuery {

	private static final long serialVersionUID = 4441486010619994034L;

	@DAOManaged
	@WebPopulate(maxLength = 255, populator=IntegerPopulator.class)
	@XMLElement
	private Integer minimumScale;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<PUDMapQueryInstance> instances;
	
	public List<PUDMapQueryInstance> getInstances() {
		return instances;
	}

	public void setInstances(List<PUDMapQueryInstance> instances) {
		this.instances = instances;
	}
		
	public Integer getMinimumScale() {
		return minimumScale;
	}

	public void setMinimumScale(Integer minimumScale) {
		this.minimumScale = minimumScale;
	}
	
	@Override
	public String getXSDTypeName() {
		
		return "PUDMapQuery" + getQueryID();
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		super.populate(xmlParser);
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		minimumScale = XMLValidationUtils.validateParameter("minimumScale", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		
		if(!errors.isEmpty()){

			throw new ValidationException(errors);
		}
		
	}
	
}
