package com.nordicpeak.flowengine;

import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.Map.Entry;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AccessInterface;
import se.unlogic.hierarchy.core.interfaces.modules.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.modules.Module;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ModuleDescriptor;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLink;
import se.unlogic.hierarchy.core.utils.extensionlinks.ExtensionLinkProvider;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.references.WeakReferenceUtils;

public class FlowAdminFlowListExtensionLinkModule extends AnnotatedForegroundModule implements ExtensionLinkProvider {
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Module ID", description = "The ID of the module to link to")
	protected Integer moduleID;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Open in new tab", description = "If link should be opened in new tab")
	protected boolean openInNewTab;
	
	private FlowAdminModule flowAdminModule;
	
	protected WeakReference<Entry<? extends ModuleDescriptor, ? extends Module<?>>> moduleEntryReference;
	
	@InstanceManagerDependency
	public void setFlowAdminModule(FlowAdminModule flowAdminModule) throws SQLException {

		if (flowAdminModule != null) {

			flowAdminModule.addFlowListExtensionLinkProvider(this);

		} else if (this.flowAdminModule != null) {

			this.flowAdminModule.removeFlowListExtensionLinkProvider(this);
		}
		
		this.flowAdminModule = flowAdminModule;
	}
	
	@Override
	public void unload() throws Exception {

		if (flowAdminModule != null) {

			flowAdminModule.removeFlowListExtensionLinkProvider(this);
		}

		super.unload();
	}

	@Override
	public AccessInterface getAccessInterface() {

		Entry<? extends ModuleDescriptor, ? extends Module<?>> moduleEntry = getModuleEntry();
		
		if (moduleEntry != null) {
			
			return moduleEntry.getKey();
		}
		
		return null;
	}

	@Override
	public ExtensionLink getExtensionLink(User user) {
		
		Entry<? extends ModuleDescriptor, ? extends Module<?>> moduleEntry = getModuleEntry();
		
		if (moduleEntry != null) {
			AnnotatedForegroundModule module = (AnnotatedForegroundModule) moduleEntry.getValue();
			ForegroundModuleDescriptor descriptor = (ForegroundModuleDescriptor) moduleEntry.getKey();
			
			return new ExtensionLink(descriptor.getName(), systemInterface.getContextPath() + module.getFullAlias(), openInNewTab, null, null);
		}
		
		return null;
	}
	
	private Entry<? extends ModuleDescriptor, ? extends Module<?>> getModuleEntry() {
		
		Entry<? extends ModuleDescriptor, ? extends Module<?>> moduleEntry = WeakReferenceUtils.getReferenceValue(moduleEntryReference);

		if (moduleEntry == null) {

			moduleEntry = ModuleUtils.findForegroundModule(ForegroundModule.class, true, moduleID, true, systemInterface.getRootSection());

			if (moduleEntry != null) {

				this.moduleEntryReference = new WeakReference<Entry<? extends ModuleDescriptor, ? extends Module<?>>>(moduleEntry);
			}
		}
		
		return moduleEntry;
	}
	
	@Override
	protected void moduleConfigured() throws Exception {
		
		if (flowAdminModule != null) {

			flowAdminModule.removeFlowListExtensionLinkProvider(this);
			flowAdminModule.addFlowListExtensionLinkProvider(this);
		}
	}
}
