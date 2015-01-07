/**
 * 
 */
package cn.liutils.api.entityx.motion;

import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 *
 */
public class GravityApply extends MotionHandler {
	
	public static final double DEFAULT_GRAVITY_VALUE = 0.08;
	
	public double gravity;

	public GravityApply(EntityX ent) {
		this(ent, DEFAULT_GRAVITY_VALUE);
	}
	
	public GravityApply(EntityX ent, double grav) {
		super(ent);
		gravity = grav;
	}
	
	@Override
	public int getPriority() {
		return 1007;
	}

	@Override
	public void onSpawnedInWorld() {}

	@Override
	public void onUpdate() {
		if(!entity.onGround) {
			this.entity.motionY -= gravity;
		}
	}

	@Override
	public String getID() {
		return "gravity";
	}

}
