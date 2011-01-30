package com.bradchen.faces.rest;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class TestHttpMethod {

	@Test
	public void testParseGet() {
		assertEquals(HttpMethod.parse("get"), HttpMethod.GET);
	}

	@Test
	public void testParsePost() {
		assertEquals(HttpMethod.parse("POST"), HttpMethod.POST);
	}

	@Test
	public void testParsePut() {
		assertEquals(HttpMethod.parse("pUt"), HttpMethod.PUT);
	}

	@Test
	public void testParseDelete() {
		assertEquals(HttpMethod.parse("dELEte"), HttpMethod.DELETE);
	}

}
