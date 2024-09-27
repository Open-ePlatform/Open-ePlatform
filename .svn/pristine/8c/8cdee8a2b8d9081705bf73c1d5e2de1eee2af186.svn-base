package com.nordicpeak.flowengine.queries.basemapquery;

import java.util.ArrayList;
import java.util.List;

import se.unlogic.hierarchy.core.annotations.FCKContent;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;
import se.unlogic.webutils.annotations.URLRewrite;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

public abstract class BaseMapQuery extends BaseQuery {

	private static final long serialVersionUID = 2230363617580102516L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@FCKContent
	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String description;

	@FCKContent
	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String helpText;
	
	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 1000)
	@XMLElement
	private String startInstruction;
	
	@Override
	public Integer getQueryID() {
		return queryID;
	}
	
	public void setQueryID(Integer queryID) {
		this.queryID = queryID;
	}
	
	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartInstruction() {
		return startInstruction;
	}

	public void setStartInstruction(String startInstruction) {
		this.startInstruction = startInstruction;
	}
	
	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		startInstruction = XMLValidationUtils.validateParameter("startInstruction", xmlParser, false, 1, 1000, StringPopulator.getPopulator(), errors);
		
		if(!errors.isEmpty()){

			throw new ValidationException(errors);
		}
		
	}

	
}
