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
package cn.liutils.cgui.gui.event.global;

import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventHandler;

/**
 * @author WeAthFolD
 */
public class GlobalMouseEvent implements GuiEvent {
	
	public final double x, y;
	public final int key;
	
	public GlobalMouseEvent(double _x, double _y, int _key) {
		x = _x;
		y = _y;
		key = _key;
	}
	
	public static abstract class GlobalMouseHandler extends GuiEventHandler<GlobalMouseEvent> {

		public GlobalMouseHandler() {
			super(GlobalMouseEvent.class);
		}
		
	}
	
}
