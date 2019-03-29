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
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.datatypes.SimpleEntry;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPResponse;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SimpleRequest;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.beans.Step;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;
import com.nordicpeak.flowengine.utils.UserAttributeTagUtils;

public class ChildQueryFilterEndpointAdminModule extends AnnotatedForegroundModule implements CRUDCallback<User> {

	public static final AnnotatedBeanTagSourceFactory<User> USER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<User>(User.class, "$user.");

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

	private AnnotatedDAO<ChildQueryFilterEndpoint> endpointDAO;
	private AnnotatedDAO<ChildQuery> queryDAO;
	private AnnotatedDAOWrapper<QueryDescriptor, Integer> queryDescriptorDAOWrapper;

	private QueryParameterFactory<ChildQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<ChildQueryFilterEndpoint, Integer> endpointIDParamFactory;

	private ChildQueryFilterEndpointCRUD endpointCRUD;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(ChildQueryFilterEndpointAdminModule.class, this)) {

			throw new RuntimeException("Unable to register module " + moduleDescriptor + " in global instance handler using key " + ChildQueryFilterEndpointAdminModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(ChildQuery.class);
		endpointDAO = daoFactory.getDAO(ChildQueryFilterEndpoint.class);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		endpointIDParamFactory = endpointDAO.getParamFactory("endpointID", Integer.class);

		AdvancedAnnotatedDAOWrapper<ChildQueryFilterEndpoint, Integer> endpointDAOWrapper = endpointDAO.getAdvancedWrapper(Integer.class);
		endpointDAOWrapper.getGetAllQuery().addRelations(ChildQueryFilterEndpoint.QUERIES_RELATION);
		endpointDAOWrapper.getGetAllQuery().disableAutoRelations(true);
		endpointDAOWrapper.getGetQuery().addRelations(ChildQueryFilterEndpoint.QUERIES_RELATION, ChildQueryFilterEndpoint.FIELDS_RELATION);
		endpointDAOWrapper.getGetQuery().disableAutoRelations(true);

		endpointCRUD = new ChildQueryFilterEndpointCRUD(endpointDAOWrapper, this);

		FlowEngineDAOFactory flowDAOFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		queryDescriptorDAOWrapper = flowDAOFactory.getQueryDescriptorDAO().getWrapper(Integer.class);
		queryDescriptorDAOWrapper.addRelations(QueryDescriptor.STEP_RELATION, Step.FLOW_RELATION, Flow.FLOW_TYPE_RELATION, Flow.CATEGORY_RELATION);
		queryDescriptorDAOWrapper.setUseRelationsOnGet(true);
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(ChildQueryFilterEndpointAdminModule.class, this);

		super.unload();
	}

	public Map<String, FilterAPIChild> getChildren(ChildQueryFilterEndpoint endpoint, User user, String parentCitizenID) {

		Map<String, FilterAPIChild> children = new HashMap<String, FilterAPIChild>();

		String address = endpoint.getAddress();

		if (user != null) {

			address = UserAttributeTagUtils.replaceTags(address, user);

			TagReplacer tagReplacer = new TagReplacer();
			tagReplacer.addTagSource(USER_TAG_SOURCE_FACTORY.getTagSource(user));

			address = tagReplacer.replace(address);
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
				XMLParser parser = new XMLParser(XMLUtils.parseXML(response.getValue(), false, false));

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

	public List<ChildQuery> getQueries(ChildQueryFilterEndpoint endpoint) {

		try {
			ArrayListQuery<Integer> idQuery = new ArrayListQuery<Integer>(queryDAO.getDataSource(), "SELECT queryID FROM " + queryDAO.getTableName() + " WHERE filterEndpointID = ?", IntegerPopulator.getPopulator());
			idQuery.setInt(1, endpoint.getEndpointID());

			List<Integer> queryIDs = idQuery.executeQuery();

			if (!CollectionUtils.isEmpty(queryIDs)) {

				List<ChildQuery> queries = new ArrayList<ChildQuery>(queryIDs.size());

				for (Integer queryID : queryIDs) {

					QueryDescriptor queryDescriptor = queryDescriptorDAOWrapper.get(queryID);

					if (queryDescriptor != null) {

						queries.add((ChildQuery) getQuery(queryDescriptor));
					}
				}

				return queries;
			}

		} catch (SQLException e) {

			log.error("Unable to get queries using endpoint " + endpoint, e);
		}

		return null;
	}

	public Query getQuery(MutableQueryDescriptor descriptor) throws SQLException {

		ChildQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	public ChildQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<ChildQuery> getQuery = new HighLevelQuery<ChildQuery>();

		getQuery.addParameter(queryIDParamFactory.getParameter(queryID));

		ChildQuery query = queryDAO.get(getQuery);

		return query;
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
	
	public List<ChildQueryFilterEndpoint> getEndpoints() throws SQLException {

		HighLevelQuery<ChildQueryFilterEndpoint> query = new HighLevelQuery<ChildQueryFilterEndpoint>();
		
		return endpointDAO.getAll(query);
	}
	
	public ChildQueryFilterEndpoint getEndpoint(Integer endpointID) throws SQLException {

		HighLevelQuery<ChildQueryFilterEndpoint> query =  new HighLevelQuery<ChildQueryFilterEndpoint>();
		query.addParameter(endpointIDParamFactory.getParameter(endpointID));
		
		return endpointDAO.get(query);
	}
}