/**
 * 
 */
package cn.liutils.api.entityx.motion;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 *
 */
public class CollisionCheck extends MotionHandler {
	
	boolean resetVel = true;
	public static final String ID = "collision";

	public CollisionCheck(EntityX ent) {
		super(ent);
	}
	
	@Override
	public int getPriority() {
		return 1010;
	}

	@Override
	public void onSpawnedInWorld() {}
	
	public CollisionCheck setResetVelocity(boolean b) {
		resetVel = b;
		return this;
	}

	@Override
	public void onUpdate() {
		MovingObjectPosition res = 
			entity.worldObj.rayTraceBlocks(
				this.createVector(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), 
				this.createVector(entity.posX, entity.posY, entity.posZ));
		if(res == null) return;
		onCollided(res);
	}
	
	protected void onCollided(MovingObjectPosition res) {
		//By default: stop right at the side of the block.
		ForgeDirection fd = ForgeDirection.getOrientation(res.sideHit);
		if(fd.offsetX != 0) {
			entity.posX = res.hitVec.xCoord + fd.offsetX * entity.width * 0.5;
			if(resetVel) {
				entity.motionX = 0;
			}
		} else if(fd.offsetY != 0) {
			entity.posY = res.hitVec.yCoord + entity.height * .5 * fd.offsetY;
			if(resetVel) {
				entity.motionY = 0;
			}
		} else {
			entity.posZ = res.hitVec.zCoord + fd.offsetZ * entity.width * 0.5;
			if(resetVel) {
				entity.motionZ = 0;
			}
		}
	}

	@Override
	public String getID() {
		return ID;
	}

}
