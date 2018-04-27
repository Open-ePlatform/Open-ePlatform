package com.nordicpeak.flowengine.beans;

import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public final class EvaluatorTypeDescriptor extends GeneratedElementable {

	@XMLElement
	private String evaluatorTypeID;

	@XMLElement
	private String name;

	public EvaluatorTypeDescriptor(String evaluatorTypeID, String name) {

		super();

		if (StringUtils.isEmpty(evaluatorTypeID)) {

			throw new RuntimeException("evaluatorTypeID cannot be null or empty");
		}

		this.evaluatorTypeID = evaluatorTypeID;
		this.name = name;
	}

	public String getEvaluatorTypeID() {

		return evaluatorTypeID;
	}

	public String getName() {

		return name;
	}

	@Override
	public String toString() {

		return name + " (ID: " + evaluatorTypeID + ")";
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((evaluatorTypeID == null) ? 0 : evaluatorTypeID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		EvaluatorTypeDescriptor other = (EvaluatorTypeDescriptor)obj;
		if(evaluatorTypeID == null){
			if(other.evaluatorTypeID != null){
				return false;
			}
		}else if(!evaluatorTypeID.equals(other.evaluatorTypeID)){
			return false;
		}
		return true;
	}
}
