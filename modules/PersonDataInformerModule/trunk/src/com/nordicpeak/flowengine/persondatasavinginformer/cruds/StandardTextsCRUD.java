package com.nordicpeak.flowengine.persondatasavinginformer.cruds;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.nordicpeak.flowengine.persondatasavinginformer.PersonDataInformerAdminModule;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerStandardText;
import com.nordicpeak.flowengine.persondatasavinginformer.enums.StandardTextType;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class StandardTextsCRUD extends ModularCRUD<InformerStandardText, Integer, User, PersonDataInformerAdminModule> {

	public StandardTextsCRUD(CRUDDAO<InformerStandardText, Integer> crudDAO, PersonDataInformerAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<InformerStandardText>(InformerStandardText.class), "InformerStandardText", "informerStandardText", "/", callback);
	}

	@Override
	protected void validateUpdatePopulation(InformerStandardText bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		if (bean.getType() == StandardTextType.TEXTFIELD && bean.getValue().length() > 1024) {
			
			throw new ValidationException(new ValidationError("value", ValidationErrorType.TooLong));
		}
	}
}