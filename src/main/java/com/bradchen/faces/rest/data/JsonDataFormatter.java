package com.bradchen.faces.rest.data;

public class JsonDataFormatter implements DataFormatter, DataParser {

	private static final String MIME = "application/json";

	@Override
	public String[] getMimeTypes() {
		return new String[] { MIME };
	}

	@Override
	public String format(Object data) {
		return null;
	}

	@Override
	public Object parse(String raw) {
		return null;
	}

}
