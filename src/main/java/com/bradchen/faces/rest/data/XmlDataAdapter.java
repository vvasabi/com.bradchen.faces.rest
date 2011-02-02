package com.bradchen.faces.rest.data;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.bradchen.faces.rest.RestfulFacesException;

public class XmlDataAdapter implements DataFormatter, DataParser {

	private static final String MIME = "application/xml";

	@Override
	public String getMimeType() {
		return MIME;
	}

	@Override
	public Object parse(String raw, Class<?> clazz) {
		try {
			StringReader reader = new StringReader(raw);
			SAXReader saxReader = new SAXReader();
			Document document;
			document = saxReader.read(reader);
			XmlUnmarshaller unmarshaller = new XmlUnmarshaller(document, clazz);
			return unmarshaller.unmarshal();
		} catch (DocumentException exception) {
			throw new RestfulFacesException(exception);
		}
	}

	@Override
	public String format(Object data) {
		try {
			XmlMarshaller marshaller = new XmlMarshaller(data);
			Document document = marshaller.marshal();
			StringWriter writer = new StringWriter();
			XMLWriter xmlWriter = new XMLWriter(writer);
			xmlWriter.write(document);
			xmlWriter.flush();
			xmlWriter.close();
			String result = writer.toString();
			writer.close();
			return result;
		} catch (IOException exception) {
			throw new RestfulFacesException(exception);
		}
	}

}
