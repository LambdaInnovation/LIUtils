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

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.component.DrawTexture;
import cn.liutils.cgui.gui.component.ElementList;
import cn.liutils.cgui.gui.component.Tint;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventHandler;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent.MouseDownHandler;
import cn.liutils.util.HudUtils;
import cn.liutils.util.render.Font;
import cn.liutils.util.render.Font.Align;


/**
 * @author WeAthFolD
 *
 */
public class Hierarchy extends Window {
	
	GuiEdit guiEdit;

	public Hierarchy(GuiEdit _guiEdit) {
		super("Hierarchy", true);
		guiEdit = _guiEdit;
		transform.width = 100;
		transform.height = 120;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		regEventHandler(new DrawEventHandler() {
			@Override
			public void handleEvent(Widget w, DrawEvent event) {
				drawSplitLine(30);
			}
		});
		
		buildHierarchy();
		addButtons();
	}
	
	private void addButtons() {
		Widget tmp;
		
		tmp = setupButton(0, "arrow_left", "De-child");
		tmp = setupButton(1, "arrow_right", "Become child");
		tmp = setupButton(2, "arrow_up", "Move up");
		tmp = setupButton(3, "arrow_down", "Move down");
		tmp = setupButton(4, "rename", "Rename");
	}
	
	private void buildHierarchy() {
		Widget hList = new Widget();
		hList.transform.x = 2;
		hList.transform.y = 32;
		hList.transform.width = 96;
		hList.transform.height = 86;
		
		ElementList el = new ElementList();
		for(Widget w : guiEdit.toEdit.getDrawList()) {
			hierarchyAdd(el, w);
		}
		hList.addComponent(el);
		
		addWidget(hList);
	}
	
	private void hierarchyAdd(ElementList list, Widget w) {
		list.addWidget(new SingleWidget(w));
		for(Widget ww : w.getDrawList()) {
			hierarchyAdd(list, ww);
		}
	}
	
	private Widget setupButton(int count, final String name, final String desc) {
		final double size = 10;
		Widget w = new Widget();
		w.addComponent(new Tint());
		w.addComponent(new DrawTexture().setTex(GuiEdit.tex(name)));
		w.transform.setSize(size, size).setPos(2 + count * 12, 11);
		w.regEventHandler(new DrawEventHandler() {
			@Override
			public void handleEvent(Widget w, DrawEvent event) {
				if(event.hovering) {
					Font.font.draw(desc, size / 2, size, 10, 0xffffff, Align.CENTER);
				}
			}
		});
		
		addWidget(w);
		return w;
	}
	
	private static class SingleWidget extends Widget {
		int hierLevel;
		Widget target;
		
		boolean on = true;
		
		static final ResourceLocation vis_on = GuiEdit.tex("vis_on"), vis_off = GuiEdit.tex("vis_off");
		
		public SingleWidget(Widget w) {
			transform.width = 96;
			transform.height = 12;
			
			hierLevel = w.getHierarchyLevel();
			target = w;
			regEventHandler(new DrawEventHandler() {
				@Override
				public void handleEvent(Widget w, DrawEvent event) {
					double r = 1, g = 1, b = 1;
					double brightness = .3;
					if(target.isFocused()) {
						brightness *= 1.6;
						r = b = .6;
					}
					GL11.glColor4d(r, g, b, brightness);
					HudUtils.drawModalRect(0, 0, w.transform.width, w.transform.height);
					
					Font.font.draw(target.getName(), 14 + hierLevel * 6, 1, 10, 0xffffff);
				}
			});
			
			{
				Widget eye = new Widget();
				eye.transform.setSize(10, 10).setPos(1, 1);
				eye.addComponent(new Tint());
				eye.addComponent(new DrawTexture().setTex(vis_on));
				eye.regEventHandler(new MouseDownHandler() {
					@Override
					public void handleEvent(Widget w, MouseDownEvent event) {
						on = !on;
						target.visible = on;
						DrawTexture.get(w).setTex(on ? vis_on : vis_off);
					}
				});
				addWidget(eye);
			}
		}
	}


}
