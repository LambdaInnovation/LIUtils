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
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventHandler;
import cn.liutils.cgui.gui.fnct.Component;
import cn.liutils.core.LIUtils;
import cn.liutils.util.render.Font;

/**
 * Editor for a single property type. Currently is generated each time it is queried.
 * @author WeAthFolD
 */
public class ComponentEditor extends Window {
	
	static Map<Class, Class<? extends ElementEditor>> editors = new HashMap();
	static {
		Class[] arr = { Integer.TYPE, Integer.class, String.class, 
			Double.TYPE, Double.class, Float.TYPE, Float.class, ResourceLocation.class
		};
		for(Class c : arr) {
			editors.put(c, ElementEditor.InputBox.class);
		}
	}
	
	Widget widget;
	Component target;
	
	public ComponentEditor(Widget _widget, Component _target) {
		super("Property: " + _target.name, true);
		widget = _widget;
		target = _target;
		generate();
		transform.x = 100;
		transform.y = 20;
		transform.width = 150;
	}
	
	private void generate() {
		try {
			double y = 12;
			
			for(final Field f : target.getPropertyList()) {
				//Generation~
				ElementEditor ee = getElementEditor(f);
				if(ee == null) {
					LIUtils.log.error("Can't find element editor for type " + f.getType());
					continue;
				}
				
				Widget drawer = new Widget();
				drawer.transform.x = 2;
				drawer.transform.y = y;
				drawer.regEventHandler(new DrawEventHandler() {
					final String name = f.getName();
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
				ee.transform.y = y + 10;
				System.out.println("TARGET:" + ee.editor.target);
				addWidget(ee);
				y += 20;
			}
			
			transform.height = y + 5;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private ElementEditor getElementEditor(Field f) {
		try {
			return editors.get(f.getType()).getConstructor(Field.class).newInstance(f);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
	
}
