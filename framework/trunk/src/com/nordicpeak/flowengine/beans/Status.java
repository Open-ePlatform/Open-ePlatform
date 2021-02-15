package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.NonNegativeStringIntegerPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.enums.ContentType;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;

@Table(name = "flowengine_flow_statuses")
@XMLElement
public class Status extends BaseStatus implements ImmutableStatus, XMLParserPopulateable, AccessInterface {

	private static final long serialVersionUID = -3364854013675598021L;

	public static final Field DEFAULT_STATUS_MAPPINGS_RELATION = ReflectionUtils.getField(Status.class, "defaultStatusMappings");
	public static final Field FLOW_RELATION = ReflectionUtils.getField(Status.class, "flow");
	public static final Field FLOW_INSTANCES_RELATION = ReflectionUtils.getField(Status.class, "flowInstances");
	public static final Field MANAGER_GROUPS_RELATION = ReflectionUtils.getField(Status.class, "managerGroupIDs");
	public static final Field MANAGER_USERS_RELATION = ReflectionUtils.getField(Status.class, "managerUserIDs");
	public static final List<Field> INTERNAL_FIELDS = ReflectionUtils.getFields(Status.class, "isUserMutable", "isUserDeletable", "isAdminMutable", "isAdminDeletable", "sortIndex", "requireSigning", "useAccessCheck");

	@DAOManaged
	@WebPopulate(paramName = "defaultExternalMessageTemplate")
	@XMLElement
	private Integer defaultExternalMessageTemplateID;

	@DAOManaged
	@WebPopulate(paramName = "defaultInternalMessageTemplate")
	@XMLElement
	private Integer defaultInternalMessageTemplateID;

	@DAOManaged(columnName = "flowID")
	@ManyToOne
	private Flow flow;

	@DAOManaged
	@OneToMany
	private List<FlowInstance> flowInstances;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<DefaultStatusMapping> defaultStatusMappings;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_statuses_manager_groups", remoteValueColumnName = "groupID")
	@WebPopulate(paramName = "group")
	@XMLElement(childName = "groupID")
	private List<Integer> managerGroupIDs;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_statuses_manager_users", remoteValueColumnName = "userID")
	@WebPopulate(paramName = "user")
	@XMLElement(childName = "userID")
	private List<Integer> managerUserIDs;

	@XMLElement
	private Integer flowInstanceCount;

	@XMLElement
	private Integer flowSubmittedInstanceCount;

	public Status() {}

	public Status(BaseStatus baseStatus) {

		super(baseStatus);
	}

	public Integer getDefaultExternalMessageTemplateID() {

		return defaultExternalMessageTemplateID;
	}

	public void setDefaultExternalMessageTemplateID(Integer defaultExternalMessageTemplateID) {

		this.defaultExternalMessageTemplateID = defaultExternalMessageTemplateID;
	}

	public Integer getDefaultInternalMessageTemplateID() {

		return defaultInternalMessageTemplateID;
	}

	public void setDefaultInternalMessageTemplateID(Integer defaultInternalMessageTemplateID) {

		this.defaultInternalMessageTemplateID = defaultInternalMessageTemplateID;
	}

	@Override
	public Flow getFlow() {

		return flow;
	}

	public void setFlow(Flow flow) {

		this.flow = flow;
	}

	@Override
	public List<FlowInstance> getFlowInstances() {

		return flowInstances;
	}

	public void setFlowInstances(List<FlowInstance> flowInstances) {

		this.flowInstances = flowInstances;
	}

	@Override
	public List<DefaultStatusMapping> getDefaulStatusMappings() {

		return defaultStatusMappings;
	}

	public void setDefaultStatusMappings(List<DefaultStatusMapping> defaultFlowStateMappings) {

		this.defaultStatusMappings = defaultFlowStateMappings;
	}

	public Integer getFlowInstanceCount() {

		return flowInstanceCount;
	}

	public void setFlowInstanceCount(Integer flowInstanceCount) {

		this.flowInstanceCount = flowInstanceCount;
	}

	public Integer getFlowSubmittedInstanceCount() {

		return flowSubmittedInstanceCount;
	}

	public void setFlowSubmittedInstanceCount(Integer flowSubmittedInstanceCount) {

		this.flowSubmittedInstanceCount = flowSubmittedInstanceCount;
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<>();

		this.statusID = XMLValidationUtils.validateParameter("statusID", xmlParser, true, PositiveStringIntegerPopulator.getPopulator(), errors);

		this.name = XMLValidationUtils.validateParameter("name", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		this.description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		this.managingTime = XMLValidationUtils.validateParameter("managingTime", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);

		this.newExternalMessagesDisallowed = xmlParser.getPrimitiveBoolean("newExternalMessagesDisallowed");
		this.newExternalMessagesAllowedDays = XMLValidationUtils.validateParameter("newExternalMessagesAllowedDays", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		this.addExternalMessage = xmlParser.getPrimitiveBoolean("addExternalMessage");
		this.defaultExternalMessageTemplateID = XMLValidationUtils.validateParameter("defaultExternalMessageTemplateID", xmlParser, false, NonNegativeStringIntegerPopulator.getPopulator(), errors);
		this.defaultInternalMessageTemplateID = XMLValidationUtils.validateParameter("defaultInternalMessageTemplateID", xmlParser, false, NonNegativeStringIntegerPopulator.getPopulator(), errors);

		this.isUserMutable = xmlParser.getPrimitiveBoolean("isUserMutable");
		this.isUserDeletable = xmlParser.getPrimitiveBoolean("isUserDeletable");
		this.isAdminMutable = xmlParser.getPrimitiveBoolean("isAdminMutable");
		this.isAdminDeletable = xmlParser.getPrimitiveBoolean("isAdminDeletable");
		this.isRestrictedAdminDeletable = xmlParser.getPrimitiveBoolean("isRestrictedAdminDeletable");

		this.sortIndex = xmlParser.getInt("sortIndex");

		this.contentType = XMLValidationUtils.validateParameter("contentType", xmlParser, true, new EnumPopulator<>(ContentType.class), errors);

		this.requireSigning = xmlParser.getPrimitiveBoolean("requireSigning");

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

	}

	public List<Integer> getManagerGroupIDs() {

		return managerGroupIDs;
	}

	public List<Integer> getManagerUserIDs() {

		return managerUserIDs;
	}

	public void setManagerGroupIDs(List<Integer> managerGroupIDs) {

		this.managerGroupIDs = managerGroupIDs;
	}

	public void setManagerUserIDs(List<Integer> managerUserIDs) {

		this.managerUserIDs = managerUserIDs;
	}

	@Override
	public boolean allowsAdminAccess() {

		return false;
	}

	@Override
	public boolean allowsUserAccess() {

		return false;
	}

	@Override
	public boolean allowsAnonymousAccess() {

		return false;
	}

	@Override
	public List<Integer> getAllowedGroupIDs() {

		return managerGroupIDs;
	}

	@Override
	public List<Integer> getAllowedUserIDs() {

		return managerUserIDs;
	}
}
