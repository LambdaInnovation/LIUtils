/**
 * 
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
	
	public abstract void keyChanged(InfWeapon info, int keyid, boolean down);
	public abstract void keyTick(InfWeapon info, int keyid);
	
	public void enterState(InfWeapon info) {}
	public void leaveState(InfWeapon info) {}

}
