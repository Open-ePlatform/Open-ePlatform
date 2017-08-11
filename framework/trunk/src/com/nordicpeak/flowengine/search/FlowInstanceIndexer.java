package com.nordicpeak.flowengine.search;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
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
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.HTTPUtils;

import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.beans.ExternalMessage;
import com.nordicpeak.flowengine.beans.Flow;
import com.nordicpeak.flowengine.beans.FlowFamily;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.InternalMessage;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.search.events.AddFlowEvent;
import com.nordicpeak.flowengine.search.events.AddFlowFamilyEvent;
import com.nordicpeak.flowengine.search.events.AddUpdateFlowInstanceEvent;
import com.nordicpeak.flowengine.search.events.DeleteFlowEvent;
import com.nordicpeak.flowengine.search.events.DeleteFlowFamilyEvent;
import com.nordicpeak.flowengine.search.events.DeleteFlowInstanceEvent;
import com.nordicpeak.flowengine.search.events.QueuedIndexEvent;

public class FlowInstanceIndexer {

	private static final String ID_FIELD = "id";
	private static final String FLOW_ID_FIELD = "flowID";
	private static final String FLOW_NAME_FIELD = "name";
	private static final String FLOW_FAMILY_ID_FIELD = "familyID";
	private static final String FIRST_SUBMITTED_FIELD = "firstSubmitted";
	private static final String STATUS_NAME_FIELD = "status";
	private static final String POSTER_FIELD = "poster";
	private static final String OWNERS_FIELD = "owners";
	private static final String MANAGER_FIELD = "manager";
	private static final String CITIZEN_IDENTIFIER = "citizenIdentifier";
	private static final String ORGANIZATION_NUMBER = "organizationNumber";
	private static final String INTERNAL_MESSAGES = "internalMessages";
	private static final String EXTERNAL_MESSAGES = "externalMessages";
	protected static final String ALLOWED_USER_FIELD = "allowedUser";
	protected static final String ALLOWED_GROUP_FIELD = "allowedGroup";
	
	private static final String[] SEARCH_FIELDS = new String[] { ID_FIELD, POSTER_FIELD, OWNERS_FIELD, MANAGER_FIELD, FLOW_NAME_FIELD, STATUS_NAME_FIELD, FIRST_SUBMITTED_FIELD, CITIZEN_IDENTIFIER, ORGANIZATION_NUMBER, INTERNAL_MESSAGES, EXTERNAL_MESSAGES};

	protected Logger log = Logger.getLogger(this.getClass());

	private final CaseInsensitiveWhitespaceAnalyzer analyzer = new CaseInsensitiveWhitespaceAnalyzer(Version.LUCENE_44);
	private final Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;

	protected final SystemInterface systemInterface;
	protected final FlowEngineDAOFactory daoFactory;
	protected final QueryParameterFactory<FlowFamily, Integer> flowFamilyIDParamFactory;
	protected final QueryParameterFactory<Flow, Boolean> flowEnabledParamFactory;
	protected final QueryParameterFactory<Flow, Integer> flowIDParamFactory;
	protected final QueryParameterFactory<FlowInstance, Integer> flowInstanceIDParamFactory;

	protected int maxHitCount;

	private boolean logIndexing = true;
	
	private LinkedBlockingQueue<QueuedIndexEvent> eventQueue = new LinkedBlockingQueue<QueuedIndexEvent>();

	private CallbackThreadPoolExecutor threadPoolExecutor;

