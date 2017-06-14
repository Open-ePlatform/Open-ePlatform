package com.nordicpeak.flowengine.persondatasavinginformer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import se.unlogic.standardutils.xsl.XSLVariableReader;

public class XSLVariableReaderRenamer extends XSLVariableReader {
	
	private final XSLVariableReader xslVariableReader;
	private final String getValueName;
	
	public XSLVariableReaderRenamer(XSLVariableReader xslVariableReader, String getValueName) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, URISyntaxException {
		super((Document) null);
		
		this.xslVariableReader = xslVariableReader;
		this.getValueName = getValueName;
	}
	
	@Override
	protected List<Document> getSubDocuments(Document doc, List<Document> subDocuments) throws SAXException, IOException, ParserConfigurationException, URISyntaxException, XPathExpressionException {
		return null;
	}
	
	@Override
	public String getValue(String name) {
		return xslVariableReader.getValue(getValueName);
	}
}
