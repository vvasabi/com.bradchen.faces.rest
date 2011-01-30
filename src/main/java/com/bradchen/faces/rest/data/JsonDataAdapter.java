package com.bradchen.faces.rest.data;

public class JsonDataAdapter implements DataAdapter {

	private static final String MIME = "application/json";

	@Override
	public String getMime() {
		return MIME;
	}

	@Override
	public String format(Object data) {
		return null;
	}

}
