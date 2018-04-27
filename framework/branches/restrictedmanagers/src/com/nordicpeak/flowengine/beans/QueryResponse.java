package com.nordicpeak.flowengine.beans;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import com.nordicpeak.flowengine.interfaces.ImmutableQueryDescriptor;

@XMLElement
public class QueryResponse extends GeneratedElementable{

	@XMLElement(name="HTML")
	private final String html;
	
	private final Document debugXML;
	
	private final List<ScriptTag> scripts;
	private final List<LinkTag> links;
	
	@XMLElement
	private final ImmutableQueryDescriptor queryDescriptor;

	public QueryResponse(ImmutableQueryDescriptor queryDescriptor) {
		
		this.html = null;
		this.scripts = null;
		this.links = null;
		this.queryDescriptor = queryDescriptor;
		this.debugXML = null;
	}
	
	public QueryResponse(String html, List<ScriptTag> scripts, List<LinkTag> links, ImmutableQueryDescriptor queryDescriptor) {

		super();
		this.html = html;
		this.scripts = scripts;
		this.links = links;
		this.queryDescriptor = queryDescriptor;
		this.debugXML = null;
	}

	public QueryResponse(String html, Document debugXML, List<ScriptTag> scripts, List<LinkTag> links, ImmutableQueryDescriptor queryDescriptor) {

		super();
		this.html = html;
		this.scripts = scripts;
		this.links = links;
		this.queryDescriptor = queryDescriptor;
		this.debugXML = debugXML;
	}

	public List<ScriptTag> getScripts() {

		return scripts;
	}

	public List<LinkTag> getLinks() {

		return links;
	}

	public String getHTML() {

		return html;
	}

	public ImmutableQueryDescriptor getQueryDescriptor() {
		
		return queryDescriptor;
	}

	public Document getDebugXML() {

		return debugXML;
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
}
