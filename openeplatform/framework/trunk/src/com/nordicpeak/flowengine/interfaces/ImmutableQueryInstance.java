package com.nordicpeak.flowengine.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.flowengine.beans.InstanceRequestMetadata;
import com.nordicpeak.flowengine.beans.PDFQueryResponse;
import com.nordicpeak.flowengine.beans.QueryResponse;

public interface ImmutableQueryInstance {

	//TODO add attribute handler

	public QueryResponse getShowHTML(HttpServletRequest req, User user, User poster, QueryHandler queryHandler, String updateURL, String queryRequestURL, InstanceRequestMetadata requestMetadata, AttributeHandler attributeHandler) throws Throwable;

	public QueryResponse getFormHTML(HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, QueryHandler queryHandler, boolean enableAjaxPosting, String queryRequestURL, InstanceRequestMetadata requestMetadata, AttributeHandler attributeHandler) throws Throwable;

	public PDFQueryResponse getPDFContent(QueryHandler queryHandler, AttributeHandler attributeHandler) throws Throwable;

	public ImmutableQueryInstanceDescriptor getQueryInstanceDescriptor();

	public QueryRequestProcessor getQueryRequestProcessor(HttpServletRequest req, User user, User poster, URIParser uriParser, QueryHandler queryHandler, InstanceRequestMetadata requestMetadata) throws Exception;

	public Element toExportXML(Document doc, QueryHandler queryHandler) throws Exception;
}