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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import cn.liutils.cgui.utils.TypeHelper;

/**
 * @author WeAthFolD
 *
 */
public class ReflectUtils {

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface CopyIgnore {}
	
	public static <T> T copy(T object) throws Exception {
		T another = (T) object.getClass().newInstance();
		for(Field f : another.getClass().getFields()) {
			if(!f.isAnnotationPresent(CopyIgnore.class) && TypeHelper.isTypeSupported(f.getType())) {
				f.set(another, f.get(object));
			}
		}
		return another;
	}
	
	public static NBTTagCompound toNBT(Object obj) throws Exception {
		NBTTagCompound ret = new NBTTagCompound();
		for(Field f : obj.getClass().getFields()) {
			if(!f.isAnnotationPresent(CopyIgnore.class)) {
				Class type = f.getType();
				String name = f.getName();
				Object val = f.get(obj);
				if(val == null) continue;
				
				if(type == Integer.class || type == Integer.TYPE) {
					ret.setInteger(name, (int) val);
				} else if(type == Double.class || type == Double.TYPE) {
					ret.setDouble(name, (double) val);
				} else if(type == Boolean.class || type == Boolean.TYPE) {
					ret.setBoolean(name, (boolean) val);
				} else if(type == Float.class || type == Float.TYPE) {
					ret.setFloat(name, (float) val);
				} else if(type == Short.class || type == Short.TYPE) {
					ret.setShort(name, (short) val);
				} else if(type == Byte.class || type == Byte.TYPE) {
					ret.setByte(name, (byte) val);
				} else if(type == String.class) {
					ret.setString(name, (String) val);
				}
			}
		}
		return ret;
	}
	
	public static void fromNBT(Object obj, NBTTagCompound tag) throws Exception {
		for(Field f : obj.getClass().getFields()) {
			if(!f.isAnnotationPresent(CopyIgnore.class)) {
				Class type = f.getType();
				String name = f.getName();
				NBTBase t = tag.getTag(name);
				if(t == null) continue;
				
				if(type == Integer.class || type == Integer.TYPE) {
					f.set(obj, tag.getInteger(name));
				} else if(type == Double.class || type == Double.TYPE) {
					f.set(obj, tag.getDouble(name));
				} else if(type == Boolean.class || type == Boolean.TYPE) {
					f.set(obj, tag.getBoolean(name));
				} else if(type == Float.class || type == Float.TYPE) {
					f.set(obj, tag.getFloat(name));
				} else if(type == Short.class || type == Short.TYPE) {
					f.set(obj, tag.getShort(name));
				} else if(type == Byte.class || type == Byte.TYPE) {
					f.set(obj, tag.getByte(name));
				} else if(type == String.class) {
					f.set(obj, tag.getString(name));
				}
			}
		}
	}
}
