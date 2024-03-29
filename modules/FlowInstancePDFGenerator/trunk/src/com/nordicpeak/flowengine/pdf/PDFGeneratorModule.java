package com.nordicpeak.flowengine.pdf;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Level;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.pdmodel.graphics.color.PDOutputIntent;
import org.apache.xmpbox.XMPMetadata;
import org.apache.xmpbox.schema.AdobePDFSchema;
import org.apache.xmpbox.schema.DublinCoreSchema;
import org.apache.xmpbox.schema.PDFAIdentificationSchema;
import org.apache.xmpbox.schema.XMPBasicSchema;
import org.apache.xmpbox.xml.XmpSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xhtmlrenderer.pdf.ITextRenderer;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.ServerStartupListener;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.settings.Setting;
import se.unlogic.hierarchy.core.settings.SingleFileUploadSetting;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.log4jutils.Log4jUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.SiteProfileUtils;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfile;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileHandler;
import se.unlogic.openhierarchy.foregroundmodules.siteprofile.interfaces.SiteProfileSettingProvider;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.ReverseListIterator;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.RelationQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.random.RandomUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.ClassPathURIResolver;
import se.unlogic.standardutils.xml.XMLTransformer;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xsl.URIXSLTransformer;
import se.unlogic.standardutils.xsl.XSLVariableReader;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.nordicpeak.flowengine.Constants;
import com.nordicpeak.flowengine.FlowBrowserModule;
import com.nordicpeak.flowengine.beans.FlowInstance;
import com.nordicpeak.flowengine.beans.FlowInstanceEvent;
import com.nordicpeak.flowengine.beans.FlowInstanceEventAttribute;
import com.nordicpeak.flowengine.beans.PDFQueryResponse;
import com.nordicpeak.flowengine.dao.FlowEngineDAOFactory;
import com.nordicpeak.flowengine.enums.EventType;
import com.nordicpeak.flowengine.events.SubmitEvent;
import com.nordicpeak.flowengine.interfaces.EvaluationHandler;
import com.nordicpeak.flowengine.interfaces.FlowEngineInterface;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstance;
import com.nordicpeak.flowengine.interfaces.ImmutableFlowInstanceEvent;
import com.nordicpeak.flowengine.interfaces.PDFAttachment;
import com.nordicpeak.flowengine.interfaces.PDFProvider;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.managers.FlowInstanceManager;
import com.nordicpeak.flowengine.managers.PDFManagerResponse;
import com.nordicpeak.flowengine.pdf.schedule.PDFTemporaryFileDeleter;
import com.nordicpeak.flowengine.utils.FlowInstanceUtils;
import com.nordicpeak.flowengine.utils.PDFByteAttachment;
import com.nordicpeak.flowengine.utils.PDFFileAttachment;
import com.nordicpeak.flowengine.utils.PDFInputStreamAttachment;
import com.nordicpeak.flowengine.utils.PDFUtils;
import com.nordicpeak.flowengine.utils.PDFXMLUtils;

import it.sauronsoftware.cron4j.Scheduler;

public class PDFGeneratorModule extends AnnotatedForegroundModule implements FlowEngineInterface, PDFProvider, SiteProfileSettingProvider{

	private static final String PDF = ".pdf";
	public static final String LOGOTYPE_SETTING_ID = "pdf.flowinstance.logofile";
	private static final String TEMP_PDF_ID_FLOW_INSTANCE_MANAGER_ATTRIBUTE = "pdf.temp.id";

	public static final RelationQuery EVENT_ATTRIBUTE_RELATION_QUERY = new RelationQuery(FlowInstanceEvent.ATTRIBUTES_RELATION);

	private static final ITextPDFCreationListener ITEXT_PDF_CREATION_LISTENER = new ITextPDFCreationListener();

