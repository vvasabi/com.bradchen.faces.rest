package com.bradchen.faces.rest.data;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import org.dom4j.Document;
import org.dom4j.Element;

import com.bradchen.faces.rest.RestfulFacesException;

public class XmlUnmarshaller {

	private Document data;
	private Class<?> resultClazz;

	public XmlUnmarshaller(Document data, Class<?> clazz) {
		this.data = data;
		this.resultClazz = clazz;
	}

	public Object unmarshal() {
		return processObject(data.getRootElement(), resultClazz);
	}

	private Object processObject(Element element, Class<?> clazz) {
		try {
			Object result = clazz.newInstance();
			Field[] fields = result.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Element fieldElement = element.element(field.getName());
				if (fieldElement == null) {
					continue;
				}
				processField(field, result, fieldElement);
			}
			return result;
		} catch (InstantiationException exception) {
			throw new RestfulFacesException(exception);
		} catch (IllegalAccessException exception) {
			throw new RestfulFacesException(exception);
		} catch (IllegalArgumentException exception) {
			throw new RestfulFacesException(exception);
		} catch (ParseException exception) {
			throw new RestfulFacesException(exception);
		}
	}

	private void processField(Field field, Object container, Element element)
			throws IllegalArgumentException, IllegalAccessException,
			ParseException {
		Class<?> clazz = field.getType();
		Object data = element.getData();
		if ((data == null) && (element.elements().size() == 0)) {
			return;
		}

		if (clazz.isPrimitive()) {
			Object parsed = parsePrimitiveValue(clazz, data);
			field.set(container, parsed);
			return;
		}
		if (String.class.equals(clazz)) {
			field.set(container, data);
			return;
		}
		if (Date.class.equals(clazz)) {
			DateFormat format = DateFormat.getDateInstance();
			field.set(container, format.parse((String)data));
			return;
		}
		// @TODO add support for Map and Iterable
		/*
		if (Arrays.asList(clazz.getInterfaces()).contains(Map.class)) {

		}*/
		field.set(container, processObject(element, clazz));
	}

	private Object parsePrimitiveValue(Class<?> clazz, Object data) {
		if (char.class.equals(clazz)) {
			String strVal = (String)data;
			if (strVal.length() != 1) {
				String message = "Invalid char: '" + strVal + "'";
				throw new RestfulFacesException(message);
			}
			return strVal.charAt(0);
		}
		if (byte.class.equals(clazz)) {
			return Byte.parseByte((String)data);
		}
		if (short.class.equals(clazz)) {
			return Short.parseShort((String)data);
		}
		if (boolean.class.equals(clazz)) {
			return Boolean.parseBoolean((String)data);
		}
		if (int.class.equals(clazz)) {
			return Integer.parseInt((String)data);
		}
		if (long.class.equals(clazz)) {
			return Long.parseLong((String)data);
		}
		if (float.class.equals(clazz)) {
			return Float.parseFloat((String)data);
		}
		if (double.class.equals(clazz)) {
			return Double.parseDouble((String)data);
		}
		String message = "Unrecognized primitive type: " + clazz.getName();
		throw new RestfulFacesException(message);
	}

}
