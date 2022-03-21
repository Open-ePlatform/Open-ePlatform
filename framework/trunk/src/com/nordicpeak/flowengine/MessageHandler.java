package com.nordicpeak.flowengine;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.fileattachments.FileAttachment;
import se.unlogic.standardutils.fileattachments.FileAttachmentUtils;

import com.nordicpeak.flowengine.beans.BaseMessage;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.enums.SenderType;
import com.nordicpeak.flowengine.events.ExternalMessageAddedEvent;
import com.nordicpeak.flowengine.events.InternalMessageAddedEvent;
import com.nordicpeak.flowengine.utils.ExternalMessageUtils;
import com.nordicpeak.flowengine.utils.FlowEngineFileAttachmentUtils;

public class MessageHandler {

	private Logger log = Logger.getLogger(this.getClass());

	private FlowAdminModule flowAdminModule;

	public MessageHandler(FlowAdminModule flowAdminModule) {

		this.flowAdminModule = flowAdminModule;
	}

	public ExternalMessageHandlerInstance getInstance(ExternalMessage externalMessage) {

		return getInstance(externalMessage, true);
	}

	public ExternalMessageHandlerInstance getInstance(ExternalMessage externalMessage, boolean manager) {

		if (externalMessage == null) {

			return null;
		}

		return new ExternalMessageHandlerInstance(flowAdminModule.getDAOFactory().getExternalMessageDAO(), externalMessage, manager);
	}

	public InternalMessageHandlerInstance getInstance(InternalMessage internalMessage) {

		if (internalMessage == null) {

			return null;
		}

		return new InternalMessageHandlerInstance(flowAdminModule.getDAOFactory().getInternalMessageDAO(), internalMessage);
	}

	public void add(ExternalMessage externalMessage) throws SQLException, IOException {

		ExternalMessageHandlerInstance messageHandlerInstance = getInstance(externalMessage, true);

		messageHandlerInstance.add();
	}

	public void add(ExternalMessage externalMessage, boolean manager) throws SQLException, IOException {

		ExternalMessageHandlerInstance messageHandlerInstance = getInstance(externalMessage, manager);

		messageHandlerInstance.add();
	}

	public void add(InternalMessage internalMessage) throws SQLException, IOException {

		InternalMessageHandlerInstance messageHandlerInstance = getInstance(internalMessage);

		messageHandlerInstance.add();
	}

	public void add(MessageHandlerInstance<? extends BaseMessage> messageHandlerInstance, TransactionHandler transactionHandler) throws SQLException, IOException {

		if (messageHandlerInstance == null || messageHandlerInstance.message == null) {

			return;
		}

		messageHandlerInstance.add(transactionHandler);
	}

	public void commited(MessageHandlerInstance<? extends BaseMessage> messageHandlerInstance) throws SQLException {

		if (messageHandlerInstance == null || messageHandlerInstance.message == null) {

			return;
		}

		messageHandlerInstance.commited();
	}

	public void cleanup(MessageHandlerInstance<? extends BaseMessage> messageHandlerInstance) {

		if (messageHandlerInstance == null) {

			return;
		}

		messageHandlerInstance.cleanup();
	}

	public abstract class MessageHandlerInstance<Message extends BaseMessage> {

		protected Message message;

		protected boolean manager;

		protected AnnotatedDAO<Message> messageDAO;

		protected List<FileAttachment> fileAttachments;

		private MessageHandlerInstance(AnnotatedDAO<Message> messageDAO, Message message, boolean manager) {

			this.messageDAO = messageDAO;
			this.message = message;
			this.manager = manager;
		}

		public void add() throws SQLException, IOException {

			TransactionHandler transactionHandler = messageDAO.createTransaction();

			try {

				add(transactionHandler);

				transactionHandler.commit();

				commited();

			} catch (SQLException | IOException e) {

				cleanup();

				throw e;

			} finally {

				TransactionHandler.autoClose(transactionHandler);
			}
		}

		public void add(TransactionHandler transactionHandler) throws SQLException, IOException {

			log.info("User " + message.getPoster() + " adding " + message.getTypeLogName() + " message for flowinstance " + message.getFlowInstance());

			messageDAO.add(message, transactionHandler, null);

			fileAttachments = FlowEngineFileAttachmentUtils.saveAttachmentData(flowAdminModule.getFileAttachmentHandler(), message);
		}

		public void commited() throws SQLException {

			flowAdminModule.getSystemInterface().getEventHandler().sendEvent(this.getClass(), new CRUDEvent<>(CRUDAction.ADD, message), EventTarget.ALL);
		}

		public void cleanup() {

			FileAttachmentUtils.deleteFileAttachments(fileAttachments);
		}
	}

	public class ExternalMessageHandlerInstance extends MessageHandlerInstance<ExternalMessage> {

		public ExternalMessageHandlerInstance(AnnotatedDAO<ExternalMessage> messageDAO, ExternalMessage externalMessage, boolean manager) {

			super(messageDAO, externalMessage, manager);
		}

		@Override
		public void commited() throws SQLException {

			super.commited();

			FlowInstance flowInstance = message.getFlowInstance();
			User user = message.getPoster();
			Timestamp added = message.getAdded();

			EventType eventType = manager ? EventType.MANAGER_MESSAGE_SENT : EventType.CUSTOMER_MESSAGE_SENT;
			SenderType senderType = manager ? SenderType.MANAGER : SenderType.USER;

			FlowInstanceEvent flowInstanceEvent = flowAdminModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, eventType, null, user, added, ExternalMessageUtils.getFlowInstanceEventAttributes(message));

			flowAdminModule.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, flowAdminModule.getSiteProfile(flowInstance), message, senderType), EventTarget.ALL);
		}
	}

	public class InternalMessageHandlerInstance extends MessageHandlerInstance<InternalMessage> {

		public InternalMessageHandlerInstance(AnnotatedDAO<InternalMessage> messageDAO, InternalMessage internalMessage) {

			super(messageDAO, internalMessage, true);
		}

		@Override
		public void commited() throws SQLException {

			super.commited();

			FlowInstance flowInstance = message.getFlowInstance();

			flowAdminModule.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new InternalMessageAddedEvent(flowInstance, flowAdminModule.getSiteProfile(flowInstance), message), EventTarget.ALL);
		}
	}

}
