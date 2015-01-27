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
public class Offset extends DrawHandler {
	
	public double tx, ty, tz;
	public final EventType phase;

	public Offset(EventType phase) {
		this.phase = phase;
	}
	
	public Offset(EventType phase, double x, double y, double z) {
		this.phase = phase;
		tx = x;
		ty = y;
		tz = z;
	}
	
	public void set(double x, double y, double z) {
		tx = x;
		ty = y;
		tz = z;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(phase);
	}

	@Override
	public String getID() {
		return "transform_" + phase.name();
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		GL11.glTranslated(tx, ty, tz);
	}

}
