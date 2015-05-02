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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.template.selector.EntitySelectorLiving;
import cn.liutils.template.selector.EntitySelectorPlayer;
import cn.liutils.util.misc.CustomExplosion;
import cn.liutils.util.space.BlockPos;
import cn.liutils.util.space.IBlockFilter;
import cn.liutils.util.space.Motion3D;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

/**
 * Many sorts of utility functions.
 * @warning 处于RBQ模式中，即将经历大量重构，慎用
 * @author WeAthFolD
 */
public class GenericUtils {

	public static IEntitySelector 
		selectorLiving = new EntitySelectorLiving(),
		selectorPlayer = new EntitySelectorPlayer();
	
	private static Random RNG = new Random();
	
	/**
	 * Return the running environment side.
	 */
	public static Side getSide() {
		return FMLCommonHandler.instance().getSide();
	}
	
	//World interact
	/**
	 * Simulate a player's attack.
	 * @param player
	 * @param damage
	 */
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
	
	public static MovingObjectPosition tracePlayer(EntityPlayer player, double dist) {
		Motion3D mo = new Motion3D(player, true);
		Vec3 v1 = mo.getPosVec(player.worldObj),
				v2 = mo.move(dist).getPosVec(player.worldObj);
		return player.worldObj.rayTraceBlocks(v1, v2);
	}
	
	public static MovingObjectPosition tracePlayerWithEntities(EntityPlayer player, double dist, IEntitySelector ies) {
		Motion3D mo = new Motion3D(player, true);
		Vec3 v1 = mo.getPosVec(player.worldObj),
			v2 = mo.move(dist).getPosVec(player.worldObj);
		return GenericUtils.rayTraceBlocksAndEntities(ies, player.worldObj, v1, v2, player);
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

	public static void explode(World world, Entity entity, float strengh, double posX, double posY, double posZ, double multiplier) {
		explode(world, entity, strengh, posX, posY, posZ, multiplier, true);
	}
	
	public static void explode(World world, Entity entity, float strengh, double posX, double posY, double posZ, double multiplier, boolean destroyTerrain) {
		if(world.isRemote)
			return;
		CustomExplosion exp = new CustomExplosion(world, entity, posX, posY, posZ, strengh)
			.setDamageMultiplyer(multiplier).setDestroyTerrain(destroyTerrain);
		exp.doExplosionA();
		exp.doExplosionB(true);
	}
	
	public static void doRangeDamage(World world, DamageSource src, Vec3 pos, float strengh, double radius, Entity... exclusion) {
		AxisAlignedBB par2 = AxisAlignedBB.getBoundingBox(pos.xCoord - radius, pos.yCoord - radius,
				pos.zCoord - radius, pos.xCoord + radius, pos.yCoord + radius, pos.zCoord + radius);
		List entitylist = world.getEntitiesWithinAABBExcludingEntity(null, par2);
		for (int i = 0; i < entitylist.size(); i++) {
			Entity ent = (Entity) entitylist.get(i);
			double distance = pos.distanceTo(Vec3.createVectorHelper(ent.posX, ent.posY, ent.posZ));
			//currently used linear attn, 1->0.1
			float damage = (float) Math.max(0, strengh * ( 1 - 0.9 * distance / radius));
			ent.attackEntityFrom(src , damage);
		}
	}
	
	public static float wrapYawAngle(float f) {
		if(f > 180.0F)
			f %= 360F;
		else if(f < -180.0F)
			f = (360.0F - f) % 360F;
		return f;
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
			return world.func_147447_a(vec1, vec2, false, true, false);
		return mop;
	}
	
	public static MovingObjectPosition traceBetweenEntities(Entity e1, Entity e2) {
		if(e1.worldObj != e2.worldObj) return null;
		Vec3 v1 = Vec3.createVectorHelper(e1.posX, e1.posY + e1.getEyeHeight(), e1.posZ),
				v2 = Vec3.createVectorHelper(e2.posX, e2.posY + e2.getEyeHeight(), e2.posZ);
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

        if (entity != null)
        {
        	System.out.println("Trace get  " + entity);
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
					if(filter.accepts(world, id, x, y, z)) {
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
	
	//Math
	public static double vecLen(double ...ds) {
		return Math.sqrt(vecLenSq(ds));
	}
	
	public static double vecLenSq(double ...ds) {
		double ret = 0.0d;
		for(double d : ds) {
			ret += d * d;
		}
		return ret;
	}
	
	public static double distanceSq(double[] vec1, double[] vec2) {
		if(vec1.length != vec2.length) {
			throw new RuntimeException("Inconsistent length");
		}
		
		double ret = 0.0;
		for(int i = 0; i < vec1.length; ++i) {
			double d = vec2[i] - vec1[i];
			ret += d * d;
		}
		
		return ret;
	}
	
	public static double distance(double x0, double y0, double z0, double x1, double y1, double z1) {
		return Math.sqrt(distanceSq(x0, y0, z0, x1, y1, z1));
	}
	
	public static double distanceSq(double x0, double y0, double z0, double x1, double y1, double z1) {
		return distanceSq(new double[] { x0, y0, z0 }, new double[] { x1, y1, z1 });
	}
	
	public static double distance(double[] vec1, double[] vec2) {
		return Math.sqrt(distanceSq(vec1, vec2));
	}
	
	public static Vec3 multiply(Vec3 vec, double factor) {
		return Vec3.createVectorHelper(vec.xCoord * factor, vec.yCoord * factor, vec.zCoord * factor);
	}
	
	public static double length(double dx, double dy, double dz) {
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public static int min(int... arr) {
		int min = Integer.MAX_VALUE;
		for(int i : arr) 
			if(i < min) min = i;
		return min;
	}
	
	public static double min(double ...arr) {
	    double min = Double.MAX_VALUE;
	    for(double d : arr)
	        if(d < min) min = d;
	    return min;
	}
	
	public static int max(int ...arr) {
	    int max = Integer.MIN_VALUE;
	    for(int i : arr)
	        if(i > max) max = i;
	    return max;
	}
	
	public static double max(double... arr) {
	    double max = Double.MIN_VALUE;
	    for(double d : arr)
	        if(d > max) max = d;
	    return max;
	}
	
	//Debug
	public static <T> T assertObj(T obj) {
		if (obj == null) {
			throw new NullPointerException();
		}
		return obj;
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
	
	/**
	 * Chop a string, doesn't care at all about seperators(spaces)
	 */
	public static List<String> rawChop(String str, int MAXCHARS) {
		List<String> ret = new ArrayList<String>();
		int cur = 0;
		while(cur < str.length()) {
			ret.add(str.substring(cur, Math.min(cur + MAXCHARS, str.length())));
			cur += MAXCHARS;
		}
		return ret;
	}
	
	public static <T> T safeFetchFrom(List<T> list, int id) {
		if(id >= 0 && id < list.size())
			return list.get(id);
		return null;
	}

}
