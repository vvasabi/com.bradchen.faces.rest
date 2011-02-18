package com.bradchen.faces.rest;

import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.HttpResponse;
import org.jboss.resteasy.spi.InjectorFactory;
import org.jboss.resteasy.spi.ResourceFactory;

/**
 * JSF implementation for the ResourceFactory. This class essentially does not
 * do much -- it just wraps around a bean instance into the ResourceFactory
 * interface.
 *
 * @author Brad Chen
 */
public final class FacesResourceFactory implements ResourceFactory {

	private final Object bean;

	/**
	 * Creates a FacesResourceFactory with the bean instance specified.
	 *
	 * @param bean bean instance
	 */
	public FacesResourceFactory(Object bean) {
		this.bean = bean;
	}

	@Override
	public Object createResource(HttpRequest request, HttpResponse response,
			InjectorFactory factory) {
		return bean;
	}

	@Override
	public Class<?> getScannableClass() {
		return bean.getClass();
	}

	@Override
	public void registered(InjectorFactory factory) {
		// not implemented
	}

	@Override
	public void requestFinished(HttpRequest request, HttpResponse response,
			Object resource) {
		// not implemented
	}

	@Override
	public void unregistered() {
		// not implemented
	}

}
