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
package cn.weaponry.template.classic.action;

import net.minecraft.item.ItemStack;
import cn.weaponry.api.action.impl.ActionShoot;
import cn.weaponry.api.info.InfWeapon;
import cn.weaponry.template.classic.WeaponClassic;

/**
 * @author WeathFolD
 *
 */
public class ClassicShoot extends ActionShoot {

	public ClassicShoot(float _dmg) {
		super(_dmg);
	}

	@Override
	protected boolean consumeAmmo(InfWeapon inf) {
		ItemStack stack = inf.getCurStack();
		WeaponClassic wc = (WeaponClassic) stack.getItem();
		
		int cur = wc.getAmmo(stack);
		if(cur > 0) {
			wc.setAmmo(stack, cur - 1);
			return true;
		}
		return false;
	}

}
