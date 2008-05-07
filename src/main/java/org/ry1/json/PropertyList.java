/* $Id$ */

package org.ry1.json;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Stores the heirarchy of bean properties to use to build a json object.
 * @author Ryan Berdeen
 *
 */
public class PropertyList {
	private List<PropertyListElement> elements = new ArrayList<PropertyListElement>();
	
	/** Adds the named value to the list of properties.
	 */
	public PropertyList includeValue(String propertyName) {
		return includeBean(propertyName, null);
	}
	
	/** Adds the named value to the list of properties.
	 */
	public PropertyList includeValues(String... propertyNames) {
		for (String propertyName : propertyNames) {
			includeValue(propertyName);
		}
		
		return this;
	}
	
	/** Adds the named bean and the bean's properties to the list of properties.
	 */
	public PropertyList includeBean(String propertyName, PropertyList propertyList) {
		elements.add(new PropertyListElement(propertyName, propertyName, null, propertyList, false));
		return this;
	}
	
	/** Adds the named bean to the list of properties and return the <code>PropertyList</code> for the bean.
	 */
	public PropertyList forBean(String propertyName) {
		PropertyList propertyList = new PropertyList();
		includeBean(propertyName, propertyList);
		return propertyList;
	}
	
	/** Adds the named list of values to the list of properties.
	 */
	public PropertyList includeListOfValues(String propertyName) {
		return includeListOfBeans(propertyName, null);
	}
	
	/** Adds the named list of beans and the beans' properties to the list of properties.
	 * @param propertyName
	 * @param propertyList
	 * @return
	 */
	public PropertyList includeListOfBeans(String propertyName, PropertyList propertyList) {
		elements.add(new PropertyListElement(propertyName, propertyName, null, propertyList, true));
		return this;
	}
	
	/** Adds the named list of beans to the property list and returns the <code>PropertyList</code> for the beans.
	 */
	public PropertyList forListOfBeans(String propertyName) {
		PropertyList propertyList = new PropertyList();
		includeListOfBeans(propertyName, propertyList);
		return propertyList;
	}
	
	public List<PropertyListElement> getElements() {
		return Collections.unmodifiableList(elements);
	}
	
	public void setElements(List<PropertyListElement> elements) {
		this.elements = elements;
	}
}
