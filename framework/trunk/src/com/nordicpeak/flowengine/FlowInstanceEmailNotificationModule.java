package com.nordicpeak.flowengine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.emailutils.framework.FileAttachment;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.emailutils.validation.StringMultiEmailValidator;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.events.EventListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.settings.SettingHandler;
import se.unlogic.hierarchy.core.settings.Setting;
import se.unlogic.hierarchy.core.settings.TextFieldSetting;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileSettingProvider;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.events.SubmitEvent;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;

public class FlowInstanceEmailNotificationModule extends AnnotatedForegroundModule implements SiteProfileSettingProvider, EventListener<SubmitEvent> {

	private static final String SETTING_ID_PREFIX = "FlowFamilyEmailAddresses:";

	private static final AnnotatedBeanTagSourceFactory<Flow> FLOW_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<Flow>(Flow.class, "$flow.");

	private static final AnnotatedBeanTagSourceFactory<FlowInstance> FLOWINSTANCE_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<FlowInstance>(FlowInstance.class, "$flowInstance.");

	private static final AnnotatedBeanTagSourceFactory<User> USER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<User>(User.class, "$poster.");

	private static final StringMultiEmailValidator MULTI_EMAIL_VALIDATOR = new StringMultiEmailValidator();

	@XSLVariable(prefix = "java.")
	private String settingNamePrefix = "Email recipients for";

