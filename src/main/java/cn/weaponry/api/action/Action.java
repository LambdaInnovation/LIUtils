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
