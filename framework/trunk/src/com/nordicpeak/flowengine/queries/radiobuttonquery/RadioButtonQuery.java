package com.nordicpeak.flowengine.queries.radiobuttonquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.w3c.dom.Document;

import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.beans.BasePaymentProduct;
import com.nordicpeak.flowengine.interfaces.PaymentProduct;
import com.nordicpeak.flowengine.interfaces.PaymentQuery;
import com.nordicpeak.flowengine.interfaces.PricedAlternative;
import com.nordicpeak.flowengine.queries.checkboxquery.Columns;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesBaseQuery;

@Table(name = "radio_button_queries")
@XMLElement
public class RadioButtonQuery extends FixedAlternativesBaseQuery implements PaymentQuery {

	private static final long serialVersionUID = -842191226937409416L;

	public static final Field ALTERNATIVES_RELATION = ReflectionUtils.getField(RadioButtonQuery.class, "alternatives");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean searchable;

	@DAOManaged
	@OneToMany(autoUpdate = true, autoAdd = true)
	@XMLElement(fixCase = true)
	private List<RadioButtonAlternative> alternatives;

	@WebPopulate(maxLength = 255)
	@DAOManaged
	@XMLElement
	private String freeTextAlternative;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean setAsAttribute;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@RequiredIfSet(paramNames = "setAsAttribute")
	@XMLElement
	private String attributeName;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<RadioButtonQueryInstance> instances;

	@DAOManaged
	@WebPopulate(maxLength = 10, required = true)
	@XMLElement
	private Columns columns = Columns.ONE;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideTitle;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideDescriptionInPDF;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean lockForManagerUpdate;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean lockForOwnerUpdate;
	
	@Override
	public Integer getQueryID() {

		return queryID;
	}

	@Override
	public List<RadioButtonAlternative> getAlternatives() {

		return alternatives;
	}

	public List<RadioButtonQueryInstance> getInstances() {

		return instances;
	}

	public void setInstances(List<RadioButtonQueryInstance> instances) {

		this.instances = instances;
	}

	public void setQueryID(int queryID) {

		this.queryID = queryID;
	}

	public boolean isSearchable() {

		return searchable;
	}

	public void setSearchable(boolean searchable) {

		this.searchable = searchable;
	}

	public void setAlternatives(List<RadioButtonAlternative> alternatives) {

		this.alternatives = alternatives;
	}

	@Override
	public String getFreeTextAlternative() {

		return freeTextAlternative;
	}

	public void setFreeTextAlternative(String freeTextAlternative) {

		this.freeTextAlternative = freeTextAlternative;
	}

	public Columns getColumns() {

		return columns;
	}

	public void setColumns(Columns columns) {

		this.columns = columns;
	}

	@Override
	public String toString() {

		if (this.queryDescriptor != null) {

			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}

		return "RadioButtonQuery (queryID: " + queryID + ")";
	}

	@Override
	public String getXSDTypeName() {

		return "RadioButtonQuery" + queryID;
	}

	@Override
	public void toXSD(Document doc) {

		toXSD(doc, 1);
	}

	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {

		List<ValidationError> errors = new ArrayList<>();

		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		freeTextAlternative = XMLValidationUtils.validateParameter("freeTextAlternative", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		hideTitle = xmlParser.getPrimitiveBoolean("hideTitle");
		hideDescriptionInPDF = xmlParser.getPrimitiveBoolean("hideDescriptionInPDF");
		lockForManagerUpdate = xmlParser.getPrimitiveBoolean("lockForManagerUpdate");
		lockForOwnerUpdate = xmlParser.getPrimitiveBoolean("lockForOwnerUpdate");

		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		if (attributeName != null) {

			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
		}

		alternatives = RadioButtonQueryCRUD.ALTERNATIVES_POPLATOR.populate(xmlParser, errors);

		if (!StringUtils.isEmpty(xmlParser.getString("columns"))) {

			columns = XMLValidationUtils.validateParameter("columns", xmlParser, true, new EnumPopulator<Columns>(Columns.class), errors);
		}

		if (!errors.isEmpty()) {

			throw new ValidationException(errors);
		}
	}

	public boolean isSetAsAttribute() {

		return setAsAttribute;
	}

	public void setSetAsAttribute(boolean setAsAttribute) {

		this.setAsAttribute = setAsAttribute;
	}

	public String getAttributeName() {

		return attributeName;
	}

	public void setAttributeName(String attributeName) {

		this.attributeName = attributeName;
	}

	public boolean isHideTitle() {

		return hideTitle;
	}

	public void setHideTitle(boolean hideTitle) {

		this.hideTitle = hideTitle;
	}
	
	public boolean isHideDescriptionInPDF() {

		return hideDescriptionInPDF;
	}

	public void setHideDescriptionInPDF(boolean hideDescriptionInPDF) {

		this.hideDescriptionInPDF = hideDescriptionInPDF;
	}

	public boolean isLockForManagerUpdate() {

		return lockForManagerUpdate;
	}

	public void setLockForManagerUpdate(boolean lockForManagerUpdate) {

		this.lockForManagerUpdate = lockForManagerUpdate;
	}

	public boolean isLockForOwnerUpdate() {
		return lockForOwnerUpdate;
	}

	public void setLockForOwnerUpdate(boolean lockForOwnerUpdate) {
		this.lockForOwnerUpdate = lockForOwnerUpdate;
	}

	@Override
	public List<? extends PaymentProduct> getPaymentProducts() {

		if (alternatives != null) {

			List<PaymentProduct> paymentProducts = new ArrayList<>();

			for (PricedAlternative alternative : alternatives) {

				if (alternative.getPrice() != null && alternative.getPrice() > 0 && alternative.getName() != null) {

					paymentProducts.add(new BasePaymentProduct(alternative.getPrice(), alternative.getName()));
				}
			}

			return paymentProducts;
		}

		return Collections.emptyList();
	}

}
