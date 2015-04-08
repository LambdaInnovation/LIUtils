/**
 * 
 */
package cn.liutils.cgui.gui.event;

/**
 * @author WeAthFolD
 */
public class DragEvent implements GuiEvent {
	
	 public double dx, dy;
	 
	 public DragEvent(double _dx, double _dy) {
		 dx = _dx;
		 dy = _dy;
	 }
}
