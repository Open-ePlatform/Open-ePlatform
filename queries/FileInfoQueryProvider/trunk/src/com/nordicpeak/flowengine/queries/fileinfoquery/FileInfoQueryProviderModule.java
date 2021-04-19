package com.nordicpeak.flowengine.queries.fileinfoquery;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.validationerrors.FileSizeLimitExceededValidationError;
import se.unlogic.hierarchy.core.validationerrors.UnableToSaveFileValidationError;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.CommitCallback;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.io.StartsWithFileFilter;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
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

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.PDFAttachment;
import com.nordicpeak.flowengine.interfaces.PDFResourceProvider;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryContentFilter;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.utils.FileIconResourceProvider;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.PDFFileAttachment;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

public class FileInfoQueryProviderModule extends BaseQueryProviderModule<FileInfoQueryInstance> implements BaseQueryCRUDCallback {

	private static final FileIconResourceProvider FILE_ICON_RESOURCE_PROVIDER = new FileIconResourceProvider();

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File store", description = "Directory where uploaded files are stored", required = true)
	protected String filestore;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Temp filestore", description = "The directory where temporary files are stored ")
	protected String tempFileStore;

	protected File tempFileStoreFile;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Disk threshold", description = "Maximum size of files in MB to be buffered on disk during file uploads.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer diskThreshold = 1000;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max file size", description = "Maximum size of individual files in MB for file uploads.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer maxFileSize = 100;

	@XSLVariable(prefix = "java.")
	protected String pdfAttachmentDescriptionPrefix = "A file from query:";

	private AnnotatedDAO<FileInfoQuery> queryDAO;
	private AnnotatedDAO<FileDescriptor> fileDescriptorDAO;
	private AnnotatedDAO<FileInfoQueryInstance> queryInstanceDAO;

	private AnnotatedDAOWrapper<FileDescriptor, Integer> fileDescriptorDAOWrapper;

	private FileInfoQueryCRUD queryCRUD;

	private QueryParameterFactory<FileInfoQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<FileInfoQueryInstance, Integer> queryInstanceIDParamFactory;

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		if (StringUtils.isEmpty(filestore)) {

			log.error("Filestore not set");

		} else if (!FileUtils.isReadable(filestore)) {

			log.error("Filestore not found/readable");
		}

