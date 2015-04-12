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
import cn.liutils.cgui.gui.fnct.TextBoxInput;
import cn.liutils.cgui.gui.fnct.TextBoxShower;
import cn.liutils.cgui.gui.property.IProperty;
import cn.liutils.cgui.gui.property.PropBasic;
import cn.liutils.cgui.gui.property.PropColor;
import cn.liutils.cgui.gui.property.PropTextBox;
import cn.liutils.cgui.gui.property.PropTexture;
import cn.liutils.cgui.loader.ui.PropertyEditor;

/**
 * Main loading interface to provide templates&customized objects to CGUI.
 * Only Properties, Functions and Widget templates registered in this class are available
 * for in-editor usage.
 * @author WeAthFolD
 */
public class CGUIEditor {
	
	public static CGUIEditor instance = new CGUIEditor();
	
	Map<String, ReggedProp> props = new HashMap();
	
	Map<String, Function> functions = new HashMap();
	Map<String, Widget> templates = new HashMap();
	
	//Built-ins.
	{
		//Default Properties
		addProperty(new PropBasic());
		addProperty(new PropColor());
		addProperty(new PropTexture());
		addProperty(new PropTextBox());
		
		//Default functions
		addFunction(new SimpleDrawer());
		addFunction(new Draggable());
		addFunction(new TextBoxInput());
		addFunction(new TextBoxShower());
		
		//Default templates
		{ //"default"
			Widget def = new Widget();
			def.addProperty(new PropTexture());
			def.addFunction(new SimpleDrawer());
			addTemplate("default", def);
		}
		{ //"input_box"
			Widget inp = new Widget();
			inp.addProperty(new PropTextBox());
			inp.addFunction(new TextBoxInput());
			inp.addFunction(new TextBoxShower());
			addTemplate("input_box", inp);
		}
	}
	
	public void addFunction(Function func) {
		functions.put(func.getName(), func);
	}
	
	public void addTemplate(String str, Widget template) {
		templates.put(str, template);
	}
	
	public void addProperty(IProperty prop) {
		PropertyEditor editor = new PropertyEditor(prop);
		props.put(prop.getName(), new ReggedProp(prop, editor));
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
	
	public IProperty getProperty(String name) {
		ReggedProp rp = props.get(name);
		return rp == null ? null : rp.property;
	}
	
	public PropertyEditor getPropertyEditor(IProperty target) {
		ReggedProp rp = props.get(target.getName());
		return rp == null ? null : rp.editor;
	}
	
	private static class ReggedProp {
		public IProperty property;
		public PropertyEditor editor;
		
		public ReggedProp(IProperty _prop, PropertyEditor _editor) {
			property = _prop;
		}
	}

}
