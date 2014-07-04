/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.entity;

import cn.liutils.api.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author WeAthFolD
 *
 */
public class EntityBlock extends Entity {

	public int blockID = 0;
	public int blockX, blockY, blockZ;
	
	/**
	 * @param par1World
	 */
	public EntityBlock(World par1World) {
		super(par1World);
	}
	
	public EntityBlock(World world, int bx, int by, int bz) {
		this(world, world.getBlockId(bx, by, bz));
		blockX = bx;
		blockY = by;
		blockZ = bz;
		setPosition(bx + 0.5, by + 0.5, bz + 0.5);
	}
	
	public EntityBlock(World world, BlockPos pos) {
		this(world, pos.x, pos.y, pos.z);
	}
	
	private EntityBlock(World world, int BID) {
		super(world);
		blockID = BID;
	}
	
	@Override
	public void onUpdate() {
		if(worldObj.isRemote && blockID == 0) {
			blockID = dataWatcher.getWatchableObjectShort(10);
		} else {
			if(blockID == 0) setDead();
			dataWatcher.updateObject(10, Short.valueOf((short)blockID));
		}
		
		super.onUpdate();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.Entity#entityInit()
	 */
	@Override
	protected void entityInit() {
		dataWatcher.addObject(10, Short.valueOf((short) 0));
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.Entity#readEntityFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		blockID = nbt.getShort("blockID");
		blockX = nbt.getInteger("blockX");
		blockY = nbt.getInteger("blockY");
		blockZ = nbt.getInteger("blockZ");
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.Entity#writeEntityToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setShort("blockID", (short) blockID);
		nbt.setInteger("blockX", blockX);
		nbt.setInteger("blockY", blockY);
		nbt.setInteger("blockZ", blockZ);
	}

}
