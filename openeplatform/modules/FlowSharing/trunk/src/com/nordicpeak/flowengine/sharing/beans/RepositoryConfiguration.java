package com.nordicpeak.flowengine.sharing.beans;

import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class RepositoryConfiguration extends GeneratedElementable {

	@XMLElement
	private String name;

	@XMLElement
	private String description;

	@XMLElement
	private String uploadDescription;

	@XMLElement
	private String url;

	private Long lastUpdate;

	private String username;
	private String password;

	public RepositoryConfiguration(String url, String username, String password) {
		super();

		this.url = url;
		this.username = username;
		this.password = password;
	}

	public String getUrl() {

		return url;
	}

	public void setUrl(String url) {

		this.url = url;
	}

	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {

		this.username = username;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public String getUploadDescription() {

		return uploadDescription;
	}

	public void setUploadDescription(String uploadDescription) {

		this.uploadDescription = uploadDescription;
	}

	@Override
	public String toString() {

		if (!StringUtils.isEmpty(name)) {

			return name + " (url= " + url + ", username= " + username + ")";

		} else {

			return "(url= " + url + ", username= " + username + ")";
		}
	}

	public String getName() {

		return name;
	}

	public void setName(String cachedName) {

		this.name = cachedName;
	}

	public Long getLastUpdate() {

		return lastUpdate;
	}

	public void setLastUpdate(Long lastUpdate) {

		this.lastUpdate = lastUpdate;
	}

}
