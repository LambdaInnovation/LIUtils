/**
 * 
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
