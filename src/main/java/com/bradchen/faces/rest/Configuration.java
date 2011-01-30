package com.bradchen.faces.rest;

import java.util.Set;

import com.bradchen.faces.rest.data.DataFormatter;

public interface Configuration {

	Set<Service> getServices();

	Set<DataFormatter> getDataFormatters();

}
