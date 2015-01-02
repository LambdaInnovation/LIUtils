/**
 * 
 */
package cn.liutils.api.client.gui;

import cn.liutils.api.client.util.HudUtils;
import cn.liutils.api.util.DebugUtils;

/**
 * Defines a drawing area on the screen and the corresponding texture mapping.
 * The drawing is done with HudUtils#drawTexturedModalRect.
 * @author WeathFolD
 */
public class DrawArea {
	public double x, y, width, height; //Drawing rectangle
	public double u, v, tWidth, tHeight; //Texture rectangle
	
	public DrawArea() {}
	
	public DrawArea(double x, double y) {
		setDrawArea(x, y, 0, 0);
	}
	
	public DrawArea(double x, double y, double w, double h) {
		setDrawArea(x, y, w, h);
	}
	
	public DrawArea(double x, double y, double w, double h,
			double u, double v, double tw, double th) {
		setData(x, y, w, h, u, v, tw, th);
	}
	
	public void setTexMapping(double u, double v, double tw, double th) {
		this.u = u;
		this.v = v;
		this.tWidth = tw;
		this.tHeight = th;
	}
	
	public void setDrawArea(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}
	
	public void setData(double x, double y, double w, double h,
			double u, double v, double tw, double th) {
		setDrawArea(x, y, w, h);
		setTexMapping(u, v, tw, th);
	}
	
	public void draw() {
		HudUtils.drawTexturedModalRect(0, 0, u, v, width, height, tWidth, tHeight);
	}
	
	@Override
	public DrawArea clone() {
		return new DrawArea(x, y, width, height, u, v, tWidth, tHeight);
	}
}
