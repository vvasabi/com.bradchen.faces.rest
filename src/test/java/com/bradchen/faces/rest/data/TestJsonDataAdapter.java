package com.bradchen.faces.rest.data;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
		Calendar calendar = Calendar.getInstance();
		calendar.set(2011, 0, 23, 12, 34, 56);
		Date date = calendar.getTime();

		adapter = new JsonDataAdapter();
		data = new MockDataObject();
		data.setName("Test");
		data.setDescription("Test Description");
		data.setValue(123);
		data.setDate(date);

		nested = new MockDataObject();
		nested.setName("Nested Object");
		nested.setDescription("This object is nested.");
		nested.setValue(432);
		data.setNested(nested);

		MockDataObject item1 = new MockDataObject();
		item1.setName("item1");
		item1.setDescription("an item");
		item1.setValue(1);

		MockDataObject item2 = new MockDataObject();
		item2.setName("item2");
		item2.setDescription("second item");
		item2.setValue(2);
		data.getMap().put("item 1",item1);
		data.getMap().put("item 2",item2);
		data.getSet().add(item1);
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
	@SuppressWarnings("rawtypes")
	public void testFormatMap() throws JsonParseException,
		JsonMappingException, IOException {
		String json = adapter.format(data);
		Map parentMap = parseJsonString(json);
		Map map = (Map)parentMap.get("map");
		Map item1 = (Map)map.get("item 1");
		Map item2 = (Map)map.get("item 2");
		assertEquals(item1.get("name"), "item1");
		assertEquals(item2.get("description"), "second item");
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testFormatSet() throws JsonParseException,
		JsonMappingException, IOException {
		String json = adapter.format(data);
		Map parentMap = parseJsonString(json);
		List set = (List)parentMap.get("set");
		Map item1 = (Map)set.get(0);
		assertEquals(item1.get("name"), "item1");
	}

	@Test
	@SuppressWarnings("rawtypes")
	public void testFormatDate() throws JsonParseException,
		JsonMappingException, IOException, ParseException {
		String json = adapter.format(data);
		Map parentMap = parseJsonString(json);
		Long timestamp = (Long)parentMap.get("date");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		assertEquals(calendar.get(Calendar.YEAR), 2011);

		// 0 for january
		assertEquals(calendar.get(Calendar.MONTH), 0);

		assertEquals(calendar.get(Calendar.DATE), 23);
		assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
		assertEquals(calendar.get(Calendar.MINUTE), 34);
		assertEquals(calendar.get(Calendar.SECOND), 56);
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

	@Test
	public void testParseMap() {
		String json = getTestJsonString();
		data = (MockDataObject)adapter.parse(json, MockDataObject.class);
		MockDataObject item = data.getMap().get("item 1");
		assertEquals(item.getName(), "map item");
		assertEquals(item.getDescription(), "item description");
		assertEquals(item.getValue(), 1);
	}

	@Test
	public void testParseSet() {
		String json = getTestJsonString();
		data = (MockDataObject)adapter.parse(json, MockDataObject.class);
		MockDataObject[] items = new MockDataObject[data.getSet().size()];
		data.getSet().toArray(items);
		assertEquals(items[0].getName(), "set item");
		assertEquals(items[0].getDescription(), "set item description");
	}

	@Test
	public void testParseDate() {
		String json = getTestJsonString();
		data = (MockDataObject)adapter.parse(json, MockDataObject.class);
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(data.getDate());
		calendar.set(Calendar.ZONE_OFFSET, 0);
		assertEquals(calendar.get(Calendar.YEAR), 2011);

		// 0 for january
		assertEquals(calendar.get(Calendar.MONTH), 0);

		assertEquals(calendar.get(Calendar.DATE), 23);
		assertEquals(calendar.get(Calendar.HOUR_OF_DAY), 12);
		assertEquals(calendar.get(Calendar.MINUTE), 34);
		assertEquals(calendar.get(Calendar.SECOND), 56);
	}

	private String getTestJsonString() {
		return "{"
			+ "\"value\": 456, "
			+ "\"description\": \"simple json data mapping\", "
			+ "\"name\": \"Json Data\","
			+ "\"date\": \"2011-01-23T12:34:56\","
			+ "\"nested\": {"
				+ "\"name\": \"nested object\","
				+ "\"description\": \"nested object description\","
				+ "\"value\": 135"
			+ "},"
			+ "\"map\": {"
				+ "\"item 1\": {"
					+ "\"name\": \"map item\","
					+ "\"description\": \"item description\","
					+ "\"value\": 1"
				+ "}"
			+ "},"
			+ "\"set\": ["
				+ "{"
					+ "\"name\": \"set item\","
					+ "\"description\": \"set item description\""
				+ "}"
			+ "]"
		+ "}";
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> parseJsonString(String json)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return (Map<String, Object>)mapper.readValue(json, HashMap.class);
	}

}
