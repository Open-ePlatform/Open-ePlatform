package com.nordicpeak.flowengine.queries.checkboxpaymentquery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.datatypes.Matrix;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;
import se.unlogic.webutils.url.URLRewriter;
import se.unlogic.webutils.validation.ValidationUtils;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.populators.FreeTextAlternativePopulator;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.queries.checkboxquery.validationerrors.TooFewAlternativesSelectedValidationError;
import com.nordicpeak.flowengine.queries.checkboxquery.validationerrors.TooManyAlternativesSelectedValidationError;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativeQueryUtils;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryCallback;
import com.nordicpeak.flowengine.queries.tablequery.SummaryTableQueryCallback;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;


public class CheckboxPaymentQueryProviderModule extends BaseQueryProviderModule<CheckboxPaymentQueryInstance> implements BaseQueryCRUDCallback, FixedAlternativesQueryCallback<CheckboxPaymentQuery>, SummaryTableQueryCallback<CheckboxPaymentQuery> {

	private static final RelationQuery SAVE_QUERY_INSTANCE_RELATION_QUERY = new RelationQuery(CheckboxPaymentQueryInstance.ALTERNATIVES_RELATION);

	@XSLVariable(prefix="java.")
	private String countText = "Count";

	@XSLVariable(prefix="java.")
	private String alternativesText = "Alternative";

	private AnnotatedDAO<CheckboxPaymentQuery> queryDAO;
	private AnnotatedDAO<CheckboxPaymentQueryInstance> queryInstanceDAO;

	private CheckboxPaymentQueryCRUD queryCRUD;

	private QueryParameterFactory<CheckboxPaymentQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<CheckboxPaymentQueryInstance, Integer> queryInstanceIDParamFactory;
	private QueryParameterFactory<CheckboxPaymentQueryInstance, CheckboxPaymentQuery> queryInstanceQueryParamFactory;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, CheckboxPaymentQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(CheckboxPaymentQuery.class);
		queryInstanceDAO = daoFactory.getDAO(CheckboxPaymentQueryInstance.class);

