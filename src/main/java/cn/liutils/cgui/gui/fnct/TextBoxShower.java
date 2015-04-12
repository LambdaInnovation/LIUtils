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

import java.util.Arrays;
import java.util.Collection;

import net.minecraft.client.Minecraft;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.property.PropTextBox;
import cn.liutils.util.render.Font;

/**
 * @author WeAthFolD
 *
 */
public class TextBoxShower extends Function<DrawEvent> {

	public TextBoxShower() {
		super(DrawEvent.class);
	}

	@Override
	public Collection<Class> getRequiredProperties() {
		return Arrays.asList(new Class[] { PropTextBox.class });
	}

	@Override
	public String getName() {
		return "text_box_shower";
	}

	@Override
	public void handleEvent(Widget w, DrawEvent event) {
		PropTextBox prop = (PropTextBox) w.getProperty("text_box");
		Font.font.draw(prop.content, 3, w.propBasic().height - prop.textSize, prop.textSize, prop.toHexColor());
		
		if(isEditable(w) && Minecraft.getSystemTime() % 1000 < 500) {
			double len = Font.font.strLen(prop.content, prop.textSize);
			Font.font.draw("|", len + 3, w.propBasic().height - prop.textSize, prop.textSize, prop.toHexColor());
		}
	}
	
	private boolean isEditable(Widget w) {
		return w.hasFunction("text_box_input");
	}

}
