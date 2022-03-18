package com.nordicpeak.flowengine.beans;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.sql.Blob;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
import se.unlogic.standardutils.collections.CaseInsensitiveStringComparator;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.date.DateStringyfier;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.image.ImageUtils;
import se.unlogic.standardutils.populators.EndsWithStringPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.populators.YearLimitedDatePopulator;
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
public class Flow extends GeneratedElementable implements ImmutableFlow, XMLParserPopulateable, Named, Icon {

	private static final long serialVersionUID = -1533312692687401406L;

	public static final EndsWithStringPopulator ICON_FILE_EXTENSION_POPULATOR = new EndsWithStringPopulator(".png", ".jpg", ".gif", ".bmp");

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
	public static final Field OVERVIEW_ATTRIBUTES_RELATION = ReflectionUtils.getField(Flow.class, "overviewAttributes");

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
	public static final Field HIDE_EXTERNAL_MESSAGES_FIELD = ReflectionUtils.getField(Flow.class, "hideExternalMessages");
	public static final Field HIDE_EXTERNAL_MESSAGE_ATTACHMENTS_FIELD = ReflectionUtils.getField(Flow.class, "hideExternalMessageAttachments");
	public static final Field READ_RECEIPTS_ENABLED_FIELD = ReflectionUtils.getField(Flow.class, "readReceiptsEnabled");
	public static final Field READ_RECEIPTS_ENABLED_BY_DEFAULT_FIELD = ReflectionUtils.getField(Flow.class, "readReceiptsEnabledByDefault");
	public static final Field EXTERNAL_LINK_FIELD = ReflectionUtils.getField(Flow.class, "externalLink");

	@DAOManaged(autoGenerated = true)
	@Key
	@StringTag
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
	@WebPopulate(populator = YearLimitedDatePopulator.class)
	@RequiredIfSet(paramNames = "unPublishDate")
	@XMLElement(valueFormatter = DateStringyfier.class)
	private Date publishDate;

	@StringTag
	@DAOManaged
	@WebPopulate(populator = YearLimitedDatePopulator.class)
	@XMLElement(valueFormatter = DateStringyfier.class)
	private Date unPublishDate;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean enabled;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean lockSubmitForUnpublishedSavedFlow;

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
	private boolean allowForeignIDs;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean requireSigning;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean useSequentialSigning;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean skipPosterSigning;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean allowPosterMultipartSigning;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean appendSigningSignatureToPDF;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean showPreviousSignaturesToSigners;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean showSubmitSurvey;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideSubmitStepText;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideSaveButton;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean showLoginQuestion;

