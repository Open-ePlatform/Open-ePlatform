package com.nordicpeak.flowengine.tags;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.settings.HTMLEditorSetting;
import se.unlogic.hierarchy.core.settings.Setting;
import se.unlogic.hierarchy.core.settings.TextFieldSetting;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.events.GlobalSettingsUpdatedEvent;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileSettingProvider;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.enums.TextTagType;

public class TextTagAdminModule extends AnnotatedForegroundModule implements CRUDCallback<User>, SiteProfileSettingProvider {

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Editor CSS", description = "Path to the desired CSS stylesheet for CKEditor (relative from the contextpath)", required = false)
	protected String cssPath;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Enable profile support", description="Controls of if profile support for tags is enabled.")
	protected boolean enableProfileSupport = true;
	
	protected TextTagCRUD textTagCRUD;

	protected AnnotatedDAO<TextTag> textTagDAO;
	
	protected QueryParameterFactory<TextTag, Integer> tagIDParamFactory;
	protected QueryParameterFactory<TextTag, String> tagNameParamFactory;

	protected SiteProfileHandler siteProfileHandler;

	protected Map<String, Setting> siteProfileSettings;

	@InstanceManagerDependency
	protected TagSharingProvider tagSharingProvider;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		if (!systemInterface.getInstanceHandler().addInstance(TextTagAdminModule.class, this)) {
			
			throw new RuntimeException("Unable to register module " + this.moduleDescriptor + " in global instance handler using key " + TextTagAdminModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(TextTagAdminModule.class, this);
		
		super.unload();
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		daoFactory.addBeanStringPopulator(new EnumPopulator<TextTagType>(TextTagType.class));

		textTagDAO = daoFactory.getDAO(TextTag.class);
		
		tagIDParamFactory = textTagDAO.getParamFactory("textTagID", Integer.class);
		tagNameParamFactory = textTagDAO.getParamFactory("name", String.class);

		textTagCRUD = new TextTagCRUD(textTagDAO.getWrapper("textTagID", Integer.class), textTagDAO.getWrapper("name", String.class), new AnnotatedRequestPopulator<TextTag>(TextTag.class), this);
	}
	
	@Override
	protected void moduleConfigured() throws Exception {

		cacheSiteProfileSettings();
		
		//Called in case value of enableProfileSupport has changed
		setSiteProfileHandler(siteProfileHandler);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return textTagCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic(alias = "add")
	public ForegroundModuleResponse addTag(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return textTagCRUD.add(req, res, user, uriParser);
	}

	@WebPublic(alias = "update")
	public ForegroundModuleResponse updateTag(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return textTagCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(alias = "delete")
	public ForegroundModuleResponse deleteTag(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return textTagCRUD.delete(req, res, user, uriParser);
	}
	
	@WebPublic(alias = "share")
	public ForegroundModuleResponse shareTags(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, IOException {

		if(!req.getMethod().equalsIgnoreCase("POST")) {
			
			redirectToDefaultMethod(req, res);
			
			log.warn("User " + user + " submitted sharing request with method " + req.getMethod());
			
			return null;
		}
		
		if(tagSharingProvider != null) {

			String[] targetNames = req.getParameterValues("target");
			
			if(targetNames == null) {
				
				redirectToDefaultMethod(req, res);
				
				log.warn("User " + user + " submitted sharing request without target parameters");
				
				return null;
			}
			
			List<TagSharingTarget> targets = getTargets(targetNames);
			
			if(targets == null) {
				
				redirectToDefaultMethod(req, res);
				
				log.warn("User " + user + " submitted sharing request with invalid target parameters");
				
				return null;
			}
			
			List<Integer> tagIDs = NumberUtils.toInt(req.getParameterValues("textTagID"));
			
			if(tagIDs == null) {
				
				redirectToDefaultMethod(req, res);
				
				log.warn("User " + user + " submitted sharing request without tag ID parameters");
				
				return null;
			}
			
			List<TextTag> tags = getTags(tagIDs);
			
			if(tags == null) {
				
				redirectToDefaultMethod(req, res);
				
				log.warn("User " + user + " submitted sharing request with invalid tag parameters");
				
				return null;
			}
			
			log.info("User " + user + " sharing tags " + tags + " with " + targets);
			
			try {
				tagSharingProvider.shareTags(tags, targets, req.getParameter("overwrite") != null);
				
			} catch (Exception e) {
				
				log.error("Error sharing tags " + tags + " with targets " + targets, e);
			}
		
		}else {
			
			log.warn("User " + user + " tried to share tags without having a tag sharing provider set" );
		}
		
		redirectToDefaultMethod(req, res);
		
		return null;
		
	}

	public void importTags(List<TextTag> tags, boolean overwrite) throws SQLException {
		
		List<TextTag> importedTags = new ArrayList<TextTag>(tags.size());
		
		for(TextTag importTag : tags) {
			
			TextTag existingTag = getTag(importTag.getName());
			
			if(existingTag != null) {
				
				if(overwrite) {
				
					existingTag.setName(importTag.getName());
					existingTag.setDescription(importTag.getDescription());
					existingTag.setDefaultValue(importTag.getDefaultValue());
					existingTag.setType(importTag.getType());
					
					log.info("Updating tag " + existingTag);
					textTagDAO.update(existingTag);
					importedTags.add(existingTag);
					
				}else{
					
					log.info("Skipping tag " + existingTag);
				}
				
			}else {
				
				log.info("Adding tag " + importTag);
				textTagDAO.add(importTag);
				importedTags.add(importTag);
			}
		}
		
		cacheSiteProfileSettings();
		
		for(TextTag textTag : importedTags) {
			
			ensureDefaultValue(textTag);
		}
	}
	
	private TextTag getTag(String name) throws SQLException {

		HighLevelQuery<TextTag> query = new HighLevelQuery<TextTag>();
		
		query.addParameter(tagNameParamFactory.getParameter(name));
		
		return textTagDAO.get(query);
	}

	private List<TagSharingTarget> getTargets(String[] targetNames) {

		if(tagSharingProvider.getTargets() != null) {
			
			List<TagSharingTarget> selectedTargets = new ArrayList<TagSharingTarget>(tagSharingProvider.getTargets().size());
			
			for(String targetName : targetNames) {
				
				TagSharingTarget target = tagSharingProvider.getTarget(targetName);
				
				if(target != null) {
					
					selectedTargets.add(target);
				}
			}
			
			if(!selectedTargets.isEmpty()) {
				
				return selectedTargets;
			}
		}
		
		return null;
	}

	private List<TextTag> getTags(List<Integer> tagIDs) throws SQLException {

		HighLevelQuery<TextTag> query = new HighLevelQuery<TextTag>();
		
		query.addParameter(tagIDParamFactory.getWhereInParameter(tagIDs));
		
		return textTagDAO.getAll(query);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(this.moduleDescriptor.toXML(doc));

		XMLUtils.appendNewElement(doc, document, "cssPath", cssPath);

		doc.appendChild(document);

		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return moduleDescriptor.getName();
	}

	public void cacheSiteProfileSettings() {

		try {

			log.info("Caching text tag site profile settings");

			Map<String, Setting> siteProfileSettings = new HashMap<String, Setting>();

			List<TextTag> textTags = textTagDAO.getAll();

			if (textTags != null) {

				for (TextTag textTag : textTags) {

					Setting setting = null;

					String description = textTag.getDescription() != null ? textTag.getDescription() : textTag.getName();

					if (textTag.getType().equals(TextTagType.TEXTFIELD)) {

						setting = new TextFieldSetting(textTag.getName(), textTag.getName(), description, textTag.getDefaultValue(), false);

					} else {

						setting = new HTMLEditorSetting(textTag.getName(), textTag.getName(), description, textTag.getDefaultValue(), false);

					}

					siteProfileSettings.put(setting.getId(), setting);

				}

			}

			this.siteProfileSettings = siteProfileSettings;

		} catch (SQLException e) {

			log.error("Unable to cache site profile settings", e);
		}

	}

	@InstanceManagerDependency(required = true)
	public void setSiteProfileHandler(SiteProfileHandler siteProfileHandler) {

		if (siteProfileHandler != null) {

			if(enableProfileSupport){
				
				siteProfileHandler.addSettingProvider(this);
				
			}else{
				
				siteProfileHandler.removeSettingProvider(this);
			}
			

		} else {

			if(this.siteProfileHandler != null){
				
				this.siteProfileHandler.removeSettingProvider(this);
			}
		}

		this.siteProfileHandler = siteProfileHandler;
	}

	@Override
	public List<Setting> getSiteProfileSettings() {

		if(siteProfileSettings != null) {
			
			return new ArrayList<Setting>(siteProfileSettings.values());
			
		}
		
		return null;
		
	}
	
	public void ensureDefaultValue(TextTag textTag) throws SQLException {
		
		if(siteProfileHandler != null) {
			
			siteProfileHandler.ensureGlobalSettingValues(Arrays.asList(siteProfileSettings.get(textTag.getName())), !enableProfileSupport);
		}
	}

	@Override
	public List<Setting> getSiteSubProfileSettings() {

		return null;
	}

	@Override
	public String getModuleName() {

		return moduleDescriptor.getName();
	}
	
	@EventListener(channel=SiteProfile.class)
	public void processEvent(GlobalSettingsUpdatedEvent event, EventSource source) throws SQLException{
		
		if(!enableProfileSupport && siteProfileHandler != null){
			
			siteProfileHandler.ensureGlobalSettingValues(getSiteProfileSettings(), !enableProfileSupport);
		}
	}

	
	Collection<? extends TagSharingTarget> getTagSharingTargets() {
	
		if(tagSharingProvider != null) {
			
			return tagSharingProvider.getTargets();
		}
		
		return null;
	}
	
	protected void deleteTagValue(TextTag textTag) throws SQLException {
		
		Setting setting = siteProfileSettings.get(textTag.getName());
		
		if(siteProfileHandler != null) {
			
			siteProfileHandler.deleteSettingValues(setting);
		}
	}
}
