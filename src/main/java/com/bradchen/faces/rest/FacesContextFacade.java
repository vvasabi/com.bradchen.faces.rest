package com.bradchen.faces.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FacesContextFacade implements ContextFacade {

	private FacesContext context;

	@Override
	public HttpMethod getMethod() {
		return HttpMethod.parse(getRequest().getMethod());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, String> getParameters() {
		return context.getExternalContext().getInitParameterMap();
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return context.getExternalContext().getResourceAsStream(path);
	}

	@Override
	public String getQuery() {
		return context.getViewRoot().getViewId();
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		return loader.loadClass(name);
	}

	@Override
	public Object resolveBean(String name) {
		Application app = context.getApplication();
		ELResolver elResolver = app.getELResolver();
		ELContext elContext = context.getELContext();
		return elResolver.getValue(elContext, null, name);
	}

	@Override
	public void writeResponse(String response) throws IOException {
		try {
			getResponse().getWriter().write(response);
		} catch (IOException exception) {
			// @TODO throw exception
		}
	}

	public FacesContextFacade(FacesContext context) {
		this.context = context;
	}

	private HttpServletResponse getResponse() {
		return (HttpServletResponse)context.getExternalContext().getResponse();
	}

	private HttpServletRequest getRequest() {
		return (HttpServletRequest)context.getExternalContext().getRequest();
	}

}
