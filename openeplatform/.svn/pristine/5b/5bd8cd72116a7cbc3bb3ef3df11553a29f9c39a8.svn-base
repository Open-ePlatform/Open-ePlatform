package com.nordicpeak.flowengine.beans;

import java.io.Serializable;
import java.sql.Date;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateStringyfier;
import se.unlogic.standardutils.object.ObjectUtils;
import se.unlogic.standardutils.populators.PastDatePopulator;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "flowengine_flow_family_management_info")
@XMLElement
public class ManagementInfo extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -4438233084038975025L;

	@DAOManaged
	@Key
	@XMLElement
	private Integer flowFamilyID;

	@DAOManaged
	@OneToOne
	@XMLElement
	private FlowFamily flowFamily;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String processOwner;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String flowResponsible;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String informationResponsible;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String status;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String organization;

	@DAOManaged
	@WebPopulate(populator = PastDatePopulator.class)
	@XMLElement(valueFormatter = DateStringyfier.class)
	private Date lastReviewed;

	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@XMLElement
	private String aboutFlow;

	@Override
	public String toString() {

		return "(flowFamily=" + flowFamily + ")";
	}

	public boolean isPopulated() {

		return !ObjectUtils.isNull(processOwner, flowResponsible, informationResponsible, status, organization, lastReviewed, aboutFlow);
	}

	public Integer getFlowFamilyID() {

		return flowFamilyID;
	}

	public void setFlowFamilyID(Integer flowFamilyID) {

		this.flowFamilyID = flowFamilyID;
	}

	public FlowFamily getFlowFamily() {

		return flowFamily;
	}

	public void setFlowFamily(FlowFamily flowFamily) {

		this.flowFamily = flowFamily;
	}

	public String getProcessOwner() {

		return processOwner;
	}

	public void setProcessOwner(String processOwner) {

		this.processOwner = processOwner;
	}

	public String getFlowResponsible() {

		return flowResponsible;
	}

	public void setFlowResponsible(String flowResponsible) {

		this.flowResponsible = flowResponsible;
	}

	public String getInformationResponsible() {

		return informationResponsible;
	}

	public void setInformationResponsible(String informationResponsible) {

		this.informationResponsible = informationResponsible;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus(String status) {

		this.status = status;
	}

	public String getOrganization() {

		return organization;
	}

	public void setOrganization(String organization) {

		this.organization = organization;
	}

	public Date getLastReviewed() {

		return lastReviewed;
	}

	public void setLastReviewed(Date lastReviewed) {

		this.lastReviewed = lastReviewed;
	}

	public String getAboutFlow() {

		return aboutFlow;
	}

	public void setAboutFlow(String aboutFlow) {

		this.aboutFlow = aboutFlow;
	}

}
