package com.nordicpeak.flowengine.beans;

import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;

import com.nordicpeak.flowengine.interfaces.InstanceMetadata;

public class DefaultInstanceMetadata implements InstanceMetadata {

	private SiteProfile siteProfile;

	public DefaultInstanceMetadata(SiteProfile siteProfile) {

		this.siteProfile = siteProfile;
	}

	@Override
	public SiteProfile getSiteProfile() {

		return siteProfile;
	}

	public void setSiteProfile(SiteProfile siteProfile) {

		this.siteProfile = siteProfile;
	}

}
