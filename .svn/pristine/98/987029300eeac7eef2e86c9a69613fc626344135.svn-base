package com.nordicpeak.flowengine.queries.generalmapquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

import com.nordicpeak.flowengine.beans.QueryDescriptor;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryCRUD;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.MapConfiguration;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.MapPrint;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.MapTool;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.Scale;
import com.nordicpeak.flowengine.queries.generalmapquery.configuration.SearchLMService;

public class GeneralMapQueryCRUD extends BaseQueryCRUD<GeneralMapQuery, GeneralMapQueryProviderModule> {

	AnnotatedDAOWrapper<GeneralMapQuery, Integer> queryDAO;

	public GeneralMapQueryCRUD(AnnotatedDAOWrapper<GeneralMapQuery, Integer> queryDAO, GeneralMapQueryProviderModule callback) {

		super(GeneralMapQuery.class, queryDAO, new AnnotatedRequestPopulator<GeneralMapQuery>(GeneralMapQuery.class), "GeneralMapQuery", "general map query", null, callback);

		this.queryDAO = queryDAO;
	}

	@Override
	protected void appendUpdateFormData(GeneralMapQuery bean, Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser) throws Exception {

		super.appendUpdateFormData(bean, doc, updateTypeElement, user, req, uriParser);

		XMLUtils.append(doc, updateTypeElement, callback.getEnabledMapConfigurations());
		XMLUtils.append(doc, updateTypeElement, callback.getMapPrints());

		SearchLMService searchLMService = callback.getSearchLMService();

		if (searchLMService != null) {

			updateTypeElement.appendChild(searchLMService.toXML(doc));
		}

	}

	@Override
	protected GeneralMapQuery populateFromUpdateRequest(GeneralMapQuery bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		List<ValidationError> errors = new ArrayList<ValidationError>();

		GeneralMapQuery generalMapQuery = super.populateFromUpdateRequest(bean, req, user, uriParser);

		this.populateQueryDescriptor((QueryDescriptor) generalMapQuery.getQueryDescriptor(), req, errors);

		MapConfiguration mapConfiguration = null;
		
		Integer mapConfigurationID = NumberUtils.toInt(req.getParameter("mapConfigurationID"));
		
		if(mapConfigurationID == null) {
			
			errors.add(new ValidationError("NoMapConfigurationChoosen"));
		
		} else if((mapConfiguration = callback.getMapConfiguration(mapConfigurationID)) == null) {
			
			errors.add(new ValidationError("MapConfigurationNotFound"));
		}
		
		if(mapConfiguration == null) {
			
			throw new ValidationException(errors);
		}

		bean.setMapConfiguration(mapConfiguration);
		
		List<GeneralMapQueryTool> generalMapQueryTools = new ArrayList<GeneralMapQueryTool>();

		if (mapConfiguration.getMapTools() != null) {

			for (MapTool mapTool : mapConfiguration.getMapTools()) {

				String id = "tool_" + mapConfiguration.getMapConfigurationID() + "_" + mapTool.getToolID();

				if (req.getParameter(id) != null) {

					GeneralMapQueryTool generalMapQueryTool = getExistingGeneralMapQueryTool(generalMapQuery, mapTool.getToolID());

					if (generalMapQueryTool == null) {

						generalMapQueryTool = new GeneralMapQueryTool();
						generalMapQueryTool.setMapTool(mapTool);
					}

					generalMapQueryTool.setOnlyOneGeometry(req.getParameter(id + "_onlyOneGeometry") != null);

					String customTooltip = req.getParameter(id + "_tooltip");

					if (!StringUtils.isEmpty(customTooltip)) {

						if (customTooltip.length() > 255) {

							errors.add(new ValidationError(id + "_tooltip", mapTool.getTooltip(), ValidationErrorType.TooLong));

							continue;
						}
					}

					generalMapQueryTool.setTooltip(customTooltip);

					generalMapQueryTools.add(generalMapQueryTool);
				}

			}

		}

		generalMapQuery.setMapTools(generalMapQueryTools);

		Collection<MapPrint> mapPrints = callback.getMapPrints();

		List<GeneralMapQueryPrint> generalMapQueryPrints = new ArrayList<GeneralMapQueryPrint>();

		if (mapPrints != null) {

			Integer useInPreviewMapPrintID = NumberUtils.toInt(req.getParameter("useInPreview"));

			for (MapPrint mapPrint : mapPrints) {

				String id = "mapprint_" + mapPrint.getMapPrintID();

				GeneralMapQueryPrint generalMapQueryPrint = getExistingGeneralMapQueryPrint(generalMapQuery, mapPrint.getMapPrintID());

				if (generalMapQueryPrint == null) {

					generalMapQueryPrint = new GeneralMapQueryPrint();
					generalMapQueryPrint.setMapPrint(mapPrint);
				}

				if (useInPreviewMapPrintID != null && useInPreviewMapPrintID.equals(mapPrint.getMapPrintID())) {

					generalMapQueryPrint.setUseInPreview(true);

				} else {

					generalMapQueryPrint.setUseInPreview(false);
				}

				Integer scaleID = NumberUtils.toInt(req.getParameter(id + "_scale"));

				Integer scale = null;

				if (scaleID != null) {

					Scale requestedScale = getScale(mapPrint, scaleID);

					if (requestedScale != null) {

						scale = requestedScale.getScale();
					}

				}

				generalMapQueryPrint.setScale(scale);

				generalMapQueryPrints.add(generalMapQueryPrint);
			}

		}

		generalMapQuery.setMapPrints(generalMapQueryPrints);

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}

		return generalMapQuery;
	}

	@Override
	protected ForegroundModuleResponse beanUpdated(GeneralMapQuery bean, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		callback.cacheGeneralMapQueryConfiguration(bean);

		return super.beanUpdated(bean, req, res, user, uriParser);
	}

	@Override
	protected List<Field> getBeanRelations() {

		return Arrays.asList(GeneralMapQuery.MAPPRINTS_RELATION, GeneralMapQuery.MAPTOOLS_RELATION, GeneralMapQuery.MAPCONFIGURATION_RELATION, MapConfiguration.MAPTOOLS_RELATION, GeneralMapQueryTool.MAPTOOL_RELATION, GeneralMapQueryPrint.MAPPRINT_RELATION, MapPrint.OUTPUTFORMAT_RELATION);
	}

	private GeneralMapQueryTool getExistingGeneralMapQueryTool(GeneralMapQuery generalMapQuery, Integer toolID) {

		if (generalMapQuery.getMapTools() != null) {

			for (GeneralMapQueryTool generalMapQueryTool : generalMapQuery.getMapTools()) {

				if (generalMapQueryTool.getMapTool().getToolID().equals(toolID)) {

					return generalMapQueryTool;
				}

			}

		}

		return null;
	}

	private GeneralMapQueryPrint getExistingGeneralMapQueryPrint(GeneralMapQuery generalMapQuery, Integer mapPrintID) {

		if (generalMapQuery.getMapPrints() != null) {

			for (GeneralMapQueryPrint generalMapQueryPrint : generalMapQuery.getMapPrints()) {

				if (generalMapQueryPrint.getMapPrint().getMapPrintID().equals(mapPrintID)) {

					return generalMapQueryPrint;
				}

			}

		}

		return null;
	}

	private Scale getScale(MapPrint mapPrint, Integer scaleID) {

		if (mapPrint.getPrintService().getScales() != null) {

			for (Scale scale : mapPrint.getPrintService().getScales()) {

				if (scale.getScaleID().equals(scaleID)) {

					return scale;
				}

			}

		}

		return null;
	}

}
