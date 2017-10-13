package com.nordicpeak.childrelationprovider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.populators.SwedishSocialSecurity12DigitsWithoutMinusPopulator;

import com.nordicpeak.childrelationprovider.exceptions.ChildRelationProviderException;

public class DummyChildRelationProvider extends AnnotatedForegroundModule implements ChildRelationProvider {
	
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "1 - citizen ID", description = "", required = true, formatValidator = SwedishSocialSecurity12DigitsWithoutMinusPopulator.class)
	private String childID1 = "201001011236";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "2 - citizen ID", description = "", required = true, formatValidator = SwedishSocialSecurity12DigitsWithoutMinusPopulator.class)
	private String childID2 = "200101011229";
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "1 - enable", description = "")
	private boolean child1;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "2 - enable", description = "")
	private boolean child2;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "1 - secret", description = "")
	private boolean secret1;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "2 - secret", description = "")
	private boolean secret2;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "1 - ensam v�rdnad", description = "")
	private boolean ensam1;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(ChildRelationProvider.class, this)) {
			
			throw new RuntimeException("Unable to register module " + moduleDescriptor + " in global instance handler using key " + ChildRelationProvider.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}
	
	@Override
	public void unload() throws Exception {
		
		systemInterface.getInstanceHandler().removeInstance(ChildRelationProvider.class, this);
		
		super.unload();
	}
	
	@Override
	public ChildrenResponse getChildrenWithGuardians(String citizenIdentifier) throws ChildRelationProviderException {
		
		Map<String, Child> children = new HashMap<String, Child>();
		
		if (child1 && !secret1) {
			
			SimpleChild child1 = new SimpleChild("kalle", "kula", childID1);
			
			if (ensam1) {
				
				child1.setGuardians(Arrays.asList(new Guardian[] { new SimpleGuardian("f�r�lder", "1", citizenIdentifier) }));
				
			} else {
				
				child1.setGuardians(Arrays.asList(new Guardian[] { new SimpleGuardian("f�r�lder", "1", citizenIdentifier), new SimpleGuardian("f�r�lder", "2", "191234567890") }));
			}
			
			children.put(child1.getCitizenIdentifier(), child1);
		}
		
		if (child2 && !secret2) {
			
			SimpleChild child2 = new SimpleChild("lisa", "kula", childID2);
			child2.setGuardians(Arrays.asList(new Guardian[] { new SimpleGuardian("f�r�lder", "1", citizenIdentifier), new SimpleGuardian("f�r�lder", "2", "191234567890") }));
			
			children.put(child2.getCitizenIdentifier(), child2);
		}
		
		return new ChildrenResponse(children, secret1 || secret2);
	}
	
}
