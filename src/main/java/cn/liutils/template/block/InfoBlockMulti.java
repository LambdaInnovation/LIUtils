/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.network.RegNetworkCall;
import cpw.mods.fml.relauncher.Side;

/**
 * Class that stores and handles per-block orientation&sub block ID info. You should delegate the
 *  save&store methods via your TileEntity that implements IMultiTile.
 * @author WeathFolD
 */
@RegistrationClass
public class InfoBlockMulti {
	
	final TileEntity te;
	
	ForgeDirection dir;
	int subID;
	
	boolean loaded; //Client-Only flag. Indicate if it was synced.
	int syncCD; //Ticks until sending next sync request.

	InfoBlockMulti(TileEntity _te, ForgeDirection _dir, int sid) {
		te = _te;
		dir = _dir;
		subID = sid;
	}
	
	/**
	 * Use this Ctor to restore your Info in TileEntity's readNBT method.
	 */
	public InfoBlockMulti(TileEntity _te, NBTTagCompound tag) {
		te = _te;
		load(tag);
	}
	
	/**
	 * Delegate this method in your TileEntity's updateEntity method.
	 */
	public void update() {
		if(te.getWorldObj().isRemote) {
			if(!loaded) {
				if(syncCD == 0) {
					//Delegate sync method
					syncCD = 10;
				} else {
					--syncCD;
				}
			}
		}
	}
	
	public void save(NBTTagCompound tag) {
		tag.setByte("dir", (byte) dir.ordinal());
		tag.setInteger("sub", subID);
	}
	
	public void load(NBTTagCompound tag) {
		dir = ForgeDirection.values()[tag.getByte("dir")];
		subID = tag.getInteger("sub");
	}
	
	@RegNetworkCall(side = Side.SERVER)
	public static void requestedByClient(int x, int y, int z) {
		
	}
	
	@RegNetworkCall(side = Side.CLIENT)
	public static void receiveReply(byte dir, byte sid) {
		
	}

}
