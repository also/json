package org.ry1.json;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class JsonBuilder {
	StringBuilder builder = new StringBuilder();
	
	public JsonBuilder appendNameValuePair(Object name, Object value) {
		appendName(name);
		appendValue(value);
		return this;
	}
	
	public JsonBuilder appendName(Object name) {
		appendString(String.valueOf(name));
		builder.append(':');
		
		return this;
	}
	
	public JsonBuilder appendValue(Object value) {
		if (value == null
		 || value instanceof Number
		 || value instanceof Boolean) appendPlain(value);
		else if (value.getClass().isArray()) appendArray(value);
		else if (value instanceof Iterable) appendIterable((Iterable) value);
		else if (value instanceof Map) appendMap((Map) value);
		else if (value instanceof Json) appendJson((Json) value);
		else appendString(value);
		return this;
	}
	
	private void appendPlain(Object o) {
		builder.append(o);
	}
	
	private static final char[] CHARS_TO_ESCAPE = "\"\\\b\f\n\r\t".toCharArray();
	private static final char[] HEX = "0123456789abcdefg".toCharArray();
	
	private void appendString(Object o) {
		builder.append('"');
		for (char c : o.toString().toCharArray()) {
			if (Character.isISOControl(c)) {
				builder.append("\\u");
				for (int i = 0; i < 4; i++) {
					builder.append(HEX[c & 0xf000] >> 12);
					c <<= 4;
				}
			}
			else {
				for (char escape : CHARS_TO_ESCAPE) {
					if (c == escape) {
						builder.append('\\');
						break;
					}
				}
				builder.append(c);
			}
		}
		builder.append('"');
	}
	
	void appendMap(Map<String, Object> m) {
		builder.append('{');
		appendAllNameValuePairs(m);
		builder.append('}');
	}
	
	void appendAllNameValuePairs(Map<String, Object> m) {
		Iterator<Entry<String, Object>> it = m.entrySet().iterator();
		if (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			appendNameValuePair(entry.getKey(), entry.getValue());
			while (it.hasNext()) {
				builder.append(',');
				entry = it.next();
				appendNameValuePair(entry.getKey(), entry.getValue());
			}
		}
	}
	
	void appendIterable(Iterable<?> i) {
		builder.append('[');
		Iterator<?> it = i.iterator();
		if (it.hasNext()) {
			appendValue(it.next());
			while (it.hasNext()) {
				builder.append(',');
				appendValue(it.next());
			}
		}
		builder.append(']');
	}
	
	private void appendArray(Object a) {
		builder.append('[');
		int length = Array.getLength(a);
		if (length > 0) {
			appendValue(Array.get(a, 0));
			for (int i = 1; i < length; i++) {
				builder.append(',');
				appendValue(Array.get(a, i));
			}
		}
		builder.append(']');
	}
	
	private void appendJson(Json json) {
		json.appendJson(this);
	}
	
	public String toString() {
		return '{' + builder.toString() + '}';
	}
}
