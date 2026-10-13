package com.nordicpeak.flowengine.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import se.unlogic.hierarchy.core.beans.SimpleAttribute;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.ElementableListener;

import com.nordicpeak.flowengine.Constants;

public class CitizenIdentifierUtils {

	public static String getUserOrManagerCitizenIdentifier(User user) {

		if (user == null) {

			return null;
		}

		String citizenIdentifier = UserUtils.getAttribute(Constants.USER_CITIZEN_IDENTIFIER_ATTRIBUTE, user);

		if (StringUtils.isEmpty(citizenIdentifier)) {

			citizenIdentifier = UserUtils.getAttribute(Constants.MANAGER_CITIZEN_IDENTIFIER_ATTRIBUTE, user);
		}

		return citizenIdentifier;

	}

	public static ElementableListener<SimpleAttribute> getMaskCitizenIdentifierAttributeElementableListener() {

		return (Document document, Element element, SimpleAttribute attributeValue) -> {

			Node nameNode = element.getElementsByTagName("Name").item(0);

			if (nameNode != null && isCitizenIdentifierAttribute(nameNode.getTextContent())) {

				Node valueNode = element.getElementsByTagName("Value").item(0);

				if (valueNode != null) {

					valueNode.setTextContent(maskCitizenIDLastFourDigits(valueNode.getTextContent()));
				}
			}
		};
	}

	public static boolean isCitizenIdentifierAttribute(String attributeName) {

		return Constants.USER_CITIZEN_IDENTIFIER_ATTRIBUTE.equals(attributeName) || Constants.MANAGER_CITIZEN_IDENTIFIER_ATTRIBUTE.equals(attributeName);
	}

	public static String maskCitizenIDLastFourDigits(String citizenID) {

		if (citizenID != null && citizenID.length() >= 4) {

			return citizenID.substring(0, citizenID.length() - 4) + "XXXX";
		}

		return citizenID;
	}
	
}
