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
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventFunc;
import cn.liutils.cgui.gui.property.IProperty;
import cn.liutils.cgui.gui.property.PropBasic;
import cn.liutils.cgui.loader.Editable;
import cn.liutils.core.LIUtils;
import cn.liutils.util.render.Font;

/**
 * Editor for a single property type. Currently is generated each time it is queried.
 * @author WeAthFolD
 */
public class PropertyEditor extends Window {
	
	static Map<Class, Class<? extends ElementEditor>> defaultEditors = new HashMap();
	static {
		Class[] arr = { Integer.TYPE, Integer.class, String.class, 
			Double.TYPE, Double.class, Float.TYPE, Float.class, ResourceLocation.class
		};
		for(Class c : arr) {
			defaultEditors.put(c, ElementEditor.InputBox.class);
		}
	}
	
	Widget widget;
	IProperty target;
	
	public PropertyEditor(Widget _widget, IProperty _target) {
		super("Property: " + _target.getName(), true);
		widget = _widget;
		target = _target;
		generate(_target);
		PropBasic basic = propBasic();
		basic.x = 100;
		basic.y = 20;
		basic.width = 150;
	}
	
	private void generate(IProperty target) {
		try {
			double y = 12;
			
			for(final Field f : target.getClass().getFields()) {
				final Editable anno = f.getAnnotation(Editable.class);
				if(anno != null) {
					//Generation~
					ElementEditor ee = getElementEditor(anno, f);
					if(ee == null) {
						LIUtils.log.error("Can't find element editor for type " + f.getType());
						continue;
					}
					
					Widget drawer = new Widget();
					drawer.propBasic().x = 2;
					drawer.propBasic().y = y;
					drawer.addFunction(new DrawEventFunc() {
						final String name = anno.value().equals("") ? f.getName() : anno.value();
						@Override
						public void handleEvent(Widget w, DrawEvent event) {
							Font.font.draw(name, 0, 0, 9, 0xffffff);
						}
					});
					addWidget(drawer);
					
					/**
					 * Inject instance
					 */
					ee.editor = this;
					ee.propBasic().y = y + 10;
					System.out.println("TARGET:" + ee.editor.target);
					addWidget(ee);
					y += 20;
				}
			}
			
			propBasic().height = y + 5;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private ElementEditor getElementEditor(Editable anno, Field f) {
		if(anno.elementEditor() == ElementEditor.Default.class) {
			try {
				Class<? extends ElementEditor> cl = defaultEditors.get(f.getType());
				if(cl == null) return null;
				return cl.getConstructor(Field.class).newInstance(f);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			return anno.elementEditor().getConstructor(Field.class).newInstance(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
