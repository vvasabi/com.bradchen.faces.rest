package com.bradchen.faces.rest.data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestJsonDataAdapter {

	private JsonDataAdapter adapter;

	private MockDataObject data;

	@BeforeMethod
	public void setup() {
		adapter = new JsonDataAdapter();
		data = new MockDataObject();
		data.setName("Test");
		data.setDescription("Test Description");
		data.setValue(123);
	}

	@Test
	public void testFormat() throws JsonParseException, JsonMappingException,
			IOException {
		String json = adapter.format(data);
		Map<String, Object> map = parseJsonString(json);
		assertEquals(map.get("name"), "Test");
		assertEquals(map.get("value"), 123);
		assertEquals(map.get("description"), "Test Description");
	}

	@Test
	public void testParse() {
		String json = "{" +
				"\"value\": 456, " +
				"\"description\": \"simple json data mapping\", " +
				"\"name\": \"Json Data\"" +
			"}";
		data = (MockDataObject)adapter.parse(json, MockDataObject.class);
		assertEquals(data.getName(), "Json Data");
		assertEquals(data.getDescription(), "simple json data mapping");
		assertEquals(data.getValue(), 456);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> parseJsonString(String json)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (Map<String, Object>)mapper.readValue(json, HashMap.class);
	}

}
