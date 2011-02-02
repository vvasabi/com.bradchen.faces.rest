package com.bradchen.faces.rest.data;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MockDataObject {

	private String name;
	private String description;
	private int value;
	private Date date;
	private MockDataObject nested;
	private Map<String, MockDataObject> map;
	private Set<MockDataObject> set;

	public MockDataObject() {
		date = new Date();
		map = new HashMap<String, MockDataObject>();
		set = new HashSet<MockDataObject>();
	}

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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Map<String, MockDataObject> getMap() {
		return map;
	}

	public void setMap(Map<String, MockDataObject> map) {
		this.map = map;
	}

	public Set<MockDataObject> getSet() {
		return set;
	}

	public void setSet(Set<MockDataObject> set) {
		this.set = set;
	}

}
