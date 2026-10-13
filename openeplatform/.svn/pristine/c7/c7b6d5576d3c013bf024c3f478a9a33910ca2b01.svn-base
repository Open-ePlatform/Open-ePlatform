package com.nordicpeak.flowengine.queries.childquery.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.childrelationprovider.Child;
import com.nordicpeak.childrelationprovider.Guardian;
import com.nordicpeak.flowengine.queries.childquery.enums.ChildAgeCalculationMode;
import com.nordicpeak.flowengine.queries.childquery.enums.ChildSelectionStatus;

@XMLElement(name = "Child")
public class StoredChild extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = 7634780772195411122L;

	@XMLElement
	private final String firstname;

	@XMLElement
	private final String lastname;

	@XMLElement
	private final String citizenIdentifier;

	@XMLElement
	private String address;

	@XMLElement
	//TODO fix typo
	private String zipcode;

	@XMLElement
	private String postalAddress;
	
	@XMLElement
	private ChildSelectionStatus selectionStatus = ChildSelectionStatus.SELECTABLE;
	
	@DAOManaged
	protected String addressUUID;

	private String municipalityCode;

	@XMLElement(name = "Guardians")
	private List<StoredGuardian> storedGuardians;

	@XMLElement(fixCase = true)
	private List<ChildAttribute> attributes;

	private boolean testChild;
	
	private ChildAgeCalculationMode calculationMode = ChildAgeCalculationMode.BY_YEAR;

	public StoredChild(Child child) {
		super();

		citizenIdentifier = child.getCitizenIdentifier();
		firstname = child.getFirstname();
		lastname = child.getLastname();
		address = child.getAddress();
		zipcode = child.getZipCode();
		postalAddress = child.getPostalAddress();
		municipalityCode = child.getMunicipalityCode();
		addressUUID = child.getAddressUUID();

		if (!CollectionUtils.isEmpty(child.getGuardians())) {

			storedGuardians = new ArrayList<StoredGuardian>(child.getGuardians().size());

			for (Guardian guardian : child.getGuardians()) {

				storedGuardians.add(new StoredGuardian(guardian));
			}
		}
	}

	public StoredChild(String firstname, String lastname, String citizenIdentifier) {

		this.firstname = firstname;
		this.lastname = lastname;
		this.citizenIdentifier = citizenIdentifier;
	}

	public String getFirstname() {

		return firstname;
	}

	public String getLastname() {

		return lastname;
	}

	public String getCitizenIdentifier() {

		return citizenIdentifier;
	}

	public List<StoredGuardian> getGuardians() {

		return storedGuardians;
	}

	public void setGuardians(List<StoredGuardian> storedGuardians) {

		this.storedGuardians = storedGuardians;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getZipcode() {

		return zipcode;
	}

	public void setZipcode(String zipcode) {

		this.zipcode = zipcode;
	}

	public String getPostalAddress() {

		return postalAddress;
	}

	public void setPostalAddress(String postalAddress) {

		this.postalAddress = postalAddress;
	}
	
	public ChildSelectionStatus getSelectable() {
		return selectionStatus;
	}

	public void setSelectable(ChildSelectionStatus selectable) {
		this.selectionStatus = selectable;
	}

	public Integer getAge() {

		return getAge(citizenIdentifier, calculationMode);
	}
	
	public static Integer getAge(String citizenIdentifier, ChildAgeCalculationMode calculationMode) {

		if (citizenIdentifier != null) {
			
			if (calculationMode == ChildAgeCalculationMode.BY_YEAR) {

				int birthYear = Integer.parseInt(citizenIdentifier.substring(0, 4));
				int currentYear = Integer.parseInt(DateUtils.YEAR_FORMATTER.format(new Date()));
				
				return currentYear - birthYear;

			} else {
				Calendar today = Calendar.getInstance();
				Calendar birthday = Calendar.getInstance();

				birthday.set(Calendar.YEAR, Integer.valueOf(citizenIdentifier.substring(0, 4)));
				birthday.set(Calendar.MONTH, Integer.valueOf(citizenIdentifier.substring(4, 6)) - 1);
				birthday.set(Calendar.DAY_OF_MONTH, Integer.valueOf(citizenIdentifier.substring(6, 8)));

				int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);

				if (!isBirthdayPassed(today, birthday)) {
					age--;
				}

				return age;
			}
		}

		return null;
	}
	
	private static boolean isBirthdayPassed(Calendar today, Calendar birthday) {

		if (today.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {

			return false;

		} else if (today.get(Calendar.MONTH) == birthday.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH)) {

			return false;
		}

		return true;
	}

	@Override
	public Element toXML(Document doc) {

		Element element = super.toXML(doc);
		
		XMLUtils.appendNewElement(doc, element, "Age", getAge());

		return element;
	}

	@Override
	public String toString() {

		return firstname + " " + lastname + " (" + citizenIdentifier + ")";
	}

	public String getMunicipalityCode() {

		return municipalityCode;
	}

	public void setMunicipalityCode(String municipalityCode) {

		this.municipalityCode = municipalityCode;
	}

	public boolean isTestChild() {
		return testChild;
	}

	public void setTestChild(boolean test) {
		this.testChild = test;
	}

	public List<ChildAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<ChildAttribute> storedFields) {
		this.attributes = storedFields;
	}

	public void setAttributes(Map<String, String> fields) {

		if (fields == null) {

			attributes = null;

		} else {

			attributes = new ArrayList<ChildAttribute>();

			for (Entry<String, String> entry : fields.entrySet()) {

				attributes.add(new ChildAttribute(entry.getKey(), entry.getValue()));
			}
		}
	}

	
	public String getAddressUUID() {
	
		return addressUUID;
	}

	public ChildAgeCalculationMode getCalculationMode() {

		return calculationMode;
	}

	public void setCalculationMode(ChildAgeCalculationMode calculationMode) {

		this.calculationMode = calculationMode;
	}

}