	@XSLVariable(prefix = "java.")
	private String settingDescriptionPrefix = "Enter one or more email addresses (separated by comma) to receive email notifications for submitted applications";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Email sender name", description = "The name displayed in the sender field of sent e-mail", required = true)
	protected String emailSenderName = "Not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Email sender address", description = "The sender address", required = true, formatValidator = EmailPopulator.class)
	protected String emailSenderAddress = "not.set@not.set.com";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Email subject", description = "The text displayed in the subject of sent email. Available tags: $flow.name", required = true)
	protected String emailSubject = "Not set";

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Email message", description = "The message used when sending emails from this module. Available tags: $flow.name, $flow.shortDescription, $flow.longDescription, $flowInstance.flowInstanceID, $poster.*", required = true)
	protected String emailMessage = "Not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF filename (without file extension)", description = "Filename of the attached PDF (without file extension). Available tags: $flow.name, $flowInstance.flowInstanceID, $poster.*", required = true)
	protected String pdfFilename = "$flow.name, $flowInstance.flowInstanceID";

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Default email recipients", description = "The default email recipients to send emails to if no email addresses is set in current site profile", required = true)
	protected String defaultEmailRecipients;

	protected List<String> emailRecipients;

	@InstanceManagerDependency
	protected PDFProvider pdfProvider;

	@InstanceManagerDependency
	protected FlowAdminModule flowAdminModule;

	protected SiteProfileHandler siteProfileHandler;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		systemInterface.getEventHandler().addEventListener(FlowInstanceManager.class, SubmitEvent.class, this);

	}

	@Override
	protected void moduleConfigured() {

		emailRecipients = getEmailAddresses(defaultEmailRecipients);

	}

	@Override
	public void unload() throws Exception {

		systemInterface.getEventHandler().removeEventListener(FlowInstanceManager.class, SubmitEvent.class, this);

		super.unload();
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return new SimpleForegroundModuleResponse("<div class=\"contentitem\">This module does not have a frontend. Check the log instead</div>", this.getDefaultBreadcrumb());
	}

	public void sendEmail(FlowInstance flowInstance, FlowInstanceEvent flowInstanceEvent, SiteProfile siteProfile) {

		try {

			List<String> recipients = null;

			if (siteProfileHandler != null) {

				if (siteProfile != null) {

					SettingHandler settingHandler = siteProfile.getSettingHandler();

					if (settingHandler == null) {
						settingHandler = siteProfileHandler.getGlobalSettingHandler();
					}

					if (settingHandler != null) {

						recipients = getEmailAddresses(settingHandler.getString(SETTING_ID_PREFIX + flowInstance.getFlow().getFlowFamily().getFlowFamilyID()));

						if (recipients == null) {

							log.info("SiteProfile " + siteProfile + " has no email recipients set for flowfamily " + flowInstance.getFlow().getFlowFamily() + ". Falling back to default email recipients.");

						}

					}

				}

			}

			if (recipients == null && emailRecipients != null) {

				recipients = emailRecipients;

			}

			if (!CollectionUtils.isEmpty(recipients)) {

				File pdfFile = pdfProvider.getPDF(flowInstance.getFlowInstanceID(), flowInstanceEvent.getEventID());

				if(pdfFile == null){

					log.warn("No PDF found for flowInstance " + flowInstance + ". No email notification will be sent.");

					return;
				}

				log.info("Sending email notification message about submitted flowinstance " + flowInstance);

				TagReplacer tagReplacer = new TagReplacer(FLOWINSTANCE_TAG_SOURCE_FACTORY.getTagSource(flowInstance), FLOW_TAG_SOURCE_FACTORY.getTagSource(flowInstance.getFlow()));

				if (flowInstance.getPoster() != null) {
					tagReplacer.addTagSource(USER_TAG_SOURCE_FACTORY.getTagSource(flowInstance.getPoster()));
				}

				String message = tagReplacer.replace(emailMessage);

				String subject = tagReplacer.replace(emailSubject);

				for (String recipient : recipients) {

					SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());
					email.addRecipient(recipient.trim());
					email.setSenderName(emailSenderName);
					email.setSenderAddress(emailSenderAddress);
					email.setSubject(subject);
					email.setMessageContentType(SimpleEmail.HTML);
					email.setMessage(message);

					String filename = tagReplacer.replace(pdfFilename) + ".pdf";

					email.add(new FileAttachment(pdfFile, FileUtils.toValidHttpFilename(filename)));

					systemInterface.getEmailHandler().send(email);

				}

			} else {

				log.warn("No default email recipients specified, email notification message will not be sent.");

			}

		} catch (InvalidEmailAddressException e) {

			log.error("Error sending email notification about submitted flowinstance " + flowInstance, e);

		} catch (NoEmailSendersFoundException e) {

			log.error("Error sending email notification about submitted flowinstance " + flowInstance, e);

		} catch (UnableToProcessEmailException e) {

			log.error("Error sending email notification about submitted flowinstance " + flowInstance, e);

		}

	}

	@Override
	public void processEvent(SubmitEvent event, EventSource source) {

		FlowInstanceEmailNotificationModule.this.sendEmail((FlowInstance)event.getFlowInstanceManager().getFlowInstance(), event.getEvent(), event.getSiteProfile());
	}

	@InstanceManagerDependency(required = true)
	public void setSiteProfileHandler(SiteProfileHandler siteProfileHandler) {

		if (siteProfileHandler != null) {

			siteProfileHandler.addSettingProvider(this);

		} else {

			this.siteProfileHandler.removeSettingProvider(this);
		}

		this.siteProfileHandler = siteProfileHandler;
	}

	@Override
	public List<Setting> getSiteProfileSettings() {

		if (flowAdminModule != null) {

			Collection<FlowFamily> flowFamilies = flowAdminModule.getCachedFlowFamilies();

			if (!CollectionUtils.isEmpty(flowFamilies)) {

				List<Setting> settings = new ArrayList<Setting>(flowFamilies.size());

				for (FlowFamily flowFamily : flowFamilies) {

					Flow latestFlow = flowAdminModule.getLatestFlowVersion(flowFamily);

					settings.add(new TextFieldSetting(SETTING_ID_PREFIX + flowFamily.getFlowFamilyID(), settingNamePrefix + ": " + latestFlow.getName(), settingDescriptionPrefix + ": " + latestFlow.getName(), null, false, MULTI_EMAIL_VALIDATOR));

				}

				return settings;

			}

		}

		return null;

	}

	private List<String> getEmailAddresses(String emails) {

		if (emails != null) {

			String[] addresses = emails.split("[,;:]");

			if (addresses != null) {

				return Arrays.asList(addresses);

			}

		}

		return null;
	}

	@Override
	public int getPriority() {

		return 50;
	}

	@Override
	public void siteProfileHandlerConfigurationUpdated(SiteProfileHandler siteProfileHandler) {
		
	}

	@Override
	public List<Setting> getSiteSubProfileSettings() {

		return null;
	}

	@Override
	public String getModuleName() {

		return moduleDescriptor.getName();
	}
}
