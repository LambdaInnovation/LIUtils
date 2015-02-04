/**
 * 
 */
package cn.liutils.api.draw;

import net.minecraft.util.ResourceLocation;
import cn.liutils.api.draw.prop.AssignColor;
import cn.liutils.api.draw.prop.AssignTexture;
import cn.liutils.api.draw.prop.DisableTexture;
import cn.liutils.api.draw.tess.GUIRect;
import cn.liutils.api.draw.tess.Transform;

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