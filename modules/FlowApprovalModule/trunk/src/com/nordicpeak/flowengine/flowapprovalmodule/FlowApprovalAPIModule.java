package com.nordicpeak.flowengine.flowapprovalmodule;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.hierarchy.foregroundmodules.rest.URIParam;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;

import com.nordicpeak.flowengine.flowapprovalmodule.beans.FlowApprovalActivityRound;


public class FlowApprovalAPIModule extends AnnotatedRESTModule {

	private AnnotatedDAO<FlowApprovalActivityRound> activityRoundDAO;
	
	private QueryParameterFactory<FlowApprovalActivityRound, Integer> activityRoundIDParamFactory;
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, FlowApprovalAdminModule.class.getName(), new XMLDBScriptProvider(FlowApprovalAdminModule.class.getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), true, true, false);

		activityRoundDAO = daoFactory.getDAO(FlowApprovalActivityRound.class);

		activityRoundIDParamFactory = activityRoundDAO.getParamFactory("activityRoundID", Integer.class);
	}	
	
	@InstanceManagerDependency(required = true)
	protected FlowApprovalAdminModule approvalAdminModule;
	
	@RESTMethod(alias = "getpdf/{activityRoundID}", method = "get")
	public void getActiveQueueSlots(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, @URIParam(name = "activityRoundID") Integer activityRoundID) throws Throwable {
		
		HighLevelQuery<FlowApprovalActivityRound> query = new HighLevelQuery<>();
		query.addRelation(FlowApprovalActivityRound.ACTIVITY_GROUP_RELATION);
		query.addParameter(activityRoundIDParamFactory.getParameter(activityRoundID));

		FlowApprovalActivityRound round = activityRoundDAO.get(query);

		if (round == null) {
			
			log.warn("Flow approval activity round with ID " + activityRoundID + " not found");
			throw new URINotFoundException(uriParser);
		}

		File pdfFile = approvalAdminModule.getSignaturesPDF(round);

		if (pdfFile == null || !pdfFile.exists()) {

			log.warn("PDF for " + round + " not found");
			throw new URINotFoundException(uriParser);
		}
		
		try {
			log.info("Sending signature PDF for " + round + " to user " + user);

			String filename = round.getActivityGroup().getName() + " - signature - " + round.getActivityRoundID() + ".pdf";

			HTTPUtils.sendFile(pdfFile, filename, req, res, ContentDisposition.ATTACHMENT);

		} catch (Exception e) {
			log.info("Error sending PDF for " + round + " to user " + user + ", " + e);
		}
	}
	
}
