package com.nordicpeak.flowengine.flowinstancerafflesummary.cruds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.ElementableListener;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.flowinstancerafflesummary.RaffleModule;
import com.nordicpeak.flowengine.flowinstancerafflesummary.beans.RaffleFlow;
import com.nordicpeak.flowengine.flowinstancerafflesummary.beans.RaffleRound;
import com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors.RaffleFlowNotFound;

public class RaffleRoundCRUD extends IntegerBasedCRUD<RaffleRound, RaffleModule> {

	public static final StringIntegerValidator stringIntegerValidator = new StringIntegerValidator();

	public RaffleRoundCRUD(CRUDDAO<RaffleRound, Integer> crudDAO, RaffleModule callback) {

		super(crudDAO, new AnnotatedRequestPopulator<RaffleRound>(RaffleRound.class), "RaffleRound", "raffleRound", "/list", callback);
	}

	protected void populateRaffleFlows(RaffleRound raffleRound, RaffleRound oldRaffleRound, HttpServletRequest req, User user, URIParser uriParser, List<ValidationError> validationErrors) throws ValidationException, Exception {

		List<Integer> flowIDs = CollectionUtils.removeDuplicates(NumberUtils.toInt(req.getParameterValues("flowID")));
		

		if (flowIDs == null) {

			raffleRound.setRaffleFlows(null);
			return;
		}

		List<RaffleFlow> raffleFlows = new ArrayList<RaffleFlow>();

		for (Integer flowID : flowIDs) {

			String existingRaffleFlowIDString = req.getParameter("raffleFlowID-" + flowID);
			Integer existingRaffleFlowID = null;

			if (!StringUtils.isEmpty(existingRaffleFlowIDString)) {

				if (!stringIntegerValidator.validateFormat(existingRaffleFlowIDString)) {

					validationErrors.add(new ValidationError("raffleFlowID-" + flowID, ValidationErrorType.InvalidFormat));
					continue;
				}

				existingRaffleFlowID = Integer.parseInt(existingRaffleFlowIDString);
			}

			RaffleFlow raffleFlow = null;

			if (existingRaffleFlowID != null) {

				for (RaffleFlow oldRaffleFlow : oldRaffleRound.getRaffleFlows()) {

					if (oldRaffleFlow.getRaffleFlowID().equals(existingRaffleFlowID)) {

						raffleFlow = oldRaffleFlow;
						break;
					}
				}

				if (raffleFlow == null) {
					validationErrors.add(new RaffleFlowNotFound(existingRaffleFlowID));
					continue;
				}

			} else {

				raffleFlow = new RaffleFlow();
				raffleFlow.setRound(raffleRound);
				raffleFlow.setFlowID(flowID);
			}

			Integer raffledStatusID = null;
			String raffledStatusIDString = req.getParameter("raffledStatusID-" + flowID);

			if (!StringUtils.isEmpty(raffledStatusIDString)) {

				if (!stringIntegerValidator.validateFormat(raffledStatusIDString)) {

					validationErrors.add(new ValidationError("raffledStatusID-" + flowID, ValidationErrorType.InvalidFormat));
				} else {

					raffledStatusID = Integer.parseInt(raffledStatusIDString);
				}
			}

			raffleFlow.setRaffledStatusID(raffledStatusID);

			String[] excludedStatusIDs = req.getParameterValues("excludedStatusIDs-" + flowID);

			if (ArrayUtils.isEmpty(excludedStatusIDs)) {

				raffleFlow.setExcludedStatusIDs(null);

			} else {

				List<Integer> excludedStatusIDsList = new ArrayList<Integer>();

				for (String excludedStatusIDString : excludedStatusIDs) {

					if (!stringIntegerValidator.validateFormat(excludedStatusIDString)) {

						validationErrors.add(new ValidationError("excludedStatusIDs-" + flowID, ValidationErrorType.InvalidFormat));
						continue;
					}

					excludedStatusIDsList.add(Integer.parseInt(excludedStatusIDString));
				}

				raffleFlow.setExcludedStatusIDs(excludedStatusIDsList);
			}

			raffleFlows.add(raffleFlow);
		}

		raffleRound.setRaffleFlows(raffleFlows);
	}

	@Override
	protected RaffleRound populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		RaffleRound raffleRound = super.populateFromAddRequest(req, user, uriParser);

