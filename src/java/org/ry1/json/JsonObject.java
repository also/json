package org.ry1.json;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public class JsonObject implements Json {
	private LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
	
	public JsonObject() {}
	
	public JsonObject(Object bean) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
				Method getter = propertyDescriptor.getReadMethod();
				set(propertyDescriptor.getName(), getter.invoke(bean, (Object[]) null));
			}
		}
		catch (Exception ex) {
			throw new IllegalArgumentException("Bean cannot be introspected", ex);
		}
	}
	
	public JsonObject(Object bean, String... propertyNames) {
		try {
			Class beanClass = bean.getClass();
			for (String propertyName : propertyNames) {
				Method getter = beanClass.getMethod("get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), (Class[]) null);
				set(propertyName, getter.invoke(bean, (Object[]) null));
			}
		}
		catch (Exception ex) {
			throw new IllegalArgumentException("Bean cannot be introspected", ex);
		}
	}

	public void appendJson(JsonBuilder builder) {
		builder.appendMap(map);
	}
	
	public Object get(String name) {
		return map.get(name);
	}
	
	public JsonObject set(String name, Object value) {
		map.put(name, value);
		return this;
	}
	
	public String toString() {
		JsonBuilder builder = new JsonBuilder();
		
		builder.appendAllNameValuePairs(map);
		
		return builder.toString();
	}
	
	public static JsonObject valueOf(String jsonString) {
		return new JsonReader().read(jsonString);
	}
}
