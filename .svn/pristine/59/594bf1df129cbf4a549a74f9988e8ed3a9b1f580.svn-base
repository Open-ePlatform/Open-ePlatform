package com.nordicpeak.flowengine.listeners;

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.ElementableListener;

import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.Evaluator;
import com.nordicpeak.flowengine.validationerrors.EvaluatorExportValidationError;


public class EvaluatorDescriptorElementableListener implements ElementableListener<EvaluatorDescriptor> {

	protected final Logger log = Logger.getLogger(getClass());
	
	protected final EvaluationHandler evaluationHandler;
	protected final List<ValidationError> validationErrors;
	
	public EvaluatorDescriptorElementableListener(EvaluationHandler evaluationHandler, List<ValidationError> validationErrors) {

		super();
		this.evaluationHandler = evaluationHandler;
		this.validationErrors = validationErrors;
	}

	@Override
	public void elementGenerated(Document doc, Element element, EvaluatorDescriptor evaluatorDescriptor) {

		try {
			Evaluator evaluator = evaluationHandler.getEvaluator(evaluatorDescriptor, null);
			
			element.appendChild(evaluator.toXML(doc));
			
		} catch (Exception e) {

			log.error("Error exporting evaluator " + evaluatorDescriptor, e);
			
			validationErrors.add(new EvaluatorExportValidationError(evaluatorDescriptor));
		}
	}
}

