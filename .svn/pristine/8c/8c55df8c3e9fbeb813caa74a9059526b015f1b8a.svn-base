package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_external_messages")
@XMLElement
public class ExternalMessage extends BaseMessage {

	private static final long serialVersionUID = -5166683291745202612L;

	public static final Field FLOWINSTANCE_RELATION = ReflectionUtils.getField(ExternalMessage.class, "flowInstance");
	public static final Field ATTACHMENTS_RELATION = ReflectionUtils.getField(ExternalMessage.class, "attachments");
	public static final Field READ_RECEIPTS_RELATION = ReflectionUtils.getField(ExternalMessage.class, "readReceipts");

	@DAOManaged(columnName = "flowInstanceID")
	@ManyToOne
	@XMLElement
	private FlowInstance flowInstance;

	@DAOManaged
	@XMLElement
	private boolean postedByManager;

	@DAOManaged
	@XMLElement
	private boolean systemMessage;

	@DAOManaged
	@XMLElement
	private boolean readReceiptEnabled;

	@DAOManaged
	@OneToMany(autoAdd = true)
	@XMLElement
	private List<ExternalMessageAttachment> attachments;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<ExternalMessageReadReceipt> readReceipts;

	@Override
	public List<ExternalMessageAttachment> getAttachments() {

		return attachments;
	}

	public void setAttachments(List<ExternalMessageAttachment> attachments) {

		this.attachments = attachments;
	}

	public boolean isReadReceiptEnabled() {

		return readReceiptEnabled;
	}

	public void setReadReceiptEnabled(boolean readReceiptEnabled) {

		this.readReceiptEnabled = readReceiptEnabled;
	}

	public List<ExternalMessageReadReceipt> getReadReceipts() {

		return readReceipts;
	}

	public void setReadReceipts(List<ExternalMessageReadReceipt> readReceipts) {

		this.readReceipts = readReceipts;
	}

	@Override
	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {

		this.flowInstance = flowInstance;
	}

	public boolean isPostedByManager() {

		return postedByManager;
	}

	public void setPostedByManager(boolean postedByManager) {

		this.postedByManager = postedByManager;
	}

	public boolean isSystemMessage() {

		return systemMessage;
	}

	public void setSystemMessage(boolean systemMessage) {

		this.systemMessage = systemMessage;
	}

	@Override
	public String getTypeLogName() {

		return "external";
	}

	public boolean hasReadReceiptAccess(User user) {

		return !readReceiptEnabled || CollectionUtils.find(readReceipts, r -> r.getUser().equals(user)) != null;
	}

}
