package com.bradchen.faces.rest;

import static org.testng.Assert.*;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestService {

	private XmlConfiguration config;
	private Service[] services;

	@BeforeMethod
	public void setup() {
		config = new XmlConfiguration();
		config.configure("restful-faces.xml");
		services = new Service[config.getServices().size()];
		config.getServices().toArray(services);
	}

	@Test
	public void testMatchesTrue() {
		Service service = services[0];
		assertTrue(service.matches("/test/abc/123"));
	}

	@Test
	public void testMatchesFalse() {
		Service service = services[0];
		assertFalse(service.matches("/test123/abc/123"));
	}

	@Test
	public void testMatchesFalseExtraParameter() {
		Service service = services[0];
		assertFalse(service.matches("/test/abc/123/xyz"));
	}

	@Test
	public void testParseParameter() {
		Service service = services[0];
		String[] result = service.getSortedParameterValues("/test/abc/123");
		assertEquals(result[0], "123");
		assertEquals(result[1], "abc");
	}

	@Test
	public void testGetParameter() {
		Service service = services[0];
		String[] params = service.getSortedParameters();
		assertEquals(params[0], "param2");
		assertEquals(params[1], "param1");
	}

	@Test
	public void testInvokeMethod() {
		Service service = services[0];
		MockBean bean = new MockBean();
		String[] result = (String[])service.invoke(bean, "/test/abc/123");
		assertEquals(result[0], "123");
		assertEquals(result[1], "abc");
	}

}
