package com.bradchen.faces.rest.data;

public interface DataFormatter {

	String getMime();

	String format(Object data);

}
