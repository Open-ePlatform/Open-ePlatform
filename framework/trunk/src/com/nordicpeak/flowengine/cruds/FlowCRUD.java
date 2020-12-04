package com.nordicpeak.flowengine.cruds;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleViewFragment;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.AdvancedIntegerBasedCRUD;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.NonNegativeStringIntegerPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.serialization.SerializationUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.templates.TemplateUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.populators.annotated.RequestMapping;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.beans.Category;
import com.nordicpeak.flowengine.beans.DefaultStatusMapping;
import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowAction;
import com.nordicpeak.flowengine.beans.FlowAdminExtensionShowView;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowForm;
import com.nordicpeak.flowengine.beans.FlowOverviewAttribute;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryTypeDescriptor;
import com.nordicpeak.flowengine.beans.StandardStatus;
import com.nordicpeak.flowengine.beans.StandardStatusGroup;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.interfaces.FlowAdminExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowAdminFragmentExtensionViewProvider;
import com.nordicpeak.flowengine.interfaces.FlowAdminShowFlowExtensionLinkProvider;
import com.nordicpeak.flowengine.interfaces.FlowSubmitSurveyProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStep;
import com.nordicpeak.flowengine.interfaces.MultiSignQuery;
import com.nordicpeak.flowengine.interfaces.MultiSignQueryinstance;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.listeners.FlowFormElementableListener;
import com.nordicpeak.flowengine.validationerrors.FlowFamilyAliasCollisionValidationError;

public class FlowCRUD extends AdvancedIntegerBasedCRUD<Flow, FlowAdminModule> {

	private static AnnotatedRequestPopulator<FlowFamily> FLOW_FAMILY_POULATOR = new AnnotatedRequestPopulator<FlowFamily>(FlowFamily.class);

	static {

		List<RequestMapping> requestMappings = new ArrayList<RequestMapping>(FLOW_FAMILY_POULATOR.getRequestMappings());

		for (RequestMapping requestMapping : requestMappings) {

			if (requestMapping.getParamName().equals("group") || requestMapping.getParamName().equals("user")) {

				FLOW_FAMILY_POULATOR.getRequestMappings().remove(requestMapping);
			}
		}
	}
	
	protected final FlowFormElementableListener flowFormElementableListener;

	public FlowCRUD(CRUDDAO<Flow, Integer> crudDAO, FlowAdminModule callback) {

		super(Flow.class, crudDAO, new AnnotatedRequestPopulator<Flow>(Flow.class), "Flow", "flow", "", callback);

		flowFormElementableListener = new FlowFormElementableListener(callback);
		
		this.setRequirePostForDelete(true);
	}

	@Override
	public Flow getBean(Integer beanID, String getMode) throws SQLException, AccessDeniedException {

		Flow flow = callback.getCachedFlow(beanID);

		if (flow == null) {

			return null;
		}

		Blob icon = flow.getIcon();

		flow = SerializationUtils.cloneSerializable(flow);

		flow.setIcon(icon);

		return flow;
	}
	
	@Override
	protected void appendBean(Flow flow, Element targetElement, Document doc, User user) {
		
		XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);
		genDoc.addFieldElementableListener(FlowForm.class, flowFormElementableListener);
		
		TemplateUtils.setTemplatedFields(flow.getFlowFamily(), callback);
		
		super.appendBean(flow, targetElement, genDoc, user);
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendAddUpdateFormData(doc, addTypeElement, user);

		callback.appendUserFlowTypes(doc, addTypeElement, user);
		
		FlowFamily dummyFlowFamily = new FlowFamily();
		TemplateUtils.setTemplatedFields(dummyFlowFamily, callback);
		
		Flow dummyFlow = new Flow();
		dummyFlow.setFlowFamily(dummyFlowFamily);

		addTypeElement.appendChild(dummyFlow.toXML(doc));
		
