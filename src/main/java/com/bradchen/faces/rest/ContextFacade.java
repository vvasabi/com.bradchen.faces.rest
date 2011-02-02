package com.bradchen.faces.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface ContextFacade {

	HttpMethod getMethod();

	Map<String, String> getParameters();

	InputStream getResourceAsStream(String path);

	String getQuery();

	Class<?> loadClass(String name) throws ClassNotFoundException;

	void writeResponse(String response) throws IOException;

	Object resolveBean(String name);

}
