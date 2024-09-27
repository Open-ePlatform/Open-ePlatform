package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
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

import com.nordicpeak.flowengine.interfaces.ImmutableDefaultStatusMapping;

@Table(name="flowengine_default_flow_statuses")
@XMLElement
public class DefaultStatusMapping extends GeneratedElementable implements Serializable, ImmutableDefaultStatusMapping, XMLParserPopulateable{

	public static final Field FLOW_STATE_RELATION = ReflectionUtils.getField(DefaultStatusMapping.class,"status");

	private static final long serialVersionUID = -959321317538132425L;

	@DAOManaged
	@Key
	@XMLElement
	private String actionID;

	@DAOManaged(columnName="flowID")
	@Key
	@ManyToOne
	private Flow flow;

	@DAOManaged(columnName="statusID")
	@Key
	@ManyToOne
	@XMLElement
	private Status status;

	public DefaultStatusMapping() {}

	public DefaultStatusMapping(String actionID, Flow flow, Status status) {

		super();
		this.actionID = actionID;
		this.flow = flow;
		this.status = status;
	}

	@Override
	public String getActionID() {

		return actionID;
	}

	public void setActionID(String actionID) {

		this.actionID = actionID;
	}


	@Override
	public Flow getFlow() {

		return flow;
	}


	public void setFlow(Flow flow) {

		this.flow = flow;
	}


	@Override
	public Status getStatus() {

		return status;
	}


	public void setStatus(Status flowState) {

		this.status = flowState;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		this.actionID = XMLValidationUtils.validateParameter("actionID", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		this.status = XMLPopulationUtils.populateBean(xmlParser, "Status", Status.class, errors);

		if(!errors.isEmpty()){

			throw new ValidationException(errors);
		}
	}
}
