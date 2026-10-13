package com.nordicpeak.flowengine.queries.singlepolygonmapquery;

import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.queries.basemapquery.BaseMapQueryCRUD;

public class SinglePolygonMapQueryCRUD extends BaseMapQueryCRUD<SinglePolygonMapQuery, SinglePolygonMapQueryInstance, SinglePolygonMapQueryProvider> {

	public SinglePolygonMapQueryCRUD(AnnotatedDAOWrapper<SinglePolygonMapQuery, Integer> queryDAO, SinglePolygonMapQueryProvider callback) {
		
		super(SinglePolygonMapQuery.class, queryDAO, new AnnotatedRequestPopulator<SinglePolygonMapQuery>(SinglePolygonMapQuery.class), "SinglePolygonMapQuery", "single polygon query", null, callback);
		
	}

}
