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
public class Rotation extends DrawHandler {

	public double angle;
	public double axisX = 0, axisY = 1, axisZ = 0;
	public final EventType phase;
	
	public Rotation(EventType p) {
		phase = p;
	}
	
	public Rotation(EventType p, double angle) {
		this(p);
		setAngle(angle);
	}
	
	public Rotation(EventType p, double angle, double ax, double ay, double az) {
		this(p);
		setAngle(angle);
		setRotation(ax, ay, az);
	}
	
	public void setAngle(double d) {
		angle = d;
	}
	
	public void setRotation(double ax, double ay, double az) {
		axisX = ax;
		axisY = ay;
		axisZ = az;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(phase);
	}

	@Override
	public String getID() {
		return "rotation";
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		GL11.glRotated(angle, axisX, axisY, axisZ);
	}

}
