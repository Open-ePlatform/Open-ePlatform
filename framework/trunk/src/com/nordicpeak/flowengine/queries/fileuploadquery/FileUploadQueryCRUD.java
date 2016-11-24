package com.nordicpeak.flowengine.queries.fileuploadquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;

public class FileUploadQueryCRUD extends BaseQueryCRUD<FileUploadQuery, FileUploadQueryProviderModule> {

	protected AnnotatedDAOWrapper<FileUploadQuery, Integer> queryDAO;
	
	public FileUploadQueryCRUD(AnnotatedDAOWrapper<FileUploadQuery, Integer> queryDAO, BeanRequestPopulator<FileUploadQuery> populator, String typeElementName, String typeLogName, String listMethodAlias, FileUploadQueryProviderModule callback) {
		
		super(FileUploadQuery.class, queryDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
		
		this.queryDAO = queryDAO;
	}

	@Override
	protected FileUploadQuery populateFromUpdateRequest(FileUploadQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {
		
		FileUploadQuery query = super.populateFromUpdateRequest(bean, req, user, uriParser);
		
		List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		this.populateQueryDescriptor((QueryDescriptor) query.getQueryDescriptor(), req, validationErrors);
		
		if(query.getMaxFileSize() != null) {
			
			if(query.getMaxFileSize() > callback.getMaxAllowedFileSize()) {
				
				validationErrors.add(new ValidationError("maxFileSize", ValidationErrorType.TooLong));
			}
			
		}
		
		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		if(query.getMaxFileSize() != null) {
			query.setMaxFileSize(query.getMaxFileSize() * BinarySizes.MegaByte);
		}
		
		List<String> allowedFileExtentions = query.getAllowedFileExtensions();
		
		if(allowedFileExtentions != null) {
			
			List<String> fileExtensions = new ArrayList<String>(allowedFileExtentions.size());
			
			for(String fileExtension : allowedFileExtentions) {
				
				fileExtensions.add(fileExtension.toLowerCase());
				
			}
			
			query.setAllowedFileExtensions(fileExtensions);
			
		}
		
		return query;
	}

	@Override
	protected void appendUpdateFormData(FileUploadQuery bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {
		
		super.appendUpdateFormData(bean, doc, updateTypeElement, user, req, uriParser);
		
		if(bean.getMaxFileSize() != null) {
			XMLUtils.appendNewElement(doc, updateTypeElement, "MaxFileSizeInMB", bean.getMaxFileSize() / BinarySizes.MegaByte);
		}

		XMLUtils.appendNewElement(doc, updateTypeElement, "MaxAllowedFileSize", callback.getMaxAllowedFileSize());
		
	}

	@Override
	protected List<Field> getBeanRelations() {
		
		return Arrays.asList(FileUploadQuery.ALLOWED_FILE_EXTENSIONS_RELATION);
	}
	
}
