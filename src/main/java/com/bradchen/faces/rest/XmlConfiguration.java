package com.bradchen.faces.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.bradchen.faces.rest.data.DataFormatter;

public final class XmlConfiguration implements Configuration {

	/**
	 * Default path to the configuration file.
	 */
	public static final String DEFAULT_CONFIG_FILE_PATH = "/WEB-INF/restful-faces.xml";

	private boolean configured;

	private ContextFacade context;

	private Set<Service> services;

	private Set<DataFormatter> formatters;

	public XmlConfiguration(ContextFacade context) {
		this.configured = false;
		this.context = context;
		this.services = new HashSet<Service>();
		this.formatters = new HashSet<DataFormatter>();
	}

	public void configure() {
		configure(DEFAULT_CONFIG_FILE_PATH);
	}

	@SuppressWarnings("unchecked")
	public void configure(String path) {
		if (configured) {
			String message = "This instance is already configured.";
			throw new ConfigurationException(message);
		}
		Document document = getXmlDocument(path);
		Element root = document.getRootElement();

		Element servicesXml = root.element("services");
		configureServices(servicesXml.elements("service"));

		Element genericFormattersXml = root.element("genericDataFormatters");
		configureDataFormatters(genericFormattersXml.elements("dataFormatter"));
		configured = true;
	}

	private Document getXmlDocument(String path) {
		try {
			InputStream stream = context.getResourceAsStream(path);
			SAXReader reader = new SAXReader();
			Document document = reader.read(stream);
			stream.close();
			return document;
		} catch (DocumentException exception) {
			String message = "Unable to parse the XML config file.";
			throw new ConfigurationException(message, exception);
		} catch (IOException exception) {
			String message = "Unable to load the XML config file.";
			throw new ConfigurationException(message, exception);
		}
	}

	private void configureServices(List<Element> services) {
		for (Element service : services) {
			configureService(service);
		}
	}

	private void configureService(Element xml) {
		String url = xml.attributeValue("url");
		if ((url == null) || "".equals(url)) {
			String message = "Service url must be defined.";
			throw new ConfigurationException(message);
		}

		HttpMethod method = getHttpMethod(xml);
		String mime = xml.attributeValue("mime");

		Element handlerElement = xml.element("handler");
		if (handlerElement == null) {
			String message = "Service handler is not defined for \""
				+ url + "\".";
			throw new ConfigurationException(message);
		}
		Handler handler = parseHandler(handlerElement);

		services.add(new Service(url, method, mime, handler));
	}

	private HttpMethod getHttpMethod(Element serviceXml) {
		try {
			return HttpMethod.parse(serviceXml.attributeValue("method"));
		} catch (HttpMethodNotFoundException exception) {
			String message = "Unrecognized or unsupported HTTP method.";
			throw new ConfigurationException(message);
		}
	}

	@SuppressWarnings("unchecked")
	private Handler parseHandler(Element xml) {
		String bean = xml.attributeValue("bean");
		if ((bean == null) || "".equals(bean)) {
			String message = "Handler bean must be defined.";
			throw new ConfigurationException(message);
		}

		String method = xml.attributeValue("method");
		if ((method == null) || "".equals(method)) {
			String message = "Handler method must be defined.";
			throw new ConfigurationException(message);
		}

		List<String> params = parseHandlerParameters(xml.elements("parameter"));

		return new Handler(bean, method, params);
	}

	private List<String> parseHandlerParameters(List<Element> xml) {
		try {
			String[] params = new String[xml.size()];
			for (Element param : xml) {
				int index = Integer.parseInt(param.attributeValue("index"));
				String name = param.attributeValue("name");
				if ((name == null) || "".equals(name)) {
					String message = "Parameter name must be defined.";
					throw new ConfigurationException(message);
				}
				params[index] = name;
			}
			return Arrays.asList(params);
		} catch (NumberFormatException exception) {
			String message = "Unable to parse parameter.";
			throw new ConfigurationException(message, exception);
		} catch (IndexOutOfBoundsException exception) {
			String message = "Unable to parse parameter.";
			throw new ConfigurationException(message, exception);
		}
	}

	private void configureDataFormatters(List<Element> formatters) {
		for (Element formatter : formatters) {
			configureDataFormatter(formatter);
		}
	}

	private void configureDataFormatter(Element xml) {
		try {
			String clazz = xml.attributeValue("class");
			if ((clazz == null) || "".equals(clazz)) {
				String message = "Formatter class must be defined.";
				throw new ConfigurationException(message);
			}
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			Object formatter = loader.loadClass(clazz).newInstance();
			if (!(formatter instanceof DataFormatter)) {
				String message = "The DataFormatter specified needs to "
					+ "implement the DataFormatter interface.";
				throw new ConfigurationException(message);
			}
			formatters.add((DataFormatter)formatter);
		} catch (InstantiationException exception) {
			String message = "Unable to instantiate the DataFormatter specified.";
			throw new ConfigurationException(message, exception);
		} catch (IllegalAccessException exception) {
			String message = "Unable to instantiate the DataFormatter specified.";
			throw new ConfigurationException(message, exception);
		} catch (ClassNotFoundException exception) {
			String message = "Could not find the DataFormatter specified.";
			throw new ConfigurationException(message, exception);
		}
	}

	@Override
	public Set<Service> getServices() {
		return Collections.unmodifiableSet(services);
	}

	@Override
	public Set<DataFormatter> getDataFormatters() {
		return Collections.unmodifiableSet(formatters);
	}

}
