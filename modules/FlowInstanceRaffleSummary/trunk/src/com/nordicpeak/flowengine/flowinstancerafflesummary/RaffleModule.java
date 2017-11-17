package com.nordicpeak.flowengine.flowinstancerafflesummary;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.UserMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleAccessInterface;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleSMS;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SMSSender;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.addflowinstance.AddFlowInstanceModule;
import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.events.StatusChangedByManagerEvent;
import com.nordicpeak.flowengine.flowinstancerafflesummary.beans.RaffleFlow;
import com.nordicpeak.flowengine.flowinstancerafflesummary.beans.RaffleRound;
import com.nordicpeak.flowengine.flowinstancerafflesummary.beans.RaffleRowInformation;
import com.nordicpeak.flowengine.flowinstancerafflesummary.cruds.RaffleRoundCRUD;
import com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors.FlowInstanceAlreadyRaffled;
import com.nordicpeak.flowengine.flowinstancerafflesummary.validationerrors.RaffleFlowNotConfigured;
import com.nordicpeak.flowengine.flowinstancesummary.FlowInstanceSummaryModule;
import com.nordicpeak.flowengine.flowinstancesummary.beans.InstanceSummaryOverviewEntry;
import com.nordicpeak.flowengine.flowinstancesummary.beans.table.AttributeExistsColumnDefinition;
import com.nordicpeak.flowengine.flowinstancesummary.beans.table.FlowInstanceIDColumnDefinition;
import com.nordicpeak.flowengine.flowinstancesummary.beans.table.FlowInstanceStatusColumnDefinition;
import com.nordicpeak.flowengine.flowinstancesummary.beans.table.NameColumnDefinition;
import com.nordicpeak.flowengine.flowinstancesummary.beans.table.RowInformation;
import com.nordicpeak.flowengine.flowinstancesummary.beans.table.SimpleAttributeColumnDefinition;
import com.nordicpeak.flowengine.flowinstancesummary.interfaces.ColumnDefinition;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.notifications.FlowFamililyNotificationSettings;
import com.nordicpeak.flowengine.notifications.StandardFlowNotificationHandler;

public class RaffleModule extends FlowInstanceSummaryModule implements CRUDCallback<User> {
	
	
	public static final ValidationError ROUND_NOT_FOUND_VALIDATION_ERROR = new ValidationError("RequestedRoundNotFound");
	public static final ValidationError ROUND_DOES_NOT_SUPPORT_ADD_VALIDATION_ERROR = new ValidationError("RoundDoesNotSupportAdd");
	
	private static final AnnotatedBeanTagSourceFactory<Flow> FLOW_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Flow>(Flow.class, "$flow.");
	private static final AnnotatedBeanTagSourceFactory<FlowInstance> FLOWINSTANCE_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<FlowInstance>(FlowInstance.class, "$flowInstance.");
	private static final AnnotatedBeanTagSourceFactory<Status> STATUS_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Status>(Status.class, "$status.");
	private static final AnnotatedBeanTagSourceFactory<Contact> CONTACT_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Contact>(Contact.class, "$contact.");
	
	public static final PositiveStringIntegerValidator positiveStringIntegerValidator = new PositiveStringIntegerValidator();
	
	private static final AddFlowInstanceCallback ADD_FLOW_INSTANCE_CALLBACK = new AddFlowInstanceCallback();
	
	public static final String numberAttribute = "raffleNumber";
	
	@XSLVariable(prefix = "java.")
	protected String cellValueYes = "Yes";
	
	@XSLVariable(prefix = "java.")
	protected String cellValueNo = "No";
	
	@XSLVariable(prefix = "java.")
	protected String numberAssignedMessage = "Raffled. Number assigned $number";
	
	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Admin groups", description = "Groups allowed to administrate fund")
	protected List<Integer> adminGroupIDs;
	
