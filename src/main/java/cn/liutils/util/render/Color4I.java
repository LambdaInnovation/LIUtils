/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.util.render;

import cn.liutils.util.RenderUtils;


/**
 * @author mkpoli
 *
 */
public class Color4I {
	public int red;
	public int green;
	public int blue;
	public int alpha;

	public Color4I(int red, int green, int blue, int alpha) {
		this.red = formatColorFloat(red);
		this.green = formatColorFloat(green);
		this.blue = formatColorFloat(blue);
		this.alpha = formatColorFloat(alpha);
	}
	

	public Color4I(int red, int green, int blue) {
		this.red = formatColorFloat(red);
		this.green = formatColorFloat(green);
		this.blue = formatColorFloat(blue);
		this.alpha = 255;
	}
	
	public void setValue(int r, int g, int b, int a) {
		red = r;
		green = g;
		blue = b;
		alpha = a;
	}
	
	public void bind() {
		RenderUtils.bindColor(red, green, blue, alpha);
	}

	private int formatColorFloat(int source) {
		int result;
		if (source < 0) {
			result = 0;
		} else if (source > 255) {
			result = 255;
		} else {
			result = source;
		}
		return result;
	}
	
	@Override
	public String toString() {
		return String.format("[Type : Color 4f  Red: %d  Green: %d  Blue: %d  Alpha: %d]", red, green, blue, alpha);
	}
	
}
