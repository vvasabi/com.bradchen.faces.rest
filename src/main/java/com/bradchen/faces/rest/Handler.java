package com.bradchen.faces.rest;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public final class Handler {

	private final String beanName;
	private final String methodName;
	private final List<String> parameters;
	private Object bean;

	public Handler(String beanName, String method, List<String> parameters) {
		this.beanName = beanName;
		this.methodName = method;
		this.parameters = Collections.unmodifiableList(parameters);
	}

	public String getBeanName() {
		return beanName;
	}

	public String getMethodName() {
		return methodName;
	}

	public Method findMethod(Object bean) {
		Method[] methods = bean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			if (!methodName.equals(method.getName())) {
				continue;
			}
			if (method.getParameterTypes().length == parameters.size()) {
				return method;
			}
		}
		return null;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public Object getBeanInstance() {
		return bean;
	}

	public void setBeanInstance(Object bean) {
		this.bean = bean;
	}

}
