package com.nordicpeak.flowengine.multisigninghandlers;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.exceptions.UnableToUpdateUserException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.core.utils.ViewFragmentUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.standardutils.bool.BooleanUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.ReverseListIterator;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.datatypes.SimpleEntry;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;

import com.nordicpeak.flowengine.BaseFlowModule;
import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.OperatingMessageModule;
import com.nordicpeak.flowengine.UserFlowInstanceModule;
import com.nordicpeak.flowengine.beans.DefaultInstanceMetadata;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.events.MultiSigningCanceledEvent;
import com.nordicpeak.flowengine.exceptions.evaluation.EvaluationException;
import com.nordicpeak.flowengine.exceptions.evaluationprovider.EvaluationProviderException;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flow.FlowDisabledException;
import com.nordicpeak.flowengine.exceptions.flowinstance.InvalidFlowInstanceStepException;
import com.nordicpeak.flowengine.exceptions.flowinstance.MissingQueryInstanceDescriptor;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.DuplicateFlowInstanceManagerIDException;
import com.nordicpeak.flowengine.exceptions.queryinstance.QueryRequestException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryInstanceNotFoundInQueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderErrorException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderException;
import com.nordicpeak.flowengine.exceptions.queryprovider.QueryProviderNotFoundException;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowEngineInterface;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.ListFlowInstancesExtensionProvider;
import com.nordicpeak.flowengine.interfaces.MultiSigningCallback;
import com.nordicpeak.flowengine.interfaces.MultiSigningHandler;
import com.nordicpeak.flowengine.interfaces.MultiSigningQueryProvider;
import com.nordicpeak.flowengine.interfaces.OperatingStatus;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.SigningProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.ManagerResponse;
import com.nordicpeak.flowengine.utils.CitizenIdentifierUtils;
import com.nordicpeak.flowengine.utils.MultiSignUtils;
import com.nordicpeak.flowengine.utils.SigningUtils;

public class MultiSigningHandlerModule extends AnnotatedForegroundModule implements MultiSigningHandler, ViewFragmentModule<ForegroundModuleDescriptor>, MultiSigningCallback, ListFlowInstancesExtensionProvider, ImmutableFlowEngineInterface, FlowInstanceAccessController {
	
