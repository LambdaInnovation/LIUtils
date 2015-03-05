/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
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
