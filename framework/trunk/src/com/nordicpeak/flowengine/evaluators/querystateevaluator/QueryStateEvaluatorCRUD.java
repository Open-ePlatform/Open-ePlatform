package com.nordicpeak.flowengine.evaluators.querystateevaluator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.evaluators.basequerystateevaluator.BaseQueryStateEvaluationCRUD;
import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQuery;

public class QueryStateEvaluatorCRUD extends BaseQueryStateEvaluationCRUD<QueryStateEvaluator, QueryStateEvaluationProviderModule> {

	private static final AnnotatedRequestPopulator<QueryStateEvaluator> POPULATOR = new AnnotatedRequestPopulator<QueryStateEvaluator>(QueryStateEvaluator.class);

	protected static EnumPopulator<SelectionMode> SELECTIONMODE_POPULATOR = new EnumPopulator<SelectionMode>(SelectionMode.class);

	public QueryStateEvaluatorCRUD(Class<QueryStateEvaluator> beanClass, AnnotatedDAOWrapper<QueryStateEvaluator, Integer> evaluatorDAO, QueryStateEvaluationProviderModule callback) {

		super(beanClass, evaluatorDAO, POPULATOR, "QueryStateEvaluator", "query state evaluator", null, callback);
	}

	@Override
	protected void appendUpdateFormData(QueryStateEvaluator bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendUpdateFormData(bean, doc, updateTypeElement, user, req, uriParser);
		
		FixedAlternativesQuery query = (FixedAlternativesQuery) callback.getQueryHandler().getQuery((MutableQueryDescriptor) bean.getEvaluatorDescriptor().getQueryDescriptor(), true);

		List<? extends ImmutableAlternative> alternatives = query.getAlternatives();

		if (alternatives != null) {

			Element alternativesElement = doc.createElement("Alternatives");
			updateTypeElement.appendChild(alternativesElement);

			for (ImmutableAlternative alternative : alternatives) {

				Element alternativeElement = doc.createElement("Alternative");
				alternativesElement.appendChild(alternativeElement);

				XMLUtils.appendNewElement(doc, alternativeElement, "alternativeID", alternative.getAlternativeID());
				XMLUtils.appendNewCDATAElement(doc, alternativeElement, "name", alternative.getName());
			}
			
			XMLUtils.appendNewCDATAElement(doc, alternativesElement, "FreeTextAlternative", query.getFreeTextAlternative());
		}

	}

}
