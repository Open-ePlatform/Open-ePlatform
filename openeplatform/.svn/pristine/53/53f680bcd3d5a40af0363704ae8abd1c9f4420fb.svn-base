package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

import it.sauronsoftware.cron4j.Scheduler;
import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.events.EventListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.BackgroundModuleDescriptor;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.collections.MethodComparator;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;


public class PopularFlowFamiliesModule extends AnnotatedBackgroundModule implements EventListener<CRUDEvent<Flow>>, Runnable {

	private static final Comparator<FlowFamily> FAMILY_COMPARATOR = new MethodComparator<FlowFamily>(FlowFamily.class, "getFlowInstanceCount", Order.DESC);

	@ModuleSetting
	@TextFieldSettingDescriptor(name="Intervall size", description="Controls how any hours back in time that the statistics should be based on")
	private int interval = 72;

	@ModuleSetting
	@TextFieldSettingDescriptor(name="Flow count", description="Controls how many flows this module should display")
	private int flowCount = 5;

	@InstanceManagerDependency(required=true)
	private FlowBrowserModule flowBrowserModule;

	private AnnotatedDAO<Flow> flowDAO;

	private List<Flow> popularFlows;

	private Scheduler scheduler;

	@SuppressWarnings("unchecked")
	@Override
	public void init(BackgroundModuleDescriptor descriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(descriptor, sectionInterface, dataSource);

		cacheFlows();

		systemInterface.getEventHandler().addEventListener(CRUDEvent.class, this, Flow.class);

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		scheduler.setDaemon(true);
		scheduler.schedule("0 * * * *", this);
		scheduler.start();
	}

	@Override
	public void update(BackgroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		cacheFlows();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void unload() throws Exception {

		try {
			scheduler.stop();
		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}

		systemInterface.getEventHandler().removeEventListener(Flow.class, CRUDEvent.class, this);

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		FlowEngineDAOFactory daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		flowDAO = daoFactory.getFlowDAO();
	}

	@Override
	protected BackgroundModuleResponse processBackgroundRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		if(popularFlows == null){

			return null;
		}

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		XMLUtils.appendNewCDATAElement(doc, documentElement, "browserModuleAlias", req.getContextPath() + flowBrowserModule.getFullAlias());
		
		Element flowsElement = XMLUtils.appendNewElement(doc, documentElement, "Flows");
		
		SiteProfile siteProfile = flowBrowserModule.getCurrentSiteProfile(req, user, uriParser, null);
		
		String absoluteFileURL = flowBrowserModule.getAbsoluteFileURL(uriParser, null);
		
		for(Flow flow : popularFlows) {
			
			flowsElement.appendChild(flow.toXML(doc, siteProfile, absoluteFileURL, req));
		}
		
		if(user != null){
			XMLUtils.appendNewElement(doc, documentElement, "loggedIn");
		}

		return new SimpleBackgroundModuleResponse(doc);
	}

	private void cacheFlows(){

		popularFlows = cacheFlows(dataSource, flowDAO, log, interval, flowCount);
	}

	public static List<FlowFamily> getFlowFamilyPopularity(DataSource dataSource, List<Integer> familyIDs, int interval) throws SQLException{

		List<FlowFamily> flowFamilies = new ArrayList<FlowFamily>(familyIDs.size());
		
		for(Integer flowFamilyID : familyIDs){

			//Get all flow IDs for this family
			List<Integer> flowIDs = new ArrayListQuery<Integer>(dataSource, "SELECT flowID FROM flowengine_flows WHERE flowFamilyID = " + flowFamilyID + " AND hideFromOverview = false", IntegerPopulator.getPopulator()).executeQuery();

			if(flowIDs != null){

				Timestamp calculateTime = new Timestamp(System.currentTimeMillis() - (MillisecondTimeUnits.HOUR * interval));

				//Get all flow instances submitted within the configured interval of hours
				ObjectQuery<Integer> instanceCountQuery = new ObjectQuery<Integer>(dataSource, "SELECT COUNT(flowInstanceID) FROM flowengine_flow_instances WHERE flowID IN (" + StringUtils.toCommaSeparatedString(flowIDs) + ") AND firstSubmitted >= ?", IntegerPopulator.getPopulator());

				instanceCountQuery.setTimestamp(1, calculateTime);

				FlowFamily flowFamily = new FlowFamily();
				flowFamily.setFlowFamilyID(flowFamilyID);
				flowFamily.setFlowInstanceCount(instanceCountQuery.executeQuery());

				//Get boost!
				ObjectQuery<Integer> boostQuery = new ObjectQuery<Integer>(dataSource, "SELECT popularityBoost FROM flowengine_flow_families WHERE flowFamilyID = ?", IntegerPopulator.getPopulator());

				boostQuery.setInt(1, flowFamilyID);

				Integer boost = boostQuery.executeQuery();

				if(boost != null){

					flowFamily.setFlowInstanceCount(flowFamily.getFlowInstanceCount() + boost);
				}

				//Get external flow redirects
				ObjectQuery<Integer> redirectCountQuery = new ObjectQuery<Integer>(dataSource, "SELECT COUNT(redirectID)/4 FROM flowengine_external_flow_redirects WHERE flowID IN (" + StringUtils.toCommaSeparatedString(flowIDs) + ") AND time >= ?", IntegerPopulator.getPopulator());

				redirectCountQuery.setTimestamp(1, calculateTime);

				Integer redirects = redirectCountQuery.executeQuery();

				if(redirects != null) {

					flowFamily.setFlowInstanceCount(flowFamily.getFlowInstanceCount() + redirects);
				}

				flowFamilies.add(flowFamily);
			}
		}
		
		return flowFamilies;
	}

