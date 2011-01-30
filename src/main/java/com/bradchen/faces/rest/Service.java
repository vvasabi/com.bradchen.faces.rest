package com.bradchen.faces.rest;

public final class Service {

	private final String url;

	private final HttpMethod method;

	private final String mime;

	private final Handler handler;

	public Service(String url, HttpMethod method, String mime,
			Handler handler) {
		this.url = url;
		this.method = method;
		this.mime = mime;
		this.handler = handler;
	}

	public String getUrl() {
		return url;
	}

	public HttpMethod getHttpMethod() {
		return method;
	}

	public String getMime() {
		return mime;
	}

	public Handler getHandler() {
		return handler;
	}

}
