package com.nordicpeak.flowengine.queries.generalmapquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;
import se.unlogic.webutils.annotations.URLRewrite;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.MapConfiguration;

@Table(name = "general_map_queries")
@XMLElement
public class GeneralMapQuery extends BaseQuery {

	private static final long serialVersionUID = 8034865424806501988L;

	public static final Field MAPTOOLS_RELATION = ReflectionUtils.getField(GeneralMapQuery.class, "mapTools");
	public static final Field MAPPRINTS_RELATION = ReflectionUtils.getField(GeneralMapQuery.class, "mapPrints");
	public static final Field MAPCONFIGURATION_RELATION = ReflectionUtils.getField(GeneralMapQuery.class, "mapConfiguration");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 1000)
	@XMLElement
	private String startInstruction;

	@TextTagReplace
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 255, required = true)
	@XMLElement
	private String requiredQueryMessage;

	@DAOManaged
	@WebPopulate(populator = IntegerPopulator.class)
	@XMLElement
	private Integer minimalDrawingScale;

	@DAOManaged
	@RequiredIfSet(paramNames = "minimalDrawingScale")
	@WebPopulate(maxLength = 65536)
	@XMLElement
	private String incorrectDrawingMessage;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean allowOnlyOneGeometry;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean forceQueryPopulation;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<GeneralMapQueryInstance> instances;

	@DAOManaged
	@OneToMany(autoAdd = true, autoUpdate = true)
	@XMLElement
	private List<GeneralMapQueryTool> mapTools;

	@DAOManaged
	@OneToMany(autoAdd = true, autoUpdate = true)
	@XMLElement
	private List<GeneralMapQueryPrint> mapPrints;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean pudSearchEnabled;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean addressSearchEnabled;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean placeSearchEnabled;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean coordinateSearchEnabled;

	@DAOManaged(columnName = "mapConfigurationID")
	@ManyToOne
	@XMLElement
	private MapConfiguration mapConfiguration;

	@XMLElement
	private String mapConfigurationName;

	@Override
	public Integer getQueryID() {

		return queryID;
	}

	public String getStartInstruction() {

		return startInstruction;
	}

	public void setStartInstruction(String startInstruction) {

		this.startInstruction = startInstruction;
	}

	public String getRequiredQueryMessage() {

		return requiredQueryMessage;
	}

	public void setRequiredQueryMessage(String requiredQueryMessage) {

		this.requiredQueryMessage = requiredQueryMessage;
	}

	public List<GeneralMapQueryTool> getMapTools() {

		return mapTools;
	}

	public void setMapTools(List<GeneralMapQueryTool> mapTools) {

		this.mapTools = mapTools;
	}

	public List<GeneralMapQueryPrint> getMapPrints() {

		return mapPrints;
	}

	public void setMapPrints(List<GeneralMapQueryPrint> mapPrints) {

		this.mapPrints = mapPrints;
	}

	public Integer getMinimalDrawingScale() {

		return minimalDrawingScale;
	}

	public void setMinimalDrawingScale(Integer minimalDrawingScale) {

		this.minimalDrawingScale = minimalDrawingScale;
	}

	public String getIncorrectDrawingMessage() {

		return incorrectDrawingMessage;
	}

	public void setIncorrectDrawingMessage(String incorrectDrawingMessage) {

		this.incorrectDrawingMessage = incorrectDrawingMessage;
	}

	public boolean isAllowOnlyOneGeometry() {

		return allowOnlyOneGeometry;
	}

	public void setAllowOnlyOneGeometry(boolean allowOnlyOneGeometry) {

		this.allowOnlyOneGeometry = allowOnlyOneGeometry;
	}

	public boolean isPudSearchEnabled() {

		return pudSearchEnabled;
	}

	public void setPudSearchEnabled(boolean pudSearchEnabled) {

		this.pudSearchEnabled = pudSearchEnabled;
	}

	public boolean isAddressSearchEnabled() {

		return addressSearchEnabled;
	}

	public void setAddressSearchEnabled(boolean addressSearchEnabled) {

		this.addressSearchEnabled = addressSearchEnabled;
	}

	public boolean isPlaceSearchEnabled() {

		return placeSearchEnabled;
	}

	public void setPlaceSearchEnabled(boolean placeSearchEnabled) {

		this.placeSearchEnabled = placeSearchEnabled;
	}

	public boolean isCoordinateSearchEnabled() {

		return coordinateSearchEnabled;
	}

	public void setCoordinateSearchEnabled(boolean coordinateSearchEnabled) {

		this.coordinateSearchEnabled = coordinateSearchEnabled;
	}

	public void setQueryID(Integer queryID) {

		this.queryID = queryID;
	}

	public MapConfiguration getMapConfiguration() {

		return mapConfiguration;
	}

	public String getMapConfigurationName() {

		return mapConfigurationName;
	}

	public void setMapConfigurationName(String mapConfigurationName) {

		this.mapConfigurationName = mapConfigurationName;
	}

	public void setMapConfiguration(MapConfiguration mapConfiguration) {

		this.mapConfiguration = mapConfiguration;
	}

	public List<GeneralMapQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<GeneralMapQueryInstance> instances) {

		this.instances = instances;
	}

	
	public boolean isForceQueryPopulation() {
	
		return forceQueryPopulation;
	}

	
	public void setForceQueryPopulation(boolean forceQueryPopulation) {
	
		this.forceQueryPopulation = forceQueryPopulation;
	}

	@Override
	public String toString() {

		if (this.queryDescriptor != null) {

			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}

		return "GeneralMapQuery (queryID: " + queryID + ")";
	}

	@Override
	public String getXSDTypeName() {

		return "GeneralMapQuery" + getQueryID();
	}

	@Override
	public void toXSD(Document doc) {

		Element complexTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexType");
		complexTypeElement.setAttribute("name", getXSDTypeName());

		Element complexContentElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexContent");
		complexTypeElement.appendChild(complexContentElement);

		Element extensionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:extension");
		extensionElement.setAttribute("base", "Query");
		complexContentElement.appendChild(extensionElement);

		Element sequenceElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:sequence");
		extensionElement.appendChild(sequenceElement);

		Element nameElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		nameElement.setAttribute("name", "Name");
		nameElement.setAttribute("type", "xs:string");
		nameElement.setAttribute("minOccurs", "1");
		nameElement.setAttribute("maxOccurs", "1");
		nameElement.setAttribute("fixed", queryDescriptor.getName());
		sequenceElement.appendChild(nameElement);

		appendFieldDefinition("Extent", "xs:string", true, doc, sequenceElement);
		appendFieldDefinition("EPSG", "xs:string", true, doc, sequenceElement);
		appendFieldDefinition("VisibleBaseLayer", "xs:string", true, doc, sequenceElement);

		Element fieldElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		fieldElement.setAttribute("name", "Geometry");
		fieldElement.setAttribute("type", "xs:string");
		fieldElement.setAttribute("minOccurs", "0");
		fieldElement.setAttribute("maxOccurs", "unbounded");

		sequenceElement.appendChild(fieldElement);

		doc.getDocumentElement().appendChild(complexTypeElement);
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		startInstruction = xmlParser.getString("startInstruction");
		requiredQueryMessage = XMLValidationUtils.validateParameter("requiredQueryMessage", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		minimalDrawingScale = XMLValidationUtils.validateParameter("minimalDrawingScale", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		incorrectDrawingMessage = XMLValidationUtils.validateParameter("incorrectDrawingMessage", xmlParser, minimalDrawingScale != null, 1, 65536, StringPopulator.getPopulator(), errors);

		allowOnlyOneGeometry = xmlParser.getPrimitiveBoolean("allowOnlyOneGeometry");
		pudSearchEnabled = xmlParser.getPrimitiveBoolean("pudSearchEnabled");
		addressSearchEnabled = xmlParser.getPrimitiveBoolean("addressSearchEnabled");
		placeSearchEnabled = xmlParser.getPrimitiveBoolean("placeSearchEnabled");
		coordinateSearchEnabled = xmlParser.getPrimitiveBoolean("coordinateSearchEnabled");

		List<XMLParser> mapToolsParser = xmlParser.getNodes("mapTools/GeneralMapQueryTool");

		if (mapToolsParser != null) {

			List<GeneralMapQueryTool> mapTools = new ArrayList<GeneralMapQueryTool>();

			for (XMLParser parser : mapToolsParser) {

				GeneralMapQueryTool tool = new GeneralMapQueryTool();
				tool.populate(parser);
				mapTools.add(tool);
			}

			this.mapTools = mapTools;
		}

		List<XMLParser> mapPrintParser = xmlParser.getNodes("mapPrints/GeneralMapQueryPrint");

		if (mapPrintParser != null) {

			List<GeneralMapQueryPrint> mapPrints = new ArrayList<GeneralMapQueryPrint>();

			for (XMLParser parser : mapPrintParser) {

				GeneralMapQueryPrint print = new GeneralMapQueryPrint();
				print.populate(parser);
				mapPrints.add(print);
			}

			this.mapPrints = mapPrints;
		}

		XMLParser mapConfigurationParser = xmlParser.getNode("MapConfiguration");

		if (mapConfigurationParser != null) {

			mapConfigurationName = mapConfigurationParser.getString("name");

		} else {

			errors.add(new ValidationError("NoMapConfigurationSet"));
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

	}

	protected void appendFieldDefinition(String name, String type, boolean required, Document doc, Element sequenceElement) {

		Element fieldElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		fieldElement.setAttribute("name", name);
		fieldElement.setAttribute("type", "xs:string");
		fieldElement.setAttribute("minOccurs", required ? "1" : "0");
		fieldElement.setAttribute("maxOccurs", "1");

		sequenceElement.appendChild(fieldElement);
	}

}
