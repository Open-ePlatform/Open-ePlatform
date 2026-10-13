package com.nordicpeak.flowengine.sharing.beans;

import java.sql.Timestamp;
import java.util.Collection;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.i18n.Language;

public class SourceUser extends User {

	private static final long serialVersionUID = 6639794024326577470L;

	private final Source source;

	public SourceUser(Source source) {
		super();

		this.source = source;
	}

	public Source getSource() {

		return source;
	}

	@Override
	public boolean isAdmin() {

		return false;
	}

	@Override
	public String getEmail() {

		return null;
	}

	@Override
	public String getFirstname() {

		return source.getName();
	}

	@Override
	public Timestamp getCurrentLogin() {

		return null;
	}

	@Override
	public Timestamp getLastLogin() {

		return null;
	}

	@Override
	public String getLastname() {

		return "";
	}

	@Override
	public String getPassword() {

		return source.getPassword();
	}

	@Override
	public String getUsername() {

		return source.getUsername();
	}

	@Override
	public Integer getUserID() {

		return null;
	}

	@Override
	public boolean isEnabled() {

		return true;
	}

	@Override
	public Timestamp getAdded() {

		return null;
	}

	@Override
	public Collection<Group> getGroups() {

		return null;
	}

	@Override
	public Language getLanguage() {

		return null;
	}

	@Override
	public String getPreferedDesign() {

		return null;
	}

	@Override
	public boolean hasFormProvider() {

		return false;
	}

	@Override
	public void setCurrentLogin(Timestamp currentLogin) {

	}

}
