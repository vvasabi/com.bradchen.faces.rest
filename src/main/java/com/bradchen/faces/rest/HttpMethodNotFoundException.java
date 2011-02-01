package com.bradchen.faces.rest;

@SuppressWarnings("serial")
public class HttpMethodNotFoundException extends HttpMethodException {

	public HttpMethodNotFoundException(String message) {
		super(message);
	}

	public HttpMethodNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
