package com.nordicpeak.authifyclient;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.SimpleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.UserUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.hash.HashAlgorithms;
import se.unlogic.standardutils.hash.HashUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.BaseFlowModule;
import com.nordicpeak.flowengine.SigningConfirmedResponse;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.SigningParty;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.interfaces.MultiSigningCallback;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.SigningCallback;
import com.nordicpeak.flowengine.interfaces.SigningProvider;
import com.nordicpeak.flowengine.managers.ImmutableFlowInstanceManager;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

public class AuthifySigningProvider extends AnnotatedForegroundModule implements SigningProvider {

	public static final String CITIZEN_IDENTIFIER = "citizenIdentifier";
	
	protected static final RelationQuery EVENT_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstanceEvent.ATTRIBUTES_RELATION);

	@XSLVariable(prefix = "java.")
	protected String signingMessage = "You will sign $flow.name with application number $flowInstance.flowInstanceID. The application has the following unique key: $hash";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Authify API key", description = "The API key used for requests to Authify rest service", required = true)
	protected String authifyAPIKey;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Authify secret key", description = "The secret key used for requests to Authify rest service", required = true)
	protected String authifySecretKey;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Include debug data", description = "Controls whether or not debug data should be included in the view fragments objects")
	protected boolean includeDebugData = false;

	@InstanceManagerDependency
	private PDFProvider pdfProvider;

	protected SimpleViewFragmentTransformer fragmentTransformer;

	protected FlowEngineDAOFactory daoFactory;

	protected AuthifyClient authifyClient;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
	}

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(SigningProvider.class, this)) {

			throw new RuntimeException("Unable to register module " + this.moduleDescriptor + " in global instance handler using key " + SigningProvider.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(SigningProvider.class, this);

		super.unload();
	}

	@Override
	protected void moduleConfigured() {

		authifyClient = new AuthifyClient(authifyAPIKey, authifySecretKey);

		try {

			fragmentTransformer = new SimpleViewFragmentTransformer(moduleDescriptor.getXslPath(), systemInterface.getEncoding(), this.getClass(), moduleDescriptor, sectionInterface);

		} catch (Exception e) {

			log.error("Unable to parse XSL stylesheet for authify signing form in module " + this.moduleDescriptor, e);

		}

	}

	@Override
	public ViewFragment sign(HttpServletRequest req, HttpServletResponse res, User user, MutableFlowInstanceManager instanceManager, SigningCallback signingCallback, boolean modifiedSinceLastSignRequest) throws IOException, FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound {

		String signingURL = signingCallback.getSigningURL(instanceManager, req);

		AuthifySession authifySession = authifyClient.getAuthifySession(instanceManager.getFlowInstanceID() + "", user, req, true);

		if (authifySession == null) {

			authifyClient.login(instanceManager.getFlowInstanceID() + "", user, signingURL, res);

			return null;

		}

		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (req.getParameter("idp") != null) {

			try {

				if (pdfProvider != null) {

					File tempPDF = pdfProvider.getTemporaryPDF(instanceManager);

					if (tempPDF != null && tempPDF.exists()) {

						String pdfHash = HashUtils.hash(tempPDF, HashAlgorithms.SHA1);

						String signMessage = signingMessage.replace("$flow.name", instanceManager.getFlowInstance().getFlow().getName());
						signMessage = signMessage.replace("$flowInstance.flowInstanceID", instanceManager.getFlowInstanceID() + "");
						signMessage = signMessage.replace("$hash", pdfHash);

						authifyClient.sign(req.getParameter("idp"), signMessage, authifySession, signingURL, user, req, res);

						return null;

					}

				}

				log.warn("Unable to find temporary PDF for flow instance " + instanceManager + " submitted by user " + user);

			} catch (Exception e) {

				log.info("Signing of flow instance " + instanceManager + " by user " + user + " failed.");

				deleteTemporaryPDF(instanceManager, user);

			}

			signingCallback.abortSigning(instanceManager);

			authifyClient.logout(authifySession, req);

			errors.add(new ValidationError("SigningFailed"));

		} else if (!CollectionUtils.isEmpty(authifyClient.getUpdatedSignAttributes(authifySession, req)) && !modifiedSinceLastSignRequest) {

			validateSigning(authifySession, user, errors);

			authifyClient.logout(authifySession, req);

			if (errors.isEmpty()) {

				log.info("User " + user + " signed flow instance " + instanceManager);

				SigningConfirmedResponse response = signingCallback.signingConfirmed(instanceManager, req, user);

				FlowInstanceEvent signingEvent = response.getSigningEvent();

				signingEvent.getAttributeHandler().setAttribute("signingProvider", this.getClass().getName());
				signingEvent.getAttributeHandler().setAttribute("signingData", authifySession.getSignXML());
//				signingEvent.getAttributeHandler().setAttribute("signingChecksum", );
				
				if(user != null){
					
					String ssn = getSocialSecurityNumber(user);
					
					if(ssn != null){
						
						signingEvent.getAttributeHandler().setAttribute(CITIZEN_IDENTIFIER, ssn);
					}
				}

				daoFactory.getFlowInstanceEventDAO().update(signingEvent, EVENT_ATTRIBUTE_RELATION_QUERY);

				FlowInstanceEvent pdfEvent = response.getSubmitEvent() != null ? response.getSubmitEvent() : signingEvent;

				if (pdfProvider != null) {

					try {

						if (pdfProvider.saveTemporaryPDF(instanceManager, pdfEvent)) {

							log.info("Temporary PDF for flow instance " + instanceManager + " requested by user " + user + " saved for event " + pdfEvent);

						} else {

							log.warn("Unable to find temporary PDF for flow instance " + instanceManager + " submitted by user " + user);
						}

					} catch (Exception e) {

						log.error("Error saving temporary PDF for flow instance " + instanceManager + " submitted by user " + user, e);
					}

				}

				signingCallback.signingComplete(instanceManager, pdfEvent, req);

				res.sendRedirect(signingCallback.getSignSuccessURL(instanceManager, req));

				return null;

			}

		}

		//TODO check age of temporary PDF
		if (pdfProvider != null && (modifiedSinceLastSignRequest || !pdfProvider.hasTemporaryPDF(instanceManager))) {

			try {
				pdfProvider.createTemporaryPDF(instanceManager, signingCallback.getSiteProfile(), user);

			} catch (Exception e) {

				log.error("Error generating temporary PDF for flow instance " + instanceManager + " submitted by user " + user, e);
			}
		}

		log.info("User " + user + " requested sign form for flow instance " + instanceManager);

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);

		Element signElement = doc.createElement("SignForm");
		document.appendChild(signElement);

		if (!errors.isEmpty()) {

			XMLUtils.append(doc, signElement, errors);

		}

		XMLUtils.appendNewElement(doc, signElement, "signingURL", signingURL);

		try {

			return fragmentTransformer.createViewFragment(doc);

		} catch (Exception e) {

			res.sendRedirect(signingCallback.getSignFailURL(instanceManager, req));

			return null;

		}

	}

	@Override
	public ViewFragment sign(HttpServletRequest req, HttpServletResponse res, User user, ImmutableFlowInstanceManager instanceManager, MultiSigningCallback signingCallback, SigningParty signingParty) throws IOException, FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound {

		String signingURL = signingCallback.getSigningURL(instanceManager, req);

		AuthifySession authifySession = authifyClient.getAuthifySession(instanceManager.getFlowInstanceID() + "", user, req, true);

		if (authifySession == null) {

			authifyClient.login(instanceManager.getFlowInstanceID() + "", user, signingURL, res);

			return null;

		}

		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (req.getParameter("idp") != null) {

			try {

				if (pdfProvider != null) {

					File signingPDF = signingCallback.getSigningPDF(instanceManager);

					if (signingPDF != null && signingPDF.exists()) {

						String pdfHash = HashUtils.hash(signingPDF, HashAlgorithms.SHA1);

						String signMessage = signingMessage.replace("$flow.name", instanceManager.getFlowInstance().getFlow().getName());
						signMessage = signMessage.replace("$flowInstance.flowInstanceID", instanceManager.getFlowInstanceID() + "");
						signMessage = signMessage.replace("$hash", pdfHash);

						authifyClient.sign(req.getParameter("idp"), signMessage, authifySession, signingURL, user, req, res);

						return null;

					}

				}

				log.warn("Unable to find any PDF for flow instance " + instanceManager + " submitted by user " + user);

			} catch (Exception e) {

				log.info("Signing of flow instance " + instanceManager + " by user " + user + " failed.");

			}

			signingCallback.abortSigning(instanceManager);

			authifyClient.logout(authifySession, req);

			errors.add(new ValidationError("SigningFailed"));

		} else if (!CollectionUtils.isEmpty(authifyClient.getUpdatedSignAttributes(authifySession, req))) {

			validateSigning(authifySession, user, errors);

			authifyClient.logout(authifySession, req);

			if (errors.isEmpty()) {

				log.info("User " + user + " signed flow instance " + instanceManager);

				FlowInstance flowInstance = (FlowInstance) instanceManager.getFlowInstance();

				FlowInstanceEvent signingEvent = new FlowInstanceEvent();
				signingEvent.setFlowInstance(flowInstance);
				signingEvent.setEventType(EventType.SIGNED);
				signingEvent.setPoster(user);
				signingEvent.setStatus(flowInstance.getStatus().getName());
				signingEvent.setStatusDescription(flowInstance.getStatus().getDescription());
				signingEvent.setAdded(TimeUtils.getCurrentTimestamp());

				signingEvent.getAttributeHandler().setAttribute("signingProvider", this.getClass().getName());
				signingEvent.getAttributeHandler().setAttribute("signingData", authifySession.getSignXML());
//				signingEvent.getAttributeHandler().setAttribute("signingChecksum", );
				signingEvent.getAttributeHandler().setAttribute(CITIZEN_IDENTIFIER, signingParty.getSocialSecurityNumber());
				signingEvent.getAttributeHandler().setAttribute(BaseFlowModule.SIGNING_CHAIN_ID_FLOW_INSTANCE_EVENT_ATTRIBUTE, signingCallback.getSigningChainID(instanceManager));

				daoFactory.getFlowInstanceEventDAO().add(signingEvent, EVENT_ATTRIBUTE_RELATION_QUERY);

				signingCallback.signingComplete(instanceManager, signingEvent, signingParty, req);

				res.sendRedirect(signingCallback.getSignSuccessURL(instanceManager, req));

				return null;

			}

		}

		log.info("User " + user + " requested multi sign form for flow instance " + instanceManager);

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		doc.appendChild(document);

		Element signElement = doc.createElement("SignForm");
		document.appendChild(signElement);

		if (!errors.isEmpty()) {

			XMLUtils.append(doc, signElement, errors);

		}

		XMLUtils.appendNewElement(doc, signElement, "signingURL", signingURL);

		try {

			return fragmentTransformer.createViewFragment(doc);

		} catch (Exception e) {

			res.sendRedirect(signingCallback.getSignFailURL(instanceManager, req));

			return null;

		}

	}

	protected void validateSigning(AuthifySession authifySession, User user, List<ValidationError> errors) {

		String signingSSN = authifySession.getAttribute(AuthifyClient.SSN_ATTRIBUTE_NAME);

		if (StringUtils.isEmpty(signingSSN)) {

			errors.add(new ValidationError("IncompleteSigning"));

			return;
		}
		
		if (user != null) {

			String userSSN = getSocialSecurityNumber(user);

			if (userSSN == null || !userSSN.equals(signingSSN)) {

				errors.add(new ValidationError("SSNNotMatching"));

				return;
			}
		}
	}

	protected String getSocialSecurityNumber(User user){
		
		if(user != null){
			
			return UserUtils.getAttribute(CITIZEN_IDENTIFIER, user);
		}
		
		return null;
	}
	
	protected void deleteTemporaryPDF(MutableFlowInstanceManager instanceManager, User user) {

		if (pdfProvider != null) {

			try {

				pdfProvider.deleteTemporaryPDF(instanceManager);

			} catch (Exception e) {

				log.error("Error deleting temporary PDF for flow instance " + instanceManager + " submitted by user " + user, e);
			}
		}
	}
	
}
