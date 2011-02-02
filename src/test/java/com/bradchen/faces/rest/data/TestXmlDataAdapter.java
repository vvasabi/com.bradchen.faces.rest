package com.bradchen.faces.rest.data;

import static org.testng.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestXmlDataAdapter {

	private XmlDataAdapter adapter;

	private MockDataObject data;
	private MockDataObject nested;

	@BeforeMethod
	public void setup() {
		adapter = new XmlDataAdapter();
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
	public void testFormat() throws IOException, DocumentException {
		String xml = adapter.format(data);
		System.out.println(xml);
		StringReader strReader = new StringReader(xml);
		SAXReader reader = new SAXReader();
		Document document = reader.read(strReader);
		Element root = document.getRootElement();
		assertEquals(root.element("name").getData(), "Test");
		assertEquals(root.element("description").getData(), "Test Description");
		assertEquals(root.element("value").getData(), "123");
		strReader.close();
	}

	@Test
	public void testFormatNestedObject() throws IOException, DocumentException {
		String xml = adapter.format(data);
		System.out.println(xml);
		StringReader strReader = new StringReader(xml);
		SAXReader reader = new SAXReader();
		Document document = reader.read(strReader);
		Element root = document.getRootElement();
		Element nested = root.element("nested");
		assertEquals(nested.element("name").getData(), "Nested Object");
		assertEquals(nested.element("description").getData(), "This object is nested.");
		assertEquals(nested.element("value").getData(), "432");
		strReader.close();
	}

	@Test
	public void testParse() {
		String xml = getTestXmlString();
		data = (MockDataObject)adapter.parse(xml, MockDataObject.class);
		assertEquals(data.getName(), "Xml Data");
		assertEquals(data.getDescription(), "simple xml data mapping");
		assertEquals(data.getValue(), 456);
	}

	@Test
	public void testParseNested() {
		String xml = getTestXmlString();
		data = (MockDataObject)adapter.parse(xml, MockDataObject.class);
		nested = data.getNested();
		assertEquals(nested.getName(), "Nested Object");
		assertEquals(nested.getDescription(), "nested description");
		assertEquals(nested.getValue(), 987);
	}

	private String getTestXmlString() {
		return "<?xml version=\"1.0\"?>"
			+ "<MockDataObject>"
				+ "<name>Xml Data</name>"
				+ "<description>simple xml data mapping</description>"
				+ "<value>456</value>"
				+ "<nested>"
					+ "<name>Nested Object</name>"
					+ "<description>nested description</description>"
					+ "<value>987</value>"
				+ "</nested>"
			+ "</MockDataObject>";
	}

}
