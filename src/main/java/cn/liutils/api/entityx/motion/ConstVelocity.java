/**
 * 
 */
package cn.liutils.api.entityx.motion;

import net.minecraft.util.Vec3;
import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 *
 */
public class ConstVelocity extends MotionHandler {
	
	public double u, v, w;
	public double velocity;
	
	public ConstVelocity(EntityX ent) {
		this(ent, 0, 0, 1, 0);
	}

	public ConstVelocity(EntityX ent, double u, double v, double w, double vel) {
		super(ent);
		this.u = u;
		this.v = v;
		this.w = w;
		normalize();
		this.velocity = vel;
	}

	@Override
	public void onSpawnedInWorld() {}

	@Override
	public void onUpdate() {
		entity.motionX = u * velocity;
		entity.motionY = v * velocity;
		entity.motionZ = w * velocity;
	}
	
	private void normalize() {
		double d = Math.sqrt(u * u + v * v + w * w);
		u /= d;
		v /= d;
		w /= d;
	}

	@Override
	public String getID() {
		return "const_vel";
	}

}