	public static final String CITIZEN_IDENTIFIER = "citizenIdentifier";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "The server URL", description = "The URL to this server including protocol")
	protected String serverURL;
	
	@XSLVariable(prefix = "java.")
	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Sign message", description = "The message displayed above the sign form (available tags: $poster, $flowInstanceID, $flow.name)", required = true)
	protected String signMessage = "Not set";
	
	@XSLVariable(prefix = "java.")
	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Signed message", description = "The message displayed above the signed form (available tags: $poster, $flowInstanceID, $flow.name, $signed)", required = true)
	protected String signedMessage = "Not set";
	
	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Flow types with hidden citizen indentifier", description = "Citizen identifier will not be displayed in the sign form for these flow types.", formatValidator = NonNegativeStringIntegerValidator.class)
	protected List<Integer> flowTypesWithHiddenCitizenIdentifier;
	
	protected ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	
	protected AnnotatedDAO<Signature> signatureDAO;
	
	protected QueryParameterFactory<Signature, Integer> flowInstanceIDParamFactory;
	protected QueryParameterFactory<Signature, String> socialSecurityNumberParamFactory;
	protected QueryParameterFactory<Signature, Timestamp> addedParamFactory;
	
	@InstanceManagerDependency
	protected PDFProvider pdfProvider;
	
	@InstanceManagerDependency(required = true)
	protected QueryHandler queryHandler;
	
	@InstanceManagerDependency(required = true)
	protected FlowBrowserModule browserModule;
	
	@InstanceManagerDependency(required = true)
	protected SigningProvider signingProvider;
	
	@InstanceManagerDependency
	protected SiteProfileHandler profileHandler;
	
	@InstanceManagerDependency
	protected OperatingMessageModule operatingMessageModule;
	
	protected UserFlowInstanceModule userFlowInstanceModule;
	
	protected AnnotatedDAO<FlowInstance> flowInstanceDAO;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getForegroundModuleXSLTCache(), this, systemInterface.getEncoding());
		
		if (!systemInterface.getInstanceHandler().addInstance(MultiSigningHandler.class, this)) {
			
			throw new RuntimeException("Unable to register module " + this.moduleDescriptor + " in global instance handler using key " + MultiSigningHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}
	
	@Override
	public void unload() throws Exception {
		
		systemInterface.getInstanceHandler().removeInstance(MultiSigningHandler.class, this);
		
		if (userFlowInstanceModule != null) {
			
			userFlowInstanceModule.removeListFlowInstancesExtensionProvider(this);
		}
		
		super.unload();
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, MultiSigningHandlerModule.class.getName(), new XMLDBScriptProvider(MultiSigningHandlerModule.class.getResourceAsStream("DB script.xml")));
		
		if (upgradeResult.isUpgrade()) {
			
			log.info(upgradeResult.toString());
		}
		
		signatureDAO = new SimpleAnnotatedDAOFactory(dataSource).getDAO(Signature.class);
		
		flowInstanceIDParamFactory = signatureDAO.getParamFactory("flowInstanceID", Integer.class);
		socialSecurityNumberParamFactory = signatureDAO.getParamFactory("socialSecurityNumber", String.class);
		addedParamFactory = signatureDAO.getParamFactory("added", Timestamp.class);
		
		FlowEngineDAOFactory daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		flowInstanceDAO = daoFactory.getFlowInstanceDAO();
	}
	
	@Override
	public ViewFragment getSigningStatusViewFragment(HttpServletRequest req, User user, URIParser uriParser, ImmutableFlowInstanceManager instanceManager) throws Exception {
		
		//TODO any checks necessary here?
		
		Set<SigningParty> signingParties = MultiSignUtils.getSigningParties(instanceManager);
		
		if (signingParties == null) {
			
			throw new RuntimeException("No signing parties found for flow instance " + instanceManager);
		}
		
		Document doc = createDocument(req, uriParser);
		
		Element signingStatusElement = doc.createElement("SigningStatus");
		doc.getDocumentElement().appendChild(signingStatusElement);
		
		XMLUtils.append(doc, signingStatusElement, instanceManager.getFlowInstance());
		
		XMLUtils.appendNewElement(doc, signingStatusElement, "SigningLink", RequestUtils.getFullContextPathURL(req) + getFullAlias() + "/sign/" + instanceManager.getFlowInstanceID());
		
		for (SigningParty signingParty : signingParties) {
			
			Element signingPartyElement = XMLUtils.append(doc, signingStatusElement, signingParty);
			
			XMLUtils.append(doc, signingPartyElement, getValidSignatureForCurrentSigningChain(instanceManager, signingParty));
		}
		
		return viewFragmentTransformer.createViewFragment(doc);
	}
	
	@WebPublic(alias = "sign", requireLogin = true)
	public ForegroundModuleResponse signFlowInstance(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Integer flowInstanceID = uriParser.getInt(2);
		ImmutableFlowInstanceManager instanceManager;
		Document doc = createDocument(req, uriParser);
		
		try {
			if (flowInstanceID == null || (instanceManager = getFlowInstanceManager(flowInstanceID, req, user, uriParser)) == null) {
				
				//Flow instance not found
				throw new URINotFoundException(uriParser);
			}
			
		} catch (FlowDisabledException e) {
			
			return showMessage(doc, "FlowDisabled");
		}
		
		Signature signature = getValidSignatureForCurrentSigningChain(instanceManager, user);
		
		if (signature == null) {
			
			SigningParty signingParty = getMatchingSigningParty(user, instanceManager);
			
			if (signingParty == null) {
				
				//TODO if we have signed this flow instance previously show that event
				
				//User does not have access to sign this flow instance
				log.info("User " + user + " not have access to flow instance " + instanceManager + ", no matching signing party found");
				return showMessage(doc, "SigningPartyNotFound");
			}
			
			if (instanceManager.getFlowState().getContentType() != ContentType.WAITING_FOR_MULTISIGN) {
				
				//User does not have access to sign this flow instance
				log.info("User does not have access to flow instance " + instanceManager + ", wrong status content type: " + instanceManager.getFlowState().getContentType());
				return showMessage(doc, "WrongStatusContentType");
			}
			
			if (BooleanUtils.toBoolean(req.getParameter("sign"))) {
				
				log.info("User " + user + " signing flow instance " + instanceManager);
				
				req.setAttribute(this.getClass().getName() + ".siteProfile", getCurrentSiteProfile(req, user, uriParser));
				
				ViewFragment fragment = signingProvider.sign(req, res, user, instanceManager, this, signingParty);
				
				if (res.isCommitted()) {
					
					return null;
				}
				
				Element signFragmentElement = doc.createElement("SignFragment");
				doc.getDocumentElement().appendChild(signFragmentElement);
				
				signFragmentElement.appendChild(fragment.toXML(doc));
				
				SimpleForegroundModuleResponse moduleResponse = new SimpleForegroundModuleResponse(doc, getDefaultBreadcrumb());
				
				ViewFragmentUtils.appendLinksAndScripts(moduleResponse, fragment);
				
				return moduleResponse;
			}
		}
		
		Element signFlowInstanceElement = doc.createElement("SignFlowInstance");
		doc.getDocumentElement().appendChild(signFlowInstanceElement);
		
		if (signature == null) {
			
			XMLUtils.appendNewCDATAElement(doc, signFlowInstanceElement, "Message", getMessage(user, instanceManager, signMessage, null));
			
		} else {
			
			XMLUtils.appendNewCDATAElement(doc, signFlowInstanceElement, "Message", getMessage(user, instanceManager, signedMessage, signature));
			
			XMLUtils.append(doc, signFlowInstanceElement, signature);
		}
		
		FlowInstance flowInstance = userFlowInstanceModule.getFlowInstance(flowInstanceID);

		if (flowInstance != null) {

			signFlowInstanceElement.appendChild(flowInstance.toXML(doc));

			List<ManagerResponse> managerResponses = instanceManager.getFullShowHTML(req, user, this, true, null, getImmutableQueryRequestBaseURL(req, instanceManager), new RequestMetadata(false));
			XMLUtils.append(doc, signFlowInstanceElement, "ManagerResponses", managerResponses);
		}
		
		signFlowInstanceElement.appendChild(instanceManager.getFlowInstance().toXML(doc));
		
		return new SimpleForegroundModuleResponse(doc, getDefaultBreadcrumb());
	}
	
	private Signature getValidSignatureForCurrentSigningChain(ImmutableFlowInstanceManager instanceManager, User user) throws SQLException {
		
		String citizenIdentifier = CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(user);
		
		if (citizenIdentifier == null) {
			
			log.warn("Unable to find " + CITIZEN_IDENTIFIER + " identifier attribute for user " + user + " requesting flow instance " + instanceManager);
			return null;
		}
		
		return getValidSignatureForCurrentSigningChain(instanceManager, citizenIdentifier);
	}
	
	private Signature getValidSignatureForCurrentSigningChain(ImmutableFlowInstanceManager instanceManager, SigningParty signingParty) throws SQLException {
		
		return getValidSignatureForCurrentSigningChain(instanceManager, signingParty.getSocialSecurityNumber());
	}
	
	private Signature getValidSignatureForCurrentSigningChain(ImmutableFlowInstanceManager instanceManager, String citizenIdentifier) throws SQLException {
		
		if (citizenIdentifier == null) {
			
			throw new NullPointerException("citizenIdentifier can not be null for flow instance " + instanceManager);
		}
		
		ImmutableFlowInstanceEvent signingChainStartEvent = SigningUtils.getLastPosterSignEvents(instanceManager.getFlowInstance());
		
		if (signingChainStartEvent == null) {
			return null;
		}
		
		Signature signature = getSignature(instanceManager.getFlowInstanceID(), citizenIdentifier, signingChainStartEvent.getAdded());
		
		if (signature != null && signature.getEventID() > signingChainStartEvent.getEventID()) {
			
			return signature;
		}
		
		return null;
	}
	
	private String getMessage(User user, ImmutableFlowInstanceManager instanceManager, String messageTemplate, Signature signature) {
		
		StringBuilder userTag = new StringBuilder();
		
		if (instanceManager.getFlowInstance().getPoster() != null) {
			
			userTag.append(instanceManager.getFlowInstance().getPoster().getFirstname());
			userTag.append(" ");
			userTag.append(instanceManager.getFlowInstance().getPoster().getLastname());
			
			String citizenIdentifier = CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(instanceManager.getFlowInstance().getPoster());
			
			if (citizenIdentifier != null && !CollectionUtils.contains(flowTypesWithHiddenCitizenIdentifier, instanceManager.getFlowInstance().getFlow().getFlowType().getFlowTypeID())) {
				
				userTag.append(" (");
				userTag.append(citizenIdentifier);
				userTag.append(")");
			}
		}
		
		String message = messageTemplate.replace("$poster", userTag.toString());
		
		message = message.replace("$flowInstanceID", instanceManager.getFlowInstanceID().toString());
		message = message.replace("$flow.name", instanceManager.getFlowInstance().getFlow().getName());
		
		if (signature != null) {
			
			message = message.replace("$signed", DateUtils.DATE_TIME_SECONDS_FORMATTER.format(signature.getAdded()));
		}
		
		return message;
	}
	
	@WebPublic(alias = "pdf", requireLogin = true)
	public ForegroundModuleResponse getPDF(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws URINotFoundException, DuplicateFlowInstanceManagerIDException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException, SQLException {
		
		Integer flowInstanceID = uriParser.getInt(2);
		ImmutableFlowInstanceManager instanceManager;
		
		try {
			if (flowInstanceID == null || (instanceManager = getFlowInstanceManager(flowInstanceID, req, user, uriParser)) == null) {
				
				//Flow instance not found
				throw new URINotFoundException(uriParser);
			}
			
		} catch (FlowDisabledException e) {
			
			return showMessage(createDocument(req, uriParser), "FlowDisabled");
		}
		
		Signature signature = getValidSignatureForCurrentSigningChain(instanceManager, user);
		
		Entry<ImmutableFlowInstanceEvent, File> eventEntry;
		
		if (signature == null) {
			
			SigningParty signingParty = getMatchingSigningParty(user, instanceManager);
			
			if (signingParty == null) {
				
				try {
					UserFlowInstanceModule.PREVIEW_ACCESS_CONTROLLER.checkFlowInstanceAccess(instanceManager.getFlowInstance(), user);
					
				} catch (AccessDeniedException e) {
					
					//User does not have access to view PDF for flow instance
					log.info("User " + user + " not have access to flow instance " + instanceManager + ", no matching signing party found");
					return showMessage(createDocument(req, uriParser), "SigningPartyNotFound");
				}
			}
			
			if (instanceManager.getFlowState().getContentType() != ContentType.WAITING_FOR_MULTISIGN) {
				
				//Flow instance not in a signable state
				log.info("User does not have access to flow instance " + instanceManager + ", wrong status content type: " + instanceManager.getFlowState().getContentType());
				return showMessage(createDocument(req, uriParser), "WrongStatusContentType");
			}
			
			eventEntry = getLastestSignPDFEventWithFile(instanceManager.getFlowInstance(), null, true);
			
		} else {
			
			//TODO Change to false to allow all signing parties to view completed PDF
			eventEntry = getLastestSignPDFEventWithFile(instanceManager.getFlowInstance(), signature.getEventID(), true);
		}
		
		if (eventEntry == null) {
			
			log.warn("Unable to find PDF for flow instance " + flowInstanceID + " requested by user " + user);
			throw new URINotFoundException(uriParser);
		}
		
		log.info("Sending PDF for flow instance " + instanceManager + ", event " + eventEntry.getKey() + " to user " + user);
		
		try {
			HTTPUtils.sendFile(eventEntry.getValue(), instanceManager.getFlowInstance().getFlow().getName() + " - " + instanceManager.getFlowInstance().getFlowInstanceID() + ".pdf", req, res, ContentDisposition.ATTACHMENT);
			
		} catch (IOException e) {
			log.info("Error sending PDF for flow instance " + instanceManager + ", event " + eventEntry.getKey() + " to user " + user + ", " + e);
		}
		
		return null;
	}
	
	public ImmutableFlowInstanceEvent getLastestSignPDFEvent(ImmutableFlowInstance flowInstance, Integer eventID, boolean skipImmediateSubmitEvent) {
		
		// eventID is set if we have already signed
		if (eventID == null) {
			
			// Not signed, restrictive search for PDF
			List<ImmutableFlowInstanceEvent> signEvents = SigningUtils.getLastestSignEvents(flowInstance, skipImmediateSubmitEvent);
			
			if (!CollectionUtils.isEmpty(signEvents)) {
				
				ImmutableFlowInstanceEvent earliestEvent = signEvents.get(signEvents.size() - 1);
				
				if (earliestEvent.getAttributeHandler().getPrimitiveBoolean("pdf")) {
					
					return earliestEvent;
				}
			}
			
		} else {
			
			// Already signed, relaxed search for PDF
			if (!CollectionUtils.isEmpty(flowInstance.getEvents())) {
				
				for (ImmutableFlowInstanceEvent event : new ReverseListIterator<ImmutableFlowInstanceEvent>(flowInstance.getEvents())) {
					
					if (event.getAttributeHandler().getPrimitiveBoolean("pdf") && (eventID == null || eventID > event.getEventID())) {
						
						return event;
					}
				}
			}
		}
		
		return null;
	}
	
	public Entry<ImmutableFlowInstanceEvent, File> getLastestSignPDFEventWithFile(ImmutableFlowInstance flowInstance, Integer eventID, boolean skipImmediateSubmitEvent) {
		
		ImmutableFlowInstanceEvent event = getLastestSignPDFEvent(flowInstance, eventID, skipImmediateSubmitEvent);
		
		if (event != null) {
			
			File pdfFile = pdfProvider.getPDF(flowInstance.getFlowInstanceID(), event.getEventID());
			
			if (pdfFile != null) {
				
				return new SimpleEntry<ImmutableFlowInstanceEvent, File>(event, pdfFile);
			}
		}
		
		return null;
	}
	
	private SigningParty getMatchingSigningParty(User user, ImmutableFlowInstanceManager instanceManager) {
		
		String socialSecurityNumber = CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(user);
		
		if (socialSecurityNumber == null) {
			
			return null;
		}
		
		Set<SigningParty> signingParties = MultiSignUtils.getSigningParties(instanceManager);
		
		if (signingParties != null) {
			
			for (SigningParty signingParty : signingParties) {
				
				if (signingParty.getSocialSecurityNumber().equals(socialSecurityNumber)) {
					
					return signingParty;
				}
			}
		}
		
		return null;
	}
	
	private boolean isFullySigned(ImmutableFlowInstanceManager instanceManager) throws SQLException {
		
		Set<SigningParty> signingParties = MultiSignUtils.getSigningParties(instanceManager);
		
		ImmutableFlowInstanceEvent signingChainStartEvent = SigningUtils.getLastPosterSignEvents(instanceManager.getFlowInstance());
		
		for (SigningParty signingParty : signingParties) {
			
			if (getSignature(instanceManager.getFlowInstanceID(), signingParty.getSocialSecurityNumber(), signingChainStartEvent.getAdded()) == null) {
				
				return false;
			}
		}
		
		return true;
	}
	
	private ImmutableFlowInstanceManager getFlowInstanceManager(Integer flowInstanceID, HttpServletRequest req, User user, URIParser uriParser) throws DuplicateFlowInstanceManagerIDException, MissingQueryInstanceDescriptor, QueryProviderNotFoundException, InvalidFlowInstanceStepException, QueryProviderErrorException, QueryInstanceNotFoundInQueryProviderException, SQLException, FlowDisabledException {
		
		FlowInstance flowInstance = browserModule.getFlowInstance(flowInstanceID);
		
		if (flowInstance != null) {
			
			if (!flowInstance.getFlow().isEnabled() || isOperatingStatusDisabled(flowInstance.getFlow())) {
				
				throw new FlowDisabledException(flowInstance.getFlow());
			}
			
			return new ImmutableFlowInstanceManager(flowInstance, queryHandler, req, new DefaultInstanceMetadata(getCurrentSiteProfile(req, user, uriParser)), browserModule.getAbsoluteFileURL(uriParser, flowInstance.getFlow()));
		}
		
		return null;
	}
	
	protected SiteProfile getCurrentSiteProfile(HttpServletRequest req, User user, URIParser uriParser) {
		
		if (this.profileHandler != null) {
			
			return profileHandler.getCurrentProfile(user, req, uriParser);
		}
		
		return null;
	}
	
	private Signature getSignature(Integer flowInstanceID, String socialSecurityNumber, Timestamp signingChainStartEventTimestamp) throws SQLException {
		
		HighLevelQuery<Signature> query = new HighLevelQuery<Signature>();
		
		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));
		query.addParameter(socialSecurityNumberParamFactory.getParameter(socialSecurityNumber));
		query.addParameter(addedParamFactory.getParameter(signingChainStartEventTimestamp, QueryOperators.BIGGER_THAN_OR_EQUALS));
		
		return signatureDAO.get(query);
	}
	
	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {
		
		return moduleDescriptor;
	}
	
	@Override
	public List<LinkTag> getLinkTags() {
		
		return links;
	}
	
	@Override
	public List<ScriptTag> getScriptTags() {
		
		return scripts;
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser) {
		
		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));
		
		return doc;
	}
	
	@Override
	public void signingComplete(ImmutableFlowInstanceManager instanceManager, FlowInstanceEvent event, SigningParty signingParty, User user, HttpServletRequest req) throws SQLException {
		
		//add signature
		Signature signature = new Signature();
		signature.setAdded(TimeUtils.getCurrentTimestamp());
		signature.setFlowInstanceID(instanceManager.getFlowInstanceID());
		signature.setSocialSecurityNumber(signingParty.getSocialSecurityNumber());
		signature.setEventID(event.getEventID());
		
		signatureDAO.add(signature);
		
		if (user != null && user instanceof MutableUser) {
			
			MutableUser mutableUser = (MutableUser) user;
			boolean changed = false;
			
			if (!StringUtils.isEmpty(signingParty.getEmail()) && StringUtils.isEmpty(mutableUser.getEmail())) {
				
				changed = true;
				mutableUser.setEmail(signingParty.getEmail());
			}
			
			MutableAttributeHandler attributeHandler = mutableUser.getAttributeHandler();
			
			if (!StringUtils.isEmpty(signingParty.getMobilePhone()) && StringUtils.isEmpty(attributeHandler.getString("mobilePhone"))) {
				
				changed = true;
				attributeHandler.setAttribute("mobilePhone", signingParty.getMobilePhone());
				attributeHandler.setAttribute("contactBySMS", true);
			}
			
			if (changed) {
				
				try {
					systemInterface.getUserHandler().updateUser(mutableUser, false, false, true);
					
				} catch (UnableToUpdateUserException e) {
					
					log.error("Unable to update user " + user, e);
				}
			}
		}
		
		if (isFullySigned(instanceManager)) {
			
			log.info("Multi-party signing of flowinstance " + instanceManager + " complete");
			
			Map<String, String> eventAttributes = new HashMap<String, String>();
			eventAttributes.put(BaseFlowModule.SIGNING_CHAIN_ID_FLOW_INSTANCE_EVENT_ATTRIBUTE, getSigningChainID(instanceManager));
			
			browserModule.multiSigningComplete(instanceManager, (SiteProfile) req.getAttribute(this.getClass().getName() + ".siteProfile"), eventAttributes);
		}
		
		SessionUtils.setAttribute(this.getClass().getName() + "." + instanceManager.getFlowInstanceID(), signature, req);
	}
	
	@Override
	public void abortSigning(ImmutableFlowInstanceManager instanceManager) {
		
	}
	
	@Override
	public String getSignFailURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {
		
		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/sign/" + instanceManager.getFlowInstanceID() + "?signprovidererror=1";
	}
	
	@Override
	public String getSignSuccessURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {
		
		return getSigningURL(instanceManager, req);
	}
	
	@Override
	public String getSigningURL(ImmutableFlowInstanceManager instanceManager, HttpServletRequest req) {
		
		return RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/sign/" + instanceManager.getFlowInstanceID() + "?sign=true";
	}
	
	@Override
	public File getSigningPDF(ImmutableFlowInstanceManager instanceManager) {
		
		Entry<ImmutableFlowInstanceEvent, File> entry = getLastestSignPDFEventWithFile(instanceManager.getFlowInstance(), null, true);
		
		if (entry != null) {
			
			return entry.getValue();
		}
		
		return null;
	}
	
	@Override
	public String getSigningURL(ImmutableFlowInstance flowInstance, SigningParty signingParty) {
		
		return serverURL + systemInterface.getContextPath() + this.getFullAlias() + "/sign/" + flowInstance.getFlowInstanceID();
	}
	
	public String getSigningURL(ImmutableFlowInstance flowInstance, SigningParty signingParty, URIParser uriParser) {
		
		return uriParser.getFullContextPath() + this.getFullAlias() + "/sign/" + flowInstance.getFlowInstanceID();
	}
	
	public boolean isOperatingStatusDisabled(ImmutableFlow flow) {
		
		if (operatingMessageModule != null) {
			
			OperatingStatus operatingStatus = operatingMessageModule.getOperatingStatus(flow.getFlowFamily().getFlowFamilyID(), false);
			
			if (operatingStatus != null && operatingStatus.isDisabled()) {
				
				return true;
			}
		}
		
		return false;
	}
	
	private ForegroundModuleResponse showMessage(Document doc, String messageKey) {
		
		Element messageElement = doc.createElement("Message");
		messageElement.setAttribute("messageKey", messageKey);
		
		doc.getDocumentElement().appendChild(messageElement);
		
		return new SimpleForegroundModuleResponse(doc, getDefaultBreadcrumb());
	}
	
	@Override
	public String getSigningChainID(ImmutableFlowInstanceManager instanceManager) {
		
		ImmutableFlowInstanceEvent event = getLastestSignPDFEvent(instanceManager.getFlowInstance(), null, false);
		
		if (event != null) {
			
			return event.getAttributeHandler().getString(BaseFlowModule.SIGNING_CHAIN_ID_FLOW_INSTANCE_EVENT_ATTRIBUTE);
		}
		
		return null;
	}

	@Override
	public boolean partyHasSigned(Integer flowInstanceID, SigningParty signingParty, Timestamp signingChainStartEventTimestamp) throws SQLException {
		
		return getSignature(flowInstanceID, signingParty.getSocialSecurityNumber(), signingChainStartEventTimestamp) != null;
	}
	
	@InstanceManagerDependency(required = true)
	public void setFlowInstanceAdminModule(UserFlowInstanceModule userFlowInstanceModule) {
		
		if (userFlowInstanceModule != null) {
			
			userFlowInstanceModule.addListFlowInstancesExtensionProvider(this);
			
		} else {
			
			this.userFlowInstanceModule.removeListFlowInstancesExtensionProvider(this);
		}
		
		this.userFlowInstanceModule = userFlowInstanceModule;
	}

	@Override
	public ExtensionLink getListFlowInstancesExtensionLink(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user) throws Exception {
		return null;
	}

	@Override
	public ViewFragment getListFlowInstancesViewFragment(List<FlowInstance> flowInstances, SiteProfileHandler profileHandler, HttpServletRequest req, URIParser uriParser, User user) throws Exception {
		
		String citizenIdentifier = CitizenIdentifierUtils.getUserOrManagerCitizenIdentifier(user);
		
		if (!StringUtils.isEmpty(citizenIdentifier)) {
			
			List<MultiSigningQueryProvider> multiSingningQueryProviders = queryHandler.getAssignableQueryProviders(MultiSigningQueryProvider.class);
			
			if (!CollectionUtils.isEmpty(multiSingningQueryProviders)) {
				
				List<Integer> queryInstanceIDs = new ArrayList<Integer>();
				
				for (MultiSigningQueryProvider queryProvider : multiSingningQueryProviders) {
					
					List<Integer> queryProviderInstanceIDs = queryProvider.getQueryInstanceIDs(citizenIdentifier);
					
					if (queryProviderInstanceIDs != null) {
						
						queryInstanceIDs.addAll(queryProviderInstanceIDs);
					}
				}
				
				if (!CollectionUtils.isEmpty(queryInstanceIDs)) {
					
					Document doc = createDocument(req, uriParser);
					
					Element listFlowInstancesExtensionElement = doc.createElement("ListFlowInstancesExtension");
					doc.getDocumentElement().appendChild(listFlowInstancesExtensionElement);
					
					if (profileHandler != null) {
						
						XMLUtils.append(doc, listFlowInstancesExtensionElement, "SiteProfiles", profileHandler.getProfiles());
					}
					
					XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);
					genDoc.addIgnoredFields(UserFlowInstanceModule.LIST_EXCLUDED_FIELDS);
					
					//@formatter:off
					ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(browserModule.getDAOFactory().getQueryInstanceDescriptorDAO().getDataSource(),
							"SELECT DISTINCT flowInstanceID FROM " + browserModule.getDAOFactory().getQueryInstanceDescriptorDAO().getTableName()
							+ " WHERE queryInstanceID IN (? " + StringUtils.repeatString(",?", queryInstanceIDs.size() - 1) + ")"
							, IntegerPopulator.getPopulator());
					//@formatter:on
					
					for (int i = 0; i < queryInstanceIDs.size(); i++) {
						
						query.setInt(i + 1, queryInstanceIDs.get(i));
					}
					
					ArrayList<Integer> flowInstanceIDs = query.executeQuery();
					
					if (flowInstanceIDs != null) {
						
						Element waitingMultiSignFlowInstancesElement = XMLUtils.appendNewElement(doc, listFlowInstancesExtensionElement, "WaitingMultiSignFlowInstances");
						
						List<FlowInstance> multiSignFlowInstances = getMultiSignFlowInstances(flowInstanceIDs);
						
						if (multiSignFlowInstances != null) {
							
							for (FlowInstance flowInstance : multiSignFlowInstances) {
								
								Element flowInstanceElement = (Element) waitingMultiSignFlowInstancesElement.appendChild(flowInstance.toXML(genDoc));
								
								XMLUtils.appendNewElement(doc, flowInstanceElement, "MultiSignURL", getSigningURL(flowInstance, null, uriParser));
							}
						}
					}
					
					return viewFragmentTransformer.createViewFragment(doc);
				}
			}
		}
		
		return null;
	}
	
	protected List<FlowInstance> getMultiSignFlowInstances(List<Integer> flowInstanceIDs) throws SQLException {
		
		LowLevelQuery<FlowInstance> query = new LowLevelQuery<FlowInstance>("SELECT i.* FROM " + browserModule.getDAOFactory().getFlowInstanceDAO().getTableName()
				+ " i INNER JOIN (SELECT statusID FROM " + browserModule.getDAOFactory().getStatusDAO().getTableName() + " WHERE contentType = '" + ContentType.WAITING_FOR_MULTISIGN + "') s ON i.statusID = s.statusID"
				+ " WHERE i.flowInstanceID IN (? " + StringUtils.repeatString(",?", flowInstanceIDs.size() - 1) + ")");
		
		query.addRelations(FlowInstance.FLOW_RELATION, FlowInstance.STATUS_RELATION, Flow.FLOW_TYPE_RELATION);
		
		query.addExcludedFields(UserFlowInstanceModule.LIST_EXCLUDED_FIELDS);
		
		query.addParameters(flowInstanceIDs);
		
		return browserModule.getDAOFactory().getFlowInstanceDAO().getAll(query);
	}

	@Override
	public int getModuleID() {
		return moduleDescriptor.getModuleID();
	}
	
	@WebPublic(alias = "cancel", requireLogin = true)
	public ForegroundModuleResponse cancelSigning(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Integer flowInstanceID = uriParser.getInt(2);
		ImmutableFlowInstanceManager instanceManager;
		Document doc = createDocument(req, uriParser);
		
		try {
			if (flowInstanceID == null || (instanceManager = getFlowInstanceManager(flowInstanceID, req, user, uriParser)) == null) {
				
				//Flow instance not found
				throw new URINotFoundException(uriParser);
			}
			
		} catch (FlowDisabledException e) {
			
			return showMessage(doc, "FlowDisabled");
		}
		
		if (CollectionUtils.isEmpty(instanceManager.getFlowInstance().getOwners()) || !instanceManager.getFlowInstance().getOwners().contains(user)) {
			
			log.info("User " + user + " not have access to flow instance " + instanceManager);
			return showMessage(doc, "SigningPartyNotFound");
		}
		
		if (instanceManager.getFlowState().getContentType() != ContentType.WAITING_FOR_MULTISIGN) {
			
			log.info("User does not have access to flow instance " + instanceManager + ", wrong status content type: " + instanceManager.getFlowState().getContentType());
			return showMessage(doc, "WrongStatusContentType");
		}
		
		if (instanceManager.getFlowInstance().getFirstSubmitted() != null) {
			
			log.info("User " + user + " attempted to cancel multipart signing of already submitted flow instance " + instanceManager);
			return showMessage(doc, "MayNotCancelSubmittedFlowInstance");
		}
		
		FlowInstance flowInstance = (FlowInstance) instanceManager.getFlowInstance();
		flowInstance.setStatus(flowInstance.getFlow().getDefaultState(FlowBrowserModule.SAVE_ACTION_ID));
		
		flowInstanceDAO.update(flowInstance);
		
		systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
		systemInterface.getEventHandler().sendEvent(FlowInstance.class, new MultiSigningCanceledEvent(instanceManager.getFlowInstance(), MultiSignUtils.getSigningParties(instanceManager), null, user), EventTarget.ALL);
		
		String preview = flowInstance.getFlow().usesPreview() ? "?preview=1" : "";
		
		res.sendRedirect(uriParser.getContextPath() + userFlowInstanceModule.getFullAlias() + "/flowinstance/" + flowInstance.getFlow().getFlowID() + "/" + flowInstance.getFlowInstanceID() + preview);
		return null;
	}
	
	@EventListener(channel = FlowInstance.class)
	public void processEvent(CRUDEvent<FlowInstance> event, EventSource source) throws SQLException {
		
		if (source.isLocal()) {
			
			if (event.getAction() == CRUDAction.DELETE) {
				
				for (FlowInstance flowInstance : event.getBeans()) {
					
					log.info("Deleting signatures for flow instance " + flowInstance);
					
					HighLevelQuery<Signature> query = new HighLevelQuery<Signature>();
					
					query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstance.getFlowInstanceID()));
					
					signatureDAO.delete(query);
				}
			}
		}
	}
	
	@Override
	public boolean supportsSequentialSigning() {
		return false;
	}
	
	protected String getImmutableQueryRequestBaseURL(HttpServletRequest req, FlowInstanceManager instanceManager) {

		return req.getContextPath() + getFullAlias() + "/iquery/" + instanceManager.getFlowID() + "/" + instanceManager.getFlowInstanceID() + "/q/";
	}
	
	@WebPublic(alias = "iquery")
	public ForegroundModuleResponse processImmutableQueryRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws ModuleConfigurationException, SQLException, AccessDeniedException, IOException, FlowDefaultStatusNotFound, EvaluationException, URINotFoundException, QueryRequestException, QueryProviderException, EvaluationProviderException, InvalidFlowInstanceStepException, MissingQueryInstanceDescriptor, DuplicateFlowInstanceManagerIDException {

		return browserModule.processImmutableQueryRequest(req, res, user, uriParser, this, true, false);
	}
	
	@Override
	public QueryHandler getQueryHandler() {

		return queryHandler;
	}

	@Override
	public void checkNewFlowInstanceAccess(Flow flow, User user, SiteProfile profile) throws AccessDeniedException {}

	@Override
	public void checkFlowInstanceAccess(ImmutableFlowInstance flowInstance, User user) throws AccessDeniedException {}

	@Override
	public boolean isMutable(ImmutableFlowInstance flowInstance, User user) {

		return false;
	}

}
