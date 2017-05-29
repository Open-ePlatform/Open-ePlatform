package com.nordicpeak.flowengine.persondatasavinginformer.query;

import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.webutils.http.BeanRequestPopulator;

import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

public class PersonDataInformerQueryCRUD extends BaseQueryCRUD<PersonDataInformerQuery, PersonDataInformerQueryProviderModule> {
	
	public PersonDataInformerQueryCRUD(AnnotatedDAOWrapper<PersonDataInformerQuery, Integer> queryDAO, BeanRequestPopulator<PersonDataInformerQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, PersonDataInformerQueryProviderModule callback) {
		
		super(PersonDataInformerQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
	}
	
}
