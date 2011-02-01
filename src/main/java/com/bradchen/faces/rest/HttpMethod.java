package com.bradchen.faces.rest;

import java.io.Serializable;

public enum HttpMethod implements Serializable {

	GET,
	POST,
	PUT,
	DELETE;

	public static HttpMethod parse(String method) {
		if ("GET".equalsIgnoreCase(method)) {
			return GET;
		}
		if ("POST".equalsIgnoreCase(method)) {
			return POST;
		}
		if ("PUT".equalsIgnoreCase(method)) {
			return PUT;
		}
		if ("DELETE".equalsIgnoreCase(method)) {
			return DELETE;
		}

		String message = "Method \"" + method + "\" is not found.";
		throw new HttpMethodNotFoundException(message);
	}

}
