package com.nordicpeak.flowengine.queries.generalmapquery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.sql.rowset.serial.SerialBlob;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.beans.ValueDescriptor;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.log4jutils.levels.LogLevel;
import se.unlogic.standardutils.arrays.ArrayUtils;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.mime.MimeUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.SimpleStringConverter;
import se.unlogic.standardutils.string.StringConverter;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLGenerator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.url.URLRewriter;

import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.enums.QueryState;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.ImmutableStatus;
import com.nordicpeak.flowengine.interfaces.InstanceMetadata;
import com.nordicpeak.flowengine.interfaces.MutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.MutableQueryInstanceDescriptor;
import com.nordicpeak.flowengine.interfaces.PDFAttachment;
import com.nordicpeak.flowengine.interfaces.PDFResourceProvider;
import com.nordicpeak.flowengine.interfaces.Query;
import com.nordicpeak.flowengine.interfaces.QueryContentFilter;
import com.nordicpeak.flowengine.interfaces.QueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUDCallback;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryProviderModule;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.MapConfiguration;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.MapPrint;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.MapTool;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.PrintConfiguration;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.PrintService;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.SearchLMService;
import com.nordicpeak.flowengine.utils.BlobPDFAttachment;
import com.nordicpeak.flowengine.utils.BlobResourceProvider;
import com.nordicpeak.flowengine.utils.JTidyUtils;
import com.nordicpeak.flowengine.utils.TextTagReplacer;
import com.vividsolutions.jts.algorithm.CentroidArea;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

public class GeneralMapQueryProviderModule extends BaseQueryProviderModule<GeneralMapQueryInstance> implements BaseQueryCRUDCallback {

	private static final StringConverter ISO_TO_UTF8_STRING_DECODER = new SimpleStringConverter(Charset.forName("ISO-8859-1"), Charset.forName("UTF-8"));
	
	private static final RelationQuery SAVE_QUERY_INSTANCE_RELATION_QUERY = new RelationQuery(GeneralMapQueryInstance.MAPPRINTS_RELATION, GeneralMapQueryInstance.GEOMETRIES_RELATION);

	@XSLVariable(prefix = "java.")
	private String pdfAttachmentDescriptionPrefix = "A file from query:";

	@XSLVariable(prefix = "java.")
	private String pdfAttachmentFilename = "Map $scale";

	@XSLVariable(prefix = "java.")
	private String pdfAttachmentFilenameWithoutScale = "Map";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Map client script URL", description = "The URL to the map client script", required = true)
	private String openEMapScriptURL;

	@ModuleSetting(allowsNull = true)
	private List<Integer> mapPrintIDs;

