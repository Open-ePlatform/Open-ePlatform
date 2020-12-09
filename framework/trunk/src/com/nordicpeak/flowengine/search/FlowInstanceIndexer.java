package com.nordicpeak.flowengine.search;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.MySQLRowLimiter;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.QueryResultsStreamer;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.threads.PriorityThreadFactory;
import se.unlogic.webutils.http.HTTPUtils;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.ExternalMessageAttachment;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowFamilyManager;
import com.nordicpeak.flowengine.beans.FlowFamilyManagerGroup;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.beans.InternalMessageAttachment;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.search.events.AddFlowEvent;
import com.nordicpeak.flowengine.search.events.AddFlowFamilyEvent;
import com.nordicpeak.flowengine.search.events.AddUpdateFlowInstanceEvent;
import com.nordicpeak.flowengine.search.events.DeleteFlowEvent;
import com.nordicpeak.flowengine.search.events.DeleteFlowFamilyEvent;
import com.nordicpeak.flowengine.search.events.DeleteFlowInstanceEvent;
import com.nordicpeak.flowengine.search.events.InitialFlowInstanceIndexingEvent;
import com.nordicpeak.flowengine.search.events.QueuedIndexEvent;

public class FlowInstanceIndexer {

	private static final String ID_FIELD = "id";
	private static final String EXTERNAL_ID_FIELD = Constants.FLOW_INSTANCE_EXTERNAL_ID_ATTRIBUTE;
	private static final String FLOW_ID_FIELD = "flowID";
	private static final String FLOW_NAME_FIELD = "name";
	private static final String FLOW_FAMILY_ID_FIELD = "familyID";
	private static final String FIRST_SUBMITTED_FIELD = "firstSubmitted";
	private static final String STATUS_NAME_FIELD = "status";
	private static final String POSTER_FIELD = "poster";
	private static final String OWNERS_FIELD = "owners";
	private static final String MANAGER_FIELD = "manager";
	private static final String MANAGER_USER_FIELD = "managerUser";
	private static final String MANAGER_GROUP_FIELD = "managerGroup";
	private static final String CITIZEN_IDENTIFIER = "citizenIdentifier";
	private static final String CHILD_CITIZEN_IDENTIFIER = "childCitizenIdentifier";
	private static final String ORGANIZATION_NUMBER = "organizationNumber";
	private static final String INTERNAL_MESSAGES = "internalMessages";
	private static final String EXTERNAL_MESSAGES = "externalMessages";
	private static final String MANAGER_DESCRIPTION = "managerDescription";
	protected static final String ALLOWED_FULL_USER_FIELD = "allowedUser";
	protected static final String ALLOWED_FULL_GROUP_FIELD = "allowedGroup";
	protected static final String ALLOWED_RESTRICTED_USER_FIELD = "allowedRestrictedUser";
	protected static final String ALLOWED_RESTRICTED_GROUP_FIELD = "allowedRestricedGroup";
	
	private static final String[] SEARCH_FIELDS = new String[] { ID_FIELD, EXTERNAL_ID_FIELD, POSTER_FIELD, OWNERS_FIELD, MANAGER_FIELD, FLOW_NAME_FIELD, STATUS_NAME_FIELD, FIRST_SUBMITTED_FIELD, CITIZEN_IDENTIFIER, CHILD_CITIZEN_IDENTIFIER, ORGANIZATION_NUMBER, INTERNAL_MESSAGES, EXTERNAL_MESSAGES, MANAGER_DESCRIPTION};

	private static final int STREAMER_CHUNK_SIZE = 300;
	
	protected Logger log = Logger.getLogger(this.getClass());

	private final CaseInsensitiveWhitespaceAnalyzer analyzer = new CaseInsensitiveWhitespaceAnalyzer();
	private final Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;

	protected final SystemInterface systemInterface;
	protected final FlowEngineDAOFactory daoFactory;
	protected final QueryParameterFactory<Flow, Boolean> flowEnabledParamFactory;
	protected final QueryParameterFactory<Flow, FlowFamily> flowFlowFamilyParamFactory;
	
