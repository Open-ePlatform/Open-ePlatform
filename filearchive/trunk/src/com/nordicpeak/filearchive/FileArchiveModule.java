package com.nordicpeak.filearchive;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.filearchive.beans.File;
import com.nordicpeak.filearchive.beans.FileCollection;
import com.nordicpeak.filearchive.cruds.FileCollectionCRUD;
import com.nordicpeak.filearchive.enums.AccessMode;
import com.nordicpeak.filearchive.enums.SecurityMode;

import it.sauronsoftware.cron4j.Scheduler;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.GroupMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.UserMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleAccessInterface;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.settings.MutableSettingHandler;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.GenericCRUD;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.usergrouplist.UserGroupListConnector;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.date.PooledSimpleDateFormat;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.SwedishSocialSecurityPopulator;
import se.unlogic.standardutils.random.RandomUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.zip.ZipUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;


public class FileArchiveModule extends AnnotatedForegroundModule implements CRUDCallback<User>, Runnable, SystemStartupListener {

	private static final PooledSimpleDateFormat LAST_MODIFIED_DATE_FORMATTER = new PooledSimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH,TimeZone.getTimeZone("GMT"));
	private static final SwedishSocialSecurityPopulator SOCIAL_SECURITY_POPULATOR = new SwedishSocialSecurityPopulator(false, true, false, true);

	@XSLVariable(prefix="java.")
	protected String addFileCollectionBreadCrumbText = "Add file collection";

	@XSLVariable(prefix="java.")
	protected String updateFileCollectionBreadCrumbText = "Update ";

	@XSLVariable(prefix="java.")
	protected String unnamedFileCollectionText = "Unnamed file collection";

	@ModuleSetting
	@EnumDropDownSettingDescriptor(name="Access mode",description="Controls which access mode should be used when checking access to files and file collections.",required=true)
	protected AccessMode accessMode = AccessMode.PUBLIC;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Hide group access", description = "Hide group access when no groups are imported")
	protected boolean hideGroupAccess = false;
	
	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name = "Searchable user groups", description="Groups to search for users in")
	protected List<Integer> searchableUserGroupIDs;

	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name="Admin groups",description="Groups allowed to administrate this module")
	protected List<Integer> adminGroupIDs;

	@ModuleSetting(allowsNull = true)
	@UserMultiListSettingDescriptor(name="Admin users",description="Users allowed to administrate this module")
	protected List<Integer> adminUserIDs;

	@ModuleSetting(allowsNull = true)
	@GroupMultiListSettingDescriptor(name="Upload groups",description="Groups allowed to create file collections and upload files")
	protected List<Integer> uploadGroupIDs;

	@ModuleSetting(allowsNull = true)
	@UserMultiListSettingDescriptor(name="Upload users",description="Users allowed to create file collections and upload files")
	protected List<Integer> uploadUserIDs;

	@ModuleSetting
	@TextFieldSettingDescriptor(name="File store",description="Directory where uploaded files are stored",required=true)
	protected String fileStore;

	@ModuleSetting(allowsNull=true)
	@TextFieldSettingDescriptor(name="Temp dir",description="Directory for temporary files. Should be on the same filesystem as the file store for best performance. If not set system default temp directory will be used")
	protected String tempDir;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max upload size", description = "Maxmium upload size in megabytes allowed in a single post request", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxRequestSize = 200;

	@ModuleSetting
	@TextFieldSettingDescriptor(name="RAM threshold",description="Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.",required=true,formatValidator=PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Display file collection URL",description="Display a link with the URL to the file collection i the show file collection view")
	protected boolean showFileCollectionURL = true;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name="Delete old files", description="Automatically delete files after given amount of days")
	protected boolean deleteOldFiles = false;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow secure collections", description = "Allow secure file collections with check on citizenIdentifier")
	protected boolean allowSecureCollections = false;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow collaboration collections", description = "Allow collaboration file collections")
	protected boolean allowCollabCollections = false;
	
	@ModuleSetting
	@TextAreaSettingDescriptor(name = "Secure collab message", description  = "Info message for showing which conditions are needed to allow secure collaboration")
	protected String allowSecureCollabMessage = "Om du kräver säker inloggning, inloggning för en medarbetare eller har valt att kryptera filsamlingen så kan du också tillåta samarbete i filsamlingen. Detta innebär att personer med åtkomst till filsamlingen har möjlighet att ladda upp fler filer.";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Start time expression", description = "Cron expression for when to run deletion", required = true)
	protected String cronExp = "0 0 * * *";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Keep files (days)", description = "Days to keep files", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected int keepFilesDays = 14;
	
	@ModuleSetting(allowsNull = true)
	@HTMLEditorSettingDescriptor(name = "List collections info text", description = "Information text to be shown above list collections")
	protected String listFileCollectionsInfo;
	
	@ModuleSetting(allowsNull = true)
	@HTMLEditorSettingDescriptor(name = "Show file collection info text", description = "Information text to be shown in show collection view")
	protected String fileCollectionText;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow file encryption", description = "Allow file encryption")
	protected boolean allowFileEncryption = false;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Encryption password minimum length", description = "Minimum length for encryption password")
	protected int encryptionPasswordLength = 16;
	
	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "File encryption information text", description = "Information text to be shown with file encryption option")
	protected String fileEncryptionText;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow user to set keep days", description = "Allow users to set keep files days")
	private boolean allowUserKeepDays = false;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "User keep days max", description = "Maximum days allowed for user to set on a file collection", formatValidator = PositiveStringIntegerValidator.class)
	private int userKeepDaysMax;
	
	@ModuleSetting(allowsNull = true)
	@HTMLEditorSettingDescriptor(name = "User keep days info text", description = "Information text to be shown when using user keep days")
	protected String userKeepDaysText;
	
	protected AnnotatedDAOWrapper<File,Integer> fileDAO;
	protected AnnotatedDAO<FileCollection> fileCollectionDAO;
	protected QueryParameterFactory<FileCollection, User> posterParamFactory;

	protected FileCollectionCRUD fileCollectionCRUD;

	protected SimpleAccessInterface adminAccessInterface;
	protected SimpleAccessInterface uploadAccessInterface;
	
	private Scheduler taskScheduler;
	
	private UserGroupListConnector userGroupListConnector;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (systemInterface.getSystemStatus() == SystemStatus.STARTED){

			this.initTaskScheduler();

		}else{

			this.systemInterface.addSystemStartupListener(this);
		}
		
		if (!systemInterface.getInstanceHandler().addInstance(FileArchiveModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + FileArchiveModule.class.getName());
		}
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.stopTaskScheduler();

		this.initTaskScheduler();
	}
	
	@Override
	public void systemStarted() throws Exception {

		this.initTaskScheduler();
		
	}
	
	private void initTaskScheduler() {

		if (deleteOldFiles) {
			this.taskScheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
			
			this.taskScheduler.setDaemon(true);
			
			this.taskScheduler.schedule(cronExp, this);
			
			this.taskScheduler.start();
		}
	}

	private void stopTaskScheduler() {

		if (this.taskScheduler != null && this.taskScheduler.isStarted()){
			
			this.taskScheduler.stop();
		}
	}

	@Override
	public void unload() throws Exception {

		stopTaskScheduler();

		super.unload();
		
		systemInterface.getInstanceHandler().removeInstance(FileArchiveModule.class, this);
	}

	@Override
	public void run() {

		if (this.systemInterface.getSystemStatus() == SystemStatus.STARTED){

			log.info("Doing removal of old files");

			try{
				HighLevelQuery<FileCollection> query = new HighLevelQuery<FileCollection>();
				query.addRelations(FileCollection.FILES_RELATION);
				
				List<FileCollection> fileCollections = this.fileCollectionDAO.getAll(query);

				if(!CollectionUtils.isEmpty(fileCollections)){
					
					long currentMillis = System.currentTimeMillis();

					for(FileCollection fileCollection : fileCollections){

						long collectionMillis = 0l;
						
						if (fileCollection.getUpdated() != null) {
							collectionMillis = fileCollection.getUpdated().getTime();
						}
						else {
							collectionMillis = fileCollection.getPosted().getTime();
						}
						
						if (fileCollection.getCollectionKeepDays() != null) {
							collectionMillis += (long) fileCollection.getCollectionKeepDays() * MillisecondTimeUnits.DAY;
						}
						else {
							collectionMillis += (long) keepFilesDays * MillisecondTimeUnits.DAY;
						}
						
						if (collectionMillis < currentMillis){
							
							if (!CollectionUtils.isEmpty(fileCollection.getFiles())) {
								for(File file : fileCollection.getFiles()){
									
									String filePath = getFileStore() + java.io.File.separator + file.getFileID();
									
									log.info("Deleting file " + file + " from collection " + fileCollection + " from path " + filePath);
									
									FileUtils.deleteFile(filePath);
								}
							}

							this.fileCollectionDAO.delete(fileCollection);

							log.info("Deleting file collection " + fileCollection);
						}
					}
				}
				
			}catch(SQLException e){

				log.error("Could not delete old files ", e);
			}
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception{

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, this.getClass().getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if(upgradeResult.isUpgrade()){

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		this.fileDAO = daoFactory.getDAO(File.class).getWrapper("fileID", Integer.class);
		this.fileCollectionDAO = daoFactory.getDAO(FileCollection.class);

		fileDAO.addRelation(File.FILE_COLLECTION_RELATION);
		fileDAO.addRelation(FileCollection.ALLOWED_USERS_RELATION);
		fileDAO.addRelation(FileCollection.ALLOWED_GROUPS_RELATION);
		fileDAO.addRelation(FileCollection.PERSONS_RELATION);
		fileDAO.setUseRelationsOnGet(true);
		fileDAO.disableAutoRelations(true);

		posterParamFactory = fileCollectionDAO.getParamFactory("poster", User.class);

		this.fileCollectionCRUD = new FileCollectionCRUD(fileCollectionDAO.getWrapper("collectionID",Integer.class), new AnnotatedRequestPopulator<FileCollection>(FileCollection.class), "FileCollection", "file collection", "/", this);
		
		userGroupListConnector = new UserGroupListConnector(systemInterface);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();
		
		userGroupListConnector.setUserGroupFilter(searchableUserGroupIDs);
	}

	@Override
	protected void parseSettings(MutableSettingHandler mutableSettingHandler) throws Exception {

		super.parseSettings(mutableSettingHandler);

		this.adminAccessInterface = new SimpleAccessInterface(adminGroupIDs,adminUserIDs);
		this.uploadAccessInterface = new SimpleAccessInterface(uploadGroupIDs,uploadUserIDs);
	}

	@Override
	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if(StringUtils.isEmpty(fileStore)){

			throw new ModuleConfigurationException("No filestore configured");
		}

		return super.processRequest(req, res, user, uriParser);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		return fileCollectionCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic
	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return fileCollectionCRUD.add(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return fileCollectionCRUD.update(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return fileCollectionCRUD.delete(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return fileCollectionCRUD.show(req, res, user, uriParser);
	}
	
	@WebPublic(alias = "users")
	public ForegroundModuleResponse getUsers(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if (!AccessUtils.checkAccess(user, adminAccessInterface) && !AccessUtils.checkAccess(user, uploadAccessInterface)) {
			throw new AccessDeniedException("User " + user + " not allowed to search for users");
		}
		
		return userGroupListConnector.getUsers(req, res, user, uriParser);
	}

	@WebPublic(alias = "groups")
	public ForegroundModuleResponse getGroups(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if (!AccessUtils.checkAccess(user, adminAccessInterface) && !AccessUtils.checkAccess(user, uploadAccessInterface)) {
			throw new AccessDeniedException("User " + user + " not allowed to search for groups");
		}
		
		return userGroupListConnector.getGroups(req, res, user, uriParser);
	}
	
	@WebPublic
	public ForegroundModuleResponse upload(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		FileCollection fileCollection = fileCollectionCRUD.getRequestedBean(req, res, user, uriParser, GenericCRUD.SHOW);
		
		if (fileCollection != null && fileCollection.isAllowsCollaboration()) {
			if (fileCollection.isEncrypted() && SessionUtils.getAttribute("collectionpassword-" + fileCollection.getCollectionID(), req) == null) {
				throw new URINotFoundException(uriParser);
			}

			checkCollectionAccess(fileCollection, uriParser, user);
			
			if (MultipartRequest.isMultipartRequest(req)) {
				
				JsonObject jsonObject = new JsonObject();
				
				try {
					MultipartRequest multipartRequest = MultipartRequest.getMultipartRequest(getRamThreshold() * BinarySizes.KiloByte, (long) getMaxRequestSize() * BinarySizes.MegaByte, getTempDir(), req);
					
					log.info("User " + user + " uploading file to collaborative collection " + fileCollection);
					
					if (multipartRequest.getFileCount() > 0) {

						Collection<FileItem> fileItems = multipartRequest.getFiles();

						Iterator<FileItem> fileIterator = fileItems.iterator();
						
						FileItem fileItem = null;

						while (fileIterator.hasNext()) {

							FileItem iteratorItem = fileIterator.next();

							if (StringUtils.isEmpty(iteratorItem.getName()) || iteratorItem.getSize() == 0) {

								fileIterator.remove();
								
								continue;
							}
							else {
								fileItem = iteratorItem;
								
								break;
							}
						}
						
						if (fileItem != null) {
							
							TransactionHandler transactionHandler = null;
							
							try {
								transactionHandler = fileDAO.createTransaction();
								
								File file = new File();
								
								String fileName = FilenameUtils.getName(fileItem.getName());
								
								file.setName(fileName);
								file.setFileItem(fileItem);
								file.setAlias(RandomUtils.getRandomString(12, RandomUtils.LOWER_CASE_CHARACTERS));
								file.setSize(fileItem.getSize());
								file.setFileCollection(fileCollection);
								
								fileDAO.add(file, transactionHandler);
								
								if (fileCollection.isEncrypted()) {
									String password = (String) SessionUtils.getAttribute("collectionpassword-" + fileCollection.getCollectionID(), req);
									
									Key secretKey = new SecretKeySpec(password.getBytes(), "AES");
									Cipher cipher = Cipher.getInstance("AES");
									cipher.init(Cipher.ENCRYPT_MODE, secretKey);
						
									String filePath = getFileStore() + java.io.File.separator + file.getFileID();
									
									log.info("Writing encrypted file " + file + " from collection " + fileCollection + " to path " + filePath);
									
									byte[] outputBytes = cipher.doFinal(fileItem.get());
									
									FileOutputStream outputStream = new FileOutputStream(filePath);
									
									outputStream.write(outputBytes);
									outputStream.close();
								}
								else {
									String filePath = getFileStore() + java.io.File.separator + file.getFileID();
									
									log.info("Writing file " + file + " from collection " + fileCollection + " to path " + filePath);
									
									FileUtils.writeFile(filePath, fileItem.getInputStream(), true);
								}
								
								transactionHandler.commit();
								
								jsonObject.putField("success", true);
							}
							catch (Exception ex) {
								log.error("Unable to add files to collection " + fileCollection, ex);
							}
							finally {
								TransactionHandler.autoClose(transactionHandler);
							}
						}
					}

				} catch (SizeLimitExceededException e) {

					jsonObject.putField("error", true);

				} catch (FileSizeLimitExceededException e) {

					jsonObject.putField("error", true);

				} catch (FileUploadException e) {

					jsonObject.putField("error", true);
				}
				
				HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
				
				return null;
			}
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	private void checkCollectionAccess(FileCollection fileCollection, URIParser uriParser, User user) throws AccessDeniedException, URINotFoundException {

		if (fileCollection.getSecurityMode() == SecurityMode.IDENTIFICATION) {
			if (user == null) {
				throw new AccessDeniedException("User need to be logged in to view secure file collection");
			}

			if (AccessUtils.checkAccess(user, getAdminAccessInterface())) {
				return;
			}

			if (fileCollection.getPoster().equals(user)) {
				return;
			}

			if (fileCollection.getCitizenIDs() == null || !fileCollection.getCitizenIDs().contains(user.getAttributeHandler().getString("citizenIdentifier"))) {
				throw new URINotFoundException(uriParser);
			}
		} else {
			if (fileCollection.getPoster().equals(user)) {
				return;
			}
			
			if (fileCollection.getSecurityMode() == SecurityMode.PUBLIC) {

				if (uriParser.size() == 4 && uriParser.get(3).equals(fileCollection.getAlias())) {
					return;
				}
				
				if (fileCollection.isEncrypted() && fileCollection.isAllowsCollaboration()) {
					return;
				}

				throw new URINotFoundException(uriParser);

			} else {

				if (AccessUtils.checkAccess(user, fileCollection) || AccessUtils.checkAccess(user, getAdminAccessInterface())) {

					return;
				}

				throw new AccessDeniedException("Access to file collection denied");
			}
		}
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse validatePerson(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		String citizenID = req.getParameter("citizenID");

		JsonObject jsonObject = new JsonObject();
		
		if (StringUtils.isEmpty(citizenID) || !getCitizenIdentifierPopulator().validateFormat(citizenID)) {
			jsonObject.putField("invalidID", true);
		}
		else {
			jsonObject.putField("isValid", true);
		}
		
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
		
		return null;
	}

	@WebPublic(alias = "downloadc")
	public ForegroundModuleResponse downloadCollection(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		FileCollection collection = fileCollectionCRUD.getRequestedBean(req, res, user, uriParser, FileCollectionCRUD.SHOW);

		if (collection == null || !(uriParser.size() == 4 && uriParser.get(3).equals(collection.getAlias()))){

			throw new URINotFoundException(uriParser);
		}
		
		if (collection.isEncrypted() && SessionUtils.getAttribute("collectionpassword-" + collection.getCollectionID(), req) == null) {
			
			throw new URINotFoundException(uriParser);
		}

		checkFileCollectionAccess(collection, user);

		if (collection.getFiles() == null) {

			redirectToMethod(req, res, "/show/" + collection.getCollectionID() + "/" + collection.getAlias());
		}

		log.info("User " + user + " downloading all files in file collection " + collection);

		long startTime = System.currentTimeMillis();

		ZipOutputStream zipOutputStream = null;

		try {
			res.setContentType("application/zip");
			
			if (collection.getName() != null) {
				
				res.setHeader("Content-Disposition", "attachment; filename=\"" + FileUtils.toValidHttpFilename(collection.getName()) + ".zip\"");
			}
			else {
				
				res.setHeader("Content-Disposition", "attachment; filename=\"files.zip\"");
			}
			
			res.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, proxy-revalidate");

			zipOutputStream = new ZipOutputStream(res.getOutputStream());
			zipOutputStream.setLevel(ZipOutputStream.STORED);

			if (collection.isEncrypted()) {
				String password = (String) SessionUtils.getAttribute("collectionpassword-" + collection.getCollectionID(), req);
				
				Key secretKey = new SecretKeySpec(password.getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES");
				cipher.init(Cipher.DECRYPT_MODE, secretKey);
				
				for (File file : collection.getFiles()) {
					
					java.io.File fileSystemFile = new java.io.File(fileStore + java.io.File.separator + file.getFileID());
					
					if (!fileSystemFile.canRead()) {
						
						log.warn("Filesystem and database are not in sync, unable to find file " + file + " in filesystem");
						continue;
					}
					
					FileInputStream inputStream = null;
					CipherInputStream cipherInputStream = null;
					
					try {
						inputStream = new FileInputStream(fileSystemFile);
						
						cipherInputStream = new CipherInputStream(inputStream, cipher); 
						
						ZipUtils.addEntry(file.getName(), cipherInputStream, zipOutputStream);
						
					} finally {
						CloseUtils.close(cipherInputStream);
						CloseUtils.close(inputStream);
					}
				}
			}
			else {
				for (File file : collection.getFiles()) {
					java.io.File fileSystemFile = new java.io.File(fileStore + java.io.File.separator + file.getFileID());
					
					if (!fileSystemFile.canRead()){
						
						log.warn("Filesystem and database are not in sync, unable to find file " + file + " in filesystem");
						continue;
					}
					
					InputStream inputStream = null;
					
					try {
						// Open the file and output streams
						inputStream = new FileInputStream(fileSystemFile);
						
						ZipUtils.addEntry(file.getName(), inputStream, zipOutputStream);
						
					} finally {
						CloseUtils.close(inputStream);
					}
				}
			}

			log.info("Sent file collection " + collection + " containing " + collection.getFiles().size() + " files to user " + user + " in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime));

		} catch (IOException e) {

			log.info("Error sending file collection " + collection + " to user " + user + " due to " + e);

		} finally {

			CloseUtils.close(zipOutputStream);
			CloseUtils.close(res.getOutputStream());
		}

		return null;
	}

	@WebPublic(alias = "download")
	public ForegroundModuleResponse downloadFile(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		File file;

		//Get file and verify alias
		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(2)) || (file = this.fileDAO.get(uriParser.getInt(2))) == null || !uriParser.get(3).endsWith(file.getAlias())) {

			throw new URINotFoundException(uriParser);
		}
		
		if (file.getFileCollection().isEncrypted() && SessionUtils.getAttribute("collectionpassword-" + file.getFileCollection().getCollectionID(), req) == null) {
			
			throw new URINotFoundException(uriParser);
		}

		checkFileCollectionAccess(file.getFileCollection(), user);

		//Check if the file exists in file system and send the file
		java.io.File fileSystemFile = new java.io.File(fileStore + java.io.File.separator + file.getFileID());

		if (fileSystemFile.exists() && fileSystemFile.canRead() && !fileSystemFile.isDirectory()) {

			log.info("Sending file " + file + " from collection " + file.getFileCollection() + " to user " + user + " accessing from " + req.getRemoteHost());

			String lastModifiedString = null;

			try {
				lastModifiedString = LAST_MODIFIED_DATE_FORMATTER.format(new Date(fileSystemFile.lastModified()));

			} catch (RuntimeException e) {

				log.info("Error formatting last modified timestamp of file " + fileSystemFile + ", " + e);
			}

			String modifiedSinceString = req.getHeader("If-Modified-Since");

			if (lastModifiedString != null && modifiedSinceString != null && modifiedSinceString.equalsIgnoreCase(lastModifiedString)) {

				res.setStatus(304);
				try {
					res.flushBuffer();
				} catch (IOException e) {}

				return null;
			}

			res.setHeader("Last-Modified", lastModifiedString);
			res.setHeader("Content-Disposition", "attachment; filename=\"" + FileUtils.toValidHttpFilename(file.getName()) + "\"");

			String contentType = MimeUtils.getMimeType(file.getName());

			if (contentType != null) {
				res.setContentType(contentType);
			} else {
				res.setContentType("application/x-unknown-mime-type");
			}

			InputStream inputStream = null;
			CipherInputStream cipherInputStream = null;
			OutputStream outputStream = null;
			FileInputStream fileInputStream = null;

			if (file.getFileCollection().isEncrypted()) {
				String password = (String) SessionUtils.getAttribute("collectionpassword-" + file.getFileCollection().getCollectionID(), req);
				
				try {
					Key secretKey = new SecretKeySpec(password.getBytes(), "AES");
					Cipher cipher = Cipher.getInstance("AES");
					cipher.init(Cipher.DECRYPT_MODE, secretKey);
					
					fileInputStream = new FileInputStream(fileSystemFile);
					
					cipherInputStream = new CipherInputStream(fileInputStream, cipher); 
					
					HTTPUtils.setContentLength(file.getSize(), res);
					
					outputStream = res.getOutputStream();
					
					StreamUtils.transfer(cipherInputStream, outputStream);
					
					outputStream.flush();
					res.flushBuffer();
				}
				catch (Exception ex) {
					
					log.info("Error sending encrypted file " + file + " to user " + user, ex);
				}
				finally {
					CloseUtils.close(cipherInputStream);
					CloseUtils.close(fileInputStream);
					CloseUtils.close(outputStream);
				}
			}
			else {
				HTTPUtils.setContentLength(fileSystemFile.length(), res);
				
				try {
					// Open the file and output streams
					inputStream = new FileInputStream(fileSystemFile);
					outputStream = res.getOutputStream();
					
					StreamUtils.transfer(inputStream, outputStream);
					
				} catch (IOException ex) {
					
					log.info("Error sending file " + file + " to user " + user, ex);
					
				} finally {
					CloseUtils.close(inputStream);
					CloseUtils.close(outputStream);
				}
			}

		} else {
			// File not found in file system
			log.warn("Filesystem and database are not in sync, unable to find file " + file + " in filesystem");
			throw new URINotFoundException(uriParser);
		}
		
		return null;
	}

	public void checkFileCollectionAccess(FileCollection collection, User user) throws AccessDeniedException {
		
		if (collection.getPoster().equals(user)) {
			return;
		}

		if (collection.getSecurityMode() == SecurityMode.ACCESS && !AccessUtils.checkAccess(user, collection) && !AccessUtils.checkAccess(user, getAdminAccessInterface())) {

			throw new AccessDeniedException("Access to file collection " + collection + " denied");
		}
		
		if (collection.getSecurityMode() == SecurityMode.IDENTIFICATION && (user == null || (!user.equals(collection.getPoster()) && !CollectionUtils.contains(collection.getCitizenIDs(), user.getAttributeHandler().getString("citizenIdentifier"))))) {
			
			throw new AccessDeniedException("Access to file collection " + collection + " denied");
		}
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		XMLUtils.appendNewElement(doc, document, "MaxUploadSize", this.maxRequestSize);
		
		if (user != null) {
			XMLUtils.appendNewElement(doc, document, "LoggedIn");
		}
		
		if (deleteOldFiles) {
			XMLUtils.appendNewElement(doc, document, "KeepFilesDays", keepFilesDays);
		}
		
		doc.appendChild(document);
		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	public boolean allowsAdminAccess() {

		return false;
	}

	public boolean allowsUserAccess() {

		return false;
	}

	public boolean allowsAnonymousAccess() {

		return false;
	}
	
	public String getAllowSecureCollabMessage() {
	
		return allowSecureCollabMessage;
	}

	public Collection<Integer> getAllowedGroupIDs() {

		return this.adminGroupIDs;
	}

	public Collection<Integer> getAllowedUserIDs() {

		return this.adminUserIDs;
	}

	public ForegroundModuleDescriptor getModuleDescriptor() {

		return moduleDescriptor;
	}

	public SectionDescriptor getSectionDescriptor() {

		return sectionInterface.getSectionDescriptor();
	}

	public Integer getMaxRequestSize() {

		return maxRequestSize;
	}

	public Integer getRamThreshold() {

		return ramThreshold;
	}


	public String getTempDir() {

		return tempDir;
	}


	public String getFileStore() {

		return fileStore;
	}


	public String getAddFileCollectionBreadCrumbText() {

		return addFileCollectionBreadCrumbText;
	}


	public String getUpdateFileCollectionBreadCrumbText() {

		return updateFileCollectionBreadCrumbText;
	}


	public String getUnnamedFileCollectionText() {

		return unnamedFileCollectionText;
	}


	public SimpleAccessInterface getAdminAccessInterface() {

		return adminAccessInterface;
	}


	public SimpleAccessInterface getUploadAccessInterface() {

		return uploadAccessInterface;
	}

	public List<FileCollection> getUserFileCollections(User user) throws SQLException {

		List<FileCollection> fileCollections = this.fileCollectionDAO.getAll();

		if (fileCollections == null) {

			return null;
		}

		Iterator<FileCollection> iterator = fileCollections.iterator();

		while (iterator.hasNext()) {

			FileCollection fileCollection = iterator.next();

			if (!AccessUtils.checkAccess(user, fileCollection) && !fileCollection.getPoster().equals(user)) {

				iterator.remove();
			}
		}

		return fileCollections;
	}

	public boolean showFileCollectionURL() {

		return showFileCollectionURL;
	}


	public AccessMode getAccessMode() {

		return accessMode;
	}

	public UserHandler getUserHandler() {

		return systemInterface.getUserHandler();
	}

	public GroupHandler getGroupHandler() {

		return systemInterface.getGroupHandler();
	}

	public String getListCollectionsInfo() {

		return listFileCollectionsInfo;
	}
	
	public boolean isAllowSecureCollections() {
		
		return allowSecureCollections;
	}
	
	public boolean isAllowCollabCollections() {
		
		return allowCollabCollections;
	}

	public SwedishSocialSecurityPopulator getCitizenIdentifierPopulator() {

		return SOCIAL_SECURITY_POPULATOR;
	}

	public String getFileCollectionText() {

		return fileCollectionText;
	}

	public boolean isAllowFileEncryption() {

		return allowFileEncryption;
	}

	public String getFileEncryptionText() {

		return fileEncryptionText;
	}

	public int getEncryptionPasswordLength() {

		return encryptionPasswordLength;
	}

	public boolean isHideGroupAccess() {

		return hideGroupAccess;
	}

	public boolean isAllowUserKeepDays() {

		return allowUserKeepDays;
	}

	public int getUserKeepDaysMax() {

		return userKeepDaysMax;
	}

	public String getUserKeepDaysText() {

		return userKeepDaysText;
	}

	public int getKeepFilesDays() {

		return keepFilesDays;
	}

	public boolean isDeleteOldFiles() {

		return deleteOldFiles;
	}
	
	public List<File> getAllFiles() throws SQLException {
		return fileDAO.getAll();
	}
}
