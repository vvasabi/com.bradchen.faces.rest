package com.bradchen.faces.rest;

@SuppressWarnings("serial")
public class MethodNotFoundException extends MethodException {

	public MethodNotFoundException(String message) {
		super(message);
	}

	public MethodNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