	@ModuleSetting(allowsNull = true)
	@UserMultiListSettingDescriptor(name = "Admin users", description = "Users allowed to administrate fund")
	protected List<Integer> adminUserIDs;
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Editor CSS", description = "Path to the desired CSS stylesheet for CKEditor (relative from the contextpath)")
	protected String cssPath;
	
	@ModuleSetting(allowsNull = true)
	@HTMLEditorSettingDescriptor(name = "Default Raffle Round Decision Email Message", description = "")
	protected String defaultRaffleRoundDecisionEmailMessage;
	
	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Default Raffle Round Decision SMS Message", description = "")
	protected String defaultRaffleRoundDecisionSMSMessage;
	
	protected SimpleAccessInterface adminAccessInterface;
	
	protected AnnotatedDAO<Status> statusDAO;
	protected AnnotatedDAO<FlowInstanceEvent> flowInstanceEventDAO;
	protected AnnotatedDAO<RaffleRound> raffleRoundDAO;
	protected AnnotatedDAO<RaffleFlow> raffleFlowDAO;
	
	protected QueryParameterFactory<Status, Integer> statusIDParamFactory;
	protected QueryParameterFactory<Status, Flow> statusFlowParamFactory;
	protected QueryParameterFactory<RaffleRound, Integer> raffleRoundIDParamFactory;
	protected QueryParameterFactory<RaffleRound, Integer> raffleRoundModuleIDParamFactory;
	protected QueryParameterFactory<RaffleFlow, Integer> raffleFlowFlowIDParamFactory;
	
	protected RaffleRoundCRUD raffleRoundCRUD;
	
	@InstanceManagerDependency
	protected AddFlowInstanceModule addFlowInstanceModule;
	
	@InstanceManagerDependency
	protected StandardFlowNotificationHandler standardFlowNotificationHandler;
	
