package com.bradchen.faces.rest.data;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;

import com.bradchen.faces.rest.RestfulFacesException;

public class XmlMarshaller {

	private Object data;

	public XmlMarshaller(Object data) {
		this.data = data;
	}

	public Document marshal() {
		try {
			Document result = DocumentFactory.getInstance().createDocument();
			Element root = result.addElement(data.getClass().getSimpleName());
			process(root, data);
			return result;
		} catch (IllegalArgumentException exception) {
			throw new RestfulFacesException(exception);
		} catch (IllegalAccessException exception) {
			throw new RestfulFacesException(exception);
		}
	}

	@SuppressWarnings("rawtypes")
	private void process(Element parent, Object node)
			throws IllegalArgumentException, IllegalAccessException {
		if (node instanceof Iterable) {
			processIterable(parent, (Iterable)node);
		} else if (node instanceof Map) {
			processMap(parent, (Map)node);
		} else {
			processFields(parent, node);
		}
	}

	@SuppressWarnings("rawtypes")
	private void processIterable(Element parent, Iterable iterable)
			throws IllegalArgumentException, IllegalAccessException {
		for (Object item : iterable) {
			Element element = parent.addElement(item.getClass().getSimpleName());
			process(element, item);
		}
	}

	@SuppressWarnings("rawtypes")
	private void processMap(Element parent, Map map)
			throws IllegalArgumentException, IllegalAccessException {
		Set keys = map.keySet();
		for (Object key : keys) {
			Element element = parent.addElement(key.toString());
			process(element, map.get(key));
		}
	}

	private void processFields(Element parent, Object node)
			throws IllegalArgumentException, IllegalAccessException {
		Field[] fields = node.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Element element = parent.addElement(field.getName());
			processField(element, field, node);
		}
	}

	@SuppressWarnings("rawtypes")
	private void processField(Element parent, Field field, Object node)
			throws IllegalArgumentException, IllegalAccessException {
		Object value = field.get(node);
		if (value == null) {
			return;
		}

		Class type = field.getType();
		if (type.isPrimitive()) {
			parent.addText(value.toString());
			return;
		}
		if ((value instanceof String) || (value instanceof Date)) {
			parent.addText(value.toString());
			return;
		}
		processFields(parent, value);
	}

}
