package com.nordicpeak.flowengine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.beans.TextTag;
import com.nordicpeak.flowengine.cruds.TextTagCRUD;
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

	protected SiteProfileHandler siteProfileHandler;

	protected Map<String, Setting> siteProfileSettings;

	@Override
	protected void moduleConfigured() throws Exception {

		cacheSiteProfileSettings();
		
		//Called in case value of enableProfileSupport has changed
		setSiteProfileHandler(siteProfileHandler);
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		daoFactory.addBeanStringPopulator(new EnumPopulator<TextTagType>(TextTagType.class));

		textTagDAO = daoFactory.getDAO(TextTag.class);

		textTagCRUD = new TextTagCRUD(textTagDAO.getWrapper("name", String.class), new AnnotatedRequestPopulator<TextTag>(TextTag.class), this);
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

						setting = new TextFieldSetting(textTag.getName(), textTag.getName(), description, textTag.getDefaultValue(), true);

					} else {

						setting = new HTMLEditorSetting(textTag.getName(), textTag.getName(), description, textTag.getDefaultValue(), true);

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
	public void siteProfileHandlerConfigurationUpdated(SiteProfileHandler siteProfileHandler) {
		
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
}
