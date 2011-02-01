package com.bradchen.faces.rest;

import java.util.Map;

public final class XmlConfigurationFactory implements ConfigurationFactory {

	public static final String PARAM_CONFIG_FILE = "restful-faces.ConfigFile";

	private Map<String, String> parameters;

	@Override
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public Configuration createConfiguration() {
		XmlConfiguration config = new XmlConfiguration();
		if (parameters.containsKey(PARAM_CONFIG_FILE)) {
			config.configure(parameters.get(PARAM_CONFIG_FILE));
		} else {
			config.configure();
		}
		return config;
	}

}
