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

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.component.Component;
import cn.liutils.cgui.gui.component.DrawTexture;
import cn.liutils.cgui.gui.component.Tint;
import cn.liutils.cgui.gui.component.Transform;
import cn.liutils.cgui.gui.event.FrameEvent;
import cn.liutils.cgui.gui.event.FrameEvent.FrameEventHandler;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent.MouseDownHandler;
import cn.liutils.util.render.Font;
import cn.liutils.util.render.Font.Align;

/**
 * @author WeAthFolD
 *
 */
public class SelectedWidgetBar extends Window {
	
	final Widget target;
	
	static final double HT = 10;

	public SelectedWidgetBar(GuiEdit guiEdit, Widget _target) {
		super(guiEdit, "Properties", false, new double[] { 220, 20 });
		target = _target;
		transform.width = 100;
		transform.height = HT;
		transform.x = 200;
		transform.y = 50;
		
		initWidgets();
	}
	
	private void initWidgets() {
		addWidget(new ComponentSelection());
	}
	
	private class ComponentSelection extends Window {
		
		Widget curEditor;
		
		public ComponentSelection() {
			super(SelectedWidgetBar.this.guiEdit, "Components", false);
			transform.x = 0;
			transform.y = HT;
			transform.width = 100;
			transform.height = 15 + target.getComponentList().size() * 11;
			
			int i = 0;
			for(Component prop : target.getComponentList()) {
				if(prop.canEdit)
					addWidget(new ComponentButton(prop, i++));
			}
			
			Widget w = new Widget();
			w.addComponent(new DrawTexture().setTex(GuiEdit.tex("toolbar/add")));
			Transform t2 = w.transform;
			t2.x = transform.width - 12;
			t2.y = 0;
			t2.width = t2.height = 10;
			addWidget(w);
		}
		
		void setPropertyEditor(Widget ce) {
			if(curEditor != null) {
				curEditor.dispose();
			}
			curEditor = ce;
			ce.transform.y = -10;
			addWidget(curEditor);
		}
		
		private class ComponentButton extends Widget {
			
			final Component c;
			
			public ComponentButton(Component _c, int n) {
				c = _c;
				
				transform.x = 0;
				transform.y = 11 + n * 11;
				transform.width = 100;
				transform.height = 10;
				
				addComponent(new Tint());
				
				regEventHandler(new MouseDownHandler() {
					@Override
					public void handleEvent(Widget w, MouseDownEvent event) {
						setPropertyEditor(new ComponentEditor(guiEdit, target, c));
					}
				});
				
				regEventHandler(new FrameEventHandler() {
					@Override
					public void handleEvent(Widget w, FrameEvent event) {
						Font.font.draw(c.name, 50, 2, 7, 0xffffff, Align.CENTER);
					}
				});
			}
			
		}
		
	}
	
}
