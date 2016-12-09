package com.nordicpeak.flowengine.queries.checkboxquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToMany;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ExportableQueryInstance;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativeQueryUtils;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;

@Table(name = "checkbox_query_instances")
@XMLElement
public class CheckboxQueryInstance extends BaseQueryInstance implements FixedAlternativesQueryInstance, StringValueQueryInstance, ExportableQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field ALTERNATIVES_RELATION = ReflectionUtils.getField(CheckboxQueryInstance.class, "alternatives");
	public static final Field QUERY_RELATION = ReflectionUtils.getField(CheckboxQueryInstance.class, "query");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged
	@XMLElement
	private Integer minChecked;

	@DAOManaged
	@XMLElement
	private Integer maxChecked;

	@DAOManaged
	@XMLElement
	private String freeTextAlternativeValue;

	@DAOManaged(columnName="queryID")
	@ManyToOne
	@XMLElement
	private CheckboxQuery query;

	@DAOManaged
	@ManyToMany(linkTable="checkbox_query_instance_alternatives")
	@XMLElement(fixCase=true)
	private List<CheckboxAlternative> alternatives;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}


	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}


	public Integer getMinChecked() {

		return minChecked;
	}


	public void setMinChecked(Integer minChecked) {

		this.minChecked = minChecked;
	}


	public Integer getMaxChecked() {

		return maxChecked;
	}


	public void setMaxChecked(Integer maxChecked) {

		this.maxChecked = maxChecked;
	}


	@Override
	public String getFreeTextAlternativeValue() {
		return freeTextAlternativeValue;
	}


	public void setFreeTextAlternativeValue(String freeTextAlternativeValue) {
		this.freeTextAlternativeValue = freeTextAlternativeValue;
	}


	@Override
	public CheckboxQuery getQuery() {

		return query;
	}


	public void setQuery(CheckboxQuery query) {

		this.query = query;
	}


	@Override
	public List<CheckboxAlternative> getAlternatives() {

		return alternatives;
	}


	public void setAlternatives(List<CheckboxAlternative> alternatives) {

		this.alternatives = alternatives;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		this.alternatives = null;
		super.reset(attributeHandler);
	}

	public void copyQueryValues() {

		this.minChecked = query.getMinChecked();
		this.maxChecked = query.getMaxChecked();
	}

	@Override
	public String toString() {

		return "CheckboxQueryInstance (queryInstanceID=" + queryInstanceID + ")";
	}


	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		FixedAlternativeQueryUtils.appendExportXMLAlternatives(doc, element, this);

		return element;
	}


	@Override
	public String getStringValue() {

		if(alternatives == null && freeTextAlternativeValue == null){

			return null;

		}

		StringBuilder stringBuilder = new StringBuilder();

		if(alternatives != null){

			for(CheckboxAlternative alternative : alternatives){

				if(stringBuilder.length() != 0){

					stringBuilder.append(", ");
				}

				stringBuilder.append(alternative.getName());
			}
		}

		if(freeTextAlternativeValue != null){

			if(stringBuilder.length() != 0){

				stringBuilder.append(", ");
			}

			stringBuilder.append(freeTextAlternativeValue);
		}

		return stringBuilder.toString();
	}


	@Override
	public List<String> getExportValueLabels(QueryHandler queryHandler) {

		List<String> labels = new ArrayList<String>();

		if (!CollectionUtils.isEmpty(query.getAlternatives())) {

			for (CheckboxAlternative alternative : query.getAlternatives()) {

				labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + alternative.getName());
			}
		}

		if (!StringUtils.isEmpty(query.getFreeTextAlternative())) {

			labels.add(getQueryInstanceDescriptor().getQueryDescriptor().getName() + ": " + query.getFreeTextAlternative());
		}
		
		return labels;
	}

	@Override
	public List<String> getExportValues() {

		List<String> values = new ArrayList<String>();

		if (!CollectionUtils.isEmpty(query.getAlternatives())) {

			for (CheckboxAlternative alternative : query.getAlternatives()) {

				boolean isSet = false;

				if (!CollectionUtils.isEmpty(getAlternatives())) {

					for (CheckboxAlternative alternative2 : getAlternatives()) {

						if (alternative.getAlternativeID().equals(alternative2.getAlternativeID())) {

							isSet = true;
							break;
						}
					}
				}

				if (!isSet) {

					values.add(null);

				} else {

					values.add(alternative.getName());
				}
			}
		}

		if (!StringUtils.isEmpty(query.getFreeTextAlternative())) {

			String value = getFreeTextAlternativeValue();

			if (StringUtils.isEmpty(value)) {

				values.add(null);

			} else {

				values.add(value);
			}
		}

		return values;
	}
}
