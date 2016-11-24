package com.nordicpeak.authifyclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.hash.HashAlgorithms;
import se.unlogic.standardutils.hash.HashUtils;
import se.unlogic.standardutils.settings.SettingNode;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLParser;
import se.unlogic.standardutils.xml.XMLUtils;

public class AuthifyClient {

	protected final Logger log = Logger.getLogger(this.getClass());
	
	protected static String VERSION = "8.6";

	protected static String RESELLER_ID = "8953565";

	protected static String AUTHIFY_SERVER = "http://loginserver1.authify.com";

	protected static String AUTHIFY_TOKEN_PARAMETER = "authify_response_token";
	
	protected static String AUTHIFY_SESSION_PREFIX = "authify_session";

	protected static String SSN_ATTRIBUTE_NAME = "socialnr";
	
	protected String apiKey;

	protected String secretKey;

	protected String hashedRequestToken;
	
	protected Map<String, String> authifyParameters;
	
	public AuthifyClient(String apiKey, String secretKey) {

		this.apiKey = apiKey;
		this.secretKey = secretKey;
		
		hashedRequestToken = HashUtils.hash(apiKey + secretKey, HashAlgorithms.MD5);
		
		authifyParameters = new HashMap<String, String>();
		
		authifyParameters.put("api_key", apiKey);
		authifyParameters.put("secret_key", secretKey);
		authifyParameters.put("authify_request_token", hashedRequestToken);
		authifyParameters.put("loginparameters", "");
		authifyParameters.put("function", "require_login");
		authifyParameters.put("reseller_id", RESELLER_ID);
		authifyParameters.put("v", VERSION);
		
	}

	public void login(String uniqueID, User user, String callbackURL, HttpServletResponse res) throws IOException {

		log.info("User " + user + " logging in to authify service");
		
		HashMap<String, String> parameters = new HashMap<String, String>();

		parameters.putAll(authifyParameters);
		parameters.put("idp", "noauth");
		parameters.put("uri", callbackURL);
		parameters.put("luid", uniqueID);
		
		sendHTTPPOSTRequestResponse(AUTHIFY_SERVER + "/request/", parameters);
		
		res.sendRedirect(AUTHIFY_SERVER + "/tokenidx.php?authify_request_token=" + hashedRequestToken);

	}
	
	public void logout(AuthifySession session, HttpServletRequest req) throws IOException {
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("authify_checksum", session.getToken());
		parameters.put("v", VERSION);
		
		sendHTTPPOSTRequestResponse(AUTHIFY_SERVER + "/out/", parameters) ;
		
		req.getSession().removeAttribute(AUTHIFY_SESSION_PREFIX + ":" + session.getId());
		
	}
	
	public AuthifySession getAuthifySession(String uniqueID, User user, HttpServletRequest req, boolean createNew) throws IOException {
		
		AuthifySession authifySession = null;
		
		if((authifySession = (AuthifySession) req.getSession().getAttribute(AUTHIFY_SESSION_PREFIX + ":" + uniqueID)) != null) {
			
			log.info("User " + user + " requesting authify session " + uniqueID);
			
			if(isLoggedIn(authifySession)) {
				
				return authifySession;
				
			}
			
			req.getSession().removeAttribute(AUTHIFY_SESSION_PREFIX + ":" + uniqueID);
			
		}
		
		if(createNew) {
		
			return createAuthifySession(uniqueID, user, req);
		
		}
		
		return null;
		
	}
	
 	public Map<String, String> getUpdatedSignAttributes(AuthifySession session, HttpServletRequest req) throws IOException {
 		
		try {

			String signXML = getTokenMetaData("extradataprofile:" + session.getIdp() + "_data", session.getToken());
			
			if(!StringUtils.isEmpty(signXML)) {
				
				Document signXMLDocument = XMLUtils.parseXML(signXML, false, true);
				
				session.setSignXML(signXML, signXMLDocument);
			
				saveAuthifySession(session, req);
			
				return session.getSignAttributes();
				
			}
			
		} catch (Exception e) {
			
			log.error("Unable to get signing information for session " + session, e);
			
		}
		
		return null;
		
 	}
 	
	private AuthifySession createAuthifySession(String uniqueID, User user, HttpServletRequest req) throws IOException {
		
		AuthifySession authifySession = null;
		
		if(req.getParameter(AUTHIFY_TOKEN_PARAMETER) != null) {
			
			log.info("Creating authify session for user " + user);
			
			String token = req.getParameter(AUTHIFY_TOKEN_PARAMETER);
			
			String xmlMetaData = getTokenMetaData(token);
			
			try {

				Document metaData = XMLUtils.parseXML(xmlMetaData, false, true);
				
				authifySession = new AuthifySession(uniqueID, token, metaData);
				
				saveAuthifySession(authifySession, req);
				
			} catch (Exception e) {
				
				log.error("Unable to create authify session for user " + user, e);			
			
			} 			
			
		}
		
		return authifySession;
		
	}
	
