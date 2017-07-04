package com.nordicpeak.flowengine.persondatasavinginformer.beans;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import se.unlogic.standardutils.annotations.PopulateOnlyIfSet;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "person_data_informer_settings")
@XMLElement
public class FlowFamilyInformerSetting extends GeneratedElementable implements Serializable {
	
	private static final long serialVersionUID = 5949025706273639735L;
	
	public static final Field DATA_ALTERNATIVES_RELATION = ReflectionUtils.getField(FlowFamilyInformerSetting.class, "dataAlternatives");
	public static final Field REASON_ALTERNATIVES_RELATION = ReflectionUtils.getField(FlowFamilyInformerSetting.class, "reasonAlternatives");
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer flowFamilyID;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean usesPersonData;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@RequiredIfSet(paramNames = "yearsSavedType", paramValues = "FINITE")
	@XMLElement
	private Integer yearsSaved;
	
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String reason;
	
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String extraInformation;
	
	@WebPopulate
	private boolean overrideComplaintDescription;
	
	@DAOManaged
	@PopulateOnlyIfSet(paramNames = "overrideComplaintDescription", paramValues = "true")
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String complaintDescription;
	
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
	
	public boolean isUsesPersonData() {
		return usesPersonData;
	}
	
	public void setUsesPersonData(boolean usesPersonData) {
		this.usesPersonData = usesPersonData;
	}
	
	public Integer getYearsSaved() {
		return yearsSaved;
	}
	
	public void setYearsSaved(Integer yearsSaved) {
		this.yearsSaved = yearsSaved;
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
	
}
