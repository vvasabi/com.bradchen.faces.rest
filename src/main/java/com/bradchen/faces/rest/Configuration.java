package com.bradchen.faces.rest;

import java.util.Set;

import com.bradchen.faces.rest.data.DataAdapter;

public interface Configuration {

	Set<Service> getServices();

	Set<DataAdapter> getDataAdapters();

}