	public boolean isLoggedIn(AuthifySession session) {
		
		String state = this.getProperty(session, "state");
		
		if(state != null && state.equalsIgnoreCase("login")) {
			
			return false;
			
		}
		
		return true;
		
	}
	
	private String getProperty(AuthifySession session, String name) {

		try {

			String xmlMetaData = getTokenMetaData(session.getToken());

			Document doc = XMLUtils.parseXML(xmlMetaData, false, true);
			
			SettingNode xmlSetting = new XMLParser(doc);
			
			return xmlSetting.getString("/Authify/*[name()='"+ name + "']");
			
		} catch (Exception e) {

			log.error("Unable to get property " + name);

		}

		return null;
	}

	public void sign(String idp, String signingData, AuthifySession session, String callbackURL, User user, HttpServletRequest req, HttpServletResponse res) throws Exception {

		log.info("Signing data using authify session " + session + " for user " + user);
		
		session.setIdp(idp);
		
		saveAuthifySession(session, req);
		
		Document doc = XMLUtils.createDomDocument();
		
		Element signDataElement = doc.createElement("signdata");

		doc.appendChild(signDataElement);
		
		XMLUtils.appendNewElement(doc, signDataElement, "data_to_sign", signingData);
		XMLUtils.appendNewElement(doc, signDataElement, "nonvisibledata", "Ingen information att visa");
		XMLUtils.appendNewElement(doc, signDataElement, "item", session.getProperty("item"));
		XMLUtils.appendNewElement(doc, signDataElement, "logged_in_idp", session.getProperty("idp"));
		XMLUtils.appendNewElement(doc, signDataElement, "uid", session.getProperty("uid"));
		XMLUtils.appendNewElement(doc, signDataElement, "mapuid", session.getProperty("mapuid"));
		XMLUtils.appendNewElement(doc, signDataElement, "luid", session.getProperty("idpuid"));
		XMLUtils.appendNewElement(doc, signDataElement, "name", session.getProperty("name"));
		
		this.store(doc, session);
		
		HashMap<String, String> parameters = new HashMap<String, String>();

		parameters.putAll(authifyParameters);
		parameters.put("uri", callbackURL);
		parameters.put("idp", idp);
		parameters.put("luid", session.getId());
		
		sendHTTPPOSTRequestResponse(AUTHIFY_SERVER + "/request/", parameters);

		res.sendRedirect(AUTHIFY_SERVER + "/tokenidx.php?authify_request_token=" + hashedRequestToken);

	}
	
	private void store(Document doc, AuthifySession session) throws Exception {

		String xml = XMLUtils.toString(doc, "UTF-8", false);
		
		HashMap<String, String> parameters = new HashMap<String, String>();
		
		parameters.put("secret_key", secretKey);
		parameters.put("api_key", apiKey);
		parameters.put("function", "ExtradataProfiles");
		parameters.put("v", VERSION);
		
		parameters.put("authify_reponse_token", session.getToken());
		parameters.put("extradata", URLEncoder.encode(xml, "UTF-8"));

		sendHTTPPOSTRequestResponse(AUTHIFY_SERVER + "/store/", parameters);

	}
	
	private String getTokenMetaData(String token) throws IOException {
		
		return this.getTokenMetaData("soap", token);
	}
	
	private String getTokenMetaData(String format, String token) throws IOException {

		HashMap<String, String> parameters = new HashMap<String, String>();

		parameters.put("api_key", apiKey);
		parameters.put("uri", "");
		parameters.put("secret_key", secretKey);
		parameters.put("function", "get_response");
		parameters.put("protocol", format);
		parameters.put("v", VERSION);

		parameters.put("authify_checksum", token);
		
		return sendHTTPPOSTRequestResponse(AUTHIFY_SERVER + "/json/", parameters);

	}
	
	private void saveAuthifySession(AuthifySession session, HttpServletRequest req) {
		
		req.getSession().setAttribute(AUTHIFY_SESSION_PREFIX + ":" + session.getId(), session);
		
	}

	private String sendHTTPPOSTRequestResponse(String server, Map<String, String> parameters) throws IOException {

		URL url = new URL(server);
		
		URLConnection connection = url.openConnection();
		
		connection.setDoOutput(true);
		
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
		
		String post_ = "";

		for (Map.Entry<String, String> entry : parameters.entrySet()) {
			
			post_ += "&";
			post_ += entry.getKey() + "=" + entry.getValue();
			
		}

		out.write(post_);

		out.close();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		
		String decodedString;
		
		String decoded = "";
		
		while ((decodedString = in.readLine()) != null) {
			decoded = decoded + decodedString;
		
		}
		
		in.close();

		return decoded;
		
	}

}
