package com.nordicpeak.flowengine.queries.checkboxpaymentquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToMany;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.beans.BaseInvoiceLine;
import com.nordicpeak.flowengine.interfaces.PaymentQuery;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;

@Table(name = "checkbox_payment_query_instances")
@XMLElement
public class CheckboxPaymentQueryInstance extends BaseQueryInstance implements FixedAlternativesQueryInstance, PaymentQuery, StringValueQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field ALTERNATIVES_RELATION = ReflectionUtils.getField(CheckboxPaymentQueryInstance.class, "alternatives");
	public static final Field QUERY_RELATION = ReflectionUtils.getField(CheckboxPaymentQueryInstance.class, "query");

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

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private CheckboxPaymentQuery query;

	@DAOManaged
	@ManyToMany(linkTable = "checkbox_payment_query_instance_alternatives")
	@XMLElement(fixCase = true)
	private List<CheckboxPaymentAlternative> alternatives;

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

	public CheckboxPaymentQuery getQuery() {

		return query;
	}

	public void setQuery(CheckboxPaymentQuery query) {

		this.query = query;
	}

	@Override
	public List<CheckboxPaymentAlternative> getAlternatives() {

		return alternatives;
	}

	public void setAlternatives(List<CheckboxPaymentAlternative> alternatives) {

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
	public List<BaseInvoiceLine> getInvoiceLines() {

		if(alternatives != null) {

			List<BaseInvoiceLine> invoiceLines = new ArrayList<BaseInvoiceLine>();

			for(CheckboxPaymentAlternative alternative : alternatives) {

				invoiceLines.add(new BaseInvoiceLine(1, alternative.getAmount(), alternative.getDescription(), ""));
			}

			return invoiceLines;
		}

		return null;

	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) {

		// TODO XML export support
		return null;
	}

	@Override
	public String getStringValue() {

		if(alternatives == null && freeTextAlternativeValue == null){

			return null;

		}

		StringBuilder stringBuilder = new StringBuilder();

		if(alternatives != null){

			for(CheckboxPaymentAlternative alternative : alternatives){

				if(stringBuilder.length() != 0){

					stringBuilder.append(", ");
				}

				stringBuilder.append(alternative.getName());
				
				if(alternative.getAmount() != null){
					
					stringBuilder.append(" (" + alternative.getAmount() + ")");
				}
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
}
