package com.nordicpeak.flowengine.evaluators.basequerystateevaluator;

import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.evaluators.baseevaluator.BaseEvaluator;

public class BaseQueryStateEvaluator extends BaseEvaluator {

	private static final long serialVersionUID = 48253580098780909L;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private QueryState queryState;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean doNotResetQueryState;

	public QueryState getQueryState() {

		return queryState;
	}

	public void setQueryState(QueryState queryState) {

		this.queryState = queryState;
	}

	public boolean isDoNotResetQueryState() {

		return doNotResetQueryState;
	}

	public void setDoNotResetQueryState(boolean doNotResetQueryState) {

		this.doNotResetQueryState = doNotResetQueryState;
	}

	@Override
	public String toString() {

		return evaluatorDescriptor.toString();
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		queryState = XMLValidationUtils.validateParameter("queryState", xmlParser, true, new EnumPopulator<QueryState>(QueryState.class), errors);

		doNotResetQueryState = xmlParser.getPrimitiveBoolean("doNotResetQueryState");
		
		if(!errors.isEmpty()) {
			
			throw new ValidationException(errors);
		}
		
	}

}
