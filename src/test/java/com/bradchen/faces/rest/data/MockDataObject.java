package com.bradchen.faces.rest.data;

public class MockDataObject {

	private String name;
	private String description;
	private int value;
	private MockDataObject nested;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public MockDataObject getNested() {
		return nested;
	}

	public void setNested(MockDataObject nested) {
		this.nested = nested;
	}

}
