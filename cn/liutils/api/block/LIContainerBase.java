/**
 * Code by Lambda Innovation, 2013.
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
 * @author WeAthFolD
 *
 */
public abstract class LIContainerBase extends BlockContainer {

	protected int guiId = -1;
	Object instance = null;
	
	/**
	 * @param id
	 * @param mat
	 */
	public LIContainerBase(int id, Material mat, Object mod) {
		super(id, mat);
	}
	
	/**
	 * 同时设置图标路径和unlocalizedName的方便函数。注意记得使用标准namespace规范。
	 * @param name
	 */
	public void setIAndU(String name) {
		String realName =  GenericUtils.splitString(name, false);
		setUnlocalizedName(realName);
		setTextureName(name);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.block.ITileEntityProvider#createNewTileEntity(net.minecraft.world.World)
	 */
	@Override
	public abstract TileEntity createNewTileEntity(World world);
	
	@Override
	/**
	 * 右键激活方块时调用的函数，方便地打开GUI。
	 */
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int idk, float what, float these, float are) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (guiId == -1 || tileEntity == null || player.isSneaking()) {
			return false;
		}
		player.openGui(instance, guiId, world, x, y, z);
		return true;
	}

	/**
	 * 让某个inventory中的物品全部掉落出来。
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
						+ rz, new ItemStack(item.itemID, item.stackSize,
						item.getItemDamage()));

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

}
