package com.nordicpeak.flowengine.interfaces;

import se.unlogic.hierarchy.core.beans.User;

public interface SubmitCheck {

	boolean isValidForSubmit(User poster, QueryHandler queryHandler);

}
