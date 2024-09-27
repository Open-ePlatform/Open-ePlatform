package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_internal_messages")
@XMLElement
public class InternalMessage extends BaseMessage {

	private static final long serialVersionUID = -934750439780152020L;

	public static final Field ATTACHMENTS_RELATION = ReflectionUtils.getField(InternalMessage.class, "attachments");
	public static final Field FLOWINSTANCE_RELATION = ReflectionUtils.getField(InternalMessage.class, "flowInstance");

	@DAOManaged(columnName = "flowInstanceID")
	@ManyToOne
	@XMLElement
	private FlowInstance flowInstance;

	@DAOManaged
	@XMLElement
	private Timestamp deleted;

	@DAOManaged
	@XMLElement(name = "deletedBy")
	private User deletedBy;

	@DAOManaged
	@OneToMany(autoAdd = true)
	@XMLElement
	private List<InternalMessageAttachment> attachments;

	@Override
	public List<InternalMessageAttachment> getAttachments() {

		return attachments;
	}

	public void setAttachments(List<InternalMessageAttachment> attachments) {

		this.attachments = attachments;
	}

	@Override
	public FlowInstance getFlowInstance() {

		return flowInstance;
	}

	public void setFlowInstance(FlowInstance flowInstance) {

		this.flowInstance = flowInstance;
	}

	public Timestamp getDeleted() {

		return deleted;
	}

	public void setDeleted(Timestamp deleted) {

		this.deleted = deleted;
	}

	public User getDeletedBy() {

		return deletedBy;
	}

	public void setDeletedBy(User deletedBy) {

		this.deletedBy = deletedBy;
	}

	@Override
	public String getTypeLogName() {

		return "internal";
	}
}
