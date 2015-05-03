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
package cn.liutils.cgui.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.annotations.CopyIgnore;
import cn.liutils.cgui.gui.event.ChangeContentEvent;
import cn.liutils.cgui.gui.event.ConfirmInputEvent;
import cn.liutils.cgui.gui.event.FrameEvent;
import cn.liutils.cgui.gui.event.FrameEvent.FrameEventHandler;
import cn.liutils.cgui.gui.event.KeyEvent;
import cn.liutils.cgui.gui.event.KeyEvent.KeyEventHandler;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent.MouseDownHandler;
import cn.liutils.cgui.utils.Color;
import cn.liutils.util.render.Font;
import cn.liutils.util.render.Font.Align;

/**
 * 事实证明UI底层是十分蛋疼的……
 * @author WeAthFolD
 */
public class TextBox extends Component {
	
	public String content = "";
	
	public boolean allowEdit = true;
	
	public boolean doesEcho = false;
	public char echoChar = '*';
	
	public Color color = new Color(0xffffff);
	
	public double size = 5;
	
	@CopyIgnore
	public int caretPos = 0;
	
	public TextBox setSize(double s) {
		size = s;
		return this;
	}
	
	public TextBox disallowEdit() {
		allowEdit = false;
		return this;
	}
	
	public TextBox setContent(String str) {
		content = str;
		return this;
	}
	
	public TextBox() {
		super("TextBox");
		addEventHandler(new KeyEventHandler() {
			
			@Override
			public void handleEvent(Widget w, KeyEvent event) {
				if(!allowEdit)
					return;
				
				int par2 = event.keyCode;
				
				if(par2 == Keyboard.KEY_RIGHT) {
					caretPos++;
				} else if(par2 == Keyboard.KEY_LEFT) {
					caretPos--;
				}
				
				if(caretPos < 0) caretPos = 0;
				if(caretPos > content.length()) caretPos = content.length();
				
				if (par2 == Keyboard.KEY_BACK && content.length() > 0) {
					if(caretPos > 0) {
						content = content.substring(0, caretPos - 1) + 
							(caretPos == content.length() ? "" : content.substring(caretPos, content.length()));
						--caretPos;
					}
					w.postEvent(new ChangeContentEvent());
				} else if(par2 == Keyboard.KEY_RETURN || par2 == Keyboard.KEY_NUMPADENTER) {
					w.postEvent(new ConfirmInputEvent());
				} else if(par2 == Keyboard.KEY_DELETE) {
					content = "";
					w.postEvent(new ChangeContentEvent());
				}
				if (ChatAllowedCharacters.isAllowedCharacter(event.inputChar)) {
					content = content.substring(0, caretPos) + event.inputChar +
							(caretPos == content.length() ? "" : content.substring(caretPos, content.length()));
					caretPos += 1;
					w.postEvent(new ChangeContentEvent());
				}
			}
			
		});
		
		addEventHandler(new MouseDownHandler() {
			@Override
			public void handleEvent(Widget w, MouseDownEvent event) {
				double len = 3;
				for(int i = 0; i < content.length(); ++i) {
					double cw = Font.font.strLen(String.valueOf(content.charAt(i)), size);
					len += cw;
					
					if(len > event.x) {
						System.out.println(len + " " + event.x + " " + (event.x - len - cw > cw / 2));
						caretPos = (event.x - len + cw > cw / 2) ? i + 1 : i;
						return;
					}
				}
				caretPos = content.length();
			}
		});
		
		addEventHandler(new FrameEventHandler() {

			@Override
			public void handleEvent(Widget w, FrameEvent event) {
				Font.font.drawTrimmed(content, 2, w.transform.height - size, size, color.asHexWithoutAlpha() & 0xFFFFFF, Align.LEFT, w.transform.width - 2, "...");
				
				if(allowEdit && w.isFocused() && Minecraft.getSystemTime() % 1000 < 500) {
					double len = Font.font.strLen(content.substring(0, caretPos), size);
					//System.out.println(color.r + " " + color.g + " " + color.b + " " + color.a);
					//System.out.println(String.format("0x%x", color.asHexWithoutAlpha()));
					Font.font.draw("|", len + 1, w.transform.height - size, size, color.asHexWithoutAlpha());
				}
			}
			
		});
	}
	
	public static TextBox get(Widget w) {
		return w.getComponent("TextBox");
	}

}
