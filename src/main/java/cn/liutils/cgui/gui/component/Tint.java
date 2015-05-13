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
package cn.liutils.cgui.gui.component;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.FrameEvent;
import cn.liutils.cgui.gui.event.FrameEvent.FrameEventHandler;
import cn.liutils.cgui.utils.Color;
import cn.liutils.util.HudUtils;

/**
 * @author WeAthFolD
 */
public class Tint extends Component {
	
	public Color 
		idleColor = new Color(0, 0, 0, 0), 
		hoverColor = new Color(1, 1, 1, 0.4);
	
	public Tint() {
		super("Tint");
		
		addEventHandler(new FrameEventHandler() {
			@Override
			public void handleEvent(Widget w, FrameEvent event) {
				if(event.hovering) hoverColor.bind();
				else idleColor.bind();
				HudUtils.drawModalRect(0, 0, w.transform.width, w.transform.height);
			}
		});
	}
}