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
package cn.liutils.cgui.loader.ui;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;
import cn.liutils.cgui.gui.property.IProperty;

/**
 * @author WeAthFolD
 */
public class EditTarget {
	
	public static final String
		INPUT_TYPE_STRING = "string",
		INPUT_TYPE_DRAG = "drag";
	
	private static Map< Class<?>, EditableType> editTypes = new HashMap();
	static {
		addEditableType(new EditInt());
		addEditableType(new EditFloat());
		addEditableType(new EditDouble());
		addEditableType(new EditStr());
		addEditableType(new EditRL());
	}
	
	final EditableType type;
	final Field field;
	
	public EditTarget(Field f) {
		if((type = getEditableType(f.getType())) == null) {
			throw new RuntimeException(
					"Editable type handling " + f.getType() + " doesnt exist.");
		}
		field = f;
	}
	
	public boolean tryEdit(IProperty p, String etype, Object value) {
		try {
			type.set(field, p, etype, value);
			System.out.println("Setting " + p + " " + value);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public String getValue(IProperty p) {
		try {
			return String.valueOf(field.get(p));
		} catch (Exception e) {
			e.printStackTrace();
			return "<invalid>";
		}
	}
	
	public static void addEditableType(EditableType type) {
		for(Class<?> c : type.getHandledClass())
			editTypes.put(c, type);
	}
	
	public static EditableType getEditableType(Class<?> fieldType) {
		return editTypes.get(fieldType);
	}
	
	public static abstract class EditableType {
		
		abstract Collection<Class<?>> getHandledClass();
		
		void set(Field f, IProperty target, String type, Object obj) {
			if(!(obj instanceof String)) {
				throw new UnsupportedOperationException("Can't use"
						+ "default editable type to set non-string");
				
			}
			try {
				setByStr(f, target, (String) obj);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		abstract void setByStr(Field f, IProperty target, String input) throws Exception;
		
	}
	
	private static class EditInt extends EditableType {

		@Override
		Collection<Class<?>> getHandledClass() {
			return Arrays.asList(new Class[] {Integer.TYPE, Integer.class});
		}

		@Override
		void setByStr(Field f, IProperty target, String input) throws Exception {
			int val = Integer.valueOf(input);
			f.set(target, val);
		}
		
	}
	
	public static class EditFloat extends EditableType {

		@Override
		Collection<Class<?>> getHandledClass() {
			return Arrays.asList(new Class[] {Float.TYPE, Float.class});
		}

		@Override
		void setByStr(Field f, IProperty target, String input) throws Exception {
			float ff = Float.valueOf(input);
			f.set(target, ff);
		}
		
	}
	
	public static class EditDouble extends EditableType {

		@Override
		Collection<Class<?>> getHandledClass() {
			return Arrays.asList(new Class[] {Double.TYPE, Double.class});
		}

		@Override
		void setByStr(Field f, IProperty target, String input) throws Exception {
			double ff = Double.valueOf(input);
			f.set(target, ff);
		}
		
	}
	
	public static class EditStr extends EditableType {

		@Override
		Collection<Class<?>> getHandledClass() {
			return Arrays.asList(new Class[] { String.class });
		}

		@Override
		void setByStr(Field f, IProperty target, String input) throws Exception {
			f.set(target, input);
		}
		
	}
	
	public static class EditRL extends EditableType {

		@Override
		Collection<Class<?>> getHandledClass() {
			return Arrays.asList(new Class[] { ResourceLocation.class });
		}

		@Override
		void setByStr(Field f, IProperty target, String input) throws Exception {
			f.set(target, new ResourceLocation(input));
		}
		
	}
}
