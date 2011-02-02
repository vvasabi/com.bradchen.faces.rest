package com.bradchen.faces.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MockContextFacade implements ContextFacade {

	private HttpMethod method;
	private String query;
	private String response;
	private Map<String, String> parameters;
	private Map<String, Object> beans;

	public MockContextFacade() {
		method = HttpMethod.GET;
		query = "";
		response = "";
		parameters = new HashMap<String, String>();
		beans = new HashMap<String, Object>();
	}

	@Override
	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	@Override
	public Map<String, String> getParameters() {
		return parameters;
	}

	public void addParameter(String key, String value) {
		parameters.put(key, value);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return getClassLoader().getResourceAsStream(path);
	}

	@Override
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return getClassLoader().loadClass(name);
	}

	public String getResponse() {
		return response;
	}

	@Override
	public void writeResponse(String response) throws IOException {
		this.response += response;
	}

	@Override
	public Object resolveBean(String name) {
		return beans.get(name);
	}

	public void addBean(String name, Object bean) {
		beans.put(name, bean);
	}

	private ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

}
