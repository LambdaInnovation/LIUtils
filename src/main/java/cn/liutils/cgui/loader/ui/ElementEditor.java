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
import cn.liutils.cgui.gui.event.ChangeContentEvent.ChangeContentFunc;
import cn.liutils.cgui.gui.event.ConfirmInputEvent;
import cn.liutils.cgui.gui.event.ConfirmInputEvent.ConfirmInputFunc;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventFunc;
import cn.liutils.cgui.gui.fnct.TextBoxInput;
import cn.liutils.cgui.gui.fnct.TextBoxShower;
import cn.liutils.cgui.gui.property.IProperty;
import cn.liutils.cgui.gui.property.PropBasic;
import cn.liutils.cgui.gui.property.PropTextBox;
import cn.liutils.util.HudUtils;

/**
 * @author WeAthFolD
 *
 */
public abstract class ElementEditor extends Widget {
	
	protected final EditTarget target;
	
	PropertyEditor editor;
	
	public ElementEditor(Field f) {
		target = new EditTarget(f);
	}
	
	boolean tryEdit(String type, Object obj) {
		return target.tryEdit(editor.target, type, obj);
	}
	
	IProperty getTargetProperty() {
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
			
			PropBasic pb = propBasic();
			pb.x = 2;
			pb.width = 120;
			pb.height = 10;
			pb.needFocus = true;
			
			addProperty(new PropTextBox());
			addFunction(new DrawEventFunc() {
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
						w.propBasic().width, w.propBasic().height);
					GL11.glColor4d(1, 1, 1, 1);
				}
			});
			addFunction(new TextBoxInput());
			addFunction(new TextBoxShower());
			addFunction(new ChangeContentFunc() {
				@Override
				public void handleEvent(Widget w, ChangeContentEvent event) {
					inputDirty = true;
				}
			});
			addFunction(new ConfirmInputFunc() {
				@Override
				public void handleEvent(Widget w, ConfirmInputEvent event) {
					if(inputDirty) {
						//Try to edit the edit target. if not successful, show error.
						if(!tryEdit("string", ((PropTextBox)w.getProperty("text_box")).content)) {
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
			PropTextBox.get(this).content = target.getValue(getTargetProperty());
		}
		
	}
}
