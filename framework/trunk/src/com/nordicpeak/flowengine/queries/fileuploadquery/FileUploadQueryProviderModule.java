package com.nordicpeak.flowengine.queries.fileuploadquery;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.InvalidFileNameException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.validationerrors.FileCountExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.FileNameLengthLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.FileSizeLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.InvalidFileExtensionValidationError;
import se.unlogic.hierarchy.core.validationerrors.UnableToSaveFileValidationError;
import se.unlogic.standardutils.base64.Base64;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.CommitCallback;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.fileicons.FileIconHandler;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.PDFAttachment;
import com.nordicpeak.flowengine.interfaces.PDFResourceProvider;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager.FlowInstanceManagerRegistery;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.utils.FileIconResourceProvider;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.PDFFileAttachment;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class FileUploadQueryProviderModule extends BaseQueryProviderModule<FileUploadQueryInstance> implements Runnable, BaseQueryCRUDCallback, SystemStartupListener {

	private static final RelationQuery SAVE_QUERY_INSTANCE_RELATION_QUERY = new RelationQuery(FileUploadQueryInstance.FILES_RELATION);

	private static final FileIconResourceProvider FILE_ICON_RESOURCE_PROVIDER = new FileIconResourceProvider();

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File store", description = "Directory where uploaded files are stored", required = true)
	protected String fileStore;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Temp file store", description = "Directory where uploaded files are stored before they are saved", required = true)
	protected String tempFileStore;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Cleanup interval", description = "Controls how often (in minutes) this module should check for abandoned files", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected int cleanupInterval = 10;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max allowed file size", description = "The max allowed file size for each file upload query", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected int maxAllowedFileSize = 50;

	@ModuleSetting(allowsNull=true)
	@TextAreaSettingDescriptor(name="Allowed file extensions", description="Default value for allowed file extensions.")
	protected List<String> defaultAllowedFileExtensions;
	
	@XSLVariable(prefix = "java.")
	protected String pdfAttachmentDescriptionPrefix = "A file from query:";

	private AnnotatedDAO<FileUploadQuery> queryDAO;
	private AnnotatedDAO<FileUploadQueryInstance> queryInstanceDAO;

	private FileUploadQueryCRUD queryCRUD;

	private QueryParameterFactory<FileUploadQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<FileUploadQueryInstance, Integer> queryInstanceIDParamFactory;

	private Thread cleanupThread;
	private boolean shutdown;
	private boolean firstRun = true;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (systemInterface.getSystemStatus() == SystemStatus.STARTED) {

			systemStarted();

		} else if (systemInterface.getSystemStatus() == SystemStatus.STARTING) {

			systemInterface.addSystemStartupListener(this);
		}

	}

	@Override
	public void unload() throws Exception {

		this.shutdown = true;

		if (cleanupThread != null && cleanupThread.isAlive()) {

			log.info("Interrupting and joining cleanup thread...");
			cleanupThread.interrupt();
			cleanupThread.join();
		}

		super.unload();
	}

	@Override
	public void systemStarted() {

		cleanupThread = new Thread(this, "Cleanup thread for module " + moduleDescriptor);
		cleanupThread.start();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FileUploadQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(FileUploadQuery.class);
		queryInstanceDAO = daoFactory.getDAO(FileUploadQueryInstance.class);

		queryCRUD = new FileUploadQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<FileUploadQuery>(FileUploadQuery.class), "FileUploadQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		checkConfiguration();

		FileUploadQuery query = new FileUploadQuery();

		query.setQueryID(descriptor.getQueryID());

		if(defaultAllowedFileExtensions != null){
			
			query.setAllowedFileExtensions(defaultAllowedFileExtensions);
		}
		
		query.setAttachmentNamePrefixMode(AttachmentNamePrefixType.QUERY_NAME);
		
		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws Throwable {

		FileUploadQuery query = new FileUploadQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

		checkConfiguration();

		FileUploadQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		checkConfiguration();

		FileUploadQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws Throwable {

		checkConfiguration();

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			FileUploadQueryInstance queryInstance = new FileUploadQueryInstance();

			queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

			if (queryInstance.getQuery() == null) {

				return null;
			}

			if(req != null){

				FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

				URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
			}
			
			queryInstance.getQuery().scanAttributeTags();

			TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

			queryInstance.set(descriptor);
			queryInstance.copyQueryValues();
			queryInstance.setInstanceManagerID(instanceManagerID);

			return queryInstance;

		} else {

			return getExistingQueryInstance(descriptor, req, instanceManagerID, instanceMetadata);
		}
	}

	@Override
	public ImmutableQueryInstance getImmutableQueryInstance(MutableQueryInstanceDescriptor descriptor, HttpServletRequest req, InstanceMetadata instanceMetadata) throws Throwable {

		return getExistingQueryInstance(descriptor, req, null, instanceMetadata);
	}

	private QueryInstance getExistingQueryInstance(MutableQueryInstanceDescriptor descriptor, HttpServletRequest req, String instanceManagerID, InstanceMetadata instanceMetadata) throws Throwable {

		FileUploadQueryInstance queryInstance = getQueryInstance(descriptor.getQueryInstanceID());

		if (queryInstance == null) {

			return null;
		}

		queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

		if (queryInstance.getQuery() == null) {

			return null;
		}
		
		if(req != null){

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}
		
		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.setInstanceManagerID(instanceManagerID);

		queryInstance.set(descriptor);

		//Only make copy of files if there are any files and if this is to be a mutable instance
		if (queryInstance.getFiles() != null && instanceManagerID != null) {

			try {

				for (FileDescriptor fileDescriptor : queryInstance.getFiles()) {

					//Assign temporary file ID
					fileDescriptor.setTemporaryFileID(queryInstance.getNextTemporaryFileID());

					//Copy existing files to a temporary location
					File sourceFile = new File(getFilePath(fileDescriptor, descriptor));

					if (!sourceFile.exists()) {

						throw new QueryInstanceFileNotFoundException("Unable to find file " + sourceFile.getAbsolutePath() + " belonging file descriptor " + fileDescriptor + " of query instance " + queryInstance);
					}

					File destinationFile = new File(getTemporaryFilePath(fileDescriptor, descriptor, instanceManagerID));

					if (destinationFile.exists()) {

						throw new QueryInstanceFileOverwriteException("Refusing to overwrite file " + destinationFile.getAbsolutePath());
					}

					if (!destinationFile.getParentFile().exists()) {

						destinationFile.getParentFile().mkdirs();
					}

					FileUtils.copyFile(sourceFile, destinationFile);
				}

			} catch (Throwable t) {

				//If anything goes wrong delete all temporary files
				close(queryInstance);

				throw t;
			}

		}

		return queryInstance;
	}

	private String getFilePath(FileDescriptor fileDescriptor, MutableQueryInstanceDescriptor descriptor) {

		return getQueryInstanceDirectory(descriptor) + File.separator + fileDescriptor.getFileID() + "." + FileUtils.getFileExtension(fileDescriptor.getName());
	}

	private String getTemporaryFilePath(FileDescriptor fileDescriptor, MutableQueryInstanceDescriptor descriptor, String instanceManagerID) {

		return getTemporaryQueryInstanceDirectory(descriptor, instanceManagerID) + File.separator + fileDescriptor.getTemporaryFileID() + "." + FileUtils.getFileExtension(fileDescriptor.getName());
	}

	public String getQueryDirectory(ImmutableQueryDescriptor descriptor) {

		return this.fileStore + File.separator + "query" + descriptor.getQueryID();
	}

	public String getQueryInstanceDirectory(ImmutableQueryInstanceDescriptor descriptor) {

		return this.getQueryDirectory(descriptor.getQueryDescriptor()) + File.separator + "instance" + descriptor.getQueryInstanceID();
	}

	public String getTemporaryQueryInstanceDirectory(ImmutableQueryInstanceDescriptor descriptor, String instanceManagerID) {

		return this.tempFileStore + File.separator + instanceManagerID + File.separator + "query" + descriptor.getQueryDescriptor().getQueryID();
	}

	private FileUploadQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<FileUploadQuery> query = new HighLevelQuery<FileUploadQuery>(FileUploadQuery.ALLOWED_FILE_EXTENSIONS_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private FileUploadQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<FileUploadQuery> query = new HighLevelQuery<FileUploadQuery>(FileUploadQuery.ALLOWED_FILE_EXTENSIONS_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private FileUploadQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<FileUploadQueryInstance> query = new HighLevelQuery<FileUploadQueryInstance>(FileUploadQueryInstance.FILES_RELATION, FileUploadQueryInstance.QUERY_RELATION, FileUploadQuery.ALLOWED_FILE_EXTENSIONS_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(FileUploadQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		checkConfiguration();

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);
		}

		String queryInstanceDirectory = getQueryInstanceDirectory(queryInstance.getQueryInstanceDescriptor());

		//Delete old files
		FileUtils.deleteFiles(queryInstanceDirectory, null, false);

		//Write new files
		if (queryInstance.getFiles() != null) {

			for (FileDescriptor fileDescriptor : queryInstance.getFiles()) {

				File temporaryFile = new File(getTemporaryFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor(), queryInstance.getInstanceManagerID()));

				if (!temporaryFile.exists()) {

					throw new QueryInstanceTemporaryFileNotFoundException("Unable to find temporary file " + temporaryFile.getAbsolutePath() + " belonging file descriptor " + fileDescriptor + " of query instance " + queryInstance);
				}

				File destinationFile = new File(getFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor()));

				if (destinationFile.exists()) {

					throw new QueryInstanceFileOverwriteException("Refusing to overwrite file " + destinationFile.getAbsolutePath());

				} else if (!destinationFile.getParentFile().exists()) {

					destinationFile.getParentFile().mkdirs();
				}

				FileUtils.copyFile(temporaryFile, destinationFile);
			}
		}
	}
	
	@Override
	protected void appendQueryInstance(FileUploadQueryInstance queryInstance, Document doc, Element targetElement, AttributeHandler attributeHandler) {
		
		super.appendQueryInstance(queryInstance, doc, targetElement, attributeHandler);
		
		if (queryInstance.getQuery().isLockOnOwnershipTransfer() && attributeHandler.getPrimitiveBoolean("OwnershipTransfered")) {
			
			XMLUtils.appendNewElement(doc, targetElement, "Locked", "true");
		}
	}

	@Override
	public void populate(FileUploadQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		checkConfiguration();
		
		FileUploadQuery query = queryInstance.getQuery();
		Integer queryID = query.getQueryID();
		
		if (query.isLockOnOwnershipTransfer() && attributeHandler.getPrimitiveBoolean("OwnershipTransfered")) {
			return;
		}

		if (!(req instanceof MultipartRequest)) {

			return;
		}

		MultipartRequest multipartReq = (MultipartRequest) req;

		//Check if any existing files should be deleted
		if (queryInstance.getFiles() != null) {

			Iterator<FileDescriptor> iterator = queryInstance.getFiles().iterator();

			while (iterator.hasNext()) {

				FileDescriptor fileDescriptor = iterator.next();

				//Check if the file should be deleted
				if (req.getParameter("q" + queryID + "_file" + fileDescriptor.getTemporaryFileID()) != null) {

					iterator.remove();

					deleteTemporaryFile(fileDescriptor, queryInstance.getQueryInstanceDescriptor(), queryInstance.getInstanceManagerID());
				}
			}
		}

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		//Get any new files
		List<FileItem> fileItems = multipartReq.getFiles("q" + queryID + "_newfile");

		if (fileItems != null) {

			//Parse files and write them to the temporary directory for this query instance

			for (FileItem fileItem : fileItems) {

				try {
					if (StringUtils.isEmpty(fileItem.getName())) {

						continue;
					}
				} catch (InvalidFileNameException e) {

					validationErrors.add(new InvalidFileExtensionValidationError(""));
					
					continue;
				}

				//validate file count
				if (queryInstance.getQuery().getMaxFileCount() != null && queryInstance.getFiles() != null && queryInstance.getFiles().size() >= queryInstance.getQuery().getMaxFileCount()) {

					validationErrors.add(new FileCountExceededValidationError(queryInstance.getQuery().getMaxFileCount()));

					break;
				}

				String fileExtension = FileUtils.getFileExtension(fileItem.getName());

				//Validate file extension
				if (queryInstance.getQuery().getAllowedFileExtensions() != null && (fileExtension == null || !queryInstance.getQuery().getAllowedFileExtensions().contains(fileExtension.toLowerCase()))) {

					validationErrors.add(new InvalidFileExtensionValidationError(FilenameUtils.getName(fileItem.getName())));

					continue;
				}

				int maxFileSize = queryInstance.getQuery().getMaxFileSize() != null ? queryInstance.getQuery().getMaxFileSize() : maxAllowedFileSize * BinarySizes.MegaByte;

				//Validate file size
				if (fileItem.getSize() > maxFileSize) {

					validationErrors.add(new FileSizeLimitExceededValidationError(FilenameUtils.getName(fileItem.getName()), fileItem.getSize(), maxFileSize));

					continue;
				}

				//Validate file name length
				String filename = FilenameUtils.getName(fileItem.getName());
				
				if (queryInstance.getQuery().getMaxFileNameLength() != null && filename.length() > queryInstance.getQuery().getMaxFileNameLength()) {

					validationErrors.add(new FileNameLengthLimitExceededValidationError(filename, filename.length(), queryInstance.getQuery().getMaxFileNameLength()));

					continue;
				}
				
				//Create file descriptor
				FileDescriptor fileDescriptor = new FileDescriptor();
				fileDescriptor.setTemporaryFileID(queryInstance.getNextTemporaryFileID());
				fileDescriptor.setName(FilenameUtils.getName(fileItem.getName()));
				fileDescriptor.setSize(fileItem.getSize());

				File temporaryFile = new File(getTemporaryFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor(), queryInstance.getInstanceManagerID()));

				//Write file
				try {
					if (!temporaryFile.getParentFile().exists()) {

						temporaryFile.getParentFile().mkdirs();
					}

					fileItem.write(temporaryFile);
				} catch (Exception e) {

					log.error("Error saving file item " + fileItem.getName() + " as file " + temporaryFile.getAbsolutePath(), e);
					validationErrors.add(new UnableToSaveFileValidationError(fileItem.getName()));
					continue;
				}

				//Add file descriptor to query instance
				if (queryInstance.getFiles() == null) {

					queryInstance.setFiles(new ArrayList<FileDescriptor>());
				}

				queryInstance.getFiles().add(fileDescriptor);
			}
		}

		if (validationErrors.isEmpty() && CollectionUtils.isEmpty(queryInstance.getFiles()) && !allowPartialPopulation && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED) {

			validationErrors.add(new ValidationError("RequiredField"));
		}

		if (!validationErrors.isEmpty()) {

			throw new ValidationException(validationErrors);
		}

		queryInstance.getQueryInstanceDescriptor().setPopulated(!CollectionUtils.isEmpty(queryInstance.getFiles()));
		
		if(queryInstance.getQuery().isSetAsAttribute()){
			
			queryInstance.resetAttribute(attributeHandler);
			queryInstance.setAttribute(attributeHandler);
		}
	}

	private void deleteTemporaryFile(FileDescriptor fileDescriptor, MutableQueryInstanceDescriptor descriptor, String instanceManagerID) {

		File file = new File(getTemporaryFilePath(fileDescriptor, descriptor, instanceManagerID));

		file.delete();
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		checkConfiguration();

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		checkConfiguration();

		FileUploadQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		FileUtils.deleteDirectory(getQueryDirectory(descriptor));

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		checkConfiguration();

		FileUploadQueryInstance queryInstance = getQueryInstance(descriptor.getQueryInstanceID());

		if (queryInstance == null) {

			return false;
		}

		this.queryInstanceDAO.delete(queryInstance, transactionHandler);

		transactionHandler.addCommitCallback(new CommitCallback() {
			
			@Override
			public void commitComplete() {

				log.info("Deleting files for query instance " + descriptor);
				
				try {
					FileUtils.deleteDirectory(getQueryInstanceDirectory(descriptor));
				
				} catch (Throwable t) {

					log.error("Error deleting files for query instance " + descriptor, t);
				}
			}
		});

		return true;
	}

	@WebPublic(alias = "fileicon")
	public ForegroundModuleResponse getFileIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (uriParser.size() != 3) {

			throw new URINotFoundException(uriParser);
		}

		FileIconHandler.streamIconByFilename(uriParser.get(2), res);

		return null;
	}

	public void close(FileUploadQueryInstance queryInstance) {

		checkConfiguration();

		FileUtils.deleteDirectory(getTemporaryQueryInstanceDirectory(queryInstance.getQueryInstanceDescriptor(), queryInstance.getInstanceManagerID()));
	}

	private void checkConfiguration() {

		if (this.fileStore == null) {

			throw new RuntimeException("No filestore configured for module/query provider " + moduleDescriptor);

		} else if (this.tempFileStore == null) {

			throw new RuntimeException("No temporary filestore configured for module/query provider " + moduleDescriptor);
		}
	}

	@Override
	public void run() {

		while (!shutdown && systemInterface.getSystemStatus() == SystemStatus.STARTED) {

			if (this.tempFileStore == null) {

				log.warn("No temp filestore set, aborting check for abandoned files.");

				sleep();
				continue;
			}

			File tempDir = new File(tempFileStore);

			if (!tempDir.exists()) {

				log.info("Temp directory for query instance files doesn't exist, aborting check for abandoned files.");

				sleep();
				continue;
			}

			//Skip the first run to allow the servlet container to load sessions...
			if (firstRun) {

				firstRun = false;

				sleep();
				continue;
			}

			FlowInstanceManagerRegistery registery = FlowInstanceManagerRegistery.getInstance();

			File[] queryDirs = tempDir.listFiles();

			if (queryDirs != null && queryDirs.length > 0) {

				for (File file : queryDirs) {

					if (!file.isDirectory()) {
						continue;
					}

					if (shutdown) {

						return;
					}

					if (registery.isActiveInstance(file.getName()) || (System.currentTimeMillis() - file.lastModified()) < MillisecondTimeUnits.MINUTE) {

						continue;
					}

					String[] dirContents = file.list();

					if (dirContents != null && dirContents.length > 0) {

						//Only log if the directory is not empty
						log.info("Deleting abandoned files left from flow instance manager ID: " + file.getName());

					} else {

						log.debug("Deleting empty folder left from flow instance manager ID: " + file.getName());
					}

					FileUtils.deleteDirectory(file);
				}
			}

			sleep();
		}

		log.info("Cleanup thread stopped");
	}

	private void sleep() {

		try {
			Thread.sleep(this.cleanupInterval * MillisecondTimeUnits.MINUTE);
		} catch (InterruptedException e) {}
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws SQLException {

		FileUploadQuery query = getQuery(sourceQueryDescriptor.getQueryID());

		query.setQueryID(copyQueryDescriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, new RelationQuery(FileUploadQuery.ALLOWED_FILE_EXTENSIONS_RELATION));
	}

	@Override
	public QueryRequestProcessor getQueryRequestProcessor(FileUploadQueryInstance queryInstance, HttpServletRequest req, User user) throws IOException {

		if (CollectionUtils.isEmpty(queryInstance.getFiles())) {

			return null;
		}

		Integer fileID = NumberUtils.toInt(req.getParameter("file"));

		if (fileID == null) {

			return null;
		}

		//Immutable query
		if (queryInstance.getInstanceManagerID() == null) {

			for (FileDescriptor fileDescriptor : queryInstance.getFiles()) {

				if (fileDescriptor.getFileID().equals(fileID)) {

					File file = new File(getFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor()));

					if (!file.exists()) {

						return null;
					}

					File tempFile = File.createTempFile("ImmutableQueryInstance-" + queryInstance.getQueryInstanceID() + "-", "-download", new File(this.tempFileStore));

					FileUtils.copyFile(file, tempFile);

					return new FileUploadQueryRequestProcessor(fileDescriptor.getName(), tempFile);
				}
			}

			//Mutable query
		} else {

			for (FileDescriptor fileDescriptor : queryInstance.getFiles()) {

				if (fileDescriptor.getTemporaryFileID().equals(fileID)) {

					File file = new File(getTemporaryFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor(), queryInstance.getInstanceManagerID()));

					if (!file.exists()) {

						return null;
					}

					File tempFile = File.createTempFile("MutableQueryInstance-" + queryInstance.getQueryInstanceID() + "-", "-download", new File(this.tempFileStore));

					FileUtils.copyFile(file, tempFile);

					return new FileUploadQueryRequestProcessor(fileDescriptor.getName(), tempFile);
				}
			}
		}

		return null;
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, FileUploadQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler), systemInterface.getEncoding()));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected List<PDFAttachment> getPDFAttachments(FileUploadQueryInstance queryInstance) {

		if (queryInstance.getFiles() == null) {

			return null;
		}
		
		FileUploadQuery query = queryInstance.getQuery();

		List<PDFAttachment> attachments = new ArrayList<PDFAttachment>(queryInstance.getFiles().size());
		
		for (FileDescriptor fileDescriptor : queryInstance.getFiles()) {
			
			File file;
			
			if (queryInstance.getQueryInstanceDescriptor().getFlowInstanceID() != null) {
				
				file = new File(getFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor()));
				
			} else {
				
				file = new File(getTemporaryFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor(), queryInstance.getInstanceManagerID()));
			}
			
			AttachmentNamePrefixType prefixMode = query.getAttachmentNamePrefixMode();
			boolean inline = query.isInlinePDFAttachments() && file.getName().toLowerCase().endsWith(".pdf");
			boolean appendPageNumber = query.isNumberInlineAttachments();
			
			if (prefixMode == AttachmentNamePrefixType.QUERY_NAME || prefixMode == null) {
				
				attachments.add(new PDFFileAttachment(file, fileDescriptor.getName(), pdfAttachmentDescriptionPrefix + " " + queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getName(), inline, appendPageNumber));
				
			} else if (prefixMode == AttachmentNamePrefixType.CUSTOM) {
				
				attachments.add(new PDFFileAttachment(file, fileDescriptor.getName(), query.getAttachmentNameCustomPrefix() + " " + fileDescriptor.getName(), inline, appendPageNumber));
				
			} else if (prefixMode == AttachmentNamePrefixType.NO_PREFIX) {
				
				attachments.add(new PDFFileAttachment(file, fileDescriptor.getName(), fileDescriptor.getName(), inline, appendPageNumber));
				
			} else {
				
				attachments.add(new PDFFileAttachment(file, fileDescriptor.getName(), pdfAttachmentDescriptionPrefix + " " + queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getName(), inline, appendPageNumber));
				log.error("Unknown prefix mode " + prefixMode + " for query " + query);
			}
		}

		return attachments;
	}

	@Override
	protected PDFResourceProvider getPDFResourceProvider(FileUploadQueryInstance queryInstance) {

		return FILE_ICON_RESOURCE_PROVIDER;
	}

	public int getMaxAllowedFileSize() {

		return maxAllowedFileSize;
	}

	@Override
	public Document createDocument(HttpServletRequest req, User poster) {

		Document doc = super.createDocument(req, poster);

		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "FormatedMaxAllowedFileSize", BinarySizeFormater.getFormatedSize(maxAllowedFileSize * BinarySizes.MegaByte));

		return doc;
	}

	public void appendFileExportXML(Document doc, Element targetElement, FileUploadQueryInstance queryInstance) throws IOException {

		if (CollectionUtils.isEmpty(queryInstance.getFiles())) {

			return;
		}

		//Immutable query
		if (queryInstance.getInstanceManagerID() == null) {

			for (FileDescriptor fileDescriptor : queryInstance.getFiles()) {

				File file = new File(getFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor()));

				if (!file.exists()) {

					continue;
				}

				appendFileXML(doc, targetElement, fileDescriptor, file);
			}

			//Mutable query
		} else {

			for (FileDescriptor fileDescriptor : queryInstance.getFiles()) {

				File file = new File(getTemporaryFilePath(fileDescriptor, queryInstance.getQueryInstanceDescriptor(), queryInstance.getInstanceManagerID()));

				if (!file.exists()) {

					continue;
				}

				appendFileXML(doc, targetElement, fileDescriptor, file);
			}
		}

	}

	private void appendFileXML(Document doc, Element targetElement, FileDescriptor fileDescriptor, File file) throws IOException {

		Element fileElement = doc.createElement("File");

		XMLUtils.appendNewElement(doc, fileElement, "ID", fileDescriptor.getFileID());
		XMLUtils.appendNewCDATAElement(doc, fileElement, "Name", fileDescriptor.getName());
		XMLUtils.appendNewElement(doc, fileElement, "Size", fileDescriptor.getSize());
		XMLUtils.appendNewElement(doc, fileElement, "EncodedData", Base64.encodeFromFile(file));

		targetElement.appendChild(fileElement);
	}

	@Override
	protected Class<FileUploadQueryInstance> getQueryInstanceClass() {

		return FileUploadQueryInstance.class;
	}

}
