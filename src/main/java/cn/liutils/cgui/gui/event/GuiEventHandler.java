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
	
	public final Widget widget;
	
	public GuiEventHandler(Widget w) {
		widget = w;
	}
	
	public abstract void handleEvent(T event);
	public abstract Class <? extends GuiEvent> getEventClass();
	
	//Helper method
	protected void checkProperty(String id, Class<? extends IProperty> pclazz) {
		IProperty p = widget.getProperty(id);
		if(p == null) {
			IProperty add;
			try {
				add = pclazz.newInstance();
				widget.addProperty(id, add);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
