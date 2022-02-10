package com.nordicpeak.flowengine.queries.childquery.filterapi;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.menu.MenuItemDescriptor;
import se.unlogic.hierarchy.core.utils.AttributeTagUtils;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.datatypes.SimpleEntry;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPResponse;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SimpleRequest;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.childrelationprovider.Child;
import com.nordicpeak.flowengine.interfaces.APISource;
import com.nordicpeak.flowengine.interfaces.APISourceHandler;
import com.nordicpeak.flowengine.interfaces.APISourceProvider;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.queries.childquery.ChildQueryProviderModule;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;
import com.nordicpeak.flowengine.queries.childquery.interfaces.ChildQueryFilterProvider;
import com.nordicpeak.flowengine.utils.UserAttributeTagUtils;

public class ChildQueryFilterEndpointAdminModule extends AnnotatedForegroundModule implements CRUDCallback<User>, ChildQueryFilterProvider, APISourceProvider {

	public static final AnnotatedBeanTagSourceFactory<User> USER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<User>(User.class, "$user.");

	@XSLVariable(prefix = "java.")
	private String apiSourceShortTypeName;
	
	@XSLVariable(prefix = "java.")
	private String apiSourceFullTypeName;
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Connection timeout", description = "The maximum time in seconds to wait for a connection")
	private Integer connectionTimeout = 10;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Read timeout", description = "The maximum time in seconds to wait for a response")
	private Integer readTimeout = 10;

	@ModuleSetting
	@SplitOnLineBreak
	@TextAreaSettingDescriptor(name = "Allowed encodings", description = "Encodings allowed to use")
	private List<String> allowedEncodings = new ArrayList<String>(Arrays.asList("ISO-8859-1", "UTF-8"));

	@InstanceManagerDependency
	private QueryHandler queryHandler;

	private AnnotatedDAO<ChildQuerySimpleFilterEndpoint> endpointDAO;

	private QueryParameterFactory<ChildQuerySimpleFilterEndpoint, Integer> endpointIDParamFactory;
	private QueryParameterFactory<ChildQuerySimpleFilterEndpoint, String> endpointNameParamFactory;

	private ChildQueryFilterEndpointCRUD endpointCRUD;

	private ChildQueryProviderModule childQueryProviderModule;

	private APISourceHandler apiSourceHandler;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, ChildQueryProviderModule.class.getName(), new XMLDBScriptProvider(ChildQueryProviderModule.class.getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		endpointDAO = daoFactory.getDAO(ChildQuerySimpleFilterEndpoint.class);

		endpointIDParamFactory = endpointDAO.getParamFactory("endpointID", Integer.class);
		endpointNameParamFactory = endpointDAO.getParamFactory("name", String.class);

		AdvancedAnnotatedDAOWrapper<ChildQuerySimpleFilterEndpoint, Integer> endpointDAOWrapper = endpointDAO.getAdvancedWrapper(Integer.class);
		endpointDAOWrapper.getGetAllQuery().disableAutoRelations(true);
		endpointDAOWrapper.getGetQuery().addRelations(ChildQuerySimpleFilterEndpoint.FIELDS_RELATION);
		endpointDAOWrapper.getGetQuery().disableAutoRelations(true);

		endpointCRUD = new ChildQueryFilterEndpointCRUD(endpointDAOWrapper, this);
	}

	@Override
	public void unload() throws Exception {

		if (this.childQueryProviderModule != null) {

			this.childQueryProviderModule.removeChildQueryFilterProvider(this);
		}

		if (this.apiSourceHandler != null) {

			this.apiSourceHandler.removeAPISourceProvider(this);
		}

		super.unload();
	}

	@InstanceManagerDependency
	public void setChildQueryProviderModule(ChildQueryProviderModule childQueryProviderModule) throws SQLException {

		if (childQueryProviderModule != null) {

			childQueryProviderModule.addChildQueryFilterProvider(this);

		} else if (this.childQueryProviderModule != null) {

			this.childQueryProviderModule.removeChildQueryFilterProvider(this);
		}

		this.childQueryProviderModule = childQueryProviderModule;
	}

