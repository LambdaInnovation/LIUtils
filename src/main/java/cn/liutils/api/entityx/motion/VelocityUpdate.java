/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.entityx.motion;

import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 */
public class VelocityUpdate extends MotionHandler {
	
	public double decrRate = 0.98; //Mutiplied to entity's motion each tick;
	public static final String ID = "velupdate";
	public boolean updateRotation = true;
	
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
//		entity.posX += entity.motionX;
//		entity.posY += entity.motionY;
//		entity.posZ += entity.motionZ;
		entity.lastTickPosX = entity.posX;
		entity.lastTickPosY = entity.posY;
		entity.lastTickPosZ = entity.posZ;
		entity.setPosition(entity.posX + entity.motionX, entity.posY + entity.motionY, entity.posZ + entity.motionZ);
		entity.motionX *= decrRate;
		entity.motionY *= decrRate;
		entity.motionZ *= decrRate;
		if(updateRotation) {
			double tmp = entity.motionX * entity.motionX + entity.motionZ * entity.motionZ;
			entity.rotationPitch = -(float) (Math.atan2(entity.motionY, Math.sqrt(tmp)) * 180 / Math.PI);
			entity.rotationYaw = -(float) (Math.atan2(entity.motionX, entity.motionZ) * 180 / Math.PI);
		}
	}
	
	public VelocityUpdate setDecr(double rate) {
		decrRate = rate;
		return this;
	}
	
	public VelocityUpdate setUpdateRot(boolean b) {
		updateRotation = b;
		return this;
	}
	
	@Override
	public int getPriority() {
		return 1008;
	}
	
	@Override
	public String getID() {
		return ID;
	}

}
