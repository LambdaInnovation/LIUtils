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

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.ChangeContentEvent;
import cn.liutils.cgui.gui.event.ChangeContentEvent.ChangeContentHandler;
import cn.liutils.cgui.gui.event.ConfirmInputEvent;
import cn.liutils.cgui.gui.event.ConfirmInputEvent.ConfirmInputHandler;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventHandler;
import cn.liutils.cgui.gui.fnct.Component;
import cn.liutils.cgui.gui.fnct.TextBox;
import cn.liutils.cgui.utils.TypeHelper;
import cn.liutils.util.HudUtils;

/**
 * @author WeAthFolD
 *
 */
public abstract class ElementEditor extends Widget {
	
	ComponentEditor editor;
	final Field targetField;
	
	public ElementEditor(Field f) {
		targetField = f;
	}
	
	boolean tryEdit(String value) {
		System.out.println("TryEdit " + getTargetComponent());
		return TypeHelper.edit(targetField, getTargetComponent(), value);
	}
	
	Component getTargetComponent() {
		return editor.target;
	}
	
	void updateTargetWidget() {
		editor.widget.dirty = true;
	}
	
	/**
	 * Default element editor. This is a special one and has hardcoded delegation.
	 */
	public static final class Default extends ElementEditor { 
		
		public Default(Field f) {
			super(f);
		}
		
	}
	
	/**
	 * Input box pref element editor. Default pref for most stuffs.
	 */
	public static class InputBox extends ElementEditor {
		
		boolean inputDirty;
		
		long lastErrorTime = -1;
		
		public InputBox(Field f) {
			super(f);
			
			transform.x = 2;
			transform.width = 120;
			transform.height = 10;
			
			addComponent(new TextBox());
			regEventHandler(new DrawEventHandler() {
				@Override
				public void handleEvent(Widget w, DrawEvent event) {
					if(lastErrorTime != -1 && Minecraft.getSystemTime() - lastErrorTime < 1000) {
						GL11.glColor4d(1, 0, 0, Minecraft.getSystemTime() % 500 < 250 ? 0.6 : 0.3);
					} else if(inputDirty) {
						GL11.glColor4d(1, 0.6, 0, 0.3);
					} else {
						GL11.glColor4d(1, 1, 1, 0.3);
					}
					HudUtils.drawModalRect(0, 0, 
						w.transform.width, w.transform.height);
					GL11.glColor4d(1, 1, 1, 1);
				}
			});
			regEventHandler(new ChangeContentHandler() {
				@Override
				public void handleEvent(Widget w, ChangeContentEvent event) {
					inputDirty = true;
				}
			});
			regEventHandler(new ConfirmInputHandler() {
				@Override
				public void handleEvent(Widget w, ConfirmInputEvent event) {
					if(inputDirty) {
						//Try to edit the edit target. if not successful, show error.
						if(!tryEdit(((TextBox)w.getComponent("TextBox")).content)) {
							lastErrorTime = Minecraft.getSystemTime();
						} else {
							updateTargetWidget();
							inputDirty = false;
						}
					}
				}
			});

		}
		
		@Override
		public void onAdded() {
			TextBox.get(this).content = TypeHelper.repr(targetField, getTargetComponent());
		}
		
	}
}
