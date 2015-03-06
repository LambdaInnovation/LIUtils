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
package cn.liutils.util;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Simple wrapping of EntityPlayer NBT. Used for tick counting&checking.
 * @author WeAthFolD
 */
public class PlayerTicker {

	public static void updateTicker(EntityPlayer player, String channel) {
		player.getEntityData().setInteger("tick_" + channel, player.ticksExisted);
	}
	
	public static int getDeltaTick(EntityPlayer player, String channel) {
		int dt = player.getEntityData().getInteger("tick_" + channel) - player.ticksExisted;
		if(dt < 0) dt = 0;
		return dt;
	}
	
	public static int getDeltaTick(EntityPlayer player, String channel, int DEFAULT_VALUE) {
		int dt = player.getEntityData().getInteger("tick_" + channel) - player.ticksExisted;
		if(dt < 0) dt = DEFAULT_VALUE;
		return dt;
	}

}
