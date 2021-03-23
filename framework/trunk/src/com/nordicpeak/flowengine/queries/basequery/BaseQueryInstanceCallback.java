package com.nordicpeak.flowengine.queries.basequery;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;

import com.nordicpeak.flowengine.beans.PDFQueryResponse;
import com.nordicpeak.flowengine.beans.QueryResponse;
import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.interfaces.QueryRequestProcessor;



public interface BaseQueryInstanceCallback<QI extends BaseQueryInstance> {

	public QueryResponse getShowHTML(QI queryInstance, HttpServletRequest req, User user, User poster, String updateURL, String queryRequestURL, RequestMetadata requestMetadata, AttributeHandler attributeHandler) throws Throwable;

	public QueryResponse getFormHTML(QI queryInstance, HttpServletRequest req, User user, User poster, List<ValidationError> validationErrors, boolean enableAjaxPosting, String queryRequestURL, RequestMetadata requestMetadata, AttributeHandler attributeHandler) throws Throwable;

	public PDFQueryResponse getPDFContent(QI queryInstance, AttributeHandler attributeHandler) throws Throwable;

	public void save(QI queryInstance, TransactionHandler transactionHandler) throws Throwable;

	public void populate(QI queryInstance, HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException;

	public QueryRequestProcessor getQueryRequestProcessor(QI queryInstance, HttpServletRequest req, User user) throws Exception;
}
