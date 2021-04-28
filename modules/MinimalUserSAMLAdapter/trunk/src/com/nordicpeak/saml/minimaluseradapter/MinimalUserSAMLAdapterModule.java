package com.nordicpeak.saml.minimaluseradapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.schema.impl.XSAnyImpl;
import org.opensaml.xml.schema.impl.XSStringImpl;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.UnableToAddUserException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.minimaluser.MinimalUser;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.saml.SAMLUserAdapter;
import com.nordicpeak.saml.SAMLUserAdapterResponse;

public class MinimalUserSAMLAdapterModule extends AnnotatedForegroundModule implements SAMLUserAdapter {

	public static final String CITIZEN_IDENTIFIER = "citizenIdentifier";

	@ModuleSetting(id = "citizenIdentifierAttribute")
	@TextFieldSettingDescriptor(id = "citizenIdentifierAttribute", name = "Assertion identifier attribute", description = "The name of the attribute in the assertion containing the user identifier", required = true)
	protected String assertionIdentifierAttribute = CITIZEN_IDENTIFIER;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User identifier attribute", description = "The name of the attribute used for looking up users based on the value of the assertion identifier attribute")
	protected String userIdentifierAttribute = CITIZEN_IDENTIFIER;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "First name attribute", description = "The name of the attribute in the assertion containing the first name", required = true)
	protected String firstNameAttribute = "givenName";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Last name attribute", description = "The name of the attribute in the assertion containing the last name", required = true)
	protected String lastNameAttribute = "surname";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Do not update firstname", description = "When existing user is logging in, firstname will not be updated even if they differ")
	protected boolean skipNameUpdate = false;
	
	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Extra attributes", description = "Extra assertion attributes to be set on user in the format of one attribute per line. Example someAssertionAttribute;someLocalAttribute")
	private List<String> extraAttributes;

	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Default groups", description = "Groups added to new users")
	private List<Integer> defaultGroups;

	private Map<String, String> attributeMappings;

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		if (extraAttributes != null) {

			attributeMappings = new HashMap<String, String>(extraAttributes.size());

			for (String attributeDefinition : extraAttributes) {

				if (attributeDefinition.contains(";")) {

					String[] splitDefinition = attributeDefinition.split(";");

					if (splitDefinition.length > 2 || StringUtils.isEmpty(splitDefinition[0]) || StringUtils.isEmpty(splitDefinition[1])) {

						log.warn("Unable to parse format of attribute: " + attributeDefinition + ", ignoring attribute.");
						continue;
					}

					attributeMappings.put(splitDefinition[0].trim(), splitDefinition[1].trim());

				} else if (!attributeDefinition.trim().isEmpty()) {

					String attribute = attributeDefinition.trim();

					attributeMappings.put(attribute, attribute);
				}
			}

		} else {

			attributeMappings = null;

		}
	}

	public SAMLUserAdapterResponse getUser(Assertion assertion) {

		HashMap<String, String> attributeMap = new HashMap<String, String>(assertion.getAttributeStatements().size());

		for (Attribute attribute : assertion.getAttributeStatements().get(0).getAttributes()) {

			if (CollectionUtils.isEmpty(attribute.getAttributeValues())) {

				continue;
			}

			Object rawAttributeValue = attribute.getAttributeValues().get(0);

			String attributeValue;

			if (rawAttributeValue instanceof XSStringImpl) {

				attributeValue = ((XSStringImpl) rawAttributeValue).getValue();

			} else if (rawAttributeValue instanceof XSAnyImpl) {

				attributeValue = ((XSAnyImpl) rawAttributeValue).getTextContent();

			} else {

				log.warn("Unsupported attribute value type received " + rawAttributeValue.getClass() + " with toString value " + rawAttributeValue.toString());

				continue;
			}

			attributeMap.put(attribute.getName(), attributeValue);
		}

		String userIdentifier = attributeMap.get(assertionIdentifierAttribute);

		if (userIdentifier == null) {

			log.warn("No " + assertionIdentifierAttribute + " attribute found in assertion.");
			return null;
		}

		User user = null;

		if (userIdentifierAttribute.equals("email")) {

			user = systemInterface.getUserHandler().getUserByEmail(userIdentifier, true, true);

		} else if (userIdentifierAttribute.equals("username")) {

			user = systemInterface.getUserHandler().getUserByUsername(userIdentifier, true, true);

		} else {

			user = systemInterface.getUserHandler().getUserByAttribute(userIdentifierAttribute, userIdentifier, true, true);
		}

		String firstName = attributeMap.get(firstNameAttribute);

		if (firstName == null) {

			log.warn("No " + firstNameAttribute + " attribute found in assertion.");
			return null;
		}

		String lastName = attributeMap.get(lastNameAttribute);

		if (lastName == null) {

			log.warn("No " + lastNameAttribute + " attribute found in assertion.");
			return null;
		}

		if (user != null) {

			if (user instanceof MutableUser) {

				boolean update = false;

				MutableUser mutableUser = ((MutableUser) user);

					
				if (!user.getFirstname().equals(firstName) && !skipNameUpdate) {

					mutableUser.setFirstname(firstName);
					update = true;
				}
	
				if (!user.getLastname().equals(lastName)) {

					mutableUser.setLastname(lastName);
					update = true;
				}

				if (updateAttributes(mutableUser, attributeMap)) {
					update = true;
				}

				if (attributeMappings != null && mutableUser.getAttributeHandler() != null) {

					MutableAttributeHandler attributeHandler = mutableUser.getAttributeHandler();

					for (String attribute : attributeMappings.keySet()) {

						if (updateAttribute(attributeMappings.get(attribute), attributeMap.get(attribute), attributeHandler)) {

							update = true;
						}

					}

				}

				if (update) {

					try {
						log.info("Updating name of user " + user);
						systemInterface.getUserHandler().updateUser(user, false, false, true);

					} catch (UnableToUpdateUserException e) {

						log.error("Error updating user " + user, e);
					}
				}
			}

			return new SAMLUserAdapterResponse(user);
		}

		MinimalUser minimalUser = createUser(userIdentifier, firstName, lastName, attributeMap);

		try {
			this.systemInterface.getUserHandler().addUser(minimalUser);
			log.info("Added account for user " + minimalUser);

		} catch (UnableToAddUserException e) {

			log.error("Unable to add account for user " + minimalUser, e);

			return null;
		}

		return new SAMLUserAdapterResponse(minimalUser);
	}

	private boolean updateAttribute(String attributeName, String attributeValue, MutableAttributeHandler attributeHandler) {

		boolean update = false;

		if (StringUtils.isEmpty(attributeValue)) {
			attributeHandler.removeAttribute(attributeName);
			return true;
		}

		String value = attributeHandler.getString(attributeName);

		if (StringUtils.isEmpty(value) || !value.equals(attributeValue)) {
			update = true;
		}

		if (update) {
			attributeHandler.setAttribute(attributeName, attributeValue);
		}

		return update;
	}

	protected boolean updateAttributes(MutableUser user, HashMap<String, String> attributeMap) {

		return false;
	}

	protected MinimalUser createUser(String userIdentifier, String firstName, String lastName, HashMap<String, String> attributeMap) {

		MinimalUser minimalUser = new MinimalUser();
		minimalUser.setFirstname(firstName);
		minimalUser.setLastname(lastName);
		minimalUser.setAdded(TimeUtils.getCurrentTimestamp());
		minimalUser.setEnabled(true);

		if (userIdentifierAttribute.equals("email")) {

			minimalUser.setEmail(userIdentifier);

		} else if (userIdentifierAttribute.equals("username")) {

			minimalUser.setUsername(userIdentifier);

		} else {

			minimalUser.getAttributeHandler().setAttribute(userIdentifierAttribute, userIdentifier);
		}

		if (defaultGroups != null) {

			minimalUser.setGroups(systemInterface.getGroupHandler().getGroups(defaultGroups, true));
		}

		return minimalUser;
	}

}
