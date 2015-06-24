package cn.liutils.util.raytrace;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.util.generic.VecUtils;
import cn.liutils.util.helper.Motion3D;
import cn.liutils.util.mc.BlockFilters;
import cn.liutils.util.mc.EntitySelectors;
import cn.liutils.util.mc.IBlockFilter;
import cn.liutils.util.mc.WorldUtils;

/**
 * A better wrap up for ray trace routines, supporting entity filtering, block filtering, and combined RayTrace of
 * blocks and entities.
 * @author WeAthFolD
 */
public class Raytrace {

	/**
	 * Perform a ray trace.
	 * @param world
	 * @param vec1 Start point
	 * @param vec2 End point
	 * @param entitySel The entity filter
	 * @param blockSel The block filter
	 * @return The trace result, might be null
	 */
	public static MovingObjectPosition perform(World world, Vec3 vec1, Vec3 vec2, IEntitySelector entitySel, IBlockFilter blockSel) {
		MovingObjectPosition 
			mop1 = rayTraceEntities(world, vec1, vec2, entitySel),
			mop2 = rayTraceBlocks(world, vec1, vec2, blockSel);

		if(mop1 != null && mop2 != null) {
			double d1 = mop1.hitVec.distanceTo(vec1);
			double d2 = mop2.hitVec.distanceTo(vec1);
			return d1 <= d2 ? mop1 : mop2;
		}
		if(mop1 != null)
			return mop1;
	
		return mop2;
	}
	
	public static MovingObjectPosition perform(World world, Vec3 vec1, Vec3 vec2, IEntitySelector entitySel) {
		return perform(world, vec1, vec2, entitySel, null);
	}
	
