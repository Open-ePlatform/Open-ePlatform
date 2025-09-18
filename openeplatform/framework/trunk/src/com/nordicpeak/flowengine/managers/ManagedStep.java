package com.nordicpeak.flowengine.managers;

import java.io.Serializable;
import java.util.List;

import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.interfaces.ImmutableStep;

public class ManagedStep implements Serializable {

	private static final long serialVersionUID = 8617217586398797775L;

	private ImmutableStep step;

	private List<ManagedQueryInstance> managedQueryInstances;

	public ManagedStep(Step step, List<ManagedQueryInstance> queryInstances) {

		super();
		this.step = step;
		this.managedQueryInstances = queryInstances;
	}

	public ImmutableStep getStep() {

		return step;
	}

	public List<ManagedQueryInstance> getManagedQueryInstances() {

		return managedQueryInstances;
	}
}
