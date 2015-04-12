/**
 * 
 */
package cn.liutils.cgui.gui.event;

import cn.liutils.cgui.gui.fnct.Function;

/**
 * Solely a notification event, fired when a widget was dragged.
 * @author WeAthFolD
 */
public class DragEvent implements GuiEvent { 
	 public DragEvent() {}
	 
	 public static abstract class DragEventFunc extends Function<DragEvent> {
		public DragEventFunc() {
			super(DragEvent.class);
		}
	 }
}
