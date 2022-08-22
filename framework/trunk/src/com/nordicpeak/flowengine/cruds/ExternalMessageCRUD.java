package com.nordicpeak.flowengine.cruds;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.standardutils.bool.BooleanUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.BaseFlowModule;
import com.nordicpeak.flowengine.MessageHandler;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.ExternalMessageReadReceipt;
import com.nordicpeak.flowengine.beans.ExternalMessageReadReceiptAttachmentDownload;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.interfaces.FlowInstanceAccessController;
import com.nordicpeak.flowengine.interfaces.MessageCRUDCallback;

public class ExternalMessageCRUD extends BaseMessageCRUD<ExternalMessage, ExternalMessageAttachment> {

	private static final Field[] RELATIONS = { ExternalMessage.FLOWINSTANCE_RELATION, ExternalMessage.READ_RECEIPTS_RELATION, ExternalMessageReadReceipt.ATTACHMENT_DOWNLOADS_RELATION };

	public ExternalMessageCRUD(MessageHandler messageHandler, AnnotatedDAO<ExternalMessage> messageDAO, AnnotatedDAO<ExternalMessageAttachment> attachmentDAO, MessageCRUDCallback callback, boolean manager) {

		super(messageHandler, messageDAO, attachmentDAO, callback, ExternalMessage.class, ExternalMessageAttachment.class, manager);
	}

	public ExternalMessage add(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, Document doc, Element element, FlowInstance flowInstance, boolean postedByManager, List<String> allowedFileExtensions) throws SQLException, IOException {

		List<ValidationError> validationErrors = new ArrayList<>();

		req = parseRequest(req, validationErrors);

		try {

			ExternalMessage externalMessage = create(req, res, uriParser, user, flowInstance, postedByManager, validationErrors, allowedFileExtensions);

			if (externalMessage != null) {

				messageHandler.add(externalMessage, postedByManager);
			}

			XMLUtils.append(doc, element, validationErrors);
			element.appendChild(RequestUtils.getRequestParameters(req, doc, "externalmessage"));

			return externalMessage;

		} finally {

			if (req instanceof MultipartRequest) {

				((MultipartRequest) req).deleteFiles();
			}
		}

	}

	public ExternalMessage create(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, FlowInstance flowInstance, boolean postedByManager, List<ValidationError> validationErrors, List<String> allowedFileExtensions) throws SQLException, IOException {

		String message = ValidationUtils.validateParameter("externalmessage", req, true, 1, 65535, StringPopulator.getPopulator(), validationErrors);
		boolean readReceiptEnabled = BooleanUtils.toBoolean(req.getParameter("externalmessage-readreceiptenabled"));

		List<ExternalMessageAttachment> attachments = getAttachments(req, user, validationErrors, allowedFileExtensions, "externalmessage-attachments");

		return create(message, attachments, readReceiptEnabled, user, flowInstance, postedByManager, validationErrors);
	}

	public ExternalMessage create(String message, List<ExternalMessageAttachment> attachments, boolean readReceiptEnabled, User user, FlowInstance flowInstance, boolean postedByManager, List<ValidationError> validationErrors) {

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

			if (flowInstance.getFlow().isReadReceiptsEnabled()) {

				externalMessage.setReadReceiptEnabled(readReceiptEnabled);
			}
		}

