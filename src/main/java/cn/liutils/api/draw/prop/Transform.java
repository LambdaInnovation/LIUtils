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
 * A standard transformation routing. Performs rotation->transform action.
 * @author WeathFolD
 */
public class Transform extends DrawHandler {
	
	public double tx, ty, tz; //Offset coordinates
	public double yaw, pitch, roll; //Rotation angles
	public double pivotX, pivotY, pivotZ; //Rotation pivot point

	public Transform() {}
	
	public Transform setOffset(double tx, double ty, double tz) {
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		return this;
	}
	
	public Transform setPivotPt(double x, double y, double z) {
		this.pivotX = x;
		this.pivotY = y;
		this.pivotZ = z;
		return this;
	}
	
	public Transform setYaw(double d) {
		this.yaw = d;
		return this;
	}
	
	public Transform setPitch(double d) {
		this.pitch = d;
		return this;
	}
	
	public Transform setRoll(double d) {
		this.roll = d;
		return this;
	}
	
	public Transform setRotation(double yaw, double pitch, double roll) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
		return this;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.PRE_TRANSFORM, EventType.POST_TRANSFORM);
	}

	@Override
	public String getID() {
		return "transform";
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		switch(event) {
		case PRE_TRANSFORM:
			GL11.glTranslated(tx, ty, tz);
			break;
		case POST_TRANSFORM:
			GL11.glTranslated(pivotX, pivotY, pivotZ);
			GL11.glRotated(yaw, 0, 1, 0);
			GL11.glRotated(pitch, 1, 0, 0);
			GL11.glRotated(roll, 0, 0, 1);
			GL11.glTranslated(-pivotX, -pivotY, -pivotZ);
			break;
		default: //wtf?
			break;
		}
	}
	
	public void perform() {
		GL11.glTranslated(tx, ty, tz);
		GL11.glTranslated(pivotX, pivotY, pivotZ);
		GL11.glRotated(yaw, 0, 1, 0);
		GL11.glRotated(pitch, 1, 0, 0);
		GL11.glRotated(roll, 0, 0, 1);
		GL11.glTranslated(-pivotX, -pivotY, -pivotZ);
	}

}
