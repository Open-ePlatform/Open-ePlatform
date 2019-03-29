package com.nordicpeak.flowengine.queries.childquery.filterapi;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;

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

import com.nordicpeak.flowengine.queries.childquery.beans.ChildQuery;

@Table(name = "child_query_filter_endpoints")
@XMLElement
public class ChildQueryFilterEndpoint extends GeneratedElementable implements Serializable {

	private static final long serialVersionUID = -8348666528294482053L;

	public static final Field QUERIES_RELATION = ReflectionUtils.getField(ChildQueryFilterEndpoint.class, "queries");
	public static final Field FIELDS_RELATION = ReflectionUtils.getField(ChildQueryFilterEndpoint.class, "fields");

	@DAOManaged
	@Key
	@XMLElement
	private Integer endpointID;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase = true)
	private List<ChildQuery> queries;

	@DAOManaged
	@WebPopulate(required = true, maxLength = 255)
	@XMLElement
	private String name;

	@DAOManaged
	@WebPopulate(required = true, maxLength = 1024)
	@XMLElement
	private String address;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String username;

	@DAOManaged
	@WebPopulate(maxLength = 255)
	@XMLElement
	private String password;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private String encoding;

	@DAOManaged
	@SplitOnLineBreak
	@NoDuplicates(comparator = CaseInsensitiveStringComparator.class)
	@WebPopulate
	@OneToMany(autoAdd = true, autoGet = true, autoUpdate = true)
	@SimplifiedRelation(table = "child_query_filter_endpoint_fields", remoteValueColumnName = "name")
	@XMLElement(fixCase = true)
	private List<String> fields;

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

	public List<ChildQuery> getQueries() {

		return queries;
	}

	public void setQueries(List<ChildQuery> queries) {

		this.queries = queries;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

}
