/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.block;

import cn.liutils.core.LIUtils;
import cn.liutils.core.network.MsgTileDirMulti;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * Handles the metadata in TileEntity so we can bypass the byte-value size limit of blockMetadata.
 * @author WeAthFolD
 */
public class TileDirectionalMulti extends TileEntity implements IMetadataProvider {

	private boolean synced = false;
	private int ticksUntilReq = 4;
	int metadata = -1;
	
	@Override
	public void updateEntity() {
		if(metadata == -1) {
			metadata = this.getBlockMetadata();
		}
		if(worldObj.isRemote && !synced && ++ticksUntilReq == 5) {
			ticksUntilReq = 0;
			LIUtils.netHandler.sendToServer(new MsgTileDirMulti.Request(this));
		}
	}
	
	@Override
	public void setMetadata(int meta) {
		synced = true;
		metadata = meta;
	}
	
    @Override
	public void readFromNBT(NBTTagCompound nbt)
    {
    	metadata = nbt.getInteger("meta");
        super.readFromNBT(nbt);
    }

    @Override
	public void writeToNBT(NBTTagCompound nbt)
    {
    	nbt.setInteger("meta", metadata);
        super.writeToNBT(nbt);
    }

	@Override
	public int getMetadata() {
		if(metadata == -1)
			metadata = this.getBlockMetadata();
		return metadata;
	}

}
