package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;
import java.util.List;

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
	
	public static final Field ATTACHMENTS_RELATION = ReflectionUtils.getField(ExternalMessage.class, "attachments");
	public static final Field FLOWINSTANCE_RELATION = ReflectionUtils.getField(ExternalMessage.class, "flowInstance");
	
	@DAOManaged(columnName="flowInstanceID")
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
	@OneToMany(autoAdd=true)
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

	@Override
	public List<ExternalMessageReadReceipt> getReadReceipts() {

		return readReceipts;
	}

	public void setReadReceipts(List<ExternalMessageReadReceipt> readRecipts) {

		this.readReceipts = readRecipts;
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
}
