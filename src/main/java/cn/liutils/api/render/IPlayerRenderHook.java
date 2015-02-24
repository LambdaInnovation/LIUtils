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
package cn.liutils.api.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Automatically attached on all the player and renders whenever player model is rendered.
 * Used for rendering custom stuffs.(Typically in 3rd person)
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
public interface IPlayerRenderHook {
	
	/**
	 * Get whether the render process is activated or not
	 * @param player
	 * @param world
	 * @return
	 */
	boolean isActivated(EntityPlayer player, World world);
	
	void renderHead(EntityPlayer player, World world);
	
	void renderBody(EntityPlayer player, World world);
}
