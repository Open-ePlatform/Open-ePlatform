package com.nordicpeak.filearchive;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;

import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;


public class ConnectorUtils {

	public static SimpleForegroundModuleResponse sendXML(HttpServletResponse res, Elementable bean, String encoding) throws TransformerException {

		Document document = XMLUtils.createDomDocument();

		document.appendChild(bean.toXML(document));

		res.setCharacterEncoding("ISO-8859-1");
		res.setContentType("text/xml");

		try {
			XMLUtils.writeXML(document, res.getOutputStream(), false, "ISO-8859-1");
		} catch (TransformerFactoryConfigurationError e) {
		} catch (TransformerException e) {
		} catch (IOException e) {}

		return null;
	}
}
