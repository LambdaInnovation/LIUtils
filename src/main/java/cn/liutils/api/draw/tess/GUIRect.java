/**
 * 
 */
package cn.liutils.api.draw.tess;

import java.util.EnumSet;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;
import cn.liutils.util.HudUtils;

/**
 * @author WeathFolD
 *
 */
public class GUIRect extends DrawHandler {
	
	protected RectMapping map = new RectMapping();
	double width, height;
	
	public GUIRect(double w, double h, double u, double v, double tw, double th) {
		setSize(w, h);
		setMapping(u, v, tw, th);
	}
	
	public GUIRect() {}
	
	public GUIRect setSize(double w, double h) {
		width = w;
		height = h;
		return this;
	}
	
	public GUIRect setMapping(double u, double v, double tw, double th) {
		map.set(u, v, tw, th);
		return this;
	}
	
	public RectMapping getMap() {
		return map;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.DO_TESS);
	}

	@Override
	public String getID() {
		return "rect_2d";
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		HudUtils.drawRect(0, 0, width, height, map);
	}

}
