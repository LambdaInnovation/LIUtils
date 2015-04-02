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
package cn.liutils.api.gui.widget;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

import cn.liutils.api.gui.Widget;
import cn.liutils.util.RenderUtils;
import cn.liutils.util.render.Font;
import cn.liutils.util.render.Font.Align;
import cn.liutils.util.render.LambdaFont;

/**
 * @author WeathFolD
 */
public class InputBox extends Widget {
	
	final float fontSize;
	final int maxLines;
	final int cpl;
	final double lineHeight;
	
	String[] contents;
	int editLine = 0;

	Font font = Font.font;
	int textColor = 0xFFFFFF;
	
	boolean echo = false;
	char echoChar = '*';
	
	public InputBox(double x, double y, double w, double _lineHeight, float _fontSize, int _maxLines, int _charPerLine) {
		super(x, y, w, _lineHeight * _maxLines);
		fontSize = _fontSize;
		maxLines = _maxLines;
		cpl = _charPerLine;
		lineHeight = _lineHeight;
		contents = new String[maxLines];
		for(int i = 0; i < contents.length; ++i) {
			contents[i] = "";
		}
	}
	
	@Override
	public boolean doesNeedFocus() {
		return true;
	}
	
	public InputBox setTextColor(int c) {
		textColor = c;
		return this;
	}
	
	public InputBox setEcho(boolean b) {
		echo = b;
		return this;
	}
	
	public InputBox setEchoChar(char ch) {
		echoChar = ch;
		return this;
	}
	
	public InputBox setFont(Font font) {
		this.font = font;
		return this;
	}
	
	@Override
	public void draw(double mx, double my, boolean hover) {
	    super.draw(mx, my, hover);
	    
		boolean focus = this.isFocused();
		double y = 0.0;
		for(int i = 0; i < contents.length; ++i) {
			String str = contents[i];
			if(echo) {
				StringBuilder sb = new StringBuilder();
				for(int l = 0; l < str.length(); ++l) {
					sb.append(echoChar);
				}
				str = sb.toString();
			}
			font.drawTrimmed((focus && editLine == i && (Minecraft.getSystemTime() % 1000 < 500)) ? str + "|" : str, 
				0, y + (lineHeight - fontSize), fontSize, textColor, Align.LEFT, width, "...");
			y += lineHeight;
		}
	}
	
	@Override
	public void handleKeyInput(char par1, int par2) {
		if (par2 == Keyboard.KEY_UP) {
			this.editLine--;
			if(editLine < 0) editLine = 0;
		} else if (par2 == Keyboard.KEY_DOWN || par2 == Keyboard.KEY_RETURN || par2 == Keyboard.KEY_NUMPADENTER) {
			this.editLine++;
			if(editLine >= maxLines) 
				editLine = maxLines - 1;
		} else if (par2 == Keyboard.KEY_BACK && contents[this.editLine].length() > 0) {
			contents[this.editLine] = contents[this.editLine].substring(0, contents[this.editLine].length() - 1);
		}
		if (ChatAllowedCharacters.isAllowedCharacter(par1) && contents[this.editLine].length() < cpl) {
			contents[this.editLine] = contents[this.editLine] + par1;
		}
	}
	
	public List<String> getContents() {
		return Arrays.asList(contents);
	}
	
	public String getContent() {
		StringBuilder sb = new StringBuilder();
		for(String str : contents) {
			sb.append(str);
		}
		return sb.toString();
	}

}
