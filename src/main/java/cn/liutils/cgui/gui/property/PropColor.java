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
package cn.liutils.cgui.gui.property;

import cn.liutils.cgui.loader.Editable;

/**
 * @author WeAthFolD
 *
 */
public class PropColor implements IProperty {
	
	@Editable(value = "color", defDouble = 1.0)
	public double r = 1.0, g = 1.0, b = 1.0, a = 1.0;
	
	public PropColor setColor3i(int ir, int ig, int ib) {
		r = ir / 255.0;
		g = ig / 255.0;
		b = ib / 255.0;
		return this;
	}
	
	public PropColor setColor4d(double dr, double dg, double db, double da) {
		r = dr;
		g = dg;
		b = db;
		a = da;
		return this;
	}

	@Override
	public String getName() {
		return "color";
	}

}
