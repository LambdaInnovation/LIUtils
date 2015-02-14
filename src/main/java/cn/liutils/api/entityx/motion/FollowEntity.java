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
	double offsetX, offsetY, offsetZ;

	public FollowEntity(T ent, Entity targ) {
		super(ent, targ.posX, targ.posY, targ.posZ);
		target = targ;
	}
	
	public FollowEntity setOffset(double ox, double oy, double oz) {
		offsetX = ox;
		offsetY = oy;
		offsetZ = oz;
		return this;
	}
	
	@Override
	public void onUpdate() {
		if(target.isDead) {
			this.alive = false;
			return;
		}
		//System.out.println(offsetY);
		this.setPos(target.posX + offsetX, target.posY + offsetY, target.posZ + offsetZ);
		
		entity.motionX = target.motionX;
		entity.motionY = target.motionY;
		entity.motionZ = target.motionZ;
		super.onUpdate();
	}
	
	@Override
	public String getID() {
		return "followent";
	}

}
