package com.bradchen.faces.rest;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestRestPhaseListener {

	private MockContextFacade context;
	private RestPhaseListener phaseListener;

	@BeforeMethod
	public void setup() {
		context = new MockContextFacade();
		context.addParameter("restful-faces.ConfigFile", "restful-faces.xml");
		phaseListener = new RestPhaseListener();
		phaseListener.configure(context);
	}

	@Test
	public void testResolveService() {
		String query = "/test/abc/123";
		assertNotNull(phaseListener.resolveService(HttpMethod.GET, query));
	}

	@Test
	public void testPerformService() {
		MockBean bean = new MockBean();
		context.addBean("bean", bean);

		String query = "/test/abc/123";
		context.setQuery(query);

		Service service = phaseListener.resolveService(HttpMethod.GET, query);
		String[] result = (String[])phaseListener.performService(service, context);
		assertEquals(result[0], "123");
		assertEquals(result[1], "abc");
	}

}
