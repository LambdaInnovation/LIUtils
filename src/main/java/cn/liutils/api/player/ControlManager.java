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
package cn.liutils.api.player;

import cn.liutils.api.player.state.StateBase.StateType;
import net.minecraft.entity.player.EntityPlayer;

/**
 * 
 * @author Violet
 *
 */
public class ControlManager {

	/**
	 * Set state
	 * @param ticks Ticks to cancel the state and negative for infinite
	 */
	public static void stateSet(EntityPlayer player, StateType type, int ticks) {
		ControlData.get(player).stateSet(type, ticks);
	}
	
	/**
	 * Modify state
	 * @param ticks Positive for increase while negative for decrease
	 */
	public static void stateModify(EntityPlayer player, StateType type, int ticks) {
		ControlData.get(player).stateModify(type, ticks);
	}

	/**
	 * Cancel some state
	 */
	public static void stateCancel(EntityPlayer player, StateType type) {
		ControlData.get(player).stateCancel(type);
	}
	
	private ControlManager() {	
	}
	
}