		return externalMessage;
	}

	@Override
	protected Field[] getRelations() {

		return RELATIONS;
	}

	@Override
	protected boolean hasMessageAccess(ExternalMessage message, User user) {

		return message.hasReadReceiptAccess(user);
	}

	public ExternalMessageReadReceipt addReadReceipt(User user, URIParser uriParser, FlowInstanceAccessController accessController) throws SQLException, ValidationException, URINotFoundException, AccessDeniedException {

		ExternalMessage externalMessage;

		Integer messageID = uriParser.getInt(2);

		if (uriParser.size() != 3 || messageID == null || (externalMessage = getMessage(messageID)) == null) {

			throw new URINotFoundException(uriParser);
		}

		accessController.checkFlowInstanceAccess(externalMessage.getFlowInstance(), user);

		if (!externalMessage.getFlowInstance().getFlow().isEnabled() || callback.isOperatingStatusDisabled(externalMessage.getFlowInstance(), manager)) {

			throw new ValidationException(Collections.singletonList(BaseFlowModule.FLOW_DISABLED_VALIDATION_ERROR));
		}

		if (!externalMessage.isReadReceiptEnabled() || !externalMessage.getFlowInstance().getFlow().isReadReceiptsEnabled()) {

			return null;
		}

		ExternalMessageReadReceipt readReceipt = CollectionUtils.find(externalMessage.getReadReceipts(), r -> r.getUser().equals(user));

		if (readReceipt != null) {

			externalMessage.setReadReceipts(null);
			readReceipt.setMessage(externalMessage);
			
			return readReceipt;
		}

		readReceipt = new ExternalMessageReadReceipt(externalMessage, user);

		messageHandler.addReadReceipt(readReceipt, externalMessage);

		return readReceipt;
	}

	@Override
	protected void requestedMessageAttachmentDownloaded(ExternalMessage externalMessage, ExternalMessageAttachment attachment, User user, boolean manager) throws SQLException {

		if (manager) {
			
			return;
		}
		
		ExternalMessageReadReceipt readReceipt = CollectionUtils.find(externalMessage.getReadReceipts(), r -> r.getUser().equals(user));

		if (readReceipt == null) {

			return;
		}

		String fileName = attachment.getFilename();

		ExternalMessageReadReceiptAttachmentDownload attachmentDownload = CollectionUtils.find(readReceipt.getAttachmentDownloads(), d -> d.getAttachmentFilename().equals(fileName));

		if (attachmentDownload != null) {

			return;
		}

		attachmentDownload = new ExternalMessageReadReceiptAttachmentDownload(readReceipt, fileName);

		messageHandler.addAttachmentDownload(attachmentDownload, externalMessage);
	}

	public List<ExternalMessageReadReceipt> getReadReceipts(User user, URIParser uriParser, FlowInstanceAccessController accessController, boolean manager) throws SQLException, AccessDeniedException, URINotFoundException, ValidationException {

		ExternalMessage message;

		Integer messageID = uriParser.getInt(2);

		if (uriParser.size() != 3 || messageID == null || (message = getMessage(messageID)) == null) {

			throw new URINotFoundException(uriParser);
		}

		accessController.checkFlowInstanceAccess(message.getFlowInstance(), user);

		if (!message.getFlowInstance().getFlow().isEnabled() || callback.isOperatingStatusDisabled(message.getFlowInstance(), manager)) {

			throw new ValidationException(Collections.singletonList(BaseFlowModule.FLOW_DISABLED_VALIDATION_ERROR));
		}

		if (!message.isReadReceiptEnabled()) {

			log.warn("User " + user + " is trying to get read receipts for " + message + " where read receipts are disabled");
			return null;
		}

		if (manager) {

			log.info("User " + user + " getting all read receipts for " + message);

			return message.getReadReceipts();
		}

		List<ExternalMessageReadReceipt> readReceipts = CollectionUtils.filter(message.getReadReceipts(), r -> r.getUser().equals(user));

		log.info("User " + user + " getting read receipts for " + message);

		return readReceipts;
	}

	public ExternalMessage disableReadReceipt(User user, URIParser uriParser, FlowInstanceAccessController accessController) throws SQLException, AccessDeniedException, URINotFoundException, ValidationException {

		ExternalMessage message;

		Integer messageID = uriParser.getInt(2);

		if (uriParser.size() != 3 || messageID == null || (message = getMessage(messageID)) == null) {

			throw new URINotFoundException(uriParser);
		}

		accessController.checkFlowInstanceAccess(message.getFlowInstance(), user);

		if (!message.getFlowInstance().getFlow().isEnabled() || callback.isOperatingStatusDisabled(message.getFlowInstance(), manager)) {

			throw new ValidationException(Collections.singletonList(BaseFlowModule.FLOW_DISABLED_VALIDATION_ERROR));
		}

		if (message.isReadReceiptEnabled() && message.getReadReceipts() == null) {

			log.info("User " + user + " disabling read receipt for " + message);

			message.setReadReceiptEnabled(false);

			messageDAO.update(message);
		}

		return message;
	}

}
