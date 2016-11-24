package com.nordicpeak.flowengine.queries.fixedalternativesquery;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryHandler;


public interface FixedAlternativesQuery extends Query{

	public List<? extends ImmutableAlternative> getAlternatives();

	public List<? extends FixedAlternativesQueryInstance> getInstances(List<Integer> queryInstanceIDs, QueryHandler queryHandler) throws SQLException;

	public String getFreeTextAlternative();

	public Map<Integer,Integer> getAlternativeConversionMap();
}
