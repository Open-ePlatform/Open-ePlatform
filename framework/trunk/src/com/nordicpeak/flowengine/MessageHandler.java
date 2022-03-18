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
import se.unlogic.standardutils.fileattachments.FileAttachmentHandler;
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

	private BaseFlowModule baseFlowModule;

	private AnnotatedDAO<ExternalMessage> externalMessageDAO;

	private AnnotatedDAO<InternalMessage> internalMessageDAO;

	private FileAttachmentHandler fileAttachmentHandler;

	public MessageHandler(BaseFlowModule baseFlowModule, AnnotatedDAO<ExternalMessage> externalMessageDAO, AnnotatedDAO<InternalMessage> internalMessageDAO, FileAttachmentHandler fileAttachmentHandler) {

		this.baseFlowModule = baseFlowModule;
		this.externalMessageDAO = externalMessageDAO;
		this.internalMessageDAO = internalMessageDAO;
		this.fileAttachmentHandler = fileAttachmentHandler;
	}

	public void setFileAttachmentHandler(FileAttachmentHandler fileAttachmentHandler) {

		this.fileAttachmentHandler = fileAttachmentHandler;
	}

	public ExternalMessageHandlerInstance getInstance(ExternalMessage externalMessage) {

		return getInstance(externalMessage, true);
	}

	public ExternalMessageHandlerInstance getInstance(ExternalMessage externalMessage, boolean manager) {

		if (externalMessage == null) {

			return null;
		}

		return new ExternalMessageHandlerInstance(externalMessageDAO, externalMessage, manager);
	}

	public InternalMessageHandlerInstance getInstance(InternalMessage internalMessage) {

		if (internalMessage == null) {

			return null;
		}

		return new InternalMessageHandlerInstance(internalMessageDAO, internalMessage);
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

			fileAttachments = FlowEngineFileAttachmentUtils.saveAttachmentData(fileAttachmentHandler, message);
		}

		public void commited() throws SQLException {

			baseFlowModule.getSystemInterface().getEventHandler().sendEvent(this.getClass(), new CRUDEvent<>(CRUDAction.ADD, message), EventTarget.ALL);
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
																	  
			FlowInstanceEvent flowInstanceEvent = baseFlowModule.getFlowInstanceEventGenerator().addFlowInstanceEvent(flowInstance, eventType, null, user, added, ExternalMessageUtils.getFlowInstanceEventAttributes(message));

			baseFlowModule.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new ExternalMessageAddedEvent(flowInstance, flowInstanceEvent, baseFlowModule.getSiteProfile(flowInstance), message, senderType), EventTarget.ALL);
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

			baseFlowModule.getSystemInterface().getEventHandler().sendEvent(FlowInstance.class, new InternalMessageAddedEvent(flowInstance, baseFlowModule.getSiteProfile(flowInstance), message), EventTarget.ALL);
		}
	}

}
