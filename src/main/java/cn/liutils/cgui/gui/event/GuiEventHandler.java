/**
 * 
 */
package cn.liutils.cgui.gui.event;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.property.IProperty;

/**
 * @author WeAthFolD
 */
public abstract class GuiEventHandler<T extends GuiEvent> {
	
	private final Class<? extends GuiEvent> eventClass;
	
	public GuiEventHandler(Class<? extends GuiEvent> _eventClass) {
		eventClass = _eventClass;
	}
	
	public abstract void handleEvent(Widget w, T event);
	
	public void onAdded(Widget w) {}
	
	public Class <? extends GuiEvent> getEventClass() {
		return eventClass;
	}
}
