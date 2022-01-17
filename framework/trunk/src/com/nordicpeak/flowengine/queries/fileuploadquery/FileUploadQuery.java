package com.nordicpeak.flowengine.queries.fileuploadquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.NoDuplicates;
import se.unlogic.standardutils.annotations.RequiredIfSet;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CaseInsensitiveStringComparator;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.populators.PositiveStringIntegerPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLValidationUtils;

import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

@Table(name = "file_upload_queries")
@XMLElement
public class FileUploadQuery extends BaseQuery {
	
	private static final long serialVersionUID = -842191226937409416L;
	
	public static final Field ALLOWED_FILE_EXTENSIONS_RELATION = ReflectionUtils.getField(FileUploadQuery.class, "allowedFileExtensions");
	
	@DAOManaged
	@Key
	@XMLElement
	private Integer queryID;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer maxFileCount;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer maxFileSize;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	private Integer maxQuerySize;
	
	@DAOManaged
	@WebPopulate(populator = PositiveStringIntegerPopulator.class)
	@XMLElement
	protected Integer maxFileNameLength = 255;
	
	@DAOManaged
	@OneToMany(autoUpdate = true, autoAdd = true)
	@SimplifiedRelation(table = "file_upload_query_extensions", remoteValueColumnName = "extension")
	@WebPopulate(maxLength = 12)
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@XMLElement
	private List<String> allowedFileExtensions;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	protected boolean inlinePDFAttachments;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	protected boolean numberInlineAttachments;
	
	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	protected AttachmentNamePrefixType attachmentNamePrefixMode;
	
	@DAOManaged
	@WebPopulate(maxLength = 80)
	@RequiredIfSet(paramNames = "attachmentNamePrefixMode", paramValues = "CUSTOM")
	@XMLElement
	protected String attachmentNameCustomPrefix;
	
	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean setAsAttribute;
	
	@DAOManaged
	@WebPopulate
	@RequiredIfSet(paramNames = "setAsAttribute")
	@XMLElement
	private String attributeName;
	
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
	private boolean lockOnOwnershipTransfer;

	@DAOManaged
	@WebPopulate(maxLength = 30)
	@XMLElement
	private String selectFilesButtonText;
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<FileUploadQueryInstance> instances;
	
	public static long getSerialversionuid() {
		
		return serialVersionUID;
	}
	
	@Override
	public Integer getQueryID() {
		
		return queryID;
	}
	
	public List<FileUploadQueryInstance> getInstances() {
		
		return instances;
	}
	
	public void setInstances(List<FileUploadQueryInstance> instances) {
		
		this.instances = instances;
	}
	
	public void setQueryID(int queryID) {
		
		this.queryID = queryID;
	}
	
	@Override
	public String toString() {
		
		if (this.queryDescriptor != null) {
			
			return queryDescriptor.getName() + " (queryID: " + queryID + ")";
		}
		
		return "FileUploadQuery (queryID: " + queryID + ")";
	}
	
	public Integer getMaxFileCount() {
		
		return maxFileCount;
	}
	
	public void setMaxFileCount(Integer maxLength) {
		
		this.maxFileCount = maxLength;
	}
	
	public List<String> getAllowedFileExtensions() {
		
		return allowedFileExtensions;
	}
	
	public void setAllowedFileExtensions(List<String> allowedFileExtensions) {
		
		this.allowedFileExtensions = allowedFileExtensions;
	}
	
	public Integer getMaxFileSize() {
		
		return maxFileSize;
	}
	
	public void setMaxFileSize(Integer maxFileSize) {
		
		this.maxFileSize = maxFileSize;
	}
	
	
	public Integer getMaxQuerySize() {
	
		return maxQuerySize;
	}

	
	public void setMaxQuerySize(Integer maxQuerySize) {
	
		this.maxQuerySize = maxQuerySize;
	}

	public Integer getMaxFileNameLength() {
		
		return maxFileNameLength;
	}
	
	public void setMaxFileNameLength(Integer maxFileNameLength) {
		
		this.maxFileNameLength = maxFileNameLength;
	}
	
	@Override
	public String getXSDTypeName() {
		
		return "FileUploadQuery" + queryID;
	}
	
	@Override
	public Element toXML(Document doc) {
		
		Element queryElement = super.toXML(doc);
		
		if (maxFileSize != null) {
			XMLUtils.appendNewElement(doc, queryElement, "FormatedMaxSize", BinarySizeFormater.getFormatedSize(maxFileSize));
		}
		
		if(maxQuerySize != null) {
			XMLUtils.appendNewElement(doc, queryElement, "FormatedMaxQuerySize", BinarySizeFormater.getFormatedSize(maxQuerySize));
		}
		
		if (maxFileNameLength != null) {
			XMLUtils.appendNewElement(doc, queryElement, "MaxFileNameLength", maxFileNameLength);
		}
		
		return queryElement;
	}
	
	@Override
	public void toXSD(Document doc) {
		
		Element complexTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexType");
		complexTypeElement.setAttribute("name", getXSDTypeName());
		
		Element complexContentElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexContent");
		complexTypeElement.appendChild(complexContentElement);
		
		Element extensionElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:extension");
		extensionElement.setAttribute("base", "Query");
		complexContentElement.appendChild(extensionElement);
		
		Element sequenceElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:sequence");
		extensionElement.appendChild(sequenceElement);
		
		Element nameElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		nameElement.setAttribute("name", "Name");
		nameElement.setAttribute("type", "xs:string");
		nameElement.setAttribute("minOccurs", "1");
		nameElement.setAttribute("maxOccurs", "1");
		nameElement.setAttribute("fixed", queryDescriptor.getName());
		sequenceElement.appendChild(nameElement);
		
		Element fileElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		fileElement.setAttribute("name", "File");
		fileElement.setAttribute("type", "FileType" + queryID);
		fileElement.setAttribute("minOccurs", "0");
		fileElement.setAttribute("maxOccurs", this.maxFileCount != null ? maxFileCount.toString() : "unbounded");
		
		sequenceElement.appendChild(fileElement);
		
		doc.getDocumentElement().appendChild(complexTypeElement);
		
		appendXSDType(doc);
	}
	
