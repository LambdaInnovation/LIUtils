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

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.ChangeContentEvent;
import cn.liutils.cgui.gui.event.ConfirmInputEvent;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.DrawEvent.DrawEventHandler;
import cn.liutils.cgui.gui.event.KeyEvent;
import cn.liutils.cgui.gui.event.KeyEvent.KeyEventHandler;
import cn.liutils.util.render.Font;

/**
 * @author WeAthFolD
 */
public class TextBox extends Component {
	
	public String content = "";
	
	public boolean allowEdit = true;
	
	public boolean doesEcho = false;
	public char echoChar = '*';
	
	public int color = 0xffffff;
	
	public double size = 5;
	
	public TextBox() {
		name = "TextBox";
		addEventHandler(new KeyEventHandler() {
			
			@Override
			public void handleEvent(Widget w, KeyEvent event) {
				if(!allowEdit)
					return;
				
				int par2 = event.keyCode;
				
				if (par2 == Keyboard.KEY_BACK && content.length() > 0) {
					content = content.substring(0, content.length() - 1);
					w.postEvent(new ChangeContentEvent());
				} else if(par2 == Keyboard.KEY_RETURN || par2 == Keyboard.KEY_NUMPADENTER) {
					w.postEvent(new ConfirmInputEvent());
				}
				if (ChatAllowedCharacters.isAllowedCharacter(event.inputChar)) {
					content = content + event.inputChar;
					w.postEvent(new ChangeContentEvent());
				}
			}
			
		});
		
		addEventHandler(new DrawEventHandler() {

			@Override
			public void handleEvent(Widget w, DrawEvent event) {
				Font.font.draw(content, 3, w.transform.height - size, size, color);
				
				if(allowEdit && w.isFocused() && Minecraft.getSystemTime() % 1000 < 500) {
					double len = Font.font.strLen(content, size);
					Font.font.draw("|", len + 3, w.transform.height - size, size, color);
				}
			}
			
		});
	}
	
	public static TextBox get(Widget w) {
		return w.getComponent("TextBox");
	}

}
