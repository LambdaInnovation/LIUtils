/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.cgui.gui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventHandler;
import cn.liutils.cgui.gui.fnct.Function;
import cn.liutils.cgui.gui.property.IProperty;
import cn.liutils.cgui.gui.property.PropBasic;
import cn.liutils.cgui.loader.CGUIEditor;


/**
 * @author WeathFolD
 */
public class Widget extends WidgetContainer {
	
	Map<String, IProperty> properties = new HashMap();
	Set<String> functions = new HashSet();
	
	public boolean 
		doesDraw = true, 
		doesListenKey = true;
	
	public boolean
		needFocus = false;
	
	public boolean disposed = false;
	public boolean dirty = true; //Indicate that this widget's pos data is dirty and requires update.

	LIGui gui;
	Widget parent;
	
	//Real-time calculated data not directly relevant to widget properties
	public double x, y;
	public double scale;
	public int zOrder;
	/**
	 * If this widget is being edited(i.e. in a CGUI edit gui) and should dummy some state-change related functions.
	 */
	public boolean editing = false;
	
	//Defaults
	{
		addProperty(new PropBasic());
	}
	
	public Widget() {}
	
	/**
	 * Return a reasonable copy of this widget. Retains all the properties and functions.
	 */
	public Widget copy() {
		Widget n = new Widget();
		for(IProperty ip : properties.values()) {
			IProperty dup = ip.copy();
			n.addProperty(dup, true);
		}
		for(String func : functions) {
			Function f = CGUIEditor.instance.getFunction(func);
			if(f == null) {
				System.err.println("Didn't find function " + func + " when trying to clone the widget.");
			} else {
				n.regEventHandler(f);
			}
		}
		return n;
	}

	public PropBasic propBasic() {
		return (PropBasic) properties.get("basic");
	}
	
	public IProperty getProperty(String name) {
		return properties.get(name);
	}
	
	public void addProperty(IProperty prop) {
		addProperty(prop, false);
	}
	
	public void addProperty(IProperty prop, boolean force) {
		if(properties.containsKey(prop.getName()) && !force)
			throw new RuntimeException("Duplicate property id " + prop.getName());
		properties.put(prop.getName(), prop);
	}
	
	/**
	 * Called when added into a GUI.
	 */
	protected void onAdded() {}
	
	public boolean initialized() {
		return gui != null;
	}
	
	public boolean isWidgetParent() {
		return parent != null;
	}
	
	public Widget getWidgetParent() {
		return parent;
	}
	
	public LIGui getGui() {
		return gui;
	}
	
	/**
	 * Dispose this gui. Will get removed next frame.
	 */
	public void dispose() {
		disposed = true;
	}
	
	/**
	 * Get the relative drawing priority
	 * If two widgets share the same parent, the one with higher priority will always be drawn first.
	 */
	public int getDrawPriority() {
		return 1;
	}
	
	//Event dispatch
	public final Widget regEventHandler(GuiEventHandler h) {
		getEventHandlers(h.getEventClass()).add(h);
		h.onAdded(this);
		if(h instanceof Function) {
			functions.add(((Function) h).getName());
		}
		
		return this;
	}
	
	public final void postEvent(GuiEvent event) {
		for(GuiEventHandler h : getEventHandlers(event.getClass())) {
			h.handleEvent(this, event);
		}
	}
	
	private Set<GuiEventHandler> getEventHandlers(Class<? extends GuiEvent> clazz) {
		Set<GuiEventHandler> ret = eventHandlers.get(clazz);
		if(ret == null) {
			eventHandlers.put(clazz, ret = new HashSet());
		}
		return ret;
	}
	
	Map< Class<? extends GuiEvent>, Set<GuiEventHandler> > eventHandlers = new HashMap();
	
	//Utils
	public void checkProperty(String id, Class<? extends IProperty> pclazz) {
		IProperty p = getProperty(id);
		if(p == null) {
			IProperty add;
			try {
				add = pclazz.newInstance();
				addProperty(add);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isPointWithin(double tx, double ty) {
		double w = propBasic().width, h = propBasic().height;
		double x1 = x + w * scale, y1 = y + h * scale;
		return (x <= tx && tx <x1) && (y <= ty && ty < y1);
	}

	@Override
	void onWidgetAdded(String name, Widget w) {
		this.dirty = true;
		w.parent = this;
		w.gui = gui;
	}

}
