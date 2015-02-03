/**
 * 
 */
package cn.weaponry.api.state;

import cn.weaponry.api.info.InfWeapon;

/**
 * @author WeathFolD
 *
 */
public abstract class WeaponState {

	public WeaponState() {}
	
	public abstract void keyChanged(InfWeapon info, int keyid, boolean down);
	public abstract void keyTick(InfWeapon info, int keyid);
	
	public void leaveState(InfWeapon info) {}

}
