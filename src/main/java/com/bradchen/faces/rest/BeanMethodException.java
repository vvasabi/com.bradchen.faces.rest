package com.bradchen.faces.rest;

@SuppressWarnings("serial")
public class BeanMethodException extends BeanException {

	public BeanMethodException(String message) {
		super(message);
	}

	public BeanMethodException(String message, Throwable cause) {
		super(message, cause);
	}

}
