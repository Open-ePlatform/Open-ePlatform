package com.nordicpeak.flowengine.queries.manualmultisignquery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.StringSwedishPhoneNumberPopulator;
import se.unlogic.standardutils.populators.SwedishSocialSecurity12DigitsWithoutMinusPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
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
import com.nordicpeak.flowengine.interfaces.MultiSigningQueryProvider;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.utils.TextTagReplacer;


public class ManualMultiSignQueryProviderModule extends BaseQueryProviderModule<ManualMultiSignQueryInstance> implements MultiSigningQueryProvider {

	private static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();
	
	@XSLVariable(prefix = "java.")
	private String exportFirstName;
	
	@XSLVariable(prefix = "java.")
	private String exportLastName;

	@XSLVariable(prefix = "java.")
	private String exportEmail;

	@XSLVariable(prefix = "java.")
	private String exportMobilePhone;
	
	@XSLVariable(prefix = "java.")
	private String exportSocialSecurityNumber;

	private AnnotatedDAO<ManualMultiSignQuery> queryDAO;
	private AnnotatedDAO<ManualMultiSignQueryInstance> queryInstanceDAO;

	private ManualMultiSignQueryCRUD queryCRUD;
	
	private QueryParameterFactory<ManualMultiSignQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<ManualMultiSignQueryInstance, Integer> queryInstanceIDParamFactory;
	private QueryParameterFactory<ManualMultiSignQueryInstance, String> queryInstanceCitizenIdentifierParamFactory;

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		//Automatic table version handling
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, ManualMultiSignQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(ManualMultiSignQuery.class);
		queryInstanceDAO = daoFactory.getDAO(ManualMultiSignQueryInstance.class);

		queryCRUD = new ManualMultiSignQueryCRUD(queryDAO.getWrapper(Integer.class), new AnnotatedRequestPopulator<ManualMultiSignQuery>(ManualMultiSignQuery.class), "ManualMultiSignQuery", "query", null, this);
		
		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);
		queryInstanceCitizenIdentifierParamFactory = queryInstanceDAO.getParamFactory("socialSecurityNumber", String.class);
	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ManualMultiSignQuery query = new ManualMultiSignQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor) throws Throwable {

		ManualMultiSignQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		System.out.println(descriptor);
		
		ManualMultiSignQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws Throwable {

		ManualMultiSignQueryInstance queryInstance = null;

		//Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new ManualMultiSignQueryInstance();

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

	private ManualMultiSignQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<ManualMultiSignQuery> query = new HighLevelQuery<ManualMultiSignQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private ManualMultiSignQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<ManualMultiSignQuery> query = new HighLevelQuery<ManualMultiSignQuery>();

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private ManualMultiSignQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<ManualMultiSignQueryInstance> query = new HighLevelQuery<ManualMultiSignQueryInstance>(ManualMultiSignQueryInstance.QUERY_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ManualMultiSignQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ManualMultiSignQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

		if (queryInstance == null) {

			return false;
		}

		this.queryInstanceDAO.delete(queryInstance, transactionHandler);

		return true;
	}

	@Override
	public void copyQuery(MutableQueryDescriptor sourceQueryDescriptor, MutableQueryDescriptor copyQueryDescriptor, TransactionHandler transactionHandler) throws SQLException {

		ManualMultiSignQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());


		this.queryDAO.add(query, transactionHandler, null);
	}

	@Override
	public void save(ManualMultiSignQueryInstance queryInstance, TransactionHandler transactionHandler) throws Throwable {

		//Check if the query instance has an ID set and if the ID of the descriptor has changed
		if(queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())){

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, null);

		}else{

			this.queryInstanceDAO.update(queryInstance, transactionHandler, null);
		}
	}

	@Override
	public void populate(ManualMultiSignQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException {

		Integer queryID = queryInstance.getQuery().getQueryID();

		if(queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED || !StringUtils.isEmpty(req.getParameter("q" + queryID +"_socialSecurityNumber")) || !StringUtils.isEmpty(req.getParameter("q" + queryID +"_firstname")) || !StringUtils.isEmpty(req.getParameter("q" + queryID +"_lastname")) || !StringUtils.isEmpty(req.getParameter("q" + queryID + "_email")) || !StringUtils.isEmpty(req.getParameter("q" + queryID + "_mobilePhone"))){

			List<ValidationError> errors = new ArrayList<ValidationError>();

			String socialSecurityNumber = ValidationUtils.validateParameter("q" + queryID +"_socialSecurityNumber", req, !allowPartialPopulation, SwedishSocialSecurity12DigitsWithoutMinusPopulator.getPopulator(), errors);
			String firstname = ValidationUtils.validateParameter("q" + queryID +"_firstname", req, !allowPartialPopulation, 1, 255, errors);
			String lastname = ValidationUtils.validateParameter("q" + queryID +"_lastname", req, !allowPartialPopulation, 1, 255, errors);
			String email = ValidationUtils.validateParameter("q" + queryID + "_email", req, false, 1, 255, EMAIL_POPULATOR, errors);
			String mobilePhone = ValidationUtils.validateParameter("q" + queryID + "_mobilePhone", req, false, StringSwedishPhoneNumberPopulator.getPopulator(), errors);
			
			if(!allowPartialPopulation && errors.isEmpty() && email == null && mobilePhone == null){
				
				errors.add(new ValidationError("NoContactChannelSpecified", (String)null, "q" + queryID + "_mobilePhone"));
				errors.add(new ValidationError("NoContactChannelSpecified", (String)null, "q" + queryID + "_email"));
			}
			
			if(!errors.isEmpty()){

				throw new ValidationException(errors);
			}
			
			queryInstance.setSocialSecurityNumber(socialSecurityNumber);
			queryInstance.setFirstname(firstname);
			queryInstance.setLastname(lastname);
			queryInstance.setEmail(email);
			queryInstance.setMobilePhone(mobilePhone);
			
			queryInstance.getQueryInstanceDescriptor().setPopulated(true);

		}else{

			queryInstance.getQueryInstanceDescriptor().setPopulated(false);
			queryInstance.reset(attributeHandler);
		}
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@Override
	protected Class<ManualMultiSignQueryInstance> getQueryInstanceClass() {

		return ManualMultiSignQueryInstance.class;
	}
	
	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		ManualMultiSignQuery query = new ManualMultiSignQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}
	
	public String getExportFirstName() {

		return exportFirstName;
	}

	public String getExportLastName() {

		return exportLastName;
	}

	public String getExportEmail() {

		return exportEmail;
	}

	public String getExportMobilePhone() {

		return exportMobilePhone;
	}

	public String getExportSocialSecurityNumber() {

		return exportSocialSecurityNumber;
	}

	@Override
	public List<Integer> getQueryInstanceIDs(String citizenIdentifier) throws SQLException {
		
		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(queryInstanceDAO.getDataSource(), "SELECT queryInstanceID FROM " + queryInstanceDAO.getTableName() + " WHERE " + queryInstanceCitizenIdentifierParamFactory.getColumnName() + " = ?", IntegerPopulator.getPopulator());
		query.setString(1, citizenIdentifier);
		
		return query.executeQuery();
	}
}
