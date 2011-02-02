package com.bradchen.faces.rest;

public final class XmlConfigurationFactory implements ConfigurationFactory {

	public static final String PARAM_CONFIG_FILE = "restful-faces.ConfigFile";

	@Override
	public Configuration createConfiguration(ContextFacade context) {
		XmlConfiguration config = new XmlConfiguration(context);
		if (context.getParameters().containsKey(PARAM_CONFIG_FILE)) {
			config.configure(context.getParameters().get(PARAM_CONFIG_FILE));
		} else {
			config.configure();
		}
		return config;
	}

}
