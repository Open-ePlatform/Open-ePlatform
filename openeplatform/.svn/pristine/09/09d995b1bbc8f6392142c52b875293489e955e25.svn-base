package com.nordicpeak.flowengine.evaluators.basequerystateevaluator;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.evaluators.baseevaluator.BaseEvaluatorCRUD;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStep;

public class BaseQueryStateEvaluationCRUD<T extends BaseQueryStateEvaluator, QP extends BaseQueryStateEvaluationProviderModule<T>> extends BaseEvaluatorCRUD<T, QP> {

	public BaseQueryStateEvaluationCRUD(Class<T> beanClass, AnnotatedDAOWrapper<T, Integer> evaluatorDAO, BeanRequestPopulator<T> populator, String typeElementName, String typeLogName, String listMethodAlias, QP callback) {

		super(beanClass, evaluatorDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
	}

	@Override
	protected void appendUpdateFormData(T bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		ImmutableFlow flow = callback.getFlowAdminModule().getFlow(bean.getEvaluatorDescriptor().getQueryDescriptor().getStep().getFlow().getFlowID());

		updateTypeElement.appendChild(flow.toXML(doc));

		Element disabledQueriesElement = doc.createElement("DisabledQueries");
		updateTypeElement.appendChild(disabledQueriesElement);

		outer: for (ImmutableStep step : flow.getSteps()) {

			if (step.getQueryDescriptors() == null) {

				continue;
			}

			Integer queryID = bean.getEvaluatorDescriptor().getQueryDescriptor().getQueryID();

			for (ImmutableQueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

				XMLUtils.appendNewElement(doc, disabledQueriesElement, "queryID", queryDescriptor.getQueryID());

				if (queryDescriptor.getQueryID().equals(queryID)) {

					break outer;
				}

			}

		}

	}

}
