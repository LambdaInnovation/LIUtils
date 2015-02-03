/**
 * 
 */
package cn.liutils.api.entityx.motion;

import net.minecraft.entity.Entity;
import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 *
 */
public class LifeTime<T extends EntityX> extends MotionHandler<T> {

	final int life;
	
	public LifeTime(T ent, int _life) {
		super(ent);
		life = _life;
	}

	@Override
	public void onCreated() {}

	@Override
	public void onUpdate() {
		if(this.entity.ticksExisted > life) {
			entity.setDead();
		}
	}

	@Override
	public String getID() {
		return "life";
	}

}