	@ModuleSetting(allowsNull = true)
	private Integer searchLMServiceID;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Log print configuration", description = "Log generated print configuration")
	private boolean logPrintConfig;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "MapFish connection timeout", description = "MapFish connection timeout")
	protected Integer mapFishConnectionTimeout = 5000;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "MapFish read timeout", description = "MapFish read timeout")
	protected Integer mapFishReadTimeout = 10000;

	@ModuleSetting
	@EnumDropDownSettingDescriptor(name = "MapFish timeout log level", description = "The log level used when there is a socket timeout while waiting for an image from MapFish", required = true)
	private LogLevel mapFishTimeoutLogLevel = LogLevel.ERROR;

	@ModuleSetting
	@EnumDropDownSettingDescriptor(name = "MapFish error log level", description = "The log level used when there is an error in the communication with MapFish", required = true)
	private LogLevel mapFishErrorLogLevel = LogLevel.ERROR;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Search LM service response encoding fix", description = "Use when results are showing accented characters incorrectly")
	private boolean useSearchLMServiceResponseEncodingFix = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Use search LM proxy", description = "Wether to use the proxy for searching or going directly from client to the server")
	private boolean useSearchProxy = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Search LM service request encoding fix", description = "Use when request URI encoding does not match connector URI encoding")
	private boolean useSearchLMServiceRequestEncodingFix = true;

	private StringConverter URL_STRING_DECODER;

	private ConcurrentHashMap<Integer, String> generalMapQueryConfigurations;

	private SearchLMService searchLMService;

	private Map<Integer, MapPrint> mapPrints;

	private AnnotatedDAO<GeneralMapQuery> queryDAO;
	private AnnotatedDAO<GeneralMapQueryInstance> queryInstanceDAO;

	private AnnotatedDAO<MapConfiguration> mapConfigurationDAO;
	private AnnotatedDAO<PrintConfiguration> printConfigurationDAO;
	private AnnotatedDAO<SearchLMService> searchLMServiceDAO;
	private AnnotatedDAO<MapPrint> mapPrintDAO;
	private AnnotatedDAO<MapTool> mapToolDAO;

	private GeneralMapQueryCRUD queryCRUD;

	private QueryParameterFactory<GeneralMapQuery, Integer> queryIDParamFactory;
	private QueryParameterFactory<GeneralMapQueryInstance, Integer> queryInstanceIDParamFactory;

	private QueryParameterFactory<MapConfiguration, Integer> mapConfigurationIDParamFactory;
	private QueryParameterFactory<MapConfiguration, Boolean> mapConfigurationEnabledParamFactory;
	private QueryParameterFactory<MapConfiguration, String> mapConfigurationNameParamFactory;
	private QueryParameterFactory<MapPrint, Integer> mapPrintIDParamFactory;
	private QueryParameterFactory<MapPrint, String> mapPrintAliasParamFactory;
	private QueryParameterFactory<MapTool, Integer> mapToolIDParamFactory;
	private QueryParameterFactory<SearchLMService, Integer> searchLMServiceIDParamFactory;

	@Override
	protected synchronized void moduleConfigured() throws Exception {

		super.moduleConfigured();

		if (searchLMServiceID != null && searchLMServiceID > -1) {

			HighLevelQuery<SearchLMService> searchLMServiceQuery = new HighLevelQuery<SearchLMService>();

			searchLMServiceQuery.addParameter(searchLMServiceIDParamFactory.getParameter(searchLMServiceID));

			SearchLMService searchLMService = searchLMServiceDAO.get(searchLMServiceQuery);

			if (searchLMService == null) {

				log.error("Search LM service with map search lm service id " + searchLMServiceID + " not found in db");
			}

			this.searchLMService = searchLMService;

		} else {

			this.searchLMService = null;
		}

		if (mapPrintIDs != null) {

			HighLevelQuery<MapPrint> mapPrintQuery = new HighLevelQuery<MapPrint>(MapPrint.LAYOUT_RELATION, MapPrint.OUTPUTFORMAT_RELATION, MapPrint.PRINTCONFIGURATION_RELATION, MapPrint.PRINTSERVICE_RELATION, MapPrint.RESOLUTION_RELATION, MapPrint.SCALE_RELATION, PrintService.LAYOUTS_RELATION, PrintService.OUTPUTFORMATS_RELATION, PrintService.RESOLUTIONS_RELATION, PrintService.SCALES_RELATION);

			mapPrintQuery.addParameter(mapPrintIDParamFactory.getWhereInParameter(mapPrintIDs));

			List<MapPrint> mapPrints = mapPrintDAO.getAll(mapPrintQuery);

			this.mapPrints = new HashMap<Integer, MapPrint>();

			if (mapPrints != null) {

				for (MapPrint mapPrint : mapPrints) {

					this.mapPrints.put(mapPrint.getMapPrintID(), mapPrint);
				}

			}

		}

		if (useSearchLMServiceRequestEncodingFix) {

			URL_STRING_DECODER = ISO_TO_UTF8_STRING_DECODER;

		} else {

			URL_STRING_DECODER = null;
		}

		cacheGeneralMapQueryConfigurations();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, GeneralMapQueryProviderModule.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		queryDAO = daoFactory.getDAO(GeneralMapQuery.class);
		queryInstanceDAO = daoFactory.getDAO(GeneralMapQueryInstance.class);

		mapConfigurationDAO = daoFactory.getDAO(MapConfiguration.class);
		printConfigurationDAO = daoFactory.getDAO(PrintConfiguration.class);
		mapPrintDAO = daoFactory.getDAO(MapPrint.class);
		mapToolDAO = daoFactory.getDAO(MapTool.class);
		searchLMServiceDAO = daoFactory.getDAO(SearchLMService.class);

		queryIDParamFactory = queryDAO.getParamFactory("queryID", Integer.class);
		queryInstanceIDParamFactory = queryInstanceDAO.getParamFactory("queryInstanceID", Integer.class);

		mapConfigurationIDParamFactory = mapConfigurationDAO.getParamFactory("mapConfigurationID", Integer.class);
		mapConfigurationEnabledParamFactory = mapConfigurationDAO.getParamFactory("enabled", boolean.class);
		mapConfigurationNameParamFactory = mapConfigurationDAO.getParamFactory("name", String.class);
		mapPrintIDParamFactory = mapPrintDAO.getParamFactory("mapPrintID", Integer.class);
		mapPrintAliasParamFactory = mapPrintDAO.getParamFactory("alias", String.class);
		mapToolIDParamFactory = mapToolDAO.getParamFactory("toolID", Integer.class);
		searchLMServiceIDParamFactory = searchLMServiceDAO.getParamFactory("searchLMServiceID", Integer.class);

		queryCRUD = new GeneralMapQueryCRUD(queryDAO.getWrapper(Integer.class), this);
	}

	private void cacheGeneralMapQueryConfigurations() {

		try {

			HashMap<Integer, String> tempGeneralMapConfigurations = new HashMap<Integer, String>();

			HighLevelQuery<GeneralMapQuery> query = new HighLevelQuery<GeneralMapQuery>(GeneralMapQuery.MAPTOOLS_RELATION, GeneralMapQuery.MAPPRINTS_RELATION, GeneralMapQueryTool.MAPTOOL_RELATION, GeneralMapQueryPrint.MAPPRINT_RELATION, GeneralMapQuery.MAPCONFIGURATION_RELATION, MapConfiguration.MAPTOOLS_RELATION);

			List<GeneralMapQuery> generalMapQueries = queryDAO.getAll(query);

			if (generalMapQueries != null) {

				for (GeneralMapQuery generalMapQuery : generalMapQueries) {

					String mapConfiguration = createGeneralMapQueryConfiguration(generalMapQuery);

					if (mapConfiguration != null) {

						tempGeneralMapConfigurations.put(generalMapQuery.getQueryID(), mapConfiguration);

					}
				}

			}

			generalMapQueryConfigurations = new ConcurrentHashMap<Integer, String>(tempGeneralMapConfigurations);

		} catch (SQLException e) {

			log.error("Error when caching map query configurations", e);
		}

	}

	private String createGeneralMapQueryConfiguration(GeneralMapQuery generalMapQuery) {

		if (generalMapQuery.getMapConfiguration() == null) {

			log.warn("Map configuration for general map query " + generalMapQuery + " not set");

			return null;
		}

		String template = generalMapQuery.getMapConfiguration().getMapConfigTemplate();

		if (generalMapQuery.getMapTools() != null) {

			StringBuilder toolConfiguration = new StringBuilder();

			for (GeneralMapQueryTool mapQueryTool : generalMapQuery.getMapTools()) {

				String toolTemplate = mapQueryTool.getMapTool().getConfigTemplate();

				if (!StringUtils.isEmpty(mapQueryTool.getTooltip())) {

					toolTemplate = toolTemplate.replace("$gmqTooltip", mapQueryTool.getTooltip());

				} else {

					toolTemplate = toolTemplate.replace("$gmqTooltip", mapQueryTool.getMapTool().getTooltip());
				}

				toolTemplate = toolTemplate.replace("$gmqOnlyOneGeometry", mapQueryTool.isOnlyOneGeometry() + "");

				toolConfiguration.append(toolTemplate + ",");
			}

			String tools = toolConfiguration.length() > 0 ? toolConfiguration.substring(0, toolConfiguration.length() - 1) : "";

			template = template.replace("$gmqTools", "[" + tools + "]");

		} else {

			template = template.replace("$gmqTools", "[]");
		}

		template = template.replace("$gmqOnlyOneGlobalGeometry", generalMapQuery.isAllowOnlyOneGeometry() + "");
		template = template.replace("$gmqIncorrectDrawingMessage", generalMapQuery.getIncorrectDrawingMessage() != null ? generalMapQuery.getIncorrectDrawingMessage() : "");
		template = template.replace("$gmqSearchPUDEnabled", (generalMapQuery.isPudSearchEnabled() || generalMapQuery.isAddressSearchEnabled() || generalMapQuery.isPlaceSearchEnabled()) + "");
		template = template.replace("$gmqSearchCoordinateEnabled", generalMapQuery.isCoordinateSearchEnabled() + "");
		template = template.replace("$gmqPudSearchEnabled", generalMapQuery.isPudSearchEnabled() + "");
		template = template.replace("$gmqAddressSearchEnabled", generalMapQuery.isAddressSearchEnabled() + "");
		template = template.replace("$gmqPlaceSearchEnabled", generalMapQuery.isPlaceSearchEnabled() + "");

		return template;
	}

	public void cacheGeneralMapQueryConfiguration(GeneralMapQuery generalMapQuery) {

		String mapConfiguration = createGeneralMapQueryConfiguration(generalMapQuery);

		generalMapQueryConfigurations.put(generalMapQuery.getQueryID(), mapConfiguration);

	}

	public void deleteGeneralMapQueryConfiguration(GeneralMapQuery generalMapQuery) {

		generalMapQueryConfigurations.remove(generalMapQuery.getQueryID());

	}

	@Override
	public Query createQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		GeneralMapQuery query = new GeneralMapQuery();

		query.setQueryID(descriptor.getQueryID());

		this.queryDAO.add(query, transactionHandler, null);

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query importQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler, Map<Integer, ImmutableStatus> statusConversionMap, QueryContentFilter contentFilter) throws Throwable {

		GeneralMapQuery query = new GeneralMapQuery();

		query.setQueryID(descriptor.getQueryID());

		query.populate(descriptor.getImportParser().getNode(XMLGenerator.getElementName(query.getClass())));
		
		contentFilter.filterHTML(query);

		if (query.getMapConfigurationName() != null) {

			MapConfiguration mapConfiguration = getMapConfiguration(query.getMapConfigurationName(), true);

			if (mapConfiguration != null) {

				query.setMapConfiguration(mapConfiguration);

				if (query.getMapTools() != null) {

					if (mapConfiguration.getMapTools() != null) {

						for (GeneralMapQueryTool mapQueryTool : query.getMapTools()) {

							for (MapTool mapTool : mapConfiguration.getMapTools()) {

								if (mapQueryTool.getMapToolAlias().equals(mapTool.getAlias())) {

									mapQueryTool.setMapTool(mapTool);
								}

							}

							if (mapQueryTool.getMapTool() == null) {

								// TODO Temporary handling
								log.warn("No matching map tool found for alias " + mapQueryTool.getMapToolAlias());
								continue;
							}

						}

					} else {

						log.warn("No map tools found for map configuration " + mapConfiguration + " cant populate tools for imported query " + query);
					}

				}

			} else {

				log.warn("No matching map configuration (" + query.getMapConfigurationName() + ") found for imported query " + query);
				
				query.setMapTools(null);
				query.setMapPrints(null);
			}

		} else {

			// TODO Temporary handling
			log.warn("No map configuration name found for imported query " + query);
			query.setMapTools(null);
			query.setMapPrints(null);
		}

		if (query.getMapPrints() != null) {

			for (GeneralMapQueryPrint generalMapQueryPrint : query.getMapPrints()) {

				MapPrint mapPrint = getMapPrint(generalMapQueryPrint.getMapPrintAlias());

				if (mapPrint == null) {

					// TODO Temporary handling
					log.warn("No matching map print (" + generalMapQueryPrint.getMapPrintAlias() + ") found for imported query " + query);
					continue;
				}

				generalMapQueryPrint.setMapPrint(mapPrint);
			}

		}

		this.queryDAO.add(query, transactionHandler, null);

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, boolean extraData) throws SQLException {

		GeneralMapQuery query = this.getQuery(descriptor.getQueryID());

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public Query getQuery(MutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		GeneralMapQuery query = this.getQuery(descriptor.getQueryID(), transactionHandler);

		if (query == null) {

			return null;
		}

		query.init(descriptor, getFullAlias() + "/config/" + descriptor.getQueryID());

		return query;
	}

	@Override
	public QueryInstance getQueryInstance(MutableQueryInstanceDescriptor descriptor, String instanceManagerID, HttpServletRequest req, User user, User poster, InstanceMetadata instanceMetadata) throws SQLException {

		GeneralMapQueryInstance queryInstance = null;

		// Check if we should create a new instance or get an existing one
		if (descriptor.getQueryInstanceID() == null) {

			queryInstance = new GeneralMapQueryInstance();

			queryInstance.setQuery(getQuery(descriptor.getQueryDescriptor().getQueryID()));

			if (queryInstance.getQuery() == null) {

				return null;
			}

			queryInstance.set(descriptor);
			queryInstance.copyQueryValues();

		} else {

			queryInstance = getQueryInstance(descriptor.getQueryInstanceID());

			if (queryInstance == null) {

				return null;
			}

			queryInstance.set(descriptor);

		}

		if (req != null) {

			FCKUtils.setAbsoluteFileUrls(queryInstance.getQuery(), RequestUtils.getFullContextPathURL(req) + ckConnectorModuleAlias);

			URLRewriter.setAbsoluteLinkUrls(queryInstance.getQuery(), req);
		}

		queryInstance.getQuery().scanAttributeTags();

		TextTagReplacer.replaceTextTags(queryInstance.getQuery(), instanceMetadata.getSiteProfile());

		return queryInstance;
	}

	private GeneralMapQuery getQuery(Integer queryID) throws SQLException {

		HighLevelQuery<GeneralMapQuery> query = new HighLevelQuery<GeneralMapQuery>(GeneralMapQuery.MAPTOOLS_RELATION, GeneralMapQuery.MAPPRINTS_RELATION, GeneralMapQueryTool.MAPTOOL_RELATION, GeneralMapQueryPrint.MAPPRINT_RELATION, GeneralMapQuery.MAPCONFIGURATION_RELATION, MapConfiguration.MAPTOOLS_RELATION, MapPrint.LAYOUT_RELATION, MapPrint.OUTPUTFORMAT_RELATION, MapPrint.RESOLUTION_RELATION, MapPrint.SCALE_RELATION, MapPrint.PRINTCONFIGURATION_RELATION, MapPrint.PRINTSERVICE_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query);
	}

	private GeneralMapQuery getQuery(Integer queryID, TransactionHandler transactionHandler) throws SQLException {

		HighLevelQuery<GeneralMapQuery> query = new HighLevelQuery<GeneralMapQuery>(GeneralMapQuery.MAPTOOLS_RELATION, GeneralMapQuery.MAPPRINTS_RELATION, GeneralMapQueryTool.MAPTOOL_RELATION, GeneralMapQueryPrint.MAPPRINT_RELATION, GeneralMapQuery.MAPCONFIGURATION_RELATION, MapConfiguration.MAPTOOLS_RELATION, MapPrint.LAYOUT_RELATION, MapPrint.OUTPUTFORMAT_RELATION, MapPrint.RESOLUTION_RELATION, MapPrint.SCALE_RELATION, MapPrint.PRINTCONFIGURATION_RELATION, MapPrint.PRINTSERVICE_RELATION);

		query.addParameter(queryIDParamFactory.getParameter(queryID));

		return queryDAO.get(query, transactionHandler);
	}

	private GeneralMapQueryInstance getQueryInstance(Integer queryInstanceID) throws SQLException {

		HighLevelQuery<GeneralMapQueryInstance> query = new HighLevelQuery<GeneralMapQueryInstance>(GeneralMapQueryInstance.QUERY_RELATION, GeneralMapQueryInstance.MAPPRINTS_RELATION, GeneralMapQueryInstance.GEOMETRIES_RELATION);

		query.addParameter(queryInstanceIDParamFactory.getParameter(queryInstanceID));

		return queryInstanceDAO.get(query);
	}

	@Override
	public void save(GeneralMapQueryInstance queryInstance, TransactionHandler transactionHandler, InstanceRequestMetadata requestMetadata) throws Throwable {

		//Check if the query instance has an ID set and if the ID of the descriptor has changed
		if (queryInstance.getQueryInstanceID() == null || !queryInstance.getQueryInstanceID().equals(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID())) {

			queryInstance.setQueryInstanceID(queryInstance.getQueryInstanceDescriptor().getQueryInstanceID());

			this.queryInstanceDAO.add(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);

		} else {

			this.queryInstanceDAO.update(queryInstance, transactionHandler, SAVE_QUERY_INSTANCE_RELATION_QUERY);
		}
	}

	@Override
	public void populate(GeneralMapQueryInstance queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, InstanceRequestMetadata requestMetadata) throws ValidationException {

		Integer queryID = queryInstance.getQuery().getQueryID();

		String extent = req.getParameter("q" + queryID + "_extent");
		String epsg = req.getParameter("q" + queryID + "_epsg");
		String baseLayer = req.getParameter("q" + queryID + "_baseLayer");
		String[] addedGeometries = req.getParameterValues("q" + queryID + "_geometry");

		GeometryFactory geometryFactory = new GeometryFactory();

		WKTReader reader = new WKTReader(geometryFactory);

		List<Geometry> geometries = null;

		if (!ArrayUtils.isEmpty(addedGeometries)) {

			geometries = populateGeometries(addedGeometries, reader);
		}

		queryInstance.setGeometries(geometries);
		queryInstance.setExtent(extent);
		queryInstance.setEpsg(epsg);
		queryInstance.setVisibleBaseLayer(baseLayer);

		if (CollectionUtils.isEmpty(geometries) && queryInstance.getQueryInstanceDescriptor().getQueryState() == QueryState.VISIBLE_REQUIRED) {

			List<ValidationError> errors = new ArrayList<ValidationError>(2);

			if (CollectionUtils.isEmpty(geometries)) {
				errors.add(new ValidationError("GeometryRequired"));
			}

			throw new ValidationException(errors);

		} else if (CollectionUtils.isEmpty(geometries) && !queryInstance.getQuery().isForceQueryPopulation()) {

			queryInstance.reset(attributeHandler);

			return;
		}

		if (StringUtils.isEmpty(queryInstance.getExtent()) || StringUtils.isEmpty(queryInstance.getEpsg())) {

			throw new ValidationException(new ValidationError("InCompleteMapQuerySubmit"));

		}

		queryInstance.getQueryInstanceDescriptor().setPopulated(true);

		generateMapImages(queryInstance, user);

	}

	@WebPublic(alias = "config")
	public ForegroundModuleResponse configureQuery(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.queryCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(alias = "toolicon")
	public ForegroundModuleResponse getToolIcon(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer toolID = uriParser.getInt(2);

		if (toolID != null) {

			HighLevelQuery<MapTool> query = new HighLevelQuery<MapTool>();
			query.addParameter(mapToolIDParamFactory.getParameter(toolID));

			MapTool mapTool = mapToolDAO.get(query);

			if (mapTool != null) {

				if (mapTool.getIcon() != null) {

					InputStream in = null;
					OutputStream out = null;

					String filename = mapTool.getTooltip() + ".png";

					try {

						HTTPUtils.setContentLength(mapTool.getIcon().length(), res);

						res.setContentType(MimeUtils.getMimeType(filename));
						res.setHeader("Content-Disposition", "inline; filename=\"" + FileUtils.toValidHttpFilename(filename) + "\"");

						in = mapTool.getIcon().getBinaryStream();

						out = res.getOutputStream();

						StreamUtils.transfer(in, out);

					} catch (RuntimeException e) {

						log.debug("Caught exception " + e + " while sending map tool icon " + filename + " to " + user);

					} catch (IOException e) {

						log.debug("Caught exception " + e + " while sending map tool icon " + filename + " to " + user);

					} finally {

						CloseUtils.close(in);
						CloseUtils.close(out);
					}

					return null;

				}

			}

		}

		throw new URINotFoundException(uriParser);

	}

	@WebPublic(alias = "clientprint")
	public ForegroundModuleResponse clientPrint(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer queryID = uriParser.getInt(2);

		String param = uriParser.get(3);

		if (queryID != null && param != null && param.equalsIgnoreCase("info.json")) {

			GeneralMapQuery generalMapQuery = getQuery(queryID);

			if (generalMapQuery != null && generalMapQuery.getMapPrints() != null) {

				for (GeneralMapQueryPrint queryPrint : generalMapQuery.getMapPrints()) {

					if (queryPrint.getMapPrint().getPrintService() != null) {

						String response = null;

						if (queryPrint.getMapPrint().getPrintService().getPrintServiceAddress().startsWith("https://")) {

							response = HTTPUtils.sendHTTPSGetRequest(queryPrint.getMapPrint().getPrintService().getPrintServiceAddress() + "/pdf/info.json", null, null, null);

						} else {

							response = HTTPUtils.sendHTTPGetRequest(queryPrint.getMapPrint().getPrintService().getPrintServiceAddress() + "/pdf/info.json", null, null, null);

						}

						if (response != null) {

							HTTPUtils.sendReponse(response, JsonUtils.getContentType(), res);

							return null;
						}

					}

				}

			}

		}

		return null;
	}

	@WebPublic(alias = "mapconfiguration")
	public ForegroundModuleResponse getMapConfiguration(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer queryID = uriParser.getInt(2);

		if (queryID != null) {

			String mapConfiguration = generalMapQueryConfigurations.get(queryID);

			if (mapConfiguration != null) {

				log.info("User " + user + " requesting map configuration");

				HTTPUtils.sendReponse(mapConfiguration, JsonUtils.getContentType(), res);

				return null;
			}

		}

		throw new URINotFoundException(uriParser);

	}

	@WebPublic(alias = "search")
	public ForegroundModuleResponse search(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (useSearchProxy && uriParser.size() >= 3) {

			String searchMethod = uriParser.get(2);

			if (searchMethod.equalsIgnoreCase("registerenheter")) {

				searchPUD(req, res, user, uriParser);

			} else if (searchMethod.equalsIgnoreCase("addresses")) {

				searchAddress(req, res, user, uriParser);

			} else if (searchMethod.equalsIgnoreCase("placenames")) {

				searchPlace(req, res, user, uriParser);

			} else if (searchMethod.equalsIgnoreCase("enhetsomraden")) {

				searchCoordinate(req, res, user, uriParser);

			} else {

				throw new URINotFoundException(uriParser);

			}

			return null;

		}

		throw new URINotFoundException(uriParser);
	}

	private void searchPUD(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (searchLMService == null || !searchLMService.isPudSearchEnabled()) {

			throw new URINotFoundException(uriParser);
		}

		String searchURL = searchLMService.getSearchLMAddress() + "/registerenheter";

		if (uriParser.size() >= 5) {
			searchURL += "/" + uriParser.get(3) + "/" + uriParser.get(4);
		}

		sendSearchReqest(req, res, user, searchURL, true);

	}

	private void searchAddress(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (searchLMService == null || !searchLMService.isAddressSearchEnabled()) {

			throw new URINotFoundException(uriParser);
		}

		sendSearchReqest(req, res, user, searchLMService.getSearchLMAddress() + "/addresses", true);

	}

	private void searchPlace(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (searchLMService == null || !searchLMService.isPlaceSearchEnabled()) {

			throw new URINotFoundException(uriParser);
		}

		sendSearchReqest(req, res, user, searchLMService.getSearchLMAddress() + "/placenames", false);

	}

	private void searchCoordinate(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (searchLMService == null || !searchLMService.isCoordinateSearchEnabled()) {

			throw new URINotFoundException(uriParser);
		}

		sendSearchReqest(req, res, user, searchLMService.getSearchLMAddress() + "/enhetsomraden", true);

	}

	private void sendSearchReqest(HttpServletRequest req, HttpServletResponse res, User user, String search, boolean appendSearchPrefix) throws IOException {

		String charset = "UTF-8";

		StringBuilder queryParameters = new StringBuilder();

		if (req.getParameterMap() != null) {

			HashMap<String, String[]> paramMap = new HashMap<String, String[]>(req.getParameterMap());

			for (String paramName : paramMap.keySet()) {

				String[] values = paramMap.get(paramName);

				if (values != null) {

					if (paramName.equalsIgnoreCase("lmuser")) {

						continue;

					} else if (paramName.equalsIgnoreCase("q") && appendSearchPrefix) {

						String prefix = searchLMService.getSearchPrefix();

						if (!StringUtils.isEmpty(prefix)) {

							String q = fixEncoding(values[0]);

							if (!q.toLowerCase().startsWith(prefix.toLowerCase())) {

								queryParameters.append("q=" + URLEncoder.encode(prefix + " " + q, charset) + "&");

								continue;
							}

						}

					} else if (paramName.equalsIgnoreCase("kommunkod")) {

						String municipalityCode = searchLMService.getMunicipalityCode();

						if (!StringUtils.isEmpty(municipalityCode)) {

							queryParameters.append("kommunkod=" + URLEncoder.encode(municipalityCode, charset) + "&");

							continue;
						}

					}

					for (String value : values) {

						queryParameters.append(paramName + "=" + URLEncoder.encode(fixEncoding(value), charset) + "&");

					}

				}

			}

		}

		String searchQuery = search + "?" + queryParameters.toString() + "lmuser=" + URLEncoder.encode(searchLMService.getLmUser(), charset);

		try {

			log.info("User " + user + " searching using http search service with query " + searchQuery);

			String response = null;

			if (searchQuery.startsWith("https://")) {

				response = HTTPUtils.sendHTTPSGetRequest(searchQuery, null, null, null);

			} else {

				response = HTTPUtils.sendHTTPGetRequest(searchQuery, null, null, null);

			}

			HTTPUtils.sendReponse(getUnescapedText(response), JsonUtils.getContentType(), res);

		} catch (IOException e) {

			log.warn("Unable to get any search result from lm search service using query " + searchQuery + ". Caused by: " + e.getMessage());

			JsonObject error = new JsonObject();
			error.putField("Error", "true");

			HTTPUtils.sendReponse(error.toJson(), JsonUtils.getContentType(), res);
		}

	}

	@Override
	public boolean deleteQuery(ImmutableQueryDescriptor descriptor, TransactionHandler transactionHandler) throws SQLException {

		GeneralMapQuery query = getQuery(descriptor.getQueryID());

		if (query == null) {

			return false;
		}

		this.queryDAO.delete(query, transactionHandler);

		deleteGeneralMapQueryConfiguration(query);

		return true;
	}

	@Override
	public boolean deleteQueryInstance(ImmutableQueryInstanceDescriptor descriptor, TransactionHandler transactionHandler) throws Throwable {

		GeneralMapQueryInstance queryInstance = this.getQueryInstance(descriptor.getQueryInstanceID());

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

		GeneralMapQuery query = getQuery(sourceQueryDescriptor.getQueryID(), transactionHandler);

		query.setQueryID(copyQueryDescriptor.getQueryID());

		if (query.getMapTools() != null) {

			for (GeneralMapQueryTool generalMapQueryTool : query.getMapTools()) {

				generalMapQueryTool.setQueryToolID(null);
			}
		}

		if (query.getMapPrints() != null) {

			for (GeneralMapQueryPrint generalMapQueryPrint : query.getMapPrints()) {

				generalMapQueryPrint.setQueryPrintID(null);
			}
		}

		queryDAO.add(query, transactionHandler, null);
		
		cacheGeneralMapQueryConfiguration(query);
		
	}

	@Override
	protected void appendPDFData(Document doc, Element showQueryValuesElement, GeneralMapQueryInstance queryInstance, AttributeHandler attributeHandler) {

		super.appendPDFData(doc, showQueryValuesElement, queryInstance, attributeHandler);

		if (queryInstance.getQuery().getDescription() != null) {

			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "Description", JTidyUtils.getXHTML(queryInstance.getQuery().getDescription(attributeHandler), systemInterface.getEncoding()));
			XMLUtils.appendNewCDATAElement(doc, showQueryValuesElement, "isHTMLDescription", queryInstance.getQuery().getDescription().contains("<") && queryInstance.getQuery().getDescription().contains(">"));
		}
	}

	@Override
	protected List<PDFAttachment> getPDFAttachments(GeneralMapQueryInstance queryInstance) {

		List<PDFAttachment> attachments = new ArrayList<PDFAttachment>();

		if (queryInstance.getMapPrints() != null) {

			for (GeneralMapQueryInstancePrint queryInstancePrint : queryInstance.getMapPrints()) {

				if (queryInstancePrint.getMapImage() != null) {

					String namePrefix;

					if (queryInstancePrint.getScale() != null) {
						namePrefix = this.pdfAttachmentFilename.replace("$scale", queryInstancePrint.getScale() + "");
					} else {
						namePrefix = this.pdfAttachmentFilenameWithoutScale;
					}

					attachments.add(new BlobPDFAttachment(queryInstancePrint.getMapImage(), namePrefix + "." + queryInstancePrint.getFormat(), this.pdfAttachmentDescriptionPrefix + " " + queryInstance.getQueryInstanceDescriptor().getQueryDescriptor().getName()));
				}

			}

		}

		if (!attachments.isEmpty()) {

			return attachments;
		}

		return null;
	}

	@Override
	protected PDFResourceProvider getPDFResourceProvider(GeneralMapQueryInstance queryInstance) {

		if (queryInstance.getMapPrints() != null) {

			for (GeneralMapQueryInstancePrint queryInstancePrint : queryInstance.getMapPrints()) {

				if (queryInstancePrint.isUseInPreview() && queryInstancePrint.getMapImage() != null) {

					return new BlobResourceProvider(queryInstancePrint.getMapImage());
				}

			}

		}

		return null;
	}

	@Override
	public QueryRequestProcessor getQueryRequestProcessor(GeneralMapQueryInstance queryInstance, HttpServletRequest req, User user, User poster, URIParser uriParser, InstanceRequestMetadata requestMetadata) throws IOException {

		if (req.getParameter("mapimage") != null && queryInstance.getMapPrints() != null) {

			for (GeneralMapQueryInstancePrint queryInstancePrint : queryInstance.getMapPrints()) {

				if (queryInstancePrint.isUseInPreview() && queryInstancePrint.getMapImage() != null) {

					return new MapImageRequestProcessor("mapimage", queryInstancePrint.getMapImage());
				}

			}

		}

		return null;

	}

	@Override
	public List<SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settingDescriptors = new ArrayList<SettingDescriptor>();

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {

			settingDescriptors.addAll(superSettings);
		}

		try {

			ArrayList<ValueDescriptor> searchLMServiceDescriptors = new ArrayList<ValueDescriptor>();

			HighLevelQuery<SearchLMService> query = new HighLevelQuery<SearchLMService>();

			List<SearchLMService> searchLMServices = searchLMServiceDAO.getAll(query);

			if (searchLMServices != null) {

				for (SearchLMService searchLMService : searchLMServices) {

					searchLMServiceDescriptors.add(new ValueDescriptor(searchLMService.getName(), searchLMService.getSearchLMServiceID()));
				}

			} else {

				searchLMServiceDescriptors.add(new ValueDescriptor("No search services", -1));
			}

			settingDescriptors.add(SettingDescriptor.createDropDownSetting("searchLMServiceID", "Search LM service", "Search LM service being used when searching", false, null, searchLMServiceDescriptors));

		} catch (SQLException e) {

			log.error("Unable to get searchLM services from db", e);
		}

		try {

			ArrayList<ValueDescriptor> mapPrintDescriptors = new ArrayList<ValueDescriptor>();

			HighLevelQuery<PrintConfiguration> query = new HighLevelQuery<PrintConfiguration>(PrintConfiguration.MAPPRINTS_RELATION);

			List<PrintConfiguration> printConfigurations = printConfigurationDAO.getAll(query);

			if (printConfigurations != null) {

				for (PrintConfiguration printConfiguration : printConfigurations) {

					List<MapPrint> mapPrints = printConfiguration.getMapPrints();

					if (mapPrints != null) {

						for (MapPrint mapPrint : mapPrints) {

							mapPrintDescriptors.add(new ValueDescriptor(mapPrint.getName(), mapPrint.getMapPrintID()));
						}

					}

				}

			}

			if (mapPrintDescriptors.isEmpty()) {

				mapPrintDescriptors.add(new ValueDescriptor("No print configurations", -1));
			}

			settingDescriptors.add(SettingDescriptor.createMultiListSetting("mapPrintIDs", "Map print ids", "Map prints generated for query instances", false, null, mapPrintDescriptors));

		} catch (SQLException e) {

			log.error("Unable to get print configurations from db", e);
		}

		return settingDescriptors;
	}

	public Collection<MapPrint> getMapPrints() {

		return mapPrints != null ? mapPrints.values() : null;
	}

	public SearchLMService getSearchLMService() {

		return searchLMService;
	}

	@Override
	public Document createDocument(HttpServletRequest req, User poster) {

		Document doc = super.createDocument(req, poster);

		Element document = doc.getDocumentElement();

		XMLUtils.appendNewElement(doc, document, "mapScriptURL", openEMapScriptURL);

		if (searchLMService != null) {

			XMLUtils.appendNewElement(doc, document, "lmUser", searchLMService.getLmUser());
		}

		if (useSearchProxy) {

			XMLUtils.appendNewElement(doc, document, "searchURL", req.getContextPath() + getFullAlias() + "/search/");

		} else {

			if (searchLMService != null) {
				XMLUtils.appendNewElement(doc, document, "searchURL", req.getContextPath() + searchLMService.getSearchLMAddress() + "/");
			}
		}

		return doc;
	}

	//Required for old search service
	private String getUnescapedText(String text) {

		if (text != null) {

			if (useSearchLMServiceResponseEncodingFix) {

				text = ISO_TO_UTF8_STRING_DECODER.decode(text);
			}
		}

		return text;
	}

	private List<Geometry> populateGeometries(String[] geometries, WKTReader reader) throws ValidationException {

		List<Geometry> populatedGeometries = new ArrayList<Geometry>(geometries.length);

		for (String object : geometries) {

			String[] objectParts = object.split("#");

			String geometryStr = objectParts[0].trim();

			com.vividsolutions.jts.geom.Geometry geometry = populateGeometry(reader, geometryStr);

			String config = null;

			if (objectParts.length > 1) {

				config = objectParts[1].trim();

			}

			populatedGeometries.add(new Geometry(geometryStr, config, geometry));

		}

		return populatedGeometries;

	}

	private com.vividsolutions.jts.geom.Geometry populateGeometry(WKTReader reader, String geometry) throws ValidationException {

		try {

			return reader.read(geometry);

		} catch (Exception e) {

			throw new ValidationException(new ValidationError("GeometryNotValid"));

		}

	}

	public void generateMapImages(GeneralMapQueryInstance queryInstance, User user) throws ValidationException {

		if (queryInstance.getQuery().getMapPrints() != null) {

			log.info("Generating map images for queryInstance " + queryInstance + " for user " + user);

			List<com.vividsolutions.jts.geom.Geometry> geometries = queryInstance.getPrintableGeometries();

			JsonArray features = new JsonArray();

			String labelStyles = "";

			List<Coordinate> allCoordinates = new ArrayList<Coordinate>();

			if (!CollectionUtils.isEmpty(geometries)) {

				List<FeatureLabel> labels = new ArrayList<FeatureLabel>();

				GeometryFactory geometryFactory = new GeometryFactory();

				for (com.vividsolutions.jts.geom.Geometry geometry : geometries) {

					JsonObject properties = new JsonObject();
					properties.putField("_style", geometry.getGeometryType());

					JsonObject featureGeometry = new JsonObject();

					featureGeometry.putField("type", geometry.getGeometryType());

					Coordinate[] coordinates = geometry.getCoordinates();

					if (coordinates != null) {

						JsonArray coords = new JsonArray();

						if (coordinates.length > 1) {

							int count = 0;

							for (Coordinate coordinate : coordinates) {

								if (count < coordinates.length - 1) {

									LineString lineString = geometryFactory.createLineString(new Coordinate[] { coordinates[count], coordinates[count + 1] });

									Point centroid = lineString.getCentroid();

									labels.add(new FeatureLabel(UUID.randomUUID().toString(), centroid, NumberUtils.formatNumber(lineString.getLength(), 1, 1, false, true) + " m"));
								}

								JsonArray coord = new JsonArray();

								String x = NumberUtils.formatNumber(coordinate.x, 0, 1, false, true);
								String y = NumberUtils.formatNumber(coordinate.y, 0, 1, false, true);

								coord.addNode(x);
								coord.addNode(y);
								coords.addNode(coord);

								count++;
							}

							if (geometry.getGeometryType().equalsIgnoreCase("LineString")) {

								featureGeometry.putField("coordinates", coords);

							} else {

								JsonArray wrapper = new JsonArray();
								wrapper.addNode(coords);

								featureGeometry.putField("coordinates", wrapper);

							}

						} else if (coordinates.length == 1) {

							String x = NumberUtils.formatNumber(coordinates[0].x, 0, 1, false, true);
							String y = NumberUtils.formatNumber(coordinates[0].y, 0, 1, false, true);

							coords.addNode(x);
							coords.addNode(y);

							featureGeometry.putField("coordinates", coords);

						}

						allCoordinates.addAll(Arrays.asList(coordinates));

					}

					JsonObject feature = new JsonObject();
					feature.putField("type", "Feature");
					feature.putField("properties", properties);
					feature.putField("geometry", featureGeometry);

					features.addNode(feature);

				}

				if (!labels.isEmpty()) {

					StringBuilder labelStylesBuilder = new StringBuilder();

					for (FeatureLabel label : labels) {

						features.addNode(label.toJson());

						JsonObject labelStyle = new JsonObject();
						labelStyle.putField("label", label.getLabel());
						labelStyle.putField("strokeColor", label.getColor());
						labelStyle.putField("strokeWidth", label.getWidth() + "");
						labelStyle.putField("labelAlign", "cm");
						labelStyle.putField("fontSize", "9px");

						labelStyle.putField("strokeOpacity", 0);
						labelStyle.putField("fillOpacity", 0);

						labelStylesBuilder.append(",\"" + label.getId() + "\":" + labelStyle.toJson());

					}

					labelStyles = labelStylesBuilder.toString();

				}

			}

			Coordinate centerCoordinate = null;

			List<Double> coordinates = NumberUtils.toDouble(Arrays.asList(queryInstance.getExtent().trim().split(",")));

			if (coordinates == null || coordinates.size() != 4) {

				throw new ValidationException(new ValidationError("UnableToGeneratePNG"));

			}

			Envelope extent = new Envelope(coordinates.get(0), coordinates.get(2), coordinates.get(3), coordinates.get(1));

			if (!allCoordinates.isEmpty()) {

				centerCoordinate = calculateMapCentroid(allCoordinates);

			} else {

				centerCoordinate = extent.centre();

			}

			List<GeneralMapQueryInstancePrint> queryInstancePrints = new ArrayList<GeneralMapQueryInstancePrint>(queryInstance.getQuery().getMapPrints().size());

			for (GeneralMapQueryPrint generalMapQueryPrint : queryInstance.getQuery().getMapPrints()) {

				MapPrint mapPrint = generalMapQueryPrint.getMapPrint();

				String templateConfig = generalMapQueryPrint.getMapPrint().getPrintConfiguration().getPrintConfigTemplate();

				templateConfig = templateConfig.replace("$labelStyles", labelStyles);

				if (generalMapQueryPrint.getScale() != null) {

					templateConfig = templateConfig.replace("$center,", "center: [" + centerCoordinate.x + "," + centerCoordinate.y + "],");
					templateConfig = templateConfig.replace("$scale,", "scale: " + generalMapQueryPrint.getScale().toString() + ",");
					templateConfig = templateConfig.replace("$bbox,", "");

				} else {

					templateConfig = templateConfig.replace("$bbox,", "bbox: [" + extent.getMinX() + "," + extent.getMinY() + "," + extent.getMaxX() + "," + extent.getMaxY() + "],");
					templateConfig = templateConfig.replace("$scale,", "").replace("$center,", "");

				}

				templateConfig = templateConfig.replace("$features", features.toJson());
				templateConfig = templateConfig.replace("$srs", queryInstance.getEpsg());
				templateConfig = templateConfig.replace("$dpi", mapPrint.getResolution().getResolution().toString());
				templateConfig = templateConfig.replace("$layout", mapPrint.getLayout().getName());
				templateConfig = templateConfig.replace("$outputFormat", mapPrint.getOutputFormat().getFormat());

				String[] baseLayer = queryInstance.getVisibleBaseLayer().trim().split("#");

				if (baseLayer.length > 1) {
					templateConfig = templateConfig.replace("$baseLayer", baseLayer[1]);
				}

				if (logPrintConfig) {

					log.info("Print config for print " + mapPrint.getName() + ": " + templateConfig);
				}

				Blob mapImage = getMapImageFromMapFish(queryInstance, mapPrint, user, templateConfig);

				try {

					if (mapImage == null || mapImage.length() == 0) {

						throw new ValidationException(new ValidationError("UnableToGeneratePNG"));
					}

				} catch (SQLException e) {

					throw new ValidationException(new ValidationError("UnableToGeneratePNG"));
				}

				GeneralMapQueryInstancePrint queryInstancePrint = new GeneralMapQueryInstancePrint(generalMapQueryPrint, mapImage);

				queryInstancePrints.add(queryInstancePrint);

			}

			queryInstance.setMapPrints(queryInstancePrints);

		}

	}

	private Coordinate calculateMapCentroid(List<Coordinate> coordinates) {

		if (coordinates.size() == 1) {

			return coordinates.get(0);
		}

		CentroidArea area = new CentroidArea();

		Coordinate[] ring = coordinates.toArray(new Coordinate[coordinates.size()]);

		if (ring.length < 4) {

			return coordinates.get(0);
		}

		area.add(ring);

		return area.getCentroid();

	}

	private SerialBlob getMapImageFromMapFish(GeneralMapQueryInstance queryInstance, MapPrint mapPrint, User user, String config) throws ValidationException {

		ByteArrayOutputStream outputStream = null;

		try {

			StringReader reader = new StringReader(config);

			StringWriter writer = new StringWriter();

			String url = mapPrint.getPrintService().getPrintServiceAddress() + "/pdf/create.json";

			HTTPUtils.sendHTTPPostRequest(reader, new URL(url), writer, "UTF-8", mapFishConnectionTimeout, mapFishReadTimeout);

			String mapImageURL = writer.toString();

			if (mapImageURL != null) {

				mapImageURL = mapImageURL.substring(11, mapImageURL.length() - 2);

				if (HTTPUtils.isValidURL(mapImageURL)) {

					log.info("Generatated map image: " + mapImageURL + " for queryInstance " + queryInstance + " for user " + user);

					outputStream = new ByteArrayOutputStream();

					HTTPUtils.sendHTTPGetRequest(mapImageURL, null, outputStream);

					return new SerialBlob(outputStream.toByteArray());

				}

			}

			log.error("Invalid response from print service when generating png for queryInstance " + queryInstance + " for user " + user);

		} catch (SocketTimeoutException e) {

			log.log(mapFishTimeoutLogLevel.getLevel(), "Unable to generate png for queryInstance " + queryInstance + " for user " + user, e);

		} catch (Exception e) {

			log.log(mapFishErrorLogLevel.getLevel(), "Unable to generate png for queryInstance " + queryInstance + " for user " + user, e);

		} finally {

			CloseUtils.close(outputStream);

		}

		throw new ValidationException(new ValidationError("UnableToGeneratePNG"));

	}

	public MapConfiguration getMapConfiguration(Integer mapConfigurationID) throws SQLException {

		HighLevelQuery<MapConfiguration> mapConfigurationQuery = new HighLevelQuery<MapConfiguration>(MapConfiguration.MAPTOOLS_RELATION);

		mapConfigurationQuery.addParameter(mapConfigurationIDParamFactory.getParameter(mapConfigurationID));

		return mapConfigurationDAO.get(mapConfigurationQuery);
	}

	public MapConfiguration getMapConfiguration(String name, boolean checkEnabled) throws SQLException {

		HighLevelQuery<MapConfiguration> query = new HighLevelQuery<MapConfiguration>(MapConfiguration.MAPTOOLS_RELATION);

		query.addParameter(mapConfigurationNameParamFactory.getParameter(name));

		if (checkEnabled) {
			query.addParameter(mapConfigurationEnabledParamFactory.getParameter(true));
		}

		return mapConfigurationDAO.get(query);
	}

	public List<MapConfiguration> getEnabledMapConfigurations() throws SQLException {

		HighLevelQuery<MapConfiguration> query = new HighLevelQuery<MapConfiguration>(MapConfiguration.MAPTOOLS_RELATION);

		query.addParameter(mapConfigurationEnabledParamFactory.getParameter(true));

		return mapConfigurationDAO.getAll(query);
	}

	public MapPrint getMapPrint(String alias) throws SQLException {

		HighLevelQuery<MapPrint> query = new HighLevelQuery<MapPrint>();

		query.addParameter(mapPrintAliasParamFactory.getParameter(alias));

		return mapPrintDAO.get(query);
	}

	@Override
	protected Class<GeneralMapQueryInstance> getQueryInstanceClass() {

		return GeneralMapQueryInstance.class;
	}

	private String fixEncoding(String input) {

		if (URL_STRING_DECODER == null) {
			return input;
		}

		return URL_STRING_DECODER.decode(input);
	}

}
