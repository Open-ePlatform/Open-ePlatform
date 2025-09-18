package com.nordicpeak.flowengine.populators;

import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.xml.XMLUtils;


public class XMLElementNamePopulator implements BeanStringPopulator<String> {

	@Override
	public boolean validateFormat(String value) {

		if(value != null && XMLUtils.toValidElementName(value).equals(value)){

			return true;
		}

		return false;
	}

	@Override
	public String getValue(String value) {

		return value;
	}

	@Override
	public Class<? extends String> getType() {

		return String.class;
	}

	@Override
	public String getPopulatorID() {

		return null;
	}

}
