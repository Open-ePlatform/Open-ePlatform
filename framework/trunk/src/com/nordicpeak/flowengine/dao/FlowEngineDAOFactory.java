package com.nordicpeak.flowengine.dao;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableUpgradeException;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;

import com.nordicpeak.flowengine.beans.APIAccessSetting;
import com.nordicpeak.flowengine.beans.AbortedFlowInstance;
import com.nordicpeak.flowengine.beans.Category;
import com.nordicpeak.flowengine.beans.DefaultStandardStatusMapping;
import com.nordicpeak.flowengine.beans.DefaultStatusMapping;
import com.nordicpeak.flowengine.beans.EvaluatorDescriptor;
import com.nordicpeak.flowengine.beans.ExternalFlowRedirect;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowAction;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowFamilyEvent;
import com.nordicpeak.flowengine.beans.FlowForm;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceAttribute;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.beans.InternalMessageAttachment;
import com.nordicpeak.flowengine.beans.OperatingMessage;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryInstanceDescriptor;
import com.nordicpeak.flowengine.beans.StandardStatus;
import com.nordicpeak.flowengine.beans.StandardStatusGroup;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.beans.UserBookmark;
import com.nordicpeak.flowengine.beans.UserOrganization;

public class FlowEngineDAOFactory {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	private final HierarchyAnnotatedDAOFactory daoFactory;
	
	public FlowEngineDAOFactory(DataSource dataSource, UserHandler userHandler, GroupHandler groupHandler) throws TableUpgradeException, SQLException, SAXException, IOException, ParserConfigurationException {
		
		// Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowEngineDAOFactory.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));
		
		if (upgradeResult.isUpgrade()) {
			
			log.info(upgradeResult.toString());
		}
		
		daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, userHandler, groupHandler, false, true, false);
	}
	
	public TransactionHandler getTransactionHandler() throws SQLException {
		
		return daoFactory.getDAO(Flow.class).createTransaction();
	}
	
	public AnnotatedDAO<FlowInstance> getFlowInstanceDAO() {
		
		return daoFactory.getDAO(FlowInstance.class);
	}
	
	public AnnotatedDAO<QueryInstanceDescriptor> getQueryInstanceDescriptorDAO() {
		
		return daoFactory.getDAO(QueryInstanceDescriptor.class);
	}
	
	public AnnotatedDAO<Flow> getFlowDAO() {
		
		return daoFactory.getDAO(Flow.class);
	}
	
	public AnnotatedDAO<FlowType> getFlowTypeDAO() {
		
		return daoFactory.getDAO(FlowType.class);
	}
	
	public AnnotatedDAO<QueryDescriptor> getQueryDescriptorDAO() {
		
		return daoFactory.getDAO(QueryDescriptor.class);
	}
	
	public AnnotatedDAO<Step> getStepDAO() {
		
		return daoFactory.getDAO(Step.class);
	}
	
	public AnnotatedDAO<FlowAction> getFlowActionDAO() {
		
		return daoFactory.getDAO(FlowAction.class);
	}
	
	public AnnotatedDAO<Status> getStatusDAO() {
		
		return daoFactory.getDAO(Status.class);
	}
	
	public AnnotatedDAO<DefaultStatusMapping> getDefaultStatusMappingDAO() {
		
		return daoFactory.getDAO(DefaultStatusMapping.class);
	}
	
	public AnnotatedDAO<EvaluatorDescriptor> getEvaluatorDescriptorDAO() {
		
		return daoFactory.getDAO(EvaluatorDescriptor.class);
	}
	
	public AnnotatedDAO<FlowFamily> getFlowFamilyDAO() {
		
		return daoFactory.getDAO(FlowFamily.class);
	}
	
	public AnnotatedDAO<StandardStatus> getStandardStatusDAO() {
		
		return daoFactory.getDAO(StandardStatus.class);
	}
	
	public AnnotatedDAO<StandardStatusGroup> getStandardStatusGroupDAO() {
		
		return daoFactory.getDAO(StandardStatusGroup.class);
	}
	
	public AnnotatedDAO<DefaultStandardStatusMapping> getDefaultStandardStatusMappingDAO() {
		
		return daoFactory.getDAO(DefaultStandardStatusMapping.class);
	}
	
	public AnnotatedDAO<Category> getCategoryDAO() {
		
		return daoFactory.getDAO(Category.class);
	}
	
	public AnnotatedDAO<ExternalMessage> getExternalMessageDAO() {
		
		return daoFactory.getDAO(ExternalMessage.class);
	}
	
	public AnnotatedDAO<InternalMessage> getInternalMessageDAO() {
		
		return daoFactory.getDAO(InternalMessage.class);
	}
	
	public AnnotatedDAO<ExternalMessageAttachment> getExternalMessageAttachmentDAO() {
		
		return daoFactory.getDAO(ExternalMessageAttachment.class);
	}
	
	public AnnotatedDAO<InternalMessageAttachment> getInternalMessageAttachmentDAO() {
		
		return daoFactory.getDAO(InternalMessageAttachment.class);
	}
	
	public AnnotatedDAO<FlowInstanceEvent> getFlowInstanceEventDAO() {
		
		return daoFactory.getDAO(FlowInstanceEvent.class);
	}
	
	public AnnotatedDAO<UserBookmark> getUserBookmarkDAO() {
		
		return daoFactory.getDAO(UserBookmark.class);
	}
	
	public AnnotatedDAO<UserOrganization> getUserOrganizationDAO() {
		
		return daoFactory.getDAO(UserOrganization.class);
	}
	
	public AnnotatedDAO<AbortedFlowInstance> getAbortedFlowInstanceDAO() {
		
		return daoFactory.getDAO(AbortedFlowInstance.class);
	}
	
	public AnnotatedDAO<OperatingMessage> getOperatingMessageDAO() {
		
		return daoFactory.getDAO(OperatingMessage.class);
	}
	
	public AnnotatedDAO<FlowFamilyEvent> getFlowFamilyEventDAO() {
		
		return daoFactory.getDAO(FlowFamilyEvent.class);
	}
	
	public AnnotatedDAO<ExternalFlowRedirect> getExternalFlowRedirectDAO() {
		
		return daoFactory.getDAO(ExternalFlowRedirect.class);
	}
	
	public AnnotatedDAO<FlowForm> getFlowFormDAO() {
		
		return daoFactory.getDAO(FlowForm.class);
	}

	public AnnotatedDAO<FlowInstanceAttribute> getFlowInstanceAttributeDAO(){
		
		return daoFactory.getDAO(FlowInstanceAttribute.class);
	}
	
	public AnnotatedDAO<APIAccessSetting> getApiAccessSettingDAO(){
		
		return daoFactory.getDAO(APIAccessSetting.class);
	}
	
}
