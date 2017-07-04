package com.nordicpeak.flowengine.queries.checkboxquery;

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
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesBaseQuery;

@Table(name = "checkbox_queries")
@XMLElement
public class CheckboxQuery extends FixedAlternativesBaseQuery {
	
	
	private static final long serialVersionUID = -842191226937409416L;
	
	public static final Field ALTERNATIVES_RELATION = ReflectionUtils.getField(CheckboxQuery.class, "alternatives");
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer minChecked;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer maxChecked;
	
	@DAOManaged
	@OneToMany(autoUpdate = true, autoAdd = true)
	@XMLElement(fixCase = true)
	private List<CheckboxAlternative> alternatives;
	
	@WebPopulate(maxLength = 255)
	@DAOManaged
	@XMLElement
	private String freeTextAlternative;
	
	@DAOManaged
	@WebPopulate(maxLength = 10, required = true)
	@XMLElement
	private Columns columns = Columns.ONE;
	
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
	@WebPopulate
	@XMLElement
	private boolean hideTitle;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<CheckboxQueryInstance> instances;
	
	public static long getSerialversionuid() {
		
		return serialVersionUID;
	}
	
	@Override
	public Integer getQueryID() {
		
		return queryID;
	}
	
	@Override
	public List<CheckboxAlternative> getAlternatives() {
		
		return alternatives;
	}
	
	public List<CheckboxQueryInstance> getInstances() {
		
		return instances;
	}
	
	public void setInstances(List<CheckboxQueryInstance> instances) {
		
		this.instances = instances;
	}
	
	public void setQueryID(int queryID) {
		
		this.queryID = queryID;
	}
	
	public void setAlternatives(List<CheckboxAlternative> alternatives) {
		
		this.alternatives = alternatives;
	}
	
	@Override
	public String getFreeTextAlternative() {
		return freeTextAlternative;
	}
	
	public void setFreeTextAlternative(String freeTextAlternative) {
		this.freeTextAlternative = freeTextAlternative;
	}
	
	public Integer getMinChecked() {
		
		return minChecked;
	}
	
	public void setMinChecked(Integer minChecked) {
		
		this.minChecked = minChecked;
	}
	
	public Integer getMaxChecked() {
		
		return maxChecked;
	}
	
	public void setMaxChecked(Integer maxChecked) {
		
		this.maxChecked = maxChecked;
	}
	
	public Columns getColumns() {
		return columns;
	}
	
	public void setColumns(Columns columns) {
		this.columns = columns;
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
		
		return "CheckboxQuery (queryID: " + queryID + ")";
	}
	
	@Override
	public String getXSDTypeName() {
		
		return "CheckboxQuery" + queryID;
	}
	
	@Override
	public void toXSD(Document doc) {
		
		if (this.alternatives != null) {
			
			if (maxChecked != null) {
				
				toXSD(doc, maxChecked);
				
			} else {
				
				toXSD(doc, this.alternatives.size());
			}
			
		} else {
			
			toXSD(doc, 1);
		}
	}
	
	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		freeTextAlternative = XMLValidationUtils.validateParameter("freeTextAlternative", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		hideTitle = xmlParser.getPrimitiveBoolean("hideTitle");
		
		minChecked = XMLValidationUtils.validateParameter("minChecked", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		maxChecked = XMLValidationUtils.validateParameter("maxChecked", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		
		alternatives = CheckboxQueryCRUD.ALTERNATIVES_POPLATOR.populate(xmlParser, errors);
		
		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		if (attributeName != null) {
			
			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
		}
		
		if (alternatives != null) {
			
			CheckboxQueryCRUD.validateMinAndMax(minChecked, maxChecked, alternatives, errors);
		}
		
		if (!StringUtils.isEmpty(xmlParser.getString("columns"))) {
			
			columns = XMLValidationUtils.validateParameter("columns", xmlParser, true, new EnumPopulator<Columns>(Columns.class), errors);
		}
		
		if (!errors.isEmpty()) {
			
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

	
	public void setQueryID(Integer queryID) {
	
		this.queryID = queryID;
	}
}
