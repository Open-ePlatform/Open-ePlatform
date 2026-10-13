package com.nordicpeak.flowengine.queries.fileinfoquery;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

public class FileInfoQueryCRUD extends BaseQueryCRUD<FileInfoQuery, FileInfoQueryProviderModule> {

	protected AnnotatedDAOWrapper<FileInfoQuery, Integer> queryDAO;

	public FileInfoQueryCRUD(AnnotatedDAOWrapper<FileInfoQuery, Integer> queryDAO, BeanRequestPopulator<FileInfoQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, FileInfoQueryProviderModule callback) {

		super(FileInfoQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected FileInfoQuery populateFromUpdateRequest(FileInfoQuery oldQuery, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		FileInfoQuery query = super.populateFromUpdateRequest(oldQuery, req, user, uriParser);

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);

		if (oldQuery.isHideTitle() && oldQuery.getDescription() == null) {

			throw new ValidationException(new ValidationError("DescriptionRequiredWhenTitleHidden"));
		}

		callback.populateFiles((MultipartRequest) req, validationErrors, oldQuery, query, user);

		if (!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		return query;
	}

	@Override
	protected void appendUpdateFormData(FileInfoQuery bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendUpdateFormData(bean, doc, updateTypeElement, user, req, uriParser);

		XMLUtils.appendNewElement(doc, updateTypeElement, "MaxFileSize", callback.getMaxFileSize());
		XMLUtils.appendNewElement(doc, (Element) doc.getFirstChild(), "FullAlias", callback.getFullAlias());
	}

	@Override
	protected ForegroundModuleResponse beanDeleted(FileInfoQuery profile, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.deleteFiles(profile);

		return super.beanDeleted(profile, req, res, user, uriParser);
	}

	@Override
	protected HttpServletRequest parseRequest(HttpServletRequest req, User user) throws ValidationException, Exception {

		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {
				return callback.parseMultipartRequest(req, user);

			} catch (FileSizeLimitExceededException e) {

				throw new ValidationException(new ValidationError("FileSizeLimitExceeded"));

			} catch (FileUploadException e) {

				log.warn(e);
				throw new ValidationException(new ValidationError("UnableToParseRequest"));
			}

		} else {

			return super.parseRequest(req, user);
		}
	}

	@Override
	protected void releaseRequest(HttpServletRequest req, User user) {

		if (req instanceof MultipartRequest) {
			((MultipartRequest) req).deleteFiles();
		}
	}

}
