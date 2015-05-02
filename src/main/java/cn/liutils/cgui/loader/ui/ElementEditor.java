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
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.component.Component;
import cn.liutils.cgui.gui.component.TextBox;
import cn.liutils.cgui.gui.event.ChangeContentEvent;
import cn.liutils.cgui.gui.event.ChangeContentEvent.ChangeContentHandler;
import cn.liutils.cgui.gui.event.ConfirmInputEvent;
import cn.liutils.cgui.gui.event.ConfirmInputEvent.ConfirmInputHandler;
import cn.liutils.cgui.gui.event.FrameEvent;
import cn.liutils.cgui.gui.event.FrameEvent.FrameEventHandler;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent.MouseDownHandler;
import cn.liutils.cgui.utils.Color;
import cn.liutils.cgui.utils.TypeHelper;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;
import cn.liutils.util.render.Font;

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
		return TypeHelper.edit(targetField, getEditInstance(), value);
	}
	
	Object getEditInstance() {
		return getTargetComponent();
	}
	
	ComponentEditor getEditor() {
		return editor;
	}
	
	Component getTargetComponent() {
		return getEditor().target;
	}
	
	void updateTargetWidget() {
		getEditor().widget.dirty = true;
	}
	
	/**
	 * Default element editor. This is a special one and has hardcoded delegation.
	 */
	public static final class Default extends ElementEditor { 
		
		public Default(Field f) {
			super(f);
		}
		
	}
	
	public static final class CheckBox extends ElementEditor {
		
		static final ResourceLocation
			OUTLINE = GuiEdit.tex("check_back"),
			CHECK = GuiEdit.tex("check");
		
		boolean state;
		
		public CheckBox(Field f) {
			super(f);
		}
		
		@Override
		public void onAdded() {
			state = (Boolean) TypeHelper.get(targetField, getEditInstance());
			
			regEventHandler(new MouseDownHandler() {
				@Override
				public void handleEvent(Widget w, MouseDownEvent event) {
					state = !state;
					
					TypeHelper.set(targetField, getEditInstance(), state);
				}
			});
			
			regEventHandler(new FrameEventHandler() {
				boolean firstLoad = true;
				
				@Override
				public void handleEvent(Widget w, FrameEvent event) {
					if(firstLoad) {
						firstLoad = false;
						transform.setSize(10, 10);
						w.dirty = true;
					}
					GL11.glColor4d(1, 1, 1, .8);
					RenderUtils.loadTexture(OUTLINE);
					HudUtils.drawRect(0, 0, 10, 10);
					
					if(state) {
						RenderUtils.loadTexture(CHECK);
						HudUtils.drawRect(0, 0, w.transform.width, w.transform.height);
					}
				}
			});
			
			transform.x = 80;
			transform.y -= 10;
			transform.setSize(0, 0);
		}
		
	}
	
	public static class ColorBox extends ElementEditor {

		public ColorBox(Field f) {
			super(f);
		}
		
		@Override
		public void onAdded() {
			String[] arr = new String[] { "r", "g", "b", "a" };
			double x = 5;
			for(final String s : arr) {
				Widget drawer = new Widget();
				drawer.regEventHandler(new FrameEventHandler() {
					@Override
					public void handleEvent(Widget w, FrameEvent event) {
						Font.font.draw(s, 0, 2, 8, 0xffffff);
					}
				});
				drawer.transform.x = x;
				addWidget(drawer);
				
				try {
					ColorEditor ce = new ColorEditor(s);
					ce.transform.x = x + 5;
					ce.transform.width = 20;
					ce.transform.height = 10;
					addWidget(ce);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				x += 30;
			}
			
			transform.setSize(10, 12);
		}
		
 		class ColorEditor extends InputBox {
			public ColorEditor(String sub) throws Exception {
				super(Color.class.getField(sub));
			}
			
			@Override
			public ComponentEditor getEditor() {
				return ColorBox.this.editor;
			}
			
			@Override Object getEditInstance() {
				try {
					return ColorBox.this.targetField.get(ColorBox.this.getTargetComponent());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
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
			
			addComponent(new TextBox().setSize(9));
			regEventHandler(new FrameEventHandler() {
				@Override
				public void handleEvent(Widget w, FrameEvent event) {
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
			TextBox.get(this).content = TypeHelper.repr(targetField, getEditInstance());
		}
		
	}
}
