package com.nordicpeak.flowengine.queries.checkboxpaymentquery;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.datatypes.Matrix;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesBaseQuery;
import com.nordicpeak.flowengine.queries.tablequery.SummaryTableQuery;
import com.nordicpeak.flowengine.queries.tablequery.SummaryTableQueryUtils;

@Table(name = "checkbox_payment_queries")
@XMLElement
public class CheckboxPaymentQuery extends FixedAlternativesBaseQuery implements SummaryTableQuery {

	private static final long serialVersionUID = -842191226937409416L;

	public static final Field ALTERNATIVES_RELATION = ReflectionUtils.getField(CheckboxPaymentQuery.class, "alternatives");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@DAOManaged
	@WebPopulate(populator=PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer minChecked;

	@DAOManaged
	@WebPopulate(populator=PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer maxChecked;

	@DAOManaged
	@OneToMany(autoUpdate=true, autoAdd=true)
	@XMLElement(fixCase=true)
	private List<CheckboxPaymentAlternative> alternatives;

	@WebPopulate(maxLength = 255)
	@DAOManaged
	@XMLElement
	private String freeTextAlternative;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<CheckboxPaymentQueryInstance> instances;

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	@Override
	public List<CheckboxPaymentAlternative> getAlternatives() {

		return alternatives;
	}

	public List<CheckboxPaymentQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<CheckboxPaymentQueryInstance> instances) {

		this.instances = instances;
	}

	public void setQueryID(int queryID) {

		this.queryID = queryID;
	}

	public void setAlternatives(List<CheckboxPaymentAlternative> alternatives) {

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

	@Override
	public String toString() {

		if(this.queryDescriptor != null){

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

		if(this.alternatives != null){

			if(maxChecked != null){

				toXSD(doc, maxChecked);

			}else{

				toXSD(doc, this.alternatives.size());
			}

		}else{

			toXSD(doc, 1);
		}
	}

	@Override
	public Matrix<String> getDataTable(List<Integer> queryInstanceIDs, QueryHandler queryHandler) throws SQLException {

		return SummaryTableQueryUtils.getGenericTableQueryCallback(this.getClass(), queryHandler, getQueryDescriptor().getQueryTypeID()).getSummaryTable(this, queryInstanceIDs);
	}
	
	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		freeTextAlternative = XMLValidationUtils.validateParameter("freeTextAlternative", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		minChecked = XMLValidationUtils.validateParameter("minChecked", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		maxChecked = XMLValidationUtils.validateParameter("maxChecked", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		
		alternatives = CheckboxPaymentQueryCRUD.ALTERNATIVES_POPLATOR.populate(xmlParser, errors);

		if(alternatives != null) {
			
			CheckboxPaymentQueryCRUD.validateMinAndMax(minChecked, maxChecked, alternatives, errors);
			
		}
		
		if(!errors.isEmpty()){

			throw new ValidationException(errors);
		}
		
	}
	
}