	static {
		//Fix PDFBox logging
		Log4jUtils.setLoggerLevel("org.apache.pdfbox.pdfparser.BaseParser", Level.OFF);
		Log4jUtils.setLoggerLevel("org.apache.pdfbox.pdmodel.common.PDNumberTreeNode", Level.OFF);
	}

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF XSL stylesheet", description = "The path in classpath relative from this class to the XSL stylesheet used to transform the XHTML for PDF output of queries", required = true)
	protected String pdfStyleSheet;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Custom stylesheets", description = "Custom stylesheets")
	private List<String> customStyleSheets;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Default logotype", description = "The path to the default logotype. The path can be in both filesystem or classpath. Use classpath:// prefix resouces in classpath and file:/ prefix f�r files in filesystem.", required = true)
	protected String defaultLogotype = "classpath://com/nordicpeak/flowengine/pdf/staticcontent/pics/logo.png";

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Supported actionID's", description = "The action ID's which will trigger a PDF to be generated when a submit event is detected")
	private List<String> supportedActionIDs;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Included fonts", description = "Path to the fonts that should be included in the PDF (the paths can be either in filesystem or classpath)")
	private List<String> includedFonts;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "PDF dir", description = "The directory where PDF files be stored", required = true)
	protected String pdfDir;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Temp dir", description = "The directory where temporary files be stored", required = true)
	protected String tempDir;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable XML debug", description = "Enables writing of the generated XML to file if a file is set below.")
	private boolean xmlDebug;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "XML debug file", description = "The file to write the generated XML to for debug purposes.")
	protected String xmlDebugFile;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Enable XHTML debug", description = "Enables writing of the generated XHTML to file if a file is set below.")
	private boolean xhtmlDebug;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "XHTML debug file", description = "The file to write the generated XHTML to for debug purposes.")
	protected String xhtmlDebugFile;

	private AnnotatedDAO<FlowInstanceEventAttribute> flowInstanceEventAttributeDAO;

	@InstanceManagerDependency(required = true)
	private EvaluationHandler evaluationHandler;

	@InstanceManagerDependency(required = true)
	private QueryHandler queryHandler;

	@InstanceManagerDependency(required = true)
	protected FlowBrowserModule browserModule;

	protected SiteProfileHandler siteProfileHandler;

	private FlowEngineDAOFactory daoFactory;

	protected URIXSLTransformer pdfTransformer;

	private Scheduler scheduler;

	protected String signatureAttachmentName = "Signature";
	protected String signingPDFAttachmentName = "Signing PDF";
	protected String inlineAttachmentPageNumber1 = "Attachment ";
	protected String inlineAttachmentPageNumber2 = " page ";
	protected String inlineAttachmentPageNumber3 = " of ";
	protected String inlineAttachmentFlowInstanceID = "Flowinstance ID";
	protected String inlineAttachmentDate = "Date";
	protected String inlineAttachmentSigning = "Signing";
	protected String inlineAttachmentSigned = "Signed";

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(PDFProvider.class, this)) {

			throw new RuntimeException("Unable to register module " + this.moduleDescriptor + " in global instance handler using key " + PDFProvider.class.getSimpleName() + ", another instance is already registered using this key.");
		}
	}

	@ServerStartupListener
	private synchronized void initScheduler() {

		if (scheduler != null) {

			log.error("Invalid state, scheduler already running!");
			stopScheduler();
		}

		if(pdfDir == null || tempDir == null || !FileUtils.directoryExists(pdfDir) || !FileUtils.directoryExists(tempDir)) {
			
			log.error("Unable to start scheduler for missing temporary directories due to missing configuration or missing directories");
			
		}else {

			scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
			scheduler.setDaemon(true);
			scheduler.schedule("0 * * * *", new PDFTemporaryFileDeleter(pdfDir, tempDir));

			scheduler.start();
		}
	}

	private synchronized void stopScheduler() {

		try {
			if (scheduler != null) {

				scheduler.stop();
				scheduler = null;
			}

		} catch (IllegalStateException e) {
			
			log.error("Error stopping scheduler", e);
		}
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);
		
		stopScheduler();
		initScheduler();
	}

	@Override
	public void unload() throws Exception {

		stopScheduler();

		systemInterface.getInstanceHandler().removeInstance(PDFProvider.class, this);

		if (siteProfileHandler != null) {

			siteProfileHandler.removeSettingProvider(this);
		}

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		daoFactory = new FlowEngineDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		HierarchyAnnotatedDAOFactory hdaoFactory = new HierarchyAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), false, true, false);

		flowInstanceEventAttributeDAO = hdaoFactory.getDAO(FlowInstanceEventAttribute.class);
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		if (pdfStyleSheet == null) {

			pdfTransformer = null;

		} else {

			URL styleSheetURL = this.getClass().getResource(pdfStyleSheet);

			if (styleSheetURL != null) {

				try {
					pdfTransformer = new URIXSLTransformer(styleSheetURL.toURI(), ClassPathURIResolver.getInstance(), true);

					XSLVariableReader variableReader = new XSLVariableReader(styleSheetURL.toURI());

					//@formatter:off
					signatureAttachmentName =  readXSLVariable("java.signature", signatureAttachmentName, variableReader);
					signingPDFAttachmentName = readXSLVariable("java.signingPDF", signingPDFAttachmentName, variableReader);
					
					inlineAttachmentPageNumber1 =        readXSLVariable("java.attachmentPageNumber1", inlineAttachmentPageNumber1, variableReader);
					inlineAttachmentPageNumber2 =        readXSLVariable("java.attachmentPageNumber2", inlineAttachmentPageNumber2, variableReader);
					inlineAttachmentPageNumber3 =        readXSLVariable("java.attachmentPageNumber3", inlineAttachmentPageNumber3, variableReader);
					inlineAttachmentFlowInstanceID =     readXSLVariable("java.attachmentFlowInstanceID", inlineAttachmentFlowInstanceID, variableReader);
					inlineAttachmentDate =               readXSLVariable("java.attachmentDate", inlineAttachmentDate, variableReader);
					inlineAttachmentSigning =            readXSLVariable("i18n.Signing", inlineAttachmentSigning, variableReader);
					inlineAttachmentSigned =             readXSLVariable("i18n.Signed", inlineAttachmentSigned, variableReader);
					//@formatter:on

					log.info("Succesfully parsed PDF stylesheet " + pdfStyleSheet);

				} catch (Exception e) {

					log.error("Unable to cache PDF style sheet " + pdfStyleSheet, e);

					pdfTransformer = null;
				}

			} else {
				log.error("Unable to cache PDF style sheet. Resource " + pdfStyleSheet + " not found");
			}
		}
	}

	protected String readXSLVariable(String variableName, String oldValue, XSLVariableReader variableReader) {

		String newValue = variableReader.getValue(variableName);

		if (!StringUtils.isEmpty(newValue)) {

			return newValue;
		}

		return oldValue;
	}

	protected File createPDF(FlowInstanceManager instanceManager, SiteProfile siteProfile, FlowInstanceEvent event, boolean temporary, Map<String, String> extraElements) throws Exception {

		return createPDF(pdfTransformer, instanceManager, siteProfile, event, temporary, extraElements);
	}

	protected File createPDF(URIXSLTransformer pdfTransformer, FlowInstanceManager instanceManager, SiteProfile siteProfile, FlowInstanceEvent event, boolean temporary, Map<String, String> extraElements) throws Exception {

		if (dependencyReadLock != null) {

			dependencyReadLock.lock();
		}

		File basePDF = null;
		File pdfWithAttachments = null;

		try {
			checkRequiredDependencies();

			if (temporary && !instanceManager.getSessionAttributeHandler().isSet(TEMP_PDF_ID_FLOW_INSTANCE_MANAGER_ATTRIBUTE)) {

				String tempID = RandomUtils.getRandomString(32, RandomUtils.LOWER_CASE_CHARACTERS);

				int attempts = 1;
				while (hasTemporaryPDF(tempID)) {

					if (attempts++ > 10) {
						throw new RuntimeException("Unable to find unused PDF tempID");
					}

					tempID = RandomUtils.getRandomString(32, RandomUtils.LOWER_CASE_CHARACTERS);
				}

				instanceManager.getSessionAttributeHandler().setAttribute(TEMP_PDF_ID_FLOW_INSTANCE_MANAGER_ATTRIBUTE, tempID);
			}

			List<PDFManagerResponse> managerResponses = instanceManager.getPDFContent(this);

			Document doc = XMLUtils.createDomDocument();
			Element documentElement = doc.createElement("Document");
			doc.appendChild(documentElement);

			documentElement.appendChild(instanceManager.getFlowInstance().toXML(doc));

			Element siteProfileElement = XMLUtils.appendNewElement(doc, documentElement, "SiteProfile");

			String logotype = null;

			if (siteProfile != null) {

				File logotypeFile = siteProfile.getSettingHandler().getFile(LOGOTYPE_SETTING_ID);

				if (logotypeFile != null) {

					logotype = "file://" + logotypeFile.getAbsolutePath();
				}

				XMLUtils.appendNewElement(doc, siteProfileElement, "Name", siteProfile.getName());

				SiteProfileUtils.appendSiteProfileValues(siteProfile.getSettingHandler(), siteProfileElement, doc);

			} else if (this.siteProfileHandler != null) {

				File logotypeFile = siteProfileHandler.getGlobalSettingHandler().getFile(LOGOTYPE_SETTING_ID);

				if (logotypeFile != null) {

					logotype = "file://" + logotypeFile.getAbsolutePath();
				}

				SiteProfileUtils.appendSiteProfileValues(siteProfileHandler.getGlobalSettingHandler(), siteProfileElement, doc);
			}

			if (logotype == null) {

				logotype = defaultLogotype;
			}

			XMLUtils.appendNewCDATAElement(doc, documentElement, "Logotype", logotype);

			XMLUtils.appendNewElement(doc, documentElement, "staticStylesheets");
			XMLUtils.append(doc, documentElement, "StyleSheets", "StyleSheet", customStyleSheets);

			Timestamp submitDate;
			ImmutableFlowInstanceEvent signingEvent = null;
			List<ImmutableFlowInstanceEvent> signEvents = null;

			if (event != null) {

				submitDate = event.getAdded();

				if (event.getEventType() == EventType.SUBMITTED || Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNED_PDF.equals(event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT))) {

					documentElement.appendChild(event.toXML(doc));

					List<? extends ImmutableFlowInstanceEvent> flowInstanceEvents;

					if (extraElements != null && extraElements.containsKey("ForceLocalFlowInstanceEvents")) {

						flowInstanceEvents = instanceManager.getFlowInstance().getEvents();

					} else {

						flowInstanceEvents = browserModule.getFlowInstanceEvents((FlowInstance) instanceManager.getFlowInstance());
					}

					String signingSessionID = event.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION);

					if (!StringUtils.isEmpty(signingSessionID)) {

						signEvents = new ArrayList<ImmutableFlowInstanceEvent>(flowInstanceEvents.size());

						for (ImmutableFlowInstanceEvent flowInstanceEvent : flowInstanceEvents) {

							if (signingSessionID.equals(flowInstanceEvent.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION))) {

								String signingEventType = flowInstanceEvent.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT);

								if (flowInstanceEvent.getEventType() == EventType.SIGNED && !Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNED_PDF.equals(signingEventType)) {
									signEvents.add(flowInstanceEvent);
								}

								if ((flowInstanceEvent.getEventType() == EventType.SIGNED || flowInstanceEvent.getEventType() == EventType.SIGNING_SKIPPED) && Constants.FLOW_INSTANCE_EVENT_SIGNING_SESSION_EVENT_SIGNING_PDF.equals(signingEventType)) {
									signingEvent = flowInstanceEvent;
								}
							}
						}

						if (CollectionUtils.isEmpty(signEvents)) {

							log.warn("Signing session ID set on " + event + " but no matching sign events found for " + instanceManager.getFlowInstance());

						} else {

							XMLUtils.append(doc, documentElement, "SignEvents", signEvents);
						}
					}

					ImmutableFlowInstanceEvent paymentEvent = FlowInstanceUtils.getLastestPaymentEvent(flowInstanceEvents, true);

					if (paymentEvent != null) {

						XMLUtils.append(doc, documentElement, "PaymentEvents", Collections.singletonList(paymentEvent));
					}

				} else if (event.getEventType() == EventType.UPDATED) {

					ImmutableFlowInstanceEvent latestSubmit = FlowInstanceUtils.getLatestSubmitEvent(instanceManager.getFlowInstance());

					if (latestSubmit != null) {

						submitDate = latestSubmit.getAdded();
					}

					XMLUtils.appendNewElement(doc, documentElement, "EditDate", DateUtils.DATE_TIME_FORMATTER.format(event.getAdded()));

					if (!instanceManager.getFlowInstance().getFlow().hidesManagerDetails()) {

						Element managerElement = XMLUtils.appendNewElement(doc, documentElement, "Manager");
						managerElement.appendChild(event.getPoster().toXML(doc));
					}
				}

			} else {

				submitDate = TimeUtils.getCurrentTimestamp();
			}

			if (!(extraElements != null && extraElements.containsKey("AnonymizePoster"))) {

				XMLUtils.appendNewElement(doc, documentElement, "PostedBy", FlowInstanceUtils.getSubmitterName(instanceManager.getFlowInstance()));
				XMLUtils.appendNewElement(doc, documentElement, "PostedByCitizenID", FlowInstanceUtils.getSubmitterCitizenID(instanceManager.getFlowInstance()));
			}

			XMLUtils.appendNewCDATAElement(doc, documentElement, "SubmitDate", DateUtils.DATE_TIME_FORMATTER.format(submitDate));

			XMLUtils.append(doc, documentElement, "ManagerResponses", managerResponses);

			if (xmlDebug && xmlDebugFile != null) {

				try {
					XMLUtils.writeXMLFile(doc, xmlDebugFile, true, systemInterface.getEncoding());

				} catch (Exception e) {

					log.error("Error writing debug XML to file " + xmlDebugFile, e);
				}
			}

			if (extraElements != null) {

				for (Map.Entry<String, String> entry : extraElements.entrySet()) {

					XMLUtils.appendNewElement(doc, documentElement, entry.getKey(), entry.getValue());
				}
			}

			StringWriter writer = new StringWriter();

			XMLTransformer.transformToWriter(pdfTransformer.getTransformer(), doc, writer, StandardCharsets.UTF_8.name(), "1.1");

			String xml = writer.toString();
			
			Document document;

			try {
				if (systemInterface.getEncoding().equalsIgnoreCase(StandardCharsets.UTF_8.name())) {

					document = PDFXMLUtils.parseXML(xml);

				} else {

					document = PDFXMLUtils.parseXML(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
				}

			} catch (Exception e) {

				log.error("Error parsing XML:\n" + xml);

				throw e;
			}

			if (xhtmlDebug && xhtmlDebugFile != null) {

				try {
					XMLUtils.writeXMLFile(document, xhtmlDebugFile, true, systemInterface.getEncoding());

				} catch (Exception e) {

					log.error("Error writing debug XHTML to file " + xhtmlDebugFile, e);
				}
			}

			basePDF = createBasePDF(document, managerResponses, instanceManager.getFlowInstanceID(), event, temporary);

			List<PDFAttachment> extraAttachments = null;

			if (signEvents != null && instanceManager.getFlowInstance().getFlow().isAppendSigningSignatureToPDF()) {

				for (ImmutableFlowInstanceEvent signEvent : new ReverseListIterator<>(signEvents)) {

					String signData = signEvent.getAttributeHandler().getString(Constants.FLOW_INSTANCE_EVENT_SIGNING_DATA);

					if (signData != null) {

						String name = signatureAttachmentName + " - ";

						if (signEvent.getPoster() != null) {

							name += signEvent.getPoster().getFirstname() + " " + signEvent.getPoster().getLastname();

						} else if (signEvent.getAttributeHandler().isSet("name")) {

							name += signEvent.getAttributeHandler().getString("name");

						} else {

							name += signEvent.getAttributeHandler().getString("firstname") + " " + signEvent.getAttributeHandler().getString("lastname");
						}

						//TODO check if this encoding should be hardcoded or not, ISO?
						extraAttachments = CollectionUtils.addAndInstantiateIfNeeded(extraAttachments, new PDFInputStreamAttachment(new ByteArrayInputStream(signData.getBytes(StandardCharsets.ISO_8859_1)), name + ".txt", name));
					}
				}

				if (signingEvent != null) {

					File signingPDF = getPDF(instanceManager.getFlowInstanceID(), signingEvent.getEventID());

					extraAttachments = CollectionUtils.addAndInstantiateIfNeeded(extraAttachments, new PDFFileAttachment(signingPDF, signingPDFAttachmentName + ".pdf", signingPDFAttachmentName));

				} else {

					log.warn("No initial signing event found for " + event + ", " + instanceManager.getFlowInstance());
				}
			}

			pdfWithAttachments = addAttachments(basePDF, managerResponses, extraAttachments, instanceManager.getFlowInstance(), event, temporary, submitDate, signEvents != null, extraElements != null && extraElements.containsKey("Signing"));

			File outputFile = writePDFA(pdfWithAttachments, instanceManager, event, temporary);

			log.info("PDF for flow instance " + instanceManager.getFlowInstance() + ", event " + event + " written to " + outputFile.getAbsolutePath());

			if (event != null && !temporary) {

				setEventAttributes(event);
			}

			return outputFile;

		} catch (Exception e) {

			if (temporary) {
				instanceManager.getSessionAttributeHandler().removeAttribute(TEMP_PDF_ID_FLOW_INSTANCE_MANAGER_ATTRIBUTE);
			}

			throw e;

		} finally {

			if (!FileUtils.deleteFile(basePDF)) {

				log.warn("Unable to delete file: " + basePDF);
			}

			if (!FileUtils.deleteFile(pdfWithAttachments)) {

				log.warn("Unable to delete file: " + pdfWithAttachments);
			}

			if (dependencyReadLock != null) {

				dependencyReadLock.unlock();
			}
		}
	}

	private void setEventAttributes(FlowInstanceEvent event) throws SQLException {

		if (!event.getAttributeHandler().getPrimitiveBoolean("pdf")) {

			event.getAttributeHandler().setAttribute("pdf", "true");

			FlowInstanceEventAttribute flowInstanceEventAttribute = new FlowInstanceEventAttribute("pdf", "true");
			flowInstanceEventAttribute.setEvent(event);

			flowInstanceEventAttributeDAO.add(flowInstanceEventAttribute);
		}
	}

	private File writePDFA(File pdfWithAttachments, FlowInstanceManager instanceManager, FlowInstanceEvent event, boolean temporary) throws Exception {

		File outputFile;

		if (temporary) {

			outputFile = getTempFile(instanceManager);

		} else {

			outputFile = getFile(instanceManager.getFlowInstanceID(), event);
		}

		outputFile.getParentFile().mkdirs();

		PDDocument document = PDDocument.load(pdfWithAttachments);

		try {
			PDDocumentCatalog catalog = document.getDocumentCatalog();
			PDDocumentInformation info = document.getDocumentInformation();

			XMPMetadata metadata = XMPMetadata.createXMPMetadata();

			PDFAIdentificationSchema id = metadata.createAndAddPFAIdentificationSchema();
			id.setPart(3);
			id.setConformance("A");

			AdobePDFSchema pdfSchema = metadata.createAndAddAdobePDFSchema();
			pdfSchema.setKeywords("Open ePlatform");
			pdfSchema.setProducer("Open ePlatform");

			GregorianCalendar calendar = new GregorianCalendar();

			XMPBasicSchema basicSchema = metadata.createAndAddXMPBasicSchema();
			basicSchema.setModifyDate(calendar);
			basicSchema.setCreateDate(calendar);
			basicSchema.setCreatorTool("Open ePlatform");
			basicSchema.setMetadataDate(new GregorianCalendar());

			DublinCoreSchema dcSchema = metadata.createAndAddDublinCoreSchema();
			dcSchema.setTitle(info.getTitle());
			dcSchema.addCreator("Open ePlatform");
			dcSchema.setDescription("Open ePlatform");

			PDMetadata metadataStream = new PDMetadata(document);
			catalog.setMetadata(metadataStream);

			XmpSerializer serializer = new XmpSerializer();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			serializer.serialize(metadata, baos, false);
			metadataStream.importXMPMetadata(baos.toByteArray());

			if (!hasColorProfile(catalog)) {

				InputStream colorProfile = PDFGeneratorModule.class.getResourceAsStream("sRGB Color Space Profile.icm");

				PDOutputIntent oi = new PDOutputIntent(document, colorProfile);
				oi.setInfo("sRGB IEC61966-2.1");
				oi.setOutputCondition("sRGB IEC61966-2.1");
				oi.setOutputConditionIdentifier("sRGB IEC61966-2.1");
				oi.setRegistryName("http://www.color.org");
				catalog.addOutputIntent(oi);
			}

			document.save(outputFile);

		} finally {

			document.close();
		}

		return outputFile;
	}

	private boolean hasColorProfile(PDDocumentCatalog catalog) {

		List<PDOutputIntent> list = catalog.getOutputIntents();

		for (PDOutputIntent outputIntent : list) {

			if (outputIntent.getRegistryName() != null && outputIntent.getRegistryName().equalsIgnoreCase("http://www.color.org")) {

				return true;
			}
		}

		return false;
	}

	private File getFile(Integer flowInstanceID, FlowInstanceEvent event) {

		if (flowInstanceID == null) {

			return null;
		}

		return new File(pdfDir + File.separator + flowInstanceID + File.separator + getFileSuffix(event, false) + ".pdf");
	}

	private File getTempFile(FlowInstanceManager instanceManager) {

		String tempID = instanceManager.getSessionAttributeHandler().getString(TEMP_PDF_ID_FLOW_INSTANCE_MANAGER_ATTRIBUTE);

		return getTempFile(tempID);
	}

	private File getTempFile(String tempID) {

		if (!StringUtils.isEmpty(tempID)) {

			return new File(tempDir + File.separator + "temp-" + tempID + ".pdf");
		}

		return null;
	}

	private File addAttachments(File basePDF, List<PDFManagerResponse> managerResponses, List<PDFAttachment> extraAttachments, ImmutableFlowInstance flowInstance, FlowInstanceEvent event, boolean temporary, Timestamp submitDate, boolean signed, boolean signing) throws IOException, DocumentException {

		File pdfTempIn = basePDF;
		File pdfTempOut = null;

		int attachmentCounter = 0;

		for (PDFManagerResponse managerResponse : managerResponses) {

			for (PDFQueryResponse queryResponse : managerResponse.getQueryResponses()) {

				if (queryResponse.getAttachments() != null) {

					Iterator<PDFAttachment> it = queryResponse.getAttachments().iterator();

					while (it.hasNext()) {
						PDFAttachment attachment = it.next();

						if (attachment.isInlineAttachment() && (attachment.getName() == null || attachment.getName().toLowerCase().endsWith(".pdf"))) {

							InputStream attachmentInputStream = null;
							File tempAttachmentPDFWithPageNumber = null;

							try {
								if (attachment.isAppendPageNumber()) {

									RandomAccessFileOrArray attachmentRandomAccessFile = null;
									OutputStream tempAttachmentOutputStream = null;

									try {
										tempAttachmentPDFWithPageNumber = File.createTempFile("pdf-attachment", flowInstance.getFlowInstanceID() + "-" + getFileSuffix(event, temporary) + ".pdf", getTempDir());
										tempAttachmentOutputStream = new BufferedOutputStream(new FileOutputStream(tempAttachmentPDFWithPageNumber));

										PdfReader reader;

										if (attachment instanceof PDFFileAttachment) {

											PDFFileAttachment pdfFileAttachment = (PDFFileAttachment) attachment;

											attachmentRandomAccessFile = new RandomAccessFileOrArray(pdfFileAttachment.getFile().getAbsolutePath(), false, false);
											reader = new PdfReader(attachmentRandomAccessFile, null);

										} else {

											attachmentInputStream = attachment.getInputStream();
											reader = new PdfReader(attachmentInputStream, null);
										}

										attachmentCounter++;

										PdfStamper stamper = new PdfStamper(reader, tempAttachmentOutputStream);

										Font font;

										if (CollectionUtils.isEmpty(includedFonts)) {

											font = new Font(BaseFont.createFont(), 8f); // Defaults to non-embedded HELVETICA, IText can not embed base 14 fonts (Type 1 fonts).
											log.warn("Using non-embedded font " + font.getFamilyname() + " for inline PDF attachment page numbering.");

										} else {

											font = new Font(BaseFont.createFont(includedFonts.get(0), BaseFont.CP1252, BaseFont.EMBEDDED), 8f);
										}

										BaseFont baseFont = font.getCalculatedBaseFont(true);
										float fontHeight = baseFont.getFontDescriptor(BaseFont.ASCENT, font.getSize()) - baseFont.getFontDescriptor(BaseFont.DESCENT, font.getSize());

										StringBuilder textBuilder = new StringBuilder();

										if (signing) {

											textBuilder.append(inlineAttachmentSigning);

										} else {

											if (!flowInstance.getFlow().isHideFlowInstanceIDFromUser()) {

												textBuilder.append(inlineAttachmentFlowInstanceID + flowInstance.getFlowInstanceID());
											}

											if (signed) {
												textBuilder.append(" | " + inlineAttachmentSigned);
											}

											textBuilder.append(" | " + inlineAttachmentDate + DateUtils.DATE_TIME_FORMATTER.format(submitDate));
										}

										String submitterText = textBuilder.toString();

										int pageCount = reader.getNumberOfPages();

										for (int pageNumber = 1; pageNumber <= pageCount; pageNumber++) {

											Rectangle pageSize = reader.getPageSize(pageNumber);

											PdfContentByte pageContents = stamper.getOverContent(pageNumber);

											ColumnText columnText = new ColumnText(pageContents);

											float lineHeight = columnText.getLeading() + (columnText.getMultipliedLeading() * fontHeight) + fontHeight;

											String pageNumberText = inlineAttachmentPageNumber1 + attachmentCounter + inlineAttachmentPageNumber2 + pageNumber + inlineAttachmentPageNumber3 + pageCount;

											float pageNumberTextWidth = baseFont.getWidthPoint(pageNumberText, font.getSize()) + 0.1f;

											int submitterTextLeftPadding = 0;

											submitterTextLeftPadding = 5;
											columnText.setAlignment(com.lowagie.text.Element.ALIGN_LEFT);

											columnText.setSimpleColumn(pageSize.getLeft() + submitterTextLeftPadding, pageSize.getBottom(), pageSize.getRight(), pageSize.getBottom() + lineHeight); // llx, lly, urx, ury)
											columnText.setText(new Phrase(submitterText, font));

											int writeTextResult = columnText.go();

											if (writeTextResult != ColumnText.NO_MORE_TEXT) {
												log.warn("Unable to fit submitter text on attachment " + attachment + " page " + attachmentCounter + ": " + writeTextResult);
											}

											columnText.setAlignment(com.lowagie.text.Element.ALIGN_LEFT);
											columnText.setSimpleColumn(pageSize.getRight() - pageNumberTextWidth - 5, pageSize.getBottom(), pageSize.getRight() - 5, pageSize.getBottom() + lineHeight);
											columnText.setText(new Phrase(pageNumberText, font));

											writeTextResult = columnText.go();

											if (writeTextResult != ColumnText.NO_MORE_TEXT) {
												log.warn("Unable to fit pagenumbering text on attachment " + attachment + " page " + attachmentCounter + ": " + writeTextResult);
											}
										}

										stamper.close();
										CloseUtils.close(attachmentInputStream);

										attachmentInputStream = new FileInputStream(tempAttachmentPDFWithPageNumber);

									} catch (Exception e) {

										log.warn("Error appending page number to inline attachment " + attachment, e);

										// Reopen stream to reset position
										if (attachmentInputStream != null) {
											CloseUtils.close(attachmentInputStream);
										}

										attachmentInputStream = attachment.getInputStream();

									} finally {

										CloseUtils.close(tempAttachmentOutputStream);

										if (attachmentRandomAccessFile != null) {

											try {
												attachmentRandomAccessFile.close();
											} catch (IOException e) {}
										}
									}

								} else {

									attachmentInputStream = attachment.getInputStream();
								}

								PDFMergerUtility merger = new PDFMergerUtility();
								merger.addSource(pdfTempIn);
								merger.addSource(attachmentInputStream);

								pdfTempOut = File.createTempFile("pdf-with-attachments", flowInstance.getFlowInstanceID() + "-" + getFileSuffix(event, temporary) + ".pdf", getTempDir());
								merger.setDestinationFileName(pdfTempOut.getAbsolutePath());

								//TODO don't merge DocumentInformation or reset to src's
								// Removes StructureTreeRoot if merged document does not have a StructureTreeRoot
								merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

								if (pdfTempIn != basePDF && !FileUtils.deleteFile(pdfTempIn)) {

									log.warn("Unable to delete temp file: " + pdfTempIn);
								}

								pdfTempIn = pdfTempOut;
								pdfTempOut = null;
								it.remove();

							} catch (Exception e) {

								log.warn("Error merging PDF from attachment " + attachment + " event " + event + " for flow instance " + flowInstance, e);

								if (pdfTempOut != null && !FileUtils.deleteFile(pdfTempOut)) {

									log.warn("Unable to delete temp file: " + pdfTempOut);
								}

							} finally {

								CloseUtils.close(attachmentInputStream);

								if (tempAttachmentPDFWithPageNumber != null && !FileUtils.deleteFile(tempAttachmentPDFWithPageNumber)) {

									log.warn("Unable to delete temp file: " + tempAttachmentPDFWithPageNumber);
								}
							}
						}
					}
				}
			}
		}

		OutputStream outputStream = null;
		RandomAccessFileOrArray inputFileRandomAccess = null;
		pdfTempOut = File.createTempFile("pdf-with-attachments", flowInstance.getFlowInstanceID() + "-" + getFileSuffix(event, temporary) + ".pdf", getTempDir());

		try {
			inputFileRandomAccess = new RandomAccessFileOrArray(pdfTempIn.getAbsolutePath(), false, false);
			outputStream = new BufferedOutputStream(new FileOutputStream(pdfTempOut));

			PdfReader reader = new PdfReader(inputFileRandomAccess, null);
			PdfStamper stamper = new PdfStamper(reader, outputStream);
			PdfWriter writer = stamper.getWriter();

			PdfArray associatedFilesArray = reader.getCatalog().getAsArray(new PdfName("AF"));

			if (associatedFilesArray == null) {

				associatedFilesArray = new PdfArray();
				reader.getCatalog().put(new PdfName("AF"), associatedFilesArray);
			}

			for (PDFManagerResponse managerResponse : managerResponses) {

				for (PDFQueryResponse queryResponse : managerResponse.getQueryResponses()) {

					if (queryResponse.getAttachments() != null) {

						for (PDFAttachment attachment : queryResponse.getAttachments()) {

							try {
								PdfFileSpecification fs = StreamPdfFileSpecification.fileEmbedded(writer, attachment.getInputStream(), attachment.getName());
								writer.addFileAttachment(attachment.getDescription(), fs);
								associatedFilesArray.add(fs.getReference());

							} catch (Exception e) {
								log.error("Error appending attachment " + attachment.getName() + " from query " + queryResponse.getQueryDescriptor(), e);
							}
						}
					}
				}
			}

			if (extraAttachments != null) {
				for (PDFAttachment attachment : extraAttachments) {

					try {
						PdfFileSpecification fs = StreamPdfFileSpecification.fileEmbedded(writer, attachment.getInputStream(), attachment.getName());
						writer.addFileAttachment(attachment.getDescription(), fs);
						associatedFilesArray.add(fs.getReference());

					} catch (Exception e) {
						log.error("Error appending extra attachment " + attachment.getName(), e);
					}
				}
			}

			if (associatedFilesArray.isEmpty()) {
				reader.getCatalog().put(new PdfName("AF"), null);
			}

			stamper.close();

		} catch (IOException e) {

			FileUtils.deleteFile(pdfTempOut);
			throw e;

		} catch (DocumentException e) {

			FileUtils.deleteFile(pdfTempOut);
			throw e;

		} finally {

			CloseUtils.close(outputStream);

			if (inputFileRandomAccess != null) {

				try {
					inputFileRandomAccess.close();
				} catch (IOException e) {}
			}

			if (pdfTempIn != basePDF && !FileUtils.deleteFile(pdfTempIn)) {

				log.warn("Unable to delete temp file: " + pdfTempIn);
			}
		}

		return pdfTempOut;
	}

	protected static void addAttachment(PdfWriter writer, File file, String description) throws IOException {

		PdfFileSpecification fs = StreamPdfFileSpecification.fileEmbedded(writer, new FileInputStream(file), file.getName());
		writer.addFileAttachment(description, fs);
	}

	private File createBasePDF(Node node, List<PDFManagerResponse> managerResponses, Integer flowInstanceID, FlowInstanceEvent event, boolean temporary) throws DocumentException, IOException {

		File basePDF = File.createTempFile("basepdf", flowInstanceID + "-" + getFileSuffix(event, temporary) + ".pdf", getTempDir());

		OutputStream basePDFOutputStream = null;

		try {
			basePDFOutputStream = new BufferedOutputStream(new FileOutputStream(basePDF));

			ITextRenderer renderer = new ITextRenderer();
			ResourceLoaderAgent callback = new ResourceLoaderAgent(renderer.getOutputDevice(), managerResponses);
			callback.setSharedContext(renderer.getSharedContext());
			renderer.getSharedContext().setUserAgentCallback(callback);
			renderer.setListener(ITEXT_PDF_CREATION_LISTENER);

			if (this.includedFonts != null) {

				for (String font : includedFonts) {

					renderer.getFontResolver().addFont(font, true);
				}
			}

			renderer.setDocument((Document) node, "flowengine");
			renderer.layout();

			renderer.createPDF(basePDFOutputStream);

		} finally {

			CloseUtils.close(basePDFOutputStream);
		}

		return basePDF;
	}

	private String getFileSuffix(FlowInstanceEvent event, boolean temporary) {

		if (temporary) {

			return "temp";
		}

		return event.getEventID().toString();
	}

	@Override
	public File getTempDir() {

		if (tempDir != null) {

			return new File(tempDir);
		}

		return null;
	}

	@EventListener(channel = FlowInstanceManager.class, priority = 10)
	public void processEvent(SubmitEvent event, EventSource source) {

		if (source.isLocal()) {

			if (this.pdfStyleSheet == null || this.supportedActionIDs == null) {

				log.warn("Module " + this.moduleDescriptor + " not properly configured, refusing to create PDF for flow instance " + event.getFlowInstanceManager().getFlowInstance());
				return;
			}

			if (event.getEvent() == null || event.getEvent().getEventType() != EventType.SUBMITTED || event.getActionID() == null || !supportedActionIDs.contains(event.getActionID())) {

				return;
			}

			log.info("Generating PDF for flow instance " + event.getFlowInstanceManager().getFlowInstance() + " triggered by flow instance event " + event.getEvent() + " by user " + event.getEvent().getPoster());

			try {
				createPDF(event.getFlowInstanceManager(), event.getSiteProfile(), event.getEvent(), false, null);

			} catch (Exception t) {

				log.error("Error generating PDF for flow instance " + event.getFlowInstanceManager().getFlowInstance() + " triggered by flow instance event " + event + " by user " + event.getEvent().getPoster(), t);

			}
		}
	}

	@EventListener(channel = FlowInstance.class)
	public void processEvent(CRUDEvent<FlowInstance> event, EventSource source) {

		if (source.isLocal() && event.getAction() == CRUDAction.DELETE) {

			for (FlowInstance flowInstance : event.getBeans()) {

				try {
					File instanceDir = new File(pdfDir + File.separator + flowInstance.getFlowInstanceID());

					if (instanceDir.exists()) {

						log.info("Deleting PDF files for flow instance " + flowInstance);

						FileUtils.deleteFiles(instanceDir, null, true);

						instanceDir.delete();
					}
				} catch (Exception e) {

					log.error("Error deleting PDF files for flow instance " + flowInstance);
				}
			}

		}
	}

	@Override
	public EvaluationHandler getEvaluationHandler() {

		return evaluationHandler;
	}

	@Override
	public QueryHandler getQueryHandler() {

		return queryHandler;
	}

	@Override
	public SystemInterface getSystemInterface() {

		return systemInterface;
	}

	@Override
	public FlowEngineDAOFactory getDAOFactory() {

		return daoFactory;
	}

	@Override
	public File getPDF(Integer flowInstanceID, Integer eventID) {

		File pdfFile = new File(pdfDir + File.separator + flowInstanceID + File.separator + eventID + PDF);

		if (pdfFile.exists()) {

			return pdfFile;
		}

		return null;
	}

	@InstanceManagerDependency(required = true)
	public void setSiteProfileHandler(SiteProfileHandler siteProfileHandler) {

		if (siteProfileHandler != null) {

			siteProfileHandler.addSettingProvider(this);

		} else {

			this.siteProfileHandler.removeSettingProvider(this);
		}

		this.siteProfileHandler = siteProfileHandler;
	}

	@Override
	public List<Setting> getSiteProfileSettings() {

		return Collections.singletonList((Setting) new SingleFileUploadSetting(LOGOTYPE_SETTING_ID, "Generated PDF logotype", "The logotype used in generated PDF documents.", false, Arrays.asList(new String[] { "jpg", "png" }), 5 * BinarySizes.MegaByte));
	}

	@Override
	public List<Setting> getSiteSubProfileSettings() {

		return Collections.singletonList((Setting) new SingleFileUploadSetting(LOGOTYPE_SETTING_ID, "Generated PDF logotype", "The logotype used in generated PDF documents.", false, Arrays.asList(new String[] { "jpg", "png" }), 5 * BinarySizes.MegaByte));
	}

	@Override
	public File createTemporaryPDF(FlowInstanceManager instanceManager, SiteProfile siteProfile, User user) throws Exception {

		return createTemporaryPDF(instanceManager, siteProfile, user, null);
	}

	@Override
	public File createTemporaryPDF(FlowInstanceManager instanceManager, SiteProfile siteProfile, User user, Map<String, String> extraElements) throws Exception {

		return createTemporaryPDF(instanceManager, siteProfile, user, extraElements, null);
	}

	@Override
	public File createTemporaryPDF(FlowInstanceManager instanceManager, SiteProfile siteProfile, User user, Map<String, String> extraElements, FlowInstanceEvent tempEvent) throws Exception {

		return createPDF(instanceManager, siteProfile, tempEvent, true, extraElements);
	}

	@Override
	public boolean saveTemporaryPDF(FlowInstanceManager instanceManager, FlowInstanceEvent event) throws Exception {

		File tempFile = getTempFile(instanceManager);

		if (tempFile == null) {

			return false;
		}

		File outputFile = getFile(instanceManager.getFlowInstanceID(), event);

		outputFile.getParentFile().mkdirs();

		FileUtils.moveFile(tempFile, outputFile);

		instanceManager.getSessionAttributeHandler().removeAttribute(TEMP_PDF_ID_FLOW_INSTANCE_MANAGER_ATTRIBUTE);
		setEventAttributes(event);

		return true;
	}

	@Override
	public boolean deleteTemporaryPDF(FlowInstanceManager instanceManager) {

		File tempFile = getTempFile(instanceManager);

		return FileUtils.deleteFile(tempFile);
	}

	@Override
	public boolean hasTemporaryPDF(FlowInstanceManager instanceManager) {

		File tempFile = getTempFile(instanceManager);

		if (tempFile == null) {

			return false;
		}

		return tempFile.exists();
	}

	private boolean hasTemporaryPDF(String tempID) {

		File tempFile = getTempFile(tempID);

		if (tempFile == null) {

			return false;
		}

		return tempFile.exists();
	}

	@Override
	public File getTemporaryPDF(FlowInstanceManager instanceManager) {

		return getTempFile(instanceManager);
	}

	@Override
	public String getModuleName() {

		return moduleDescriptor.getName();
	}

	@Override
	public List<PDFByteAttachment> getPDFAttachments(File pdfFile, boolean getData) throws IOException {

		return PDFUtils.getAttachments(pdfFile, getData);
	}

	@Override
	public byte[] removePDFAttachments(File pdfFile) throws Exception {

		return PDFUtils.removeAttachments(pdfFile);
	}

	@Override
	public List<String> getIncludedFonts() {

		return this.includedFonts;
	}

	@Override
	public String getLogotype(SiteProfile siteProfile) {

		if (siteProfile != null) {

			File logotypeFile = siteProfile.getSettingHandler().getFile(LOGOTYPE_SETTING_ID);

			if (logotypeFile != null) {

				return "file://" + logotypeFile.getAbsolutePath();
			}

		} else if (siteProfileHandler != null) {

			File logotypeFile = siteProfileHandler.getGlobalSettingHandler().getFile(LOGOTYPE_SETTING_ID);

			if (logotypeFile != null) {

				return "file://" + logotypeFile.getAbsolutePath();
			}
		}

		return defaultLogotype;
	}

	@Override
	public boolean deletePDF(Integer flowInstanceID, Integer eventID) {

		try {

			File pdfFile = getPDF(flowInstanceID, eventID);

			if (pdfFile != null) {

				log.info("Removing PDF file " + pdfFile.getAbsolutePath());
				FileUtils.deleteFile(pdfFile);
			}

			return true;
			
		} catch (Exception e) {
			
			log.error("Error deleting PDF file for flowinstanceID " + flowInstanceID + " and eventID " + eventID, e);
		}
		return false;

	}
}
