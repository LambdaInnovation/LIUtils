/**
 * 
 */
package cn.liutils.cgui.gui.event;

import cn.liutils.cgui.gui.fnct.Function;

/**
 * @author WeAthFolD
 */
public class MouseDownEvent implements GuiEvent {
	public final double x, y;
	
	public MouseDownEvent(double _x, double _y) {
		x = _x;
		y = _y;
	}
	
	public static abstract class MouseDownFunc extends Function<MouseDownEvent> {

		public MouseDownFunc() {
			super(MouseDownEvent.class);
		}

	}

}
