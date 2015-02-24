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
package cn.weaponry.api.state;

import cn.weaponry.api.info.InfWeapon;

/**
 * Apply of finite state machine model on weapon states. This state handles weapon control events, and transits state
 * by calling {@link InfWeapon#transitState(WeaponState)}.
 * @author WeathFolD
 */
public abstract class WeaponState {

	public WeaponState() {}
	
	public void keyChanged(InfWeapon info, int keyid, boolean down) {}
	public void keyTick(InfWeapon info, int keyid) {}
	public void update(InfWeapon info) {}
	
	public void enterState(InfWeapon info) {}
	public void leaveState(InfWeapon info) {}
	
	public boolean doesCancelSwing(InfWeapon info) {
		return true;
	}

}
