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

import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

import scala.actors.threadpool.Arrays;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.KeyEvent;
import cn.liutils.cgui.gui.property.PropTextBox;

/**
 * @author WeAthFolD
 *
 */
public class TextBoxInput extends Function<KeyEvent> {

	public TextBoxInput() {
		super(KeyEvent.class);
	}

	@Override
	public Collection getRequiredProperties() {
		return Arrays.asList(new Class[] { PropTextBox.class });
	}

	@Override
	public String getName() {
		return "text_box_input";
	}

	@Override
	public void handleEvent(Widget w, KeyEvent event) {
		int par2 = event.keyCode;
		PropTextBox prop = (PropTextBox) w.getProperty("text_box");
		
		if (par2 == Keyboard.KEY_BACK && prop.content.length() > 0) {
			prop.content = prop.content.substring(0, prop.content.length() - 1);
		}
		if (ChatAllowedCharacters.isAllowedCharacter(event.inputChar)) {
			prop.content = prop.content + event.inputChar;
		}
	}

}
