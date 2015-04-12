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
package cn.liutils.cgui.gui.fnct;

import java.util.ArrayList;
import java.util.Collection;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.GuiEvent;

/**
 * Class that repr gui function. Under cover it is a GuiEventHandler.
 * @author WeAthFolD
 */
public abstract class Function<T extends GuiEvent> {
	
	private final Class<? extends GuiEvent> eventClass;
	
	public Function(Class<? extends GuiEvent> _eventClass) {
		eventClass = _eventClass;
	}
	
	public abstract void handleEvent(Widget w, T event);
	
	public void onAdded(Widget w) {}
	
	public Class <? extends GuiEvent> getEventClass() {
		return eventClass;
	}

	/**
	 * @return All the class of <code>IProperty</code>s required for this function to work correctly.
	 */
	public Collection<Class> getRequiredProperties() { return new ArrayList(); }
	
	/**
	 * @return An identifier of this function. Used just in cgui.
	 */
	public String getName() { return null; }
	
}
