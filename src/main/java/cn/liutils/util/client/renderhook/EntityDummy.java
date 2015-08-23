/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.util.client.renderhook;

import cn.annoreg.core.Registrant;
import cn.annoreg.mc.RegEntity;
import cn.liutils.util.client.ViewOptimize.IAssociatePlayer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
@Registrant
@RegEntity(clientOnly = true)
@RegEntity.HasRender
public class EntityDummy extends Entity implements IAssociatePlayer {
	
	@RegEntity.Render
	public static RenderDummy renderer;
	
	EntityPlayer player;
	DummyRenderData data;
	
	boolean set;
	float lastRotationYaw;
	float lastRotationPitch;
	
	public EntityDummy(EntityPlayer _player) {
		super(_player.worldObj);
		player = _player;
		data = DummyRenderData.get(player);
		ignoreFrustumCheck = true;
	}

	@Override
	protected void entityInit() {
	}
	
	@Override
	public void onUpdate() {
		if(!set) {
			set = true;
			lastRotationYaw = player.rotationYawHead;
			lastRotationPitch = player.rotationPitch;
		} else {
			lastRotationYaw = rotationYaw;
			lastRotationPitch = rotationPitch;
		}
		
		posX = player.posX;
		posY = player.posY;
		posZ = player.posZ;
		rotationYaw = player.rotationYawHead;
		rotationPitch = player.rotationPitch;
	}
	
	@Override
	public EntityPlayer getPlayer() {
		return player;
	}
	
	/**
	 * TODO: Support all passes
	 */
	@Override
	public boolean shouldRenderInPass(int pass) {
		return pass == 1;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound t) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound t) {}

}
