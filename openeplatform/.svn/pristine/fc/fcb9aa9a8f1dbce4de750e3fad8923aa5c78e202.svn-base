package org.oeplatform.pbl;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.PathType;
import se.unlogic.hierarchy.core.handlers.SimpleSettingHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;

public class PBLKnowledgeBankModule extends AnnotatedForegroundModule {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PBL API URL ", description = "The URL to the PBL knowledge bank rest api", required = true)
	protected String pblAPIURL;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "PBL background module alias", description = "Aliases for BPL background module", required = true)
	protected String pblBgModuleAliases = "*";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PBL background module name", description = "The name of the PBL background module", required = true)
	protected String pblBgModuleName = "PBL module (background)";

	@ModuleSetting
	@EnumDropDownSettingDescriptor(name = "PBL background module xslt path type", description = "The pathtype of the xslt", required = true)
	protected PathType pblBgModuleXSLType = PathType.Classpath;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PBL background module xsl path", description = "The xsl path of the PBL background module", required = true)
	protected String pblBgModuleXSLPath;

	@ModuleSetting
	@TextAreaSettingDescriptor(name = "PBL background module slots", description = "Slots for PBL background module", required = true)
	protected String pblBgModuleSlots;

	protected WeakReference<SimpleBackgroundModuleDescriptor> bgPBLModule = null;

	@Override
	protected void moduleConfigured() {

		try {
			
			createPBLBackgroundModule();
			
		} catch (Exception e) {
			
			log.error("Unable to create background module for PBL proxy module", e);
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		String word = null;

		if ((word = req.getParameter("word")) != null) {

			try {

				word = URLEncoder.encode(word, "ISO-8859-1");
				
				log.info("User " + user + " searching for word " + word + " in PBL knowledge bank");

				String response = HTTPUtils.sendHTTPGetRequest(pblAPIURL + "?word=" + word, null, null, null);

				HTTPUtils.sendReponse(getUnescapedText(response), JsonUtils.getContentType(), res);

			} catch (IOException e) {

				log.warn("Unable to get word " + word + " from PBL knowledge bank. Caused by: " + e.getMessage());

				JsonObject error = new JsonObject();
				error.putField("Error", "true");

				HTTPUtils.sendReponse(error.toJson(), JsonUtils.getContentType(), res);

			}

		}

		return null;

	}

	private void createPBLBackgroundModule() throws Exception {

		if (!StringUtils.isEmpty(this.pblBgModuleXSLPath) && !StringUtils.isEmpty(this.pblBgModuleSlots)) {

			boolean update = false;
			SimpleBackgroundModuleDescriptor descriptor = null;

			if (bgPBLModule != null && (descriptor = bgPBLModule.get()) != null) {
				update = true;
			} else {
				descriptor = new SimpleBackgroundModuleDescriptor();
			}

			if (!StringUtils.isEmpty(this.pblBgModuleAliases)) {
				String[] aliases = this.pblBgModuleAliases.split("\\n");
				
				descriptor.setAliases(Arrays.asList(aliases));
			}

			String[] slots = this.pblBgModuleSlots.split("\\n");
			descriptor.setSlots(Arrays.asList(slots));

			descriptor.setXslPathType(this.pblBgModuleXSLType);
			descriptor.setXslPath(this.pblBgModuleXSLPath);
			descriptor.setName(this.pblBgModuleName);
			descriptor.setClassname(PBLKnowledgeBankBgModule.class.getName());
			descriptor.setAdminAccess(true);
			descriptor.setUserAccess(true);
			descriptor.setAnonymousAccess(true);

			Map<String, List<String>> settings = this.moduleDescriptor.getMutableSettingHandler().getMap();

			settings.put("pblProxyModuleAlias", Collections.singletonList(this.sectionInterface.getSectionDescriptor().getFullAlias() + "/" + moduleDescriptor.getAlias()));

			descriptor.setMutableSettingHandler(new SimpleSettingHandler(settings));
			descriptor.setEnabled(true);
			descriptor.setSectionID(sectionInterface.getSectionDescriptor().getSectionID());
			descriptor.setStaticContentPackage("staticcontent");
			descriptor.setDataSourceID(this.moduleDescriptor.getDataSourceID());

			if (update) {

				sectionInterface.getBackgroundModuleCache().update(descriptor);

			} else {

				sectionInterface.getBackgroundModuleCache().cache(descriptor);
				bgPBLModule = new WeakReference<SimpleBackgroundModuleDescriptor>(descriptor);

			}

		}

	}

	@Override
	public void unload() throws Exception {

		super.unload();

		SimpleBackgroundModuleDescriptor bgModuleDescriptor = null;

		if (bgPBLModule != null && (bgModuleDescriptor = bgPBLModule.get()) != null) {

			try {
				sectionInterface.getBackgroundModuleCache().unload(bgModuleDescriptor);
			} catch (Exception e) {
				log.error("Error unloding background module " + bgModuleDescriptor + " while unloading module " + moduleDescriptor, e);
			}
		}
	}

	private String getUnescapedText(String text) {

		if (text != null) {

			Charset utf8charset = Charset.forName("UTF-8");

			Charset iso88591charset = Charset.forName("ISO-8859-1");

			ByteBuffer inputBuffer = ByteBuffer.wrap(text.getBytes());

			CharBuffer data = utf8charset.decode(inputBuffer);

			ByteBuffer outputBuffer = iso88591charset.encode(data);

			text = new String(outputBuffer.array());

		}

		return text;
	}

}
