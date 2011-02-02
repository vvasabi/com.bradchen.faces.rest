package com.bradchen.faces.rest;

@SuppressWarnings("serial")
public class RestfulFacesException extends RuntimeException {

	public RestfulFacesException(String message) {
		super(message);
	}

	public RestfulFacesException(Throwable cause) {
		super(cause);
	}

	public RestfulFacesException(String message, Throwable cause) {
		super(message, cause);
	}

}
