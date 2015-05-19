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
package cn.liutils.util;

import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.core.entity.IEntityLink;

/**
 * Entity-related utility functions
 * @author WeAthFolD
 */
public class EntityUtils {

	/**
	 * Try to find the index of a item in player's inventory. if fail, return -1.
	 */
	public static int getSlotByStack(ItemStack item, EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		for(int i = 0; i < inv.mainInventory.length; i++) {
			ItemStack is = inv.mainInventory[i];
			if(is != null && item == is)
				return i;
		}
		return -1;
	}
	
	/**
	 * Returns the distance from e1 to e2 squared in plane XZ.
	 */
	public static double getDistanceSqFlat(Entity e1, Entity e2) {
		double x1 = e1.posX - e2.posX, x2 = e1.posZ - e2.posZ;
		return x1 * x1 + x2 * x2;
	}
	
	/**
	 * Return the list of entities around e in radius rad, e itself excluded.
	 */
	public static List<Entity> getEntitiesAround(Entity e, double rad) {
		return getEntitiesAround(e.worldObj, e.posX, e.posY, e.posZ, rad, null);
	}
	
	/**
	 * Get all entities around e within distance rad, with filter.
	 */
	public static List<Entity> getEntitiesAround(Entity e, double rad, IEntitySelector selector) {
		return getEntitiesAround(e.worldObj, e.posX, e.posY, e.posZ, rad, selector);
	}
	
	/**
	 * Getting all entites around ent while checking if ent can see it.
	 */
	public static List<Entity> getEntitiesAround_checkSight(EntityLivingBase ent, double rad, IEntitySelector selector) {
		List<Entity> l = getEntitiesAround(ent.worldObj, ent.posX, ent.posY, ent.posZ, rad, selector);
		if(l != null)
			for(int i = 0; i < l.size(); i++) {
				Entity e = l.get(i);
				if(!ent.canEntityBeSeen(ent))
					l.remove(i);
			}
		return l;
	}
	
	/**
	 * Get the nearest entity to pos within the list.
	 */
	public static Entity getNearestEntityTo(Entity pos, List<Entity> list) {
		Entity ent = null;
		double dist = Double.MAX_VALUE;
		if(list != null)
		for(Entity e : list) {
			double d = e.getDistanceSqToEntity(pos);
			if(d < dist) {
				ent = e;
				dist = d;
			}
		}
		return ent;
	}
	
	public static Entity getNearestEntityTo(Entity e, double rad, IEntitySelector sel, Entity ...exclusions) {
		return getNearestEntityTo(e, getEntitiesAround(e.worldObj, e.posX, e.posY, e.posZ, rad, sel, exclusions));
	}
	
	/**
	 * Get all entities around space point(x, y, z) in radius [rad], with exclusion and filter.
	 */
	public static List<Entity> getEntitiesAround(World world, double x, double y, double z, double rad, IEntitySelector selector, Entity...exclusions) {
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x - rad, y - rad, z - rad, x + rad, y + rad, z + rad);
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(exclusions.length > 0 ? exclusions[0] : null, box, selector);
		return list;
	}
	
	/**
	 * Set the entity pos to the corresponding vec3.
	 */
	public static void applyEntityToPos(Entity entity, Vec3 vec3) {
		entity.setPosition(vec3.xCoord, vec3.yCoord, vec3.zCoord);
	}

	/**
	 * WeAthFolD: 把一个奇怪的生物生成到世界中。
	 * mkpoli: 喂 变态 哪里奇怪了
	 */
	public static Entity spawnCreature(World par0World,
			Class<? extends EntityLiving> c, EntityLivingBase thrower) {
		double x, y, z;
		Vec3 lookVec = thrower.getLookVec();
		x = thrower.posX + lookVec.xCoord * 2;
		y = thrower.posY;
		z = thrower.posZ + lookVec.zCoord * 2;
		
		return spawnCreature(par0World, thrower, c, x, y, z);
	}
	
    public static Entity spawnCreature(World par0World, EntityLivingBase thrower, Class<? extends EntityLiving> c, double par2, double par4, double par6)
    {
            Entity entity = null;

    		try {
    			entity = c.getConstructor(World.class).newInstance(par0World);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
            
            for (int j = 0; j < 1; ++j)
            {

                if (entity != null && entity instanceof EntityLivingBase)
                {
                	EntityLiving entityliving = (EntityLiving)entity;
                    entity.setLocationAndAngles(par2, par4, par6, MathHelper.wrapAngleTo180_float(par0World.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.onSpawnWithEgg((IEntityLivingData)null);
                    if (entityliving instanceof IEntityLink && thrower != null) {
        				((IEntityLink) entityliving).setLinkedEntity(thrower);
        			}
                    par0World.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }

            return entity;
    }

	private static void setEntityHeading(Entity ent, double par1, double par3,
			double par5, double moveSpeed) {
		float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5
				* par5);
		par1 /= f2;
		par3 /= f2;
		par5 /= f2;
		par1 *= moveSpeed;
		par3 *= moveSpeed;
		par5 *= moveSpeed;
		ent.motionX = par1;
		ent.motionY = par3;
		ent.motionZ = par5;
		float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
		ent.prevRotationYaw = ent.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
		ent.prevRotationPitch = ent.rotationPitch = (float) (Math.atan2(par3,
				f3) * 180.0D / Math.PI);
	}
	
	public static Entity getNearestTargetWithinAABB(World world, double x, double y, double z, float range, IEntitySelector selector, Entity... exclusion) {
		List<Entity> entList = getEntitiesAround(world, x, y, z, range, selector, exclusion);
		double distance = range + 10000.0;
		Entity target = null;
		for(Entity e : entList) {
			for(Entity ex : exclusion)
				if(e == ex) continue;
			double d = e.getDistanceSq(x, y, z);
			if(d < distance) {
				target = e;
				distance = d;
			}
		}
		return target;
	}
	
	/**
	 * Calc the approximated voilume of entity.
	 */
	public static float getEntityVolume(Entity e) {
		return e.width * e.width * e.height;
	}

}
