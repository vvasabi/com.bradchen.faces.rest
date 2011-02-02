package com.bradchen.faces.rest;

import java.io.IOException;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

@SuppressWarnings("serial")
public class RestPhaseListener implements PhaseListener {

	public static final String DEFAULT_CONFIG_FACTORY
		= "com.bradchen.faces.rest.XmlConfigurationFactory";

	public static final String PARAM_CONFIG_FACTORY
		= "restful-faces.ConfigFactory";

	Configuration config;

	@Override
	public void afterPhase(PhaseEvent event) {
		try {
			FacesContext facesContext = event.getFacesContext();
			ContextFacade context = new FacesContextFacade(facesContext);

			if (config == null) {
				configure(context);
			}

			String query = context.getQuery();
			Service service = resolveService(context.getMethod(), query);
			if (service == null) {
				return;
			}

			Object result = performService(service, context);
			if (result != null) {
				context.writeResponse(result.toString());
			}

			facesContext.responseComplete();
		} catch (IOException exception) {
			throw new RestfulFacesException(exception);
		}
	}

	Object performService(Service service, ContextFacade context) {
		String beanName = service.getHandler().getBeanName();
		Object bean = context.resolveBean(beanName);
		return service.invoke(bean, context.getQuery());
	}

	void configure(ContextFacade context) {
		String factoryClazz = DEFAULT_CONFIG_FACTORY;
		if (context.getParameters().containsKey(PARAM_CONFIG_FACTORY)) {
			factoryClazz = context.getParameters().get(PARAM_CONFIG_FACTORY);
		}
		ConfigurationFactory factory = createConfigFactory(context, factoryClazz);
		config = factory.createConfiguration(context);
	}

	private ConfigurationFactory createConfigFactory(ContextFacade context,
		String clazz) {
		try {
			return (ConfigurationFactory)context.loadClass(clazz).newInstance();
		} catch (InstantiationException exception) {
			String message = "Unable to instantiate the factory class specified.";
			throw new ConfigurationException(message, exception);
		} catch (IllegalAccessException exception) {
			String message = "Unable to instantiate the factory class specified.";
			throw new ConfigurationException(message, exception);
		} catch (ClassNotFoundException exception) {
			String message = "Unable to instantiate the factory class specified.";
			throw new ConfigurationException(message, exception);
		}
	}

	Service resolveService(HttpMethod method, String query) {
		Set<Service> services = config.getServices();
		for (Service service : services) {
			if (service.serves(method) && service.matches(query)) {
				return service;
			}
		}
		return null;
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		// nothing to be done
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
