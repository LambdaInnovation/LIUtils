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
package cn.liutils.template.selector;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntitySelectorLiving implements IEntitySelector {

	@Override
	public boolean isEntityApplicable(Entity entity) {
		boolean b = IEntitySelector.selectAnything.isEntityApplicable(entity)
				&& entity instanceof EntityLivingBase && !entity.isEntityInvulnerable();
		if(entity instanceof EntityPlayer) {
			b &= !((EntityPlayer)entity).capabilities.isCreativeMode;
		}
		return b;
	}

}
