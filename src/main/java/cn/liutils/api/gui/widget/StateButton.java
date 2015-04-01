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

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cn.liutils.api.draw.tess.GUIRect;
import cn.liutils.api.draw.tess.RectMapping;
import cn.liutils.api.gui.Widget;
import cn.liutils.core.LIUtils;
import cn.liutils.util.RenderUtils;
import cn.liutils.util.render.LambdaFont;
import cn.liutils.util.render.LambdaFont.Align;

/**
 * @author WeathFolD
 *
 */
public abstract class StateButton extends Widget {
	
	public enum ButtonState {
		IDLE, ACTIVE, INVALID;
	}
	
	private ButtonState state = ButtonState.IDLE;
	private GUIRect rect;
	protected double[][] maps;
	
	protected LambdaFont font;
	protected String text;
	protected float textSize;
	protected int[][] textColors = {
		{255, 255, 255, 255},
		{255, 255, 255, 255},
		{255, 255, 255, 255}
	};
	
	public StateButton(double x, double y, double w, double h, ResourceLocation tex, double[][] mappingData) {
	    this(x, y, w, h, tex, w, h, mappingData);
	}
	
	public StateButton(double x, double y, double w, double h, ResourceLocation tex, double tw, double th, double[][] mappingData) {
		super(x, y, w, h);
		this.initTexDraw(tex, 0, 0, tw, th);
		loadMappingData(mappingData);
		rect = (GUIRect) drawer.getHandler("rect_2d");
	}
	
	public void setFont(LambdaFont ttf) {
		font = ttf;
	}
	
	public void setText(String str) {
		text = str;
	}
	
	private void loadMappingData(double[][] data) {
	    maps = new double[3][];
	    for(int i = 0; i < 3; ++i) {
	        double[] rep = data.length > i ? data[i] : null;
	        if(maps[i] == null) {
	            maps[i] = rep == null ? data[0] : rep;
	        }
	    }
	}
	
	/**
	 * You can pass null into colorData means use default color
	 */
	public void setTextData(float size, int[][] texColorData) {
		textSize = size;
		if(texColorData != null) {
			textColors = texColorData;
		}
	}
	
	public void setInvalid(boolean is) {
		state = is ? ButtonState.INVALID : ButtonState.IDLE;
	}
	
	@Override
	public void draw(double mx, double my, boolean hover) {
		switch(state) {
		case IDLE:
			if(hover) state = ButtonState.ACTIVE;
			break;
		case ACTIVE:
			if(!hover) state = ButtonState.IDLE;
			break;
		case INVALID:
			break;
		}
		
		RectMapping rm = rect.getMap();
		int index = state.ordinal();
		rm.u0 = maps[index][0];
		rm.v0 = maps[index][1];
		
		super.draw(mx, my, hover);
		
		if(text != null) {
			if(font == null) {
				LIUtils.log.error("NULL FONT in " + this);
				return;
			}
			RenderUtils.bindColor(textColors[index]);
			font.draw(StatCollector.translateToLocal(text), 
				width / 2, height / 2 - textSize / 2, textSize, Align.CENTER);
			RenderUtils.bindIdentity();
		}
	}
	
	@Override
	public final void onMouseDown(double mx, double my) {
		if(state != ButtonState.INVALID) {
			buttonPressed(mx, my);
		}
	}
	
	public abstract void buttonPressed(double mx, double my);
	
}
