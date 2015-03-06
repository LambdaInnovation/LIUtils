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
package cn.liutils.api.draw;

import net.minecraft.util.ResourceLocation;
import cn.liutils.api.draw.prop.AssignColor;
import cn.liutils.api.draw.prop.AssignTexture;
import cn.liutils.api.draw.prop.DisableTexture;
import cn.liutils.api.draw.prop.Transform;
import cn.liutils.api.draw.tess.GUIRect;

/**
 * @author WeathFolD
 *
 */
public class GUIObject extends DrawObject {
	
	protected GUIRect rect;
	protected Transform transform = new Transform();

	public GUIObject(double x, double y, double w, double h, double u, double v, double tw, double th) {
		rect = new GUIRect(w, h, u, v, tw, th);
		rect.setMapping(u, v, tw, th);
		getTransform().setOffset(x, y, 0);
		addHandlers(rect, transform);
	}
	
	public GUIRect getRect() {
		return rect;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public static class Tex extends GUIObject {
		
		AssignTexture tex;

		public Tex(ResourceLocation texture, double x, double y, double w, double h, double u, double v,
				double tw, double th) {
			super(x, y, w, h, u, v, tw, th);
			addHandler(tex = new AssignTexture(texture));
		}
		
		public void changeTexture(ResourceLocation texture) {
			tex.set(texture);
		}
		
	}
	
	public static class Color extends GUIObject {
		
		AssignColor color;

		public Color(double x, double y, double w, double h, double u,
				double v, double tw, double th) {
			super(x, y, w, h, u, v, tw, th);
			this.addHandlers(
				DisableTexture.instance(),
				color = new AssignColor());
		}
		
		public Color setColor4ub(int r, int g, int b, int a) {
			color.set(r, g, b, a);
			return this;
		}
		
		public Color setColor4f(float r, float g, float b, float a) {
			color.set(r, g, b, a);
			return this;
		}
		
	}

}
