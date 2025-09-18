package com.nordicpeak.filearchive.cruds;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.filearchive.FileArchiveModule;
import com.nordicpeak.filearchive.beans.File;
import com.nordicpeak.filearchive.beans.FileCollection;
import com.nordicpeak.filearchive.enums.AccessMode;
import com.nordicpeak.filearchive.enums.SecurityMode;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.IntegerBasedCRUD;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.enums.EnumUtils;
import se.unlogic.standardutils.hash.HashAlgorithms;
import se.unlogic.standardutils.hash.HashUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.random.RandomUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;

public class FileCollectionCRUD extends IntegerBasedCRUD<FileCollection, FileArchiveModule> {

	public FileCollectionCRUD(CRUDDAO<FileCollection, Integer> crudDAO, BeanRequestPopulator<FileCollection> populator, String typeElementName, String typeLogName, String listMethodAlias, FileArchiveModule fileArchiveModule) {

		super(crudDAO, populator, typeElementName, typeLogName, listMethodAlias, fileArchiveModule);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if (!(AccessUtils.checkAccess(user, callback.getUploadAccessInterface()) || AccessUtils.checkAccess(user, callback.getAdminAccessInterface()))) {

			throw new AccessDeniedException("Add file collection access denied");
		}
	}

	@Override
	protected void checkUpdateAccess(FileCollection bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if (!(AccessUtils.checkAccess(user, callback.getAdminAccessInterface()) || isOwner(bean, user))) {

			throw new AccessDeniedException("Update file collection access denied");
		}
		
		if (bean.isEncrypted()) {
			
			throw new AccessDeniedException("Update file collection access denied for encrypted collection");
		}
	}

	@Override
	protected void checkDeleteAccess(FileCollection bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException {

		if (!(AccessUtils.checkAccess(user, callback.getAdminAccessInterface()) || isOwner(bean, user))) {

			throw new AccessDeniedException("Delete file collection access denied");
		}
	}

	private boolean isOwner(FileCollection fileCollection, User user) {

		if (user != null && fileCollection.getPoster() != null && fileCollection.getPoster().equals(user)) {

			return true;
		}

		return false;
	}

	@Override
	protected void checkListAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, SQLException {
		
		if (!(AccessUtils.checkAccess(user, callback.getUploadAccessInterface()) || AccessUtils.checkAccess(user, callback.getAdminAccessInterface()))) {

			throw new AccessDeniedException("List access denied");
		}
	}

	@Override
	public ForegroundModuleResponse showBean(FileCollection bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		if (bean.getSecurityMode() == SecurityMode.IDENTIFICATION && (user == null || (!user.getAttributeHandler().isSet("citizenIdentifier") && !user.equals(bean.getPoster())))) {
			
			Document doc = this.callback.createDocument(req, uriParser, user);
			Element showTypeElement = doc.createElement("SecureCollection");
			doc.getFirstChild().appendChild(showTypeElement);

			if (user != null) {
				XMLUtils.appendNewElement(doc, showTypeElement, "LoggedIn");
			}
			
			return createShowBeanModuleResponse(bean, doc, req, user, uriParser);
		}
		
		if (bean.isEncrypted() && req.getMethod().equals("POST")) {
			String password = req.getParameter("password");
			
			if (StringUtils.isEmpty(password)) {
				validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("password", ValidationErrorType.RequiredField));
			}
			else {
				String passwordHash = HashUtils.hash(password, HashAlgorithms.SHA256);
				
				if (passwordHash.equals(bean.getEncryptionHash())) {
					SessionUtils.setAttribute("collectionpassword-" + bean.getCollectionID(), password, req);
					
					log.info("User " + user + " decrypting collection " + bean);
				}
				else {
					log.info("User " + user + " failed to decrypt collection " + bean);
					
					validationErrors = CollectionUtils.addAndInstantiateIfNeeded(validationErrors, new ValidationError("InvalidPassword"));
				}
			}
		}
		
		return super.showBean(bean, req, res, user, uriParser, validationErrors);
	}

