package com.nordicpeak.flowengine.interfaces;

import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface XMLDocumentFilter {

	public void processXMLDocument(ImmutableFlowInstance flowInstance, Document doc, Element element) throws SQLException;

	public boolean shouldProcessXMLDocument(ImmutableFlowInstance flowInstance) throws SQLException;
	
	public String getContainingElementName();

}
