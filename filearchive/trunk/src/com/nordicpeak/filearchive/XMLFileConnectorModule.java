package com.nordicpeak.filearchive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.nordicpeak.filearchive.beans.File;
import com.nordicpeak.filearchive.beans.FileCollection;

import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.date.PooledSimpleDateFormat;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;


public class XMLFileConnectorModule extends AnnotatedXMLModule {

	private static final PooledSimpleDateFormat LAST_MODIFIED_DATE_FORMATTER = new PooledSimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.ENGLISH,TimeZone.getTimeZone("GMT"));

	@ModuleSetting
	@TextFieldSettingDescriptor(name="File store",description="Directory where uploaded files are stored",required=true)
	protected String fileStore;

	private AnnotatedDAOWrapper<FileCollection, Integer> fileCollectionDAO;
	private AnnotatedDAOWrapper<File, Integer> fileDAO;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception{

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FileArchiveModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if(upgradeResult.isUpgrade()){

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		this.fileDAO = daoFactory.getDAO(File.class).getWrapper("fileID", Integer.class);
		this.fileDAO.setUseRelationsOnGet(true);
		this.fileDAO.disableAutoRelations(true);
		this.fileDAO.addRelation(File.FILE_COLLECTION_RELATION);
		this.fileDAO.addRelation(FileCollection.ALLOWED_GROUPS_RELATION);
		this.fileDAO.addRelation(FileCollection.ALLOWED_USERS_RELATION);

		this.fileCollectionDAO = daoFactory.getDAO(FileCollection.class).getWrapper("collectionID",Integer.class);
	}

	@WebPublic
	public Elementable list(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		log.info("User " + user + " listing file collections.");

		List<FileCollection> fileCollections = fileCollectionDAO.getAll();

		if(fileCollections != null){

			Iterator<FileCollection> iterator = fileCollections.iterator();

			while(iterator.hasNext()){

				FileCollection fileCollection = iterator.next();

				if(!AccessUtils.checkAccess(user, fileCollection)){

					iterator.remove();
				}
			}
		}

		return new FileCollectionResponse(fileCollections);
	}

	@WebPublic
	public Elementable get(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		File file;

		//Get file and verify alias
		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (file = this.fileDAO.get(NumberUtils.toInt(uriParser.get(2)))) == null) {

			throw new URINotFoundException(uriParser);

		}else if(!AccessUtils.checkAccess(user, file.getFileCollection())){

			throw new AccessDeniedException("Access to file collection " + file.getFileCollection() + " denied");
		}

		//Check if the file exists in file system and send the file
		java.io.File fileSystemFile = new java.io.File(fileStore + java.io.File.separator + file.getFileID());

		if (fileSystemFile.exists() && fileSystemFile.canRead() && !fileSystemFile.isDirectory()) {

			log.info("Sending file " + file + " to user " + user + " accessing from " + req.getRemoteHost());

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

			HTTPUtils.setContentLength(fileSystemFile.length(), res);
			res.setHeader("Content-Disposition", "inline; filename=\"" + FileUtils.toValidHttpFilename(file.getName()) + "\"");

			String contentType = MimeUtils.getMimeType(file.getName());

			if (contentType != null) {
				res.setContentType(contentType);
			} else {
				res.setContentType("application/x-unknown-mime-type");
			}

			InputStream inputStream = null;
			OutputStream outputStream = null;

			try {
				// Open the file and output streams
				inputStream = new FileInputStream(fileSystemFile);
				outputStream = res.getOutputStream();

				StreamUtils.transfer(inputStream, outputStream);

			} catch (IOException e) {

				log.info("Error sending file " + file + " to user " + user + ", " + e);

			} finally {
				CloseUtils.close(inputStream);
				CloseUtils.close(outputStream);
			}

		} else {
			// File not found in file system
			log.warn("Filesystem and database are not in sync, unable to find file " + file + " in filesystem");
			throw new URINotFoundException(uriParser);
		}
		return null;
	}
}
