/**
 * 
 */
package cn.liutils.template.block;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Base class for generic energy sinks.
 * @author WeathFolD
 */
public class TileGenericSink extends TileEntity implements IEnergySink {
	
	public int maxInput = 512;
	protected double maxEnergy;
	
	public double curEnergy;
	
	protected boolean init = false;
	protected boolean energyNetInit = false;
	
	public TileGenericSink() {}
	
	@Override
	public void onChunkUnload() {
		onDestroy();
	}
	
	@Override
	public void invalidate() {
		onDestroy();
	}
	
	@Override
	public void updateEntity() {
		if(!init) {
			init = onInit();
		}
	}
	
	void onDestroy() {
		if(!worldObj.isRemote)
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
	}
	
	boolean onInit() {
		if(!worldObj.isRemote) {
			energyNetInit = !MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			return energyNetInit;
		}
		return true;
	}

	public TileGenericSink(double maxEnergy) {
		this.maxEnergy = maxEnergy;
	}
	
	public double getMaxEnergy() {
		return maxEnergy;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter,
			ForgeDirection direction) {
		return true;
	}

	@Override
	public double demandedEnergyUnits() {
		return Math.max(0, getMaxEnergy() - curEnergy);
	}

	@Override
	public double injectEnergyUnits(ForgeDirection fd, double amt) {
		curEnergy += amt;
		double left = Math.max(0, curEnergy - getMaxEnergy());
		curEnergy -= left;
		return left;
	}

	@Override
	public int getMaxSafeInput() {
		return maxInput;
	}
	
    public void readFromNBT(NBTTagCompound nbt) {
    	super.readFromNBT(nbt);
    	curEnergy = nbt.getDouble("energy");
    }
    
    public void writeToNBT(NBTTagCompound nbt) {
    	super.writeToNBT(nbt);
    	nbt.setDouble("energy", curEnergy);
    }

}
