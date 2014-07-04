/** 
 * Copyright (c) LambdaCraft Modding Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.half-life.cn/
 * 
 * LambdaCraft is open-source. It is distributed under the terms of the
 * LambdaCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * LambdaCraft是完全开源的。它的发布遵从《LambdaCraft开源协议》。你允许阅读，修改以及调试运行
 * 源代码， 然而你不允许将源代码以另外任何的方式发布，除非你得到了版权所有者的许可。
 */
package cn.liutils.api.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cn.liutils.api.util.selector.EntitySelectorLiving;
import cn.liutils.api.util.selector.EntitySelectorPlayer;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * 一些乱七八糟的辅助函数。
 * @author WeAthFolD
 */
public class GenericUtils {

	public static IEntitySelector selectorLiving = new EntitySelectorLiving(),
			selectorPlayer = new EntitySelectorPlayer();

	private static Random rand = new Random();
	
	/**
	 * 自动封装横向视角的角度。
	 * @param f
	 * @return
	 */
	public static float wrapYawAngle(float f) {
		if(f > 180.0F)
			f -= 360.0F;
		else if(f < -180.0F)
			f = 360.0F - f;
		return f;
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
					int id = world.getBlockId(x, y, z);
					if(id != 0) {
						set.add(new BlockPos(x, y, z, id));
					}
				}
			}
		}
		return set;
	}
	
	public static NBTTagCompound loadCompound(ItemStack item) {
		if(item.stackTagCompound == null)
			item.stackTagCompound = new NBTTagCompound();
		return item.stackTagCompound;
	}
	
	/**
	 * 获取Entity的体积。
	 * @param e
	 * @return
	 */
	public static float getEntitySize(Entity e) {
		return e.width * e.width * e.height;
	}
	
	/**
	 * 获取一个随机音效，目录名遵循"soundname[a,b,c,d,...]"
	 * @param sndPath
	 * @param countSounds
	 * @return
	 */
	public static String getRandomSound(String sndPath, int countSounds) {
		int a = rand.nextInt(countSounds);
		return sndPath.concat(String.valueOf((char)('a' + a)));
	}
	
	/**
	 * 用来分割MC命名空间规范的字符串，可以选择返回":"后的内容或是返回命名空间。
	 * @param str 要分割的字符串
	 * @param isNamespace
	 * @return
	 */
	public static String splitString(String str, boolean isNamespace) {
		String[] strs = str.split(":");
		if(strs.length < 2) return str;
		return isNamespace ? strs[0] : strs[1];
	}
	
	public static List<Entity> getEntitiesAround(Entity e, double rad) {
		return getEntitiesAround(e.worldObj, e.posX, e.posY, e.posZ, rad, null);
	}
	
	public static List<Entity> getEntitiesAround(Entity e, double rad, IEntitySelector selector) {
		return getEntitiesAround(e.worldObj, e.posX, e.posY, e.posZ, rad, selector);
	}
	
	public static List<Entity> getEntitiesAround_CheckSight(EntityLivingBase ent, double rad, IEntitySelector selector) {
		List<Entity> l = getEntitiesAround(ent.worldObj, ent.posX, ent.posY, ent.posZ, rad, selector);
		if(l != null)
			for(int i = 0; i < l.size(); i++) {
				Entity e = l.get(i);
				if(!ent.canEntityBeSeen(ent))
					l.remove(i);
			}
		return l;
	}
	
	public static Entity getNearestEntityTo(Entity pos, List<Entity> list) {
		Entity ent = null;
		double dist = 10000.0;
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
	
	public static List<Entity> getEntitiesAround(World world, double x, double y, double z, double rad, IEntitySelector selector, Entity...exclusions) {
		rad *= 0.5;
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(x - rad, y - rad, z - rad, x + rad, y + rad, z + rad);
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(exclusions.length > 0 ? exclusions[0] : null, box, selector);
		return list;
	}

	public static <T, U> T getKeyByValueFromMap(Map<T, U>map, U value) {
        T o = null;  
        ArrayList all = new ArrayList(); // 建一个数组用来存放符合条件的KEY值  
        Set set = map.entrySet();  
        Iterator it = set.iterator();  
        while (it.hasNext()) {  
            Map.Entry<T, U> entry = (Map.Entry) it.next();  
            if (entry.getValue().equals(value)) {  
                o = entry.getKey();  
                return o;
            }  
        }
		return null; 
	}
	
	public static <T> T safeFetchFrom(List<T> list, int id) {
		if(id >= 0 && id < list.size())
			return list.get(id);
		return null;
	}
	
}
