package com.nordicpeak.flowengine.cruds;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.fileattachments.FileAttachment;
import se.unlogic.standardutils.fileattachments.FileAttachmentUtils;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.interfaces.MessageCRUDCallback;
import com.nordicpeak.flowengine.utils.FlowEngineFileAttachmentUtils;

public class ExternalMessageCRUD extends BaseMessageCRUD<ExternalMessage, ExternalMessageAttachment> {

	public ExternalMessageCRUD(AnnotatedDAO<ExternalMessage> messageDAO, AnnotatedDAO<ExternalMessageAttachment> attachmentDAO, MessageCRUDCallback callback, boolean manager) {

		super(messageDAO, attachmentDAO, callback, ExternalMessage.class, ExternalMessageAttachment.class, manager);
	}

	public ExternalMessage add(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, Document doc, Element element, FlowInstance flowInstance, boolean postedByManager) throws SQLException, IOException {

		List<ValidationError> validationErrors = new ArrayList<>();

		req = parseRequest(req, validationErrors);
		
		TransactionHandler transactionHandler = null;
		
		List<FileAttachment> addedFileAttachments = null;
		
		try {

			ExternalMessage externalMessage = create(req, res, uriParser, user, flowInstance, postedByManager, validationErrors);

			if (externalMessage != null) {
				
				log.info("User " + user + " adding external message for flowinstance " + flowInstance);

				transactionHandler = messageDAO.createTransaction();

				messageDAO.add(externalMessage, transactionHandler, null);
				
				addedFileAttachments = FlowEngineFileAttachmentUtils.saveAttachmentData(callback.getFileAttachmentHandler(), externalMessage);
				
				transactionHandler.commit();
			}

			XMLUtils.append(doc, element, validationErrors);

			return externalMessage;

		}catch(Throwable t){	
			
			FileAttachmentUtils.deleteFileAttachments(addedFileAttachments);
			
			throw t;
			
		} finally {
			
			TransactionHandler.autoClose(transactionHandler);

			if (req instanceof MultipartRequest) {

				((MultipartRequest) req).deleteFiles();
			}
		}
		
	}

	public ExternalMessage create(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, FlowInstance flowInstance, boolean postedByManager, List<ValidationError> validationErrors) throws SQLException, IOException {
		
		String message = ValidationUtils.validateParameter("externalmessage", req, true, 1, 65535, StringPopulator.getPopulator(), validationErrors);
		
		List<ExternalMessageAttachment> attachments = getAttachments(req, user, validationErrors);
		
		return create(message, attachments, user, flowInstance, postedByManager, validationErrors);
	}
	
	public ExternalMessage create(String message, List<ExternalMessageAttachment> attachments, User user, FlowInstance flowInstance, boolean postedByManager, List<ValidationError> validationErrors) {

		ExternalMessage externalMessage = null;

		if (attachments != null && flowInstance.getFlow().isHideExternalMessageAttachments()) {

			log.warn("User " + user + " tried adding an external message for flowinstance " + flowInstance + " with an attachment while attachments are disabled.");

			validationErrors.add(new ValidationError("UnableToParseRequest"));
		}

		if (validationErrors.isEmpty()) {

			externalMessage = new ExternalMessage();
			externalMessage.setFlowInstance(flowInstance);
			externalMessage.setPoster(user);
			externalMessage.setMessage(message);
			externalMessage.setAdded(TimeUtils.getCurrentTimestamp());
			externalMessage.setAttachments(attachments);
			externalMessage.setPostedByManager(postedByManager);
		}

		return externalMessage;
	}

	@Override
	protected Field getFlowInstanceRelation() {
		
		return ExternalMessage.FLOWINSTANCE_RELATION;
	}

	public void add(ExternalMessage externalMessage, TransactionHandler transactionHandler) throws SQLException {

		messageDAO.add(externalMessage, transactionHandler, null);
	}

}
