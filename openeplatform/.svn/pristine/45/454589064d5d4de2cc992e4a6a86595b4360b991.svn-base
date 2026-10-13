package com.nordicpeak.flowengine.queries.pudmapquery;

import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQueryCRUD;

public class PUDMapQueryCRUD extends BaseMapQueryCRUD<PUDMapQuery, PUDMapQueryInstance, PUDMapQueryProvider> {

	public PUDMapQueryCRUD(AnnotatedDAOWrapper<PUDMapQuery, Integer> queryDAO, PUDMapQueryProvider callback) {
		
		super(PUDMapQuery.class, queryDAO, new AnnotatedRequestPopulator<PUDMapQuery>(PUDMapQuery.class), "PUDMapQuery", "pud map query", null, callback);
		
	}

}
