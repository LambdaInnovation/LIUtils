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

import java.util.Map.Entry;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventFunc;
import cn.liutils.cgui.gui.event.LostFocusEvent;
import cn.liutils.cgui.gui.event.LostFocusEvent.LostFocusFunc;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent.MouseDownFunc;
import cn.liutils.cgui.gui.fnct.SimpleDrawer;
import cn.liutils.cgui.gui.property.PropBasic;
import cn.liutils.cgui.gui.property.PropColor;
import cn.liutils.cgui.gui.property.PropTexture;
import cn.liutils.cgui.loader.CGUIEditor;
import cn.liutils.util.HudUtils;
import cn.liutils.util.render.Font;
import cn.liutils.util.render.Font.Align;

/**
 * @author WeAthFolD
 *
 */
public class Toolbar extends Window {
	
	boolean isLocked = false;
	
	public Toolbar() {
		super("Toolbar", false);
		propBasic().setPos(10, 10).setSize(200, 30);
		
		addWidget(new Button(0, "open", "Open CGUI File"));
		addWidget(new Button(1, "save", "Save CGUI File"));
		addWidget(new Button(2, "add", "Add Widget") {
			@Override public void triggerEvent() {
				isLocked = true;
				Widget tl = new TemplateList();
				Toolbar.this.addWidget(tl);
				getGui().gainFocus(tl);
			}
		});
		addWidget(new Button(3, "remove", "Remove Widget"));
		
	}
	
	class Button extends Widget {
		public Button(int i, final String tn, final String name) {
			propBasic().setSize(18, 18).setPos(5 + i * 20, 10);
			this.addProperty(new PropTexture().init(new ResourceLocation("liutils:textures/cgui/toolbar/" + tn + ".png")));
			this.addProperty(new PropColor().setColor3i(127, 190, 255));
			addFunction(new SimpleDrawer() {
				@Override
				public void handleEvent(Widget widget, DrawEvent event) {
					super.handleEvent(widget, event);
					if(event.hovering && !isLocked) {
						GL11.glColor4d(1, 1, 1, .5);
						HudUtils.drawModalRect(0, 0, propBasic().width, propBasic().height);
						
						Font.font.draw(name, 9, 19, 10, 0x9fceff, Align.CENTER);
					}
				}
			});
			addFunction(new MouseDownFunc() {
				@Override
				public void handleEvent(Widget w, MouseDownEvent event) {
					if(!isLocked)
						triggerEvent();
				}
			});
		}
		
		public void triggerEvent() {}
	}
	
	class TemplateList extends Widget {
		public TemplateList() {
			PropBasic pw = this.propBasic();
			pw.x = 30;
			pw.y = 30;
			
			final double width = 50, height = 12; //For a single subelement
			
			int i = 0;
			for(final Entry<String, Widget> e : CGUIEditor.instance.getTemplates()) {
				Widget one = new Widget();
				final String name = e.getKey();
				one.addFunction(new DrawEventFunc() {
					@Override
					public void handleEvent(Widget w, DrawEvent event) {
						GL11.glColor4d(.3, .3, .3, event.hovering ? 0.8 : 0.5);
						HudUtils.drawModalRect(0, 0, w.propBasic().width, w.propBasic().height);
						
						Font.font.draw(name, 25, 1.5, 10, 0x98b8e2, Align.CENTER);
					}
				});
				one.addFunction(new MouseDownFunc() {
					@Override
					public void handleEvent(Widget w, MouseDownEvent event) {
						isLocked = false;
						TemplateList.this.dispose();
						gui().toEdit.addWidget(CGUIEditor.instance.createFromTemplate(name));
					}
				});
				PropBasic opw = one.propBasic();
				opw.x = 0;
				opw.y = (i++) * height;
				opw.width = width;
				opw.height = height;
				addWidget(one);
			}
			
			addFunction(new LostFocusFunc() {
				@Override
				public void handleEvent(Widget w, LostFocusEvent event) {
					isLocked = false;
					w.dispose();
				}
			});
		}
	}
	
}
