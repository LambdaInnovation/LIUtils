/**
 * Copyright (C) Lambda-Innovation, 2013-2014
 * This code is open-source. Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 */
package cn.liutils.api.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * 主要用来在子方块太多的时候，托管metadata以绕过byte值存储的限制~
 * @author WeAthFolD
 */
public class TileDirectionedMulti extends TileEntity implements IMetadataProvider {

	int metadata;
	
	public TileDirectionedMulti(int meta) {
		metadata = meta;
	}
	
	public TileDirectionedMulti() {}
	
	public void updateEntity() {
		if(metadata == 0)
			metadata = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}
	
	public void setMetadata(int meta) {
		metadata = meta;
	}
	
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
    }

	@Override
	public int getMetadata() {
		return metadata;
	}

}