	@FCKContent
	@URLRewrite
	@DAOManaged
	@WebPopulate(maxLength = 65535)
	@RequiredIfSet(paramNames = "showLoginQuestion", paramValues = "true")
	@XMLElement(cdata = true)
	protected String loginQuestionText;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_tags", remoteValueColumnName = "tag")
	@WebPopulate(maxLength = 255)
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@SplitOnLineBreak
	@XMLElement(fixCase = true, childName = "tag")
	private List<String> tags;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "flowengine_flow_checks", remoteValueColumnName = "value", preserveListOrder = true, indexColumn = "checkIndex")
	@WebPopulate(maxLength = 255)
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
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
	private boolean hideExternalMessages;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideExternalMessageAttachments;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean readReceiptsEnabled;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean readReceiptsEnabledByDefault;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideInternalMessages;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideFromUser;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideFromManager;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean hideFlowInstanceIDFromUser;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean alwaysStartFromFirstStep;

	@DAOManaged
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String userDescriptionTemplate;

	@DAOManaged
	@WebPopulate(maxLength = 1024)
	@XMLElement
	private String managerDescriptionTemplate;

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

	@DAOManaged
	@OneToMany
	private List<FlowOverviewAttribute> overviewAttributes;

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
	public boolean isLockSubmitForUnpublishedSavedFlow() {

		return lockSubmitForUnpublishedSavedFlow;
	}

	public void setLockSubmitForUnpublishedSavedFlow(boolean lockSubmitForUnpublishedSavedFlow) {

		this.lockSubmitForUnpublishedSavedFlow = lockSubmitForUnpublishedSavedFlow;
	}

	@Override
	public boolean isPublished() {

		Date currentDate = DateUtils.getCurrentSQLDate(false);

		if (publishDate == null || publishDate.after(currentDate)) {

			return false;

		}
		if (unPublishDate != null && unPublishDate.before(currentDate)) {

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

		if (iconLastModified != null) {

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

		return Objects.hash(flowID);
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
		if (!Objects.equals(flowID, other.flowID)) {
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
	public boolean usesSequentialSigning() {

		return useSequentialSigning;
	}

	public void setUseSequentialSigning(boolean useSequentialSigning) {

		this.useSequentialSigning = useSequentialSigning;
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

		List<ValidationError> errors = new ArrayList<>();

		this.name = XMLValidationUtils.validateParameter("name", xmlParser, true, 1, 255, StringPopulator.getPopulator(), errors);
		this.shortDescription = XMLValidationUtils.validateParameter("shortDescription", xmlParser, true, 1, 65535, StringPopulator.getPopulator(), errors);

		this.skipOverview = xmlParser.getPrimitiveBoolean("skipOverview");

		this.longDescription = XMLValidationUtils.validateParameter("longDescription", xmlParser, !skipOverview, 1, 16777215, StringPopulator.getPopulator(), errors);

		this.iconLastModified = TimeUtils.getCurrentTimestamp();

		String icon = xmlParser.getString("icon");

		if (!StringUtils.isEmpty(icon)) {

			this.iconFileName = XMLValidationUtils.validateParameter("iconFileName", xmlParser, true, 1, 255, ICON_FILE_EXTENSION_POPULATOR, errors);

			try {
				byte[] imageData = Base64.decode(icon);

				BufferedImage bufferedImage = ImageUtils.getImage(imageData);

				if (bufferedImage != null) {

					this.icon = new SerialBlob(imageData);

				} else {

					errors.add(new ValidationError("UnableToParseIcon"));
				}

			} catch (Exception e) {

				errors.add(new ValidationError("UnableToParseIcon"));
			}
		}

		//DatePopulator datePopulator = new DatePopulator();

		//this.publishDate = XMLValidationUtils.validateParameter("publishDate", xmlParser, false, datePopulator, errors);
		//this.unPublishDate = XMLValidationUtils.validateParameter("unPublishDate", xmlParser, false, datePopulator, errors);

		usePreview = xmlParser.getPrimitiveBoolean("usePreview");
		paymentSupportEnabled = xmlParser.getPrimitiveBoolean("paymentSupportEnabled");
		requireAuthentication = xmlParser.getPrimitiveBoolean("requireAuthentication");
		showLoginQuestion = xmlParser.getPrimitiveBoolean("showLoginQuestion");
		loginQuestionText = XMLValidationUtils.validateParameter("loginQuestionText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		requireSigning = xmlParser.getPrimitiveBoolean("requireSigning");
		useSequentialSigning = xmlParser.getPrimitiveBoolean("useSequentialSigning");
		skipPosterSigning = xmlParser.getPrimitiveBoolean("skipPosterSigning");
		allowPosterMultipartSigning = xmlParser.getPrimitiveBoolean("allowPosterMultipartSigning");
		appendSigningSignatureToPDF = xmlParser.getPrimitiveBoolean("appendSigningSignatureToPDF");
		showPreviousSignaturesToSigners = xmlParser.getPrimitiveBoolean("showPreviousSignaturesToSigners");
		allowForeignIDs = xmlParser.getPrimitiveBoolean("allowForeignIDs");
		showSubmitSurvey = xmlParser.getPrimitiveBoolean("showSubmitSurvey");
		hideSubmitStepText = xmlParser.getPrimitiveBoolean("hideSubmitStepText");
		hideSaveButton = xmlParser.getPrimitiveBoolean("hideSaveButton");

		this.tags = XMLValidationUtils.validateParameters("Tags/tag", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		this.checks = XMLValidationUtils.validateParameters("Checks/check", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);

		// Handle old flow form format
		if (xmlParser.getPrimitiveBoolean("hasPDF")) {

			FlowForm flowForm = new FlowForm();
			flowForm.setExternalURL(XMLValidationUtils.validateParameter("externalPDF", xmlParser, false, 1, 255, StringHTTPURLPopulator.getPopulator(), errors));

			flowForms = new ArrayList<>();
			flowForms.add(flowForm);

		} else {

			this.flowForms = XMLPopulationUtils.populateBeans(xmlParser, "FlowForms/FlowForm", FlowForm.class, errors);
		}

		overviewAttributes = XMLPopulationUtils.populateBeans(xmlParser, "OverviewAttributes/FlowOverviewAttribute", FlowOverviewAttribute.class, errors);

		this.externalLink = XMLValidationUtils.validateParameter("externalLink", xmlParser, false, 1, 1024, StringPopulator.getPopulator(), errors);

		//Only populated if no externalLink is set
		if (externalLink == null) {

			this.submittedMessage = XMLValidationUtils.validateParameter("submittedMessage", xmlParser, false, 1, 16777215, StringPopulator.getPopulator(), errors);

			this.statuses = XMLPopulationUtils.populateBeans(xmlParser, "Statuses/Status", Status.class, errors);
			this.defaultFlowStateMappings = XMLPopulationUtils.populateBeans(xmlParser, "defaultFlowStateMappings/DefaultStatusMapping", DefaultStatusMapping.class, errors);
			this.steps = XMLPopulationUtils.populateBeans(xmlParser, "Steps/Step", Step.class, errors);
			this.hideManagerDetails = xmlParser.getPrimitiveBoolean("hideManagerDetails");
		}

		hideFromOverview = xmlParser.getPrimitiveBoolean("hideFromOverview");
		hideFromUser = xmlParser.getPrimitiveBoolean("hideFromUser");
		hideFromManager = xmlParser.getPrimitiveBoolean("hideFromManager");
		hideFlowInstanceIDFromUser = xmlParser.getPrimitiveBoolean("hideFlowInstanceIDFromUser");
		hideInternalMessages = xmlParser.getPrimitiveBoolean("hideInternalMessages");
		hideExternalMessages = xmlParser.getPrimitiveBoolean("hideExternalMessages");
		hideExternalMessageAttachments = xmlParser.getPrimitiveBoolean("hideExternalMessageAttachments");
		readReceiptsEnabled = xmlParser.getPrimitiveBoolean("readReceiptsEnabled");
		readReceiptsEnabledByDefault = xmlParser.getPrimitiveBoolean("readReceiptsEnabledByDefault");

		this.userDescriptionTemplate = XMLValidationUtils.validateParameter("userDescriptionTemplate", xmlParser, false, 1, 1024, StringPopulator.getPopulator(), errors);
		this.managerDescriptionTemplate = XMLValidationUtils.validateParameter("managerDescriptionTemplate", xmlParser, false, 1, 1024, StringPopulator.getPopulator(), errors);

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

	public boolean isHideExternalMessages() {

		return hideExternalMessages;
	}

	public void setHideExternalMessages(boolean hideExternalMessages) {

		this.hideExternalMessages = hideExternalMessages;
	}

	public boolean isHideExternalMessageAttachments() {

		return hideExternalMessageAttachments;
	}

	public void setHideExternalMessageAttachments(boolean hideExternalMessageAttachments) {

		this.hideExternalMessageAttachments = hideExternalMessageAttachments;
	}

	public boolean isReadReceiptsEnabled() {

		return readReceiptsEnabled;
	}

	public void setReadReceiptsEnabled(boolean readReceiptsEnabled) {

		this.readReceiptsEnabled = readReceiptsEnabled;
	}
	
	
	public boolean isReadReceiptsEnabledByDefault() {

		return readReceiptsEnabledByDefault;
	}
	
	
	public void setReadReceiptsEnabledByDefault(boolean readReceiptsEnabledByDefault) {

		this.readReceiptsEnabledByDefault = readReceiptsEnabledByDefault;
	}

	@Override
	public boolean isHideFromUser() {

		return hideFromUser;
	}

	public void setHideFromUser(boolean hideFromUser) {

		this.hideFromUser = hideFromUser;
	}

	@Override
	public boolean isHideFlowInstanceIDFromUser() {

		return hideFlowInstanceIDFromUser;
	}

	public void setHideFlowInstanceIDFromUser(boolean hideFlowInstanceIDFromUser) {

		this.hideFlowInstanceIDFromUser = hideFlowInstanceIDFromUser;
	}

	@Override
	public boolean isAlwaysStartFromFirstStep() {

		return alwaysStartFromFirstStep;
	}

	public void setAlwaysStartFromFirstStep(boolean alwaysStartFromFirstStep) {

		this.alwaysStartFromFirstStep = alwaysStartFromFirstStep;
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

	public boolean isShowLoginQuestion() {

		return showLoginQuestion;
	}

	public void setShowLoginQuestion(boolean showLoginQuestion) {

		this.showLoginQuestion = showLoginQuestion;
	}

	public String getLoginQuestionText() {

		return loginQuestionText;
	}

	public void setLoginQuestionText(String loginQuestionText) {

		this.loginQuestionText = loginQuestionText;
	}

	@Override
	public String getUserDescriptionTemplate() {

		return userDescriptionTemplate;
	}

	public void setUserDescriptionTemplate(String userDescriptionTemplate) {

		this.userDescriptionTemplate = userDescriptionTemplate;
	}

	@Override
	public String getManagerDescriptionTemplate() {

		return managerDescriptionTemplate;
	}

	public void setManagerDescriptionTemplate(String managerDescriptionTemplate) {

		this.managerDescriptionTemplate = managerDescriptionTemplate;
	}

	@Override
	public boolean isAllowForeignIDs() {

		return allowForeignIDs;
	}

	public void setAllowForeignIDs(boolean allowForeignIDs) {

		this.allowForeignIDs = allowForeignIDs;
	}

	@Override
	public boolean isSkipPosterSigning() {

		return skipPosterSigning;
	}

	public void setSkipPosterSigning(boolean skipPosterSigningInMultiSigning) {

		this.skipPosterSigning = skipPosterSigningInMultiSigning;
	}

	@Override
	public boolean isAllowPosterMultipartSigning() {

		return allowPosterMultipartSigning;
	}

	public void setAllowPosterMultipartSigning(boolean allowPosterMultipartSigning) {

		this.allowPosterMultipartSigning = allowPosterMultipartSigning;
	}

	@Override
	public boolean isAppendSigningSignatureToPDF() {

		return appendSigningSignatureToPDF;
	}

	public void setAppendSigningSignatureToPDF(boolean appendSigningSignatureToPDF) {

		this.appendSigningSignatureToPDF = appendSigningSignatureToPDF;
	}

	public boolean isShowPreviousSignaturesToSigners() {

		return showPreviousSignaturesToSigners;
	}

	public void setShowPreviousSignaturesToSigners(boolean showPreviousSignaturesToSigners) {

		this.showPreviousSignaturesToSigners = showPreviousSignaturesToSigners;
	}

	public List<FlowOverviewAttribute> getOverviewAttributes() {

		return overviewAttributes;
	}

	public void setOverviewAttributes(List<FlowOverviewAttribute> overviewAttributes) {

		this.overviewAttributes = overviewAttributes;
	}

	@Override
	public boolean isHideSaveButton() {

		return hideSaveButton;
	}

	public void setHideSaveButton(boolean hideSaveButton) {

		this.hideSaveButton = hideSaveButton;
	}

	public boolean isHideFromManager() {

		return hideFromManager;
	}

	public void setHideFromManager(boolean hideFromManager) {

		this.hideFromManager = hideFromManager;
	}

}
