/**
 * 
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
	
	public float r, g, b, a;

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
