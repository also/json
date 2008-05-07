/* $Id$ */

package org.ry1.json;

public class NoOpTransformer implements Transformer {
	public static final NoOpTransformer INSTANCE = new NoOpTransformer();
	
	public Object transform(Object value) {
		return value;
	}
}