	@InstanceManagerDependency
	protected SMSSender smsSender;
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		super.createDAOs(dataSource);
		
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, this.getClass().getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("dao/DB script.xml")));
		
		if (upgradeResult.isUpgrade()) {
			
			log.info(upgradeResult.toString());
		}
		
		statusDAO = daoFactory.getStatusDAO();
		statusIDParamFactory = statusDAO.getParamFactory("statusID", Integer.class);
		statusFlowParamFactory = statusDAO.getParamFactory("flow", Flow.class);
		
		HierarchyAnnotatedDAOFactory daoFactory2 = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), false, true, false);
		
		raffleRoundDAO = daoFactory2.getDAO(RaffleRound.class);
		raffleRoundIDParamFactory = raffleRoundDAO.getParamFactory("roundID", Integer.class);
		raffleRoundModuleIDParamFactory = raffleRoundDAO.getParamFactory("moduleID", Integer.class);
		
		raffleFlowDAO = daoFactory2.getDAO(RaffleFlow.class);
		raffleFlowFlowIDParamFactory = raffleFlowDAO.getParamFactory("flowID", Integer.class);
		
		AnnotatedDAOWrapper<RaffleRound, Integer> raffleRoundDAOWrapper = raffleRoundDAO.getWrapper(Integer.class);
		
		raffleRoundCRUD = new RaffleRoundCRUD(raffleRoundDAOWrapper, this);
	}
	
	@Override
	protected void moduleConfigured() throws Exception {
		
		super.moduleConfigured();
		
		ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.WARN);
		
		adminAccessInterface = new SimpleAccessInterface(adminGroupIDs, adminUserIDs);
	}
	
	@WebPublic
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		return list(req, res, user, uriParser, null);
	}
	
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Throwable {
		
		return listRounds(req, res, user, uriParser, getRounds(), validationErrors);
	}
	
	protected ForegroundModuleResponse listRounds(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<RaffleRound> rounds, List<ValidationError> validationErrors) throws Throwable {
		
		log.info("User " + user + " listing rounds");
		
		Document doc = createDocument(req, uriParser, user);
		Element listElement = doc.createElement("ListRounds");
		doc.getFirstChild().appendChild(listElement);
		
		if (!CollectionUtils.isEmpty(validationErrors)) {
			
			listElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			XMLUtils.append(doc, listElement, "ValidationErrors", validationErrors);
		}
		
		Element roundsElement = XMLUtils.appendNewElement(doc, listElement, "Rounds");
		boolean isAdminForFund = isAdminForFund(user);
		
		if (!CollectionUtils.isEmpty(rounds)) {
			
			roundsLoop: for (RaffleRound round : rounds) {
				
				Element roundElement = doc.createElement("Round");
				
				XMLUtils.appendNewElement(doc, roundElement, "Name", round.getName());
				XMLUtils.appendNewElement(doc, roundElement, "ID", round.getRoundID());
				XMLUtils.appendNewElement(doc, roundElement, "URL", systemInterface.getContextPath() + getFullAlias() + "/show/" + round.getRoundID());
				
				if (round.getStartDate() != null) {
					XMLUtils.appendNewElement(doc, roundElement, "Start", DateUtils.DATE_FORMATTER.format(round.getStartDate()));
				}
				
				if (round.getEndDate() != null) {
					XMLUtils.appendNewElement(doc, roundElement, "End", DateUtils.DATE_FORMATTER.format(round.getEndDate()));
				}
				
				int flowInstanceCount = 0;
				
				if (!CollectionUtils.isEmpty(round.getFlowIDs())) {
					
					List<Flow> flows = getFlows(round.getFlowIDs(), Flow.STATUSES_RELATION);
					
					if (!CollectionUtils.isEmpty(flows)) {
						
						for (Flow flow : flows) {
							
							if (!isAdminForFund && !AccessUtils.checkAccess(user, flow.getFlowFamily())) {
								continue roundsLoop;
							}
							
							flowInstanceCount += getFlowInstanceCount(flow, round.getStartDate(), round.getEndDate());
						}
					}
				}
				
				XMLUtils.appendNewElement(doc, roundElement, "FlowInstanceCount", flowInstanceCount);
				
				roundsElement.appendChild(roundElement);
			}
		}
		
		if (isAdminForFund) {
			
			XMLUtils.appendNewElement(doc, listElement, "RaffleAdmin");
		}
		
		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName());
	}
	
	@WebPublic
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		return show(req, res, user, uriParser, null);
	}
	
	public void checkRoundAccess(RaffleRound round, List<Flow> flows, User user) throws AccessDeniedException {
		
		if (!isAdminForFund(user)) {
			
			if (CollectionUtils.isEmpty(flows)) {
				
				throw new AccessDeniedException("Raffle round " + round + " is empty and user is not round admin");
				
			} else {
				
				for (Flow flow : flows) {
					
					if (!AccessUtils.checkAccess(user, flow.getFlowFamily())) {
						
						throw new AccessDeniedException("User does not have access to all flows in raffle round " + round);
					}
				}
			}
		}
	}
	
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Throwable {
		
		Integer roundID;
		RaffleRound round;
		
		if (uriParser.size() == 3 && (roundID = uriParser.getInt(2)) != null && (round = getRound(roundID)) != null) {
			
			log.info("User " + user + " showing summary of round " + round);
			
			if (validationErrors == null) {
				
				validationErrors = new ArrayList<ValidationError>();
			}
			
			List<Flow> flows = getFlows(round.getFlowIDs());
			
			checkRoundAccess(round, flows, user);
			
			List<FlowInstance> flowInstances = getFlowInstances(flows, round.getStartDate(), round.getEndDate(), FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.FLOW_RELATION);
			
			Document doc = createDocument(req, uriParser, user);
			Element summaryElement = showSummary(doc, req, res, user, uriParser, getColumns(), getRows(round, flowInstances), validationErrors);
			
			summaryElement.appendChild(round.toXML(doc));
			
			if (addFlowInstanceModule != null) {
				
				XMLUtils.appendNewElement(doc, summaryElement, "AddFlowInstanceModule");
				
				if (round.getAddFlowID() != null) {
					
					XMLUtils.appendNewElement(doc, summaryElement, "AddFlowInstance");
				}
			}
			
			if (isAdminForFund(user)) {
				
				XMLUtils.appendNewElement(doc, summaryElement, "RaffleAdmin");
			}
			
			return new SimpleForegroundModuleResponse(doc, round.getName());
		}
		
		return list(req, res, user, uriParser, Arrays.asList(ROUND_NOT_FOUND_VALIDATION_ERROR));
	}
	
	@WebPublic
	public ForegroundModuleResponse raffle(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Integer roundID;
		RaffleRound round;
		
		if (uriParser.size() == 3 && (roundID = uriParser.getInt(2)) != null && (round = getRound(roundID)) != null) {
			
			List<Flow> flows = getFlows(round.getFlowIDs());
			
			checkRoundAccess(round, flows, user);
			
			List<ValidationError> validationErrors = null;
			
			if (req.getMethod().equalsIgnoreCase("POST")) {
				
				validationErrors = new ArrayList<ValidationError>();
				
				log.info("User " + user + " raffling round " + round);
				
				List<FlowInstance> flowInstances = getFlowInstances(flows, round.getStartDate(), round.getEndDate(), FlowInstance.ATTRIBUTES_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION);
				
				if (!CollectionUtils.isEmpty(flowInstances)) {
					
					Map<FlowInstance, Status> oldStatusMap = new HashMap<FlowInstance, Status>();
					
					for (Iterator<FlowInstance> it = flowInstances.iterator(); it.hasNext();) {
						
						FlowInstance flowInstance = it.next();
						
						RaffleFlow foundRaffleFlow = null;
						
						for (RaffleFlow raffleFlow : round.getRaffleFlows()) {
							
							if (raffleFlow.getFlowID().equals(flowInstance.getFlow().getFlowID())) {
								
								foundRaffleFlow = raffleFlow;
								
								if (raffleFlow.getRaffledStatusID() == null) {
									
									validationErrors.add(new RaffleFlowNotConfigured(raffleFlow));
									it.remove();
									break;
								}
								
								if (isFlowInstanceExcluded(raffleFlow, flowInstance)) {
									
									it.remove();
									break;
								}
								
								Integer statusID = flowInstance.getStatus().getStatusID();
								
								if (statusID.equals(raffleFlow.getRaffledStatusID()) || flowInstance.getAttributeHandler().getInt(numberAttribute) != null) {
									
									validationErrors.add(new FlowInstanceAlreadyRaffled(flowInstance));
									it.remove();
									break;
								}
								
								oldStatusMap.put(flowInstance, flowInstance.getStatus());
								flowInstance.setStatus(getStatus(raffleFlow.getRaffledStatusID()));
								break;
							}
						}
						
						if (foundRaffleFlow == null) {
							
							validationErrors.add(new ValidationError("error"));
							log.error("Missing RaffleFlow for flowinstance " + flowInstance);
						}
					}
					
					if (validationErrors.isEmpty()) {
						
						if (!CollectionUtils.isEmpty(flowInstances)) {
							
							List<Integer> freeNumbers = new ArrayList<Integer>();
							
							for (int i = 0; i < flowInstances.size(); i++) {
								
								freeNumbers.add(i + 1);
							}
							
							for (FlowInstance flowInstance : flowInstances) {
								
								int number = freeNumbers.remove((int) (Math.random() * freeNumbers.size()));
								flowInstance.getAttributeHandler().setAttribute(numberAttribute, number);
							}
							
							HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FlowInstance.ATTRIBUTES_RELATION);
							
							flowInstanceDAO.update(flowInstances, query);
							
							Timestamp now = TimeUtils.getCurrentTimestamp();
							
							for (FlowInstance flowInstance : flowInstances) {
								
								boolean sendCustomNotification = (flowInstance.getPoster() != null || !CollectionUtils.isEmpty(flowInstance.getOwners())) && round.isOverrideStatusChangedNotification();
								
								FlowInstanceEvent flowInstanceStatusEvent = flowInstanceAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.STATUS_UPDATED, null, user, now);
								systemInterface.getEventHandler().sendEvent(FlowInstance.class, new StatusChangedByManagerEvent(flowInstance, flowInstanceStatusEvent, getCurrentSiteProfile(req, user, uriParser), oldStatusMap.get(flowInstance), user, sendCustomNotification), EventTarget.ALL);
								
								String details = numberAssignedMessage.replace("$number", flowInstance.getAttributeHandler().getString(numberAttribute));
								flowInstanceAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, details, user, now);
								
								if (sendCustomNotification) {
									
									FlowFamililyNotificationSettings notificationSettings = standardFlowNotificationHandler.getNotificationSettings(flowInstance.getFlow());
									
									if (notificationSettings.isSendStatusChangedUserEmail() || notificationSettings.isSendStatusChangedUserSMS()) {
										
										Collection<Contact> contacts = standardFlowNotificationHandler.getContactsFromDB(flowInstance);
										
										if (contacts != null) {
											
											for (Contact contact : contacts) {
												
												if (contact.getMobilePhone() != null && notificationSettings.isSendStatusChangedUserSMS()) {
													
													sendContactSMS(round, flowInstance, contact, round.getDecisionSMSMessage());
												}
												
												if (contact.getEmail() != null && notificationSettings.isSendStatusChangedUserEmail()) {
													
													sendContactEmail(round, flowInstance, contact, notificationSettings.getStatusChangedUserEmailSubject(), round.getDecisionEmailMessage());
												}
											}
										}
									}
								}
							}
							
							systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(FlowInstance.class, CRUDAction.UPDATE, flowInstances), EventTarget.ALL);
						}
						
					} else {
						
						return show(req, res, user, uriParser, validationErrors);
					}
				}
			}
			
			redirectToMethod(req, res, "/show/" + roundID);
			return null;
		}
		
		return list(req, res, user, uriParser, Arrays.asList(ROUND_NOT_FOUND_VALIDATION_ERROR));
	}
	
	@WebPublic
	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Integer roundID;
		RaffleRound round;
		
		if (uriParser.size() == 3 && (roundID = uriParser.getInt(2)) != null && (round = getRound(roundID)) != null && round.getAddFlowID() != null) {
			
			return addFlowInstanceModule.createAddFlowInstanceRequest(req, res, round.getAddFlowID(), user, systemInterface.getContextPath() + getFullAlias() + "/show/" + round.getRoundID(), new RequestMetadata(true), null, ADD_FLOW_INSTANCE_CALLBACK);
		}
		
		return show(req, res, user, uriParser, Arrays.asList(ROUND_DOES_NOT_SUPPORT_ADD_VALIDATION_ERROR));
	}
	
	private boolean isFlowInstanceExcluded(RaffleFlow raffleFlow, FlowInstance flowInstance) {
		
		if (!CollectionUtils.isEmpty(autoExcludedStatusTypes) && autoExcludedStatusTypes.contains(flowInstance.getStatus().getContentType())) {
			
			return true;
		}
		
		if (!CollectionUtils.isEmpty(raffleFlow.getExcludedStatusIDs()) && raffleFlow.getExcludedStatusIDs().contains(flowInstance.getStatus().getStatusID())) {
			
			return true;
		}
		
		return false;
	}
	
	public FlowInstanceEvent createFlowInstanceEvent(ImmutableFlowInstance flowInstance, EventType eventType, String details, User user, Timestamp timestamp) throws SQLException {
		
		FlowInstanceEvent flowInstanceEvent = new FlowInstanceEvent();
		flowInstanceEvent.setFlowInstance((FlowInstance) flowInstance);
		flowInstanceEvent.setEventType(eventType);
		flowInstanceEvent.setDetails(details);
		flowInstanceEvent.setPoster(user);
		flowInstanceEvent.setStatus(flowInstance.getStatus().getName());
		flowInstanceEvent.setStatusDescription(flowInstance.getStatus().getDescription());
		
		if (timestamp == null) {
			
			flowInstanceEvent.setAdded(TimeUtils.getCurrentTimestamp());
			
		} else {
			
			flowInstanceEvent.setAdded(timestamp);
		}
		
		return flowInstanceEvent;
	}
	
	protected List<ColumnDefinition<RowInformation>> getColumns() {
		
		List<ColumnDefinition<RowInformation>> columns = new ArrayList<ColumnDefinition<RowInformation>>();
		
		columns.add(new FlowInstanceIDColumnDefinition("Ärendenummer"));
		columns.add(new AttributeExistsColumnDefinition("Manuell", "manual", cellValueYes, cellValueNo));
		columns.add(new NameColumnDefinition("Sökandes namn"));
		columns.add(new SimpleAttributeColumnDefinition("Personnummer", "citizenIdentifier"));
		columns.add(new SimpleAttributeColumnDefinition("Mobiltelefon", "mobilePhone"));
		columns.add(new SimpleAttributeColumnDefinition("Nummer", numberAttribute).setRightAligned(true));
		columns.add(new FlowInstanceStatusColumnDefinition("Status"));
		
		return columns;
	}
	
	protected List<RowInformation> getRows(RaffleRound round, List<FlowInstance> flowInstances) {
		
		List<RowInformation> rows = new ArrayList<RowInformation>();
		
		String flowBaseURL = systemInterface.getContextPath() + flowInstanceAdminModule.getFullAlias() + "/overview/";
		
		if (!CollectionUtils.isEmpty(flowInstances)) {
			
			int i = 0;
			for (FlowInstance flowInstance : flowInstances) {
				
				RaffleFlow foundRaffleFlow = null;
				
				for (RaffleFlow raffleFlow : round.getRaffleFlows()) {
					
					if (raffleFlow.getFlowID().equals(flowInstance.getFlow().getFlowID())) {
						
						foundRaffleFlow = raffleFlow;
						break;
					}
				}
				
				rows.add(new RaffleRowInformation(foundRaffleFlow, autoExcludedStatusTypes, flowInstance, i++, flowBaseURL));
			}
		}
		
		return rows;
	}
	
	public RaffleRound getRound(Integer id) throws SQLException {
		
		HighLevelQuery<RaffleRound> query = new HighLevelQuery<RaffleRound>(raffleRoundIDParamFactory.getParameter(id));
		
		return raffleRoundDAO.get(query);
	}
	
	protected List<RaffleRound> getRounds() throws SQLException {
		
		HighLevelQuery<RaffleRound> query = new HighLevelQuery<RaffleRound>(raffleRoundModuleIDParamFactory.getParameter(moduleDescriptor.getModuleID()));
		
		return raffleRoundDAO.getAll(query);
	}
	
	protected Status getStatus(Integer statusID) throws SQLException {
		
		HighLevelQuery<Status> query = new HighLevelQuery<Status>(statusIDParamFactory.getParameter(statusID));
		
		return statusDAO.get(query);
	}
	
	protected List<Status> getStatuses(List<Integer> statusIDs) throws SQLException {
		
		if (CollectionUtils.isEmpty(statusIDs)) {
			
			return null;
		}
		
		HighLevelQuery<Status> query = new HighLevelQuery<Status>(statusIDParamFactory.getWhereInParameter(statusIDs));
		
		return statusDAO.getAll(query);
	}
	
	public List<Status> getFlowStatuses(Flow flow) throws SQLException {
		
		HighLevelQuery<Status> query = new HighLevelQuery<Status>(statusFlowParamFactory.getParameter(flow));
		
		return statusDAO.getAll(query);
	}
	
	public Flow getFlow(Integer flowID, Field... extraRelations) throws SQLException {
		
		HighLevelQuery<Flow> query = new HighLevelQuery<Flow>(flowIDParamFactory.getParameter(flowID));
		
		if (!ArrayUtils.isEmpty(extraRelations)) {
			
			query.addRelations(extraRelations);
		}
		
		return flowDAO.get(query);
	}
	
	@Override
	public List<InstanceSummaryOverviewEntry> getInstanceSummaryEntries(User user) {
		
		boolean isAdminForFund = isAdminForFund(user);
		
		try {
			List<RaffleRound> rounds = getRounds();
			
			int roundCount = 0;
			int flowInstanceCount = 0;
			
			if (!CollectionUtils.isEmpty(rounds)) {
				
				roundsLoop: for (RaffleRound round : rounds) {
					
					if (!CollectionUtils.isEmpty(round.getFlowIDs())) {
						
						List<Flow> flows = getFlows(round.getFlowIDs(), Flow.STATUSES_RELATION);
						
						if (!CollectionUtils.isEmpty(flows)) {
							
							if (!isAdminForFund) {
								
								for (Flow flow : flows) {
									
									if (!AccessUtils.checkAccess(user, flow.getFlowFamily())) {
										
										continue roundsLoop;
									}
								}
							}
							
							roundCount++;
							
							for (Flow flow : flows) {
								
								flowInstanceCount += getFlowInstanceCount(flow, round.getStartDate(), round.getEndDate());
							}
						}
					}
				}
			}
			
			if (roundCount > 0 || isAdminForFund) {
				
				String method = "/list";
				
				if (roundCount == 1 && !isAdminForFund) {
					
					method = "/show/" + rounds.get(0).getRoundID();
				}
				
				return Collections.singletonList(new InstanceSummaryOverviewEntry(moduleDescriptor.getName(), systemInterface.getContextPath() + getFullAlias() + method, flowInstanceCount, roundCount));
			}
			
		} catch (SQLException e) {
			
			log.error("Error getting rounds for user " + user, e);
		}
		
		return null;
	}
	
	public boolean isAdminForFund(User user) {
		
		return AccessUtils.checkAccess(user, adminAccessInterface);
	}
	
	@Override
	public String getTitlePrefix() {
		
		return this.moduleDescriptor.getName();
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addRound(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		return raffleRoundCRUD.add(req, res, user, uriParser);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateRound(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		return raffleRoundCRUD.update(req, res, user, uriParser);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse deleteRound(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		return raffleRoundCRUD.delete(req, res, user, uriParser);
	}
	
	public Integer getModuleID() {
		
		return moduleDescriptor.getModuleID();
	}
	
	protected static void sendEmptyJSONResponse(HttpServletResponse res) throws IOException {
		
		JsonObject jsonObject = new JsonObject(1);
		jsonObject.putField("hitCount", "0");
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}
	
	protected void sendJSONResponse(JsonArray jsonArray, HttpServletResponse res) throws IOException {
		
		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", Integer.toString(jsonArray.size()));
		jsonObject.putField("hits", jsonArray);
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}
	
	@WebPublic(alias = "flows")
	public ForegroundModuleResponse getFlows(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		String search = req.getParameter("q");
		
		if (StringUtils.isEmpty(search)) {
			
			sendEmptyJSONResponse(res);
			return null;
		}
		
		if (!systemInterface.getEncoding().equalsIgnoreCase("UTF-8")) {
			search = URLDecoder.decode(search, "UTF-8");
		}
		
		LowLevelQuery<Flow> query = new LowLevelQuery<Flow>("SELECT * FROM " + flowDAO.getTableName() + " WHERE name LIKE ? ORDER BY name LIMIT 20");
		
		query.addParameter("%" + search + "%");
		
		List<Flow> flows = flowDAO.getAll(query);
		
		log.info("User " + user + " searching for flows using query " + search + ", found " + CollectionUtils.getSize(flows) + " hits");
		
		if (flows == null) {
			
			sendEmptyJSONResponse(res);
			return null;
		}
		
		JsonArray jsonArray = new JsonArray();
		
		for (Flow flow : flows) {
			
			JsonObject instance = new JsonObject(4);
			instance.putField("ID", flow.getFlowID().toString());
			instance.putField("Name", flow.getName());
			instance.putField("Version", flow.getVersion().toString());
			
			JsonArray statuses = new JsonArray();
			
			List<Status> statuses2 = getFlowStatuses(flow);
			
			if (!CollectionUtils.isEmpty(statuses2)) {
				
				for (Status status : statuses2) {
					
					if (CollectionUtils.isEmpty(autoExcludedStatusTypes) || !autoExcludedStatusTypes.contains(status.getContentType())) {
						
						JsonObject status2 = new JsonObject(2);
						status2.putField("ID", status.getStatusID());
						status2.putField("Name", status.getName());
						
						statuses.addNode(status2);
					}
				}
			}
			
			instance.putField("Statuses", statuses);
			
			jsonArray.addNode(instance);
		}
		
		sendJSONResponse(jsonArray, res);
		
		return null;
	}
	
	public List<ContentType> getAutoExcludedStatusTypes() {
		
		return autoExcludedStatusTypes;
	}
	
	public String getCssPath() {
		
		return cssPath;
	}
	
	private TagReplacer getTagReplacer(RaffleRound raffleRound, FlowInstance flowInstance, Contact contact) {
		
		TagReplacer tagReplacer = new TagReplacer();
		
		tagReplacer.addTagSource(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource(flowInstance));
		tagReplacer.addTagSource(FLOW_TAG_SOURCE_FACTORY.getTagSource(flowInstance.getFlow()));
		tagReplacer.addTagSource(STATUS_TAG_SOURCE_FACTORY.getTagSource(flowInstance.getStatus()));
		tagReplacer.addTagSource(CONTACT_TAG_SOURCE_FACTORY.getTagSource(contact));
		tagReplacer.addTagSource(new SingleTagSource("$flowInstance.url", standardFlowNotificationHandler.getUserFlowInstanceModuleAlias(flowInstance) + "/overview/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID()));
		tagReplacer.addTagSource(new SingleTagSource("$number", flowInstance.getAttributeHandler().getString(numberAttribute)));
		
		return tagReplacer;
	}
	
	private void sendContactEmail(RaffleRound raffleRound, FlowInstance flowInstance, Contact contact, String subject, String message) {
		
		if (contact.getEmail() == null || subject == null || message == null) {
			
			return;
		}
		
		TagReplacer tagReplacer = getTagReplacer(raffleRound, flowInstance, contact);
		
		SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());
		
		try {
			email.addRecipient(contact.getEmail());
			email.setMessageContentType(SimpleEmail.HTML);
			email.setSenderName(standardFlowNotificationHandler.getEmailSenderName(flowInstance));
			email.setSenderAddress(standardFlowNotificationHandler.getEmailSenderAddress(flowInstance));
			email.setSubject(tagReplacer.replace(subject));
			email.setMessage(tagReplacer.replace(message));
			
			systemInterface.getEmailHandler().send(email);
			
		} catch (Exception e) {
			
			log.error("Error generating/sending email " + email, e);
		}
	}
	
	private void sendContactSMS(RaffleRound raffleRound, FlowInstance flowInstance, Contact contact, String message) {
		
		if (contact.getMobilePhone() == null || smsSender == null || message == null) {
			
			return;
		}
		
		TagReplacer tagReplacer = getTagReplacer(raffleRound, flowInstance, contact);
		
		SimpleSMS sms = new SimpleSMS();
		
		try {
			sms.setSenderName(standardFlowNotificationHandler.getSmsSenderName(flowInstance));
			sms.setMessage(tagReplacer.replace(message));
			sms.addRecipient(contact.getMobilePhone());
			
			smsSender.send(sms);
			
		} catch (Exception e) {
			
			log.error("Error generating/sending sms " + sms, e);
		}
	}
	
	public String getDefaultRaffleRoundDecisionEmailMessage() {
		
		return defaultRaffleRoundDecisionEmailMessage;
	}
	
	public String getDefaultRaffleRoundDecisionSMSMessage() {
		
		return defaultRaffleRoundDecisionSMSMessage;
	}
}
