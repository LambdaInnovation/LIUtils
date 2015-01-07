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
	
	double decrRate = 0.98; //Mutiplied to entity's motion each tick;
	
	public VelocityUpdate(EntityX ent, double decr) {
		super(ent);
		decrRate = decr;
	}

	public VelocityUpdate(EntityX ent) {
		super(ent);
	}

	@Override
	public void onSpawnedInWorld() {}

	@Override
	public void onUpdate() {
		entity.posX += entity.motionX;
		entity.posY += entity.motionY;
		entity.posZ += entity.motionZ;
		entity.motionX *= decrRate;
		entity.motionX *= decrRate;
		entity.motionX *= decrRate;
	}
	
	public int getPriority() {
		return 1008;
	}
	
	@Override
	public String getID() {
		return "velupdate";
	}

}
