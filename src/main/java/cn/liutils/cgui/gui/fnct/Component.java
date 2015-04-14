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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventBus;
import cn.liutils.cgui.gui.event.GuiEventHandler;
import cn.liutils.cgui.gui.fnct.annotations.CopyIgnore;
import cn.liutils.cgui.utils.TypeHelper;
import cn.liutils.core.LIUtils;

/**
 * Component is the basic content of Widget. It can define a set of EventHandlers and store information itself.
 * The (non-runtime-state) information stored in the component will be copied as a widget is copied.
 * @author WeAthFolD
 */
public class Component {
	
	static Map<Class, List<Field>> copiedFields = new HashMap();

	private GuiEventBus eventBus;
	
	public String name = "Default";
	
	public Component() {
		checkCopyFields();
		eventBus = new GuiEventBus();
	}
	
	private void checkCopyFields() {
		if(copiedFields.containsKey(getClass()))
			return;
		List<Field> ret = new ArrayList<Field>();
		for(Field f : getClass().getFields()) {
			if(!f.isAnnotationPresent(CopyIgnore.class) && TypeHelper.isTypeSupported(f.getType())) {
				ret.add(f);
			}
		}
		copiedFields.put(getClass(), ret);
	}
	
	protected void addEventHandler(GuiEventHandler handler) {
		eventBus.regEventHandler(handler);
	}
	
	public void postEvent(Widget w, GuiEvent event) {
		eventBus.postEvent(w, event);
	}
	
	public Component copy() {
		try {
			Component c = getClass().newInstance();
			for(Field f : copiedFields.get(getClass())) {
				TypeHelper.set(f, c, TypeHelper.copy(f, this));
			}
			return c;
		} catch(Exception e) {
			LIUtils.log.error("Unexpected error occured copying component of type " + getClass());
			e.printStackTrace();
		}
		return null;
	}
	
	public Collection<Field> getPropertyList() {
		return copiedFields.get(getClass());
	}
	
}
