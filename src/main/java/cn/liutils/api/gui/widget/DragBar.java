/**
 * 
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
		
		public GUIRect rect = new GUIRect();
		public AssignTexture tex;

		public Bar() {
			super(0, 0, DragBar.this.width, barHeight);
			this.drawer = new DrawObject();
			drawer.addHandler(rect);
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
	
	public void setProgress(double d) {
		progress = d;
		onProgressChanged();
	}
	
	public double getProgress() {
		return progress;
	}

	public Widget setTexMapping(double u, double v, double tw, double th) {
		bar.rect.setMappingBySize(u, v, tw, th);
		return this;
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
