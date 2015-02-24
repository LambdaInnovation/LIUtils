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
package cn.liutils.core.player;

import org.lwjgl.input.Mouse;

import net.minecraft.util.MouseHelper;

/**
 * 
 * @author Violet
 *
 */
public class MouseHelperX extends MouseHelper {
	
	private static boolean locked = false;
	
	public static void lock() {
		locked = true;
	}
	
	public static void unlock() {
		locked = false;
	}
	
	@Override
	public void mouseXYChange() {
		super.mouseXYChange();
		if (locked) {
			deltaX = 0;
			deltaY = 0;
		}
	}
}
