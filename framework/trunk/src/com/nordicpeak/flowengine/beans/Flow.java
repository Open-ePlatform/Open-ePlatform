package com.nordicpeak.flowengine.beans;

import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.FCKContent;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.standardutils.annotations.NoDuplicates;
import se.unlogic.standardutils.annotations.PopulateOnlyIfSet;
import se.unlogic.standardutils.annotations.RequiredIfNotSet;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.base64.Base64;
import se.unlogic.standardutils.beans.Named;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateStringyfier;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.string.StringTag;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGeneratorDocument;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLParserPopulateable;
import se.unlogic.standardutils.xml.XMLPopulationUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLValidationUtils;
import se.unlogic.webutils.annotations.URLRewrite;
import se.unlogic.webutils.populators.StringHTTPURLPopulator;
import se.unlogic.webutils.url.URLRewriter;

import com.nordicpeak.flowengine.annotations.TextTagReplace;
import com.nordicpeak.flowengine.interfaces.Icon;
import com.nordicpeak.flowengine.interfaces.ImmutableFlow;
import com.nordicpeak.flowengine.utils.TextTagReplacer;

@Table(name = "flowengine_flows")
@XMLElement
public class Flow extends GeneratedElementable implements ImmutableFlow, XMLParserPopulateable, Named, Icon{
	
	
	private static final long serialVersionUID = -1533312692687401406L;
	
	public static final Field DEFAULT_FLOW_STATE_MAPPINGS_RELATION = ReflectionUtils.getField(Flow.class, "defaultFlowStateMappings");
	public static final Field FLOW_TYPE_RELATION = ReflectionUtils.getField(Flow.class, "flowType");
	public static final Field CATEGORY_RELATION = ReflectionUtils.getField(Flow.class, "category");
	public static final Field FLOW_FAMILY_RELATION = ReflectionUtils.getField(Flow.class, "flowFamily");
	public static final Field STEPS_RELATION = ReflectionUtils.getField(Flow.class, "steps");
	public static final Field STATUSES_RELATION = ReflectionUtils.getField(Flow.class, "statuses");
	public static final Field TAGS_RELATION = ReflectionUtils.getField(Flow.class, "tags");
	public static final Field CHECKS_RELATION = ReflectionUtils.getField(Flow.class, "checks");
	public static final Field FLOW_INSTANCES_RELATION = ReflectionUtils.getField(Flow.class, "flowInstances");
	public static final Field FLOW_FORMS_RELATION = ReflectionUtils.getField(Flow.class, "flowForms");
	
	public static final Field ICON_BLOB_FIELD = ReflectionUtils.getField(Flow.class, "icon");
	public static final Field DESCRIPTION_SHORT_FIELD = ReflectionUtils.getField(Flow.class, "shortDescription");
	public static final Field DESCRIPTION_LONG_FIELD = ReflectionUtils.getField(Flow.class, "longDescription");
	public static final Field SUBMITTED_MESSAGE_FIELD = ReflectionUtils.getField(Flow.class, "submittedMessage");
	public static final Field PUBLISH_DATE_FIELD = ReflectionUtils.getField(Flow.class, "publishDate");
	public static final Field USE_PREVIEW_FIELD = ReflectionUtils.getField(Flow.class, "usePreview");
	public static final Field ICON_FILE_NAME_FIELD = ReflectionUtils.getField(Flow.class, "iconFileName");
	public static final Field REQUIRE_AUTHENTICATION_FIELD = ReflectionUtils.getField(Flow.class, "requireAuthentication");
	public static final Field REQUIRES_SIGNING_FIELD = ReflectionUtils.getField(Flow.class, "requireSigning");
	public static final Field SHOW_SUBMIT_SURVEY_FIELD = ReflectionUtils.getField(Flow.class, "showSubmitSurvey");
	public static final Field HIDE_SUBMIT_STEP_TEXT_FIELD = ReflectionUtils.getField(Flow.class, "hideSubmitStepText");
	public static final Field FLOW_FORMS_FIELD = ReflectionUtils.getField(Flow.class, "flowForms");
	public static final Field HIDE_MANAGER_DETAILS_FIELD = ReflectionUtils.getField(Flow.class, "hideManagerDetails");
	public static final Field HIDE_FROM_OVERVIEW_FIELD = ReflectionUtils.getField(Flow.class, "hideFromOverview");
	public static final Field HIDE_INTERNAL_MESSAGES_FIELD = ReflectionUtils.getField(Flow.class, "hideInternalMessages");
	
