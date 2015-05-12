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
package cn.liutils.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Elegant and fast way to register specific kinds of items/blocks.
 * (Although less elegant than AnnoReg)
 * @author WeathFolD
 */
public class RegUtils {

	/**
	 * Register a item instance of itemClass which have an empty constructor with key id and return its instance.
	 */
	public static <T extends Item> T reg(Class<? extends T> itemClass, String id) {
		try {
			Item it = itemClass.getConstructor().newInstance();
			GameRegistry.registerItem(it, id);
			return (T) it; //Hah, casting done here
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Register n item instances of itemClass which should have a single-int-par Ctor with key id.
	 * We do it by creating an array of size n and initialized the array each by Ctor(0),...,Ctor(n-1)
	 * The registering result is returned.
	 */
	public static <T extends Item> T[] reg(Class<? extends T> itemClass, int n, String id) {
		T[] res = (T[]) Array.newInstance(itemClass, n); //这个也是醉了
		try {
			for(int i = 0; i < n; ++i) {
				Item it = itemClass.getConstructor(Integer.TYPE).newInstance(i);
				GameRegistry.registerItem(it, id + i);
				res[i] = (T) it;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static Field getObfField(Class cl, String normName, String obfName) {
		Field f = null;
		try {
			f = cl.getDeclaredField(normName);
			if(f == null)
				f = cl.getDeclaredField(obfName);
			f.setAccessible(true);
			return f;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static <T> T getFieldInstance(Class cl, Object instance, String normName, String obfName) {
		Field f = getObfField(cl, normName, obfName);
		try {
			return (T) f.get(instance);
		} catch(Exception e) {
			return null;
		}
	}

}
