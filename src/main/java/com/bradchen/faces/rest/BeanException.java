package com.bradchen.faces.rest;

@SuppressWarnings("serial")
public class BeanException extends RuntimeException {

	public BeanException(String message) {
		super(message);
	}

	public BeanException(String message, Throwable cause) {
		super(message, cause);
	}

}
