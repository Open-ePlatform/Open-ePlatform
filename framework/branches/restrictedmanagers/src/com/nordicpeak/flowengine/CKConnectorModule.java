package com.nordicpeak.flowengine;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.FCKConnector;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.FlowType;

public class CKConnectorModule extends AnnotatedForegroundModule {

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "File store path", description = "Path to the directory to be used as filestore for this module", required = false)
	protected String filestore;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max upload size", description = "Maxmium upload size in megabytes allowed in a single post request", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer diskThreshold = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name="RAM threshold",description="Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.",required=true,formatValidator=PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;

	protected FCKConnector connector;

	@InstanceManagerDependency(required=true)
	protected FlowAdminModule flowAdminModule;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		connector = new FCKConnector(filestore, diskThreshold, ramThreshold);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		connector.setDiskThreshold(diskThreshold);
		connector.setRamThreshold(ramThreshold);
		connector.setFilestorePath(filestore);
	}

	@WebPublic
	public SimpleForegroundModuleResponse connector(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerFactoryConfigurationError, TransformerException, IOException, URINotFoundException, SQLException, AccessDeniedException {


		if (hasFlowTypeAccess(user)) {

			this.connector.processRequest(req, res, uriParser, user, moduleDescriptor);

			return null;

		}else{
			
			throw new AccessDeniedException("User does not have access to any flow type");	
		}
	}

	private boolean hasFlowTypeAccess(User user) {

		for(FlowType flowType : flowAdminModule.getCachedFlowTypes()){
			
			if(AccessUtils.checkAccess(user, flowType.getAdminAccessInterface())){
				
				return true;
			}
		}
		
		return false;
	}

	@WebPublic
	public ForegroundModuleResponse file(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		this.connector.processFileRequest(req, res, user, uriParser, moduleDescriptor, sectionInterface, 2, null);

		return null;
	}

}
