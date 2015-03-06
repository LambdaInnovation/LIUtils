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
package cn.liutils.api.potion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

/**
 * This class defines the behavior of a potion that can only be applied to a player.
 * @author acaly
 *
 */
public abstract class CustomPlayerPotion extends Potion {
	
	public CustomPlayerPotion(boolean isBad, int color) {
		super(PotionRegistry.getFreeId(), isBad, color);
	}

	public abstract void onStart(EntityPlayer player);
	public abstract void onFinish(EntityPlayer player);
	public abstract void onTick(EntityPlayer player);
}
