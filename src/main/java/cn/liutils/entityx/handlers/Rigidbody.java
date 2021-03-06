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
package cn.liutils.entityx.handlers;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import cn.liutils.entityx.MotionHandler;
import cn.liutils.entityx.event.CollideEvent;
import cn.liutils.util.generic.VecUtils;
import cn.liutils.util.mc.BlockFilters;
import cn.liutils.util.mc.IBlockFilter;
import cn.liutils.util.raytrace.Raytrace;

/**
 * Rigidbody will update velocity and apply gravity and do simple collision.
 * @author WeAthFolD
 */
public class Rigidbody extends MotionHandler {
	
	public double gravity = 0.00; //block/tick^2
	public double linearDrag = 1.0;
	
	public IEntitySelector entitySel;
	public IBlockFilter blockFil = BlockFilters.filNormal;
	
	public boolean accurateCollision = false;

	@Override
	public String getID() {
		return "Rigidbody";
	}

	@Override
	public void onStart() {}

	@Override
	public void onUpdate() {
		Entity target = getTarget();
		
		//Collision detection
		MovingObjectPosition mop = null;
		if(accurateCollision) {
			float hw = target.width / 2, hh = target.height;
			Vec3[] points = {
				VecUtils.vec(target.posX - hw, target.posY, 	 target.posZ - hw),
				VecUtils.vec(target.posX - hw, target.posY, 	 target.posZ + hw),
				VecUtils.vec(target.posX + hw, target.posY, 	 target.posZ + hw),
				VecUtils.vec(target.posX + hw, target.posY, 	 target.posZ - hw),
				VecUtils.vec(target.posX - hw, target.posY + hh, target.posZ - hw),
				VecUtils.vec(target.posX - hw, target.posY + hh, target.posZ + hw),
				VecUtils.vec(target.posX + hw, target.posY + hh, target.posZ + hw),
				VecUtils.vec(target.posX + hw, target.posY + hh, target.posZ - hw),
			};
			Vec3 motion = VecUtils.vec(target.motionX, target.motionY, target.motionZ);
			for(Vec3 vec : points) {
				Vec3 next = VecUtils.add(vec, motion);
				if((mop = Raytrace.perform(target.worldObj, vec, next, entitySel, blockFil)) != null)
					break;
			}
		} else {
			Vec3 cur = Vec3.createVectorHelper(target.posX, target.posY, target.posZ),
					next = Vec3.createVectorHelper(target.posX + target.motionX, target.posY + target.motionY, target.posZ + target.motionZ);
			mop = Raytrace.perform(target.worldObj, cur, next, entitySel, blockFil);
		}
		
		if(mop != null) {
			getEntityX().postEvent(new CollideEvent(mop)); //Let the event handlers do the actual job.
		}
		
		//Velocity update
		target.motionY -= gravity;
		
		target.motionX *= linearDrag;
		target.motionY *= linearDrag;
		target.motionZ *= linearDrag;
		
		target.lastTickPosX = target.posX;
		target.lastTickPosY = target.posY;
		target.lastTickPosZ = target.posZ;
		target.setPosition(target.posX + target.motionX, target.posY + target.motionY, target.posZ + target.motionZ);
	}

}
