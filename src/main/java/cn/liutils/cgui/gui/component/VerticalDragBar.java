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
import cn.liutils.cgui.gui.event.DragEvent;
import cn.liutils.cgui.gui.event.DragEvent.DragEventHandler;
import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventHandler;

/**
 * @author WeAthFolD
 *
 */
public class VerticalDragBar extends Component {
	
	public double y0, y1;

	public VerticalDragBar() {
		super("VerticalDragBar");
		
		this.addEventHandler(new DragEventHandler() {

			@Override
			public void handleEvent(Widget w, DragEvent event) {
				w.getGui().moveWidgetToAbsPos(w, w.x, w.getGui().mouseY);
				w.getGui().updateWidget(w);
				
				w.postEvent(new DraggedEvent());
				if(w.transform.y > y1) {
					w.transform.y = y1;
				} else if(w.transform.y < y0) {
					w.transform.y = y0;
				}
				w.dirty = true;
			}
			
		});
	}
	
	public static VerticalDragBar get(Widget w) {
		return w.getComponent("VerticalDragBar");
	}
	
	public double getProgress(Widget w) {
		return (w.transform.y - y0) / (y1 - y0);
	}
	
	public VerticalDragBar setArea(double _y0,  double _y1) {
		y0 = _y0;
		y1 = _y1;
		
		return this;
	}
	
	public static class DraggedEvent implements GuiEvent {}

	public abstract static class DraggedHandler extends GuiEventHandler<DraggedEvent> {

		public DraggedHandler() {
			super(DraggedEvent.class);
		}
		
	}
}
