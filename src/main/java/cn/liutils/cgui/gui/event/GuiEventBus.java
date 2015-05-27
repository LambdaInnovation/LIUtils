package cn.liutils.cgui.gui.event;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cn.liutils.cgui.gui.Widget;

public final class GuiEventBus {
	
	public GuiEventBus() {
	}
	
	Map< Class<? extends GuiEvent>, LinkedList<GuiEventHandler> > eventHandlers = new HashMap();
	
	public final void postEvent(Widget widget, GuiEvent event) {
		for(GuiEventHandler h : getEventHandlers(event.getClass())) {
			if(h.enabled)
				h.handleEvent(widget, event);
		}
	}
	
	public void regEventHandler(GuiEventHandler handler) {
		getEventHandlers(handler.getEventClass()).add(handler);
	}
	
	public void regAtBeginning(GuiEventHandler handler) {
		getEventHandlers(handler.getEventClass()).addFirst(handler);
	}
	
	/**
	 * Get the event handlers for a specified event type. Modification to this list 
	 * will have effect instantly.
	 */
	public LinkedList<GuiEventHandler> getEventHandlers(Class<? extends GuiEvent> clazz) {
		LinkedList<GuiEventHandler> ret = eventHandlers.get(clazz);
		if(ret == null) {
			eventHandlers.put(clazz, ret = new LinkedList());
		}
		return ret;
	}
	
	public GuiEventBus copy() {
		GuiEventBus ret = new GuiEventBus();
		for(Entry<Class<? extends GuiEvent>, LinkedList<GuiEventHandler>> ent : eventHandlers.entrySet()) {
			//Light copy, but as long as GuiEventHandler is stateless, this is just fine.
			ret.getEventHandlers(ent.getKey()).addAll(ent.getValue()); 
		}
		return ret;
	}
	
}
