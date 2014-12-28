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
