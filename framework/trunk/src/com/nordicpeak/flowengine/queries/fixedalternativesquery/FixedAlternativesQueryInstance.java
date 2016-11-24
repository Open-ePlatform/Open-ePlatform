package com.nordicpeak.flowengine.queries.fixedalternativesquery;

import java.util.List;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.QueryInstance;


public interface FixedAlternativesQueryInstance extends QueryInstance{

	/**
	 * @return the alternatives that have been selected
	 */
	public List<? extends ImmutableAlternative> getAlternatives();

	public String getFreeTextAlternativeValue();
}
