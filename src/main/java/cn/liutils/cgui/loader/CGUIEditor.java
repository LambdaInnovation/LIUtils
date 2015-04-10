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
package cn.liutils.cgui.loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.fnct.Draggable;
import cn.liutils.cgui.gui.fnct.Function;
import cn.liutils.cgui.gui.fnct.SimpleDrawer;
import cn.liutils.cgui.gui.property.PropTexture;

/**
 * Main loading interface to provide templates&customized objects to CGUI.
 * @author WeAthFolD
 */
public class CGUIEditor {
	
	public static CGUIEditor instance = new CGUIEditor();
	
	Map<String, Function> functions = new HashMap();
	Map<String, Widget> templates = new HashMap();
	
	//Built-ins.
	{
		//Default templates
		{
		Widget def = new Widget();
		def.addProperty(new PropTexture());
		def.regEventHandler(new SimpleDrawer());
		addTemplate("default", def);
		}
		
		//Default functions
		addFunction(new SimpleDrawer());
		addFunction(new Draggable());
	}
	
	public void addFunction(Function func) {
		functions.put(func.getName(), func);
	}
	
	public void addTemplate(String str, Widget template) {
		templates.put(str, template);
	}
	
	public Collection<Function> getFunctions() {
		return functions.values();
	}
	
	public Set<Entry<String, Widget>> getTemplates() {
		return templates.entrySet();
	}
	
	public Widget createFromTemplate(String name) {
		Widget prototype = templates.get(name);
		return prototype == null ? null : prototype.copy();
	}
	
	public Function getFunction(String name) {
		return functions.get(name);
	}

}
