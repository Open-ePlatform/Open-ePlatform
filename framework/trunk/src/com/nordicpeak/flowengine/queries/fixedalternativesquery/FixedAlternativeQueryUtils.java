package com.nordicpeak.flowengine.queries.fixedalternativesquery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.MutableAlternative;
import com.nordicpeak.flowengine.interfaces.QueryHandler;

public class FixedAlternativeQueryUtils {

	@SuppressWarnings("unchecked")
	public static <X extends FixedAlternativesQuery> FixedAlternativesQueryCallback<X> getGenericFixedAlternativesQueryCallback(Class<? extends FixedAlternativesQuery> clazz, QueryHandler queryHandler, String queryTypeID) {

		return (FixedAlternativesQueryCallback<X>) queryHandler.getQueryProvider(queryTypeID);
	}

	public static List<Integer> getAlternativeIDs(FixedAlternativesQuery query) {

		if (query.getAlternatives() == null) {

			return null;
		}

		List<Integer> alternativeIDs = new ArrayList<Integer>(query.getAlternatives().size());

		for (ImmutableAlternative alternative : query.getAlternatives()) {

			alternativeIDs.add(alternative.getAlternativeID());
		}

		return alternativeIDs;
	}

	public static void clearAlternativeIDs(List<? extends MutableAlternative> alternatives) {

		if (alternatives != null) {

			for (MutableAlternative alternative : alternatives) {

				alternative.setAlternativeID(null);
			}
		}
	}

	public static Map<Integer, Integer> getAlternativeConversionMap(List<? extends ImmutableAlternative> alternatives, List<Integer> oldAlternativeIDs) {

		if(oldAlternativeIDs != null){

			HashMap<Integer, Integer> alternativeConversionMap = new HashMap<Integer, Integer>(oldAlternativeIDs.size());

			int index = 0;

			for(Integer oldAlternativeID : oldAlternativeIDs){

				alternativeConversionMap.put(oldAlternativeID, alternatives.get(index).getAlternativeID());

				index++;
			}

			return alternativeConversionMap;
		}

		return null;
	}

	public static void appendExportXMLAlternatives(Document doc, Element element, FixedAlternativesQueryInstance queryInstance) {

		if(queryInstance.getAlternatives() != null){

			for(ImmutableAlternative alternative : queryInstance.getAlternatives()){

				XMLUtils.appendNewElement(doc, element, "Value", alternative.getName());
			}
		}

		if(queryInstance.getFreeTextAlternativeValue() != null){

			XMLUtils.appendNewCDATAElement(doc, element, "TextAlternative", queryInstance.getFreeTextAlternativeValue());
		}
	}
}
