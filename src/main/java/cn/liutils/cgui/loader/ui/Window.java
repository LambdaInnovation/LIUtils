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
import cn.liutils.cgui.gui.event.DragEvent;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventHandler;
import cn.liutils.cgui.gui.property.PropWidget;
import cn.liutils.util.HudUtils;
import cn.liutils.util.render.Font;

/**
 * @author WeAthFolD
 */
public class Window extends Widget {
	
	public Window(final String name) {
		this.regEventHandler(new GuiEventHandler<DrawEvent>(this) {
			@Override
			public void handleEvent(DrawEvent event) {
				PropWidget p = propWidget();
				final double bar_ht = 10;
				
				GuiEdit.bindColor(2);
				HudUtils.drawModalRect(0, 0, p.width, bar_ht);
				
				GuiEdit.bindColor(1);
				HudUtils.drawModalRect(0, bar_ht, p.width, p.height - bar_ht);
			}

			@Override
			public Class<? extends GuiEvent> getEventClass() {
				return DrawEvent.class;
			}
		});
		
		this.regEventHandler(new GuiEventHandler<DragEvent>(this) {
			@Override
			public void handleEvent(DragEvent event) {
				getGui().updateDragWidget();
			}
			@Override
			public Class<? extends GuiEvent> getEventClass() {
				return DragEvent.class;
			}
		});
		
		regEventHandler(new GuiEventHandler<DrawEvent>(this) {
			@Override
			public void handleEvent(DrawEvent event) {
				Font.font.draw(name, 10, 0, 10, 0xffffff);
			}

			@Override
			public Class<? extends GuiEvent> getEventClass() {
				return DrawEvent.class;
			}
		});
	}
}