	@DAOManaged(autoGenerated = true)
	@Key
	@XMLElement
	private Integer flowID;
	
	@StringTag
	@DAOManaged
	@OrderBy
	@WebPopulate(required = true, maxLength = 255)
	@XMLElement
	private String name;
	
	@DAOManaged
	@StringTag
	@XMLElement
	private Integer version;
	
	@FCKContent
	@TextTagReplace
	@StringTag
	@DAOManaged
	@URLRewrite
	@WebPopulate(required = true, maxLength = 65535)
	private String shortDescription;
	
	@FCKContent
	@TextTagReplace
	@StringTag
	@DAOManaged
	@URLRewrite
	@WebPopulate(maxLength = 16777215)
	@RequiredIfNotSet(paramNames = "skipOverview")
	private String longDescription;
	
	@FCKContent
	@TextTagReplace
	@DAOManaged
	@URLRewrite
	@WebPopulate(maxLength = 16777215)
	@RequiredIfSet(paramNames = "typeOfFlow", paramValues = "INTERNAL")
	private String submittedMessage;
	
	@DAOManaged
	@XMLElement
	private String iconFileName;
	
	@DAOManaged
	private Timestamp iconLastModified;
	
	@DAOManaged
	private transient Blob icon;
	
	@StringTag
	@DAOManaged
	@WebPopulate
	@RequiredIfSet(paramNames = "unPublishDate")
	@XMLElement(valueFormatter = DateStringyfier.class)
	private Date publishDate;
	
	@StringTag
	@DAOManaged
	@WebPopulate
	@XMLElement(valueFormatter = DateStringyfier.class)
	private Date unPublishDate;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean enabled;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean usePreview;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean paymentSupportEnabled;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireAuthentication;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireSigning;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean showSubmitSurvey;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideSubmitStepText;
	
	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_tags", remoteValueColumnName = "tag")
	@WebPopulate(maxLength = 255)
	@NoDuplicates
	@SplitOnLineBreak
	@XMLElement(fixCase = true, childName = "tag")
	private List<String> tags;
	
	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_checks", remoteValueColumnName = "value", preserveListOrder=true, indexColumn="checkIndex")
	@WebPopulate(maxLength = 255)
	@NoDuplicates
	@SplitOnLineBreak
	@XMLElement(fixCase = true, childName = "check")
	private List<String> checks;
	
	@DAOManaged(columnName = "flowTypeID")
	@ManyToOne
	@XMLElement
	private FlowType flowType;
	
	@DAOManaged(columnName = "categoryID")
	@ManyToOne
	@XMLElement
	private Category category;
	
	@DAOManaged(columnName = "flowFamilyID")
	@ManyToOne
	@XMLElement
	private FlowFamily flowFamily;
	
	@DAOManaged
	@WebPopulate(populator = StringHTTPURLPopulator.class, maxLength = 1024)
	@PopulateOnlyIfSet(paramNames = { "typeOfFlow" }, paramValues = { "EXTERNAL" })
	@RequiredIfSet(paramNames = "typeOfFlow", paramValues = "EXTERNAL")
	@XMLElement
	private String externalLink;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean skipOverview;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideManagerDetails;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideFromOverview;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideInternalMessages;
	
	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<Status> statuses;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<DefaultStatusMapping> defaultFlowStateMappings;
	
	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<Step> steps;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<FlowInstance> flowInstances;
	
	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<FlowForm> flowForms;
	
	@XMLElement
	private Integer flowInstanceCount;
	
	@XMLElement
	private Integer flowSubmittedInstanceCount;
	
	@XMLElement
	private Boolean latestVersion;
	
	@XMLElement
	private Boolean popular;
	
	private boolean hasTextTags;
	
	private boolean hasFileURLs;
	