		if (StringUtils.isEmpty(tempFileStore)) {

			log.error("Temp filestore not set");
			tempFileStoreFile = null;

		} else if (!FileUtils.isReadable(tempFileStore)) {

			log.error("Temp filestore not found/readable");
			tempFileStoreFile = null;

		} else {

			tempFileStoreFile = new File(tempFileStore);
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FileInfoQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(FileInfoQuery.class);
		fileDescriptorDAO = daoFactory.getDAO(FileDescriptor.class);
		queryInstanceDAO = daoFactory.getDAO(FileInfoQueryInstance.class);

		fileDescriptorDAOWrapper = daoFactory.getDAO(FileDescriptor.class).getWrapper(Integer.class);

		queryCRUD = new FileInfoQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<FileInfoQuery>(FileInfoQuery.class), FileInfoQuery.class.getSimpleName(), "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		checkConfiguration();

		FileInfoQuery query = new FileInfoQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap, QueryContentFilter contentFilter) throws Throwable {

		checkConfiguration();

		FileInfoQuery query = new FileInfoQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));
		
		contentFilter.filterHTML(query);

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

		checkConfiguration();

		FileInfoQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		checkConfiguration();

		FileInfoQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		checkConfiguration();

		FileInfoQueryInstance queryInstance;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new FileInfoQueryInstance();

		} else {

			queryInstance = getQueryInstance(descriptor.getQueryInstanceID());

			if (queryInstance == null) {

				return null;
			}
		}

		queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

		if (queryInstance.getQuery() == null) {

			return null;
		}

		if (req != null) {

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}
		
		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		return queryInstance;
	}

	private FileInfoQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<FileInfoQuery> query = new HighLevelQuery<FileInfoQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private FileInfoQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<FileInfoQuery> query = new HighLevelQuery<FileInfoQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private FileInfoQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<FileInfoQueryInstance> query = new HighLevelQuery<FileInfoQueryInstance>(FileInfoQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(FileInfoQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}

	@Override
	public void populate(FileInfoQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		queryInstance.getQueryInstanceDescriptor().setPopulated(!queryInstance.getQuery().isDontSetPopulated());
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		checkConfiguration();

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		checkConfiguration();

		FileInfoQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		transactionHandler.addCommitCallback(new CommitCallback() {
			
			@Override
			public void commitComplete() {

				log.info("Deleting files for query instance " + descriptor);
				
				try {
					deleteFiles(query);
				
				} catch (Throwable t) {

					log.error("Error deleting files for query instance " + descriptor, t);
				}
			}
		});	
		
		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		FileInfoQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

		if (queryInstance == null) {

			return false;
		}

		this.queryInstanceDAO.delete(queryInstance, transactionHandler);

		return true;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@Override
	public QueryRequestProcessor getQueryRequestProcessor(FileInfoQueryInstance queryInstance, HttpServletRequest req, User user) throws IOException {

		if (CollectionUtils.isEmpty(queryInstance.getQuery().getFiles())) {

			return null;
		}

		Integer fileID = NumberUtils.toInt(req.getParameter("file"));

		if (fileID == null) {

			return null;
		}

		for (FileDescriptor fileDescriptor : queryInstance.getQuery().getFiles()) {

			if (fileDescriptor.getFileID().equals(fileID)) {

				File file = new File(getFileDescriptorFilestorePath(queryInstance.getQuery(), fileDescriptor));

				if (!file.exists()) {

					log.warn("File " + file.getAbsolutePath() + " belonging to query " + queryInstance.getQuery() + " is missing!");
					return null;
				}

				return new FileInfoQueryRequestProcessor(fileDescriptor.getName(), file);
			}
		}

		return null;
	}

	@WebPublic(alias = "file")
	public ForegroundModuleResponse downloadFile(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer queryID;
		Integer fileDescriptorID;

		FileInfoQuery query;
		FileDescriptor fileDescriptor;

		if (uriParser.size() == 4 && (queryID = uriParser.getInt(2)) != null && (fileDescriptorID = uriParser.getInt(3)) != null && (fileDescriptor = fileDescriptorDAOWrapper.get(fileDescriptorID)) != null && (query = getQuery(queryID)) != null) {

			QueryDescriptor queryDescriptor = getFlowAdminModule().getQueryDescriptor(query.getQueryID());
			
			getFlowAdminModule().checkFlowStructureManipulationAccess(user, queryDescriptor.getStep().getFlow());
			
			File file = new File(getFileDescriptorFilestorePath(query, fileDescriptor));

			if (file.exists()) {

				FileInfoQueryRequestProcessor requestProcessor = new FileInfoQueryRequestProcessor(fileDescriptor.getName(), file);

				try {
					requestProcessor.processRequest(req, res, user, uriParser);

				} finally {
					requestProcessor.close();
				}

				return null;

			} else {

				log.warn("File " + file.getAbsolutePath() + " belonging to query " + query + " is missing!");
			}
		}

		throw new URINotFoundException(uriParser);
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws SQLException, IOException {

		FileInfoQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		List<FileDescriptor> fileDescriptors = query.getFiles();

		Map<File, FileDescriptor> fileMap = null;
		
		if(fileDescriptors != null) {
			
			fileMap = new HashMap<File, FileDescriptor>(fileDescriptors.size());
			
			for(FileDescriptor fileDescriptor : fileDescriptors) {
				
				File existingFile = new File(getFileDescriptorFilestorePath(getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler), fileDescriptor));
				
				//Reset file descriptor
				fileDescriptor.setFileID(null);
				fileDescriptor.setQuery(query);
				
				fileMap.put(existingFile, fileDescriptor);
			}
		}				

		//Write to database and get new file ID's for file descriptors, then copy files on disc
		queryDAO.add(query, transactionHandler, null);
		
		if(!CollectionUtils.isEmpty(fileMap)) {
		
			for(File existingFile : fileMap.keySet()) {
				
				FileDescriptor resetFileDescriptor = fileMap.get(existingFile);
				
				File destinationFile = new File(getFileDescriptorFilestorePath(query, resetFileDescriptor));
				
				FileUtils.copyFile(existingFile, destinationFile);
			}
		
		}
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, FileInfoQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler), systemInterface.getEncoding()));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected List<PDFAttachment> getPDFAttachments(FileInfoQueryInstance queryInstance) {

		if (queryInstance.getQuery().getFiles() == null) {

			return null;
		}

		List<PDFAttachment> attachments = new ArrayList<PDFAttachment>(queryInstance.getQuery().getFiles().size());

		for (FileDescriptor fileDescriptor : queryInstance.getQuery().getFiles()) {

			attachments.add(new PDFFileAttachment(new File(getFileDescriptorFilestorePath(queryInstance.getQuery(), fileDescriptor)), fileDescriptor.getName(), this.pdfAttachmentDescriptionPrefix + " " + queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getName()));
		}

		return attachments;
	}

	@Override
	protected PDFResourceProvider getPDFResourceProvider(FileInfoQueryInstance queryInstance) {

		return FILE_ICON_RESOURCE_PROVIDER;
	}

	@Override
	protected Class<FileInfoQueryInstance> getQueryInstanceClass() {

		return FileInfoQueryInstance.class;
	}

	public int getMaxFileSize() {

		return maxFileSize;
	}

	private void checkConfiguration() {

		if (filestore == null) {

			throw new RuntimeException("No filestore configured for module/query provider " + moduleDescriptor);
		}
	}

	public String getQueryFilestorePath(FileInfoQuery query) {

		return this.filestore + File.separator + "query-" + query.getQueryID() + "-";
	}

	public String getFileDescriptorFilestorePath(FileInfoQuery query, FileDescriptor fileDescriptor) {

		return getQueryFilestorePath(query) + fileDescriptor.getFileID();
	}

	public MultipartRequest parseMultipartRequest(HttpServletRequest req, User user) throws FileUploadException {

		return MultipartRequest.getMultipartRequest(ramThreshold * BinarySizes.KiloByte, diskThreshold * BinarySizes.MegaByte, (long) (maxFileSize * BinarySizes.MegaByte), tempFileStore, req);
	}

	public void deleteFiles(FileInfoQuery query) {

		if (!CollectionUtils.isEmpty(query.getFiles())) {

			FileUtils.deleteFiles(filestore, new StartsWithFileFilter("query-" + query.getQueryID() + "-", false), false);
		}
	}

	public void populateFiles(MultipartRequest req, List<ValidationError> validationErrors, FileInfoQuery oldQuery, FileInfoQuery query, User user) throws SQLException {

		if (!CollectionUtils.isEmpty(oldQuery.getFiles())) {

			List<FileDescriptor> existingFileDescriptors = new LinkedList<FileDescriptor>(oldQuery.getFiles());

			// Check if any existing files should be deleted
			if (!CollectionUtils.isEmpty(existingFileDescriptors)) {

				for (Iterator<FileDescriptor> it = existingFileDescriptors.iterator(); it.hasNext();) {

					FileDescriptor existingFileDescriptor = it.next();

					// Check if the file should be deleted
					if (req.getParameter("fileuploader_delete_" + existingFileDescriptor.getFileID()) != null) {

						it.remove();

						File existingFile = new File(getFileDescriptorFilestorePath(oldQuery, existingFileDescriptor));

						log.info("User " + user + " removing file " + existingFile.getName());

						if (!FileUtils.deleteFile(existingFile)) {

							log.warn("File " + existingFile + " missing");
						}

						fileDescriptorDAO.delete(existingFileDescriptor);
					}
				}
			}
		}

		//Get any new files
		List<FileItem> fileItems = req.getFiles("fileuploader_newfile");

		if (fileItems != null) {

			//Parse files and write them to the temporary directory for this query instance

			for (FileItem fileItem : fileItems) {

				if (StringUtils.isEmpty(fileItem.getName())) {

					continue;
				}

				long maxFileSizeInBytes = maxFileSize * BinarySizes.MegaByte;

				// Validate file size
				if (fileItem.getSize() > maxFileSizeInBytes) {

					validationErrors.add(new FileSizeLimitExceededValidationError(FilenameUtils.getName(fileItem.getName()), fileItem.getSize(), maxFileSizeInBytes));
					continue;
				}

				// Create file descriptor
				FileDescriptor fileDescriptor = new FileDescriptor();
				fileDescriptor.setName(FilenameUtils.getName(fileItem.getName()));
				fileDescriptor.setSize(fileItem.getSize());
				fileDescriptor.setQuery(query);

				// Save file
				File destinationFile = null;

				TransactionHandler transactionHandler = null;

				try {
					transactionHandler = new TransactionHandler(dataSource);
					fileDescriptorDAO.add(fileDescriptor, transactionHandler, null);

					destinationFile = new File(getFileDescriptorFilestorePath(oldQuery, fileDescriptor));

					fileItem.write(destinationFile);

					transactionHandler.commit();

				} catch (Exception e) {

					if (destinationFile != null) {

						log.error("Error saving file item " + FilenameUtils.getName(fileItem.getName()) + " as file " + destinationFile.getAbsolutePath(), e);
					} else {

						log.error("Error saving file item " + FilenameUtils.getName(fileItem.getName()), e);
					}
					validationErrors.add(new UnableToSaveFileValidationError(FilenameUtils.getName(fileItem.getName())));

					continue;

				} finally {

					TransactionHandler.autoClose(transactionHandler);
				}

				// Add file descriptor to query instance
				if (query.getFiles() == null) {

					query.setFiles(new ArrayList<FileDescriptor>());
				}

				query.getFiles().add(fileDescriptor);
			}
		}
	}

	@WebPublic(alias = "fileicon")
	public ForegroundModuleResponse getFileIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (uriParser.size() != 3) {

			throw new URINotFoundException(uriParser);
		}

		FileIconHandler.streamIconByFilename(uriParser.get(2), res);

		return null;
	}
}
