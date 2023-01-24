package com.nordicpeak.flowengine.persondatasavinginformer.query;

import java.lang.reflect.Field;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nordicpeak.flowengine.interfaces.ImmutableAlternative;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.persondatasavinginformer.beans.PersonDataInformerQueryInstanceSettingStorage;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativeQueryUtils;
import com.nordicpeak.flowengine.queries.fixedalternativesquery.FixedAlternativesQueryInstance;

import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

@Table(name = "person_data_informer_query_instances")
@XMLElement
public class PersonDataInformerQueryInstance extends BaseQueryInstance implements FixedAlternativesQueryInstance {

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(PersonDataInformerQueryInstance.class, "query");
	public static final Field SETTING_STORAGES_RELATION = ReflectionUtils.getField(PersonDataInformerQueryInstance.class, "settingStorages");

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private PersonDataInformerQuery query;

	@DAOManaged
	@XMLElement
	private boolean accepted;

	@DAOManaged
	@XMLElement
	private String reason;

	@DAOManaged
	@XMLElement
	private String extraInformation;

	@DAOManaged
	@XMLElement
	private String complaintDescription;

	@DAOManaged
	@XMLElement
	private String extraInformationStorage;

	@DAOManaged
	@XMLElement
	private String confirmationText;

	@DAOManaged
	@XMLElement
	private String dataRecipient;

	@DAOManaged
	@XMLElement
	private String ownerName;

	@DAOManaged
	@XMLElement
	private String ownerEmail;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<PersonDataInformerQueryInstanceSettingStorage> settingStorages;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public PersonDataInformerQuery getQuery() {

		return query;
	}

	public void setQuery(PersonDataInformerQuery query) {

		this.query = query;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		super.reset(attributeHandler);

		this.accepted = false;
	}

	public boolean isAccepted() {

		return accepted;
	}

	public void setAccepted(boolean accepted) {

		this.accepted = accepted;
	}

	public String getReason() {

		return reason;
	}

	public void setReason(String reason) {

		this.reason = reason;
	}

	public String getExtraInformation() {

		return extraInformation;
	}

	public void setExtraInformation(String extraInformation) {

		this.extraInformation = extraInformation;
	}

	public String getComplaintDescription() {

		return complaintDescription;
	}

	public void setComplaintDescription(String complaintDescription) {

		this.complaintDescription = complaintDescription;
	}

	public String getExtraInformationStorage() {

		return extraInformationStorage;
	}

	public void setExtraInformationStorage(String extraInformationStorage) {

		this.extraInformationStorage = extraInformationStorage;
	}

	public String getConfirmationText() {

		return confirmationText;
	}

	public void setConfirmationText(String confirmationText) {

		this.confirmationText = confirmationText;
	}

	public String getDataRecipient() {

		return dataRecipient;
	}

	public void setDataRecipient(String dataRecipient) {

		this.dataRecipient = dataRecipient;
	}

	public String getOwnerName() {

		return ownerName;
	}

	public void setOwnerName(String ownerName) {

		this.ownerName = ownerName;
	}

	public String getOwnerEmail() {

		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {

		this.ownerEmail = ownerEmail;
	}

	public List<PersonDataInformerQueryInstanceSettingStorage> getSettingStorages() {

		return settingStorages;
	}

	public void setSettingStorages(List<PersonDataInformerQueryInstanceSettingStorage> settingStorages) {

		this.settingStorages = settingStorages;
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + " (queryInstanceID=" + queryInstanceID + ")";
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		XMLUtils.appendNewElement(doc, element, "Accepted", accepted);

		FixedAlternativeQueryUtils.appendExportXMLAlternatives(doc, element, this);

		return element;
	}

	@Override
	public List<? extends ImmutableAlternative> getAlternatives() {

		if (accepted) {

			return query.getAlternatives();
		}

		return null;
	}

	@Override
	public String getFreeTextAlternativeValue() {

		return null;
	}

	public void defaultQueryValues() {

	}

	@Override
	public Element toXML(Document doc) {

		Element element = super.toXML(doc);

		element.appendChild(queryInstanceDescriptor.getQueryDescriptor().getStep().getFlow().getFlowFamily().toXML(doc));

		return element;
	}
}
