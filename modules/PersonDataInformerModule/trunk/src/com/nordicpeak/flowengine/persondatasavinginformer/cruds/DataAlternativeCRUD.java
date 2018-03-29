package com.nordicpeak.flowengine.persondatasavinginformer.cruds;

import com.nordicpeak.flowengine.persondatasavinginformer.PersonDataInformerAdminModule;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerDataAlternative;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class DataAlternativeCRUD extends ModularCRUD<InformerDataAlternative, Integer, User, PersonDataInformerAdminModule> {

	public DataAlternativeCRUD(CRUDDAO<InformerDataAlternative, Integer> crudDAO, PersonDataInformerAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<InformerDataAlternative>(InformerDataAlternative.class), "InformerDataAlternative", "informerDataAlternative", "/", callback);
	}
}