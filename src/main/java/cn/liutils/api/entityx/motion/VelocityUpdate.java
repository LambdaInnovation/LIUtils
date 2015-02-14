/**
 * 
 */
package cn.liutils.api.entityx.motion;

import net.minecraft.util.Vec3;
import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 */
public class VelocityUpdate extends MotionHandler {
	
	public double decrRate = 0.98; //Mutiplied to entity's motion each tick;
	public static final String ID = "velupdate";
	
	public VelocityUpdate(EntityX ent, double decr) {
		super(ent);
		decrRate = decr;
	}

	public VelocityUpdate(EntityX ent) {
		super(ent);
	}

	@Override
	public void onCreated() {}

	@Override
	public void onUpdate() {
		entity.posX += entity.motionX;
		entity.posY += entity.motionY;
		entity.posZ += entity.motionZ;
		entity.motionX *= decrRate;
		entity.motionX *= decrRate;
		entity.motionX *= decrRate;
	}
	
	public VelocityUpdate setDecr(double rate) {
		decrRate = rate;
		return this;
	}
	
	public int getPriority() {
		return 1008;
	}
	
	@Override
	public String getID() {
		return ID;
	}

}
