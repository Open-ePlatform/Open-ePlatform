package com.nordicpeak.authifyclient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.settings.SettingNode;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;

@XMLElement
public class AuthifySession extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -8124971846265909352L;

	@XMLElement
	private String id;
	
	@XMLElement
	private String token;
	
	@XMLElement
	private String idp;

	@XMLElement(cdata=true)
	private String signXML;
	
	private Map<String, String> metaData;
	
	private Map<String, String> signAttributes;
	
	public AuthifySession(String id, String token, Document xmlMetaData) {
		
		this.id = id;
		this.token = token;

		metaData = new HashMap<String, String>();
		
		SettingNode settingNode = new XMLParser(xmlMetaData);
		
		setMetaData("item", settingNode);
		setMetaData("state", settingNode);
		setMetaData("idp", settingNode);
		setMetaData("uid", settingNode);
		setMetaData("mapuid", settingNode);
		setMetaData("luid", settingNode);
		setMetaData("idpuid", settingNode);
		setMetaData("email", settingNode);
		
	}
	
	public String getId() {
		return id;
	}
	
	public String getToken() {
		return token;
	}

	public String getIdp() {
		return idp;
	}

	public void setIdp(String idp) {
		this.idp = idp;
	}

	public String getProperty(String name) {
		
		return this.metaData.get(name);
	}
	
	public String getSignXML() {
		
		return signXML;
	}

	public void setSignXML(String signXML, Document signXMLDocument) {

		List<XMLParser> attributes = new XMLParser(signXMLDocument).getNodes("//Authify/data/nodeValue0/datatoauthify/user/*");
	
		if(attributes != null) {
			
			if(signAttributes == null) {
				
				signAttributes = new HashMap<String, String>();
			
			}
			
			for(XMLParser parser : attributes) {
				
				signAttributes.put(parser.getElementName(), parser.getString("."));
			}
			
		}
		
	}
	
	public Map<String, String> getSignAttributes() {
		
		return signAttributes;
	}
	
	public String getAttribute(String name) {
		
		return this.signAttributes.get(name);
	}
	
	@Override
	public String toString() {
		
		return "AuthifySession [id=" + id + ", token=" + token + "]";
	}

	@Override
	public Element toXML(Document doc) {
		
		Element element = super.toXML(doc);

		if(metaData != null) {
			
			Element metaDataElement = doc.createElement("metaData");

			element.appendChild(metaDataElement);
			
			for(String name : metaData.keySet()) {
				
				XMLUtils.appendNewElement(doc, metaDataElement, name, metaData.get(name));
				
			}
			
		}
		
		if(signAttributes != null) {
			
			Element signAttributesElement = doc.createElement("signAttributes");

			element.appendChild(signAttributesElement);
			
			for(String name : signAttributes.keySet()) {
				
				XMLUtils.appendNewElement(doc, signAttributesElement, name, signAttributes.get(name));
				
			}
			
		}
	
		return element;
	}
	
	private void setMetaData(String name, SettingNode settingNode) {
		
		String value = settingNode.getString("/Authify/data/nodeValue0/*[name()='" + name + "']");
		
		metaData.put(name, value);
		
	}
	
}