	public void appendXSDType(Document doc) {
		
		Element complexTypeElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:complexType");
		complexTypeElement.setAttribute("name", "FileType" + queryID);
		
		Element sequenceElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:sequence");
		complexTypeElement.appendChild(sequenceElement);
		
		addElementType(doc, sequenceElement, "ID", "xs:string");
		addElementType(doc, sequenceElement, "Name", "xs:string");
		addElementType(doc, sequenceElement, "Size", "xs:long");
		addElementType(doc, sequenceElement, "EncodedData", "xs:string");
		
		doc.getDocumentElement().appendChild(complexTypeElement);
	}
	
	private void addElementType(Document doc, Element sequenceElement, String name, String type) {
		
		Element element = doc.createElementNS("http://www.w3.org/2001/XMLSchema", "xs:element");
		element.setAttribute("name", name);
		element.setAttribute("type", type);
		element.setAttribute("minOccurs", "1");
		element.setAttribute("maxOccurs", "1");
		
		sequenceElement.appendChild(element);
	}
	
	@Override
	public void populate(XMLParser xmlParser) throws ValidationException {
		
		List<ValidationError> errors = new ArrayList<>();
		
		description = XMLValidationUtils.validateParameter("description", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		helpText = XMLValidationUtils.validateParameter("helpText", xmlParser, false, 1, 65535, StringPopulator.getPopulator(), errors);
		
		maxFileCount = XMLValidationUtils.validateParameter("maxFileCount", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		maxFileSize = XMLValidationUtils.validateParameter("maxFileSize", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		maxQuerySize = XMLValidationUtils.validateParameter("maxQuerySize", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		maxFileNameLength = XMLValidationUtils.validateParameter("maxFileNameLength", xmlParser, false, PositiveStringIntegerPopulator.getPopulator(), errors);
		
		allowedFileExtensions = xmlParser.getStrings("allowedFileExtensions/value");
		
		inlinePDFAttachments = xmlParser.getPrimitiveBoolean("inlinePDFAttachments");
		numberInlineAttachments = xmlParser.getPrimitiveBoolean("numberInlineAttachments");
		lockOnOwnershipTransfer = xmlParser.getPrimitiveBoolean("lockOnOwnershipTransfer");
		hideTitle = xmlParser.getPrimitiveBoolean("hideTitle");
		hideDescriptionInPDF = xmlParser.getPrimitiveBoolean("hideDescriptionInPDF");
		
		attachmentNamePrefixMode = XMLValidationUtils.validateParameter("attachmentNamePrefixMode", xmlParser, false, AttachmentNamePrefixType.getPopulator(), errors);
		
		selectFilesButtonText = XMLValidationUtils.validateParameter("selectFilesButtonText", xmlParser, false, 1, 30, StringPopulator.getPopulator(), errors);
		
		if (attachmentNamePrefixMode == null) {
			
			attachmentNamePrefixMode = AttachmentNamePrefixType.QUERY_NAME;
		}
		
		attachmentNameCustomPrefix = XMLValidationUtils.validateParameter("attachmentNameCustomPrefix", xmlParser, attachmentNamePrefixMode == AttachmentNamePrefixType.CUSTOM, 0, 80, StringPopulator.getPopulator(), errors);
		
		attributeName = XMLValidationUtils.validateParameter("attributeName", xmlParser, false, 1, 255, StringPopulator.getPopulator(), errors);
		
		if (attributeName != null) {
			
			setAsAttribute = xmlParser.getPrimitiveBoolean("setAsAttribute");
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
	
	public AttachmentNamePrefixType getAttachmentNamePrefixMode() {
		
		return attachmentNamePrefixMode;
	}
	
	public void setAttachmentNamePrefixMode(AttachmentNamePrefixType attachmentNamePrefixMode) {
		
		this.attachmentNamePrefixMode = attachmentNamePrefixMode;
	}
	
	public String getAttachmentNameCustomPrefix() {
		
		return attachmentNameCustomPrefix;
	}
	
	public void setAttachmentNameCustomPrefix(String attachmentNameCustomPrefix) {
		
		this.attachmentNameCustomPrefix = attachmentNameCustomPrefix;
	}
	
	public boolean isInlinePDFAttachments() {
		return inlinePDFAttachments;
	}

	public void setInlinePDFAttachments(boolean inlinePDFAttachments) {
		this.inlinePDFAttachments = inlinePDFAttachments;
	}

	public boolean isNumberInlineAttachments() {
		return numberInlineAttachments;
	}

	public void setNumberInlineAttachments(boolean numberInlineAttachments) {
		this.numberInlineAttachments = numberInlineAttachments;
	}

	public boolean isLockOnOwnershipTransfer() {

		return lockOnOwnershipTransfer;
	}

	public void setLockOnOwnershipTransfer(boolean lockOnOwnershipTransfer) {

		this.lockOnOwnershipTransfer = lockOnOwnershipTransfer;
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

	public String getSelectFilesButtonText() {

		return selectFilesButtonText;
	}

	public void setSelectFilesButtonText(String selectFilesButtonText) {

		this.selectFilesButtonText = selectFilesButtonText;
	}

}
