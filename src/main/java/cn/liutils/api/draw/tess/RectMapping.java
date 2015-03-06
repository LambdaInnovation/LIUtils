/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.draw.tess;

import javax.vecmath.Vector2d;

/**
 * Represents texture mapping info of a rectangle.
 * @author WeathFolD
 */
public class RectMapping {

	public double u0, v0, tw, th;
	
	public RectMapping() {
		this(0, 0, 1, 1);
	}
	
	public RectMapping(double u0, double v0, double u1, double v1) {
		set(u0, v0, u1, v1);
	}
	
	public void set(double u0, double v0, double tw, double th) {
		this.u0 = u0;
		this.v0 = v0;
		this.tw = tw;
		this.th = th;
	}
	
	public void setAbs(double u0, double v0, double u1, double v1) {
		set(u0, v0, u1 - u0, v1 - v0);
	}
	
	public double getMinU() {
		return u0;
	}
	
	public double getMaxU() {
		return u0 + tw;
	}
	
	public double getMinV() {
		return v0;
	}
	
	public double getMaxV() {
		return v0 + th;
	}

}
