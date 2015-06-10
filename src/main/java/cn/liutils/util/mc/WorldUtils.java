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
package cn.liutils.util.mc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.util.helper.BlockPos;
import cn.liutils.util.helper.IBlockFilter;
import cn.liutils.util.mc.EntitySelectors.SelectorList;

/**
 * Utils about block/entity lookup and interaction.
 * @author WeAthFolD
 */
public class WorldUtils {
	
	public static AxisAlignedBB getBoundingBox(Vec3 vec1, Vec3 vec2) {
		double minX = 0.0, minY = 0.0, minZ = 0.0, maxX = 0.0, maxY = 0.0, maxZ = 0.0;
		if(vec1.xCoord < vec2.xCoord) {
			minX = vec1.xCoord;
			maxX = vec2.xCoord;
		} else {
			minX = vec2.xCoord;
			maxX = vec1.xCoord;
		}
		if(vec1.yCoord < vec2.yCoord) {
			minY = vec1.yCoord;
			maxY = vec2.yCoord;
		} else {
			minY = vec2.yCoord;
			maxY = vec1.yCoord;
		}
		if(vec1.zCoord < vec2.zCoord) {
			minZ = vec1.zCoord;
			maxZ = vec2.zCoord;
		} else {
			minZ = vec2.zCoord;
			maxZ = vec1.zCoord;
		}
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public static MovingObjectPosition rayTraceBlocksAndEntities(World world, Vec3 vec1, Vec3 vec2) {
		return rayTraceBlocksAndEntities(world, vec1, vec2, null);
	}

	public static MovingObjectPosition rayTraceBlocksAndEntities(World world, Vec3 vec1, Vec3 vec2, IEntitySelector ef, Entity ...excls) {
		MovingObjectPosition 
			mop1 = rayTraceEntities(world, vec1, vec2, ef, excls),
			mop2 = world.rayTraceBlocks(vec1, vec2);
		if(mop1 != null && mop2 != null) {
			double d1 = mop1.hitVec.distanceTo(vec1);
			double d2 = mop2.hitVec.distanceTo(vec1);
			return d1 <= d2 ? mop1 : mop2;
		}
		if(mop1 != null)
			return mop1;
		
		return mop2;
	}
	
	public static MovingObjectPosition rayTraceEntities(World world, Vec3 vec1, Vec3 vec2) {
		return rayTraceEntities(world, vec1, vec2, null);
	}
	
	public static MovingObjectPosition rayTraceEntities(World world, Vec3 vec1, Vec3 vec2, IEntitySelector selector, Entity... exclusion) {
        Entity entity = null;
        AxisAlignedBB boundingBox = getBoundingBox(vec1, vec2);
        List list = world.getEntitiesWithinAABBExcludingEntity(null, boundingBox.expand(1.0D, 1.0D, 1.0D), selector);
        double d0 = 0.0D;

        for (int j = 0; j < list.size(); ++j) {
            Entity entity1 = (Entity)list.get(j);

            if(!entity1.canBeCollidedWith() || (selector != null && !selector.isEntityApplicable(entity1)))
            	continue;
            
            boolean b = true;
            for(Entity e : exclusion) {
            	if(entity1.equals(e)) {
            		b = false;
            		break;
            	}
            }
            if(!b) continue;
            
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
	
	public static List<BlockPos> getBlocksWithin(Entity entity, double range, int max, IBlockFilter ...filters) {
		return getBlocksWithin(entity.worldObj, entity.posX, entity.posY, entity.posZ, range, max, filters);
	}
	
	public static List<BlockPos> getBlocksWithin(TileEntity te, double range, int max, IBlockFilter ...filters) {
		return getBlocksWithin(te.getWorldObj(), te.xCoord + 0.5, te.yCoord + 0.5, te.zCoord + 0.5, range, max, filters);
	}
	
	public static List<BlockPos> getBlocksWithin(
			World world,
			final double x, final double y, final double z,
			double range, int max,
			IBlockFilter ...filter) {
		IBlockFilter [] fs = new IBlockFilter[filter.length + 1];
		for(int i = 0; i < filter.length; ++i)
			fs[i] = filter[i];
		
		final double rangeSq = range * range;
		
		fs[filter.length] = new IBlockFilter() {

			@Override
			public boolean accepts(World world, int xx, int yy, int zz) {
				double dx = xx - x, dy = yy - y, dz = zz - z;
				return dx * dx + dy * dy + dz * dz <= rangeSq;
			}
			
		};
		
		int minX = MathHelper.floor_double(x - range),
			minY = MathHelper.floor_double(y - range),
			minZ = MathHelper.floor_double(z - range),
			maxX = MathHelper.ceiling_double_int(x + range),
			maxY = MathHelper.ceiling_double_int(y + range),
			maxZ = MathHelper.ceiling_double_int(z + range);
		
		return getBlocksWithin(world, minX, minY, minZ, maxX, maxY, maxZ, max, fs);
	}
	
	public static List<BlockPos> getBlocksWithin(
		World world,
		int minX, int minY, int minZ, 
		int maxX, int maxY, int maxZ, 
		int max, 
		IBlockFilter ...filter) {
		
		List<BlockPos> ret = new ArrayList();
		for(int x = minX; x <= maxX; ++x) {
			for(int y = minY; y <= maxY; ++y) {
				for(int z = minZ; z <= maxZ; ++z) {
					boolean match = true;
					for(IBlockFilter f : filter) {
						if(!f.accepts(world, x, y, z)) {
							match = false;
							break;
						}
					}
					if(match) {
						ret.add(new BlockPos(world, x, y, z));
						if(ret.size() == max)
							return ret;
					}
				}
			}
		}
		
		return ret;
	}
	
	public static List<Entity> getEntities(TileEntity te, double range, IEntitySelector filter) {
		return getEntities(te.getWorldObj(), te.xCoord + 0.5, te.yCoord + 0.5, te.zCoord + 0.5, range, filter);
	}
	
	public static List<Entity> getEntities(Entity ent, double range, IEntitySelector filter) {
		return getEntities(ent.worldObj, ent.posX, ent.posY, ent.posZ, range, filter);
	}
	
	public static List<Entity> getEntities(World world, double x, double y, double z, double range, IEntitySelector filter) {
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
			x - range, y - range, z - range, 
			x + range, y + range, z + range);
		SelectorList list = new SelectorList(filter, new EntitySelectors.RestrictRange(x, y, z, range));
		return getEntities(world, box, list);
	}
	
	public static List<Entity> getEntities(World world, AxisAlignedBB box, IEntitySelector filter) {
		return world.getEntitiesWithinAABBExcludingEntity(null, box, filter);
	}
	
}
