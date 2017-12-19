package com.nordicpeak.flowengine.sharing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.ServerStartupListener;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkProvider;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.datatypes.SimpleEntry;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.populators.NonNegativeStringIntegerPopulator;
import se.unlogic.standardutils.readwrite.ReadWriteUtils;
import se.unlogic.standardutils.settings.SettingNode;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SimpleRequest;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.interfaces.FlowAdminShowFlowExtensionLinkProvider;
import com.nordicpeak.flowengine.sharing.beans.RepositoryConfiguration;
import com.nordicpeak.flowengine.sharing.validators.RepositoryConfigurationValidator;

public class FlowCatalogModule extends AnnotatedForegroundModule implements ExtensionLinkProvider, FlowAdminShowFlowExtensionLinkProvider, Runnable {
	
	
	protected static final List<Field> FLOW_IGNORED_FIELDS = Arrays.asList(FlowType.ALLOWED_ADMIN_GROUPS_RELATION, FlowType.ALLOWED_GROUPS_RELATION, FlowType.ALLOWED_QUERIES_RELATION, FlowType.ALLOWED_ADMIN_USERS_RELATION, FlowType.ALLOWED_USERS_RELATION, FlowType.CATEGORIES_RELATION, Flow.STATUSES_RELATION, Flow.DEFAULT_FLOW_STATE_MAPPINGS_RELATION, Flow.STEPS_RELATION, Flow.TAGS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION);
	
	protected static final NonNegativeStringIntegerPopulator NON_NEGATIVE_STRING_INTEGER_POPULATOR = new NonNegativeStringIntegerPopulator();
	
	protected static final ValidationError FLOW_FAMILY_NOT_FOUND_VALIDATION_ERROR = new ValidationError("FlowFamilyNotFound");
	protected static final ValidationError FLOW_NOT_FOUND_VALIDATION_ERROR = new ValidationError("FlowNotFound");
	protected static final ValidationError ACCESS_DENIED_VALIDATION_ERROR = new ValidationError("AccessDenied");
	protected static final ValidationError UNKNOWN_REMOTE_ERROR_VALIDATION_ERROR = new ValidationError("UnknownRemoteError");
	protected static final ValidationError REPOSITORY_COMMUNICATION_FAILED_VALIDATION_ERROR = new ValidationError("RepositoryCommunicationFailed");
	protected static final ValidationError ERROR_EXPORTING_FLOW_VALIDATION_ERROR = new ValidationError("ErrorExportingFlow");
	
	@XSLVariable(prefix = "java.")
	protected String shareFlowTitle;
	
	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Repositories", description = "http|https://url:username:password", formatValidator = RepositoryConfigurationValidator.class)
	protected List<String> repositoriesSettings;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Connection Timeout", description = "Connection timeout in seconds", formatValidator = StringIntegerValidator.class, required = true)
	protected Integer connectionTimeout = 5;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Read Timeout", description = "Read timeout in seconds", formatValidator = StringIntegerValidator.class, required = true)
	protected Integer readTimeout = 10;
	
	protected FlowAdminModule flowAdminModule;
	protected StaticContentModule staticContentModule;
	
	private List<RepositoryConfiguration> repositories = new ArrayList<RepositoryConfiguration>();
	
	protected ExtensionLink flowListExtensionLink;
	protected ExtensionLink flowShowExtensionLink;
	
