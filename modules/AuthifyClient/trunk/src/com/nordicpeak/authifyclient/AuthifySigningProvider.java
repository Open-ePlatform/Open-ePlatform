package com.nordicpeak.authifyclient;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
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
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.hash.HashAlgorithms;
import se.unlogic.standardutils.hash.HashUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.exceptions.flow.FlowDefaultStatusNotFound;
import com.nordicpeak.flowengine.exceptions.flowinstancemanager.FlowInstanceManagerClosedException;
import com.nordicpeak.flowengine.exceptions.queryinstance.UnableToSaveQueryInstanceException;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.SigningCallback;
import com.nordicpeak.flowengine.interfaces.SigningProvider;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;

public class AuthifySigningProvider extends AnnotatedForegroundModule implements SigningProvider {

	protected static final RelationQuery EVENT_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstanceEvent.ATTRIBUTES_RELATION);

	@XSLVariable(prefix="java.")
	protected String signingMessage = "You will sign $flow.name with application number $flowInstance.flowInstanceID. The application has the following unique key: $hash";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Authify API key", description = "The API key used for requests to Authify rest service", required = true)
	protected String authifyAPIKey;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Authify secret key", description = "The secret key used for requests to Authify rest service", required = true)
	protected String authifySecretKey;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User SSN attribute name", description = "The SSN attribute name in attribute handler to use when validating signing (leave empty to desiable SSN validation)", required = false)
	protected String userSSNAttributeName;

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

		if (this.equals(systemInterface.getInstanceHandler().getInstance(SigningProvider.class))) {

			systemInterface.getInstanceHandler().removeInstance(SigningProvider.class);
		}

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
	public ViewFragment sign(HttpServletRequest req, HttpServletResponse res, User user, MutableFlowInstanceManager instanceManager, SigningCallback signingCallback, boolean savedDuringCurrentRequest) throws IOException, FlowInstanceManagerClosedException, UnableToSaveQueryInstanceException, SQLException, FlowDefaultStatusNotFound {

		String signingURL = signingCallback.getSigningURL(instanceManager, req);

		AuthifySession authifySession = authifyClient.getAuthifySession(instanceManager.getFlowInstanceID() + "", user, req, true);

		if (authifySession == null) {

			authifyClient.login(instanceManager.getFlowInstanceID() + "", user, signingURL, res);

			return null;

		}

		List<ValidationError> errors = new ArrayList<ValidationError>();

		if (req.getParameter("idp") != null) {

			try {

				if(pdfProvider != null) {
				
					File tempPDF = pdfProvider.getTemporaryPDF(instanceManager.getFlowInstanceID());
					
					if(tempPDF != null && tempPDF.exists()) {
						
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
			
		} else if (!CollectionUtils.isEmpty(authifyClient.getUpdatedSignAttributes(authifySession, req)) && !savedDuringCurrentRequest) {

			validateSigning(authifySession, user, errors);

			authifyClient.logout(authifySession, req);

			if (errors.isEmpty()) {

				log.info("User " + user + " signed flow instance " + instanceManager);

				FlowInstanceEvent event = signingCallback.signingConfirmed(instanceManager, user);

				event.getAttributeHandler().setAttribute("signingProvider", this.getClass().getName());
				event.getAttributeHandler().setAttribute("signingXML", authifySession.getSignXML());

				daoFactory.getFlowInstanceEventDAO().update(event, EVENT_ATTRIBUTE_RELATION_QUERY);

				if (pdfProvider != null) {

					try {

						if (pdfProvider.saveTemporaryPDF(instanceManager.getFlowInstanceID(), event)) {

							log.info("Temporary PDF for flow instance " + instanceManager + " requested by user " + user + " saved for event " + event);

						} else {

							log.warn("Unable to find temporary PDF for flow instance " + instanceManager + " submitted by user " + user);
						}

					} catch (Exception e) {

						log.error("Error saving temporary PDF for flow instance " + instanceManager + " submitted by user " + user, e);
					}

				}

				signingCallback.signingComplete(instanceManager, event, req);

				res.sendRedirect(signingCallback.getSignSuccessURL(instanceManager, req));

				return null;

			}

		}

		if (pdfProvider != null && (savedDuringCurrentRequest || !pdfProvider.hasTemporaryPDF(instanceManager.getFlowInstanceID()))) {

			try {
				pdfProvider.createTemporaryPDF(instanceManager, true, signingCallback.getSiteProfile(), user);

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

	protected void validateSigning(AuthifySession authifySession, User user, List<ValidationError> errors) {

		String signingSSN = authifySession.getAttribute(AuthifyClient.SSN_ATTRIBUTE_NAME);

		if (StringUtils.isEmpty(signingSSN)) {

			errors.add(new ValidationError("IncompleteSigning"));

			return;
		}

		if (userSSNAttributeName != null && user != null && user.getAttributeHandler() != null) {

			String userSSN = user.getAttributeHandler().getString(userSSNAttributeName);

			if (userSSN == null || !userSSN.equals(signingSSN)) {

				errors.add(new ValidationError("SSNNotMatching"));

				return;
			}

		}

	}

	protected void deleteTemporaryPDF(MutableFlowInstanceManager instanceManager, User user) {

		if (pdfProvider != null) {

			try {

				pdfProvider.deleteTemporaryPDF(instanceManager.getFlowInstanceID());

			} catch (Exception e) {

				log.error("Error deleting temporary PDF for flow instance " + instanceManager + " submitted by user " + user, e);
			}
		}

	}

}
