package cn.liutils.api.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cn.liutils.api.util.selector.EntitySelectorLiving;
import cn.liutils.api.util.selector.EntitySelectorPlayer;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * All sorts of utility functions.
 * @author WeAthFolD
 */
public class GenericUtils {

	public static IEntitySelector selectorLiving = new EntitySelectorLiving(),
			selectorPlayer = new EntitySelectorPlayer();
	
	private static Random RNG = new Random();
	
	public static float wrapYawAngle(float f) {
		if(f > 180.0F)
			f %= 360F;
		else if(f < -180.0F)
			f = (360.0F - f) % 360F;
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
					Block id = world.getBlock(x, y, z);
					if(id != Blocks.air) {
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
	
	@SideOnly(Side.CLIENT)
	/**
	 * Judge if the player is playing the client game and isn't opening any GUI.
	 * @return
	 */
	public static boolean isPlayerInGame() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		return player != null && Minecraft.getMinecraft().currentScreen == null;
	}
	
}
