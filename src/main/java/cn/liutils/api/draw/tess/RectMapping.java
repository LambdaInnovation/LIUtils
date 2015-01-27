/**
 * 
 */
package cn.liutils.api.draw.tess;

import javax.vecmath.Vector2d;

/**
 * Represents texture mapping info of a rectangle.
 * @author WeathFolD
 */
public class RectMapping {

	double u0, v0, u1, v1;
	
	public RectMapping() {
		this(0, 0, 1, 1);
	}
	
	public RectMapping(double u0, double v0, double u1, double v1) {
		set(u0, v0, u1, v1);
	}
	
	public void set(double u0, double v0, double u1, double v1) {
		this.u0 = u0;
		this.v0 = v0;
		this.u1 = u1;
		this.v1 = v1;
	}
	
	public void setBySize(double u, double v, double tw, double th) {
		set(u, v, u + tw, v + th);
	}
	
	/**
	 * Set the mapping with a resolution multiple.
	 * @param texWidth width of the texture
	 * @param texHeight height of the texture
	 * @param u uCoord
	 * @param v vCoord
	 * @param uw Mapping width
	 * @param vw Mapping height
	 */
	public void setWithResolution(double texWidth, double texHeight, double u, double v, double uw, double vw) {
		set(u / texWidth, v / texHeight, (u + uw) / texWidth, (v + vw) / texHeight);
	}

}
