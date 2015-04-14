package cn.liutils.cgui.gui.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.liutils.cgui.gui.Widget;

public final class GuiEventBus {
	
	public GuiEventBus() {
	}
	
	Map< Class<? extends GuiEvent>, List<GuiEventHandler> > eventHandlers = new HashMap();
	
	public final void postEvent(Widget widget, GuiEvent event) {
		for(GuiEventHandler h : getEventHandlers(event.getClass())) {
			if(h.enabled)
				h.handleEvent(widget, event);
		}
	}
	
	public void regEventHandler(GuiEventHandler handler) {
		getEventHandlers(handler.getEventClass()).add(handler);
	}
	
	/**
	 * Get the event handlers for a specified event type. Modification to this list 
	 * will have effect instantly.
	 */
	public List<GuiEventHandler> getEventHandlers(Class<? extends GuiEvent> clazz) {
		List<GuiEventHandler> ret = eventHandlers.get(clazz);
		if(ret == null) {
			eventHandlers.put(clazz, ret = new ArrayList());
		}
		return ret;
	}
	
	public GuiEventBus copy() {
		GuiEventBus ret = new GuiEventBus();
		for(Entry<Class<? extends GuiEvent>, List<GuiEventHandler>> ent : eventHandlers.entrySet()) {
			//Light copy, but as long as GuiEventHandler is stateless, this is just fine.
			ret.getEventHandlers(ent.getKey()).addAll(ent.getValue()); 
		}
		return ret;
	}
	
}
