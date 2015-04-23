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
import cn.liutils.cgui.gui.event.FrameEvent;
import cn.liutils.cgui.gui.event.FrameEvent.FrameEventHandler;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent.MouseDownHandler;
import cn.liutils.cgui.loader.ui.event.AddTargetEvent;
import cn.liutils.cgui.loader.ui.event.AddTargetEvent.AddTargetHandler;
import cn.liutils.util.HudUtils;
import cn.liutils.util.render.Font;
import cn.liutils.util.render.Font.Align;


/**
 * @author WeAthFolD
 *
 */
public class Hierarchy extends Window {
	
	Widget hList;

	public Hierarchy(GuiEdit _guiEdit) {
		super(_guiEdit, "Hierarchy", true, new double[] { 150, 80 });
		transform.width = 100;
		transform.height = 120;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		regEventHandler(new FrameEventHandler() {
			@Override
			public void handleEvent(Widget w, FrameEvent event) {
				drawSplitLine(30);
			}
		});
		
		buildHierarchy();
		addButtons();
		getGui().regEventHandler(new AddTargetHandler() {
			@Override
			public void handleEvent(Widget w, AddTargetEvent event) {
				buildHierarchy();
			}
		});
	}
	
	Widget getAccessTarget() {
		return guiEdit.toEdit.getFocus();
	}
	
	private void addButtons() {
		Widget tmp;
		
		tmp = setupButton(0, "arrow_left", "De-child");
		tmp.regEventHandler(new MouseDownHandler() {
			@Override
			public void handleEvent(Widget w, MouseDownEvent event) {
				if(getAccessTarget() != null) {
					getAccessTarget().moveLeft();
					buildHierarchy();
				}
			}
		});
		tmp = setupButton(1, "arrow_right", "Become child");
		tmp.regEventHandler(new MouseDownHandler() {
			@Override
			public void handleEvent(Widget w, MouseDownEvent event) {
				if(getAccessTarget() != null) {
					getAccessTarget().moveRight();
					buildHierarchy();
				}
			}
		});
		tmp = setupButton(2, "arrow_up", "Move up");
		tmp.regEventHandler(new MouseDownHandler() {
			@Override
			public void handleEvent(Widget w, MouseDownEvent event) {
				if(getAccessTarget() != null) {
					getAccessTarget().moveUp();
					buildHierarchy();
				}
			}
		});
		tmp = setupButton(3, "arrow_down", "Move down");
		tmp.regEventHandler(new MouseDownHandler() {
			@Override
			public void handleEvent(Widget w, MouseDownEvent event) {
				if(getAccessTarget() != null) {
					getAccessTarget().moveDown();
					buildHierarchy();
				}
			}
		});
		tmp = setupButton(4, "rename", "Rename");

		tmp = setupButton(5, "remove", "Remove");
		tmp.regEventHandler(new MouseDownHandler() {
			@Override
			public void handleEvent(Widget w, MouseDownEvent event) {
				if(getAccessTarget() != null) {
					getAccessTarget().dispose();
					buildHierarchy();
				}
			}
		});
		
		tmp = setupButton(6, "duplicate", "Duplicate");
		tmp.regEventHandler(new MouseDownHandler() {
			@Override
			public void handleEvent(Widget w, MouseDownEvent event) {
				if(getAccessTarget() != null) {
					getAccessTarget().getAbstractParent().addWidget(getAccessTarget().copy());
					buildHierarchy();
				}
			}
		});
	}
	
	private void buildHierarchy() {
		if(hList != null) 
			hList.dispose();
		
		hList = new Widget();
		hList.transform.x = 2;
		hList.transform.y = 32;
		hList.transform.width = 96;
		hList.transform.height = 86;
		
		ElementList el = new ElementList();
		for(Widget w : guiEdit.toEdit.getDrawList()) {
			if(!w.disposed)
				hierarchyAdd(el, w);
		}
		hList.addComponent(el);
		
		addWidget(hList);
	}
	
	private void hierarchyAdd(ElementList list, Widget w) {
		list.addWidget(new SingleWidget(w));
		for(Widget ww : w.getDrawList()) {
			if(!ww.disposed)
				hierarchyAdd(list, ww);
		}
	}
	
	private Widget setupButton(int count, final String name, final String desc) {
		final double size = 10;
		Widget w = new Widget();
		final Tint tint = new Tint();
		w.addComponent(tint);
		w.addComponent(new DrawTexture().setTex(GuiEdit.tex(name)));
		w.transform.setSize(size, size).setPos(2 + count * 12, 11);
		w.regEventHandler(new FrameEventHandler() {
			@Override
			public void handleEvent(Widget w, FrameEvent event) {
				Widget target = getAccessTarget();
				tint.enabled = target != null && target.visible;
				
				if(event.hovering) {
					Font.font.draw(desc, size / 2, size, 10, 0xffffff, Align.CENTER);
				}
			}
		});
		
		addWidget(w);
		return w;
	}
	
	private class SingleWidget extends Widget {
		int hierLevel;
		Widget target;
		
		boolean on = true;
		
		final ResourceLocation 
			vis_on = GuiEdit.tex("vis_on"), 
			vis_off = GuiEdit.tex("vis_off");
		
		public SingleWidget(Widget w) {
			transform.width = 96;
			transform.height = 12;
			
			hierLevel = w.getHierarchyLevel();
			target = w;
			regEventHandler(new FrameEventHandler() {
				@Override
				public void handleEvent(Widget w, FrameEvent event) {
					double r = 1, g = 1, b = 1;
					double brightness = .3;
					if(target.isFocused()) {
						brightness *= 1.6;
						r = b = .6;
					}
					GL11.glColor4d(r, g, b, brightness);
					HudUtils.drawModalRect(0, 0, w.transform.width, w.transform.height);
					
					String name = target.getName();
					Font.font.draw(name == null ? "<error>" : name, 14 + hierLevel * 6, 1, 10, 0xffffff);
				}
			});
			
			regEventHandler(new MouseDownHandler() {
				@Override
				public void handleEvent(Widget w, MouseDownEvent event) {
					guiEdit.toEdit.gainFocus(target);
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
