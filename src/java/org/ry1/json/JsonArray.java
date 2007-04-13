package org.ry1.json;

import java.util.ArrayList;

public class JsonArray implements Json {
	private ArrayList<Object> list = new ArrayList<Object>();
	
	public void appendJson(JsonBuilder builder) {
		builder.appendIterable(list);
	}
	
	public void addValue(Object value) {
		list.add(value);
	}
	
	public String toString() {
		JsonBuilder builder = new JsonBuilder();
		
		appendJson(builder);
		
		return builder.toString();
	}
	
	public ArrayList<Object> getList() {
		return list;
	}
}