	private boolean hasRelativeURLs;
	
	@Override
	public Integer getFlowID() {
		
		return flowID;
	}
	
	public void setFlowID(Integer flowID) {
		
		this.flowID = flowID;
	}
	
	@Override
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		
		this.name = name;
	}
	
	@Override
	public Category getCategory() {
		
		return category;
	}
	
	public void setCategory(Category category) {
		
		this.category = category;
	}
	
	@Override
	public List<Status> getStatuses() {
		
		return statuses;
	}
	
	public void setStatuses(List<Status> flowStates) {
		
		this.statuses = flowStates;
	}
	
	@Override
	public List<Step> getSteps() {
		
		return steps;
	}
	
	public void setSteps(List<Step> steps) {
		
		this.steps = steps;
	}
	
	public List<FlowInstance> getFlowInstances() {
		
		return flowInstances;
	}
	
	public void setFlowInstances(List<FlowInstance> flowInstances) {
		
		this.flowInstances = flowInstances;
	}
	
	public String getShortDescription() {
		
		return shortDescription;
	}
	
	public void setShortDescription(String shortDescription) {
		
		this.shortDescription = shortDescription;
	}
	
	public String getLongDescription() {
		
		return longDescription;
	}
	
	public void setLongDescription(String longDescription) {
		
		this.longDescription = longDescription;
	}
	
	@Override
	public String getIconFileName() {
		
		return iconFileName;
	}
	
	public void setIconFileName(String iconFileName) {
		
		this.iconFileName = iconFileName;
	}
	
	@Override
	public Blob getIcon() {
		
		return icon;
	}
	
	public void setIcon(Blob icon) {
		
		this.icon = icon;
	}
	
	public Date getPublishDate() {
		
		return publishDate;
	}
	
	public void setPublishDate(Date publishDate) {
		
		this.publishDate = publishDate;
	}
	
	public Date getUnPublishDate() {
		
		return unPublishDate;
	}
	
	public void setUnPublishDate(Date unPublishDate) {
		
		this.unPublishDate = unPublishDate;
	}
	
	@Override
	public FlowType getFlowType() {
		
		return flowType;
	}
	
	public void setFlowType(FlowType flowType) {
		
		this.flowType = flowType;
	}
	
	@Override
	public boolean isEnabled() {
		
		return enabled;
	}
	
	public void setEnabled(boolean disabled) {
		
		this.enabled = disabled;
	}
	
	@Override
	public boolean isPublished() {
		
		Date currentDate = DateUtils.getCurrentSQLDate(false);
		
		if (publishDate == null || publishDate.after(currentDate)) {
			
			return false;
			
		} else if (unPublishDate != null && unPublishDate.before(currentDate)) {
			
			return false;
		}
		
		return true;
	}
	
	public List<FlowForm> getFlowForms() {
		return flowForms;
	}
	
	public void setFlowForms(List<FlowForm> flowForms) {
		this.flowForms = flowForms;
	}
	
	public boolean hasTextTags() {
		
		return hasTextTags;
	}
	
	public void setHasTextTags(boolean hasTextTags) {
		
		this.hasTextTags = hasTextTags;
	}
	
	@Override
	public String toString() {
		
		return StringUtils.toLogFormat(name, 30) + " (ID: " + flowID + ")";
	}
	
	@Override
	public boolean usesPreview() {
		
		return usePreview;
	}
	
	public void setUsePreview(boolean usePreview) {
		
		this.usePreview = usePreview;
	}
	
	@Override
	public String getSubmittedMessage() {
		
		return submittedMessage;
	}
	
	public void setSubmittedMessage(String submittedMessage) {
		
		this.submittedMessage = submittedMessage;
	}
	
	public List<DefaultStatusMapping> getDefaultFlowStateMappings() {
		
		return defaultFlowStateMappings;
	}
	
	public void setDefaultFlowStateMappings(List<DefaultStatusMapping> defaultFlowStateMappings) {
		
		this.defaultFlowStateMappings = defaultFlowStateMappings;
	}
	
	@Override
	public Status getDefaultState(String actionID) {
		
		if (this.defaultFlowStateMappings != null) {
			
			for (DefaultStatusMapping flowStateMapping : this.defaultFlowStateMappings) {
				
				if (flowStateMapping.getActionID().equals(actionID)) {
					
					return flowStateMapping.getStatus();
				}
			}
		}
		
		return null;
	}
	
	public boolean isExpired() {
		
		Date currentDate = DateUtils.getCurrentSQLDate(false);
		
		if (unPublishDate != null && unPublishDate.before(currentDate)) {
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public Element toXML(Document doc) {
		
		Element flowElement = super.toXML(doc);
		
		XMLUtils.appendNewElement(doc, flowElement, "shortDescription", shortDescription);
		XMLUtils.appendNewElement(doc, flowElement, "longDescription", longDescription);
		XMLUtils.appendNewElement(doc, flowElement, "submittedMessage", submittedMessage);
		XMLUtils.appendNewElement(doc, flowElement, "published", isPublished());
		
		if(iconLastModified != null){
			
			XMLUtils.appendNewElement(doc, flowElement, "IconLastModified", iconLastModified.getTime());
		}
		
		return flowElement;
	}
	
	public Element toXML(Document doc, SiteProfile siteProfile, String absoluteFileURL, HttpServletRequest req) {
		
		XMLGeneratorDocument genDoc = new XMLGeneratorDocument(doc);
		genDoc.addIgnoredFields(FLOW_FAMILY_RELATION);
		
		Element flowElement = super.toXML(genDoc);
		
		if (flowFamily != null) {
			
			flowElement.appendChild(flowFamily.toXML(doc, siteProfile));
		}
		
		String shortDescription = this.shortDescription;
		String longDescription = this.longDescription;
		String submittedMessage = this.submittedMessage;
		
		if (siteProfile != null && hasTextTags) {
			
			if (shortDescription != null) {
				
				shortDescription = TextTagReplacer.replaceTextTags(shortDescription, siteProfile.getSettingHandler());
			}
			
			if (longDescription != null) {
				
				longDescription = TextTagReplacer.replaceTextTags(longDescription, siteProfile.getSettingHandler());
			}
			
			if (submittedMessage != null) {
				
				submittedMessage = TextTagReplacer.replaceTextTags(submittedMessage, siteProfile.getSettingHandler());
			}
			
		}
		
		if (absoluteFileURL != null && hasFileURLs) {
			
			if (shortDescription != null) {
				
				shortDescription = FCKUtils.setAbsoluteFileUrls(shortDescription, absoluteFileURL);
			}
			
			if (longDescription != null) {
				
				longDescription = FCKUtils.setAbsoluteFileUrls(longDescription, absoluteFileURL);
			}
			
			if (submittedMessage != null) {
				
				submittedMessage = FCKUtils.setAbsoluteFileUrls(submittedMessage, absoluteFileURL);
			}
		}
		
		if (req != null && hasRelativeURLs) {
			
			if (shortDescription != null) {
				
				shortDescription = URLRewriter.setAbsoluteLinkUrls(shortDescription, req);
			}
			
			if (longDescription != null) {
				
				longDescription = URLRewriter.setAbsoluteLinkUrls(longDescription, req);
			}
			
			if (submittedMessage != null) {
				
				submittedMessage = URLRewriter.setAbsoluteLinkUrls(submittedMessage, req);
			}
		}
		
		XMLUtils.appendNewElement(doc, flowElement, "shortDescription", shortDescription);
		XMLUtils.appendNewElement(doc, flowElement, "longDescription", longDescription);
		XMLUtils.appendNewElement(doc, flowElement, "submittedMessage", submittedMessage);
		XMLUtils.appendNewElement(doc, flowElement, "published", isPublished());
		
		return flowElement;
		
	}
	
	public Integer getFlowInstanceCount() {
		
		return flowInstanceCount;
	}
	
	public void setFlowInstanceCount(Integer flowInstanceCount) {
		
		this.flowInstanceCount = flowInstanceCount;
	}
	
	public Integer getFlowSubmittedInstanceCount() {
		
		return flowSubmittedInstanceCount;
	}
	
	public void setFlowSubmittedInstanceCount(Integer flowSubmittedInstanceCount) {
		
		this.flowSubmittedInstanceCount = flowSubmittedInstanceCount;
	}
	
	@Override
	public FlowFamily getFlowFamily() {
		
		return flowFamily;
	}
	
	public void setFlowFamily(FlowFamily flowFamily) {
		
		this.flowFamily = flowFamily;
	}
	
	@Override
	public Integer getVersion() {
		
		return version;
	}
	
	public void setVersion(Integer version) {
		
		this.version = version;
	}
	
	public boolean isLatestVersion() {
		
		return latestVersion != null && latestVersion;
	}
	
	public void setLatestVersion(Boolean latestVersion) {
		
		this.latestVersion = latestVersion;
	}
	
	@Override
	public List<String> getTags() {
		
		return tags;
	}
	
	public void setTags(List<String> tags) {
		
		this.tags = tags;
	}
	
	@Override
	public List<String> getChecks() {
		
		return checks;
	}
	
	public void setChecks(List<String> checks) {
		
		this.checks = checks;
	}
	
	public void setRequireAuthentication(boolean requireAuthentication) {
		
		this.requireAuthentication = requireAuthentication;
	}
	
	@Override
	public boolean requiresAuthentication() {
		
		return requireAuthentication;
	}
	
	public Boolean getPopular() {
		
		return popular;
	}
	
	public void setPopular(Boolean popular) {
		
		this.popular = popular;
	}
	
	@Override
	public int hashCode() {
		
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flowID == null) ? 0 : flowID.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Flow other = (Flow) obj;
		if (flowID == null) {
			if (other.flowID != null) {
				return false;
			}
		} else if (!flowID.equals(other.flowID)) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean requiresSigning() {
		
		return requireSigning;
	}
	
	public void setRequireSigning(boolean requireSigning) {
		
		this.requireSigning = requireSigning;
	}
	
	@Override
	public boolean showsSubmitSurvey() {
		
		return showSubmitSurvey;
	}
	
	public void setShowSubmitSurvey(boolean showSubmitSurvey) {
		
		this.showSubmitSurvey = showSubmitSurvey;
	}
	
	public String getExternalLink() {
		
		return externalLink;
	}
	
	public void setExternalLink(String externalLink) {
		
		this.externalLink = externalLink;
	}
	
	public boolean isInternal() {
		
		return externalLink == null;
	}
	
	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {
		
		List<ValidationError> errors = new ArrayList<ValidationError>();
		
		this.name = XMLValidationUtils.validateParameter("name", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		this.shortDescription = XMLValidationUtils.validateParameter("shortDescription", xmlParser, true, 1, 65535, StringPopulator.getPopulator(), errors);
		
		this.skipOverview = xmlParser.getPrimitiveBoolean("skipOverview");
		
		this.longDescription = XMLValidationUtils.validateParameter("longDescription", xmlParser, !skipOverview, 1, 16777215, StringPopulator.getPopulator(), errors);
		
		this.iconFileName = XMLValidationUtils.validateParameter("iconFileName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		this.iconLastModified = TimeUtils.getCurrentTimestamp();
		
		String icon = xmlParser.getString("icon");
		
		if (!StringUtils.isEmpty(icon)) {
			
			try {
				this.icon = new SerialBlob(Base64.decode(icon));
				
			} catch (Exception e) {
				
				errors.add(new ValidationError("ErrorParsingFlowIcon"));
			}
		}
		
		//DatePopulator datePopulator = new DatePopulator();
		
		//this.publishDate = XMLValidationUtils.validateParameter("publishDate", xmlParser, false, datePopulator, errors);
		//this.unPublishDate = XMLValidationUtils.validateParameter("unPublishDate", xmlParser, false, datePopulator, errors);
		
		this.usePreview = xmlParser.getPrimitiveBoolean("usePreview");
		this.paymentSupportEnabled = xmlParser.getPrimitiveBoolean("paymentSupportEnabled");
		this.requireAuthentication = xmlParser.getPrimitiveBoolean("requireAuthentication");
		this.requireSigning = xmlParser.getPrimitiveBoolean("requireSigning");
		
		this.tags = XMLValidationUtils.validateParameters("Tags/tag", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		this.checks = XMLValidationUtils.validateParameters("Checks/check", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		// Handle old flow form format
		if (xmlParser.getPrimitiveBoolean("hasPDF")) {
			
			FlowForm flowForm = new FlowForm();
			flowForm.setExternalURL(XMLValidationUtils.validateParameter("externalPDF", xmlParser, false, 1, 255, StringHTTPURLPopulator.getPopulator(), errors));
			
			flowForms = new ArrayList<FlowForm>();
			flowForms.add(flowForm);
			
		} else {
			
			this.flowForms = XMLPopulationUtils.populateBeans(xmlParser, "FlowForms/FlowForm", FlowForm.class, errors);
		}
		
		this.externalLink = XMLValidationUtils.validateParameter("externalLink", xmlParser, false, 1, 1024, StringPopulator.getPopulator(), errors);
		
		//Only populated if no externalLink is set
		if (externalLink == null) {
			
			this.submittedMessage = XMLValidationUtils.validateParameter("submittedMessage", xmlParser, true, 1, 16777215, StringPopulator.getPopulator(), errors);
			
			this.statuses = XMLPopulationUtils.populateBeans(xmlParser, "Statuses/Status", Status.class, errors);
			this.defaultFlowStateMappings = XMLPopulationUtils.populateBeans(xmlParser, "defaultFlowStateMappings/DefaultStatusMapping", DefaultStatusMapping.class, errors);
			this.steps = XMLPopulationUtils.populateBeans(xmlParser, "Steps/Step", Step.class, errors);
			this.hideManagerDetails = xmlParser.getPrimitiveBoolean("hideManagerDetails");
		}
		
		this.hideFromOverview = xmlParser.getPrimitiveBoolean("hideFromOverview");
		hideInternalMessages = xmlParser.getPrimitiveBoolean("hideInternalMessages");
		
		if (!errors.isEmpty()) {
			
			throw new ValidationException(errors);
		}
	}
	
	public boolean hasFileURLs() {
		
		return hasFileURLs;
	}
	
	public void setHasFileURLs(boolean hasFileURLs) {
		
		this.hasFileURLs = hasFileURLs;
	}
	
	public boolean hasRelativeURLs() {
		
		return hasRelativeURLs;
	}
	
	public void setHasRelativeURLs(boolean hasRelativeURLs) {
		
		this.hasRelativeURLs = hasRelativeURLs;
	}
	
	@Override
	public boolean skipOverview() {
		
		return skipOverview;
	}
	
	public void setSkipOverview(boolean skipOverview) {
		
		this.skipOverview = skipOverview;
	}
	
	public Step getStep(Integer stepID) {
		
		if (steps != null) {
			
			for (Step step : steps) {
				
				if (step.getStepID().equals(stepID)) {
					
					return step;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public boolean hidesManagerDetails() {
		
		return hideManagerDetails;
	}
	
	public void setHideManagerDetails(boolean hideManagerDetails) {
		
		this.hideManagerDetails = hideManagerDetails;
	}
	
	public boolean isHideFromOverview() {
		
		return hideFromOverview;
	}
	
	public void setHideFromOverview(boolean hideFromOverview) {
		
		this.hideFromOverview = hideFromOverview;
	}
	
	public boolean isHideInternalMessages() {
		return hideInternalMessages;
	}
	
	public void setHideInternalMessages(boolean hideInternalMessages) {
		this.hideInternalMessages = hideInternalMessages;
	}

	
	@Override
	public boolean isPaymentSupportEnabled() {
	
		return paymentSupportEnabled;
	}

	
	public void setPaymentSupportEnabled(boolean paymentSupportEnabled) {
	
		this.paymentSupportEnabled = paymentSupportEnabled;
	}

	@Override
	public String getIconFilename() {

		return this.iconFileName;
	}

	@Override
	public Blob getIconBlob() {

		return this.icon;
	}
	
	@Override
	public Timestamp getIconLastModified() {
	
		return iconLastModified;
	}

	
	public void setIconLastModified(Timestamp iconLastModified) {
	
		this.iconLastModified = iconLastModified;
	}
	
}
