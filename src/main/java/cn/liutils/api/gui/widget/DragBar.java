/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.gui.widget;

import net.minecraft.util.ResourceLocation;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.prop.AssignTexture;
import cn.liutils.api.draw.tess.GUIRect;
import cn.liutils.api.gui.Widget;

/**
 * Textured progress bar render. You specify an area indicating the whole scrolling area, 
 * then specify the scroll bar's height.(It's width is determined by widget width).
 * The drawing area specified is no longer used in drawing the whole widget, but for drawing the scroll bar.
 * @author WeathFolD
 */
public class DragBar extends Widget {
	
	private class Bar extends Widget {
		
		public GUIRect rect;
		public AssignTexture tex;

		public Bar() {
			super(0, 0, DragBar.this.width, barHeight);
			this.initTexDraw(null, 0, 0, 0, 0);
			rect = (GUIRect) drawer.getHandler("rect_2d");
		}
		
		public void draw(double mx, double my, boolean mouseHovering) {
			this.posY = progress * (DragBar.this.height - barHeight);
			this.updatePos();
			super.draw(mx, my, mouseHovering);
		}
		
		public void onMouseDrag(double x0, double y0) {
			if(!enableDragging)
				return;
			double full = DragBar.this.height - barHeight;
			double y = Math.max(Math.min(posY + y0, full), 0) / full;
			setProgress(y);
			onProgressChanged();
		}
		
	}
	
	/**
	 * Relative progress (0.0->1.0)
	 */
	private double progress;
	
	protected double barHeight;
	Bar bar;
	
	public boolean enableDragging = false;

	/**
	 * @param sh The bar drawing height
	 */
	public DragBar(double x, double y, double w,
			double h, double sh) {
		super(x, y, w, h);
		barHeight = sh;
		bar = new Bar();
	}
	
	@Override
	public void onAdded() {
		addWidget(bar);
	}
	
	public void setProgress(double d) {
		progress = d;
		onProgressChanged();
	}
	
	public double getProgress() {
		return progress;
	}

	public Widget setTexMapping(double u, double v, double tw, double th) {
		bar.rect.setMapping(u, v, tw, th);
		return this;
	}
	
	public void addSetTexture(ResourceLocation tex) {
		bar.addSetTexture(tex);
	}
	
	@Override
	public void initTexDraw(ResourceLocation tex, double u, double v, double tw, double th) {
		setTexMapping(u, v, tw, th);
		addSetTexture(tex);
	}
	
	public Widget setTexture(ResourceLocation tex) {
		if(bar.tex == null) {
			bar.tex = new AssignTexture(tex);
			bar.drawer.addHandler(bar.tex);
		} else {
			bar.tex.texture = tex;
		}
		
		return this;
	}
	
	//---------------EVENT HANDLER-------------------
	public void onProgressChanged() {}

}
