package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.HttpSessionEvent;
import javax.sql.DataSource;

import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SessionListener;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.time.TimeUtils;

import com.nordicpeak.flowengine.beans.AbortedFlowInstance;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.managers.MutableFlowInstanceManager;



public class AbortedFlowInstanceListenerModule extends AnnotatedForegroundModule implements SessionListener{

	private AnnotatedDAO<AbortedFlowInstance> abortedFlowInstanceDAO;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if(systemInterface.getSessionListenerHandler() != null){

			systemInterface.getSessionListenerHandler().addListener(this);

		}else{

			log.warn("No session listener handler available, unable to listen for expired sessions");
		}
	}

	@Override
	public void unload() throws Exception {

		if(systemInterface.getSessionListenerHandler() != null){

			systemInterface.getSessionListenerHandler().removeListener(this);
		}

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		abortedFlowInstanceDAO = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler()).getAbortedFlowInstanceDAO();
	}

	@Override
	public void sessionCreated(HttpSessionEvent event) {}

	@SuppressWarnings("rawtypes")
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {

		Enumeration enumeration = event.getSession().getAttributeNames();

		while(enumeration.hasMoreElements()){

			String attributeName = (String)enumeration.nextElement();

			if(attributeName.startsWith(Constants.FLOW_INSTANCE_SESSION_PREFIX)){

				MutableFlowInstanceManager flowInstanceManager = (MutableFlowInstanceManager)event.getSession().getAttribute(attributeName);

				if(!flowInstanceManager.isPreviouslySaved()){

					AbortedFlowInstance abortedFlowInstance = new AbortedFlowInstance();

					abortedFlowInstance.setAdded(TimeUtils.getCurrentTimestamp());
					abortedFlowInstance.setFlowFamilyID(flowInstanceManager.getFlowInstance().getFlow().getFlowFamily().getFlowFamilyID());
					abortedFlowInstance.setFlowID(flowInstanceManager.getFlowID());
					abortedFlowInstance.setStepID(flowInstanceManager.getCurrentStep().getStepID());

					try{
						abortedFlowInstanceDAO.add(abortedFlowInstance);

						log.info("Added aborted flow instance entry for flow " + flowInstanceManager.getFlowInstance().getFlow());

					}catch(SQLException e){

						log.info("Error adding aborted flowinstance bean for flow instance manager " + flowInstanceManager, e);
					}
				}
			}
		}
	}

}
