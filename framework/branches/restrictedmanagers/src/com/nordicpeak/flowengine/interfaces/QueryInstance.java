package com.nordicpeak.flowengine.interfaces;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.validation.ValidationException;

import com.nordicpeak.flowengine.beans.RequestMetadata;
import com.nordicpeak.flowengine.queries.basequery.BaseQuery;

public interface QueryInstance extends ImmutableQueryInstance, Serializable {

	public void populate(HttpServletRequest req, User user, User poster, boolean allowPartialPopulation, QueryHandler queryHandler, MutableAttributeHandler attributeHandler, RequestMetadata requestMetadata) throws ValidationException;

	public void save(TransactionHandler transactionHandler, QueryHandler queryHandler) throws Throwable;

	public void reset(MutableAttributeHandler attributeHandler);

	@Override
	public MutableQueryInstanceDescriptor getQueryInstanceDescriptor();

	public void close(QueryHandler queryHandler);
	
	public abstract BaseQuery getQuery();
}
