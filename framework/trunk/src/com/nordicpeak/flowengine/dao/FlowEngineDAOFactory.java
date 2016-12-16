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
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowType;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.beans.InternalMessageAttachment;
import com.nordicpeak.flowengine.beans.OperatingMessage;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.QueryInstanceDescriptor;
import com.nordicpeak.flowengine.beans.StandardStatus;
import com.nordicpeak.flowengine.beans.Status;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.beans.UserBookmark;
import com.nordicpeak.flowengine.beans.UserFavourite;
import com.nordicpeak.flowengine.beans.UserOrganization;

public class FlowEngineDAOFactory {
	
	protected Logger log = Logger.getLogger(this.getClass());
	
	private final AnnotatedDAO<FlowType> flowTypeDAO;
	private final AnnotatedDAO<FlowFamily> flowFamilyDAO;
	private final AnnotatedDAO<Flow> flowDAO;
	private final AnnotatedDAO<Step> stepDAO;
	private final AnnotatedDAO<FlowInstance> flowInstanceDAO;
	private final AnnotatedDAO<QueryDescriptor> queryDescriptorDAO;
	private final AnnotatedDAO<QueryInstanceDescriptor> queryInstanceDescriptorDAO;
	private final AnnotatedDAO<FlowAction> flowActionDAO;
	private final AnnotatedDAO<Status> statusDAO;
	private final AnnotatedDAO<DefaultStatusMapping> defaultStatusMappingDAO;
	private final AnnotatedDAO<EvaluatorDescriptor> evaluatorDescriptorDAO;
	private final AnnotatedDAO<StandardStatus> standardStatusDAO;
	private final AnnotatedDAO<DefaultStandardStatusMapping> defaultStandardStatusMappingDAO;
	private final AnnotatedDAO<Category> categoryDAO;
	private final AnnotatedDAO<FlowInstanceEvent> flowInstanceEventDAO;
	private final AnnotatedDAO<ExternalMessage> externalMessageDAO;
	private final AnnotatedDAO<InternalMessage> internalMessageDAO;
	private final AnnotatedDAO<ExternalMessageAttachment> externalMessageAttachmentDAO;
	private final AnnotatedDAO<InternalMessageAttachment> internalMessageAttachmentDAO;
	private final UserFavouriteDAO userFavouriteDAO;
	private final AnnotatedDAO<UserBookmark> userBookmarkDAO;
	private final AnnotatedDAO<UserOrganization> userOrganizationDAO;
	private final AnnotatedDAO<AbortedFlowInstance> abortedFlowInstanceDAO;
	private final AnnotatedDAO<OperatingMessage> operatingMessageDAO;
	private final AnnotatedDAO<FlowFamilyEvent> flowFamilyEventDAO;
	private final AnnotatedDAO<ExternalFlowRedirect> externalFlowRedirectDAO;
	private final AnnotatedDAO<FlowForm> flowFormDAO;
	
	public FlowEngineDAOFactory(DataSource dataSource, UserHandler userHandler, GroupHandler groupHandler) throws TableUpgradeException, SQLException, SAXException, IOException, ParserConfigurationException {
		
		// Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowEngineDAOFactory.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));
		
		if (upgradeResult.isUpgrade()) {
			
			log.info(upgradeResult.toString());
		}
		
		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, userHandler, groupHandler, false, true, false);
		
