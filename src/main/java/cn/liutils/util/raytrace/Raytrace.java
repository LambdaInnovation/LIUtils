package cn.liutils.util.raytrace;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.util.generic.VecUtils;
import cn.liutils.util.helper.Motion3D;
import cn.liutils.util.mc.WorldUtils;

public class Raytrace {

	public static MovingObjectPosition perform(World world, Vec3 vec1, Vec3 vec2, TraceOption option) {
		if(option == null)
			option = TraceOptions.defaultOption;
		
		MovingObjectPosition 
			mop1 = rayTraceEntities(world, vec1, vec2, option.entitySel),
			mop2 = world.rayTraceBlocks(vec1, vec2);
		
		if(mop1 != null && mop2 != null) {
			double d1 = mop1.hitVec.distanceTo(vec1);
			double d2 = VecUtils.add(mop2.hitVec, VecUtils.vec(.5, .5, .5)).distanceTo(vec1);
			return d1 <= d2 ? mop1 : mop2;
		}
		if(mop1 != null)
			return mop1;
	
		return mop2;
	}
	
	public static MovingObjectPosition rayTraceEntities(World world, Vec3 vec1, Vec3 vec2, IEntitySelector selector) {
        Entity entity = null;
        AxisAlignedBB boundingBox = WorldUtils.getBoundingBox(vec1, vec2);
        List list = world.getEntitiesWithinAABBExcludingEntity(null, boundingBox.expand(1.0D, 1.0D, 1.0D), selector);
        double d0 = 0.0D;

        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity)list.get(j);

            if(!entity1.canBeCollidedWith() || (selector != null && !selector.isEntityApplicable(entity1)))
            	continue;
            
            float f = 0.3F;
            AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
            MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec1, vec2);

            if (movingobjectposition1 != null) {
                double d1 = vec1.distanceTo(movingobjectposition1.hitVec);

                if (d1 < d0 || d0 == 0.0D)
                {
                    entity = entity1;
                    d0 = d1;
                }
            }
        }

        if (entity != null) {
            return new MovingObjectPosition(entity);
        }
        return null;
	}
	
	public static MovingObjectPosition traceLiving(EntityLivingBase entity, double dist) {
		return traceLiving(entity, dist, null);
	}
	
	public static MovingObjectPosition traceLiving(EntityLivingBase entity, double dist, TraceOption option) {
		Motion3D mo = new Motion3D(entity, true);
		Vec3 v1 = mo.getPosVec(), v2 = mo.move(dist).getPosVec();
		return perform(entity.worldObj, v1, v2, option);
	}
	

}
