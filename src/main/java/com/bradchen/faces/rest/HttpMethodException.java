package com.bradchen.faces.rest;

@SuppressWarnings("serial")
public abstract class HttpMethodException extends RuntimeException {

	public HttpMethodException(String message) {
		super(message);
	}

	public HttpMethodException(String message, Throwable cause) {
		super(message, cause);
	}

}
