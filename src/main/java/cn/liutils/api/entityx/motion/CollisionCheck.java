/**
 * 
 */
package cn.liutils.api.entityx.motion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;
import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;
import cn.liutils.util.DebugUtils;
import cn.liutils.util.GenericUtils;

/**
 * @author WeathFolD
 *
 */
public class CollisionCheck extends MotionHandler {
	
	boolean resetVel = true;
	public static final String ID = "collision";
	IEntitySelector selector = null;
	List<Entity> excls = new ArrayList();

	public CollisionCheck(EntityX ent) {
		super(ent);
		excls.add(entity);
	}
	
	@Override
	public int getPriority() {
		return 1010;
	}

	@Override
	public void onCreated() {}
	
	public CollisionCheck setResetVelocity(boolean b) {
		resetVel = b;
		return this;
	}

	public CollisionCheck setSelector(IEntitySelector sel) {
		selector = sel;
		return this;
	}
	
	public CollisionCheck addExclusion(Entity ...ents) {
		for(Entity e : ents) {
			excls.add(e);
		}
		return this;
	}
	
	@Override
	public void onUpdate() {
		MovingObjectPosition res = 
			GenericUtils.rayTraceBlocksAndEntities(selector, entity.worldObj,
				this.createVector(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ), 
				this.createVector(entity.posX, entity.posY, entity.posZ), 
				excls.toArray(new Entity[excls.size()]));
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
