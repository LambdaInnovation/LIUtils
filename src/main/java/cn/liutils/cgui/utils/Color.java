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
package cn.liutils.cgui.utils;

import org.lwjgl.opengl.GL11;

/**
 * @author WeAthFolD
 */
public class Color {
	
	public double r, g, b, a;
	
	public Color(double _r, double _g, double _b, double _a) {
		setColor4d(_r, _g, _b, _a);
	}
	
	public Color() {
		this(1, 1, 1, 1);
	}
	
	public void setColor4i(int r, int g, int b, int a) {
		setColor4d(r / 255.0, g / 255.0, b / 255.0, a / 255.0);
	}
	
	public void setColor4d(double _r, double _g, double _b, double _a) {
		r = _r;
		g = _g;
		b = _b;
		a = _a;
	}
	
	public int asHexColor() {
		byte ir = (byte) (r * 255), ig = (byte) (g * 255), ib = (byte) (b * 255);
		return ir | ig << 8 | ib << 16;
	}
	
	public void bind() {
		GL11.glColor4d(r, g, b, a);
	}
}
