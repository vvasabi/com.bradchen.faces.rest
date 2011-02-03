package com.bradchen.faces.rest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bradchen.faces.rest.data.DataFormatter;
import com.bradchen.faces.rest.data.DataParser;

public final class Service {

	/**
	 * Regex pattern to match parameters in service urls.
	 */
	public static final String PATTERN_PARAM = "\\{([\\w]+)\\}";

	private final String url;

	private final Pattern regex;

	private final Pattern paramPattern;

	private final HttpMethod method;

	private final String mime;

	private final Handler handler;

	public Service(String url, HttpMethod method, String mime,
			Handler handler) {
		this.url = url;
		this.regex = createRegexFromUrl();
		this.paramPattern = Pattern.compile(PATTERN_PARAM);
		this.method = method;
		this.mime = mime;
		this.handler = handler;
	}

	private Pattern createRegexFromUrl() {
		// escape dots
		String regex = url.replaceAll("\\.", "\\.");

		// replace parameters with patterns
		regex = regex.replaceAll("\\{[^}]+\\}", "([^/]+)");
		return Pattern.compile(regex);
	}

	public String getUrl() {
		return url;
	}

	public HttpMethod getHttpMethod() {
		return method;
	}

	public String getMime() {
		return mime;
	}

	public Handler getHandler() {
		return handler;
	}

	public boolean serves(HttpMethod httpMethod) {
		if (httpMethod == null) {
			return true;
		}
		return method.equals(httpMethod);
	}

	public boolean matches(String query) {
		return regex.matcher(query).matches();
	}

	public Object invoke(Object bean, String query) {
		try {
			String[] params = parseQuery(query);
			Method method = handler.findMethod(bean);
			if (method == null) {
				String message = "Unable to find the handler method specified:" +
					handler.getMethodName();
				throw new BeanMethodException(message);
			}
			return method.invoke(bean, (Object[])params);
		} catch (IllegalArgumentException exception) {
			String message = "Unable to invoke the handler method.";
			throw new BeanMethodException(message, exception);
		} catch (IllegalAccessException exception) {
			String message = "Unable to invoke the handler method.";
			throw new BeanMethodException(message, exception);
		} catch (InvocationTargetException exception) {
			String message = "Unable to invoke the handler method.";
			throw new BeanMethodException(message, exception);
		}
	}

	private String[] parseQuery(String query) {
		String[] params = getParameterOrder();
		Matcher matcher = regex.matcher(query);
		Map<String, String> map = new HashMap<String, String>();
		if (matcher.matches()) {
			for (int i = 1; i <= params.length; i++) {
				map.put(params[i - 1], matcher.group(i));
			}
		}

		String[] result = new String[params.length];
		for (int i = 0; i < params.length; i++) {
			result[i] = map.get(handler.getParameters().get(i));
		}
		return result;
	}

	private String[] getParameterOrder() {
		Matcher matcher = paramPattern.matcher(url);
		int i = 0;
		int max = handler.getParameters().size();
		String[] result = new String[max];
		while (matcher.find() && (i < max)) {
			result[i] = matcher.group(1);
			i++;
		}
		return result;
	}

	public DataFormatter resolveFormatter(Set<DataFormatter> formatters,
			ContextFacade context) {
		if (formatters.size() == 0) {
			return null;
		}
		if (context.getAcceptedContentTypes() == null) {
			return formatters.iterator().next();
		}

		String[] types = context.getAcceptedContentTypes();
		for (String type : types) {
			DataFormatter formatter = findMatch(formatters, type);
			if (formatter != null) {
				return formatter;
			}
		}
		return null;
	}

	private DataFormatter findMatch(Set<DataFormatter> formatters, String type) {
		String mime = type.split(";[\\s]*")[0];
		Pattern regex = Pattern.compile(mime.replace("*", ".*"));
		for (DataFormatter formatter : formatters) {
			if (regex.matcher(formatter.getMimeType()).matches()) {
				return formatter;
			}
		}
		return null;
	}

	public DataParser resolveParser(Set<DataParser> parsers,
			ContextFacade context) {
		String contentType = context.getRequestContentType();
		if ((parsers.size() == 0) || (contentType == null)) {
			return null;
		}

		String mime = contentType.split(";")[0].trim();
		for (DataParser parser : parsers) {
			if (parser.getMimeType().equalsIgnoreCase(mime)) {
				return parser;
			}
		}
		return null;
	}

}
