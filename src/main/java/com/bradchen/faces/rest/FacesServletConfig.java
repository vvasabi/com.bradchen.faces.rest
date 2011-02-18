package com.bradchen.faces.rest;

import java.util.Collections;
import java.util.Enumeration;

import javax.faces.context.ExternalContext;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * Faces implementation for ServletConfig.
 *
 * @author Brad Chen
 */
public final class FacesServletConfig implements ServletConfig {

	private final ExternalContext context;

	/**
	 * Creates a FacesServletConfig using the ExternalContext from FacesContext.
	 *
	 * @param context ExternalContext from FacesContext
	 */
	public FacesServletConfig(ExternalContext context) {
		this.context = context;
	}

	@Override
	public String getInitParameter(String key) {
		return context.getInitParameter(key);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Enumeration getInitParameterNames() {
		return Collections.enumeration(context.getInitParameterMap().keySet());
	}

	@Override
	public ServletContext getServletContext() {
		return (ServletContext)context.getContext();
	}

	@Override
	public String getServletName() {
		return FacesServlet.class.getName();
	}

}