	public static List<Flow> cacheFlows(DataSource dataSource, AnnotatedDAO<Flow> flowDAO, Logger log, int interval, int flowCount){

		log.info("Caching the " + flowCount + " most popular flow families over the past " + interval + " hours...");

		try{
			//Get ID of all families with at least one published flow
			List<Integer> familyIDs = new ArrayListQuery<Integer>(dataSource, "SELECT DISTINCT flowFamilyID FROM flowengine_flows WHERE publishDate <= CURDATE() AND (unPublishDate IS NULL OR unPublishDate > CURDATE());", IntegerPopulator.getPopulator()).executeQuery();

			if(familyIDs != null){

				List<FlowFamily> flowFamilies = getFlowFamilyPopularity(dataSource, familyIDs, interval);

				Collections.sort(flowFamilies, FAMILY_COMPARATOR);

				ArrayList<Flow> flows = new ArrayList<Flow>(flowCount);

				//Get latest published flow for each family until flowCount has been reached
				for(FlowFamily flowFamily : flowFamilies){

					LowLevelQuery<Flow> flowQuery = new LowLevelQuery<Flow>("SELECT * FROM flowengine_flows WHERE flowFamilyID = ? AND publishDate <= CURDATE() AND (unPublishDate IS NULL OR unPublishDate > CURDATE()) ORDER BY version DESC LIMIT 1;");
					flowQuery.addParameter(flowFamily.getFlowFamilyID());
					flowQuery.addRelations(Flow.FLOW_TYPE_RELATION, Flow.STEPS_RELATION);

					Flow flow = flowDAO.get(flowQuery);

					if(flow != null && !flow.getFlowType().isUseAccessFilter() && !flow.isHideFromOverview()){

						flow.setFlowFamily(flowFamily);
						flow.setHasTextTags(TextTagReplacer.hasTextTags(flow));
						flows.add(flow);

						if(flows.size() == flowCount){

							break;
						}
					}
				}

				if(!flows.isEmpty()){

					log.info("Cached " + flows.size() + " flows");

					return flows;
				}
			}

			log.info("No flows cached.");

		}catch(SQLException e){

			log.error("Error caching flows", e);
		}

		return null;
	}

	@Override
	public void processEvent(CRUDEvent<Flow> event, EventSource source) {

		if(!skipEvent(event.getAction(), event.getBeans())) {
			
			cacheFlows();
		}
	}

	private boolean skipEvent(CRUDAction action, List<Flow> flows) {

		if(action == CRUDAction.ADD) {
			
			for(Flow flow : flows) {
				
				if(flow.getPublishDate() != null) {
					
					return false;
				}
			}
		
		}else if(action == CRUDAction.UPDATE) {
			
			for(Flow flow : flows) {
				
				if(flow.getPublishDate() != null || (this.popularFlows != null && this.popularFlows.contains(flow))) {
					
					return false;
				}
			}
		
		}else if(action == CRUDAction.DELETE) {
		
			if(this.popularFlows != null) {
			
				for(Flow flow : flows) {
					
					if(this.popularFlows.contains(flow)) {
						
						return false;
					}
				}
			
			}
		}
		
		return true;
	}
	
	@Override
	public void run() {

		cacheFlows();
	}

	@Override
	public int getPriority() {

		return 0;
	}

	public List<Flow> getPopularFlows() {

		return popularFlows;
	}

}
