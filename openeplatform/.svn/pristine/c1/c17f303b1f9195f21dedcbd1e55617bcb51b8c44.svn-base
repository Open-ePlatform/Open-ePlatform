package com.nordicpeak.flowengine.evaluators.calculatequerystateevaluator;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;

import com.nordicpeak.flowengine.evaluators.basequerystateevaluator.BaseQueryStateEvaluator;

public class CalculatedQueryStateEvaluator extends BaseQueryStateEvaluator {

	private static final long serialVersionUID = -8539482079381691634L;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private Integer fromValue;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private Integer toValue;

	public Integer getFromValue() {

		return fromValue;
	}

	public void setFromValue(Integer fromValue) {

		this.fromValue = fromValue;
	}

	public Integer getToValue() {

		return toValue;
	}

	public void setToValue(Integer toValue) {

		this.toValue = toValue;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		super.populate(xmlParser);

		fromValue = xmlParser.getInteger("fromValue");

		toValue = xmlParser.getInteger("toValue");

	}

}
