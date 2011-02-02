package com.bradchen.faces.rest.data;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.bradchen.faces.rest.RestfulFacesException;

public class JsonDataAdapter implements DataFormatter, DataParser {

	private static final String MIME = "application/json";

	@Override
	public String getMimeType() {
		return MIME;
	}

	@Override
	public String format(Object data) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(data);
		} catch (JsonGenerationException exception) {
			throw new RestfulFacesException(exception);
		} catch (JsonMappingException exception) {
			throw new RestfulFacesException(exception);
		} catch (IOException exception) {
			throw new RestfulFacesException(exception);
		}
	}

	@Override
	public Object parse(String raw, Class<?> clazz) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(raw, clazz);
		} catch (JsonParseException exception) {
			throw new RestfulFacesException(exception);
		} catch (JsonMappingException exception) {
			throw new RestfulFacesException(exception);
		} catch (IOException exception) {
			throw new RestfulFacesException(exception);
		}
	}

}
