package com.nordicpeak.flowengine.queries.fixedalternativesquery;

import java.util.List;
import java.util.Map;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.Query;


public interface FixedAlternativesQuery extends Query{

	public List<? extends ImmutableAlternative> getAlternatives();

	public String getFreeTextAlternative();

	public Map<Integer,Integer> getAlternativeConversionMap();

}