		flowTypeDAO = daoFactory.getDAO(FlowType.class);
		flowFamilyDAO = daoFactory.getDAO(FlowFamily.class);
		flowDAO = daoFactory.getDAO(Flow.class);
		stepDAO = daoFactory.getDAO(Step.class);
		flowInstanceDAO = daoFactory.getDAO(FlowInstance.class);
		queryDescriptorDAO = daoFactory.getDAO(QueryDescriptor.class);
		queryInstanceDescriptorDAO = daoFactory.getDAO(QueryInstanceDescriptor.class);
		flowActionDAO = daoFactory.getDAO(FlowAction.class);
		statusDAO = daoFactory.getDAO(Status.class);
		defaultStatusMappingDAO = daoFactory.getDAO(DefaultStatusMapping.class);
		evaluatorDescriptorDAO = daoFactory.getDAO(EvaluatorDescriptor.class);
		standardStatusDAO = daoFactory.getDAO(StandardStatus.class);
		defaultStandardStatusMappingDAO = daoFactory.getDAO(DefaultStandardStatusMapping.class);
		categoryDAO = daoFactory.getDAO(Category.class);
		flowInstanceEventDAO = daoFactory.getDAO(FlowInstanceEvent.class);
		externalMessageDAO = daoFactory.getDAO(ExternalMessage.class);
		internalMessageDAO = daoFactory.getDAO(InternalMessage.class);
		externalMessageAttachmentDAO = daoFactory.getDAO(ExternalMessageAttachment.class);
		internalMessageAttachmentDAO = daoFactory.getDAO(InternalMessageAttachment.class);
		userFavouriteDAO = new UserFavouriteDAO(dataSource, UserFavourite.class, daoFactory);
		userBookmarkDAO = daoFactory.getDAO(UserBookmark.class);
		userOrganizationDAO = daoFactory.getDAO(UserOrganization.class);
		abortedFlowInstanceDAO = daoFactory.getDAO(AbortedFlowInstance.class);
		operatingMessageDAO = daoFactory.getDAO(OperatingMessage.class);
		flowFamilyEventDAO = daoFactory.getDAO(FlowFamilyEvent.class);
		externalFlowRedirectDAO = daoFactory.getDAO(ExternalFlowRedirect.class);
		flowFormDAO = daoFactory.getDAO(FlowForm.class);
	}
	
	public TransactionHandler getTransactionHandler() throws SQLException {
		
		return flowInstanceDAO.createTransaction();
	}
	
	public AnnotatedDAO<FlowInstance> getFlowInstanceDAO() {
		
		return flowInstanceDAO;
	}
	
	public AnnotatedDAO<QueryInstanceDescriptor> getQueryInstanceDescriptorDAO() {
		
		return queryInstanceDescriptorDAO;
	}
	
	public AnnotatedDAO<Flow> getFlowDAO() {
		
		return flowDAO;
	}
	
	public AnnotatedDAO<FlowType> getFlowTypeDAO() {
		
		return flowTypeDAO;
	}
	
	public AnnotatedDAO<QueryDescriptor> getQueryDescriptorDAO() {
		
		return queryDescriptorDAO;
	}
	
	public AnnotatedDAO<Step> getStepDAO() {
		
		return stepDAO;
	}
	
	public AnnotatedDAO<FlowAction> getFlowActionDAO() {
		
		return flowActionDAO;
	}
	
	public AnnotatedDAO<Status> getStatusDAO() {
		
		return statusDAO;
	}
	
	public AnnotatedDAO<DefaultStatusMapping> getDefaultStatusMappingDAO() {
		
		return defaultStatusMappingDAO;
	}
	
	public AnnotatedDAO<EvaluatorDescriptor> getEvaluatorDescriptorDAO() {
		
		return evaluatorDescriptorDAO;
	}
	
	public AnnotatedDAO<FlowFamily> getFlowFamilyDAO() {
		
		return flowFamilyDAO;
	}
	
	public AnnotatedDAO<StandardStatus> getStandardStatusDAO() {
		
		return standardStatusDAO;
	}
	
	public AnnotatedDAO<DefaultStandardStatusMapping> getDefaultStandardStatusMappingDAO() {
		
		return defaultStandardStatusMappingDAO;
	}
	
	public AnnotatedDAO<Category> getCategoryDAO() {
		
		return categoryDAO;
	}
	
	public AnnotatedDAO<ExternalMessage> getExternalMessageDAO() {
		
		return externalMessageDAO;
	}
	
	public AnnotatedDAO<InternalMessage> getInternalMessageDAO() {
		
		return internalMessageDAO;
	}
	
	public AnnotatedDAO<ExternalMessageAttachment> getExternalMessageAttachmentDAO() {
		
		return externalMessageAttachmentDAO;
	}
	
	public AnnotatedDAO<InternalMessageAttachment> getInternalMessageAttachmentDAO() {
		
		return internalMessageAttachmentDAO;
	}
	
	public AnnotatedDAO<FlowInstanceEvent> getFlowInstanceEventDAO() {
		
		return flowInstanceEventDAO;
	}
	
	public UserFavouriteDAO getUserFavouriteDAO() {
		
		return userFavouriteDAO;
	}
	
	public AnnotatedDAO<UserBookmark> getUserBookmarkDAO() {
		
		return userBookmarkDAO;
	}
	
	public AnnotatedDAO<UserOrganization> getUserOrganizationDAO() {
		
		return userOrganizationDAO;
	}
	
	public AnnotatedDAO<AbortedFlowInstance> getAbortedFlowInstanceDAO() {
		
		return abortedFlowInstanceDAO;
	}
	
	public AnnotatedDAO<OperatingMessage> getOperatingMessageDAO() {
		
		return operatingMessageDAO;
	}
	
	public AnnotatedDAO<FlowFamilyEvent> getFlowFamilyEventDAO() {
		
		return flowFamilyEventDAO;
	}
	
	public AnnotatedDAO<ExternalFlowRedirect> getExternalFlowRedirectDAO() {
		
		return externalFlowRedirectDAO;
	}
	
	public AnnotatedDAO<FlowForm> getFlowFormDAO() {
		return flowFormDAO;
	}
	
}