	public static MovingObjectPosition perform(World world, Vec3 vec1, Vec3 vec2) {
		return perform(world, vec1, vec2, null, null);
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
	
	/**
	 * Mojang code with minor changes to support block filtering.
	 */
    public static MovingObjectPosition rayTraceBlocks(World world, Vec3 v1, Vec3 v2, IBlockFilter filter) {
        if(Double.isNaN(v1.xCoord) || Double.isNaN(v1.yCoord) || Double.isNaN(v1.zCoord) ||
        		Double.isNaN(v2.xCoord) || Double.isNaN(v2.yCoord) || Double.isNaN(v2.zCoord)) {
        	return null;
        }
        
    	//HACKHACK: copy the vec to prevent modifying the parameter
    	v1 = VecUtils.copy(v1);
    	if(filter == null)
    		filter = BlockFilters.filNormal;
        
        int i = MathHelper.floor_double(v2.xCoord);
        int j = MathHelper.floor_double(v2.yCoord);
        int k = MathHelper.floor_double(v2.zCoord);
        int l = MathHelper.floor_double(v1.xCoord);
        int i1 = MathHelper.floor_double(v1.yCoord);
        int j1 = MathHelper.floor_double(v1.zCoord);
        Block block = world.getBlock(l, i1, j1);
        
        int k1 = world.getBlockMetadata(l, i1, j1);

        if (filter.accepts(world, l, i1, j1, block) && block.canCollideCheck(k1, false))
        {
            MovingObjectPosition movingobjectposition = block.collisionRayTrace(world, l, i1, j1, v1, v2);

            if (movingobjectposition != null)
            {
                return movingobjectposition;
            }
        }

        MovingObjectPosition movingobjectposition2 = null;
        k1 = 200;

        while (k1-- >= 0)
        {
            if (Double.isNaN(v1.xCoord) || Double.isNaN(v1.yCoord) || Double.isNaN(v1.zCoord))
            {
                return null;
            }

            if (l == i && i1 == j && j1 == k)
            {
                return null;
            }

            boolean flag6 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d0 = 999.0D;
            double d1 = 999.0D;
            double d2 = 999.0D;

            if (i > l)
            {
                d0 = (double)l + 1.0D;
            }
            else if (i < l)
            {
                d0 = (double)l + 0.0D;
            }
            else
            {
                flag6 = false;
            }

            if (j > i1)
            {
                d1 = (double)i1 + 1.0D;
            }
            else if (j < i1)
            {
                d1 = (double)i1 + 0.0D;
            }
            else
            {
                flag3 = false;
            }

            if (k > j1)
            {
                d2 = (double)j1 + 1.0D;
            }
            else if (k < j1)
            {
                d2 = (double)j1 + 0.0D;
            }
            else
            {
                flag4 = false;
            }

            double d3 = 999.0D;
            double d4 = 999.0D;
            double d5 = 999.0D;
            double d6 = v2.xCoord - v1.xCoord;
            double d7 = v2.yCoord - v1.yCoord;
            double d8 = v2.zCoord - v1.zCoord;

            if (flag6)
            {
                d3 = (d0 - v1.xCoord) / d6;
            }

            if (flag3)
            {
                d4 = (d1 - v1.yCoord) / d7;
            }

            if (flag4)
            {
                d5 = (d2 - v1.zCoord) / d8;
            }

            boolean flag5 = false;
            byte b0;

            if (d3 < d4 && d3 < d5)
            {
                if (i > l)
                {
                    b0 = 4;
                }
                else
                {
                    b0 = 5;
                }

                v1.xCoord = d0;
                v1.yCoord += d7 * d3;
                v1.zCoord += d8 * d3;
            }
            else if (d4 < d5)
            {
                if (j > i1)
                {
                    b0 = 0;
                }
                else
                {
                    b0 = 1;
                }

                v1.xCoord += d6 * d4;
                v1.yCoord = d1;
                v1.zCoord += d8 * d4;
            }
            else
            {
                if (k > j1)
                {
                    b0 = 2;
                }
                else
                {
                    b0 = 3;
                }

                v1.xCoord += d6 * d5;
                v1.yCoord += d7 * d5;
                v1.zCoord = d2;
            }

            Vec3 vec32 = Vec3.createVectorHelper(v1.xCoord, v1.yCoord, v1.zCoord);
            l = (int)(vec32.xCoord = (double)MathHelper.floor_double(v1.xCoord));

            if (b0 == 5)
            {
                --l;
                ++vec32.xCoord;
            }

            i1 = (int)(vec32.yCoord = (double)MathHelper.floor_double(v1.yCoord));

            if (b0 == 1)
            {
                --i1;
                ++vec32.yCoord;
            }

            j1 = (int)(vec32.zCoord = (double)MathHelper.floor_double(v1.zCoord));

            if (b0 == 3)
            {
                --j1;
                ++vec32.zCoord;
            }

            Block block1 = world.getBlock(l, i1, j1);
            int l1 = world.getBlockMetadata(l, i1, j1);

            if (filter.accepts(world, l, i1, j1, block1))
            {
                if (block1.canCollideCheck(l1, false))
                {
                    MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(world, l, i1, j1, v1, v2);

                    if (movingobjectposition1 != null)
                    {
                        return movingobjectposition1;
                    }
                }
                else
                {
                    movingobjectposition2 = new MovingObjectPosition(l, i1, j1, b0, v1, false);
                }
            }
        }

        return null;
    }
	
	public static MovingObjectPosition traceLiving(EntityLivingBase entity, double dist) {
		return traceLiving(entity, dist, null, null);
	}
	
	public static MovingObjectPosition traceLiving(EntityLivingBase entity, double dist, IEntitySelector entitySel) {
		return traceLiving(entity, dist, entitySel, null);
	}
	
	/**
	 * Performs a RayTrace starting from the target entity's eye towards its looking direction.
	 * The trace will automatically ignore the target entity.
	 */
	public static MovingObjectPosition traceLiving(EntityLivingBase entity, double dist, IEntitySelector entitySel, IBlockFilter blockSel) {
		Motion3D mo = new Motion3D(entity, true);
		Vec3 v1 = mo.getPosVec(), v2 = mo.move(dist).getPosVec();
		
		IEntitySelector exclude = EntitySelectors.excludeOf(entity);
		
		return perform(entity.worldObj, v1, v2, entitySel == null ? exclude : EntitySelectors.combine(exclude, entitySel), blockSel);
	}
	

}
