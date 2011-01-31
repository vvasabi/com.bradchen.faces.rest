package com.bradchen.faces.rest;

import java.util.List;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bradchen.faces.rest.data.DataFormatter;

public class TestXmlConfiguration {

	private XmlConfiguration config;
	private Service[] services;
	private DataFormatter[] formatters;

	@BeforeMethod
	public void setup() {
		config = new XmlConfiguration();
		config.configure("restful-faces.xml");
		services = new Service[config.getServices().size()];
		config.getServices().toArray(services);

		formatters = new DataFormatter[config.getDataFormatters().size()];
		config.getDataFormatters().toArray(formatters);
	}

	@Test
	public void testServicesSize() {
		assertEquals(services.length, 1);
	}

	@Test
	public void testServiceContent() {
		assertEquals(services[0].getUrl(), "/test/{param1}/{param2}");
		assertEquals(services[0].getMime(), "application/json");
		assertEquals(services[0].getHttpMethod(), HttpMethod.GET);
	}

	@Test
	public void testServiceHandler() {
		Handler handler = services[0].getHandler();
		assertEquals(handler.getBean(), "bean");
		assertEquals(handler.getMethod(), "method");

		List<String> parameters = handler.getParameters();
		assertEquals(parameters.get(0), "param1");
		assertEquals(parameters.get(1), "param2");
	}

	@Test
	public void testDataFormattersSize() {
		assertEquals(formatters.length, 1);
	}

	@Test
	public void testDataFormatterClass() {
		assertEquals(formatters[0].getMimeTypes()[0], "application/json");
	}

	@Test(expectedExceptions = ConfigurationException.class)
	public void testConfigureSecondTime() {
		config.configure();
	}

}
