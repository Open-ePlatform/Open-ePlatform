package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.hash.HashAlgorithm;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;


public interface GenericSigningProvider {

	public ViewFragment showSignForm(HttpServletRequest req, HttpServletResponse res, User user, GenericSigningRequest signingRequest, List<ValidationError> validationErrors) throws Exception;
	
	public SigningResponse processSigning(HttpServletRequest req, HttpServletResponse res, User user, GenericSigningRequest signingRequest) throws ValidationException, Exception;
	
	public HashAlgorithm getSignHashAlgorithm();
	
}
