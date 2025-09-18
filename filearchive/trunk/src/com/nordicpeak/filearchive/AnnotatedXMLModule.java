package com.nordicpeak.filearchive;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.webutils.http.URIParser;

public abstract class AnnotatedXMLModule extends AnnotatedForegroundModule {

	private static final Class<?> RETURN_TYPE = Elementable.class;

	@Override
	protected Class<?> getReturnType() {

		return RETURN_TYPE;
	}

	@Override
	protected ForegroundModuleResponse invoke(Method method, HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		try {
			Elementable response = (Elementable) method.invoke(this, req, res, user, uriParser);

			if (response != null) {

				ConnectorUtils.sendXML(res, response, systemInterface.getEncoding());
			}

			return null;
		} catch (InvocationTargetException e) {
			throw e.getTargetException();
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return null;
	}
}