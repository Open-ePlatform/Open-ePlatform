package com.nordicpeak.flowengine.cruds;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.MessageHandler;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.beans.InternalMessageAttachment;
import com.nordicpeak.flowengine.interfaces.MessageCRUDCallback;

public class InternalMessageCRUD extends BaseMessageCRUD<InternalMessage, InternalMessageAttachment> {

	private static Field[] RELATIONS = { InternalMessage.FLOWINSTANCE_RELATION };

	public InternalMessageCRUD(AnnotatedDAO<InternalMessage> messageDAO, MessageHandler messageHandler, AnnotatedDAO<InternalMessageAttachment> attachmentDAO, MessageCRUDCallback callback, boolean manager) {

		super(messageHandler, messageDAO, attachmentDAO, callback, InternalMessage.class, InternalMessageAttachment.class, manager);
	}

	public InternalMessage add(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, Document doc, Element element, FlowInstance flowInstance, List<String> allowedFileExtensions) throws SQLException, IOException {

		List<ValidationError> validationErrors = new ArrayList<>();

		req = parseRequest(req, validationErrors);

		try {

			InternalMessage internalMessage = create(req, user, flowInstance, validationErrors, allowedFileExtensions);

			if (internalMessage != null) {

				messageHandler.add(internalMessage);
			}

			XMLUtils.append(doc, element, validationErrors);
			element.appendChild(RequestUtils.getRequestParameters(req, doc, "internalmessage"));

			return internalMessage;

		} finally {

			if (req instanceof MultipartRequest) {

				((MultipartRequest) req).deleteFiles();
			}
		}

	}

	public InternalMessage create(HttpServletRequest req, User user, FlowInstance flowInstance, List<ValidationError> errors, List<String> allowedFileExtensions) throws SerialException, SQLException {

		String message = ValidationUtils.validateParameter("internalmessage", req, true, 1, 65535, StringPopulator.getPopulator(), errors);

		List<InternalMessageAttachment> attachments = getAttachments(req, user, errors, allowedFileExtensions, "internalmessage-attachments");

		InternalMessage internalMessage = null;

		if (errors.isEmpty()) {

			internalMessage = new InternalMessage();
			internalMessage.setFlowInstance(flowInstance);
			internalMessage.setPoster(user);
			internalMessage.setMessage(message);
			internalMessage.setAdded(TimeUtils.getCurrentTimestamp());
			internalMessage.setAttachments(attachments);
		}

		return internalMessage;
	}

	@Override
	protected Field[] getRelations() {

		return RELATIONS;
	}

	@Override
	protected void requestedMessageAttachmentDownloaded(InternalMessage message, InternalMessageAttachment attachment, User user, boolean manager) throws SQLException {

		return;
	}
}