	@Override
	protected void checkShowAccess(FileCollection fileCollection, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException {
		
		if (uriParser.size() < 4 || !uriParser.get(3).equals(fileCollection.getAlias())) {
			throw new URINotFoundException(uriParser);
		}
		
		if (fileCollection.getPoster().equals(user)) {
			return;
		}

		if (fileCollection.getSecurityMode() == SecurityMode.IDENTIFICATION) {

			if (!fileCollection.getPoster().equals(user) && user != null && user.getAttributeHandler().isSet("citizenIdentifier") && (fileCollection.getCitizenIDs() == null || !fileCollection.getCitizenIDs().contains(user.getAttributeHandler().getString("citizenIdentifier")))) {
				throw new AccessDeniedException("Access to file collection denied");
			}
			
		} else if (fileCollection.getSecurityMode() == SecurityMode.ACCESS) {

			if (!fileCollection.getPoster().equals(user) && !AccessUtils.checkAccess(user, fileCollection) && !AccessUtils.checkAccess(user, callback.getAdminAccessInterface())) {
				throw new AccessDeniedException("Access to file collection denied");
			}
		}
	}

	@Override
	protected List<FileCollection> getAllBeans(User user) throws SQLException {

		if (AccessUtils.checkAccess(user, callback.getAdminAccessInterface())) {

			return super.getAllBeans(user);
		}

		return callback.getUserFileCollections(user);
	}

	@Override
	protected HttpServletRequest parseRequest(HttpServletRequest req, User user) throws ValidationException {

		if (MultipartRequest.isMultipartRequest(req)) {

			try {
				log.info("Parsing multipart request from user " + user + "...");
				return MultipartRequest.getMultipartRequest(callback.getRamThreshold() * BinarySizes.KiloByte, (long) callback.getMaxRequestSize() * BinarySizes.MegaByte, callback.getTempDir(), req);

			} catch (SizeLimitExceededException e) {

				throw new ValidationException(new ValidationError("FileSizeLimitExceeded"));

			} catch (FileSizeLimitExceededException e) {

				throw new ValidationException(new ValidationError("FileSizeLimitExceeded"));

			} catch (FileUploadException e) {

				throw new ValidationException(new ValidationError("UnableToParseRequest"));
			}
		}

		return req;
	}

	private List<File> getNewFiles(HttpServletRequest req, User user, URIParser uriParser) throws SerialException, SQLException {

		if (!(req instanceof MultipartRequest)) {

			return null;
		}

		MultipartRequest multipartRequest = (MultipartRequest) req;

		if (multipartRequest.getFileCount() > 0) {

			Collection<FileItem> fileItems = multipartRequest.getFiles();

			Iterator<FileItem> fileIterator = fileItems.iterator();

			while (fileIterator.hasNext()) {

				FileItem fileItem = fileIterator.next();

				if (StringUtils.isEmpty(fileItem.getName()) || fileItem.getSize() == 0) {

					fileIterator.remove();
					continue;
				}

			}

			List<File> files = new ArrayList<File>();

			for (FileItem fileItem : fileItems) {

				File file = new File();

				String fileName = FilenameUtils.getName(fileItem.getName());

				file.setName(fileName);
				file.setFileItem(fileItem);
				file.setAlias(RandomUtils.getRandomString(12, RandomUtils.LOWER_CASE_CHARACTERS));
				file.setSize(fileItem.getSize());

				files.add(file);
			}

			return files;
		}

		return null;
	}

	@Override
	protected void releaseRequest(HttpServletRequest req, User user) {

		if (req instanceof MultipartRequest) {

			((MultipartRequest) req).deleteFiles();
		}
	}

	@Override
	protected FileCollection populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		FileCollection fileCollection = super.populateFromAddRequest(req, user, uriParser);
		fileCollection.setAlias(RandomUtils.getRandomString(12, RandomUtils.LOWER_CASE_CHARACTERS));

		fileCollection.setFiles(getNewFiles(req, user, uriParser));

		fileCollection.setPosted(TimeUtils.getCurrentTimestamp());
		fileCollection.setPoster(user);

		return fileCollection;
	}

	@Override
	protected FileCollection populateFromUpdateRequest(FileCollection bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		FileCollection fileCollection = super.populateFromUpdateRequest(bean, req, user, uriParser);

		if (fileCollection.getFiles() == null) {

			fileCollection.setFiles(getNewFiles(req, user, uriParser));

		} else {

			List<File> newFiles = getNewFiles(req, user, uriParser);

			if (newFiles != null) {

				bean.getFiles().addAll(newFiles);
			}
		}

		fileCollection.setUpdated(TimeUtils.getCurrentTimestamp());
		fileCollection.setEditor(user);

		return fileCollection;
	}