	private Thread cachingThread;
	private boolean stopCacheThread;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		super.init(moduleDescriptor, sectionInterface, dataSource);
	}
	
	@Override
	protected void moduleConfigured() throws Exception {
		
		stopCacheThread();
		
		repositories.clear();
		
		if (ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.ERROR)) {
			
			if (!CollectionUtils.isEmpty(repositoriesSettings)) {
				for (String configRow : repositoriesSettings) {
					
					String[] splits = configRow.split("(?<!https?):");
					
					if (splits.length == 3) {
						
						String url = splits[0];
						String username = splits[1];
						String password = splits[2];
						
						repositories.add(new RepositoryConfiguration(url.replace("|", ":"), username, password));
						
					} else {
						
						log.warn("Incorrect format of config row \"" + configRow + "\"");
					}
				}
				
				cacheRepositoryInfo();
			}
			
			generateExtensionLinks(staticContentModule);
		}
		
		super.moduleConfigured();
	}
	
	@ServerStartupListener
	public void serverStarted() {
		
		cacheRepositoryInfo();
	}
	
	private void cacheRepositoryInfo() {
		
		stopCacheThread();
		stopCacheThread = false;
		
		cachingThread = new Thread(this);
		
		cachingThread.start();
	}
	
	private void stopCacheThread() {
		
		if (cachingThread != null) {
			
			stopCacheThread = true;
			
			try {
				cachingThread.interrupt();
				cachingThread.join();
			} catch (InterruptedException e) {}
		}
	}
	
	@Override
	public void unload() throws Exception {
		
		if (flowAdminModule != null) {
			
			flowAdminModule.removeFlowListExtensionLinkProvider(this);
			flowAdminModule.removeFlowShowExtensionLinkProvider(this);
		}
		
		stopCacheThread();
		
		super.unload();
	}
	
	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		return listRepositories(req, res, user, uriParser, null);
	}
	
	protected ForegroundModuleResponse listRepositories(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws ModuleConfigurationException, SQLException {
		
		log.info("User " + user + " listing repositories");
		
		Document doc = createDocument(req, uriParser, user);
		
		Element listFamiliesElement = doc.createElement("ListRepositories");
		doc.getDocumentElement().appendChild(listFamiliesElement);
		
		Element repositoriesElement = XMLUtils.appendNewElement(doc, listFamiliesElement, "Repositories");
		
		for (int i = 0; i < repositories.size(); i++) {
			
			Element repositoryElement = XMLUtils.appendNewElement(doc, repositoriesElement, "Repository");
			XMLUtils.appendNewElement(doc, repositoryElement, "RepositoryIndex", i);
			
			RepositoryConfiguration repo = repositories.get(i);
			fetchRepositoryInfo(repo);
			
			repositoryElement.appendChild(repo.toXML(doc));
		}
		
		if (validationErrors != null) {
			
			XMLUtils.append(doc, listFamiliesElement, "ValidationErrors", validationErrors);
		}
		
		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
	}
	
	@WebPublic(alias = "flows")
	public ForegroundModuleResponse getFlowData(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		log.info("Sending shared flows to user " + user);
		
		res.setCharacterEncoding("ISO-8859-1");
		res.setContentType(JsonUtils.getContentType());
		
		PrintWriter writer = res.getWriter();
		
		writer.append("{\"rows\":[");
		
		boolean first = true;;
		JsonArray row = new JsonArray(8);
		
		for (int i = 0; i < repositories.size(); i++) {
			
			RepositoryConfiguration repo = repositories.get(i);
			
			Document response = sendGetRequest(repo, "flows");
			
			if (response != null) {
				
				XMLParser responseParser = new XMLParser(response);
				List<XMLParser> sharedFlows = responseParser.getNodes("SharedFlows/SharedFlow", true);
				
				for (SettingNode sharedFlow : sharedFlows) {

					if (!first) {
						
						writer.append(",");
						
					} else {
						
						first = false;
					}
					
					row.addNode(sharedFlow.getString("name"));
					row.addNode(sharedFlow.getString("Source/name"));
					row.addNode(repo.getName());
					row.addNode(sharedFlow.getString("familySize"));
					row.addNode(sharedFlow.getString("added"));
					row.addNode(i);
					row.addNode(sharedFlow.getString("Source/sourceID"));
					row.addNode(sharedFlow.getString("flowFamilyID"));
					
					writer.append(row.toJson());
					row.clearNodes();
				}
				
			} else {
				
//				XMLUtils.appendNewElement(doc, repositoryElement, "Missing");
			}
		}
		
		writer.append("]}");
		writer.flush();
		
//		log.info("Sending orders took " + TimeUtils.millisecondsToString(System.currentTimeMillis() - start));
		return null;
	}
	
	@WebPublic(alias = "family")
	public ForegroundModuleResponse listFlowVersions(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Integer repositoryIndex;
		Integer sourceID;
		Integer flowFamilyID;
		
		if (uriParser.size() == 5 && (repositoryIndex = uriParser.getInt(2)) != null && (sourceID = uriParser.getInt(3)) != null && (flowFamilyID = uriParser.getInt(4)) != null) {
			
			return listFlowVersions(req, res, user, uriParser, repositoryIndex, sourceID, flowFamilyID, null);
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	protected ForegroundModuleResponse listFlowVersions(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, Integer repositoryIndex, Integer sourceID, Integer flowFamilyID, ValidationError validationError) throws Throwable {
		
		if (repositoryIndex >= 0 && repositoryIndex < repositories.size()) {
			
			log.info("User " + user + " listing flow versions in flow family ID " + flowFamilyID);
			
			List<ValidationError> validationErrors = new ArrayList<ValidationError>();
			
			RepositoryConfiguration repo = repositories.get(repositoryIndex);
			
			Document response = sendGetRequest(repo, "family/" + sourceID + "/" + flowFamilyID);
			
			if (response != null) {
				
				SettingNode responseParser = new XMLParser(response);
				String status = responseParser.getString("/Response/Status");
				
				if ("OK".equals(status)) {
					
					Document doc = createDocument(req, uriParser, user);
					
					Element listVersionsElement = doc.createElement("ListFlowVersions");
					doc.getDocumentElement().appendChild(listVersionsElement);
					
					XMLUtils.appendNewElement(doc, listVersionsElement, "RepositoryIndex", repositoryIndex);
					
					copyChildrenToOtherDocument(doc, response.getDocumentElement(), listVersionsElement);
					
					return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
					
				} else if ("NotFound".equals(status)) {
					
					validationErrors.add(FLOW_FAMILY_NOT_FOUND_VALIDATION_ERROR);
					
				} else {
					
					log.warn("Unknown status received: " + status);
					validationErrors.add(UNKNOWN_REMOTE_ERROR_VALIDATION_ERROR);
				}
				
			} else {
				
				validationErrors.add(REPOSITORY_COMMUNICATION_FAILED_VALIDATION_ERROR);
			}
			
			return listRepositories(req, res, user, uriParser, validationErrors);
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	@WebPublic(alias = "download")
	public ForegroundModuleResponse downloadFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Integer repositoryIndex;
		Integer sharedflowID;
		
		if (uriParser.size() == 4 && (repositoryIndex = uriParser.getInt(2)) != null && (sharedflowID = uriParser.getInt(3)) != null && repositoryIndex >= 0 && repositoryIndex < repositories.size()) {
			
			RepositoryConfiguration repository = repositories.get(repositoryIndex);
			
			log.info("User " + user + " downloading flow with sharedflowID " + sharedflowID);
			
			HttpURLConnection connection = null;
			
			InputStream inputStream = null;
			
			try {
				
				if (repository.getUrl().startsWith("http")) {
					
					connection = HTTPUtils.getHttpURLConnection(repository.getUrl() + "/download/" + sharedflowID, null);
					
				} else {
					
					connection = HTTPUtils.getHttpsURLConnection(repository.getUrl() + "/download/" + sharedflowID, null);
				}
				
				if (repository.getUsername() != null && repository.getPassword() != null) {
					
					HTTPUtils.setBasicAuthentication(connection, repository.getUsername(), repository.getPassword());
				}
				
				if (connection.getErrorStream() != null) {
					
					inputStream = connection.getErrorStream();
					
				} else {
					
					inputStream = connection.getInputStream();
				}
				
				if (connection.getResponseCode() == HttpServletResponse.SC_OK) {
					
					String contentDisposition = connection.getHeaderField("Content-Disposition");
					String filename = URLDecoder.decode(contentDisposition.substring(contentDisposition.indexOf("filename*=UTF-8''") + "filename*=UTF-8''".length()), "UTF-8");
					
					HTTPUtils.sendFile(inputStream, filename, null, null, req, res, ContentDisposition.INLINE, null);
					return null;
					
				} else {
					
					InputStreamReader reader = null;
					StringWriter stringWriter = new StringWriter();
					
					try {
						reader = new InputStreamReader(inputStream);
						ReadWriteUtils.transfer(reader, stringWriter);
						
					} finally {
						ReadWriteUtils.closeReader(reader);
					}
					
					Document response = XMLUtils.parseXML(stringWriter.toString(), false, false);
					
					Document doc = createDocument(req, uriParser, user);
					
					Element errorElement = doc.createElement("DownloadError");
					doc.getDocumentElement().appendChild(errorElement);
					
					XMLUtils.appendNewElement(doc, errorElement, "RepositoryIndex", repositoryIndex);
					
					copyChildrenToOtherDocument(doc, response.getDocumentElement(), errorElement);
					
					return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
				}
				
			} catch (FileNotFoundException e) {
				
				throw new URINotFoundException(uriParser);
				
			} finally {
				
				CloseUtils.close(inputStream);
				
				if (connection != null) {
					connection.disconnect();
				}
			}
			
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	@WebPublic(alias = "import")
	public ForegroundModuleResponse importFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Integer repositoryIndex;
		Integer sharedflowID;
		
		if (uriParser.size() == 4 && (repositoryIndex = uriParser.getInt(2)) != null && (sharedflowID = uriParser.getInt(3)) != null && repositoryIndex >= 0 && repositoryIndex < repositories.size()) {
			
			RepositoryConfiguration repository = repositories.get(repositoryIndex);
			
			log.info("User " + user + " importing flow with sharedflowID " + sharedflowID);
			
			HttpURLConnection connection = null;
			
			InputStream inputStream = null;
			
			try {
				
				if (repository.getUrl().startsWith("http")) {
					
					connection = HTTPUtils.getHttpURLConnection(repository.getUrl() + "/download/" + sharedflowID, null);
					
				} else {
					
					connection = HTTPUtils.getHttpsURLConnection(repository.getUrl() + "/download/" + sharedflowID, null);
				}
				
				if (repository.getUsername() != null && repository.getPassword() != null) {
					
					HTTPUtils.setBasicAuthentication(connection, repository.getUsername(), repository.getPassword());
				}
				
				if (connection.getErrorStream() != null) {
					inputStream = connection.getErrorStream();
					
				} else {
					inputStream = connection.getInputStream();
				}
				
				if (connection.getResponseCode() == HttpServletResponse.SC_OK) {
					
					String contentDisposition = connection.getHeaderField("Content-Disposition");
					String filename = URLDecoder.decode(contentDisposition.substring(contentDisposition.indexOf("filename*=UTF-8''") + "filename*=UTF-8''".length()), "UTF-8");
					
					user.getSession().setAttribute("FlowImportFileName", filename);
					
					ByteArrayOutputStream buffer = new ByteArrayOutputStream(inputStream.available());
					
					StreamUtils.transfer(inputStream, buffer);
					user.getSession().setAttribute("FlowImportFile", buffer.toByteArray());
					
					res.sendRedirect(req.getContextPath() + flowAdminModule.getFullAlias() + "/importflow");
					return null;
					
				} else {
					
					InputStreamReader reader = null;
					StringWriter stringWriter = new StringWriter();
					
					try {
						reader = new InputStreamReader(inputStream);
						ReadWriteUtils.transfer(reader, stringWriter);
						
					} finally {
						ReadWriteUtils.closeReader(reader);
					}
					
					Document response = XMLUtils.parseXML(stringWriter.toString(), false, false);
					
					Document doc = createDocument(req, uriParser, user);
					
					Element errorElement = doc.createElement("DownloadError");
					doc.getDocumentElement().appendChild(errorElement);
					
					XMLUtils.appendNewElement(doc, errorElement, "RepositoryIndex", repositoryIndex);
					
					copyChildrenToOtherDocument(doc, response.getDocumentElement(), errorElement);
					
					return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
				}
				
			} catch (FileNotFoundException e) {
				
				throw new URINotFoundException(uriParser);
				
			} finally {
				
				CloseUtils.close(inputStream);
				
				if (connection != null) {
					connection.disconnect();
				}
			}
			
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	@WebPublic(alias = "delete")
	public ForegroundModuleResponse deleteFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Integer repositoryIndex;
		Integer sharedflowID;
		
		if (uriParser.size() == 4 && (repositoryIndex = uriParser.getInt(2)) != null && (sharedflowID = uriParser.getInt(3)) != null && repositoryIndex >= 0 && repositoryIndex < repositories.size()) {
			
			log.info("User " + user + " deleting flow with sharedflowID " + sharedflowID);
			
			List<ValidationError> validationErrors = new ArrayList<ValidationError>();
			
			RepositoryConfiguration repo = repositories.get(repositoryIndex);
			
			Document response = sendGetRequest(repo, "delete/" + sharedflowID);
			
			if (response != null) {
				
				SettingNode responseParser = new XMLParser(response);
				String status = responseParser.getString("/Response/Status");
				
				if ("Deleted".equals(status)) {
					
					Integer familySize = responseParser.getInteger("/Response/SharedFlow/familySize");
					
					if (familySize > 0) {
						redirectToMethod(req, res, "/family/" + repositoryIndex + "/" + responseParser.getInteger("/Response/SharedFlow/Source/sourceID") + "/" + responseParser.getInteger("/Response/SharedFlow/flowFamilyID"));
						
					} else {
						
						redirectToDefaultMethod(req, res);
					}
					
					return null;
					
				} else if ("NotFound".equals(status)) {
					
					validationErrors.add(FLOW_NOT_FOUND_VALIDATION_ERROR);
					
				} else if ("AccessDenied".equals(status)) {
					
					validationErrors.add(ACCESS_DENIED_VALIDATION_ERROR);
					
				} else {
					
					log.warn("Unknown status received: " + status);
					validationErrors.add(UNKNOWN_REMOTE_ERROR_VALIDATION_ERROR);
				}
				
			} else {
				
				validationErrors.add(REPOSITORY_COMMUNICATION_FAILED_VALIDATION_ERROR);
			}
			
			return listRepositories(req, res, user, uriParser, validationErrors);
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	protected ForegroundModuleResponse showShareFlowForm(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors, Flow flow) throws ModuleConfigurationException, SQLException {
		
		log.info("User " + user + " requesting share flow form for flow " + flow);
		
		Document doc = createDocument(req, uriParser, user);
		
		Element shareFlowElement = doc.createElement("ShareFlow");
		doc.getDocumentElement().appendChild(shareFlowElement);
		
		XMLGeneratorDocument generatorDocument = new XMLGeneratorDocument(doc);
		generatorDocument.setIgnoredFields(FLOW_IGNORED_FIELDS);
		
		shareFlowElement.appendChild(flow.toXML(generatorDocument));
		
		Element repositoriesElement = XMLUtils.appendNewElement(doc, shareFlowElement, "Repositories");
		
		for (int i = 0; i < repositories.size(); i++) {
			
			RepositoryConfiguration repo = repositories.get(i);
			
			Document response = sendGetRequest(repo, "info");
			
			if (response != null) {
				
				SettingNode responseParser = new XMLParser(response);
				
				updateRepositoryInfo(responseParser, repo);
				
				if (responseParser.getNode("/Response/UploadAccess") != null) {
					
					Element repositoryElement = XMLUtils.appendNewElement(doc, repositoriesElement, "Repository");
					XMLUtils.appendNewElement(doc, repositoryElement, "RepositoryIndex", i);
					
					copyChildrenToOtherDocument(doc, response.getDocumentElement(), repositoryElement);
				}
				
			} else {
				
				Element repositoryElement = XMLUtils.appendNewElement(doc, shareFlowElement, "MissingRepository");
				repositoryElement.appendChild(repo.toXML(doc));
			}
		}
		
		if (validationErrors != null) {
			
			XMLUtils.append(doc, shareFlowElement, "ValidationErrors", validationErrors);
		}
		
		shareFlowElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		
		return new SimpleForegroundModuleResponse(doc, this.getDefaultBreadcrumb());
	}
	
	@WebPublic(alias = "share")
	public ForegroundModuleResponse shareFlow(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		Flow flow = null;
		
		if (uriParser.size() == 3 && (flow = flowAdminModule.getRequestedFlow(req, user, uriParser)) != null) {
			
			if (!AccessUtils.checkAccess(user, flow.getFlowType().getAdminAccessInterface()) && !AccessUtils.checkAccess(user, flowAdminModule)) {
				
				throw new AccessDeniedException("User does not have access to " + flow + " in flow type " + flow.getFlowType());
			}
			
			if (req.getMethod().equalsIgnoreCase("POST")) {
				
				List<ValidationError> validationErrors = new ArrayList<ValidationError>();
				
				Integer repositoryIndex = ValidationUtils.validateParameter("repositoryIndex", req, true, 0, Integer.toString(repositories.size()).length(), NON_NEGATIVE_STRING_INTEGER_POPULATOR, validationErrors);
				
				if (validationErrors.isEmpty()) {
					
					RepositoryConfiguration repo = repositories.get(repositoryIndex);
					fetchRepositoryInfo(repo);
					
					String comment = ValidationUtils.validateParameter("comment", req, false, 0, 65535, validationErrors);
					
					if (validationErrors.isEmpty()) {
						
						Document exportDoc = flowAdminModule.getExportFlowDocument(flow, validationErrors);
						
						if (validationErrors.isEmpty()) {
							
							log.info("User " + user + " sharing flow " + flow + " to repository " + repo);
							
							List<Entry<String, String>> requestParameters = new ArrayList<Entry<String, String>>();
							
							requestParameters.add(new SimpleEntry<String, String>("flowFamilyID", flow.getFlowFamily().getFlowFamilyID().toString()));
							requestParameters.add(new SimpleEntry<String, String>("flowID", flow.getFlowID().toString()));
							requestParameters.add(new SimpleEntry<String, String>("version", flow.getVersion().toString()));
							requestParameters.add(new SimpleEntry<String, String>("name", URLEncoder.encode(flow.getName(), systemInterface.getEncoding())));
							
							if (!StringUtils.isEmpty(comment)) {
								requestParameters.add(new SimpleEntry<String, String>("comment", URLEncoder.encode(comment, systemInterface.getEncoding())));
							}
							
							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							XMLUtils.writeXML(exportDoc, byteArrayOutputStream, true, systemInterface.getEncoding());
							
							Document response = sendPostRequest(repo, "add", requestParameters, new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
							
							if (response != null) {
								
								SettingNode responseParser = new XMLParser(response);
								SettingNode responseErrors = responseParser.getNode("/Response/ValidationErrors");
								
								if (responseErrors == null) {
									
									redirectToMethod(req, res, "/family/" + repositoryIndex + "/" + responseParser.getInteger("/Response/SharedFlow/Source/sourceID") + "/" + responseParser.getInteger("/Response/SharedFlow/flowFamilyID"));
									return null;
									
								} else {
									
									List<? extends SettingNode> errors = responseErrors.getNodes("validationError");
									
									for (SettingNode error : errors) {
										
										String messageKey = error.getString("messageKey");
										String displayName = error.getString("displayName");
										String fieldName = error.getString("fieldName");
										String validationErrorType = error.getString("validationErrorType");
										
										if (!StringUtils.isEmpty(validationErrorType) && !StringUtils.isEmpty(messageKey)) {
											
											validationErrors.add(new ValidationError(fieldName, ValidationErrorType.valueOf(validationErrorType), messageKey));
											
										} else if (!StringUtils.isEmpty(messageKey)) {
											
											validationErrors.add(new ValidationError(messageKey, displayName, fieldName));
											
										} else {
											
											validationErrors.add(new ValidationError(fieldName, displayName, ValidationErrorType.valueOf(validationErrorType)));
										}
									}
								}
								
							} else {
								
								validationErrors.add(REPOSITORY_COMMUNICATION_FAILED_VALIDATION_ERROR);
							}
							
						} else {
							
							validationErrors.add(ERROR_EXPORTING_FLOW_VALIDATION_ERROR);
						}
					}
				}
				
				return showShareFlowForm(req, res, user, uriParser, validationErrors, flow);
				
			} else {
				
				return showShareFlowForm(req, res, user, uriParser, null, flow);
			}
		}
		
		throw new URINotFoundException(uriParser);
		
	}
	
	private Document sendGetRequest(RepositoryConfiguration repository, String method) {
		
		try {
			String response = HTTPUtils.sendHTTPGetRequest(repository.getUrl() + "/" + method, null, null, repository.getUsername(), repository.getPassword(), connectionTimeout * MillisecondTimeUnits.SECOND, readTimeout * MillisecondTimeUnits.SECOND);
			
			return XMLUtils.parseXML(response, false, false);
			
		} catch (Exception e) {
			
			log.warn("Error communicating with repository " + repository, e);
			return null;
		}
	}
	
	private Document sendPostRequest(RepositoryConfiguration repository, String method, List<Entry<String, String>> requestParameters, ByteArrayInputStream outputData) {
		
		try {
			SimpleRequest request = new SimpleRequest(repository.getUrl() + "/" + method);
			request.setConnectionTimeout(connectionTimeout * MillisecondTimeUnits.SECOND);
			request.setReadTimeout(readTimeout * MillisecondTimeUnits.SECOND);
			request.setRequestParameters(requestParameters);
			
			List<Entry<String, String>> headerEntries = new ArrayList<Entry<String, String>>(2);
			
			headerEntries.add(new SimpleEntry<String, String>("Content-Type", "text/xml; charset=" + systemInterface.getEncoding()));
			headerEntries.add(new SimpleEntry<String, String>("Content-Length", Long.toString(outputData.available())));
			
			request.setHeaders(headerEntries);
			
			if (repository.getUsername() != null && repository.getPassword() != null) {
				
				request.setUsername(repository.getUsername());
				request.setPassword(repository.getPassword());
			}
			
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			
			HTTPUtils.sendHTTPPostRequest(request, outputData, byteArrayOutputStream);
			
			return XMLUtils.parseXML(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), false, false);
			
		} catch (Exception e) {
			
			log.warn("Error communicating with repository " + repository, e);
			return null;
		}
	}
	
	private void fetchRepositoryInfo(RepositoryConfiguration repo) {
		
		synchronized(repo) {
			
			if (repo.getLastUpdate() == null || System.currentTimeMillis() - repo.getLastUpdate() > MillisecondTimeUnits.HOUR) {
				
				Document response = sendGetRequest(repo, "info");
				
				if (response != null) {
					
					SettingNode responseParser = new XMLParser(response);
					updateRepositoryInfo(responseParser, repo);
				}
			}
		}
	}
	
	private void updateRepositoryInfo(SettingNode responseParser, RepositoryConfiguration repo) {
		
		synchronized(repo) {
			
			String repoName = responseParser.getString("/Response/Name");
			
			if (!StringUtils.isEmpty(repoName)) {
				
				repo.setName(repoName);
				repo.setDescription(responseParser.getString("/Response/Description"));
				repo.setUploadDescription(responseParser.getString("/Response/UploadDescription"));
				repo.setLastUpdate(System.currentTimeMillis());
			}
		}
	}
	
	protected void copyChildrenToOtherDocument(Document targetDocument, Element sourceElement, Element targetElement) {
		
		NodeList nodes = sourceElement.getChildNodes();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			targetElement.appendChild(targetDocument.importNode(node, true));
		}
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {
		
		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));
		
		doc.appendChild(documentElement);
		return doc;
	}
	
	@InstanceManagerDependency(required = true)
	public void setStaticContentModule(StaticContentModule staticContentModule) {
		
		generateExtensionLinks(staticContentModule);
		
		this.staticContentModule = staticContentModule;
	}
	
	@InstanceManagerDependency
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) {
		
		if (flowAdminModule != null) {
			
			flowAdminModule.addFlowListExtensionLinkProvider(this);
			flowAdminModule.addFlowShowExtensionLinkProvider(this);
			
		} else if (this.flowAdminModule != null) {
			
			this.flowAdminModule.removeFlowListExtensionLinkProvider(this);
			this.flowAdminModule.removeFlowShowExtensionLinkProvider(this);
		}
		
		this.flowAdminModule = flowAdminModule;
	}
	
	private void generateExtensionLinks(StaticContentModule staticContentModule) {
		
		if (staticContentModule != null) {
			
			flowListExtensionLink = new ExtensionLink(moduleDescriptor.getName(), systemInterface.getContextPath() + this.getFullAlias(), staticContentModule.getModuleContentURL(moduleDescriptor) + "/pics/database.png", "bottom-right");
			flowShowExtensionLink = new ExtensionLink(shareFlowTitle, systemInterface.getContextPath() + this.getFullAlias() + "/share", staticContentModule.getModuleContentURL(moduleDescriptor) + "/pics/share.png", "top-right");
			
		} else {
			
			flowListExtensionLink = null;
			flowShowExtensionLink = null;
		}
	}
	
	@Override
	public ExtensionLink getExtensionLink(User user) {
		
		if (hasRequiredDependencies) {
			
			return flowListExtensionLink;
		}
		
		return null;
	}
	
	@Override
	public ExtensionLink getShowFlowExtensionLink(User user) {
		
		if (hasRequiredDependencies) {
			
			return flowShowExtensionLink;
		}
		
		return null;
	}
	
	@Override
	public AccessInterface getAccessInterface() {
		
		return moduleDescriptor;
	}
	
	@Override
	public void run() {
		
		try {
			if (!CollectionUtils.isEmpty(repositories)) {
				
				long startTime = System.currentTimeMillis();
				
				for (RepositoryConfiguration repo : repositories) {
					
					if (systemInterface.getSystemStatus() != SystemStatus.STARTED || stopCacheThread) {
						return;
					}
					
					fetchRepositoryInfo(repo);
				}
				
				log.info("Cached information for " + repositories.size() + " repositories in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");
			}
			
		} catch (Throwable t) {
			log.warn("Error caching repository information", t);
		}
	}
	
}
