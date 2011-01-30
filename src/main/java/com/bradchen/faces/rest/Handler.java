package com.bradchen.faces.rest;

import java.util.Collections;
import java.util.List;

public final class Handler {

	private final String bean;

	private final String method;

	private final List<String> parameters;

	public Handler(String bean, String method, List<String> parameters) {
		this.bean = bean;
		this.method = method;
		this.parameters = Collections.unmodifiableList(parameters);
	}

	public String getBean() {
		return bean;
	}

	public String getMethod() {
		return method;
	}

	public List<String> getParameters() {
		return parameters;
	}

}
