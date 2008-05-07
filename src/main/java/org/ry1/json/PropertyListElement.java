/* $Id$ */

package org.ry1.json;

public class PropertyListElement {
	public final boolean list;
	public final String propertyName;
	public final String targetName;
	public final PropertyList propertyList;
	public final Transformer transformer;
	
	public PropertyListElement(String propertyName, String targetName, Transformer transformer, PropertyList propertyList, boolean list) {
		this.propertyName = propertyName;
		this.targetName = targetName != null ? targetName : propertyName;
		this.transformer = transformer != null ? transformer : NoOpTransformer.INSTANCE;
		this.propertyList = propertyList;
		this.list = list;
	}
}