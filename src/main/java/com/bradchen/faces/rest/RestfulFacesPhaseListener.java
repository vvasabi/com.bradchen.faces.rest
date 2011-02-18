package com.bradchen.faces.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResourceFactory;
import org.jboss.resteasy.util.GetRestful;

/**
 * PhaseListener that dispatches work to RESTEasy's HttpServletDispatcher, if
 * the request url is identified as a request for a REST resource.
 *
 * @author Brad Chen
 */
@SuppressWarnings("serial")
public class RestfulFacesPhaseListener implements PhaseListener {

	private static final String SETTING_BEANS = "restful-faces.beans";

	private Map<String, Class<?>> beanClasses;

	@Override
	public void afterPhase(PhaseEvent event) {
		if (beanClasses == null) {
			loadBeanClasses(event.getFacesContext());
		}

		// See if a resource is mapped to the current url.
		Object bean = getBeanForRequest(event.getFacesContext());
		if (bean == null) {
			return;
		}

		try {
			// Grab resources.
			ExternalContext ext = event.getFacesContext().getExternalContext();
			ServletConfig config = new FacesServletConfig(ext);
			HttpServletRequest request = (HttpServletRequest)ext.getRequest();
			HttpServletResponse response = (HttpServletResponse)ext.getResponse();

			// Initialize the dispatcher.
			HttpServletDispatcher dispatcher = new HttpServletDispatcher();
			dispatcher.init(config);

			// Put bean into the registry.
			Registry registry = dispatcher.getDispatcher().getRegistry();
			ResourceFactory factory = new FacesResourceFactory(bean);
			registry.addResourceFactory(factory);

			// Execute the dispatcher.
			dispatcher.service(request.getMethod(), request, response);

			// Done. Clean up.
			event.getFacesContext().responseComplete();
			dispatcher.destroy();
		} catch (ServletException exception) {
			throw new RuntimeException(exception);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void loadBeanClasses(FacesContext context) {
		beanClasses = new HashMap<String, Class<?>>();
		ExternalContext ext = context.getExternalContext();
		String settingBeans = ext.getInitParameter(SETTING_BEANS);
		if (settingBeans == null) {
			return;
		}
		String[] beanNames = settingBeans.trim().split("\\s*,\\s*");
		for (String beanName : beanNames) {
			Object bean = resolveBean(context, beanName);
			if (bean != null) {
				beanClasses.put(beanName, bean.getClass());
			}
		}
	}

	private Object resolveBean(FacesContext context, String name) {
		ELContext elContext = context.getELContext();
		ELResolver elResolver = context.getApplication().getELResolver();
		return elResolver.getValue(elContext, null, name);
	}

	private Object getBeanForRequest(FacesContext context) {
		Set<String> beanNames = beanClasses.keySet();
		for (String beanName : beanNames) {
			Class<?> clazz = beanClasses.get(beanName);
			if (GetRestful.isRootResource(clazz) &&
					isRequestedResource(context, clazz)) {
				return resolveBean(context, beanName);
			}
		}
		return null;
	}

	private boolean isRequestedResource(FacesContext context, Class<?> clazz) {
		Path path = clazz.getAnnotation(Path.class);
		String requestPath = context.getViewRoot().getViewId();
		return requestPath.startsWith(path.value() + "/");
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		// not implemented
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
