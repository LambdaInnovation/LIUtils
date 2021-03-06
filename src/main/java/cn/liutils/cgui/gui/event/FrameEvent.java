/**
 * 
 */
package cn.liutils.cgui.gui.event;


/**
 * @author WeAthFolD
 */
public class FrameEvent implements GuiEvent {
	public final double mx, my;
	public final boolean hovering;
	
	public FrameEvent(double _mx, double _my, boolean hov) {
		mx = _mx;
		my = _my;
		hovering = hov;
	}
	
	public abstract static class FrameEventHandler extends GuiEventHandler<FrameEvent> {
		public FrameEventHandler() {
			super(FrameEvent.class);
		}
	}
}
