package com.nordicpeak.flowengine.queries.checkboxpaymentquery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.interfaces.MutableAlternative;

public class CheckboxPaymentAlternativesPopulator {

	public List<CheckboxPaymentAlternative> populate(List<CheckboxPaymentAlternative> currentAlternatives, HttpServletRequest req, List<ValidationError> validationErrors) {

		String[] alternativeIDs = req.getParameterValues("alternativeID");

		List<CheckboxPaymentAlternative> alternatives = new ArrayList<CheckboxPaymentAlternative>();

		if (alternativeIDs != null) {

			for (String alternativeID : alternativeIDs) {

				//TODO validate max length
				String name = ValidationUtils.validateNotEmptyParameter("alternative_" + alternativeID, req, validationErrors);

				String sortOrder = req.getParameter("sortorder_" + alternativeID);

				if (!StringUtils.isEmpty(name) && NumberUtils.isInt(sortOrder)) {

					String[] nameParts = name.split(":");

					CheckboxPaymentAlternative alternative = new CheckboxPaymentAlternative();

					alternative.setName(nameParts[0]);

					if (nameParts.length > 2 && NumberUtils.isInt(nameParts[2])) {
						alternative.setDescription(nameParts[1]);
						alternative.setAmount(Integer.valueOf(nameParts[2]));
					}

					alternative.setSortIndex(NumberUtils.toInt(sortOrder));

					if (NumberUtils.isInt(alternativeID)) {

						this.checkForExistingAlternatives(currentAlternatives, alternative, NumberUtils.toInt(alternativeID));

					}

					alternatives.add(alternative);

				}

			}

		}

		return alternatives;

	}

	public List<CheckboxPaymentAlternative> populate(XMLParser xmlParser, List<ValidationError> errors) throws ValidationException {

		List<XMLParser> xmlParsers = xmlParser.getNodes("Alternatives/CheckboxPaymentAlternative");

		if (CollectionUtils.isEmpty(xmlParsers)) {

			errors.add(new ValidationError("NoAlternativesFound"));

			return null;

		}

		List<CheckboxPaymentAlternative> alternatives = new ArrayList<CheckboxPaymentAlternative>();

		for (XMLParser parser : xmlParsers) {

			CheckboxPaymentAlternative alternative = new CheckboxPaymentAlternative();
			alternative.populate(parser);
			alternatives.add(alternative);

		}

		return alternatives;

	}

	protected void checkForExistingAlternatives(List<CheckboxPaymentAlternative> currentAlternatives, CheckboxPaymentAlternative alternative, Integer alternativeID) {

		if (!CollectionUtils.isEmpty(currentAlternatives)) {

			for (MutableAlternative queryAlternative : currentAlternatives) {

				if (queryAlternative.getAlternativeID().equals(alternativeID)) {

					alternative.setAlternativeID(alternativeID);
					break;

				}

			}

		}

	}

}