		List<StandardStatusGroup> statusGroups = callback.getDAOFactory().getStandardStatusGroupDAO().getAll();
		XMLUtils.append(doc, addTypeElement, "StandardStatusGroups", statusGroups);
	}

	@Override
	protected void appendUpdateFormData(Flow flow, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendAddUpdateFormData(doc, updateTypeElement, user);
		
		XMLUtils.append(doc, updateTypeElement, "OverviewAttributes", flow.getOverviewAttributes());
	}

	private void appendAddUpdateFormData(Document doc, Element typeElement, User user) {

		if (callback.submitSurveyEnabled()) {
			XMLUtils.appendNewElement(doc, typeElement, "SubmitSurveyEnabled");
		}

		XMLUtils.appendNewElement(doc, typeElement, "ckConnectorModuleAlias", callback.getCkConnectorModuleAlias());
		XMLUtils.appendNewElement(doc, typeElement, "cssPath", callback.getCssPath());

		if (callback.allowSkipOverviewForFlowForms()) {

			XMLUtils.appendNewElement(doc, typeElement, "AllowSkipOverviewForFlowForms", "true");
		}

		if (!callback.hasPublishAccess(user)) {

			XMLUtils.appendNewElement(doc, typeElement, "LacksPublishAccess", "true");
		}
		
		XMLUtils.appendNewElement(doc, typeElement, "DefaultStatisticsMode", callback.getDefaultStatisticsMode());
		XMLUtils.appendNewElement(doc, typeElement, "RequiresTags", callback.requiresTags());
		
		if (callback.getMultiSigningHandler() != null && callback.getMultiSigningHandler().supportsSequentialSigning()) {

			XMLUtils.appendNewElement(doc, typeElement, "SupportsSequentialSigning");
		}
		
		if (callback.isBlockForeignIDs()) {
			
			XMLUtils.appendNewElement(doc, typeElement, "ForeignIDsBlocked");
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(Flow bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.ADD, bean), EventTarget.ALL);
		
		callback.addFlowFamilyEvent(callback.getEventFlowAddedMessage(), bean, user);

		callback.redirectToMethod(req, res, "/showflow/" + bean.getFlowID());

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(Flow bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.UPDATE, bean), EventTarget.ALL);
		
		callback.getEventHandler().sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(CRUDAction.UPDATE, bean.getFlowFamily()), EventTarget.ALL);
		
		callback.addFlowFamilyEvent(callback.getEventFlowUpdatedMessage(), bean, user);

		callback.redirectToMethod(req, res, "/showflow/" + bean.getFlowID());

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(Flow bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.getEventHandler().sendEvent(Flow.class, new CRUDEvent<Flow>(CRUDAction.DELETE, bean), EventTarget.ALL);

		if (req.getAttribute("flowFamilyDeleted") != null) {

			callback.getEventHandler().sendEvent(FlowFamily.class, new CRUDEvent<FlowFamily>(CRUDAction.DELETE, bean.getFlowFamily()), EventTarget.ALL);

			return super.beanDeleted(bean, req, res, user, uriParser);

		} else {
			
			callback.addFlowFamilyEvent(callback.getEventFlowDeletedMessage() + " \"" + bean.getName() + "\"", bean, user);

			Flow flow = callback.getLatestFlowVersion(bean.getFlowFamily());

			callback.redirectToMethod(req, res, "/showflow/" + flow.getFlowID());

			return null;
		}
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if (!callback.hasAnyFlowTypeAccess(user)) {

			throw new AccessDeniedException("Add flow access denied, user does not have access to any flow types.");
		}
	}

	@Override
	protected void checkUpdateAccess(Flow bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user, bean);
	}

	@Override
	protected void checkDeleteAccess(Flow bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user, bean);

		if ((bean.isPublished() && bean.isEnabled()) || bean.getFlowInstanceCount() > 0) {

			throw new AccessDeniedException("Delete flow access denied since the requested flow is published or has flow instances connected to it.");
		}
	}

	@Override
	protected void checkShowAccess(Flow bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(user, bean);
	}

	public void checkAccess(User user, Flow bean) throws AccessDeniedException {

		if (!callback.hasFlowAccess(user, bean)) {

			throw new AccessDeniedException("User does not have access to flow type " + bean.getFlowType());
		}
	}

	@Override
	protected void validateAddPopulation(Flow bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		if (bean.isInternal()) {

			bean.setEnabled(false);
		}

		if (!callback.hasPublishAccess(user)) {

			bean.setPublishDate(null);
			bean.setUnPublishDate(null);
		}

		Integer flowtypeID = NumberUtils.toInt(req.getParameter("flowTypeID"));

		FlowType flowType = null;

		if (flowtypeID == null) {

			throw new ValidationException(new ValidationError("flowTypeID", ValidationErrorType.RequiredField));

		} else if ((flowType = callback.getCachedFlowType(flowtypeID)) == null) {

			throw new ValidationException(new ValidationError("SelectedFlowTypeNotFound"));

		} else if (!AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())) {

			throw new ValidationException(new ValidationError("FlowTypeAccessDenied"));
		}

		FlowFamily flowFamily = FLOW_FAMILY_POULATOR.populate(req);

		flowFamily.setVersionCount(1);
		bean.setFlowFamily(flowFamily);

		bean.setFlowType(flowType);

		List<ValidationError> errors = new ArrayList<ValidationError>();

		validateFlowCategory(bean, req, errors);

		validateFlowSettings(bean);

		validateContactFields(bean.getFlowFamily(), errors);

		validateAliases(bean.getFlowFamily(), errors);
		
		validateTags(bean, errors);
		
		validateFlowOverviewAttributes(bean, req, errors);
		
		if (req.getParameter("addstandardstatuses") != null && bean.isInternal()) {
			
			Integer statusGroupID = ValidationUtils.validateParameter("statusGroupID", req, true, PositiveStringIntegerPopulator.getPopulator(), errors);
			
			if (statusGroupID != null) {
				
				StandardStatusGroup statusGroup = callback.getStatusGroup(statusGroupID, StandardStatusGroup.STANDARD_STATUSES_RELATION, StandardStatus.DEFAULT_STANDARD_STATUS_MAPPINGS_RELATION);
		
				if (statusGroup == null) {
			
					errors.add(new ValidationError("statusGroupID", ValidationErrorType.InvalidFormat));
					
				} else {
						
					callback.replaceFlowStatusesWithStandardStatuses(bean, statusGroup);
				}
			}
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

		bean.setVersion(1);

		if (bean.getExternalLink() != null) {

			bean.setUsePreview(false);
			bean.setRequireSigning(false);
			bean.setSubmittedMessage(null);

			return;
		}
		
		bean.setIconLastModified(TimeUtils.getCurrentTimestamp());
	}

	@Override
	protected Flow populateFromUpdateRequest(Flow bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		boolean enabled = bean.isEnabled();
		Date publish = bean.getPublishDate();
		Date unpublish = bean.getUnPublishDate();

		bean = super.populateFromUpdateRequest(bean, req, user, uriParser);

		// Revert user publish changes if they are not allowed to publish
		if (!callback.hasPublishAccess(user)) {

			bean.setEnabled(enabled);
			bean.setPublishDate(publish);
			bean.setUnPublishDate(unpublish);
		}

		return bean;
	}

	@Override
	protected void validateUpdatePopulation(Flow bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		FLOW_FAMILY_POULATOR.populate(bean.getFlowFamily(), req);
		
		List<ValidationError> errors = new ArrayList<ValidationError>();

		validateFlowCategory(bean, req, errors);
		
		validateFlowSettings(bean);

		validateContactFields(bean.getFlowFamily(), errors);

		validateAliases(bean.getFlowFamily(), errors);

		validateTags(bean, errors);
		
		validateFlowOverviewAttributes(bean, req, errors);
		
		if (bean.isInternal() && bean.isEnabled() && bean.isPublished()) {

			List<FlowAction> requiredFlowActions = callback.getFlowActions(false);

			if (requiredFlowActions != null) {

				if (bean.getDefaultFlowStateMappings() != null) {

					actionLoop: for (FlowAction flowAction : requiredFlowActions) {

						for (DefaultStatusMapping mapping : bean.getDefaultFlowStateMappings()) {

							if (flowAction.getActionID().equals(mapping.getActionID())) {

								continue actionLoop;
							}
						}

						log.info("Flow " + bean + " is missing status mapping for action " + flowAction);
						errors.add(new ValidationError("MissingDefaultStatusMapping"));

					}

				} else {

					log.info("Flow " + bean + " is missing status mappings for all required actions");
					errors.add(new ValidationError("MissingDefaultStatusMapping"));
				}

			}

			if (bean.requiresSigning() && requiresMultiSigning(bean.getFlowID()) && !hasMultiSignStatus(bean)) {

				errors.add(new ValidationError("MissingDefaultStatusMappingForMultiSigning"));
			}
			
			if (bean.requiresSigning() && bean.isPaymentSupportEnabled() && !hasPaymentStatus(bean)) {

				errors.add(new ValidationError("MissingDefaultStatusMappingForPayment"));
			}

			if(callback.isRequireManagers() && !bean.getFlowFamily().hasManagers()){
				
				errors.add(FlowAdminModule.NO_MANAGERS_VALIDATION_ERROR);
			}
		}

		if (bean.isEnabled() && !hasRequiredContent(bean)) {

			errors.add(FlowAdminModule.FLOW_HAS_NO_CONTENT_VALIDATION_ERROR);
		}

		if (bean.isEnabled() && bean.isInternal() && callback.allowSkipOverviewForFlowForms() && bean.skipOverview() && bean.getSteps() == null && CollectionUtils.isEmpty(bean.getFlowForms())) {

			errors.add(FlowAdminModule.FLOW_HAS_NO_STEPS_AND_SKIP_OVERVIEW_IS_SET_VALIDATION_ERROR);
		}

		if (!CollectionUtils.isEmpty(bean.getFlowForms()) && bean.skipOverview() && !callback.allowSkipOverviewForFlowForms()) {

			errors.add(FlowAdminModule.MAY_NOT_SET_SKIP_OVERVIEW_IF_FLOW_FORM_IS_SET_VALIDATION_ERROR);
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
	}
	
	private void validateFlowSettings(Flow flow) {
		
		if (flow.isHideFromUser()) {
			
			flow.setHideExternalMessages(true);
		}
	}

	private void validateTags(Flow bean, List<ValidationError> errors) {

		if(bean.getTags() != null){
		
			TreeSet<String> tagSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			tagSet.addAll(bean.getTags());
			bean.setTags(new ArrayList<String>(tagSet));
		}
		
		if(callback.requiresTags() && CollectionUtils.isEmpty(bean.getTags())){
			
			errors.add(new ValidationError("tags", ValidationErrorType.RequiredField));
		}
	}

	private void validateFlowOverviewAttributes(Flow flow, HttpServletRequest req, List<ValidationError> validationErrors) {

		String[] attributeIDs = req.getParameterValues("overviewAttributeID");

		if (attributeIDs != null) {

			List<FlowOverviewAttribute> overviewAttributes = new ArrayList<FlowOverviewAttribute>(attributeIDs.length);

			for (String alternativeID : attributeIDs) {

				String name = ValidationUtils.validateParameter("overviewAttributeName_" + alternativeID, req, true, 1, 50, validationErrors);
				String value = ValidationUtils.validateParameter("overviewAttributeValue_" + alternativeID, req, true, 1, 1024, validationErrors);
				Integer sortIndex = ValidationUtils.validateParameter("overviewAttributeSortIndex_" + alternativeID, req, true, NonNegativeStringIntegerPopulator.getPopulator(), validationErrors);

				if (name == null || value == null || sortIndex == null) {
					continue;
				}

				FlowOverviewAttribute alternative = new FlowOverviewAttribute();

				alternative.setName(name);
				alternative.setValue(value);
				alternative.setSortIndex(sortIndex);

				for(FlowOverviewAttribute existingAttribute : overviewAttributes) {
					
					if(existingAttribute.getName().equalsIgnoreCase(alternative.getName())) {
						
						validationErrors.add(new ValidationError("DuplicateOverviewAttributeNames", alternative.getName()));
					}
				}
				
				overviewAttributes.add(alternative);
			}
			
			flow.setOverviewAttributes(overviewAttributes);
			
		} else {
			
			flow.setOverviewAttributes(null);
		}
	}

	public boolean hasRequiredContent(Flow flow) {

		if (flow.getSteps() == null && CollectionUtils.isEmpty(flow.getFlowForms()) && flow.isInternal()) {

			return false;
		}

		return true;
	}

	protected boolean hasMultiSignStatus(Flow bean) {

		if (bean.getDefaultFlowStateMappings() != null) {

			for (DefaultStatusMapping statusMapping : bean.getDefaultFlowStateMappings()) {

				if (statusMapping.getActionID().equalsIgnoreCase(FlowBrowserModule.MULTI_SIGNING_ACTION_ID)) {

					return true;
				}
			}
		}

		return false;
	}
	
	protected boolean hasPaymentStatus(Flow bean) {

		if (bean.getDefaultFlowStateMappings() != null) {

			for (DefaultStatusMapping statusMapping : bean.getDefaultFlowStateMappings()) {

				if (statusMapping.getActionID().equalsIgnoreCase(FlowBrowserModule.PAYMENT_ACTION_ID)) {

					return true;
				}
			}
		}

		return false;
	}

	protected boolean requiresMultiSigning(Integer flowID) {

		ImmutableFlow flow = callback.getFlow(flowID);

		if (flow.getSteps() != null) {

			for (ImmutableStep step : flow.getSteps()) {

				if (step.getQueryDescriptors() != null) {

					for (ImmutableQueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

						QueryTypeDescriptor queryTypeDescriptor = callback.getQueryHandler().getQueryType(queryDescriptor.getQueryTypeID());

						if (queryTypeDescriptor != null && MultiSignQueryinstance.class.isAssignableFrom(queryTypeDescriptor.getQueryInstanceClass())) {

							try {
								Query query = callback.getQueryHandler().getQuery((QueryDescriptor)queryDescriptor, false);
								
								if(query != null && query instanceof MultiSignQuery && ((MultiSignQuery)query).requiresMultipartSigning()){
									
									return true;
								}
								
							} catch (Exception e) {
								
								log.error("Unable to get query " + queryDescriptor + " from query handler to check if multipart signing is required",e);
							}
						}
					}
				}
			}
		}

		return false;
	}

	private void validateContactFields(FlowFamily flowFamily, List<ValidationError> errors) {

		if ((!StringUtils.isEmpty(flowFamily.getContactEmail()) || !StringUtils.isEmpty(flowFamily.getContactPhone())) && StringUtils.isEmpty(flowFamily.getContactName())) {

			errors.add(new ValidationError("contactName", ValidationErrorType.RequiredField));
		}

		if (!StringUtils.isEmpty(flowFamily.getOwnerEmail()) && StringUtils.isEmpty(flowFamily.getOwnerName())) {

			errors.add(new ValidationError("ownerName", ValidationErrorType.RequiredField));
		}

	}

	private void validateAliases(FlowFamily flowFamily, List<ValidationError> errors) {

		if (!CollectionUtils.isEmpty(flowFamily.getAliases())) {

			TreeSet<String> tagSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			tagSet.addAll(flowFamily.getAliases());
			flowFamily.setAliases(new ArrayList<String>(tagSet));
			
			for (String alias : flowFamily.getAliases()) {

				FlowFamily existing = callback.getFlowFamilyByAlias(alias);

				if (existing != null && (flowFamily.getFlowFamilyID() == null || !flowFamily.getFlowFamilyID().equals(existing.getFlowFamilyID()))) {

					errors.add(new FlowFamilyAliasCollisionValidationError(alias, callback.getLatestFlowVersion(existing).getName()));
				}

				if (callback.checkAliasForSystemCollision(alias)) {

					errors.add(new ValidationError(alias, ValidationErrorType.Other, "FlowFamilyAliasAlreadyInUseBySystem"));
				}
			}
		}
	}

	private void validateFlowCategory(Flow flow, HttpServletRequest req, List<ValidationError> errors) throws ValidationException {

		Integer categoryID = NumberUtils.toInt(req.getParameter("categoryID"));

		if (categoryID != null) {

			List<Category> categories = flow.getFlowType().getCategories();

			if (categories != null) {

				Category choosenCategory = null;

				for (Category category : categories) {

					if (category.getCategoryID().equals(categoryID)) {
						choosenCategory = category;
					}

				}

				if (choosenCategory == null) {

					errors.add(new ValidationError("CategoryNotFound"));

					return;
				}

				flow.setCategory(choosenCategory);

			}

		} else {

			flow.setCategory(null);
		}
	}

	@Override
	protected void appendShowFormData(Flow flow, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {
		
		XMLUtils.append(doc, showTypeElement, "FlowVersions", callback.getFlowVersions(flow.getFlowFamily(), Order.DESC));
		
		XMLUtils.append(doc, showTypeElement, "EvaluatorTypes", callback.getEvaluationHandler().getAvailableEvaluatorTypes());
		
		XMLUtils.append(doc, showTypeElement, "QueryTypes", callback.getQueryHandler().getAvailableQueryTypes());
		
		flow.getFlowFamily().setManagerUsersAndGroups(callback.getUserHandler(), callback.getGroupHandler());
		XMLUtils.append(doc, showTypeElement, "ManagerGroups", flow.getFlowFamily().getManagerGroups());
		XMLUtils.append(doc, showTypeElement, "ManagerUsers", flow.getFlowFamily().getManagers());
		
		Document menuDoc = XMLUtils.createDomDocument();
		Element menuDocElement = menuDoc.createElement("Document");
		menuDoc.appendChild(menuDocElement);
		
		Element showFlowMenuElement = XMLUtils.appendNewElement(menuDoc, menuDocElement, "ShowFlowMenu");
		
		XMLUtils.appendNewElement(menuDoc, showFlowMenuElement, "TestFlowURI", req.getContextPath() + callback.getFullAlias() + "/overview/" + flow.getFlowID());
		XMLUtils.appendNewElement(menuDoc, showFlowMenuElement, "PreviewQueriesURI", req.getContextPath() + callback.getFullAlias() + "/testflowallsteps/" + flow.getFlowID());
		
		FlowSubmitSurveyProvider submitSurveyProvider = callback.getSystemInterface().getInstanceHandler().getInstance(FlowSubmitSurveyProvider.class);
		
		if (submitSurveyProvider != null) {
			
			XMLUtils.appendNewElement(doc, showTypeElement, "SubmitSurveyEnabled");
			
			ViewFragment viewFragment = submitSurveyProvider.getShowFlowSurveysFragment(flow.getFlowID());
			
			if (viewFragment != null) {
				
				req.setAttribute("ShowFlowSurveysFragment", viewFragment);
				
				XMLUtils.appendNewElement(doc, showTypeElement, "ShowFlowSurveysHTML", viewFragment.getHTML());
				XMLUtils.appendNewElement(menuDoc, showFlowMenuElement, "ShowFlowSurveys", true);
			}
		}
		
		if (callback.getNotificationHandler() != null) {

			try {
			
				ViewFragment viewFragment = callback.getNotificationHandler().getCurrentSettingsView(flow, req, user, uriParser);

				if (viewFragment != null) {

					req.setAttribute("NotificationsFragment", viewFragment);

					Element notificationsElement = viewFragment.toXML(doc);
					showTypeElement.appendChild(notificationsElement);
					doc.renameNode(notificationsElement, "", "Notifications");

					XMLUtils.appendNewElement(menuDoc, showFlowMenuElement, "ShowNotifications", true);
				}

			} catch (Exception e) {

				log.error("Error getting view fragment from notification handler for flow " + flow, e);
			}
		}
		
		if(flow.getExternalLink() == null) {
			XMLUtils.appendNewElement(menuDoc, showFlowMenuElement, "IsInternal", true);
		}
		
		List<FlowAdminExtensionViewProvider> extensionProviders = callback.getExtensionViewProviders();
		
		if (extensionProviders != null) {
			
			List<FlowAdminExtensionShowView> showViews = new ArrayList<FlowAdminExtensionShowView>(extensionProviders.size());
			
			for (FlowAdminExtensionViewProvider extensionProvider : extensionProviders) {
				
				try {
					FlowAdminExtensionShowView showView = extensionProvider.getShowView(flow, req, user, uriParser);
					
					if (showView != null && showView.getViewFragment() != null) {

						ViewFragment viewFragment = showView.getViewFragment();
						
						Element extensionProviderElement = XMLUtils.appendNewElement(doc, showTypeElement, "ExtensionProvider");
						XMLUtils.appendNewElement(doc, extensionProviderElement, "HTML", viewFragment.getHTML());
						XMLUtils.appendNewElement(doc, extensionProviderElement, "Title", extensionProvider.getExtensionViewTitle());
						XMLUtils.appendNewElement(doc, extensionProviderElement, "ID", extensionProvider.getModuleID());
						XMLUtils.appendNewElement(doc, extensionProviderElement, "Enabled", showView.isEnabled());
						
						if(showView.isEnabled()) {
							
							Element element = XMLUtils.appendNewElement(menuDoc, showFlowMenuElement, "ExtensionProvider");
							XMLUtils.appendNewElement(menuDoc, element, "Title", extensionProvider.getExtensionViewTitle());
							XMLUtils.appendNewElement(menuDoc, element, "ID", extensionProvider.getModuleID());
						}
						
						showViews.add(showView);
						
						if (viewFragment instanceof SimpleViewFragment) {
							
							SimpleViewFragment simpleViewFragment = (SimpleViewFragment) viewFragment;
							
							if (simpleViewFragment.getDebugXML() != null) {
								
								Element debugXMLElement = XMLUtils.appendNewElement(doc, extensionProviderElement, "DebugXML");
								debugXMLElement.appendChild(doc.adoptNode(simpleViewFragment.getDebugXML().getDocumentElement()));
							}
						}
					}
					
				} catch (Exception e) {
					
					log.error("Error while getting show view fragment for extension provider " + extensionProvider, e);
				}
			}
			
			if (!showViews.isEmpty()) {
				
				req.setAttribute("ExtensionProviderShowViews", showViews);
			}
		}
		
		List<FlowAdminFragmentExtensionViewProvider> fragmentExtensionProviders = callback.getFragmentExtensionViewProviders();
		
		if (fragmentExtensionProviders != null) {
			
			Map<FlowAdminFragmentExtensionViewProvider, FlowAdminExtensionShowView> showViews = new HashMap<>(fragmentExtensionProviders.size());
			
			for (FlowAdminFragmentExtensionViewProvider fragmentExtensionProvider : fragmentExtensionProviders) {
				
				try {
					FlowAdminExtensionShowView showView = fragmentExtensionProvider.getShowView(callback.getFragmentExtensionViewProviderURL(fragmentExtensionProvider, flow), flow, req, user, uriParser);
					
					if (showView != null && showView.getViewFragment() != null) {
						
						ViewFragment viewFragment = showView.getViewFragment();
						
						Element extensionProviderElement = XMLUtils.appendNewElement(doc, showTypeElement, "ExtensionProvider");
						XMLUtils.appendNewElement(doc, extensionProviderElement, "HTML", viewFragment.getHTML());
						XMLUtils.appendNewElement(doc, extensionProviderElement, "Title", fragmentExtensionProvider.getExtensionViewTitle());
						XMLUtils.appendNewElement(doc, extensionProviderElement, "ID", fragmentExtensionProvider.getModuleID());
						XMLUtils.appendNewElement(doc, extensionProviderElement, "Enabled", showView.isEnabled());
						
						if(showView.isEnabled()) {
							
							Element element = XMLUtils.appendNewElement(menuDoc, showFlowMenuElement, "ExtensionProvider");
							XMLUtils.appendNewElement(menuDoc, element, "Title", fragmentExtensionProvider.getExtensionViewTitle());
							XMLUtils.appendNewElement(menuDoc, element, "ID", fragmentExtensionProvider.getModuleID());
						}
						
						showViews.put(fragmentExtensionProvider, showView);
						
						if (viewFragment instanceof SimpleViewFragment) {
							
							SimpleViewFragment simpleViewFragment = (SimpleViewFragment) viewFragment;
							
							if (simpleViewFragment.getDebugXML() != null) {
								
								Element debugXMLElement = XMLUtils.appendNewElement(doc, extensionProviderElement, "DebugXML");
								debugXMLElement.appendChild(doc.adoptNode(simpleViewFragment.getDebugXML().getDocumentElement()));
							}
						}
					}
					
				} catch (Exception e) {
					
					log.error("Error while getting show view fragment for fragment extension provider " + fragmentExtensionProvider, e);
				}
			}
			
			if (!showViews.isEmpty()) {
				
				req.setAttribute("FragmentExtensionProviderShowViews", showViews);
			}
		}
		
		req.setAttribute("FlowMenuDocument", menuDoc);
		
		List<FlowAdminShowFlowExtensionLinkProvider> showExtensionLinkProviders = callback.getFlowShowExtensionLinkProviders();
		
		if (!CollectionUtils.isEmpty(showExtensionLinkProviders)) {
			
			List<ExtensionLink> extensionLinks = new ArrayList<ExtensionLink>(showExtensionLinkProviders.size());
			
			for (FlowAdminShowFlowExtensionLinkProvider linkProvider : showExtensionLinkProviders) {
				
				if (linkProvider.getAccessInterface() == null || AccessUtils.checkAccess(user, linkProvider.getAccessInterface())) {
					
					try {
						ExtensionLink link = linkProvider.getShowFlowExtensionLink(user);
						
						if (link != null) {
							
							extensionLinks.add(link);
						}
						
					} catch (Exception e) {
						
						log.error("Error getting extension link from provider " + linkProvider, e);
					}
				}
			}
			
			XMLUtils.append(doc, showTypeElement, extensionLinks);
		}
		
		if (flow.isEnabled() && flow.getSteps() == null && !CollectionUtils.isEmpty(flow.getFlowForms()) && flow.isInternal()) {
			
			XMLUtils.appendNewElement(doc, showTypeElement, "MayNotRemoveFlowFormIfNoSteps");
		}
		
		if (callback.allowSkipOverviewForFlowForms()) {
			
			XMLUtils.appendNewElement(doc, showTypeElement, "AllowSkipOverviewForFlowForms", "true");
		}
		
		if (callback.hasPublishAccess(user)) {
			
			XMLUtils.appendNewElement(doc, showTypeElement, "PublishAccess", "true");
		}
		
		if (flow.getFlowFamily().usesAutoManagerAssignment()) {
			
			XMLUtils.appendNewElement(doc, showTypeElement, "UsesAutoManagerAssignment");
		}
		
		XMLUtils.append(doc, showTypeElement, "FlowFamilyEvents", callback.getRecentFlowFamilyEvents(flow.getFlowFamily()));
		
		if (callback.getMultiSigningHandler() != null && callback.getMultiSigningHandler().supportsSequentialSigning()) {

			XMLUtils.appendNewElement(doc, showTypeElement, "SupportsSequentialSigning");
		}
		
		if (callback.isBlockForeignIDs()) {
			
			XMLUtils.appendNewElement(doc, showTypeElement, "ForeignIDsBlocked");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected SimpleForegroundModuleResponse createShowBeanModuleResponse(Flow flow, Document doc, HttpServletRequest req, User user, URIParser uriParser) {

		SimpleForegroundModuleResponse moduleResponse = super.createShowBeanModuleResponse(flow, doc, req, user, uriParser);

		ViewFragment surveysFragment = (ViewFragment) req.getAttribute("ShowFlowSurveysFragment");

		if (surveysFragment != null) {

			ViewFragmentUtils.appendLinksAndScripts(moduleResponse, surveysFragment);
		}
		
		ViewFragment notificationsFragment = (ViewFragment) req.getAttribute("NotificationsFragment");

		if (notificationsFragment != null) {

			ViewFragmentUtils.appendLinksAndScripts(moduleResponse, notificationsFragment);
		}

		List<FlowAdminExtensionShowView> extensionShowViews = (List<FlowAdminExtensionShowView>) req.getAttribute("ExtensionProviderShowViews");

		if (extensionShowViews != null) {

			for (FlowAdminExtensionShowView showView : extensionShowViews) {

				ViewFragmentUtils.appendLinksAndScripts(moduleResponse, showView.getViewFragment());
			}
		}
		
		Map<FlowAdminFragmentExtensionViewProvider, FlowAdminExtensionShowView> fragmentExtensionViewShowViews = (Map<FlowAdminFragmentExtensionViewProvider, FlowAdminExtensionShowView>) req.getAttribute("FragmentExtensionProviderShowViews");

		if (fragmentExtensionViewShowViews != null) {

			for (Entry<FlowAdminFragmentExtensionViewProvider, FlowAdminExtensionShowView> entry : fragmentExtensionViewShowViews.entrySet()) {

				FlowAdminFragmentExtensionViewProvider fragmentExtensionProvider = entry.getKey();
				FlowAdminExtensionShowView showView = entry.getValue();
				
				String extensionRequestURL = callback.getFragmentExtensionViewProviderURL(fragmentExtensionProvider, flow);
				
				ViewFragmentUtils.appendLinksAndScripts(moduleResponse, showView.getViewFragment(), extensionRequestURL);
			}
		}

		Document menuDoc = (Document) req.getAttribute("FlowMenuDocument");

		if(menuDoc != null) {
			
			SimpleBackgroundModuleResponse backgroundModuleResponse = new SimpleBackgroundModuleResponse(menuDoc, callback.getModuleTransformer());
			backgroundModuleResponse.setSlots(Collections.singletonList("left-content-container.flowmenu"));

			moduleResponse.addBackgroundModuleResponse(backgroundModuleResponse);
			
		}
		
		return moduleResponse;
	}

	@Override
	public ForegroundModuleResponse showBean(Flow bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		this.checkShowAccess(bean, user, req, uriParser);

		log.info("User " + user + " viewing " + this.typeLogName + " " + bean);

		Document doc = this.callback.createDocument(req, uriParser, user);
		Element showTypeElement = doc.createElement("Show" + typeElementName);
		doc.getFirstChild().appendChild(showTypeElement);

		this.appendBean(bean, showTypeElement, doc, user);

		this.appendShowFormData(bean, doc, showTypeElement, user, req, res, uriParser);

		if (res.isCommitted()) {

			return null;
		}

		if (validationErrors != null) {
			XMLUtils.append(doc, showTypeElement, validationErrors);
			showTypeElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		SimpleForegroundModuleResponse moduleResponse = createShowBeanModuleResponse(bean, doc, req, user, uriParser);

		List<Breadcrumb> breadcrumbs = getShowBreadcrumbs(bean, req, user, uriParser);

		if (breadcrumbs != null) {

			moduleResponse.addBreadcrumbsLast(breadcrumbs);
		}

		return moduleResponse;
	}

	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		return callback.list(req, res, user, uriParser, validationErrors);
	}

	@Override
	protected void deleteBean(Flow bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = callback.getDAOFactory().getFlowDAO().createTransaction();

			callback.getDAOFactory().getFlowDAO().delete(bean, transactionHandler);

			if (bean.getSteps() != null) {

				for (Step step : bean.getSteps()) {

					if (step.getQueryDescriptors() != null) {

						for (QueryDescriptor queryDescriptor : step.getQueryDescriptors()) {

							if (queryDescriptor.getEvaluatorDescriptors() != null) {

								for (EvaluatorDescriptor evaluatorDescriptor : queryDescriptor.getEvaluatorDescriptors()) {

									callback.getEvaluationHandler().deleteEvaluator(evaluatorDescriptor, transactionHandler);
								}
							}

							callback.getQueryHandler().deleteQuery(queryDescriptor, transactionHandler);
						}
					}
				}
			}

			//Check if the flow family has any more flow, else delete the family too
			if (!callback.hasFlows(bean.getFlowFamily(), transactionHandler)) {

				callback.getDAOFactory().getFlowFamilyDAO().delete(bean.getFlowFamily(), transactionHandler);

				req.setAttribute("flowFamilyDeleted", true);
			}

			transactionHandler.commit();

		} finally {

			TransactionHandler.autoClose(transactionHandler);
		}
	}
	
	
}