	protected final QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;
	protected final QueryParameterFactory<FlowInstance, Flow> flowInstanceFlowParamFactory;
	protected final QueryParameterFactory<FlowInstance, Timestamp> flowInstanceFirstSubmittedParamFactory;

	protected int maxHitCount;

	private boolean logIndexing = true;
	
	private LinkedBlockingQueue<QueuedIndexEvent> eventQueue = new LinkedBlockingQueue<QueuedIndexEvent>();

	private CallbackThreadPoolExecutor threadPoolExecutor;

	public FlowInstanceIndexer(FlowEngineDAOFactory daoFactory, int maxHitCount, SystemInterface systemInterface) throws IOException {

		super();
		this.daoFactory = daoFactory;
		this.maxHitCount = maxHitCount;
		this.systemInterface = systemInterface;

		flowEnabledParamFactory = daoFactory.getFlowDAO().getParamFactory("enabled", boolean.class);
		flowFlowFamilyParamFactory = daoFactory.getFlowDAO().getParamFactory("flowFamily", FlowFamily.class);

		flowInstanceIDParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flowInstanceID", Integer.class);
		flowInstanceFlowParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flow", Flow.class);
		flowInstanceFirstSubmittedParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("firstSubmitted", Timestamp.class);

		indexWriter = new IndexWriter(index, new IndexWriterConfig(analyzer));

		int availableProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();

		threadPoolExecutor = new CallbackThreadPoolExecutor(availableProcessors, availableProcessors, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), this);
		threadPoolExecutor.setThreadFactory(new PriorityThreadFactory(Thread.MIN_PRIORITY, "Flow instance indexer pool"));
	}

	public void cacheFlowInstances() {

		eventQueue.add(new InitialFlowInstanceIndexingEvent(getAllFlowInstancesStreamer()));
		checkQueueState(false);
	}

	public void close() {

		threadPoolExecutor.shutdownNow();
		try {
			threadPoolExecutor.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {}

		this.eventQueue.clear();
		threadPoolExecutor.purge();

		try{
			indexWriter.close();
		}catch(IOException e){
			log.warn("Error closing index writer", e);
		}

		try{
			index.close();
		}catch(IOException e){
			log.warn("Error closing index", e);
		}
	}

	public boolean isIndexable(ImmutableFlowInstance flowInstance){
		
		return flowInstance.getFirstSubmitted() != null;
	}
	
	public void search(HttpServletRequest req, HttpServletResponse res, User user, boolean checkAccess, boolean includeDescription) throws IOException {

		//Check if the index contains any documents
		if (indexReader == null || indexReader.numDocs() == 0) {

			sendEmptyResponse(res);
			return;
		}

		String queryString = req.getParameter("q");
		
		log.info("User " + user + " searching for: " + StringUtils.toLogFormat(queryString, 50));

		if(StringUtils.isEmpty(queryString)){

			sendEmptyResponse(res);
			return;
		}
		
		queryString = URLDecoder.decode(queryString, "UTF-8");
		//queryString = StringUtils.parseUTF8(queryString);
		queryString = queryString.trim();

		MultiFieldQueryParser parser = new MultiFieldQueryParser(SEARCH_FIELDS, analyzer);

		Query query;

		try{
			query = parser.parse(QueryParser.escape(queryString) + "*");
			//query = parser.parse(SearchUtils.rewriteQueryString(queryString));

		}catch(ParseException e){

			log.warn("Unable to parse query string " + StringUtils.toLogFormat(queryString, 50) + " requsted by user " + user + " accessing from " + req.getRemoteAddr());

			sendEmptyResponse(res);
			return;
		}

		TopDocs results;

		if(checkAccess){
			
			BooleanQuery.Builder builder = new BooleanQuery.Builder();
			builder.add(query, Occur.MUST);
			
			appendFilter(builder, user);
			
			results = searcher.search(builder.build(), maxHitCount);
			
		}else{
			
			results = searcher.search(query, maxHitCount);
		}
		
		if(results.scoreDocs.length == 0){

			sendEmptyResponse(res);
			return;
		}

		JsonArray jsonArray = new JsonArray();

		//Create JSON from hits
		for(ScoreDoc scoreDoc : results.scoreDocs){
			
			Document doc = searcher.doc(scoreDoc.doc);

			JsonObject instance = new JsonObject(4);
			instance.putField(ID_FIELD, doc.get(ID_FIELD));
			instance.putField(FLOW_NAME_FIELD, doc.get(FLOW_NAME_FIELD));
			instance.putField(STATUS_NAME_FIELD, doc.get(STATUS_NAME_FIELD));
			instance.putField(FIRST_SUBMITTED_FIELD, doc.get(FIRST_SUBMITTED_FIELD));
			
			if(includeDescription) {
			
				String managerDescription = doc.get(MANAGER_DESCRIPTION);
				
				if(managerDescription == null) {
					
					managerDescription = "";
				}

				instance.putField(MANAGER_DESCRIPTION, managerDescription);				
			}
			
			jsonArray.addNode(instance);
		}

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", Integer.toString(jsonArray.size()));
		jsonObject.putField("hits", jsonArray);
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
		return;
	}

	public static void sendEmptyResponse(HttpServletResponse res) throws IOException {

		JsonObject jsonObject = new JsonObject(1);
		jsonObject.putField("hitCount", "0");
		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
	}

	public int getMaxHitCount() {

		return maxHitCount;
	}

	public void setMaxHitCount(int maxHitCount) {

		this.maxHitCount = maxHitCount;
	}

	public void addFlowFamilies(List<FlowFamily> beans) {

		for (FlowFamily flowFamily : beans) {

			try {
				eventQueue.add(new AddFlowFamilyEvent(flowFamily, getFlowInstanceStreamerForFlowFamily(flowFamily)));
				checkQueueState(false);

			} catch (SQLException e) {
				log.error("Error queuing " + flowFamily + " for indexing");
			}
		}
	}

	public void updateFlowFamilies(List<FlowFamily> beans) {

		for (FlowFamily flowFamily : beans) {

			try {
				eventQueue.add(new DeleteFlowFamilyEvent(flowFamily));
				eventQueue.add(new AddFlowFamilyEvent(flowFamily, getFlowInstanceStreamerForFlowFamily(flowFamily)));
				checkQueueState(false);

			} catch (SQLException e) {
				log.error("Error queuing " + flowFamily + " for indexing");
			}
		}
	}

	public void deleteFlowFamilies(List<FlowFamily> beans) {

		for(FlowFamily flowFamily : beans){

			eventQueue.add(new DeleteFlowFamilyEvent(flowFamily));
			checkQueueState(false);
		}
	}

	public void addFlows(List<Flow> beans) {

		for(Flow flow : beans){

			if (flow.isEnabled()) {
				eventQueue.add(new AddFlowEvent(flow, getFlowInstanceStreamerForFlow(flow)));
				checkQueueState(false);
			}
		}
	}

	public void updateFlows(List<Flow> beans) {

		for(Flow flow : beans){

			eventQueue.add(new DeleteFlowEvent(flow));
			
			if (flow.isEnabled()) {
				eventQueue.add(new AddFlowEvent(flow, getFlowInstanceStreamerForFlow(flow)));
			}
			
			checkQueueState(false);
		}
	}

	public void deleteFlows(List<Flow> beans) {

		for(Flow flow : beans){

			eventQueue.add(new DeleteFlowEvent(flow));
			checkQueueState(false);
		}
	}

	public void addFlowInstances(List<FlowInstance> beans) {

		for(FlowInstance flowInstance : beans){

			eventQueue.add(new AddUpdateFlowInstanceEvent(flowInstance));
			checkQueueState(false);
		}
	}

	public void updateFlowInstances(List<FlowInstance> beans) {

		for(FlowInstance flowInstance : beans){

			eventQueue.add(new AddUpdateFlowInstanceEvent(flowInstance));
			checkQueueState(false);
		}
	}

	public void deleteFlowInstances(List<FlowInstance> beans) {

		for(FlowInstance flowInstance : beans){

			eventQueue.add(new DeleteFlowInstanceEvent(flowInstance));
			checkQueueState(false);
		}
	}

	public synchronized void checkQueueState(boolean commit) {

		if (systemInterface.getSystemStatus() != SystemStatus.STARTED) {

			return;
		}

		if (threadPoolExecutor.getExecutingThreadCount() == 0 && threadPoolExecutor.getQueue().isEmpty()) {

			try {
				if (commit) {

					if (logIndexing) {
						log.info("Committing index changes from last event.");
					}

					this.indexWriter.commit();
					this.indexReader = DirectoryReader.open(index);
					this.searcher = new IndexSearcher(indexReader);
				}

			} catch (IOException e) {
				log.error("Unable to commit index", e);
			}

			while (true) {

				QueuedIndexEvent nextEvent = eventQueue.peek();

				if (nextEvent == null) {

					log.debug("No queued search events found, thread pool idle.");
					return;
				}
				
				if (logIndexing) {
					log.info("Processing " + nextEvent);
				}

				int tasks = nextEvent.queueTasks(threadPoolExecutor, this);
				
				if (!nextEvent.hasRemainingTasks()) {
					eventQueue.remove();
				}

				if (tasks > 0) {

					if (logIndexing) {
						log.info("Queued " + tasks + " tasks for " + nextEvent);
					}

					return;
				}
			}
		}
	}

	public boolean isValidState() {

		return systemInterface.getSystemStatus() == SystemStatus.STARTED;
	}

	public void indexFlowInstance(FlowInstance flowInstance, Flow flow, FlowFamily flowFamily) {
		
		log.debug("Indexing flow instance " + flowInstance);
		
		try {
			Document doc = new Document();
			
			doc.add(new StringField(ID_FIELD, flowInstance.getFlowInstanceID().toString(), Field.Store.YES));
			doc.add(new StringField(FLOW_ID_FIELD, flow.getFlowID().toString(), Field.Store.YES));
			doc.add(new StringField(FLOW_FAMILY_ID_FIELD, flowFamily.getFlowFamilyID().toString(), Field.Store.YES));
			doc.add(new TextField(FLOW_NAME_FIELD, flow.getName(), Field.Store.YES));
			doc.add(new TextField(FIRST_SUBMITTED_FIELD, DateUtils.DATE_TIME_FORMATTER.format(flowInstance.getFirstSubmitted()), Field.Store.YES));
			doc.add(new TextField(STATUS_NAME_FIELD, flowInstance.getStatus().getName(), Field.Store.YES));
			
			AttributeHandler attributeHandler = flowInstance.getAttributeHandler();
			
			String citizenIdentifier = attributeHandler.getString(CITIZEN_IDENTIFIER);
			
			if (citizenIdentifier == null && flowInstance.getPoster() != null) {
				
				citizenIdentifier = flowInstance.getPoster().getAttributeHandler().getString(CITIZEN_IDENTIFIER);
			}
			
			if (citizenIdentifier != null) {
				
				doc.add(new TextField(CITIZEN_IDENTIFIER, citizenIdentifier, Field.Store.NO));
			}
			
			String childCitizenIdentifier = attributeHandler.getString(CHILD_CITIZEN_IDENTIFIER);
			
			if (childCitizenIdentifier != null) {
				
				doc.add(new TextField(CHILD_CITIZEN_IDENTIFIER, childCitizenIdentifier, Field.Store.NO));
			}
			
			String externalID = attributeHandler.getString(Constants.FLOW_INSTANCE_EXTERNAL_ID_ATTRIBUTE);
			
			if (externalID != null) {
				
				doc.add(new TextField(EXTERNAL_ID_FIELD, externalID, Field.Store.NO));
			}
			
			String organizationNumber = attributeHandler.getString(ORGANIZATION_NUMBER);
			
			if (organizationNumber != null) {
				
				doc.add(new TextField(ORGANIZATION_NUMBER, organizationNumber, Field.Store.NO));
			}
			
			String organizationName = attributeHandler.getString(Constants.FLOW_INSTANCE_ORGANIZATION_NAME_ATTRIBUTE);
			
			if (organizationName != null) {
				
				doc.add(new TextField(POSTER_FIELD, organizationName, Field.Store.NO));
			}
			
			if (flowInstance.getPoster() != null) {
				
				doc.add(new TextField(POSTER_FIELD, flowInstance.getPoster().getFirstname() + " " + flowInstance.getPoster().getLastname(), Field.Store.NO));
				
			} else {
				
				String firstname = attributeHandler.getString(Constants.FLOW_INSTANCE_FIRSTNAME_ATTRIBUTE);
				String lastname = attributeHandler.getString(Constants.FLOW_INSTANCE_LASTNAME_ATTRIBUTE);
				
				if (firstname != null || lastname != null) {
					
					doc.add(new TextField(POSTER_FIELD, firstname + " " + lastname, Field.Store.NO));
				}
			}
			
			if (flowInstance.getOwners() != null) {
				
				for (User owner : flowInstance.getOwners()) {
					
					doc.add(new TextField(OWNERS_FIELD, owner.getFirstname() + " " + owner.getLastname(), Field.Store.NO));
					
					if (owner.getAttributeHandler().isSet("citizenIdentifier")) {
						
						doc.add(new TextField(CITIZEN_IDENTIFIER, owner.getAttributeHandler().getString("citizenIdentifier"), Field.Store.NO));
					}
				}
			}
			
			if (flowInstance.getManagers() != null) {
				
				for (User manager : flowInstance.getManagers()) {
					
					doc.add(new StoredField(MANAGER_USER_FIELD, manager.getUserID()));
					doc.add(new TextField(MANAGER_FIELD, manager.getFirstname() + " " + manager.getLastname(), Field.Store.NO));
				}
			}
			
			if (flowInstance.getManagerGroups() != null) {
				
				for (Group managerGroup : flowInstance.getManagerGroups()) {
					
					doc.add(new StoredField(MANAGER_GROUP_FIELD, managerGroup.getGroupID()));
				}
			}
			
			if (flowInstance.getInternalMessages() != null) {
				
				for (InternalMessage message : flowInstance.getInternalMessages()) {
					
					doc.add(new TextField(INTERNAL_MESSAGES, message.getMessage(), Field.Store.NO));
				}
			}
			
			if (flowInstance.getExternalMessages() != null) {
				
				for (ExternalMessage message : flowInstance.getExternalMessages()) {
					
					doc.add(new TextField(EXTERNAL_MESSAGES, message.getMessage(), Field.Store.NO));
				}
			}
			
			if (flowFamily.getManagerGroups() != null) {
				
				for (FlowFamilyManagerGroup managerGroup : flowFamily.getManagerGroups()) {
					
					if (managerGroup.isRestricted()) {
						doc.add(new StoredField(ALLOWED_RESTRICTED_GROUP_FIELD, managerGroup.getGroupID()));
						
					} else {
						doc.add(new StoredField(ALLOWED_FULL_GROUP_FIELD, managerGroup.getGroupID()));
					}
				}
			}
			
			List<FlowFamilyManager> activeManagers = flowFamily.getActiveManagers();
			
			if (activeManagers != null) {
				
				for (FlowFamilyManager manager : activeManagers) {
					
					if (manager.isRestricted()) {
						doc.add(new StoredField(ALLOWED_RESTRICTED_USER_FIELD, manager.getUserID()));
						
					} else {
						doc.add(new StoredField(ALLOWED_FULL_USER_FIELD, manager.getUserID()));
					}
				}
			}
			
			if(flowInstance.getManagerDescription() != null) {
				
				doc.add(new TextField(MANAGER_DESCRIPTION, flowInstance.getManagerDescription(), Field.Store.YES));
			}
			
			addAdditionalFields(doc, flowInstance, flowFamily);
			
			indexWriter.addDocument(doc);
			
		} catch (Exception e) {
			
			log.error("Error indexing flow instance " + flowInstance, e);
		}
	}

	protected void addAdditionalFields(Document doc, FlowInstance flowInstance, FlowFamily flowFamily) {
		
		//No extra fields to add here.
	}
	
	public void deleteDocument(FlowInstance flowInstance) {

		log.debug("Removing flow instance " + flowInstance + " from index.");

		BooleanQuery.Builder query = new BooleanQuery.Builder();

		query.add(new TermQuery(new Term(ID_FIELD, flowInstance.getFlowInstanceID().toString())), Occur.MUST);

		try{
			indexWriter.deleteDocuments(query.build());

		}catch(Exception e){

			log.error("Error removing flow instance " + flowInstance + " from index", e);
		}
	}

	public void deleteDocuments(Flow flow) {

		log.debug("Removing flow instances belonging to flow " + flow + " from index.");

		BooleanQuery.Builder query = new BooleanQuery.Builder();

		query.add(new TermQuery(new Term(FLOW_ID_FIELD, flow.getFlowID().toString())), Occur.MUST);

		try{
			indexWriter.deleteDocuments(query.build());

		}catch(Exception e){

			log.error("Error removing flow instances belonging to flow " + flow + " from index", e);
		}
	}

	public void deleteDocuments(FlowFamily flowFamily) {

		log.debug("Removing flow instances belonging to flow family " + flowFamily + " from index.");

		BooleanQuery.Builder query = new BooleanQuery.Builder();

		query.add(new TermQuery(new Term(FLOW_FAMILY_ID_FIELD, flowFamily.getFlowFamilyID().toString())), Occur.MUST);

		try{
			indexWriter.deleteDocuments(query.build());

		}catch(Exception e){

			log.error("Error removing flow instances belonging to flow family " + flowFamily + " from index", e);
		}
	}

	public FlowInstance getFlowInstance(Integer flowInstanceID) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.FLOW_RELATION,  FlowInstance.STATUS_RELATION, FlowInstance.ATTRIBUTES_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, FlowInstance.OWNERS_RELATION);

		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));

		return daoFactory.getFlowInstanceDAO().get(query);
	}

	public QueryResultsStreamer<FlowInstance, Integer> getFlowInstanceStreamerForFlow(Flow flow) {
		
		HighLevelQuery<FlowInstance> query = new HighLevelQuery<>(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.FLOW_RELATION,  FlowInstance.STATUS_RELATION, FlowInstance.ATTRIBUTES_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, FlowInstance.OWNERS_RELATION);
		query.addCachedRelations(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION);
		
		query.addExcludedField(ExternalMessageAttachment.DATA_FIELD);
		query.addExcludedField(InternalMessageAttachment.DATA_FIELD);
		
		query.addParameter(flowInstanceFlowParamFactory.getParameter(flow));
		query.addParameter(flowInstanceFirstSubmittedParamFactory.getIsNotNullParameter());
		
		query.setRowLimiter(new MySQLRowLimiter(STREAMER_CHUNK_SIZE));
		
		return new QueryResultsStreamer<FlowInstance, Integer>(daoFactory.getFlowInstanceDAO(), FlowInstance.ID_FIELD, Integer.class, Order.ASC, query);
	}
	
	public QueryResultsStreamer<FlowInstance, Integer> getFlowInstanceStreamerForFlowFamily(FlowFamily flowFamily) throws SQLException {
		
		HighLevelQuery<Flow> getFlowsQueries = new HighLevelQuery<>();
		getFlowsQueries.addParameter(flowFlowFamilyParamFactory.getParameter(flowFamily));
		getFlowsQueries.addParameter(flowEnabledParamFactory.getParameter(true));
		
		List<Flow> flows = daoFactory.getFlowDAO().getAll(getFlowsQueries);
		
		if (flows == null) {
			return null;
		}

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<>(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.FLOW_RELATION,  FlowInstance.STATUS_RELATION, FlowInstance.ATTRIBUTES_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, FlowInstance.OWNERS_RELATION);
		query.addCachedRelations(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION);
		
		query.addExcludedField(ExternalMessageAttachment.DATA_FIELD);
		query.addExcludedField(InternalMessageAttachment.DATA_FIELD);
		
		query.addParameter(flowInstanceFlowParamFactory.getWhereInParameter(flows));
		query.addParameter(flowInstanceFirstSubmittedParamFactory.getIsNotNullParameter());
		
		query.setRowLimiter(new MySQLRowLimiter(STREAMER_CHUNK_SIZE));
		
		return new QueryResultsStreamer<FlowInstance, Integer>(daoFactory.getFlowInstanceDAO(), FlowInstance.ID_FIELD, Integer.class, Order.ASC, query);
	}

	public QueryResultsStreamer<FlowInstance, Integer> getAllFlowInstancesStreamer() {
		
		HighLevelQuery<FlowInstance> query = new HighLevelQuery<>(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.FLOW_RELATION,  FlowInstance.STATUS_RELATION, FlowInstance.ATTRIBUTES_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, FlowInstance.OWNERS_RELATION);
		query.addCachedRelations(FlowInstance.MANAGERS_RELATION, FlowInstance.MANAGER_GROUPS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.FLOW_RELATION, Flow.FLOW_FAMILY_RELATION);
		
		query.addExcludedField(ExternalMessageAttachment.DATA_FIELD);
		query.addExcludedField(InternalMessageAttachment.DATA_FIELD);
		
		query.addParameter(flowInstanceFirstSubmittedParamFactory.getIsNotNullParameter());
		query.addRelationParameter(Flow.class, flowEnabledParamFactory.getParameter(true));
		
		query.setRowLimiter(new MySQLRowLimiter(STREAMER_CHUNK_SIZE));
		
		return new QueryResultsStreamer<FlowInstance, Integer>(daoFactory.getFlowInstanceDAO(), FlowInstance.ID_FIELD, Integer.class, Order.ASC, query);
	}

	public boolean isLogIndexing() {
	
		return logIndexing;
	}
	
	public void setLogIndexing(boolean logIndexing) {
	
		this.logIndexing = logIndexing;
	}
	
	protected void appendFilter(BooleanQuery.Builder builder, User user) {
		
		BooleanQuery.Builder familyFullManagerQuery = new BooleanQuery.Builder();
		BooleanQuery.Builder familyRestrictedManagerQuery = new BooleanQuery.Builder();
		familyRestrictedManagerQuery.setMinimumNumberShouldMatch(1);
		
		if (user.getUserID() != null) {
			
			familyFullManagerQuery.add(IntPoint.newRangeQuery(ALLOWED_FULL_USER_FIELD, user.getUserID(), user.getUserID()), Occur.SHOULD);
			familyRestrictedManagerQuery.add(IntPoint.newRangeQuery(ALLOWED_RESTRICTED_USER_FIELD, user.getUserID(), user.getUserID()), Occur.SHOULD);
		}
		
		if (user.getGroups() != null) {
			
			for (Group group : user.getGroups()) {
				
				if (group.isEnabled() && group.getGroupID() != null) {
					
					familyFullManagerQuery.add(IntPoint.newRangeQuery(ALLOWED_FULL_GROUP_FIELD, group.getGroupID(), group.getGroupID()), Occur.SHOULD);
					familyRestrictedManagerQuery.add(IntPoint.newRangeQuery(ALLOWED_RESTRICTED_GROUP_FIELD, group.getGroupID(), group.getGroupID()), Occur.SHOULD);
				}
			}
		}
		
		BooleanQuery.Builder instanceManagerQuery = new BooleanQuery.Builder();
		instanceManagerQuery.add(IntPoint.newRangeQuery(MANAGER_USER_FIELD, user.getUserID(), user.getUserID()), Occur.SHOULD);
		
		if (user.getGroups() != null) {
			
			for (Group group : user.getGroups()) {
				
				if (group.isEnabled() && group.getGroupID() != null) {
					
					instanceManagerQuery.add(IntPoint.newRangeQuery(MANAGER_GROUP_FIELD, group.getGroupID(), group.getGroupID()), Occur.SHOULD);
				}
			}
		}
		
		familyRestrictedManagerQuery.add(instanceManagerQuery.build(), Occur.MUST);
		
		builder.add(familyFullManagerQuery.build(), Occur.SHOULD);
		builder.add(familyRestrictedManagerQuery.build(), Occur.SHOULD);
	}
}
