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

import java.util.Collection;

import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventHandler;
import cn.liutils.cgui.gui.property.IProperty;

/**
 * Class that repr gui function. Under cover it is a GuiEventHandler.
 * @author WeAthFolD
 */
public abstract class Function<T extends GuiEvent> extends GuiEventHandler<T>{
	
	public Function(Class<? extends GuiEvent> _eventClass) {
		super(_eventClass);
	}

	/**
	 * @return All the <code>IProperty</code>s required for this function to work correctly.
	 */
	public abstract Collection getRequiredProperties();
	
	public abstract String getName();
	
}
