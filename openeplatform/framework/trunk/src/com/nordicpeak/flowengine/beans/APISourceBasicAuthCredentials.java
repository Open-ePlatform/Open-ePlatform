package com.nordicpeak.flowengine.beans;

public class APISourceBasicAuthCredentials {

	private final String username;

	private final String password;

	public APISourceBasicAuthCredentials(String username, String password) {

		this.username = username;
		this.password = password;

	}

	public String getUsername() {

		return username;
	}

	public String getPassword() {

		return password;
	}

}
