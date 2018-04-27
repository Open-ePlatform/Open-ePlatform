package com.nordicpeak.flowengine.managers;

import java.io.Serializable;
import java.util.List;

import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableStep;


public class ImmutableManagedStep implements Serializable{

	private static final long serialVersionUID = 8617217586398797775L;

	private ImmutableStep step;
	
	private List<ImmutableQueryInstance> queryInstances;

	public ImmutableManagedStep(Step step, List<ImmutableQueryInstance> queryInstances) {

		super();
		this.step = step;
		this.queryInstances = queryInstances;
	}

	
	public ImmutableStep getStep() {
	
		return step;
	}

	
	public List<ImmutableQueryInstance> getQueryInstances() {
	
		return queryInstances;
	}
}
