/**
 * 
 */
package cn.liutils.api.gui.widget;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.gui.DrawArea;
import cn.liutils.api.gui.LIGui;
import cn.liutils.api.gui.Widget;
import cn.liutils.util.RenderUtils;
import cn.liutils.util.render.TextUtils;
import cn.liutils.util.render.TrueTypeFont;

/**
 * @author WeathFolD
 *
 */
public class TextButton extends Widget {
	
	double downU = -1, downV = -1;
	double invalidU = -1, invalidV = -1;
	String text = null;
	TrueTypeFont font;
	float size;
	int[] 
		color = {255, 255, 255, 255},
		activeColor = {255, 255, 255, 255},
		inActiveColor = {255, 255, 255, 255};

	public TextButton(String id, Widget par, double x, double y, double w,
			double h) {
		super(id, par, x, y, w, h);
	}
	
	public TextButton(String id, LIGui scr, double x, double y, double w,
			double h) {
		super(id, scr, x, y, w, h);
	}
	
	public void setTextProps(String str, float size) {
		setTextProps(str, TextUtils.FONT_CONSOLAS_64, size);
	}
	
	public void setTextProps(String str, TrueTypeFont ttf, float size) {
		text = str;
		font = ttf;
		this.size = size;
	}
	
	public void setDownMapping(double u, double v) {
		downU = u;
		downV = v;
	}
	
	public void setInvalidMapping(double u, double v) {
		invalidU = u;
		invalidV = v;
	}
	
	public void setTextColor(int[] barr) {
		setTextColor(barr[0], barr[1], barr[2], barr.length > 3 ? barr[3] : 255);
	}
	
	public void setTextColor(int r, int g, int b, int a) {
		color = new int[] { r, g, b, a };
	}
	
	public void setActiveColor(int[] barr) {
		setActiveColor(barr[0], barr[1], barr[2], barr.length > 3 ? barr[3] : 255);
	}
	
	public void setActiveColor(int r, int g, int b, int a) {
		activeColor = new int[] { r, g, b, a };
	}
	
	public void setInactiveColor(int[] barr) {
		setInactiveColor(barr[0], barr[1], barr[2], barr.length > 3 ? barr[3] : 255);
	}
	
	public void setInactiveColor(int r, int g, int b, int a) {
		inActiveColor = new int[] { r, g, b, a };
	}
	
	@Override
	public void draw(double mx, double my, boolean mouseHovering) {
		DrawArea area = getArea();
		double ou = area.u, ov = area.v;
		int[] color;
		if(!this.receiveEvent) {
			if(invalidU != -1 && invalidV != -1) {
				area.u = invalidU;
				area.v = invalidV;
			} 
			color = this.inActiveColor;
		} else if(mouseHovering) {
			area.u = downU;
			area.v = downV;
			color = this.activeColor;
		} else {
			color = this.color;
		}
		super.draw(mx, my, mouseHovering);
		area.u = ou;
		area.v = ov;
		
		if(text != null) {
			RenderUtils.bindColor(color);
			TextUtils.drawText(font, text, area.width / 2, 
				area.height / 2 - size / 2, size, 
				TrueTypeFont.ALIGN_CENTER);
			GL11.glColor4d(1, 1, 1, 1);
		}
	}

}
