/**
 * 
 */
package cn.weaponry.api.action;

import cn.weaponry.api.info.InfWeapon;

/**
 * A single action entity that exists during the holding time of one same item.
 * You can define what the action do when it's added, during its lifetime and when its ended.
 * You can add (possibly more than 1 times simultaneously) an instance of Action into InfWeapon to make it work.
 * @author WeathFolD
 */
public abstract class Action {

	public Action() {}
	
	public void onActionBegin(InfWeapon inf, int life) {}
	
	/**
	 * Called per tick this action is added into a InfWeapon. Return true if you want to end this action(onActionEnd will be called)
	 * @param inf
	 * @param passed ticks since added
	 * @param life the lifetime of this action
	 * @return if you wanna end the action
	 */
	public boolean onActionTick(InfWeapon inf, int passed, int life) {
		return false;
	}
	
	public void onActionEnd(InfWeapon inf, int passed, int life) {}
	
	public String getID() {
		return "_default";
	}

}
