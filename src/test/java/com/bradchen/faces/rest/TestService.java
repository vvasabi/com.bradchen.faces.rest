package com.bradchen.faces.rest;

import static org.testng.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.bradchen.faces.rest.data.DataFormatter;
import com.bradchen.faces.rest.data.DataParser;

public class TestService {

	private MockContextFacade context;
	private XmlConfiguration config;
	private Service[] services;

	@BeforeMethod
	public void setup() {
		context = new MockContextFacade();
		config = new XmlConfiguration(context);
		config.configure("restful-faces.xml");
		services = new Service[config.getServices().size()];
		config.getServices().toArray(services);
	}

	@Test
	public void testServesTrue() {
		Service service = services[0];
		assertTrue(service.serves(HttpMethod.GET));
	}

	@Test
	public void testServesFalse() {
		Service service = services[0];
		assertFalse(service.serves(HttpMethod.POST));
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
	public void testParseQuery() throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Method method = Service.class.getDeclaredMethod("parseQuery", String.class);
		method.setAccessible(true);
		Service service = services[0];
		String[] result = (String[])method.invoke(service, "/test/abc/123");
		assertEquals(result[0], "123");
		assertEquals(result[1], "abc");
	}

	@Test
	public void testGetParameterOrder() throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Method method = Service.class.getDeclaredMethod("getParameterOrder");
		method.setAccessible(true);
		Service service = services[0];
		String[] params = (String[])method.invoke(service);
		assertEquals(params[0], "param2");
		assertEquals(params[1], "param1");
	}

	@Test
	public void testResolveFormatterAcceptNull() {
		Service service = services[0];
		Set<DataFormatter> formatters = config.getDataFormatters();
		DataFormatter formatter = service.resolveFormatter(formatters, context);
		assertEquals(formatter.getMimeType(), "application/json");
	}

	@Test
	public void testResolveFormatterAcceptAll() {
		context.setAcceptedContentTypes(new String[] {"*/*"});
		Service service = services[0];
		Set<DataFormatter> formatters = config.getDataFormatters();
		DataFormatter formatter = service.resolveFormatter(formatters, context);
		assertEquals(formatter.getMimeType(), "application/json");
	}

	@Test
	public void testResolveFormatterAcceptJson() {
		context.setAcceptedContentTypes(new String[] {"application/json"});
		Service service = services[0];
		Set<DataFormatter> formatters = config.getDataFormatters();
		DataFormatter formatter = service.resolveFormatter(formatters, context);
		assertEquals(formatter.getMimeType(), "application/json");
	}

	@Test
	public void testResolveParserNullContentType() {
		Service service = services[0];
		Set<DataParser> parsers = config.getDataParsers();
		DataParser parser = service.resolveParser(parsers, context);
		assertNull(parser);
	}

	@Test
	public void testResolveParserJsonContentType() {
		context.setRequestContentType("application/json; utf-8");
		Service service = services[0];
		Set<DataParser> parsers = config.getDataParsers();
		DataParser parser = service.resolveParser(parsers, context);
		assertEquals(parser.getMimeType(), "application/json");
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
