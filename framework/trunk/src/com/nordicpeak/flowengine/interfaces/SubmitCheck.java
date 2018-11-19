package com.nordicpeak.flowengine.interfaces;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;

public interface SubmitCheck {

	boolean isValidForSubmit(MutableAttributeHandler attributeHandler, User poster, QueryHandler queryHandler) throws Exception;

}
