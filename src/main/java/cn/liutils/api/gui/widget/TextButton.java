/**
 * 
 */
package cn.liutils.api.gui.widget;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.gui.LIGui;
import cn.liutils.api.gui.Widget;
import cn.liutils.util.DebugUtils;
import cn.liutils.util.render.TextUtils;
import cn.liutils.util.render.TrueTypeFont;

/**
 * @author WeathFolD
 *
 */
public class TextButton extends Widget {
	
	double downU = -1, downV = -1;
	String text = null;
	TrueTypeFont font;
	float size;
	double[] color = { 1.0, 1.0, 1.0, 1.0 };

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
	
	public void setTextColor(double r, double g, double b, double a) {
		color = new double[] { r, g, b, a };
	}
	
	public void setTextColor(int[] barr) {
		setTextColor(barr[0], barr[1], barr[2], 255);
	}
	
	public void setTextColor(int r, int g, int b, int a) {
		color = new double[] { r / 255.0, g / 255.0, b / 255.0, a / 255.0 };
		System.out.println(DebugUtils.formatArray(color));
	}
	
	@Override
	public void draw(double mx, double my, boolean mouseHovering) {
		double ou = area.u, ov = area.v;
		if(mouseHovering) {
			area.u = downU;
			area.v = downV;
		}
		super.draw(mx, my, mouseHovering);
		area.u = ou;
		area.v = ov;
		
		if(text != null) {
			GL11.glColor4d(color[0], color[1], color[2], color[3]);
			TextUtils.drawText(font, text, area.width / 2, 
				area.height / 2 - size / 2, size, 
				TrueTypeFont.ALIGN_CENTER);
			GL11.glColor4d(1, 1, 1, 1);
		}
	}

}
