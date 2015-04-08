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
package cn.liutils.cgui.loader.ui;

import net.minecraft.util.ResourceLocation;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventHandler;
import cn.liutils.cgui.gui.fnct.SimpleDrawer;
import cn.liutils.util.render.Font;

/**
 * @author WeAthFolD
 *
 */
public class Toolbar extends Window {
	
	public Toolbar() {
		super("Toolbar");
		propWidget().setPos(10, 10).setSize(200, 30);
		
		addWidget(new Button(0, "open", "Open CGUI File"));
		addWidget(new Button(1, "save", "Save CGUI File"));
		addWidget(new Button(2, "add", "Add Widget"));
		addWidget(new Button(3, "remove", "Remove Widget"));
	}
	
	static class Button extends Widget {
		public Button(int i, final String tn, String name) {
			propWidget().setSize(18, 18).setPos(i * 20, 10);
			regEventHandler(new SimpleDrawer(this, new ResourceLocation("liutils:textures/cgui/toolbar/" + tn + ".png")));
		}
	}
	
}
