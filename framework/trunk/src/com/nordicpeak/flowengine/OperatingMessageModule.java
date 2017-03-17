package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.SiteProfileFilterModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.OperatingMessage;
import com.nordicpeak.flowengine.cruds.OperatingMessageCRUD;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.interfaces.OperatingStatus;

public class OperatingMessageModule extends AnnotatedForegroundModule implements CRUDCallback<User> {

	private OperatingMessageCRUD messageCRUD;

	private AnnotatedDAO<OperatingMessage> operatingMessageDAO;

	private CopyOnWriteArraySet<OperatingMessage> operatingMessageCache;

	private QueryParameterFactory<OperatingMessage, Timestamp> endTimeParameterFactory;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Enable site profile support", description="Controls if site profile support is enabled")
	protected boolean enableSiteProfileSupport;
	
	@InstanceManagerDependency(required = true)
	private FlowAdminModule flowAdminModule;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(OperatingMessageModule.class, this)) {

			throw new RuntimeException("Unable to register module in global instance handler using key " + OperatingMessageModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
		
		cacheComingOperatingMessages();
	}
	
	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(OperatingMessageModule.class, this);

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		FlowEngineDAOFactory daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		operatingMessageDAO = daoFactory.getOperatingMessageDAO();

		endTimeParameterFactory = operatingMessageDAO.getParamFactory("endTime", Timestamp.class);

		messageCRUD = new OperatingMessageCRUD(operatingMessageDAO.getAdvancedWrapper("messageID", Integer.class), this);
		
	}

	private void cacheComingOperatingMessages() throws SQLException {

		log.info("Caching comping operating messages");
		
		HighLevelQuery<OperatingMessage> query = new HighLevelQuery<OperatingMessage>();

		Timestamp currentTimestamp = TimeUtils.getCurrentTimestamp();

		query.addParameter(endTimeParameterFactory.getParameter(currentTimestamp, QueryOperators.BIGGER_THAN));

		List<OperatingMessage> operatingMessages = operatingMessageDAO.getAll(query);

		if (operatingMessages != null) {

			operatingMessageCache = new CopyOnWriteArraySet<OperatingMessage>(operatingMessages);
			
			return;
		}

		operatingMessageCache = new CopyOnWriteArraySet<OperatingMessage>();
		
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return messageCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic
	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return messageCRUD.add(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return messageCRUD.update(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return messageCRUD.delete(req, res, user, uriParser);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		if(enableSiteProfileSupport) {
			XMLUtils.appendNewElement(doc, documentElement, "enableSiteProfileSupport", "true");
		}
		
		doc.appendChild(documentElement);
		return doc;

	}

	@Override
	public String getTitlePrefix() {

		return moduleDescriptor.getName();
	}

	public FlowAdminModule getFlowAdminModule() {
		
		return flowAdminModule;
	}
	
	public OperatingStatus getOperatingStatus(Integer flowFamilyID, boolean manager) {

		List<OperatingMessage> pastMessages = null;
		
		Timestamp currentTime = TimeUtils.getCurrentTimestamp();

		OperatingStatus operatingStatus = null;
		
		for(OperatingMessage operatingMessage : operatingMessageCache){
			
			if(enableSiteProfileSupport) {
				
				if(operatingMessage.getProfileIDs() != null && SiteProfileFilterModule.getCurrentProfile() != null && !operatingMessage.getProfileIDs().contains(SiteProfileFilterModule.getCurrentProfile().getProfileID())) {
					
					//Message is set for specific profiles, but current profile is not one of them. Message should not be shown.
					continue;
				}
			}
			
			if (operatingMessage.getStartTime().before(currentTime) && operatingMessage.getEndTime().after(currentTime)) {
				
				if(manager && operatingMessage.allowsManagingOfInstances()){
					
					continue;
				}
				
				if(operatingStatus == null && operatingMessage.isGlobal()) {
				
					operatingStatus = operatingMessage;
					
				} else if ((operatingMessage.getFlowFamilyIDs() != null && flowFamilyID != null && operatingMessage.getFlowFamilyIDs().contains(flowFamilyID))) {

					operatingStatus = operatingMessage;
					
					break;
				}

			} else if (!operatingMessage.getEndTime().after(currentTime)) {

				pastMessages = CollectionUtils.addAndInstantiateIfNeeded(pastMessages, operatingMessage);
			}
		}
		
		if(pastMessages != null){
			
			operatingMessageCache.removeAll(pastMessages);
		}
		
		return operatingStatus;

	}
	
	public OperatingStatus getGlobalOperatingStatus(boolean manager) {
		
		return getOperatingStatus(null, manager);
	}
	
	public void addOrUpdateOperatingMessage(OperatingMessage operatingMessage) {
		
		operatingMessageCache.remove(operatingMessage);
		
		if(operatingMessage.getEndTime().after(TimeUtils.getCurrentTimestamp())) {

			operatingMessageCache.add(operatingMessage);
		
		}
		
	}
	
	public void deleteOperatingMessage(OperatingMessage operatingMessage) {
		
		operatingMessageCache.remove(operatingMessage);
		
	}

}
