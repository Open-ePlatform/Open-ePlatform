package com.nordicpeak.flowengine.persondatasavinginformer.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import com.nordicpeak.flowengine.annotations.TextTagReplace;

import se.unlogic.standardutils.annotations.PopulateOnlyIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToMany;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "person_data_informer_settings")
@XMLElement
public class FlowFamilyInformerSetting extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = 5949025706273639735L;

	public static final Field DATA_ALTERNATIVES_RELATION = ReflectionUtils.getField(FlowFamilyInformerSetting.class, "dataAlternatives");
	public static final Field REASON_ALTERNATIVES_RELATION = ReflectionUtils.getField(FlowFamilyInformerSetting.class, "reasonAlternatives");
	public static final Field STORAGE_SETTINGS_RELATION = ReflectionUtils.getField(FlowFamilyInformerSetting.class, "storageSettings");

	@DAOManaged
	@Key
	@XMLElement
	private Integer flowFamilyID;

	@WebPopulate
	private boolean overrideReasonDescription;

	@TextTagReplace
	@DAOManaged
	@PopulateOnlyIfSet(paramNames = "overrideReasonDescription", paramValues = "true")
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String reason;

	@WebPopulate
	private boolean overrideExtraInformation;

	@TextTagReplace
	@DAOManaged
	@PopulateOnlyIfSet(paramNames = "overrideExtraInformation", paramValues = "true")
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String extraInformation;

	@WebPopulate
	private boolean overrideComplaintDescription;

	@TextTagReplace
	@DAOManaged
	@PopulateOnlyIfSet(paramNames = "overrideComplaintDescription", paramValues = "true")
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String complaintDescription;

	@WebPopulate
	private boolean overrideExtraInformationStorage;

	@TextTagReplace
	@DAOManaged
	@PopulateOnlyIfSet(paramNames = "overrideExtraInformationStorage", paramValues = "true")
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String extraInformationStorage;

	@WebPopulate
	private boolean overrideConfirmationText;

	@TextTagReplace
	@DAOManaged
	@PopulateOnlyIfSet(paramNames = "overrideConfirmationText", paramValues = "true")
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String confirmationText;

	@TextTagReplace
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String dataRecipient;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<InformerDataSettingStorage> storageSettings;

	@DAOManaged
	@ManyToMany(linkTable = "person_data_informer_settings_data_alternatives")
	@XMLElement(fixCase = true)
	private List<InformerDataAlternative> dataAlternatives;

	@DAOManaged
	@ManyToMany(linkTable = "person_data_informer_settings_reason_alternatives")
	@XMLElement(fixCase = true)
	private List<InformerReasonAlternative> reasonAlternatives;

	public Integer getFlowFamilyID() {

		return flowFamilyID;
	}

	public void setFlowFamilyID(Integer flowFamilyID) {

		this.flowFamilyID = flowFamilyID;
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

	public String getExtraInformationStorage() {

		return extraInformationStorage;
	}

	public void setExtraInformationStorage(String extraInformationStorage) {

		this.extraInformationStorage = extraInformationStorage;
	}

	public String getDataRecipient() {

		return dataRecipient;
	}

	public void setDataRecipient(String dataRecipient) {

		this.dataRecipient = dataRecipient;
	}

	public List<InformerDataAlternative> getDataAlternatives() {

		return dataAlternatives;
	}

	public void setDataAlternatives(List<InformerDataAlternative> dataAlternatives) {

		this.dataAlternatives = dataAlternatives;
	}

	public List<InformerReasonAlternative> getReasonAlternatives() {

		return reasonAlternatives;
	}

	public void setReasonAlternatives(List<InformerReasonAlternative> reasonAlternatives) {

		this.reasonAlternatives = reasonAlternatives;
	}

	public boolean isOverrideComplaintDescription() {

		return overrideComplaintDescription;
	}

	public void setOverrideComplaintDescription(boolean overrideComplaintDescription) {

		this.overrideComplaintDescription = overrideComplaintDescription;
	}

	public String getComplaintDescription() {

		return complaintDescription;
	}

	public void setComplaintDescription(String complaintDescription) {

		this.complaintDescription = complaintDescription;
	}

	public String getConfirmationText() {

		return confirmationText;
	}

	public void setConfirmationText(String confirmationText) {

		this.confirmationText = confirmationText;
	}

	public List<InformerDataSettingStorage> getStorageSettings() {

		return storageSettings;
	}

	public void setStorageSettings(List<InformerDataSettingStorage> storageSettings) {

		this.storageSettings = storageSettings;
	}
}