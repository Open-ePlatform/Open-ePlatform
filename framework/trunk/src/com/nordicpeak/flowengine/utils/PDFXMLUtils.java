package com.nordicpeak.flowengine.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import se.unlogic.standardutils.string.StringUtils;

public class PDFXMLUtils {

	private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY;
	
	static {
		try {
			DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
			
			DOCUMENT_BUILDER_FACTORY.setFeature("http://xml.org/sax/features/namespaces", false);
			DOCUMENT_BUILDER_FACTORY.setFeature("http://xml.org/sax/features/validation", false);
			DOCUMENT_BUILDER_FACTORY.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			DOCUMENT_BUILDER_FACTORY.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Document parseXML(InputStream stream) throws SAXException, IOException, ParserConfigurationException {

		Document doc = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder().parse(stream);

		return doc;
	}
	
	public static Document parseXML(String string) throws SAXException, IOException, ParserConfigurationException {

		return parseXML(StringUtils.getInputStream(string));
	}
}
