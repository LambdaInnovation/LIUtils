package cn.liutils.util;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cn.liutils.template.selector.EntitySelectorLiving;
import cn.liutils.template.selector.EntitySelectorPlayer;
import cn.liutils.util.space.BlockPos;
import cn.liutils.util.space.Motion3D;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * All sorts of utility functions.
 * @warning 处于RBQ模式中，即将经历大量重构，慎用
 * @author WeAthFolD
 */
public class GenericUtils {

	public static IEntitySelector selectorLiving = new EntitySelectorLiving(),
			selectorPlayer = new EntitySelectorPlayer();
	
	private static Random RNG = new Random();
	
	public static void doEntityAttack(Entity ent, DamageSource ds, float damage) {
		ent.attackEntityFrom(ds, damage);
	}
	
	public static void doPlayerAttack(EntityPlayer player, float damage) {
		Motion3D mo = new Motion3D(player, true);
		MovingObjectPosition mop = GenericUtils.rayTraceBlocksAndEntities(GenericUtils.selectorLiving,
			player.worldObj, 
			mo.getPosVec(player.worldObj), 
			mo.move(1.4).getPosVec(player.worldObj), 
			player);
		if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY) {
			mop.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), damage);
		}
	}
	
	public static void Explode(World world, Entity entity, float strengh,
			double radius, double posX, double posY, double posZ,
			int additionalDamage) {
		explode(world, entity, strengh, radius, posX, posY, posZ, additionalDamage, 1.0, 1.0F);
	}
	
	
	public static AxisAlignedBB getBoundingBox(double minX, double minY,double minZ,double maxX,double maxY, double maxZ) {
		double i;
		if(minX > maxX) {
			i = maxX;
			maxX = minX;
			minX = i;
		}
		if(minY > maxY) {
			i = maxY;
			maxY = minY;
			minY = i;
		}
		if(minZ > maxZ) {
			i = maxZ;
			maxZ = minZ;
			minZ = i;
		}
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}

	public static void explode(World world, Entity entity, float strengh,
			double radius, double posX, double posY, double posZ,
			int additionalDamage, double velocityRadius, float soundRadius) {
		if(world.isRemote) //Ignore client operations
			return;
		
		Explosion explosion = world.createExplosion(entity, posX, posY, posZ, strengh, true);
		
		if (additionalDamage <= 0)
			return;

		doRangeDamage(world, DamageSource.setExplosionSource(explosion), 
				world.getWorldVec3Pool().getVecFromPool(posX, posY, posZ), 
				additionalDamage, radius, entity);
	}
	
	public static void doRangeDamage(World world, DamageSource src, Vec3 pos, float strengh, double radius, Entity... exclusion) {
		AxisAlignedBB par2 = AxisAlignedBB.getBoundingBox(pos.xCoord - 4, pos.yCoord - 4,
				pos.zCoord - 4, pos.xCoord + 4, pos.yCoord + 4, pos.zCoord + 4);
		List entitylist = world
				.getEntitiesWithinAABBExcludingEntity(null, par2);
		if (entitylist.size() > 0) {
			for (int i = 0; i < entitylist.size(); i++) {
				Entity ent = (Entity) entitylist.get(i);
				if (ent instanceof EntityLivingBase) {
					double distance = pos.distanceTo(world.getWorldVec3Pool().getVecFromPool(ent.posX, ent.posY, ent.posZ));
					int damage = (int) ((1 - distance / 6.928) * strengh);
					ent.attackEntityFrom(src , damage);
				}
			}
		}
	}
	
	public static float wrapYawAngle(float f) {
		if(f > 180.0F)
			f %= 360F;
		else if(f < -180.0F)
			f = (360.0F - f) % 360F;
		return f;
	}
	
	public static double distance(double dx, double dy, double dz) {
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	/**
	 * An alias of Minecraft.getSystemTime() in case of server calls
	 * @return
	 */
    public static long getSystemTime()
    {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
	
	public static MovingObjectPosition rayTraceBlocksAndEntities(World world, Vec3 vec1, Vec3 vec2) {
		MovingObjectPosition mop = rayTraceEntities(null, world, vec1, vec2);
		if(mop == null)
			return world.rayTraceBlocks(vec1, vec2);
		return mop;
	}
	
	public static MovingObjectPosition rayTraceBlocksAndEntities(IEntitySelector selector, World world, Vec3 vec1, Vec3 vec2, Entity... exclusion) {
		MovingObjectPosition mop = rayTraceEntities(selector, world, vec1, vec2, exclusion);
		if(mop == null)
			return world.rayTraceBlocks(vec1, vec2);
		return mop;
	}
	
	public static MovingObjectPosition traceBetweenEntities(Entity e1, Entity e2) {
		if(e1.worldObj != e2.worldObj) return null;
		Vec3 v1 = e1.worldObj.getWorldVec3Pool().getVecFromPool(e1.posX, e1.posY + e1.getEyeHeight(), e1.posZ),
				v2 = e2.worldObj.getWorldVec3Pool().getVecFromPool(e2.posX, e2.posY + e2.getEyeHeight(), e2.posZ);
		MovingObjectPosition mop = e1.worldObj.rayTraceBlocks(v1, v2);
		return mop;
	}
	
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
	
	public static MovingObjectPosition rayTraceEntities(IEntitySelector selector, World world, Vec3 vec1, Vec3 vec2, Entity... exclusion) {
        Entity entity = null;
        AxisAlignedBB boundingBox = getBoundingBox(vec1, vec2);
        List list = world.getEntitiesWithinAABBExcludingEntity(null, boundingBox.expand(1.0D, 1.0D, 1.0D), selector);
        double d0 = 0.0D;

        for (int j = 0; j < list.size(); ++j)
        {
            Entity entity1 = (Entity)list.get(j);

            Boolean b = entity1.canBeCollidedWith();
            if(!b)
            	continue;
            for(Entity e : exclusion) {
            	if(e == entity1)
            		b = false;
            }
            if (b && entity1.canBeCollidedWith())
            {
                float f = 0.3F;
                AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f, f, f);
                MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec1, vec2);

                if (movingobjectposition1 != null)
                {
                    double d1 = vec1.distanceTo(movingobjectposition1.hitVec);

                    if (d1 < d0 || d0 == 0.0D)
                    {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null)
        {
            return new MovingObjectPosition(entity);
        }
        return null;
	}
	
	/**
	 * 获取一个区域内所有的方块信息。
	 * @param world
	 * @param box
	 * @return
	 */
	public static Set<BlockPos> getBlocksWithinAABB(World world, AxisAlignedBB box) {
		Set<BlockPos> set = new HashSet();
		int minX = MathHelper.floor_double(box.minX), minY = MathHelper.floor_double(box.minY), minZ = MathHelper.floor_double(box.minZ),
			maxX = MathHelper.ceiling_double_int(box.maxX), maxY = MathHelper.ceiling_double_int(box.maxY), maxZ = MathHelper.ceiling_double_int(box.maxZ);
		for(int x = minX; x <= maxX; x++) {
			for(int y = minY; y <= maxY; y++) {
				for(int z = minZ; z <= maxZ; z++) {
					Block id = world.getBlock(x, y, z);
					if(id != Blocks.air) {
						set.add(new BlockPos(x, y, z, id));
					}
				}
			}
		}
		return set;
	}
	
	public static NBTTagCompound loadCompound(ItemStack stack) {
		if(stack.stackTagCompound == null)
			stack.stackTagCompound = new NBTTagCompound();
		return stack.stackTagCompound;
	}
	
	/**
	 * Acquire a random sound string. the pathname goes like "soundname[a, b, c, d, ...]"
	 * @param sndPath
	 * @param countSounds
	 * @return
	 */
	public static String getRandomSound(String sndPath, int countSounds) {
		int a = RNG.nextInt(countSounds);
		return sndPath.concat(String.valueOf((char)('a' + a)));
	}
	
	/**
	 * get MC-namespace splitted string.
	 * @param str
	 * @param isNamespace
	 * @return splitted string
	 */
	public static String splitString(String str, boolean isNamespace) {
		String[] strs = str.split(":");
		if(strs.length < 2) return str;
		return isNamespace ? strs[0] : strs[1];
	}
	
	public static <T> T safeFetchFrom(List<T> list, int id) {
		if(id >= 0 && id < list.size())
			return list.get(id);
		return null;
	}
	
	public static Vec3 multiply(Vec3 vec, double factor) {
		return vec.myVec3LocalPool.getVecFromPool(vec.xCoord * factor, vec.yCoord * factor, vec.zCoord * factor);
	}
	
	public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double dz = z2 - z1;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static double length(double dx, double dy, double dz) {
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static int mini(int... arr) {
		int min = Integer.MAX_VALUE;
		for(int i : arr) 
			if(i < min) min = i;
		return min;
	}
	
	public static <T> T assertObj(T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
	}

}
