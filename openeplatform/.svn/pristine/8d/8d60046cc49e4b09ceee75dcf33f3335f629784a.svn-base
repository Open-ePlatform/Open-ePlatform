package com.nordicpeak.flowengine.queries.fileuploadquery;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.interfaces.QueryHandler;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;
import com.nordicpeak.flowengine.interfaces.StringValueQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryInstance;
import com.nordicpeak.flowengine.queries.basequery.BaseQueryUtils;

@Table(name = "file_upload_query_instances")
@XMLElement
public class FileUploadQueryInstance extends BaseQueryInstance implements StringValueQueryInstance{

	private static final long serialVersionUID = -7761759005604863873L;

	public static final Field QUERY_RELATION = ReflectionUtils.getField(FileUploadQueryInstance.class, "query");
	public static final Field FILES_RELATION = ReflectionUtils.getField(FileUploadQueryInstance.class, "files");

	private int temporaryFileCounter = 1;

	private String instanceManagerID;

	@DAOManaged
	@Key
	@XMLElement
	private Integer queryInstanceID;

	@DAOManaged(columnName = "queryID")
	@ManyToOne
	@XMLElement
	private FileUploadQuery query;

	@DAOManaged
	@OneToMany
	@XMLElement(fixCase=true)
	private List<FileDescriptor> files;

	public Integer getQueryInstanceID() {

		return queryInstanceID;
	}

	public void setQueryInstanceID(Integer queryInstanceID) {

		this.queryInstanceID = queryInstanceID;
	}

	@Override
	public FileUploadQuery getQuery() {

		return query;
	}

	public void setQuery(FileUploadQuery query) {

		this.query = query;
	}

	@Override
	public void reset(MutableAttributeHandler attributeHandler) {

		this.setFiles(null);
		
		if(query.isSetAsAttribute()){

			resetAttribute(attributeHandler);
		}
		
		super.reset(attributeHandler);
	}
	
	public void setAttribute(MutableAttributeHandler attributeHandler){
		
		if(!CollectionUtils.isEmpty(files)){
			
			StringBuilder filenameBuilder = new StringBuilder();
			
			for(FileDescriptor file : files) {
				
				filenameBuilder.append(file.getName() + ", ");
			}
			
			String filenameString = filenameBuilder.substring(0, filenameBuilder.length()-2);
			
			attributeHandler.setAttribute(query.getAttributeName(), filenameString);
		}
	}
	
	public void resetAttribute(MutableAttributeHandler attributeHandler){
		
		attributeHandler.removeAttribute(query.getAttributeName());
	}

	public void copyQueryValues() {

	}

	@Override
	public String toString() {

		return "FileUploadQueryInstance (queryInstanceID: " + queryInstanceID + ")";
	}

	public List<FileDescriptor> getFiles() {

		return files;
	}

	public void setFiles(List<FileDescriptor> files) {

		this.files = files;
	}

	public synchronized int getNextTemporaryFileID() {

		return temporaryFileCounter++;
	}

	@Override
	public void close(QueryHandler queryHandler) {

		queryHandler.getQueryProvider(this.getQueryInstanceDescriptor().getQueryDescriptor().getQueryTypeID(), FileUploadQueryProviderModule.class).close(this);
	}

	public String getInstanceManagerID() {

		return instanceManagerID;
	}

	public void setInstanceManagerID(String instanceManagerID) {

		this.instanceManagerID = instanceManagerID;
	}

	@Override
	public QueryRequestProcessor getQueryRequestProcessor(HttpServletRequest req, User user, User poster, URIParser uriParser, QueryHandler queryHandler, InstanceRequestMetadata requestMetadata) throws Exception {

		return BaseQueryUtils.getGenericQueryInstanceProvider(this.getClass(), queryHandler, queryInstanceDescriptor.getQueryDescriptor().getQueryTypeID()).getQueryRequestProcessor(this, req, user, poster, uriParser, requestMetadata);
	}

	@Override
	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception {

		Element element = getBaseExportXML(doc);

		queryHandler.getQueryProvider(queryInstanceDescriptor.getQueryDescriptor().getQueryTypeID(), FileUploadQueryProviderModule.class).appendFileExportXML(doc, element, this);

		return element;
	}
	
	@Override
	public String getStringValue() {

		if(files == null){

			return null;
		}

		StringBuilder stringBuilder = new StringBuilder();

		for(FileDescriptor fileDescriptor : files){

			if(stringBuilder.length() != 0){

				stringBuilder.append(", ");
			}

			stringBuilder.append(fileDescriptor.getName() + " (" + BinarySizeFormater.getFormatedSize(fileDescriptor.getSize()) + ")");
		}

		return stringBuilder.toString();
	}
}
