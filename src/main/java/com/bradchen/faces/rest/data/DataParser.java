package com.bradchen.faces.rest.data;

public interface DataParser extends DataAdapter {

	Object parse(String raw);

}