	@Override
	protected void validateAddPopulation(FileCollection bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		populateAccess(bean, req);
		
		if (bean.getCollectionKeepDays() != null && (bean.getCollectionKeepDays() < 1 || bean.getCollectionKeepDays() > callback.getUserKeepDaysMax())) {
			throw new ValidationException(new ValidationError("collectionKeepDays", ValidationErrorType.InvalidFormat));
		}

		if (callback.isAllowFileEncryption() && bean.isEncrypted()) {
			String password = req.getParameter("encryptionPassword");

			if (password.length() < callback.getEncryptionPasswordLength()) {
			
				throw new ValidationException(new ValidationError("encryptionPassword", ValidationErrorType.TooShort));
			
			} else if (password.length() > callback.getEncryptionPasswordLength()) {
			
				throw new ValidationException(new ValidationError("encryptionPassword", ValidationErrorType.TooLong));
			}

			bean.setEncryptionHash(HashUtils.hash(password, HashAlgorithms.SHA256));
		}
	}

	private void populateAccess(FileCollection bean, HttpServletRequest req) throws ValidationException {

		bean.setCitizenIDs(null);
		bean.setAllowedGroupIDs(null);
		bean.setAllowedUserIDs(null);

		SecurityMode securityMode = null;

		if (callback.isAllowSecureCollections() || callback.getAccessMode() == AccessMode.ACCESS_INTERFACE) {
			securityMode = EnumUtils.toEnum(SecurityMode.class, req.getParameter("securityMode"));
		} else {
			securityMode = SecurityMode.PUBLIC;
		}

		if (securityMode == null) {
			throw new ValidationException(new ValidationError("InvalidSecurityMode"));
		}
		
		if ((securityMode == SecurityMode.PUBLIC && !bean.isEncrypted()) || !callback.isAllowCollabCollections()) {
			bean.setAllowsCollaboration(false);
		}

		if (callback.isAllowSecureCollections() && securityMode == SecurityMode.IDENTIFICATION) {
			String[] citizenIDs = req.getParameterValues("citizenID");

			if (citizenIDs != null) {
				bean.setCitizenIDs(new ArrayList<String>());

				for (String citizenID : citizenIDs) {
					if (!callback.getCitizenIdentifierPopulator().validateFormat(citizenID)) {
						throw new ValidationException(new ValidationError("citizenID", ValidationErrorType.InvalidFormat));
					}

					bean.getCitizenIDs().add(citizenID);
				}

				bean.setCitizenIDs(CollectionUtils.removeDuplicates(bean.getCitizenIDs()));
			}
		} else if (callback.getAccessMode() == AccessMode.ACCESS_INTERFACE && securityMode == SecurityMode.ACCESS) {
			bean.setAllowedUserIDs(NumberUtils.toInt(req.getParameterValues("user")));
			bean.setAllowedGroupIDs(NumberUtils.toInt(req.getParameterValues("group")));
		}

		bean.setSecurityMode(securityMode);
	}

