/**
 * 
 */
package cn.liutils.api.gui.widget;

import net.minecraft.util.ResourceLocation;
import cn.liutils.api.gui.LIGui;
import cn.liutils.api.gui.Widget;

/**
 * Textured progress bar render. You specify an area indicating the whole scrolling area, 
 * then specify the scroll bar's height.(It's width is determined by widget width).
 * The drawing area specified is no longer used in drawing the whole widget, but for drawing the scroll bar.
 * @author WeathFolD
 */
public class DragBar extends Widget {
	
	private class Bar extends Widget {

		public Bar() {
			super("bar", DragBar.this, 0, 0, DragBar.this.getRawArea().width, barHeight);
		}
		
		public void draw(double mx, double my, boolean mouseHovering) {
			this.getRawArea().y = progress * (DragBar.this.getRawArea().height - barHeight);
			screen.updateWidgetPos(this);
			super.draw(mx, my, mouseHovering);
		}
		
		public void onMouseDrag(double x0, double y0) {
			if(!enableDragging)
				return;
			double full = DragBar.this.getRawArea().height - barHeight;
			double y = Math.max(Math.min(getRawArea().y + y0, full), 0) / full;
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
	public DragBar(String id, Widget par, double x, double y, double w,
			double h, double sh) {
		super(id, par, x, y, w, h);
		barHeight = sh;
		bar = new Bar();
	}

	/**
	 * @param sh The bar drawing height
	 */
	public DragBar(String id, LIGui scr, double x, double y, double w,
			double h, double sh) {
		super(id, scr, x, y, w, h);
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
		bar.setTexMapping(u, v, tw, th);
		return this;
	}
	
	public Widget setTexture(ResourceLocation tex, int width, int height) {
		bar.setTexture(tex, width, height);
		return this;
	}
	
	//---------------EVENT HANDLER-------------------
	public void onProgressChanged() {}

}
