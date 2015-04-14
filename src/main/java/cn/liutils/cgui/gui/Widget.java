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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventBus;
import cn.liutils.cgui.gui.event.GuiEventHandler;
import cn.liutils.cgui.gui.fnct.Component;
import cn.liutils.cgui.gui.fnct.Transform;


/**
 * @author WeathFolD
 */
public class Widget extends WidgetContainer {
	
	private GuiEventBus eventBus = new GuiEventBus();
	private List<Component> components = new ArrayList();
	
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
	
	public Transform transform;
	
	//Defaults
	{
		addComponent(transform = new Transform());
	}
	
	public Widget() {}
	
	/**
	 * Return a reasonable copy of this widget. Retains all the properties and functions, 
	 * along with its all sub widgets recursively.
	 */
	public Widget copy() {
		Widget n = null;
		try {
			n = getClass().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		copyInfoTo(n);
		return n;
	}
	
	protected void copyInfoTo(Widget n) {
		n.components.clear();
		for(Component c : components) {
			n.addComponent(c.copy());
		}
		n.transform = n.getComponent("Transform");
		n.eventBus = eventBus.copy();
		
		//Also copy the widget's sub widgets recursively.
		for(Widget asub : getDrawList()) {
			n.addWidget(asub.getName(), asub.copy());
		}
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
	
	//Component handling
	/**
	 * Java generic type is shit, so use it at your own risk.
	 * @return the first component with the name specified, or null if no such component.
	 */
	public <T extends Component> T getComponent(String name) {
		for(Component c : components) {
			if(c.name.equals(name))
				return (T) c;
		}
		return null;
	}
	
	public Widget addComponent(Component c) {
		components.add(c);
		return this;
	}
	
	/**
	 * Return the raw component list. Any modification on the list will directly change the behavior of the widget.
	 */
	public List<Component> getComponentList() {
		return components;
	}
	
	//Event dispatch
	public final Widget regEventHandler(GuiEventHandler h) {
		eventBus.regEventHandler(h);
		return this;
	}
	
	public final void postEvent(GuiEvent event) {
		for(Component c : components) {
			c.postEvent(this, event);
		}
		eventBus.postEvent(this, event);
	}
	
	//Utils
	public String getName() {
		return this.isWidgetParent() ? parent.getWidgetName(this) : getGui().getWidgetName(this);
	}
	
	public boolean isPointWithin(double tx, double ty) {
		double w = transform.width, h = transform.height;
		double x1 = x + w * scale, y1 = y + h * scale;
		return (x <= tx && tx <x1) && (y <= ty && ty < y1);
	}
	
	public boolean isFocused() {
		return gui.getFocus() == this;
	}

	@Override
	void onWidgetAdded(String name, Widget w) {
		this.dirty = true;
		w.parent = this;
		w.gui = gui;
	}

}