	@Override
	protected void validateUpdatePopulation(FileCollection bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, SQLException, Exception {

		populateAccess(bean, req);
	}

	@Override
	protected void addBean(FileCollection bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		super.addBean(bean, req, user, uriParser);

		writeNewFiles(bean);
	}

	@Override
	protected void updateBean(FileCollection bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		ArrayList<Integer> deleteFileIDs = NumberUtils.toInt(req.getParameterValues("deletefile"));

		ArrayList<File> filesToDelete = null;

		if (deleteFileIDs != null && bean.getFiles() != null) {

			filesToDelete = new ArrayList<File>(deleteFileIDs.size());

			Iterator<File> iterator = bean.getFiles().iterator();

			while (iterator.hasNext()) {

				File file = iterator.next();

				if (deleteFileIDs.contains(file.getFileID())) {

					iterator.remove();
					filesToDelete.add(file);
				}
			}
		}

		super.updateBean(bean, req, user, uriParser);

		deleteFiles(bean, filesToDelete);

		writeNewFiles(bean);
	}

	@Override
	protected void deleteBean(FileCollection bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		super.deleteBean(bean, req, user, uriParser);

		deleteFiles(bean, bean.getFiles());
	}

	private void writeNewFiles(FileCollection bean) throws IOException {

		if (bean.getFiles() != null) {
			
			if (bean.isEncrypted()) {
				try {
					Key secretKey = new SecretKeySpec(bean.getEncryptionPassword().getBytes(), "AES");
					Cipher cipher = Cipher.getInstance("AES");
					cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		
					for (File file : bean.getFiles()) {
						
						if (file.getFileItem() != null) {
							
							String filePath = callback.getFileStore() + java.io.File.separator + file.getFileID();
							
							log.info("Writing encrypted file " + file + " from collection " + bean + " to path " + filePath);
							
							InputStream inputStream = file.getFileItem().getInputStream();
							CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
							
							FileOutputStream outputStream = new FileOutputStream(filePath);
							
							StreamUtils.transfer(cipherInputStream, outputStream);
							
							CloseUtils.close(cipherInputStream);
							CloseUtils.close(inputStream);
							CloseUtils.close(outputStream);
						}
					}
				}
				catch (Exception ex) {
					log.error("Unable to encrypt files in collection " + bean, ex);
				}
			}
			else {
				for (File file : bean.getFiles()) {
					
					if (file.getFileItem() != null) {
						
						String filePath = callback.getFileStore() + java.io.File.separator + file.getFileID();
						
						log.info("Writing file " + file + " from collection " + bean + " to path " + filePath);
						
						FileUtils.writeFile(filePath, file.getFileItem().getInputStream(), true);
					}
				}
			}
		}
	}

	private void deleteFiles(FileCollection bean, List<File> filesToDelete) {

		if (filesToDelete == null) {

			return;
		}

		for (File file : filesToDelete) {

			String filePath = callback.getFileStore() + java.io.File.separator + file.getFileID();

			log.info("Deleting file " + file + " from collection " + bean + " from path " + filePath);

			FileUtils.deleteFile(filePath);
		}
	}

	@Override
	protected List<Breadcrumb> getAddBreadcrumbs(HttpServletRequest req, User user, URIParser uriParser) {

		return CollectionUtils.getList(callback.getDefaultBreadcrumb(), new Breadcrumb(callback.getAddFileCollectionBreadCrumbText(), uriParser.getFormattedURI(), URLType.RELATIVE_FROM_CONTEXTPATH));
	}

	@Override
	protected List<Breadcrumb> getUpdateBreadcrumbs(FileCollection bean, HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		return CollectionUtils.getList(callback.getDefaultBreadcrumb(), new Breadcrumb(callback.getUpdateFileCollectionBreadCrumbText() + getFileCollectionName(bean), uriParser.getFormattedURI(), URLType.RELATIVE_FROM_CONTEXTPATH));
	}

	@Override
	protected List<Breadcrumb> getShowBreadcrumbs(FileCollection bean, HttpServletRequest req, User user, URIParser uriParser) {

		return CollectionUtils.getList(callback.getDefaultBreadcrumb(), new Breadcrumb(getFileCollectionName(bean), uriParser.getFormattedURI(), URLType.RELATIVE_FROM_CONTEXTPATH));
	}

	protected String getFileCollectionName(FileCollection fileCollection) {

		if (fileCollection.getName() != null) {

			return fileCollection.getName();
		}

		return callback.getUnnamedFileCollectionText();
	}

	@Override
	protected void appendShowFormData(FileCollection bean, Document doc, Element showTypeElement, User user, HttpServletRequest req, HttpServletResponse res, URIParser uriParser) throws SQLException, IOException, Exception {

		if (callback.showFileCollectionURL() && AccessUtils.checkAccess(user, callback.getUploadAccessInterface())) {

			showTypeElement.appendChild(doc.createElement("ShowFileCollectionURL"));
		}
		
		XMLUtils.appendNewElement(doc, showTypeElement, "FileCollectionText", callback.getFileCollectionText());
		
		if (SessionUtils.getAttribute("collectionpassword-" + bean.getCollectionID(), req) != null) {
			showTypeElement.appendChild(doc.createElement("FileCollectionPasswordSet"));
		}
		
		if (callback.isDeleteOldFiles()) {
			Timestamp compareDate = null;
			
			if (bean.getUpdated() != null) {
				compareDate = bean.getUpdated();
			}
			else {
				compareDate = bean.getPosted();
			}
			
			if (bean.getCollectionKeepDays() != null) {
				XMLUtils.appendNewElement(doc, showTypeElement, "RemainingDays", bean.getCollectionKeepDays() - DateUtils.getDaysBetween(compareDate, new Date()));
			}
			else {
				XMLUtils.appendNewElement(doc, showTypeElement, "RemainingDays", callback.getKeepFilesDays() - DateUtils.getDaysBetween(compareDate, new Date()));
			}
		}
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(doc, addTypeElement);
		
		if (callback.isAllowUserKeepDays()) {
			XMLUtils.appendNewElement(doc, addTypeElement, "AllowsUserKeepDays");
			XMLUtils.appendNewElement(doc, addTypeElement, "UserKeepDaysMax", callback.getUserKeepDaysMax());
			XMLUtils.appendNewElement(doc, addTypeElement, "UserKeepDaysText", callback.getUserKeepDaysText());
			XMLUtils.appendNewElement(doc, addTypeElement, "DefaultKeepDays", callback.getKeepFilesDays());
		}
	}

	@Override
	protected void appendUpdateFormData(FileCollection bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		appendFormData(doc, updateTypeElement);
		
		AccessUtils.appendAllowedGroupsAndUsers(doc, updateTypeElement, bean, callback.getUserHandler(), callback.getGroupHandler());
	}
	
	public void appendFormData(Document doc, Element addTypeElement) {

		XMLUtils.appendNewElement(doc, addTypeElement, "AccessMode", callback.getAccessMode());

		XMLUtils.appendNewElement(doc, addTypeElement, "AllowSecureCollections", callback.isAllowSecureCollections());
		
		XMLUtils.appendNewElement(doc, addTypeElement, "allowSecureCollabMessage", callback.getAllowSecureCollabMessage());

		if (callback.isAllowFileEncryption()) {
			XMLUtils.appendNewElement(doc, addTypeElement, "AllowsFileEncryption");
			XMLUtils.appendNewElement(doc, addTypeElement, "EncryptionText", callback.getFileEncryptionText());
			XMLUtils.appendNewElement(doc, addTypeElement, "EncryptionPasswordLength", callback.getEncryptionPasswordLength());
		}

		if (callback.isAllowCollabCollections()) {
			XMLUtils.appendNewElement(doc, addTypeElement, "AllowsCollaboration");
		}
		
		if (callback.isHideGroupAccess()) {
			XMLUtils.appendNewElement(doc, addTypeElement, "HideGroupAccess");
		}
	}

	@Override
	protected void appendListFormData(Document doc, Element listTypeElement, User user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		if (AccessUtils.checkAccess(user, callback.getUploadAccessInterface()) || AccessUtils.checkAccess(user, callback.getAdminAccessInterface())) {

			XMLUtils.appendNewElement(doc, listTypeElement, "AddAccess");
		}

		if (!StringUtils.isEmpty(callback.getListCollectionsInfo())) {
			XMLUtils.appendNewElement(doc, listTypeElement, "ListFileCollectionsInfo", callback.getListCollectionsInfo());
		}
	}

	@Override
	protected void appendAllBeans(Document doc, Element listTypeElement, User user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationErrors) throws SQLException {

		if (callback.getAccessMode() == AccessMode.PUBLIC || AccessUtils.checkAccess(user, callback.getAdminAccessInterface())) {

			XMLUtils.appendNewElement(doc, listTypeElement, "AdminAccess");

			super.appendAllBeans(doc, listTypeElement, user, req, uriParser, validationErrors);

		} else {

			List<FileCollection> fileCollections = getAllBeans(user, req, uriParser);

			if (fileCollections != null) {

				Element fileCollectionsElement = doc.createElement(this.typeElementPluralName);
				listTypeElement.appendChild(fileCollectionsElement);

				for (FileCollection fileCollection : fileCollections) {

					Element fileCollectionElement = fileCollection.toXML(doc);
					fileCollectionsElement.appendChild(fileCollectionElement);

					if (isOwner(fileCollection, user)) {

						XMLUtils.appendNewElement(doc, fileCollectionElement, "IsOwner");
					}
				}
			}
		}
	}

	@Override
	protected ForegroundModuleResponse beanAdded(FileCollection bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.redirectToMethod(req, res, "/show/" + bean.getCollectionID() + "/" + bean.getAlias());

		return null;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(FileCollection bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.redirectToMethod(req, res, "/show/" + bean.getCollectionID() + "/" + bean.getAlias());

		return null;
	}
}
