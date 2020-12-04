package com.nordicpeak.flowengine.persondatasavinginformer.cruds;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.persondatasavinginformer.PersonDataInformerAdminModule;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.InformerReasonAlternative;

public class ReasonAlternativeCRUD extends ModularCRUD<InformerReasonAlternative, Integer, User, PersonDataInformerAdminModule> {

	public ReasonAlternativeCRUD(CRUDDAO<InformerReasonAlternative, Integer> crudDAO, PersonDataInformerAdminModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<InformerReasonAlternative>(InformerReasonAlternative.class), "InformerReasonAlternative", "informerReasonAlternative", "/", callback);
	
		setRequirePostForDelete(true);
	}
}