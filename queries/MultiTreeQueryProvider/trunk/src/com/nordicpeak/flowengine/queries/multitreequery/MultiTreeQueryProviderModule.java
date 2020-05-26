package com.nordicpeak.flowengine.queries.multitreequery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.beans.SimpleImmutableAlternative;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.queries.multitreequery.beans.MultiTreeQuery;
import com.nordicpeak.flowengine.queries.multitreequery.beans.MultiTreeQueryInstance;
import com.nordicpeak.flowengine.queries.multitreequery.beans.StoredTreeNode;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;
import com.nordicpeak.treequerydataprovider.TreeDataHandler;
import com.nordicpeak.treequerydataprovider.TreeDataProvider;
import com.nordicpeak.treequerydataprovider.TreeNode;

public class MultiTreeQueryProviderModule extends BaseQueryProviderModule<MultiTreeQueryInstance> implements TreeDataHandler {

	@XSLVariable(prefix = "java.")
	private String missingTreeProvider = "Missing";

	@XSLVariable(prefix = "java.")
	protected String selectedAlternativeName = "This variable should be set by your stylesheet";

	private AnnotatedDAO<MultiTreeQuery> queryDAO;
	private AnnotatedDAO<MultiTreeQueryInstance> queryInstanceDAO;

	private MultiTreeQueryCRUD queryCRUD;

	private QueryParameterFactory<MultiTreeQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<MultiTreeQueryInstance, Integer> queryInstanceIDParamFactory;

	private ConcurrentHashMap<String, TreeDataProvider> treeProviders = new ConcurrentHashMap<>();

