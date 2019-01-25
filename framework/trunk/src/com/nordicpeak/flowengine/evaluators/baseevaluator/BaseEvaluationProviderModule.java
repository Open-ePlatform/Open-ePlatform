package com.nordicpeak.flowengine.evaluators.baseevaluator;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.events.EventHandler;
import se.unlogic.hierarchy.core.interfaces.instances.InstanceListener;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.EvaluatorTypeDescriptor;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;


public abstract class BaseEvaluationProviderModule<E extends BaseEvaluator> extends AnnotatedForegroundModule implements GenericEvaluationProvider<E>, InstanceListener<EvaluationHandler>, BaseEvaluatorCRUDCallback{

	@XSLVariable(prefix = "java.")
	protected String evaluatorTypeName = "This variable should be set by your stylesheet";

	@ModuleSetting
	@TextFieldSettingDescriptor(name="Evaluator type ID", description="The ID used to be used for evaluators of this type (this ID should not be changes if there are users with open flow instances in their session)", required=true)
	protected String evaluatorTypeID = this.getClass().getName();

	protected String oldEvaluatorTypeID;

	protected FlowAdminModule flowAdminModule;

	protected EvaluatorTypeDescriptor evaluatorDescriptor;

	@Override
	protected synchronized void moduleConfigured() throws Exception {

		super.moduleConfigured();

		if(evaluatorDescriptor == null){

			evaluatorDescriptor = new EvaluatorTypeDescriptor(evaluatorTypeID, evaluatorTypeName);

			//Rename legacy integer based queryTypeID's on module startup
			oldEvaluatorTypeID = this.moduleDescriptor.getModuleID().toString();

			systemInterface.getInstanceHandler().addInstanceListener(EvaluationHandler.class, this);

		}else if(!evaluatorDescriptor.getEvaluatorTypeID().equals(evaluatorTypeID)){

			//evaluatorTypeID has been changed since the module was started

			oldEvaluatorTypeID = evaluatorDescriptor.getEvaluatorTypeID();

			EvaluationHandler evaluationHandler = systemInterface.getInstanceHandler().getInstance(EvaluationHandler.class);

			if(evaluationHandler != null){

				evaluationHandler.removeEvaluationProvider(evaluatorDescriptor);
			}

			evaluatorDescriptor = new EvaluatorTypeDescriptor(evaluatorTypeID, evaluatorTypeName);

			if(evaluationHandler != null){

				addEvaluationProvider(evaluationHandler);
			}
		}

		checkOldEvaluatorTypeID();
	}

	protected void checkOldEvaluatorTypeID() throws SQLException {

		if(flowAdminModule != null && oldEvaluatorTypeID != null && flowAdminModule.getEvaluatorCount(oldEvaluatorTypeID) > 0){

			flowAdminModule.changeEvaluatorTypeID(oldEvaluatorTypeID, evaluatorDescriptor.getEvaluatorTypeID());

			this.oldEvaluatorTypeID = null;
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstanceListener(EvaluationHandler.class, this);

		EvaluationHandler evaluationHandler = systemInterface.getInstanceHandler().getInstance(EvaluationHandler.class);

		if (evaluationHandler != null) {

			evaluationHandler.removeEvaluationProvider(evaluatorDescriptor);
		}
		
		super.unload();
	}

	protected void addEvaluationProvider(EvaluationHandler evaluationHandler){

		if(!evaluationHandler.addEvaluationProvider(this)){

			log.error("Unable to add evaluation provider " + this + " to evaluation handler, a evaluation provider with ID " + evaluatorTypeID + " is already registered");
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		return null;
	}

	@Override
	public EvaluatorTypeDescriptor getEvaluatorType() {

		return evaluatorDescriptor;
	}

	@Override
	public <InstanceType extends EvaluationHandler> void instanceAdded(Class<EvaluationHandler> key, InstanceType instance) {

		instance.addEvaluationProvider(this);
	}

	@Override
	public <InstanceType extends EvaluationHandler> void instanceRemoved(Class<EvaluationHandler> key, InstanceType instance) {}

	@Override
	public FlowAdminModule getFlowAdminModule() {

		return flowAdminModule;
	}

	@Override
	public EventHandler getEventHandler() {

		return systemInterface.getEventHandler();
	}

	@Override
	public String getAbsoluteFileURL(URIParser uriParser, Object bean) {

		return null;
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(documentElement);
		return doc;
	}

	@InstanceManagerDependency(required=true)
	public synchronized void setFlowAdminModule(FlowAdminModule flowAdminModule) throws SQLException{

		this.flowAdminModule = flowAdminModule;

		checkOldEvaluatorTypeID();
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	
	@Override
	public String getEvaluatorTypeName() {
	
		return evaluatorTypeName;
	}
}
