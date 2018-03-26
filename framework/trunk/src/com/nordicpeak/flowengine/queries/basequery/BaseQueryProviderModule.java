package com.nordicpeak.flowengine.queries.basequery;

import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.events.EventHandler;
import se.unlogic.hierarchy.core.interfaces.instances.InstanceListener;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.ClassPathURIResolver;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLTransformer;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xsl.XSLTransformer;
import se.unlogic.standardutils.xsl.XSLVariableReader;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.FlowAdminModule;
import com.nordicpeak.flowengine.beans.PDFQueryResponse;
import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.QueryTypeDescriptor;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstance;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.PDFAttachment;
import com.nordicpeak.flowengine.interfaces.PDFResourceProvider;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryProvider;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;


public abstract class BaseQueryProviderModule<QI extends BaseQueryInstance> extends AnnotatedForegroundModule implements QueryProvider, BaseQueryInstanceCallback<QI>, InstanceListener<QueryHandler>, BaseQueryCRUDCallback {

	@XSLVariable(prefix = "java.")
	protected String queryTypeName = "This variable should be set by your stylesheet";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Query XSL stylesheet", description = "The path in classpath relative from this class to the XSL stylesheet used to transform the HTML of queries", required = true)
	protected String queryStyleSheet;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF XSL stylesheet", description = "The path in classpath relative from this class to the XSL stylesheet used to transform the XHTML for PDF output of queries", required = true)
	protected String pdfStyleSheet;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Include debug data",description="Controls whether or not debug data should be included in the query response objects")
	protected boolean includeDebugData = false;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Editor CSS", description = "Path to the desired CSS stylesheet for FCKEditor (relative from the contextpath)", required = false)
	protected String cssPath;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="CKEditor connector module alias", description="The full alias of the CKEditor connector module (relative from the contextpath). Leave empty if you do not want to activate file manager for CKEditor")
	protected String ckConnectorModuleAlias;

	@ModuleSetting
	@TextFieldSettingDescriptor(name="Query type ID", description="The ID used to be used for queries of this type (this ID should not be changes if there are users with open flow instances in their session)", required=true)
	protected String queryTypeID = this.getClass().getName();
	
	protected String oldQueryTypeID;
	
	protected FlowAdminModule flowAdminModule;
	
	protected QueryTypeDescriptor queryTypeDescriptor;

	protected XSLTransformer queryTransformer;

	protected XSLTransformer pdfTransformer;

	protected List<ScriptTag> queryScripts;
	protected List<LinkTag> queryLinks;

	@Override
	protected synchronized void moduleConfigured() throws Exception {

		super.moduleConfigured();
		
		createQueryTransformer();
		
		if(queryTypeDescriptor == null){
			
			queryTypeDescriptor = new QueryTypeDescriptor(queryTypeID, queryTypeName, getQueryInstanceClass());
			
			//Rename legacy integer based queryTypeID's on module startup
			oldQueryTypeID = this.moduleDescriptor.getModuleID().toString();
			
			systemInterface.getInstanceHandler().addInstanceListener(QueryHandler.class, this);
			
		}else if(!queryTypeDescriptor.getQueryTypeID().equals(queryTypeID)){
			
			//queryTypeID has been changed since the module was started
			
			oldQueryTypeID = queryTypeDescriptor.getQueryTypeID();
			
			QueryHandler queryHandler = systemInterface.getInstanceHandler().getInstance(QueryHandler.class);

			if(queryHandler != null){

				queryHandler.removeQueryProvider(queryTypeDescriptor, this);
			}
			
			queryTypeDescriptor = new QueryTypeDescriptor(queryTypeID, queryTypeName, getQueryInstanceClass());
			
			if(queryHandler != null){

				addQueryProvider(queryHandler);
			}
		}
		
		checkOldQueryTypeID();
	}

	protected abstract Class<QI> getQueryInstanceClass();

	protected void checkOldQueryTypeID() throws SQLException {

		if(flowAdminModule != null && oldQueryTypeID != null && flowAdminModule.getQueryCount(oldQueryTypeID) > 0){
			
			QueryHandler queryHandler = systemInterface.getInstanceHandler().getInstance(QueryHandler.class);

			if (queryHandler == null || (queryHandler != null && queryHandler.getQueryProvider(oldQueryTypeID) == null)) {

				flowAdminModule.changeQueryTypeID(oldQueryTypeID, queryTypeDescriptor.getQueryTypeID());

			} else {

				log.info("Another query provider is still registered for '" + oldQueryTypeID + "', avoiding query type rename.");
			}
			
			this.oldQueryTypeID = null;
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstanceListener(QueryHandler.class, this);

		QueryHandler queryHandler = systemInterface.getInstanceHandler().getInstance(QueryHandler.class);

		if(queryHandler != null){

			queryHandler.removeQueryProvider(queryTypeDescriptor, this);
		}

		this.flowAdminModule = null;
		
		super.unload();
	}

	public void createQueryTransformer(){

		if(queryStyleSheet == null){

			queryTransformer = null;

		}else{

			URL styleSheetURL = this.getClass().getResource(queryStyleSheet);

			if(styleSheetURL != null){

				try {
					queryTransformer = systemInterface.getStandaloneXSLTransformerFactory().getXSLTransformer(styleSheetURL.toURI(),ClassPathURIResolver.getInstance(), true);

					parseQueryXSLStyleSheet(styleSheetURL);

					log.info("Succesfully parsed query stylesheet " + queryStyleSheet);

				} catch (Exception e){

					log.error("Unable to cache query style sheet " + queryStyleSheet,e);

					queryTransformer = null;
				}

			}else{
				log.error("Unable to cache query style sheet. Resource " + queryStyleSheet + " not found");
			}
		}

		if(pdfStyleSheet == null){

			pdfTransformer = null;

		}else{

			URL styleSheetURL = this.getClass().getResource(pdfStyleSheet);

			if(styleSheetURL != null){

				try {
					pdfTransformer = systemInterface.getStandaloneXSLTransformerFactory().getXSLTransformer(styleSheetURL.toURI(),ClassPathURIResolver.getInstance(), true);

					log.info("Succesfully parsed PDF stylesheet " + pdfStyleSheet);

				} catch (Exception e){

					log.error("Unable to cache PDF style sheet " + pdfStyleSheet,e);

					pdfTransformer = null;
				}

			}else{
				log.error("Unable to cache PDF style sheet. Resource " + pdfStyleSheet + " not found");
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void parseQueryXSLStyleSheet(URL styleSheetURL){

		try {
			XSLVariableReader variableReader = new XSLVariableReader(styleSheetURL.toURI());

			List<ScriptTag> globalScripts = ModuleUtils.getGlobalScripts(variableReader);
			List<ScriptTag> localScripts = ModuleUtils.getScripts(variableReader, sectionInterface, "f", moduleDescriptor);

			this.queryScripts = CollectionUtils.combine(globalScripts,localScripts);

			List<LinkTag> globalLinks = ModuleUtils.getGlobalLinks(variableReader);
			List<LinkTag> localLinks = ModuleUtils.getLinks(variableReader, sectionInterface, "f", moduleDescriptor);
			
			this.queryLinks = CollectionUtils.combine(globalLinks, localLinks);

		} catch (Exception e) {

			log.error("Unable to get scripts and links from query style sheet " + queryStyleSheet,e);

			this.queryScripts = null;
			this.queryLinks = null;
		}
	}

	@Override
	public QueryResponse getShowHTML(QI queryInstance, HttpServletRequest req, User user, User poster, String updateURL, String queryRequestURL, AttributeHandler attributeHandler) throws TransformerConfigurationException, TransformerException {

		Document doc = createDocument(req, poster);

		Element showQueryValuesElement = doc.createElement("ShowQueryValues");
		doc.getDocumentElement().appendChild(showQueryValuesElement);

		XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "updateURL", updateURL);
		XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "queryRequestURL", queryRequestURL);

		appendQueryInstance(queryInstance, doc, showQueryValuesElement, attributeHandler);

		return createQueryResponse(doc, queryInstance.getQueryInstanceDescriptor().getQueryDescriptor());
	}

	@Override
	public QueryResponse getFormHTML(QI queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, RequestMetadata requestMetadata, AttributeHandler attributeHandler) throws TransformerConfigurationException, TransformerException {

		Document doc = createDocument(req, poster);

		Element showQueryFormElement = doc.createElement("ShowQueryForm");
		doc.getDocumentElement().appendChild(showQueryFormElement);

		XMLUtils.appendNewCDATAElement(doc, showQueryFormElement, "queryRequestURL", queryRequestURL);
		
		appendQueryInstance(queryInstance, doc, showQueryFormElement, attributeHandler);
		
		if (!CollectionUtils.isEmpty(validationErrors) && req.getMethod().equalsIgnoreCase("POST")) {

			showQueryFormElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		if (enableAjaxPosting) {
			
			showQueryFormElement.appendChild(doc.createElement("EnableAjaxPosting"));
		}

		XMLUtils.append(doc, showQueryFormElement, "ValidationErrors", validationErrors);
		
		showQueryFormElement.appendChild(requestMetadata.toXML(doc));

		return createQueryResponse(doc, queryInstance.getQueryInstanceDescriptor().getQueryDescriptor());
	}

	@Override
	public PDFQueryResponse getPDFContent(QI queryInstance, AttributeHandler attributeHandler) throws Throwable {

		if(pdfTransformer == null){
			
			throw new ModuleConfigurationException("No PDF style sheet set for module " + moduleDescriptor);
		}
		
		Document doc = createDocument();

		Element showQueryValuesElement = doc.createElement("ShowQueryValues");
		doc.getDocumentElement().appendChild(showQueryValuesElement);

		appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);
		
		if(this.includeDebugData){

			return new PDFQueryResponse(transformQuery(doc, pdfTransformer, true), doc, queryInstance.getQueryInstanceDescriptor().getQueryDescriptor(), getPDFResourceProvider(queryInstance), getPDFAttachments(queryInstance));
		}

		return new PDFQueryResponse(transformQuery(doc, pdfTransformer, true), null, queryInstance.getQueryInstanceDescriptor().getQueryDescriptor(), getPDFResourceProvider(queryInstance), getPDFAttachments(queryInstance));
	}

	protected void appendQueryInstance(QI queryInstance, Document doc, Element targetElement, AttributeHandler attributeHandler) {

		if(queryInstance.getQuery().hasDescriptionAttributeTags() || queryInstance.getQuery().hasHelpTextAttributeTags()){
			
			XMLGeneratorDocument generatorDocument = new XMLGeneratorDocument(doc);
			
			if(queryInstance.getQuery().hasDescriptionAttributeTags()){
				
				generatorDocument.addIgnoredField(BaseQuery.DESCRIPTION_FIELD);
				generatorDocument.addAssignableElementableListener(BaseQuery.class, new QueryDescriptionListener(attributeHandler));
			}
			
			if(queryInstance.getQuery().hasHelpTextAttributeTags()){
				
				generatorDocument.addIgnoredField(BaseQuery.HELP_TEXT_FIELD);
				generatorDocument.addAssignableElementableListener(BaseQuery.class, new QueryHelpTextListener(attributeHandler));
			}
			
			targetElement.appendChild(queryInstance.toXML(generatorDocument));
			
		}else{
			
			targetElement.appendChild(queryInstance.toXML(doc));
		}
	}
	
	protected List<PDFAttachment> getPDFAttachments(QI queryInstance) {

		return null;
	}

	protected PDFResourceProvider getPDFResourceProvider(QI queryInstance) {

		return null;
	}

	protected void appendPDFData(Document doc, Element showQueryValuesElement, QI queryInstance, AttributeHandler attributeHandler) {

		appendQueryInstance(queryInstance, doc, showQueryValuesElement, attributeHandler);
	}

	protected QueryResponse createQueryResponse(Document doc, ImmutableQueryDescriptor queryDescriptor) throws TransformerConfigurationException, TransformerException {

		if(this.includeDebugData){

			return new QueryResponse(transformQuery(doc, queryTransformer, false), doc, queryScripts, queryLinks, queryDescriptor);
		}

		return new QueryResponse(transformQuery(doc, queryTransformer, false), queryScripts, queryLinks, queryDescriptor);
	}

	protected String transformQuery(Document doc, XSLTransformer xslTransformer, boolean pdf) throws TransformerConfigurationException, TransformerException {

		StringWriter stringWriter = new StringWriter();

		if(pdf){

			XMLTransformer.transformToWriter(xslTransformer.getTransformer(), doc, stringWriter, "UTF-8", "1.1");
			
		}else{
			
			XMLTransformer.transformToWriter(xslTransformer.getTransformer(), doc, stringWriter, systemInterface.getEncoding());
		}

		return stringWriter.toString();
	}

	public Document createDocument() {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.queryTypeDescriptor.toXML(doc));
		doc.appendChild(document);
		return doc;
	}

	public Document createDocument(HttpServletRequest req, User poster) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.queryTypeDescriptor.toXML(doc));
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req));
		XMLUtils.appendNewElement(doc, document, "fullAlias", this.getFullAlias());
		XMLUtils.appendNewElement(doc, document, "useCKEditorForDescription", true);
		XMLUtils.appendNewElement(doc, document, "ckConnectorModuleAlias", ckConnectorModuleAlias);

		doc.appendChild(document);
		return doc;
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		XMLUtils.appendNewElement(doc, documentElement, "cssPath", cssPath);
		XMLUtils.appendNewElement(doc, documentElement, "useCKEditorForDescription", true);
		XMLUtils.appendNewElement(doc, documentElement, "ckConnectorModuleAlias", ckConnectorModuleAlias);

		doc.appendChild(documentElement);
		return doc;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception, Throwable {

		throw new URINotFoundException(uriParser);
	}

	@Override
	public QueryTypeDescriptor getQueryType() {

		return queryTypeDescriptor;
	}

	@Override
	public synchronized <InstanceType extends QueryHandler> void instanceAdded(Class<QueryHandler> key, InstanceType instance) {

		addQueryProvider(instance);
	}

	@Override
	public synchronized <InstanceType extends QueryHandler> void instanceRemoved(Class<QueryHandler> key, InstanceType instance) {

		instance.removeQueryProvider(queryTypeDescriptor, this);
	}
	
	protected void addQueryProvider(QueryHandler queryHandler){
		
		if(!queryHandler.addQueryProvider(this)){
			
			log.error("Unable to add query provider " + this + " to query handler, a query provider with ID " + queryTypeID + " is already registered");
		}
	}
	
	@Override
	public ImmutableQueryInstance getImmutableQueryInstance(MutableQueryInstanceDescriptor descriptor, HttpServletRequest req, InstanceMetadata instanceMetadata) throws Throwable {

		return getQueryInstance(descriptor, null, req, null, null, instanceMetadata);
	}

	@Override
	public FlowAdminModule getFlowAdminModule() {
		return flowAdminModule;
	}

	@Override
	public EventHandler getEventHandler() {

		return systemInterface.getEventHandler();
	}

	@Override
	public QueryRequestProcessor getQueryRequestProcessor(QI queryInstance, HttpServletRequest req, User user) throws Exception {

		return null;
	}
	
	@InstanceManagerDependency(required=true)
	public synchronized void setFlowAdminModule(FlowAdminModule flowAdminModule) throws SQLException{
		
		this.flowAdminModule = flowAdminModule;
		
		checkOldQueryTypeID();
	}
	
	//This method is only use for CRUD's
	@Override
	public String getAbsoluteFileURL(URIParser uriParser, Object bean) {

		if(ckConnectorModuleAlias != null){
			
			return uriParser.getContextPath() + ckConnectorModuleAlias;
		}

		return null;
	}
	
	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		throw new RuntimeException("Query import not supported for query type " + queryTypeDescriptor);
	}

	
	public String getQueryTypeName() {
	
		return queryTypeName;
	}
}
