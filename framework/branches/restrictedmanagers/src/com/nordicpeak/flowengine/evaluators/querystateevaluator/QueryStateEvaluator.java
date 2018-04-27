package com.nordicpeak.flowengine.evaluators.querystateevaluator;

import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.evaluators.basequerystateevaluator.BaseQueryStateEvaluator;

@Table(name = "query_state_evaluators")
@XMLElement
public class QueryStateEvaluator extends BaseQueryStateEvaluator {

	private static final long serialVersionUID = -336493853396860255L;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private SelectionMode selectionMode;

	@DAOManaged
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "query_state_evaluator_alternatives", remoteValueColumnName = "alternativeID")
	@WebPopulate(paramName = "alternativeID")
	@XMLElement(fixCase = true, childName = "alternativeID")
	private List<Integer> requiredAlternativeIDs;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean freeTextAlternative;

	public List<Integer> getRequiredAlternativeIDs() {

		return requiredAlternativeIDs;
	}

	public void setRequiredAlternativeIDs(List<Integer> requiredAlternativeIDs) {

		this.requiredAlternativeIDs = requiredAlternativeIDs;
	}

	public SelectionMode getSelectionMode() {

		return selectionMode;
	}

	public void setSelectionMode(SelectionMode selectionMode) {

		this.selectionMode = selectionMode;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		super.populate(xmlParser);

		List<ValidationError> errors = new ArrayList<ValidationError>();

		selectionMode = XMLValidationUtils.validateParameter("selectionMode", xmlParser, true, QueryStateEvaluatorCRUD.SELECTIONMODE_POPULATOR, errors);

		requiredAlternativeIDs = xmlParser.getIntegers("RequiredAlternativeIDs/alternativeID");
		freeTextAlternative = xmlParser.getPrimitiveBoolean("freeTextAlternative");
	}

	public boolean isFreeTextAlternative() {

		return freeTextAlternative;
	}

	public void setFreeTextAlternative(boolean freeTextAlternative) {

		this.freeTextAlternative = freeTextAlternative;
	}

}