	@InstanceManagerDependency
	public void setAPISourceHandler(APISourceHandler apiSourceHandler) throws SQLException {

		if (apiSourceHandler != null) {

			apiSourceHandler.addAPISourceProvider(this);

		} else if (this.apiSourceHandler != null) {

			this.apiSourceHandler.removeAPISourceProvider(this);
		}

		this.apiSourceHandler = apiSourceHandler;
		
		//Check if this module is fully cached, then trigger an update of the menu items
		if(sectionInterface.getForegroundModuleCache().isCached(moduleDescriptor)) {
			
			this.sectionInterface.getMenuCache().moduleUpdated(moduleDescriptor, this);
		}
	}

	protected Map<String, FilterAPIChild> getChildren(ChildQuerySimpleFilterEndpoint endpoint, Map<String, Child> navetChildMap, User user, String parentCitizenID, ImmutableFlow flow, AttributeHandler attributeHandler) {

		log.info("Getting filter children from " + endpoint);

		Map<String, FilterAPIChild> children = new HashMap<String, FilterAPIChild>();

		TagReplacer tagReplacer = new TagReplacer();

		String address = endpoint.getAddress();

		//Build childCitizenIdentifier tag source
		StringBuilder childCitizenIdentifierListBuilder = new StringBuilder();

		for (String childCitizenIdentifier : navetChildMap.keySet()) {

			if (childCitizenIdentifierListBuilder.length() != 0) {

				childCitizenIdentifierListBuilder.append(",");
			}

			childCitizenIdentifierListBuilder.append(childCitizenIdentifier);
		}

		tagReplacer.addTagSource(new SingleTagSource("$childCitizenIdentifiers", childCitizenIdentifierListBuilder.toString()));

		//Replace user attribute tags
		if (user != null) {

			address = UserAttributeTagUtils.replaceTags(address, user, true);
		}

		//Build user tag source
		tagReplacer.addTagSource(USER_TAG_SOURCE_FACTORY.getTagSource(user));

		//Replace tags
		address = tagReplacer.replace(address);

		//Replace attribute tags
		if (attributeHandler != null) {

			address = AttributeTagUtils.replaceTags(address, attributeHandler, false, true);
		}

		SimpleRequest simpleRequest = new SimpleRequest(address);

		simpleRequest.setFollowRedirects(false);
		simpleRequest.setConnectionTimeout(connectionTimeout * 1000);
		simpleRequest.setReadTimeout(readTimeout * 1000);

		if (endpoint.getUsername() != null && endpoint.getPassword() != null) {

			simpleRequest.setUsername(endpoint.getUsername());
			simpleRequest.setPassword(endpoint.getPassword());
		}

		List<Entry<String, String>> headerEntries = new ArrayList<Entry<String, String>>(2);

		headerEntries.add(new SimpleEntry<String, String>("NoLoginRedirect", "NoLoginRedirect"));
		simpleRequest.setHeaders(headerEntries);

		try {
			HTTPResponse response = HTTPUtils.sendHTTPGetRequest(simpleRequest, endpoint.getCharset());

			try {
				XMLParser parser = new XMLParser(XMLUtils.parseXML(response.getValue(), endpoint.getCharset().toString(), false, false));

				List<XMLParser> items = parser.getNodes("/Response/Child");

				if (items.size() > 0) {

					for (XMLParser item : items) {

						String childCitizenID = item.getString("CitizenIdentifier");

						Map<String, String> fields = null;

						if (endpoint.getFields() != null) {

							fields = new HashMap<String, String>();

							List<XMLParser> xmlFields = item.getNodes("Fields/Field");

							for (XMLParser field : xmlFields) {

								String fieldName = field.getString("Name");

								if (endpoint.getFields().contains(fieldName)) {

									String fieldValue = field.getString("Value");

									fields.put(fieldName, fieldValue);
								}
							}
						}

						children.put(childCitizenID, new FilterAPIChild(childCitizenID, fields));
					}
				}

			} catch (Exception e) {

				log.warn("Error parsing children XML for " + endpoint, e);
				return null;
			}

		} catch (IOException e) {

			log.warn("Error connecting to " + endpoint, e);
			return null;
		}

		return children;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return endpointCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return endpointCRUD.show(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return endpointCRUD.add(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return endpointCRUD.update(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		return endpointCRUD.delete(req, res, user, uriParser);
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

	@Override
	public String getTitlePrefix() {

		return moduleDescriptor.getName();
	}

	public List<String> getAllowedEncodings() {

		return allowedEncodings;
	}

	@Override
	public List<ChildQuerySimpleFilterEndpoint> getEndpoints() throws SQLException {

		HighLevelQuery<ChildQuerySimpleFilterEndpoint> query = new HighLevelQuery<ChildQuerySimpleFilterEndpoint>();

		return endpointDAO.getAll(query);
	}

	public ChildQuerySimpleFilterEndpoint getEndpoint(Integer endpointID) throws SQLException {

		HighLevelQuery<ChildQuerySimpleFilterEndpoint> query = new HighLevelQuery<ChildQuerySimpleFilterEndpoint>();
		query.addParameter(endpointIDParamFactory.getParameter(endpointID));

		ChildQuerySimpleFilterEndpoint endpoint = endpointDAO.get(query);

		if (endpoint != null) {
			endpoint.setAdminModule(this);
		}

		return endpoint;
	}

	@Override
	public ChildQuerySimpleFilterEndpoint getEndpoint(String name) throws SQLException {

		HighLevelQuery<ChildQuerySimpleFilterEndpoint> query = new HighLevelQuery<ChildQuerySimpleFilterEndpoint>();
		query.addParameter(endpointNameParamFactory.getParameter(name));

		ChildQuerySimpleFilterEndpoint endpoint = endpointDAO.get(query);

		if (endpoint != null) {
			endpoint.setAdminModule(this);
		}

		return endpoint;
	}

	public List<ChildQuery> getQueries(ChildQuerySimpleFilterEndpoint endpoint) throws SQLException {

		return childQueryProviderModule.getQueriesUsingFilterEndpoint(endpoint.getName());
	}

	public ChildQueryProviderModule getChildQueryProviderModule() {

		return childQueryProviderModule;
	}

	@Override
	public String getName() {

		return moduleDescriptor.getName();
	}

	@Override
	public List<? extends APISource> getAPISources() throws Exception {

		return endpointDAO.getAll();
	}

	@Override
	public String getShortTypeName() {

		return apiSourceShortTypeName;
	}
	
	@Override
	public String getFullTypeName() {

		return apiSourceFullTypeName;
	}
	
	@Override
	public String getAddURL() {

		return this.getFullAlias() + "/add";
	}
	
	@Override
	public String getBaseShowURL() {

		return this.getFullAlias() + "/show/";
	}

	@Override
	public String getBaseUpdateURL() {

		return this.getFullAlias() + "/update/";
	}

	@Override
	public String getBaseDeleteURL() {

		return this.getFullAlias() + "/delete/";
	}

	@Override
	public boolean isInUse(APISource apiSource) throws SQLException {
		
		return childQueryProviderModule.hasQueriesUsingFilterEndpoint(apiSource.getName());
	}

	public APISourceHandler getAPISourceHandler() {

		return apiSourceHandler;
	}

	@Override
	public List<? extends MenuItemDescriptor> getVisibleMenuItems() {

		if(this.apiSourceHandler != null) {
			
			return null;
		
		}else {
		
			return super.getVisibleMenuItems();
		}
	}
}