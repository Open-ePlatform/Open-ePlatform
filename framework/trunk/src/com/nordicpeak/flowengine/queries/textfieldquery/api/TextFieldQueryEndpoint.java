package com.nordicpeak.flowengine.queries.textfieldquery.api;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.NoDuplicates;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.collections.CaseInsensitiveStringComparator;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

import com.nordicpeak.flowengine.interfaces.APISource;
import com.nordicpeak.flowengine.queries.textfieldquery.TextFieldQuery;

@Table(name = "text_field_query_endpoints")
@XMLElement(name = "Endpoint")
public class TextFieldQueryEndpoint extends GeneratedElementable implements Serializable, APISource {

	private static final long serialVersionUID = -8348666528294482053L;

	public static final Field QUERIES_RELATION = ReflectionUtils.getField(TextFieldQueryEndpoint.class, "queries");
	public static final Field FIELDS_RELATION = ReflectionUtils.getField(TextFieldQueryEndpoint.class, "fields");

	@DAOManaged(autoGenerated=true)
	@Key
	@XMLElement
	private Integer endpointID;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<TextFieldQuery> queries;

	@DAOManaged
	@WebPopulate(required = true, maxLength = 255)
	@XMLElement
	private String name;

	@DAOManaged
	@WebPopulate(required = true, maxLength = 1024)
	private String address;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	private String username;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	private String password;

	@DAOManaged
	@WebPopulate(required = true)
	private String encoding;

	@DAOManaged
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@WebPopulate(required = true, maxLength = 255)
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "text_field_query_endpoint_fields", remoteValueColumnName = "name")
	@XMLElement(fixCase = true)
	private List<String> fields;
	
	public Element toXMLFull(Document doc) {
		
		Element element = toXML(doc);
		
		XMLUtils.appendNewElement(doc, element, "address", address);
		XMLUtils.appendNewElement(doc, element, "username", username);
		XMLUtils.appendNewElement(doc, element, "password", password);
		XMLUtils.appendNewElement(doc, element, "encoding", encoding);
		
		return element;
	}

	@Override
	public String toString() {

		return name + " (ID: " + endpointID + ")";
	}

	public Integer getEndpointID() {

		return endpointID;
	}

	public void setEndpointID(Integer endpointID) {

		this.endpointID = endpointID;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getAddress() {

		return address;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {

		this.username = username;
	}

	public String getPassword() {

		return password;
	}

	public void setPassword(String password) {

		this.password = password;
	}

	public String getEncoding() {

		return encoding;
	}

	public void setEncoding(String encoding) {

		this.encoding = encoding;
	}

	public Charset getCharset() {

		return Charset.forName(encoding);
	}

	public List<TextFieldQuery> getQueries() {

		return queries;
	}

	public void setQueries(List<TextFieldQuery> queries) {

		this.queries = queries;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

}
