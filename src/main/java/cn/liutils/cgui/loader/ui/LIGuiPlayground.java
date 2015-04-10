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

import cn.liutils.cgui.gui.LIGui;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventHandler;
import cn.liutils.cgui.gui.fnct.Draggable;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;
import cn.liutils.util.render.Font;

/**
 * @author WeAthFolD
 *
 */
public class LIGuiPlayground extends LIGui {
	
	@Override
	public void draw(double mx, double my) {
		super.draw(mx, my);
		
		String focusName = this.getFocus() == null ? "<Background>" : this.getWidgetName(getFocus());
		Font.font.draw("Current selection: " + focusName, 5, 5, 10, 0x89b1e7);
	}
	
	@Override
	public boolean addWidget(String name, Widget w) {
		boolean ret = super.addWidget(name, w);
		if(ret) {
			w.regEventHandler(new DrawEventHandler() {
				@Override
				public void handleEvent(Widget w, DrawEvent event) {
					if(getFocus() == w) {
						RenderUtils.bindColor(112, 223, 122, 200);
						HudUtils.drawRectOutline(0, 0, w.propBasic().width, w.propBasic().height, 3);
					} else {
						HudUtils.drawRectOutline(0, 0, w.propBasic().width, w.propBasic().height, 1);
					}
				}
			});
			w.regEventHandler(new Draggable());
			w.needFocus = true;
		}
		return ret;
	}
}
