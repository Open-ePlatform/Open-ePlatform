package com.nordicpeak.flowengine.persondatasavinginformer.cruds;

import com.nordicpeak.flowengine.persondatasavinginformer.PersonDataInformerAdminModule;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerStandardText;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class StandardTextsCRUD extends ModularCRUD<InformerStandardText, Integer, User, PersonDataInformerAdminModule> {

	public StandardTextsCRUD(CRUDDAO<InformerStandardText, Integer> crudDAO, PersonDataInformerAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<InformerStandardText>(InformerStandardText.class), "InformerStandardText", "informerStandardText", "/", callback);
	}
}