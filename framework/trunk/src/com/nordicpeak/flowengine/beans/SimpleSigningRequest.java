package com.nordicpeak.flowengine.beans;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.standardutils.hash.HashAlgorithms;
import se.unlogic.standardutils.hash.HashUtils;
import se.unlogic.standardutils.string.StringUtils;

import com.nordicpeak.flowengine.interfaces.GenericSigningRequest;

public class SimpleSigningRequest implements GenericSigningRequest {

	private final String description;
	private final String hashToSign;
	private final String signingFormURL;
	private final String processSigningURL;

	public SimpleSigningRequest(String description, String dataToSign, String signingFormURL, String processSigningURL) {

		super();

		this.description = description;
		this.hashToSign = HashUtils.hash(dataToSign, HashAlgorithms.SHA1);
		this.signingFormURL = signingFormURL;
		this.processSigningURL = processSigningURL;
	}
	
	public SimpleSigningRequest(String description, File fileToSign, String signingFormURL, String processSigningURL) throws IOException {

		super();

		this.description = description;
		this.hashToSign = HashUtils.hash(fileToSign, HashAlgorithms.SHA1);
		this.signingFormURL = signingFormURL;
		this.processSigningURL = processSigningURL;
	}

	@Override
	public String getDescription() {

		return description;
	}

	@Override
	public String getHashToSign() {

		return hashToSign;
	}

	@Override
	public String getSigningFormURL(HttpServletRequest req) {

		return signingFormURL;
	}

	@Override
	public String getProcessSigningURL(HttpServletRequest req) {

		return processSigningURL;
	}

	@Override
	public String toString() {

		return StringUtils.toLogFormat(description, 40);
	}
}
