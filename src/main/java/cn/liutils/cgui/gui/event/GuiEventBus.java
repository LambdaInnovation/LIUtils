package cn.liutils.cgui.gui.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import cn.liutils.cgui.gui.Widget;

public final class GuiEventBus {
	
	public GuiEventBus() {}
	
	Map< Class<? extends GuiEvent>, LinkedList<GuiHandlerNode> > eventHandlers = new HashMap();
	
	public final void postEvent(Widget widget, GuiEvent event) {
		List<GuiHandlerNode> list = eventHandlers.get(event.getClass());
		if(list != null) {
			for(GuiHandlerNode n : list) {
				n.handler.handleEvent(widget, event);
			}
		}
	}
	
	@Deprecated
	public void regEventHandler(GuiEventHandler handler) {
		reg(handler.getEventClass(), handler);
	}
	
	@Deprecated
	public void regAtBeginning(GuiEventHandler handler) {
		regAtBeginning(handler.getEventClass(), handler);
	}
	
	public void reg(Class<? extends GuiEvent> clazz, IGuiEventHandler handler) {
		getRawList(clazz).add(new GuiHandlerNode(handler));
	}
	
	public void regAtBeginning(Class<? extends GuiEvent> clazz, IGuiEventHandler handler) {
		getRawList(clazz).addFirst(new GuiHandlerNode(handler));
	}
	
	/**
	 * Get the event handlers for a specified event type. Modification to this list 
	 * will have NO effect.
	 */
	private List<IGuiEventHandler> getEventHandlers(Class<? extends GuiEvent> clazz) {
		LinkedList<GuiHandlerNode> ret = eventHandlers.get(clazz);
		if(ret == null) {
			eventHandlers.put(clazz, ret = new LinkedList());
		}
		
		
		return ret.stream().map((GuiHandlerNode n)->n.handler).collect(Collectors.toList());
	}
	
	private LinkedList<GuiHandlerNode> getRawList(Class<? extends GuiEvent> clazz) {
		LinkedList<GuiHandlerNode> ret = eventHandlers.get(clazz);
		if(ret == null) {
			eventHandlers.put(clazz, ret = new LinkedList());
		}
		
		return ret;
	}
	
	public GuiEventBus copy() {
		GuiEventBus ret = new GuiEventBus();
		
		for(Entry< Class<? extends GuiEvent>, LinkedList<GuiHandlerNode>> ent : eventHandlers.entrySet()) {
			ret.getRawList(ent.getKey()).addAll(ent.getValue());
		}
		return ret;
	}
	
	private class GuiHandlerNode {
		IGuiEventHandler handler;
		public boolean enabled = true;
		
		public GuiHandlerNode(IGuiEventHandler handler) {
			this.handler = handler;
		}
	}
	
}
