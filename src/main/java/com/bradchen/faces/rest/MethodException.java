package com.bradchen.faces.rest;

@SuppressWarnings("serial")
public abstract class MethodException extends RuntimeException {

	public MethodException(String message) {
		super(message);
	}

	public MethodException(String message, Throwable cause) {
		super(message, cause);
	}

}
