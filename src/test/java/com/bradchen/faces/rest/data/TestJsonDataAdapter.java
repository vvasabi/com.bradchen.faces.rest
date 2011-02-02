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
	private MockDataObject nested;

	@BeforeMethod
	public void setup() {
		adapter = new JsonDataAdapter();
		data = new MockDataObject();
		data.setName("Test");
		data.setDescription("Test Description");
		data.setValue(123);

		nested = new MockDataObject();
		nested.setName("Nested Object");
		nested.setDescription("This object is nested.");
		nested.setValue(432);
		data.setNested(nested);
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
	@SuppressWarnings("unchecked")
	public void testFormatNested() throws JsonParseException,
			JsonMappingException, IOException {
		String json = adapter.format(data);
		Map<String, Object> parentMap = parseJsonString(json);
		Map<String, Object> nestedMap = (Map<String, Object>)parentMap.get("nested");
		assertEquals(nestedMap.get("name"), "Nested Object");
		assertEquals(nestedMap.get("description"), "This object is nested.");
		assertEquals(nestedMap.get("value"), 432);
	}

	@Test
	public void testParse() {
		String json = getTestJsonString();
		data = (MockDataObject)adapter.parse(json, MockDataObject.class);
		assertEquals(data.getName(), "Json Data");
		assertEquals(data.getDescription(), "simple json data mapping");
		assertEquals(data.getValue(), 456);
	}

	@Test
	public void testParseNested() {
		String json = getTestJsonString();
		data = (MockDataObject)adapter.parse(json, MockDataObject.class);
		nested = data.getNested();
		assertEquals(nested.getName(), "nested object");
		assertEquals(nested.getDescription(), "nested object description");
		assertEquals(nested.getValue(), 135);
	}

	private String getTestJsonString() {
		return "{"
			+ "\"value\": 456, "
			+ "\"description\": \"simple json data mapping\", "
			+ "\"name\": \"Json Data\","
			+ "\"nested\": {"
				+ "\"name\": \"nested object\","
				+ "\"description\": \"nested object description\","
				+ "\"value\": 135"
			+ "}"
		+ "}";
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> parseJsonString(String json)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (Map<String, Object>)mapper.readValue(json, HashMap.class);
	}

}
