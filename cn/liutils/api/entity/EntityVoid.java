/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author WeAthFolD
 *
 */
public class EntityVoid extends Entity {

	/**
	 * @param par1World
	 */
	public EntityVoid(World par1World) {
		super(par1World);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.Entity#entityInit()
	 */
	@Override
	protected void entityInit() {
		setDead();
	}
	
    @Override
	public void onUpdate()
    {
    }

	/* (non-Javadoc)
	 * @see net.minecraft.entity.Entity#readEntityFromNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	/* (non-Javadoc)
	 * @see net.minecraft.entity.Entity#writeEntityToNBT(net.minecraft.nbt.NBTTagCompound)
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

}