		raffleRound.setModuleID(callback.getModuleID());

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		populateRaffleFlows(raffleRound, null, req, user, uriParser, validationErrors);

		if (!CollectionUtils.isEmpty(validationErrors)) {

			throw new ValidationException(validationErrors);
		}

		return raffleRound;
	}

	@Override
	protected RaffleRound populateFromUpdateRequest(RaffleRound oldRaffleRound, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		RaffleRound raffleRound = super.populateFromUpdateRequest(oldRaffleRound, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		populateRaffleFlows(raffleRound, oldRaffleRound, req, user, uriParser, validationErrors);

		if (!CollectionUtils.isEmpty(validationErrors)) {

			throw new ValidationException(validationErrors);
		}

		return raffleRound;
	}

	protected void appendFormData(RaffleRound bean, Document doc, Element typeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		String[] flowIDs = req.getParameterValues("flowID");

		if (!ArrayUtils.isEmpty(flowIDs)) {

			List<Integer> flowIDsList = new ArrayList<Integer>();

			for (String flowIDString : flowIDs) {

				if (!stringIntegerValidator.validateFormat(flowIDString)) {

					continue;
				}

				flowIDsList.add(Integer.parseInt(flowIDString));
			}

			List<Flow> flows = callback.getFlows(flowIDsList, Flow.STATUSES_RELATION);

			Element flowsElement = doc.createElement("Flows");
			
			for (Flow flow : flows) {
				
				filterFlowStatuses(flow);

				flowsElement.appendChild(flow.toXML(doc));
			}

			typeElement.appendChild(flowsElement);
		}
		
		XMLUtils.appendNewElement(doc, typeElement, "cssPath", callback.getCssPath());
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(null, doc, addTypeElement, user, req, uriParser);
		
		XMLUtils.appendNewElement(doc, addTypeElement, "defaultRaffleRoundDecisionEmailMessage", callback.getDefaultRaffleRoundDecisionEmailMessage());
		XMLUtils.appendNewElement(doc, addTypeElement, "defaultRaffleRoundDecisionSMSMessage", callback.getDefaultRaffleRoundDecisionSMSMessage());
	}

	@Override
	protected void appendUpdateFormData(RaffleRound bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(bean, doc, updateTypeElement, user, req, uriParser);
	}
	
	private void filterFlowStatuses(Flow flow){
		
		if (!CollectionUtils.isEmpty(flow.getStatuses())) {

			for (Iterator<Status> it = flow.getStatuses().iterator(); it.hasNext();) {
				Status status = it.next();

				if (!CollectionUtils.isEmpty(callback.getAutoExcludedStatusTypes()) && callback.getAutoExcludedStatusTypes().contains(status.getContentType())) {
					it.remove();
				}
			}
		}
	}

	@Override
	protected void appendBean(RaffleRound raffleRound, Element targetElement, Document doc, User user) {

		XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);

		genDoc.addElementableListener(RaffleFlow.class, new ElementableListener<RaffleFlow>() {

			@Override
			public void elementGenerated(Document doc, Element element, RaffleFlow raffleFlow) {

				try {
					Flow flow = callback.getFlow(raffleFlow.getFlowID(), Flow.STATUSES_RELATION);
					
					filterFlowStatuses(flow);

					element.appendChild(flow.toXML(doc));

				} catch (SQLException e) {

					log.error("", e);
				}
			}
		});

		Element element = raffleRound.toXML(genDoc);

		if (raffleRound.getAddFlowID() != null) {

			try {
				Flow flow = callback.getFlow(raffleRound.getAddFlowID());
				
				filterFlowStatuses(flow);

				element.appendChild(flow.toXML(doc));

			} catch (SQLException e) {

				log.error("", e);
			}
		}

		targetElement.appendChild(element);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if (!callback.isAdminForFund(user)) {

			throw new AccessDeniedException("Not admin for fund");
		}
	}

	@Override
	protected void checkUpdateAccess(RaffleRound round, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if (!callback.isAdminForFund(user)) {

			throw new AccessDeniedException("Not admin for fund");
		}
	}

	@Override
	protected void checkDeleteAccess(RaffleRound round, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if (!callback.isAdminForFund(user)) {

			throw new AccessDeniedException("Not admin for fund");
		}
	}

}
