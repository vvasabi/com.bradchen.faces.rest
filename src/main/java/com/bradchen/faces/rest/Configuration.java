package com.bradchen.faces.rest;

import java.util.Set;

import com.bradchen.faces.rest.data.DataFormatter;
import com.bradchen.faces.rest.data.DataParser;

public interface Configuration {

	Set<Service> getServices();

	Set<DataFormatter> getDataFormatters();

	Set<DataParser> getDataParsers();

}
