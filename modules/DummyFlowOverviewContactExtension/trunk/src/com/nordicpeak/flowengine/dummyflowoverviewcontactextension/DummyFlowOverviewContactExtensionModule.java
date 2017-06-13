package com.nordicpeak.flowengine.dummyflowoverviewcontactextension;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SimpleViewFragment;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.ViewFragmentModule;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.staticcontent.StaticContentModule;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.beans.ExtensionView;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.interfaces.FlowBrowserExtensionViewProvider;

public class DummyFlowOverviewContactExtensionModule extends AnnotatedForegroundModule implements ViewFragmentModule<ForegroundModuleDescriptor>, FlowBrowserExtensionViewProvider {
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Extension title", description = "", required = true)
	private String extensionViewTitle = "Frågor om e-tjänsten";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this extension provider compared to other providers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int priority = 0;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable fragment XML debug", description = "Enables debugging of fragment XML")
	private boolean debugFragmententXML;
	
	@InstanceManagerDependency(required = true)
	private StaticContentModule staticContentModule;
	
	private FlowBrowserModule flowBrowserModule;
	
	private ExtensionView extensionView;
	
	@Override
	protected void moduleConfigured() throws Exception {
		super.moduleConfigured();
		
		extensionView = new ExtensionView(extensionViewTitle, new SimpleViewFragment(""), "left-contact");
	}
	
	@InstanceManagerDependency(required = true)
	public void setFlowBrowserModule(FlowBrowserModule flowBrowserModule) {
		
		if (flowBrowserModule == null && this.flowBrowserModule != null) {
			
			this.flowBrowserModule.removeExtensionViewProvider(this);
		}
		
		this.flowBrowserModule = flowBrowserModule;
		
		if (this.flowBrowserModule != null) {
			
			this.flowBrowserModule.addExtensionViewProvider(this);
		}
	}
	
	@Override
	public void unload() throws Exception {
		
		if (flowBrowserModule != null) {
			
			setFlowBrowserModule(null);
		}
		
		super.unload();
	}
	
	@Override
	public int getPriority() {
		return priority;
	}
	
	@Override
	public ForegroundModuleDescriptor getModuleDescriptor() {
		
		return moduleDescriptor;
	}
	
	@Override
	public List<LinkTag> getLinkTags() {
		
		return links;
	}
	
	@Override
	public List<ScriptTag> getScriptTags() {
		
		return scripts;
	}
	
	@Override
	public ExtensionView getFlowOverviewExtensionView(Flow flow, HttpServletRequest req, User user, URIParser uriParser) throws TransformerConfigurationException, TransformerException, SQLException {
		
		return extensionView;
	}
	
}