	private ImmutableAlternative selectedAlternative;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(MultiTreeQueryProviderModule.class, this)) {

			throw new RuntimeException("Unable to register module " + moduleDescriptor + " in global instance handler using key " + MultiTreeQueryProviderModule.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		ModuleUtils.checkRequiredModuleSettings(moduleDescriptor, this, systemInterface, Level.ERROR);

		selectedAlternative = new SimpleImmutableAlternative(selectedAlternativeName, 1, 1);
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(MultiTreeQueryProviderModule.class, this);

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, MultiTreeQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(MultiTreeQuery.class);
		queryInstanceDAO = daoFactory.getDAO(MultiTreeQueryInstance.class);

		queryCRUD = new MultiTreeQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<>(MultiTreeQuery.class), "MultiTreeQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		MultiTreeQuery query = new MultiTreeQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setSelectedAlternative(selectedAlternative);

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws Throwable {

		MultiTreeQuery query = new MultiTreeQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

		MultiTreeQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setSelectedAlternative(selectedAlternative);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		MultiTreeQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		query.setSelectedAlternative(selectedAlternative);

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		MultiTreeQueryInstance queryInstance;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new MultiTreeQueryInstance();

		} else {

			queryInstance = getQueryInstance(descriptor.getQueryInstanceID());

			if (queryInstance == null) {

				return null;
			}
		}

		queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

		if (queryInstance.getQuery() == null) {

			return null;
		}

		queryInstance.getQuery().setSelectedAlternative(selectedAlternative);

		if (req != null) {

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}

		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		//If this is a new query instance copy the default values
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance.setFullTree(getFullTree(queryInstance, poster, null));
		}

		return queryInstance;
	}

	@Override
	public QueryResponse getShowHTML(MultiTreeQueryInstance queryInstance, HttpServletRequest req, User user, User poster, String updateURL, String queryRequestURL, AttributeHandler attributeHandler) throws TransformerConfigurationException, TransformerException {
	
		queryInstance.generateNodeHierarchy();
		
		return super.getShowHTML(queryInstance, req, user, poster, updateURL, queryRequestURL, attributeHandler);
	}
	
	@Override
	public QueryResponse getFormHTML(MultiTreeQueryInstance queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, RequestMetadata requestMetadata, AttributeHandler attributeHandler) throws TransformerConfigurationException, TransformerException {

		if (queryInstance.getFullTree() == null) {

			queryInstance.setFullTree(getFullTree(queryInstance, poster, requestMetadata));
		}

		return super.getFormHTML(queryInstance, req, user, poster, validationErrors, enableAjaxPosting, queryRequestURL, requestMetadata, attributeHandler);
	}

	private MultiTreeQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<MultiTreeQuery> query = new HighLevelQuery<>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private MultiTreeQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<MultiTreeQuery> query = new HighLevelQuery<>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private MultiTreeQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<MultiTreeQueryInstance> query = new HighLevelQuery<>(MultiTreeQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(MultiTreeQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}

	@Override
	public void populate(MultiTreeQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		Integer queryID = queryInstance.getQuery().getQueryID();

		List<ValidationError> validationErrors = new ArrayList<>();

		List<String> treeNodeIndentifiers = ValidationUtils.validateParameters("q" + queryID + "_selectedkey", req, false, StringPopulator.getPopulator(), validationErrors);

		List<StoredTreeNode> selectedTreeNodes = null;

		if (!CollectionUtils.isEmpty(treeNodeIndentifiers)) {

			for (String treeNodeIdentifier : treeNodeIndentifiers) {

				StoredTreeNode selectedTreeNode = null;

				if (queryInstance.getFullTree() != null) {

					selectedTreeNode = queryInstance.getFullTree().find(treeNodeIdentifier);

				} else if (queryInstance.getSelectedNodeKeys() != null && queryInstance.getSelectedNodeKeys().contains(treeNodeIdentifier)) {

					selectedTreeNode = queryInstance.getSelectedTreeNodeWithParents(treeNodeIdentifier);
				}

				if (selectedTreeNode == null || queryInstance.getQuery().isOnlyAllowSelectingLeafs() && !selectedTreeNode.isEmpty()) {

					validationErrors.add(new ValidationError("InvalidFormat"));

				} else {

					selectedTreeNodes = CollectionUtils.addAndInstantiateIfNeeded(selectedTreeNodes, selectedTreeNode);
				}
			}

		}

		if (selectedTreeNodes == null) {

			if (!allowPartialPopulation && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED) {

				validationErrors.add(new ValidationError("Required"));

			} else {

				queryInstance.reset(attributeHandler);
				return;
			}
		}

		if (!validationErrors.isEmpty()) {

			throw new ValidationException(validationErrors);
		}

		List<String> selectedTreeNodeKeys = new ArrayList<>(selectedTreeNodes.size());
		Set<StoredTreeNode> storedTreeNodes = new HashSet<>();

		for (StoredTreeNode selectedTreeNode : selectedTreeNodes) {

			selectedTreeNodeKeys.add(selectedTreeNode.getKey());
			storedTreeNodes.addAll(selectedTreeNode.getDirectParents());
		}

		queryInstance.setSelectedNodeKeys(selectedTreeNodeKeys);
		queryInstance.setStoredTreeNodes(new ArrayList<>(storedTreeNodes));

		queryInstance.getQueryInstanceDescriptor().setPopulated(true);

		if (queryInstance.getQuery().isSetAsAttribute()) {

			queryInstance.resetFlowInstanceAttributes(attributeHandler);
			queryInstance.setFlowInstanceAttributes(attributeHandler);
		}
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		MultiTreeQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		MultiTreeQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

		if (queryInstance == null) {

			return false;
		}

		this.queryInstanceDAO.delete(queryInstance, transactionHandler);

		return true;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap) throws SQLException {

		MultiTreeQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		queryDAO.add(query, transactionHandler, null);
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, MultiTreeQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler)));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}

		XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "StaticContent", "classpath://" + getClass().getPackage().getName().replace('.', '/') + "/staticcontent");
	}

	@Override
	protected Class<MultiTreeQueryInstance> getQueryInstanceClass() {

		return MultiTreeQueryInstance.class;
	}

	private StoredTreeNode getFullTree(MultiTreeQueryInstance queryInstance, User poster, RequestMetadata requestMetadata) {

		MultiTreeQuery query = queryInstance.getQuery();

		if (!StringUtils.isEmpty(query.getProviderIdentifier())) {

			TreeDataProvider provider = treeProviders.get(query.getProviderIdentifier());

			if (provider != null) {

				TreeNode treeNode = provider.getTree();

				if (treeNode != null) {

					return getStoredTreeNode(treeNode);
				}
			}

			log.warn("MultiTreeQueryDataProvider " + query.getProviderIdentifier() + " not found");
		}

		return null;
	}

	private StoredTreeNode getStoredTreeNode(TreeNode treeNode) {

		StoredTreeNode storedTreeNode = new StoredTreeNode(treeNode);

		for (TreeNode subNode : treeNode.values()) {

			storedTreeNode.put(getStoredTreeNode(subNode));
		}

		return storedTreeNode;
	}

	@Override
	public void addTreeProvider(TreeDataProvider treeProvider) {

		if (treeProviders.containsKey(treeProvider.getTreeID())) {

			log.error("TreeProvider \"" + treeProvider.getTreeName() + "\" (ID= " + treeProvider.getTreeID() + ") is already registered");
			return;
		}

		treeProviders.put(treeProvider.getTreeID(), treeProvider);

		log.info("MultiTreeQueryDataProvider " + treeProvider.getTreeName() + " registered");
	}

	@Override
	public boolean removeTreeProvider(TreeDataProvider treeProvider) {

		boolean removed = treeProviders.remove(treeProvider.getTreeID(), treeProvider);

		if (removed) {
			log.info("MultiTreeQueryDataProvider " + treeProvider.getTreeName() + " unregistered");
		}

		return removed;
	}

	@Override
	public TreeDataProvider getTreeProvider(String id) {

		return treeProviders.get(id);
	}

	@Override
	public Collection<TreeDataProvider> getTreeProviders() {

		return treeProviders.values();
	}

	public String getMissingTreeProvider() {

		return missingTreeProvider;
	}
}