	public FlowInstanceIndexer(FlowEngineDAOFactory daoFactory, int maxHitCount, SystemInterface systemInterface) throws IOException {

		super();
		this.daoFactory = daoFactory;
		this.maxHitCount = maxHitCount;
		this.systemInterface = systemInterface;

		flowFamilyIDParamFactory = daoFactory.getFlowFamilyDAO().getParamFactory("flowFamilyID", Integer.class);

		flowIDParamFactory = daoFactory.getFlowDAO().getParamFactory("flowID", Integer.class);
		flowEnabledParamFactory = daoFactory.getFlowDAO().getParamFactory("enabled", boolean.class);

		flowInstanceIDParamFactory = daoFactory.getFlowInstanceDAO().getParamFactory("flowInstanceID", Integer.class);

		indexWriter = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_44, analyzer));

		int availableProcessors = ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors();

		threadPoolExecutor = new CallbackThreadPoolExecutor(availableProcessors, availableProcessors, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), this);
	}

	public void cacheFlowInstances() {

		List<FlowFamily> families;

		try{
			families = getFlowFamilies();

		}catch(SQLException e){

			log.error("Error gettings enabled flow families from DB", e);

			return;
		}

		if(families != null){

			for(FlowFamily flowFamily : families){

				eventQueue.add(new AddFlowFamilyEvent(flowFamily));
				checkQueueState(false);
			}
		}
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
	
	public void search(HttpServletRequest req, HttpServletResponse res, User user, boolean checkAccess) throws IOException {

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

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, SEARCH_FIELDS, analyzer);

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
			
			Filter filter = getFilter(user);
			
			results = searcher.search(query, filter, maxHitCount);
			
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

		for(FlowFamily flowFamily : beans){

			eventQueue.add(new AddFlowFamilyEvent(flowFamily));
			checkQueueState(false);
		}
	}

	public void updateFlowFamilies(List<FlowFamily> beans) {

		for(FlowFamily flowFamily : beans){

			eventQueue.add(new DeleteFlowFamilyEvent(flowFamily));
			eventQueue.add(new AddFlowFamilyEvent(flowFamily));
			checkQueueState(false);
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

			eventQueue.add(new AddFlowEvent(flow));
			checkQueueState(false);
		}
	}

	public void updateFlows(List<Flow> beans) {

		for(Flow flow : beans){

			eventQueue.add(new DeleteFlowEvent(flow));
			eventQueue.add(new AddFlowEvent(flow));
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

					if(logIndexing){
						log.info("Committing index changes from last event.");
					}
					
					this.indexWriter.commit();
					this.indexReader = DirectoryReader.open(index);
					this.searcher = new IndexSearcher(indexReader);
				}

			} catch (IOException e) {
				log.error("Unable to commit index", e);
			}

			while(true){

				QueuedIndexEvent nextEvent = eventQueue.poll();

				if (nextEvent == null) {

					log.debug("No queued search events found, thread pool idle.");
					return;
				}
				if(logIndexing){
					log.info("Processing " + nextEvent);
				}

				int tasks = nextEvent.queueTasks(threadPoolExecutor, this);

				if(tasks > 0){

					return;
				}
			}
		}
	}

	public boolean isValidState() {

		return systemInterface.getSystemStatus() == SystemStatus.STARTED;
	}

	public void indexFlowInstance(FlowInstance flowInstance, Flow flow, FlowFamily flowFamily){

		log.debug("Indexing flow instance " + flowInstance);

		try{
			Document doc = new Document();

			doc.add(new StringField(ID_FIELD, flowInstance.getFlowInstanceID().toString(), Field.Store.YES));
			doc.add(new StringField(FLOW_ID_FIELD, flow.getFlowID().toString(), Field.Store.YES));
			doc.add(new StringField(FLOW_FAMILY_ID_FIELD, flowFamily.getFlowFamilyID().toString(), Field.Store.YES));
			doc.add(new TextField(FLOW_NAME_FIELD, flow.getName(), Field.Store.YES));
			doc.add(new TextField(FIRST_SUBMITTED_FIELD, DateUtils.DATE_TIME_FORMATTER.format(flowInstance.getFirstSubmitted()), Field.Store.YES));
			doc.add(new TextField(STATUS_NAME_FIELD, flowInstance.getStatus().getName(), Field.Store.YES));

			AttributeHandler attributeHandler = flowInstance.getAttributeHandler();
			
			String citizenIdentifier = attributeHandler.getString(CITIZEN_IDENTIFIER);
			
			if(citizenIdentifier == null && flowInstance.getPoster() != null){
				
				citizenIdentifier = flowInstance.getPoster().getAttributeHandler().getString(CITIZEN_IDENTIFIER);
			}
			
			if(citizenIdentifier != null){
				
				doc.add(new TextField(CITIZEN_IDENTIFIER, citizenIdentifier, Field.Store.NO));
			}
			
			String organizationNumber = attributeHandler.getString(ORGANIZATION_NUMBER);
			
			if(organizationNumber != null){
				
				doc.add(new TextField(ORGANIZATION_NUMBER, organizationNumber, Field.Store.NO));
			}
			
			String organizationName = attributeHandler.getString(Constants.FLOW_INSTANCE_ORGANIZATION_NAME_ATTRIBUTE);
			
			if(organizationName != null){
				
				doc.add(new TextField(POSTER_FIELD, organizationName, Field.Store.NO));
			}
			
			if(flowInstance.getPoster() != null){

				doc.add(new TextField(POSTER_FIELD, flowInstance.getPoster().getFirstname() + " " + flowInstance.getPoster().getLastname(), Field.Store.NO));
				
			}else{
				
				String firstname = attributeHandler.getString(Constants.FLOW_INSTANCE_FIRSTNAME_ATTRIBUTE);
				String lastname = attributeHandler.getString(Constants.FLOW_INSTANCE_LASTNAME_ATTRIBUTE);
				
				if(firstname != null || lastname != null){
					
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

			if(flowInstance.getManagers() != null){

				for(User manager : flowInstance.getManagers()){

					doc.add(new TextField(MANAGER_FIELD, manager.getFirstname() + " " + manager.getLastname(), Field.Store.NO));
				}
			}
			
			if(flowInstance.getInternalMessages() != null){
				
				for(InternalMessage message : flowInstance.getInternalMessages()){
					
					doc.add(new TextField(INTERNAL_MESSAGES, message.getMessage(), Field.Store.NO));
				}
			}

			if(flowInstance.getExternalMessages() != null){
				
				for(ExternalMessage message : flowInstance.getExternalMessages()){
					
					doc.add(new TextField(EXTERNAL_MESSAGES, message.getMessage(), Field.Store.NO));
				}
			}
			
			if(flowFamily.getManagerGroupIDs() != null){

				for(Integer groupID : flowFamily.getManagerGroupIDs()){

					doc.add(new IntField(ALLOWED_GROUP_FIELD, groupID, Field.Store.YES));
				}
			}

			if(flowFamily.getManagerUserIDs() != null){

				for(Integer userID : flowFamily.getManagerUserIDs()){

					doc.add(new IntField(ALLOWED_USER_FIELD, userID, Field.Store.YES));
				}
			}
			
			addAdditionalFields(doc, flowInstance, flowFamily);
			
			indexWriter.addDocument(doc);

		}catch(Exception e){

			log.error("Error indexing flow instance " + flowInstance, e);
		}
	}

	protected void addAdditionalFields(Document doc, FlowInstance flowInstance, FlowFamily flowFamily) {
		
		//No extra fields to add here.
	}
	
	public void deleteDocument(FlowInstance flowInstance) {

		log.debug("Removing flow instance " + flowInstance + " from index.");

		BooleanQuery query = new BooleanQuery();

		query.add(new TermQuery(new Term(ID_FIELD, flowInstance.getFlowInstanceID().toString())), Occur.MUST);

		try{
			indexWriter.deleteDocuments(query);

		}catch(Exception e){

			log.error("Error removing flow instance " + flowInstance + " from index", e);
		}
	}

	public void deleteDocuments(Flow flow) {

		log.debug("Removing flow instances belonging to flow " + flow + " from index.");

		BooleanQuery query = new BooleanQuery();

		query.add(new TermQuery(new Term(FLOW_ID_FIELD, flow.getFlowID().toString())), Occur.MUST);

		try{
			indexWriter.deleteDocuments(query);

		}catch(Exception e){

			log.error("Error removing flow instances belonging to flow " + flow + " from index", e);
		}
	}

	public void deleteDocuments(FlowFamily flowFamily) {

		log.debug("Removing flow instances belonging to flow family " + flowFamily + " from index.");

		BooleanQuery query = new BooleanQuery();

		query.add(new TermQuery(new Term(FLOW_FAMILY_ID_FIELD, flowFamily.getFlowFamilyID().toString())), Occur.MUST);

		try{
			indexWriter.deleteDocuments(query);

		}catch(Exception e){

			log.error("Error removing flow instances belonging to flow family " + flowFamily + " from index", e);
		}
	}

	public FlowInstance getFlowInstance(Integer flowInstanceID) throws SQLException {

		HighLevelQuery<FlowInstance> query = new HighLevelQuery<FlowInstance>(FlowInstance.MANAGERS_RELATION, FlowInstance.FLOW_RELATION,  FlowInstance.STATUS_RELATION, FlowInstance.ATTRIBUTES_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, FlowInstance.OWNERS_RELATION);

		query.addParameter(flowInstanceIDParamFactory.getParameter(flowInstanceID));

		return daoFactory.getFlowInstanceDAO().get(query);
	}

	public Flow getFlow(Integer flowID) throws SQLException {

		HighLevelQuery<Flow> query = new HighLevelQuery<Flow>(Flow.FLOW_INSTANCES_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.ATTRIBUTES_RELATION, Flow.FLOW_FAMILY_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, FlowInstance.OWNERS_RELATION);

		query.addParameter(flowEnabledParamFactory.getParameter(true));
		query.addParameter(flowIDParamFactory.getParameter(flowID));

		return daoFactory.getFlowDAO().get(query);
	}

	public FlowFamily getFlowFamily(Integer flowFamilyID) throws SQLException {

		HighLevelQuery<FlowFamily> query = new HighLevelQuery<FlowFamily>(FlowFamily.FLOWS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, Flow.FLOW_INSTANCES_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, FlowInstance.OWNERS_RELATION);

		query.addParameter(flowFamilyIDParamFactory.getParameter(flowFamilyID));
		query.addRelationParameter(Flow.class, flowEnabledParamFactory.getParameter(true));

		return daoFactory.getFlowFamilyDAO().get(query);
	}

	private List<FlowFamily> getFlowFamilies() throws SQLException {

		HighLevelQuery<FlowFamily> query = new HighLevelQuery<FlowFamily>(FlowFamily.FLOWS_RELATION, FlowFamily.MANAGER_GROUPS_RELATION, FlowFamily.MANAGER_USERS_RELATION, Flow.FLOW_INSTANCES_RELATION, FlowInstance.STATUS_RELATION, FlowInstance.MANAGERS_RELATION, FlowInstance.ATTRIBUTES_RELATION, FlowInstance.INTERNAL_MESSAGES_RELATION, FlowInstance.EXTERNAL_MESSAGES_RELATION, FlowInstance.OWNERS_RELATION);

		query.addRelationParameter(Flow.class, flowEnabledParamFactory.getParameter(true));

		return daoFactory.getFlowFamilyDAO().getAll(query);
	}

	
	public boolean isLogIndexing() {
	
		return logIndexing;
	}

	
	public void setLogIndexing(boolean logIndexing) {
	
		this.logIndexing = logIndexing;
	}
	
	protected Filter getFilter(User user) {

		BooleanQuery booleanQuery = new BooleanQuery();

		if(user.getUserID() != null){

			booleanQuery.add(NumericRangeQuery.newIntRange(ALLOWED_USER_FIELD, user.getUserID(), user.getUserID(), true, true), Occur.SHOULD);
		}

		if(user.getGroups() != null){

			for(Group group : user.getGroups()){

				if(group.getGroupID() != null){

					booleanQuery.add(NumericRangeQuery.newIntRange(ALLOWED_GROUP_FIELD, group.getGroupID(), group.getGroupID(), true, true), Occur.SHOULD);
				}
			}
		}

		return new QueryWrapperFilter(booleanQuery);
	}
}
