package cn.liutils.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import org.lwjgl.Sys;

import scala.Char;
import cn.liutils.template.selector.EntitySelectorLiving;
import cn.liutils.template.selector.EntitySelectorPlayer;
import cn.liutils.util.space.BlockPos;
import cn.liutils.util.space.IBlockFilter;
import cn.liutils.util.space.Motion3D;

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
	
	/**
	 * split the string into multiple lines, each line has no more char than MAXCHARS.
	 * It should be guaranteed that any word within the paragraph is no longer than MAXCHARS.
	 */
	public static List<String> chopString(String str, int MAXCHARS) {
		List<String> ret = new ArrayList();
		String[] chop = str.split(" ");
		String cur = "";
		for(int i = 0; i < chop.length; ++i) {
			if(chop[i].length() > MAXCHARS) {
				ret.addAll(rawChop(str, MAXCHARS));
			} else if(cur.length() + chop[i].length() <= MAXCHARS) {
				cur += chop[i] + " ";
			} else {
				ret.add(cur);
				cur = chop[i] + " ";
			}
		}
		if(cur != "") ret.add(cur);
		return ret;
	}
	
	public static List<String> rawChop(String str, int MAXCHARS) {
		List<String> ret = new ArrayList<String>();
		int cur = 0;
		while(cur < str.length()) {
			ret.add(str.substring(cur, Math.min(cur + MAXCHARS, str.length())));
			cur += MAXCHARS;
		}
		return ret;
	}
	
	public static MovingObjectPosition tracePlayer(EntityPlayer player, double dist) {
		Motion3D mo = new Motion3D(player, true);
		Vec3 v1 = mo.getPosVec(player.worldObj),
				v2 = mo.move(dist).getPosVec(player.worldObj);
		return player.worldObj.rayTraceBlocks(v1, v2);
	}
	
	public static int randIntv(int fr, int to) {
		return RNG.nextInt(to - fr) + fr;
	}
	
	public static double randIntv(double fr, double to) {
		return (to - fr) * RNG.nextDouble() + fr;
	}
	
	/**
	 * Return: how many not merged
	 */
	public static int mergeStackable(InventoryPlayer inv, ItemStack stack) {
		for(int i = 0; i < inv.getSizeInventory() - 4 && stack.stackSize > 0; ++i) {
			ItemStack is = inv.getStackInSlot(i);
			if(is != null && is.getItem() == stack.getItem()) {
				is.stackSize += stack.stackSize;
				int left = Math.max(0, is.stackSize - is.getMaxStackSize());
				stack.stackSize = left;
				is.stackSize -= left;
			}
		}
		if(stack.stackSize > 0) {
			int id = inv.getFirstEmptyStack();
			if(id == -1) {
				return stack.stackSize;
			}
			inv.setInventorySlotContents(id, stack.copy());
			return 0;
		}
		return 0;
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
		AxisAlignedBB par2 = AxisAlignedBB.getBoundingBox(pos.xCoord - radius, pos.yCoord - radius,
				pos.zCoord - radius, pos.xCoord + radius, pos.yCoord + radius, pos.zCoord + radius);
		System.out.println("rad " + radius);
		List entitylist = world.getEntitiesWithinAABBExcludingEntity(null, par2);
		System.out.println(entitylist.size());
		for (int i = 0; i < entitylist.size(); i++) {
			Entity ent = (Entity) entitylist.get(i);
			if (ent instanceof EntityLivingBase) {
				double distance = pos.distanceTo(world.getWorldVec3Pool().getVecFromPool(ent.posX, ent.posY, ent.posZ));
				//currently used linear attn, 1->0.1
				float damage = (float) Math.max(0, strengh * ( 1 - 0.9 * distance / radius));
				System.out.println(damage);
				ent.attackEntityFrom(src , damage);
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
		return Math.sqrt(distanceSq(dx, dy, dz));
	}
	
	public static double distanceSq(double dx, double dy, double dz) {
		return dx * dx + dy * dy + dz * dz;
	}
	
	public static double distanceSq(double x0, double y0, double z0, double x1, double y1, double z1) {
		return distanceSq(x0 - x1, y0 - y1, z0 - z1);
	}
	
	public static double distance(double x0, double y0, double z0, double x1, double y1, double z1) {
		return distance(x0 - x1, y0 - y1, z0 - z1);
	}
	
	public static double planeDistance(double dx, double dy) {
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static double planeDistance(double x0, double y0, double x1, double y1) {
		return planeDistance(x1 - x0, y1 - y0);
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
            for(Entity e : exclusion) {
            	if(entity1.equals(e))
            		b = false;
            }
            if(!b) continue;
            
            if (entity1.canBeCollidedWith())
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
	
	public static Set<BlockPos> getBlocksWithinAABB(World world, AxisAlignedBB box) {
		return getBlocksWithinAABB(world, box, IBlockFilter.always);
	}
	
	/**
	 * 获取一个区域内所有的方块信息。
	 * @param world
	 * @param box
	 * @return
	 */
	public static Set<BlockPos> getBlocksWithinAABB(World world, AxisAlignedBB box, IBlockFilter filter) {
		Set<BlockPos> set = new HashSet();
		int minX = MathHelper.floor_double(box.minX), minY = MathHelper.floor_double(box.minY), minZ = MathHelper.floor_double(box.minZ),
			maxX = MathHelper.ceiling_double_int(box.maxX), maxY = MathHelper.ceiling_double_int(box.maxY), maxZ = MathHelper.ceiling_double_int(box.maxZ);
		for(int x = minX; x <= maxX; x++) {
			for(int y = minY; y <= maxY; y++) {
				for(int z = minZ; z <= maxZ; z++) {
					Block id = world.getBlock(x, y, z);
					if(id != Blocks.air && filter.accepts(world, id, x, y, z)) {
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
