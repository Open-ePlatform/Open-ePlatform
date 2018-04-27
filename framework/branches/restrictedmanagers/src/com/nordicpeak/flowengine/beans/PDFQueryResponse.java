package com.nordicpeak.flowengine.beans;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;
import com.nordicpeak.flowengine.interfaces.PDFAttachment;
import com.nordicpeak.flowengine.interfaces.PDFResourceProvider;

@XMLElement
public class PDFQueryResponse extends GeneratedElementable {

	@XMLElement(name = "XHTML")
	private final String xhtml;

	private final Document debugXML;

	private final PDFResourceProvider pdfResourceProvider;

	@XMLElement
	private final ImmutableQueryDescriptor queryDescriptor;

	private final List<PDFAttachment> attachments;

	public PDFQueryResponse(String xhtml, ImmutableQueryDescriptor queryDescriptor) {

		super();
		this.xhtml = xhtml;
		this.queryDescriptor = queryDescriptor;

		this.debugXML = null;
		this.pdfResourceProvider = null;
		this.attachments = null;
	}

	public PDFQueryResponse(String xhtml, Document debugXML, ImmutableQueryDescriptor queryDescriptor, PDFResourceProvider pdfResourceProvider, List<PDFAttachment> attachments) {

		super();
		this.xhtml = xhtml;
		this.debugXML = debugXML;
		this.pdfResourceProvider = pdfResourceProvider;
		this.queryDescriptor = queryDescriptor;
		this.attachments = attachments;
	}

	@Override
	public Element toXML(Document doc) {

		Element queryResponseElement = super.toXML(doc);

		if(this.debugXML != null){

			Element debugXMLElement = doc.createElement("DebugXML");
			queryResponseElement.appendChild(debugXMLElement);

			debugXMLElement.appendChild(doc.adoptNode(debugXML.getDocumentElement()));
		}

		return queryResponseElement;
	}

	public String getXhtml() {

		return xhtml;
	}

	public Document getDebugXML() {

		return debugXML;
	}

	public PDFResourceProvider getPdfResourceProvider() {

		return pdfResourceProvider;
	}

	public ImmutableQueryDescriptor getQueryDescriptor() {

		return queryDescriptor;
	}

	public List<PDFAttachment> getAttachments() {

		return attachments;
	}
}