		queryCRUD = new CheckboxPaymentQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<CheckboxPaymentQuery>(CheckboxPaymentQuery.class), "CheckboxPaymentQuery", "query", null, this);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
		queryInstanceQueryParamFactory = queryInstanceDAO.getParamFactory("query", CheckboxPaymentQuery.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		CheckboxPaymentQuery query = new CheckboxPaymentQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		CheckboxPaymentQuery query = new CheckboxPaymentQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		List<Integer> oldAlternativeIDs = FixedAlternativeQueryUtils.getAlternativeIDs(query);

		FixedAlternativeQueryUtils.clearAlternativeIDs(query.getAlternatives());

		this.queryDAO.add(query, transactionHandler, null);

		query.setAlternativeConversionMap(FixedAlternativeQueryUtils.getAlternativeConversionMap(query.getAlternatives(), oldAlternativeIDs));

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor) throws SQLException {

		CheckboxPaymentQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		CheckboxPaymentQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		CheckboxPaymentQueryInstance queryInstance = null;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new CheckboxPaymentQueryInstance();

		} else {

			queryInstance = getQueryInstance(descriptor.getQueryInstanceID());

			if (queryInstance == null) {

				return null;
			}
		}

		queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

		if(queryInstance.getQuery() == null){

			return null;
		}

		if(req != null){

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req, true);
		}

		queryInstance.getQuery().scanAttributeTags();
		
		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		queryInstance.set(descriptor);

		//If this is a new query instance copy the default values
		if(descriptor.getQueryInstanceID() == null){

			queryInstance.copyQueryValues();
		}

		return queryInstance;
	}

	private CheckboxPaymentQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<CheckboxPaymentQuery> query = new HighLevelQuery<CheckboxPaymentQuery>(CheckboxPaymentQuery.ALTERNATIVES_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private CheckboxPaymentQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<CheckboxPaymentQuery> query = new HighLevelQuery<CheckboxPaymentQuery>(CheckboxPaymentQuery.ALTERNATIVES_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private CheckboxPaymentQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<CheckboxPaymentQueryInstance> query = new HighLevelQuery<CheckboxPaymentQueryInstance>(CheckboxPaymentQueryInstance.ALTERNATIVES_RELATION, CheckboxPaymentQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(CheckboxPaymentQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		//Check if the query instance has an ID set and if the ID of the descriptor has changed
		if(queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())){

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);

		}else{

			this.queryInstanceDAO.update(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);
		}
	}

	@Override
	public void populate(CheckboxPaymentQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		List<CheckboxPaymentAlternative> availableAlternatives = queryInstance.getQuery().getAlternatives();

		if (CollectionUtils.isEmpty(availableAlternatives)) {

			//If the parent query doesn't have any alternatives then there is no population to do
			queryInstance.reset(attributeHandler);
			return;
		}

		List<CheckboxPaymentAlternative> selectedAlternatives = new ArrayList<CheckboxPaymentAlternative>(queryInstance.getQuery().getAlternatives().size());

		for (CheckboxPaymentAlternative alternative : availableAlternatives) {

			if (req.getParameter("q" + queryInstance.getQuery().getQueryID() + "_alternative" + alternative.getAlternativeID()) != null) {

				selectedAlternatives.add(alternative);
			}
		}

		int alternativesSelected = selectedAlternatives.size();

		List<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String freeTextAlternativeValue = null;
		
		if(queryInstance.getQuery().getFreeTextAlternative() != null){
			
			freeTextAlternativeValue = FreeTextAlternativePopulator.populate(queryInstance.getQuery().getQueryID(), "_freeTextAlternative", req, validationErrors);
		}

		if(freeTextAlternativeValue != null){
			
			if(!validationErrors.isEmpty() && !ValidationUtils.containsValidationErrorWithMessageKey("FreeTextAlternativeValueRequired", validationErrors)){
				
				allowPartialPopulation = false;
			}
			
			alternativesSelected++;
		}

		//If partial population is allowed and the user has not select any alternatives, skip validation
		if (allowPartialPopulation) {

			if(alternativesSelected == 0) {

				queryInstance.setAlternatives(null);
				queryInstance.setFreeTextAlternativeValue(null);
				queryInstance.getQueryInstanceDescriptor().setPopulated(false);

			} else {

				queryInstance.setAlternatives(selectedAlternatives);
				queryInstance.setFreeTextAlternativeValue(freeTextAlternativeValue);
				queryInstance.getQueryInstanceDescriptor().setPopulated(true);

			}

			return;
		}

		//Check if this query is required or if the user has selected any alternatives anyway
		if (queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED || alternativesSelected != 0) {

			if (queryInstance.getMinChecked() != null && alternativesSelected < queryInstance.getMinChecked()) {

				validationErrors.add(new TooFewAlternativesSelectedValidationError(alternativesSelected, queryInstance.getMinChecked()));

			} else if (queryInstance.getMaxChecked() != null && alternativesSelected > queryInstance.getMaxChecked()) {

				validationErrors.add(new TooManyAlternativesSelectedValidationError(alternativesSelected, queryInstance.getMaxChecked()));

			} else if (queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED && alternativesSelected == 0) {

				validationErrors.add(new ValidationError("RequiredQuery"));
			}
		}

		if(!validationErrors.isEmpty()) {
			throw new ValidationException(validationErrors);
		}

		queryInstance.setFreeTextAlternativeValue(freeTextAlternativeValue);
		queryInstance.setAlternatives(selectedAlternatives);
		queryInstance.getQueryInstanceDescriptor().setPopulated(!selectedAlternatives.isEmpty());
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		CheckboxPaymentQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		CheckboxPaymentQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

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
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler) throws SQLException {

		CheckboxPaymentQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		if(query.getAlternatives() != null){

			for(CheckboxPaymentAlternative alternative : query.getAlternatives()){

				alternative.setAlternativeID(null);
			}
		}

		this.queryDAO.add(query, transactionHandler, new RelationQuery(CheckboxPaymentQuery.ALTERNATIVES_RELATION));
	}

	@Override
	public List<CheckboxPaymentQueryInstance> getQueryInstances(CheckboxPaymentQuery CheckboxPaymentQuery, List<Integer> queryInstanceIDs) throws SQLException {

		HighLevelQuery<CheckboxPaymentQueryInstance> query = new HighLevelQuery<CheckboxPaymentQueryInstance>();

		query.addRelation(CheckboxPaymentQueryInstance.ALTERNATIVES_RELATION);

		query.addParameter(queryInstanceQueryParamFactory.getParameter(CheckboxPaymentQuery));
		query.addParameter(queryInstanceIDParamFactory.getWhereInParameter(queryInstanceIDs));

		return this.queryInstanceDAO.getAll(query);
	}

	@Override
	public Matrix<String> getSummaryTable(CheckboxPaymentQuery query, List<Integer> queryInstanceIDs) throws SQLException {

		if(query.getAlternatives() == null){

			return null;
		}

		List<CheckboxPaymentQueryInstance> instances;

		if(queryInstanceIDs != null){

			instances = getQueryInstances(query, queryInstanceIDs);

		}else{

			instances = null;
		}

		Matrix<String> table = new Matrix<String>(query.getFreeTextAlternative() != null ? query.getAlternatives().size() + 2 : query.getAlternatives().size() + 1, 2);

		table.setCell(0, 0, alternativesText);
		table.setCell(0, 1, countText);

		int currentRow = 1;

		for(CheckboxPaymentAlternative alternative : query.getAlternatives()){

			table.setCell(currentRow, 0, alternative.getName());

			int selectionCount = 0;

			if(instances != null){

				for(CheckboxPaymentQueryInstance instance : instances){

					if(instance.getAlternatives() != null && instance.getAlternatives().contains(alternative)){

						selectionCount++;
					}
				}
			}

			table.setCell(currentRow, 1, String.valueOf(selectionCount));

			currentRow++;
		}

		if(query.getFreeTextAlternative() != null){

			table.setCell(currentRow, 0, query.getFreeTextAlternative());

			int selectionCount = 0;

			if(instances != null){

				for(CheckboxPaymentQueryInstance instance : instances){

					if(instance.getFreeTextAlternativeValue() != null){

						selectionCount++;
					}
				}
			}

			table.setCell(currentRow, 1, String.valueOf(selectionCount));
		}

		return table;
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, CheckboxPaymentQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if(queryInstance.getQuery().getDescription() != null){

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler)));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected Class<CheckboxPaymentQueryInstance> getQueryInstanceClass() {

		return CheckboxPaymentQueryInstance.class;
	}

}
