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
package cn.liutils.cgui.gui.property;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.loader.Editable;

/**
 * @author WeAthFolD
 *
 */
public class PropTextBox extends PropColor {
	
	@Editable("content")
	public String content = "";
	
	@Editable("echo")
	public boolean doesEcho = false;
	@Editable("echo")
	public char echoChar = '*';
	
	@Editable("size")
	public int textSize = 9;

	@Override
	public String getName() {
		return "text_box";
	}

	@Override
	public IProperty copy() {
		PropTextBox ptb = new PropTextBox();
		ptb.setColor4d(r, g, b, a);
		ptb.content = content;
		ptb.doesEcho = doesEcho;
		ptb.echoChar = echoChar;
		return ptb;
	}

	public static PropTextBox get(Widget w) {
		return (PropTextBox) w.getProperty("text_box");
	}
	
}
