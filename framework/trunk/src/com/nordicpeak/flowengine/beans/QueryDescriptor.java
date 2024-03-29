package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.NonNegativeStringIntegerPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLPopulationUtils;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.populators.XMLElementNamePopulator;

@Table(name = "flowengine_query_descriptors")
@XMLElement
public class QueryDescriptor extends GeneratedElementable implements MutableQueryDescriptor, XMLParserPopulateable {

	private static final long serialVersionUID = 4177464037556545092L;

	public static final Field STEP_RELATION = ReflectionUtils.getField(QueryDescriptor.class,"step");
	public static final Field QUERY_INSTANCE_DESCRIPTORS_RELATION = ReflectionUtils.getField(QueryDescriptor.class,"queryInstanceDescriptors");
	public static final Field EVALUATOR_DESCRIPTORS_RELATION = ReflectionUtils.getField(QueryDescriptor.class,"evaluatorDescriptors");

	@DAOManaged(autoGenerated = true)
	@Key
	@XMLElement
	private Integer queryID;

	@DAOManaged
	@WebPopulate(required = true, maxLength = 255)
	@XMLElement
	private String name;

	@DAOManaged
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String comment;
	
	@DAOManaged
	@OrderBy
	@XMLElement
	private Integer sortIndex;

	@DAOManaged
	@WebPopulate(required=true)
	@XMLElement
	private QueryState defaultQueryState;

	@DAOManaged(columnName="stepID")
	@ManyToOne
	private Step step;

	@DAOManaged
	@XMLElement
	private String queryTypeID;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean exported;

	@DAOManaged
	@WebPopulate(maxLength = 255, populator=XMLElementNamePopulator.class)
	@RequiredIfSet(paramNames = "exported")
	@XMLElement
	private String xsdElementName;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<QueryInstanceDescriptor> queryInstanceDescriptors;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase=true)
	private List<EvaluatorDescriptor> evaluatorDescriptors;

	@DAOManaged
	@WebPopulate
	@XMLElement
	boolean mergeWithPreviousQuery;
	
	private XMLParser importParser;

	@Override
	public XMLParser getImportParser() {

		return importParser;
	}

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	public void setQueryID(Integer queryID) {

		this.queryID = queryID;
	}

	@Override
	public String getName() {

		return name;
	}

	@Override
	public void setName(String name) {

		this.name = name;
	}

	@Override
	public String getComment() {

		return comment;
	}

	@Override
	public void setComment(String comment) {

		this.comment = comment;
	}
	
	@Override
	public Integer getSortIndex() {

		return sortIndex;
	}

	public void setSortIndex(Integer index) {

		this.sortIndex = index;
	}

	@Override
	public QueryState getDefaultQueryState() {

		return defaultQueryState;
	}

	@Override
	public void setDefaultQueryState(QueryState defaultQueryState) {

		this.defaultQueryState = defaultQueryState;
	}

	@Override
	public Step getStep() {

		return step;
	}

	public void setStep(Step step) {

		this.step = step;
	}

	public List<QueryInstanceDescriptor> getQueryInstanceDescriptors() {

		return queryInstanceDescriptors;
	}

	public void setQueryInstanceDescriptors(List<QueryInstanceDescriptor> queryInstanceDescriptors) {

		this.queryInstanceDescriptors = queryInstanceDescriptors;
	}

	@Override
	public String getQueryTypeID() {

		return queryTypeID;
	}

	public void setQueryTypeID(String queryTypeID) {

		this.queryTypeID = queryTypeID;
	}

	@Override
	public String toString() {

		return name + " (ID: " + queryID + ")";
	}

	@Override
	public List<EvaluatorDescriptor> getEvaluatorDescriptors() {

		return evaluatorDescriptors;
	}

	public void setEvaluatorDescriptors(List<EvaluatorDescriptor> evaluatorDescriptors) {

		this.evaluatorDescriptors = evaluatorDescriptors;
	}

	@Override
	public boolean isExported() {

		return exported;
	}

	@Override
	public void setExported(boolean exported) {

		this.exported = exported;
	}

	@Override
	public String getXSDElementName() {

		return xsdElementName;
	}

	public void setXSDElementName(String name) {

		this.xsdElementName = name;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		this.queryID = XMLValidationUtils.validateParameter("queryID", xmlParser, true, PositiveStringIntegerPopulator.getPopulator(), errors);
		this.name = XMLValidationUtils.validateParameter("name", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		this.sortIndex = XMLValidationUtils.validateParameter("sortIndex", xmlParser, true, NonNegativeStringIntegerPopulator.getPopulator(), errors);
		this.queryTypeID = XMLValidationUtils.validateParameter("queryTypeID", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		this.defaultQueryState = XMLValidationUtils.validateParameter("defaultQueryState", xmlParser, true, new EnumPopulator<QueryState>(QueryState.class), errors);
		this.comment = XMLValidationUtils.validateParameter("comment", xmlParser, false, 1, 1024, StringPopulator.getPopulator(), errors);
		
		this.exported = xmlParser.getPrimitiveBoolean("exported");
		this.xsdElementName = XMLValidationUtils.validateParameter("xsdElementName", xmlParser, false, 1, 255, new XMLElementNamePopulator(), errors);
		this.mergeWithPreviousQuery = xmlParser.getPrimitiveBoolean("mergeWithPreviousQuery");
		
		//Backwards compatibility for flows exported with FlowEngine version before 1.2
		if(xsdElementName == null){

			this.exported = false;
		}

		this.evaluatorDescriptors = XMLPopulationUtils.populateBeans(xmlParser, "EvaluatorDescriptors/EvaluatorDescriptor", EvaluatorDescriptor.class, errors);

		if(!errors.isEmpty()){

			throw new ValidationException(errors);
		}

		this.importParser = xmlParser;
	}

}
