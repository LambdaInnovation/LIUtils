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

public abstract class LIContainerBase extends BlockContainer {

	protected int guiId = -1;
	Object instance = null;
	
	/**
	 * @param id
	 * @param mat
	 */
	public LIContainerBase(Material mat) {
		super(mat);
	}
	
	/**
	 * Utility func that sets both BlockName and TextureName. Remember to use namespace.
	 * @param name
	 */
	public void setIAndU(String name) {
		String realName =  GenericUtils.splitString(name, false);
		setBlockName(realName);
		setBlockTextureName(name);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (guiId == -1 || tileEntity == null || player.isSneaking()) {
			return false;
		}
		player.openGui(instance, guiId, world, x, y, z);
		return true;
	}
	
	protected static Random rand = new Random();
	
	protected void dropItems(World world, int x, int y, int z,
			ItemStack[] inventory) {
		for (ItemStack item : inventory) {
			dropItem(world, x, y, z, item);
		}
	}
	
	protected void dropItem(World world, int x, int y, int z, ItemStack item) {
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

	@Override
	public abstract TileEntity createNewTileEntity(World var1, int var2);

}
