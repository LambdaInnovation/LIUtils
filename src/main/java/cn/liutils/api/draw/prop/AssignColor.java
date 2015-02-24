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
package cn.liutils.api.draw.prop;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * @author WeathFolD
 *
 */
public class AssignColor extends DrawHandler {
	
	public float r = 1, g = 1, b = 1, a = 1;

	public AssignColor() {}
	
	public AssignColor(float r, float g, float b, float a) {
		set(r, g, b, a);
	}
	
	public AssignColor(int r, int g, int b, int a) {
		set(r, g, b, a);
	}
	
	public void set(int r, int g, int b, int a) {
		set(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
	}
	
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.PRE_TESS);
	}

	@Override
	public String getID() {
		return "color";
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		GL11.glColor4f(r, g, b, a);
	}

}
