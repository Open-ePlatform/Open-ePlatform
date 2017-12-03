package com.nordicpeak.flowengine.attachments;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.ModuleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.core.validationerrors.FileSizeLimitExceededValidationError;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.URLStringConverterUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowInstanceAdminModule;
import com.nordicpeak.flowengine.UserFlowInstanceModule;
import com.nordicpeak.flowengine.beans.Contact;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.notifications.FlowFamililyNotificationSettings;
import com.nordicpeak.flowengine.notifications.StandardFlowNotificationHandler;

public class FlowInstanceAttachmentsModule extends AnnotatedForegroundModule implements ViewFragmentModule<ForegroundModuleDescriptor> {
	
	
	@XSLVariable(prefix = "java.")
	protected String attachmentsUpdatedMessage = "Updated attachments";
	
	@XSLVariable(prefix = "java.")
	protected String tabTitle;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmententXML;
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Temp dir", description = "Directory for temporary files. Should be on the same filesystem as the file store for best performance. If not set system default temp directory will be used")
	protected String tempDir;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max request size", description = "Maxmium allowed request size in megabytes", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxRequestSize = 1000;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max file size", description = "Maxmium allowed file size in megabytes", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxFileSize = 50;
	
	@ModuleSetting
	@TextAreaSettingDescriptor(name = "SMS to users when new attachments are added", description = "The SMS messages sent to the users when they receive new attachments.", required = true)
	@XSLVariable(prefix = "java.")
	private String newAttachmentsUserSMS;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "New attachments email subject (users)", description = "The subject of emails sent to the users when new attachments are added", required = true)
	@XSLVariable(prefix = "java.")
	private String newAttachmentsUserEmailSubject;
	
	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "New attachments email message (users)", description = "The message of emails sent to the users when new attachments are added", required = true)
	@XSLVariable(prefix = "java.")
	private String newAttachmentsUserEmailMessage;
	
	@InstanceManagerDependency(required = true)
	protected StandardFlowNotificationHandler flowNotificationHandler;
	
	private AnnotatedDAO<Attachment> attachmentDAO;
	
	private AnnotatedDAOWrapper<Attachment, Integer> attachmentDAOWrapper;
	
	private QueryParameterFactory<Attachment, Integer> attachmentFlowInstanceIDParameterFactory;
	
	private UserFlowInstanceModule userFlowInstanceModule;
	private FlowInstanceAdminModule flowInstanceAdminModule;
	
	private ModuleViewFragmentTransformer<ForegroundModuleDescriptor> viewFragmentTransformer;
	
	private FlowInstanceOverviewExtensionProviderWrapper userFlowInstanceWrapper = new FlowInstanceOverviewExtensionProviderWrapper(false, this);
	private FlowInstanceOverviewExtensionProviderWrapper managerFlowInstanceWrapper = new FlowInstanceOverviewExtensionProviderWrapper(true, this);
	
	@Override
	protected void moduleConfigured() throws Exception {
		
		super.moduleConfigured();
		
		ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.WARN);
		
		viewFragmentTransformer = new ModuleViewFragmentTransformer<ForegroundModuleDescriptor>(sectionInterface.getModuleXSLTCache(), this, systemInterface.getEncoding());
		viewFragmentTransformer.setDebugXML(debugFragmententXML);
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowInstanceAttachmentsModule.class.getName(), new XMLDBScriptProvider(FlowInstanceAttachmentsModule.class.getResourceAsStream("DB script.xml")));
		
		if (upgradeResult.isUpgrade()) {
			
			log.info(upgradeResult.toString());
		}
		
		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface);
		
		attachmentDAO = daoFactory.getDAO(Attachment.class);
		
		attachmentDAOWrapper = attachmentDAO.getWrapper(Integer.class);
		
		attachmentFlowInstanceIDParameterFactory = attachmentDAO.getParamFactory("flowInstanceID", Integer.class);
	}
	
	@Override
	public void unload() throws Exception {

		if(userFlowInstanceModule != null){
			
			setUserFlowInstanceModule(null);
		}
		
		if(flowInstanceAdminModule != null){
			
			setFlowInstanceAdminModule(null);
		}
		
		super.unload();
	}
	
	public void deleteAttachment(FlowInstance flowInstance, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, URINotFoundException, AccessDeniedException, IOException {
		
		Integer attachmentID;
		
		if (IntegerPopulator.getPopulator().validateFormat(req.getParameter("attachmentID")) && (attachmentID = Integer.parseInt(req.getParameter("attachmentID"))) != null) {
			
			Attachment attachment = attachmentDAOWrapper.get(attachmentID);
			
			if (attachment == null) {
				
				throw new URINotFoundException(uriParser);
			}
			
			Timestamp now = TimeUtils.getCurrentTimestamp();
			
			log.info("User " + user + " deleting attachment " + attachment + " from flowInstance " + flowInstance);
			
			attachmentDAO.delete(attachment);
			
			flowInstanceAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, attachmentsUpdatedMessage, user, now);
			systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(FlowInstance.class, CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
			
			flowInstanceAdminModule.redirectToMethod(req, res, "/overview/" + flowInstance.getFlowInstanceID() + "#attachments");
			return;
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	public void downloadAttachment(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstance flowInstance) throws SQLException, URINotFoundException, AccessDeniedException, IOException {
		
		Integer attachmentID;
		
		if (IntegerPopulator.getPopulator().validateFormat(req.getParameter("attachmentID")) && (attachmentID = Integer.parseInt(req.getParameter("attachmentID"))) != null) {
			
			Attachment attachment = attachmentDAOWrapper.get(attachmentID);
			
			if (attachment != null) {
				
				log.info("User " + user + " downloading attachment " + attachment + " from flowInstance " + flowInstance);
				
				InputStream in = null;
				OutputStream out = null;
				
				try {
					
					Blob data = attachment.getData();
					
					HTTPUtils.setContentLength(data.length(), res);
					
					res.setContentType(MimeUtils.getMimeType(attachment.getFilename()));
					res.setHeader("Content-Disposition", "inline; filename=\"" + FileUtils.toValidHttpFilename(attachment.getFilename()) + "\"");
					
					in = data.getBinaryStream();
					
					out = res.getOutputStream();
					
					StreamUtils.transfer(in, out);
					
				} catch (Exception e) {
					
					log.debug("Caught exception " + e + " while sending message attachment " + attachment.getFilename() + " to " + user);
					
				} finally {
					
					CloseUtils.close(in);
					CloseUtils.close(out);
				}
				
				return;
			}
		}
		
		throw new URINotFoundException(uriParser);
		
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {
		
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		
		doc.appendChild(document);
		
		return doc;
	}
	
	public String getTitlePrefix() {
		
		return moduleDescriptor.getName();
	}
	
	public List<Attachment> getAttachments(Integer flowInstanceID) throws SQLException {
		
		HighLevelQuery<Attachment> query = new HighLevelQuery<Attachment>();
		query.addParameter(attachmentFlowInstanceIDParameterFactory.getParameter(flowInstanceID));
		
		return attachmentDAO.getAll(query);
	}
	
	public Integer getAttachmentsCount(Integer flowInstanceID) throws SQLException {
		
		HighLevelQuery<Attachment> query = new HighLevelQuery<Attachment>();
		query.addParameter(attachmentFlowInstanceIDParameterFactory.getParameter(flowInstanceID));
		
		return attachmentDAO.getCount(query);
	}
	
	public SectionDescriptor getSectionDescriptor() {
		
		return sectionInterface.getSectionDescriptor();
	}
	
	@InstanceManagerDependency(required = true)
	public void setUserFlowInstanceModule(UserFlowInstanceModule userFlowInstanceModule) {
		
		if (userFlowInstanceModule != null) {
			
			userFlowInstanceModule.addFlowInstanceOverviewExtensionProvider(userFlowInstanceWrapper);
			
		} else {
			
			this.userFlowInstanceModule.removeFlowInstanceOverviewExtensionProvider(userFlowInstanceWrapper);
		}
		
		this.userFlowInstanceModule = userFlowInstanceModule;
	}
	
	@InstanceManagerDependency(required = true)
	public void setFlowInstanceAdminModule(FlowInstanceAdminModule flowInstanceAdminModule) {
		
		if (flowInstanceAdminModule != null) {
			
			flowInstanceAdminModule.addFlowInstanceOverviewExtensionProvider(managerFlowInstanceWrapper);
			
		} else {
			
			this.flowInstanceAdminModule.removeFlowInstanceOverviewExtensionProvider(managerFlowInstanceWrapper);
		}
		
		this.flowInstanceAdminModule = flowInstanceAdminModule;
	}
	
	public ExtensionLink getOverviewTabHeaderExtensionLink(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user) throws Exception {
		
		Integer attachmentCount = getAttachmentsCount(flowInstance.getFlowInstanceID());
		
		return new ExtensionLink(tabTitle + " (" + attachmentCount + ")", "#attachments", null, null);
	}
	
	public ViewFragment getOverviewTabContentsViewFragment(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user, boolean manager) throws Exception {
		
		return getTabContentsViewFragment(flowInstance, req, uriParser, user, null, manager);
	}
	
	public ForegroundModuleResponse processOverviewExtensionRequest(FlowInstance flowInstance, HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, boolean manager) throws Exception {
		
		String action = req.getParameter("action");
		
		if ("download".equals(action)) {
			
			downloadAttachment(req, res, user, uriParser, flowInstance);
			return null;
		}
		
		if (manager) {
			
			if(flowInstance.getStatus().getContentType().equals(ContentType.ARCHIVED)){
				
				throw new AccessDeniedException("Flow instance " + flowInstance + " is archived");
			}
			
			if (MultipartRequest.isMultipartRequest(req)) {
				
				addAttachment(req, res, user, uriParser, flowInstance);
				return null;
				
			} else if ("delete".equals(action)) {
				
				deleteAttachment(flowInstance, req, res, user, uriParser);
				return null;
			}
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	public ViewFragment getTabContentsViewFragment(FlowInstance flowInstance, HttpServletRequest req, URIParser uriParser, User user, List<ValidationError> validationErrors, boolean manager) throws SQLException, TransformerConfigurationException, TransformerException {
		
		Document doc = createDocument(req, uriParser, user);
		Element documentElement = doc.getDocumentElement();
		
		Element tabElement = XMLUtils.appendNewElement(doc, documentElement, "TabContents");
		
		XMLUtils.append(doc, tabElement, flowInstance);
		
		List<Attachment> attachments = getAttachments(flowInstance.getFlowInstanceID());
		
		if (!CollectionUtils.isEmpty(attachments)) {
			
			XMLUtils.append(doc, tabElement, "Attachments", attachments);
		}
		
		if (manager) {
			
			if (flowInstance.getStatus().getContentType() != ContentType.ARCHIVED) {
				XMLUtils.appendNewElement(doc, tabElement, "Manager");
			}
			
			XMLUtils.appendNewElement(doc, tabElement, "ModuleURL", uriParser.getContextPath() + flowInstanceAdminModule.getOverviewExtensionRequestMethodAlias(flowInstance, getOverviewExtensionProviderID()));
			
		} else {
			
			XMLUtils.appendNewElement(doc, tabElement, "ModuleURL", uriParser.getContextPath() + userFlowInstanceModule.getOverviewExtensionRequestMethodAlias(flowInstance, getOverviewExtensionProviderID()));
		}
		
		XMLUtils.appendNewElement(doc, tabElement, "FormatedMaxFileSize", BinarySizeFormater.getFormatedSize(getMaxFileSize() * BinarySizes.MegaByte));
		XMLUtils.appendNewElement(doc, tabElement, "TabTitle", tabTitle);
		
		XMLUtils.append(doc, tabElement, validationErrors);
		
		return viewFragmentTransformer.createViewFragment(doc);
	}
	
	public void addAttachment(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FlowInstance flowInstance) throws SQLException, URINotFoundException, AccessDeniedException, IOException, TransformerConfigurationException, TransformerException {
		
		log.info("User " + user + " adding attachment to flow instance " + flowInstance);
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		if (MultipartRequest.isMultipartRequest(req)) {
			
			MultipartRequest multipartRequest = null;
			
			try {
				multipartRequest = new MultipartRequest(getRamThreshold() * BinarySizes.KiloByte, getMaxRequestSize() * BinarySizes.MegaByte, getTempDir(), req);
				Timestamp now = TimeUtils.getCurrentTimestamp();
				
				List<Attachment> attachments = null;
				
				if (multipartRequest.getFileCount() > 0) {
					
					Collection<FileItem> files = multipartRequest.getFiles();
					
					Iterator<FileItem> fileIterator = files.iterator();
					
					while (fileIterator.hasNext()) {
						
						FileItem fileItem = fileIterator.next();
						
						if (StringUtils.isEmpty(fileItem.getName()) || fileItem.getSize() == 0) {
							
							fileIterator.remove();
							continue;
						}
						
						if (fileItem.getSize() > (getMaxFileSize() * BinarySizes.MegaByte)) {
							
							validationErrors.add(new FileSizeLimitExceededValidationError(URLStringConverterUtils.getUTF8StringDecoder().decode(FilenameUtils.getName(fileItem.getName())), fileItem.getSize(), getMaxFileSize() * BinarySizes.MegaByte));
							
							fileIterator.remove();
							continue;
						}
					}
					
					attachments = new ArrayList<Attachment>();
					
					for (FileItem fileItem : files) {
						
						Attachment attachment = new Attachment();
						
						String fileName = URLStringConverterUtils.getUTF8StringDecoder().decode((FilenameUtils.getName(fileItem.getName())));
						
						attachment.setFilename(fileName);
						attachment.setSize(fileItem.getSize());
						attachment.setData(new SerialBlob(fileItem.get()));
						attachment.setAdded(now);
						attachment.setPoster(user);
						attachment.setFlowInstanceID(flowInstance.getFlowInstanceID());
						
						attachments.add(attachment);
					}
				}
				
				if (!CollectionUtils.isEmpty(attachments)) {
					
					attachmentDAO.addAll(attachments, null);
					
					flowInstanceAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, EventType.OTHER_EVENT, attachmentsUpdatedMessage, user, now);
					systemInterface.getEventHandler().sendEvent(FlowInstance.class, new CRUDEvent<FlowInstance>(FlowInstance.class, CRUDAction.UPDATE, flowInstance), EventTarget.ALL);
					
					FlowFamililyNotificationSettings notificationSettings = flowNotificationHandler.getNotificationSettings(flowInstance.getFlow());
					
					if (!CollectionUtils.isEmpty(flowInstance.getOwners()) && (notificationSettings.isSendExternalMessageReceivedUserEmail() || notificationSettings.isSendExternalMessageReceivedUserSMS())) {
						
						Collection<Contact> contacts = flowNotificationHandler.getContactsFromDB(flowInstance);
						
						if (contacts != null) {
							
							for (Contact contact : contacts) {
								
								if (notificationSettings.isSendExternalMessageReceivedUserEmail()) {
									
									flowNotificationHandler.sendContactEmail(flowInstance, contact, newAttachmentsUserEmailSubject, newAttachmentsUserEmailMessage, null);
								}
								
								if (notificationSettings.isSendExternalMessageReceivedUserSMS()) {
									
									flowNotificationHandler.sendContactSMS(flowInstance, contact, newAttachmentsUserSMS);
								}
							}
						}
					}
					
				} else {
					
					validationErrors.add(new ValidationError("attachment", ValidationErrorType.RequiredField));
				}
				
			} catch (SizeLimitExceededException e) {
				
				validationErrors.add(new FileSizeLimitExceededValidationError(null, e.getActualSize(), e.getPermittedSize()));
				
			} catch (FileSizeLimitExceededException e) {
				
				validationErrors.add(new FileSizeLimitExceededValidationError(e.getFileName(), e.getActualSize(), e.getPermittedSize()));
				
			} catch (FileUploadException e) {
				
				validationErrors.add(new ValidationError("UnableToParseRequest"));
				
			} finally {
				
				if (multipartRequest != null) {
					multipartRequest.deleteFiles();
				}
			}
		}
		
		JsonObject jsonResponse = new JsonObject();
		
		if (validationErrors.isEmpty()) {
			
			jsonResponse.putField("response", getTabContentsViewFragment(flowInstance, req, uriParser, user, validationErrors, true).getHTML());
			
		} else {
			
			jsonResponse.putField("errors", getValidationErrorsViewFragment(req, uriParser, user, validationErrors).getHTML());
		}
		
		HTTPUtils.sendReponse(jsonResponse.toJson(), JsonUtils.getContentType(), res);
	}
	
	public ViewFragment getValidationErrorsViewFragment(HttpServletRequest req, URIParser uriParser, User user, List<ValidationError> validationErrors) throws TransformerConfigurationException, TransformerException {
		
		Document doc = createDocument(req, uriParser, user);
		Element documentElement = doc.getDocumentElement();
		
		XMLUtils.append(doc, documentElement, "ValidationErrors", validationErrors);
		
		return viewFragmentTransformer.createViewFragment(doc);
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
	
	public int getRamThreshold() {
		return ramThreshold;
	}
	
	public long getMaxRequestSize() {
		return maxRequestSize;
	}
	
	public long getMaxFileSize() {
		return maxFileSize;
	}
	
	public String getTempDir() {
		return tempDir;
	}
	
	public String getOverviewExtensionProviderID() {
		return moduleDescriptor.getModuleID().toString();
	}
}
