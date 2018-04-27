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

import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.beans.InternalMessageAttachment;
import com.nordicpeak.flowengine.interfaces.MessageCRUDCallback;

public class InternalMessageCRUD extends BaseMessageCRUD<InternalMessage, InternalMessageAttachment> {

	public InternalMessageCRUD(AnnotatedDAO<InternalMessage> messageDAO, AnnotatedDAO<InternalMessageAttachment> attachmentDAO, MessageCRUDCallback callback, boolean manager) {
		
		super(messageDAO, attachmentDAO, callback, InternalMessage.class, InternalMessageAttachment.class, manager);
	}

	public InternalMessage add(HttpServletRequest req, HttpServletResponse res, URIParser uriParser, User user, Document doc, Element element, FlowInstance flowInstance) throws SQLException, IOException {
		
		List<ValidationError> errors = new ArrayList<ValidationError>();

		req = parseRequest(req, errors);

		try{
			String message = ValidationUtils.validateParameter("internalmessage", req, true, 1, 65535, StringPopulator.getPopulator(), errors);

			List<InternalMessageAttachment> attachments = getAttachments(req, user, errors);

			if (errors.isEmpty()) {

				log.info("User " + user + " adding internal message for flowinstance " + flowInstance);

				InternalMessage internalMessage = new InternalMessage();
				internalMessage.setFlowInstance(flowInstance);
				internalMessage.setPoster(user);
				internalMessage.setMessage(message);
				internalMessage.setAdded(TimeUtils.getCurrentTimestamp());
				internalMessage.setAttachments(attachments);

				messageDAO.add(internalMessage);

				return internalMessage;

			}

			XMLUtils.append(doc, element, errors);

			return null;

		}finally{

			if (req instanceof MultipartRequest) {

				((MultipartRequest)req).deleteFiles();
			}
		}

	}

	@Override
	protected Field getFlowInstanceRelation() {
		
		return InternalMessage.FLOWINSTANCE_RELATION;
	}

	@Override
	protected String getTypeLogName() {
		
		return "internal";
	}
	
}
