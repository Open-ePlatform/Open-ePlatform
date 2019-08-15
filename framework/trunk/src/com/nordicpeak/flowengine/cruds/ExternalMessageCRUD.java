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

public class ExternalMessageCRUD extends BaseMessageCRUD<ExternalMessage, ExternalMessageAttachment> {

	public ExternalMessageCRUD(AnnotatedDAO<ExternalMessage> messageDAO, AnnotatedDAO<ExternalMessageAttachment> attachmentDAO, MessageCRUDCallback callback, boolean manager) {

		super(messageDAO, attachmentDAO, callback, ExternalMessage.class, ExternalMessageAttachment.class, manager);
	}

	public ExternalMessage add(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, Document doc, Element element, FlowInstance flowInstance, boolean postedByManager) throws SQLException, IOException {

		List<ValidationError> validationErrors = new ArrayList<>();

		req = parseRequest(req, validationErrors);
		
		ExternalMessage externalMessage = create(req, res, uriParser, user, flowInstance, postedByManager, validationErrors);

		if (externalMessage != null) {

			log.info("User " + user + " adding external message for flowinstance " + flowInstance);

			messageDAO.add(externalMessage);
		}

		XMLUtils.append(doc, element, validationErrors);

		return externalMessage;

	}

	public ExternalMessage create(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, FlowInstance flowInstance, boolean postedByManager, List<ValidationError> validationErrors) throws SQLException, IOException {

		try {

			ExternalMessage externalMessage = null;

			String message = ValidationUtils.validateParameter("externalmessage", req, true, 1, 65535, StringPopulator.getPopulator(), validationErrors);

			List<ExternalMessageAttachment> attachments = getAttachments(req, user, validationErrors);

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

		} finally {

			if (req instanceof MultipartRequest) {

				((MultipartRequest) req).deleteFiles();
			}
		}
	}

	@Override
	protected Field getFlowInstanceRelation() {
		
		return ExternalMessage.FLOWINSTANCE_RELATION;
	}

	@Override
	protected String getTypeLogName() {
		
		return "external";
	}

}
