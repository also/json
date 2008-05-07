/* $Id$ */

package org.ry1.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvokingTransformer implements Transformer {
	private Object transformer;
	private Method method;
	
	public MethodInvokingTransformer(Object transformer, String methodName) {
		this.transformer = transformer;
		for (Method method : transformer.getClass().getMethods()) {
			if (method.getName().equals(methodName)) {
				this.method = method;
				break;
			}
		}
		if (method == null) {
			throw new IllegalArgumentException("Valid transforming method [" + methodName + "] does not exist in class [" + transformer.getClass() + "]");
		}
	}
	
	public MethodInvokingTransformer(Object transformer, Method method) {
		this.transformer = transformer;
		this.method = method;
	}

	public Object transform(Object value) throws InvocationTargetException, IllegalAccessException {
		return method.invoke(transformer, new Object[] {value});
	}

}
