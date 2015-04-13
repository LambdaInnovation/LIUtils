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

import org.lwjgl.opengl.GL11;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventFunc;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent.MouseDownFunc;
import cn.liutils.cgui.gui.property.IProperty;
import cn.liutils.cgui.gui.property.PropBasic;
import cn.liutils.cgui.loader.CGUIEditor;
import cn.liutils.core.LIUtils;
import cn.liutils.util.HudUtils;
import cn.liutils.util.render.Font;
import cn.liutils.util.render.Font.Align;

/**
 * @author WeAthFolD
 *
 */
public class SelectedWidgetBar extends Window {
	
	final Widget target;
	
	static final double HT = 30;

	public SelectedWidgetBar(Widget _target) {
		super(_target.getName() + " properties", false);
		PropBasic pb = propBasic();
		target = _target;
		pb.width = 100;
		pb.x = HT;
		pb.y = 20;
		
		initEvents();
		initWidgets();
	}
	
	private void initEvents() {
		addFunction(new DrawEventFunc() {
			@Override
			public void handleEvent(Widget w, DrawEvent event) {
				
			}
		});
		
		target.propBasic().x = 10;
	}
	
	private void initWidgets() {
		Widget temp = CGUIEditor.instance.createFromTemplate("input_box");
		PropBasic pb = temp.propBasic();
		pb.width = 100;
		pb.height = 12;
		pb.x = 2;
		pb.y = 12;
		addWidget(temp);
		
		addWidget(new PropertySelection());
	}
	
	private class PropertySelection extends Window {
		
		Widget curEditor;
		
		public PropertySelection() {
			super("Properties", false);
			PropBasic pb = propBasic();
			pb.x = 0;
			pb.y = HT;
			
			int i = 0;
			for(IProperty prop : target.getProperties()) {
				addWidget(new PropertyButton(prop, i++));
			}
		}
		
		void setPropertyEditor(Widget pe) {
			if(curEditor != null) {
				curEditor.dispose();
			}
			curEditor = pe;
			pe.propBasic().y = -30;
			addWidget(curEditor);
		}
		
		private class PropertyButton extends Widget {
			
			final IProperty prop;
			
			public PropertyButton(IProperty _prop, int n) {
				final PropBasic pb = propBasic();
				prop = _prop;
				pb.x = 0;
				pb.y = 11 + n * 11;
				pb.width = 100;
				pb.height = 10;
				
				addFunction(new DrawEventFunc() {
					@Override
					public void handleEvent(Widget w, DrawEvent event) {
						Font.font.draw(prop.getName(), pb.width * 0.5, 0, 10, 0xffffff, Align.CENTER);
						GL11.glColor4d(1, 1, 1, event.hovering ? 0.7 : 0.4);
						HudUtils.drawModalRect(0, 0, pb.width, pb.height);
					}
				});
				
				addFunction(new MouseDownFunc() {
					@Override
					public void handleEvent(Widget w, MouseDownEvent event) {
						PropertyEditor pe = CGUIEditor.instance.getPropertyEditor(target, prop);
						
						if(pe != null) {
							setPropertyEditor(pe);
						} else {
							LIUtils.log.error("Property not registered: " + prop.getName());
						}
					}
				});
			}
			
		}
		
	}
	
}
