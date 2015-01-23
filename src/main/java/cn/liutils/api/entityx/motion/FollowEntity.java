/**
 * 
 */
package cn.liutils.api.entityx.motion;

import net.minecraft.entity.Entity;
import cn.liutils.api.entityx.EntityX;

/**
 * @author WeathFolD
 */
public class FollowEntity<T extends EntityX> extends ApplyPos<T> {
	
	Entity target;

	public FollowEntity(T ent, Entity targ) {
		super(ent, targ.posX, targ.posY, targ.posZ);
		target = targ;
	}
	
	@Override
	public void onUpdate() {
		if(target.isDead) {
			this.alive = false;
			return;
		}
		this.setPos(target.posX, target.posY, target.posZ);
		super.onUpdate();
	}
	
	@Override
	public String getID() {
		return "followent";
	}

}
