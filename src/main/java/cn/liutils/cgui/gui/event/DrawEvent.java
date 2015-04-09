/**
 * 
 */
package cn.liutils.cgui.gui.event;

/**
 * @author WeAthFolD
 */
public class DrawEvent implements GuiEvent {
	public final double mx, my;
	public final boolean hovering;
	
	public DrawEvent(double _mx, double _my, boolean hov) {
		mx = _mx;
		my = _my;
		hovering = hov;
	}
}
