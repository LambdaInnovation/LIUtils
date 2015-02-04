package cn.liutils.api.render.model;

import java.util.Collection;
import java.util.HashMap;

import javax.vecmath.Vector3d;

public abstract class Model {
	
	HashMap<String, Vector3d> parts;

	public Model() {}
	
	/**
	 * Data format:</br>
	 * <code>
	 * {</br>
	 * 	{ String , { double, double, double } },</br>
	 * 	{ ... }</br>
	 * }
	 * </code>
	 */
	public Model(Object[][] data) {
		parseData(data);
	}
	
	private void parseData(Object[][] data) {
		for(Object[] one : data) {
			String name = (String) one[0];
			double[] arr = (double[]) one[1];
			parts.put(name, new Vector3d(arr));
		}
	}
	
	public final Collection<String> getParts() {
		return parts.keySet();
	}
	
	public abstract void draw(String part);
	
	public abstract void drawAll();
	
	public final Vector3d getPivotPt(String part) {
		return parts.get(part);
	}

}
