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
package cn.liutils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Client-Side judgement helper and other stuffs.
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
public class ClientUtils {
	
	/**
	 * Judge if the player is playing the client game and isn't opening any GUI.
	 * @return
	 */
	public static boolean isPlayerInGame() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		return player != null && Minecraft.getMinecraft().currentScreen == null;
	}
	
	/**
	 * Quick alias for playing sound
	 * @param src
	 * @param pitch
	 */
	public static void playSound(ResourceLocation src, float pitch) {
		Minecraft.getMinecraft().getSoundHandler().playSound(
			PositionedSoundRecord.func_147674_a(src, pitch));
	}

}
