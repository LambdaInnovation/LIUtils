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
package cn.liutils.api.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cn.liutils.api.util.GenericUtils;

/**
 * The superclass of containers.
 * 
 * @author WeAthFolD
 * 
 */
public abstract class LIContainerBase extends BlockContainer {
	/**
	 * GUI id.
	 */
	protected int guiId = -1;
	Object instance = null;
	
	/**
	 * @param id
	 * @param mat
	 */
	public LIContainerBase(int id, Material mat, Object mod) {
		super(mat);
	}
	
	/**
	 * Set icon paths and unlocalizedName at the same time. Remember to use standard of namespace.
	 * 同时设置图标路径和unlocalizedName的方便函数。注意记得使用标准namespace规范。
	 * @param name
	 */
	public void setIAndU(String name) {
		String realName =  GenericUtils.splitString(name, false);
		setBlockName(realName);
		setBlockTextureName(name);
	}
	
	@Override
	/**
	 * It will be called when the BlockActivated then show the GUI.
	 * 右键激活方块时调用的函数，方便地打开GUI。
	 */
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (guiId == -1 || tileEntity == null || player.isSneaking()) {
			return false;
		}
		player.openGui(instance, guiId, world, x, y, z);
		return true;
	}

	/**
	 * Drop out all items of a inventory.
	 * 让某个inventory中的物品全部掉落出来。
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param inventory
	 */
	protected void dropItems(World world, int x, int y, int z,
			ItemStack[] inventory) {
		Random rand = new Random();

		for (ItemStack item : inventory) {

			if (item != null && item.stackSize > 0) {
				float rx = rand.nextFloat() * 0.8F + 0.1F;
				float ry = rand.nextFloat() * 0.8F + 0.1F;
				float rz = rand.nextFloat() * 0.8F + 0.1F;

				EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z
						+ rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));

				if (item.hasTagCompound()) {
					entityItem.getEntityItem().setTagCompound(
							(NBTTagCompound) item.getTagCompound().copy());
				}

				float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntityInWorld(entityItem);
				item.stackSize = 0;
			}
		}
	}

	@Override
	public abstract TileEntity createNewTileEntity(World var1, int var2);

}
