/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.player.state;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * 
 * @author Violet
 *
 */
public class StateLockPosition extends StateBase {

	public static final StateType TYPE = StateType.LOCK_POSITION;
	
	private double posX, posY, posZ;
	
	public StateLockPosition(EntityPlayer player, ByteBuf buf) {
		super(TYPE, player, buf);
		if (player.worldObj.isRemote)
			fmlDispatcher.registerClientTick(this);
		else
			fmlDispatcher.registerServerTick(this);
	}

	public StateLockPosition(EntityPlayer player, int ticks) {
		super(TYPE, player, ticks);
		posX = player.posX;
		posY = player.posY;
		posZ = player.posZ;
		if (player.worldObj.isRemote)
			fmlDispatcher.registerClientTick(this);
		else
			fmlDispatcher.registerServerTick(this);
	}
	
	@Override
	protected boolean onEvent(Event event) {
		if (event instanceof ServerTickEvent) {
			player.setPosition(posX, posY + player.yOffset, posZ);
			return true;
		}
		if (event instanceof ClientTickEvent) {
			if (!Minecraft.getMinecraft().isGamePaused())
				player.setPosition(posX, posY + player.yOffset, posZ);
			return true;
		}
		return false;
	}
	
	@Override
	protected void readBytes(ByteBuf buf) {
		posX = buf.readDouble();
		posY = buf.readDouble();
		posZ = buf.readDouble();
	}

	@Override
	protected void writeBytes(ByteBuf buf) {
		buf.writeDouble(posX);
		buf.writeDouble(posY);
		buf.writeDouble(posZ);
	}

}
