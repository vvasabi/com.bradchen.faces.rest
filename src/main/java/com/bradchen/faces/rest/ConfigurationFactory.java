package com.bradchen.faces.rest;

import java.util.Map;

public interface ConfigurationFactory {

	void setParameters(Map<String, String> parameters);

	Configuration createConfiguration();

}
