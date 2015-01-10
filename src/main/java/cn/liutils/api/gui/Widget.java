/**
 * 
 */
package cn.liutils.api.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.gui.LIGui.WidgetCoord;
import cn.liutils.core.LIUtils;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * <code>Widget</code> describes an arbitrary GUI element specifying an area on screen.
 * Widget is registered and handled by a LIGui. You can specify either a LIGuiScreen or Widget as its parent.</br>
 * A widget can own multiple subWidgets. This enables page-like widget placement
 * (The coords specified serve as relative coordinate to its first parent).
 * <br/> The ID is the widget's universal identifier. Don't make it collide.
 * @author WeathFolD
 * @see cn.liutils.api.gui.LIGui
 */
public class Widget implements Comparable<Widget>, Iterable<Widget> {

	public enum Alignment { TOPLEFT, CENTER };
	Alignment style = Alignment.CENTER;
	
	public final LIGui screen; //Uppermost screen parent.
	private Widget parent; //Last parent, maybe null
	
	public final String ID; //THE universal identifier.
	
	final DrawArea area; //Drawing area.
	protected int zOrder; //The zOrder automatically assigned by LIGuiScreen.
	
	public boolean visible = true; //If this widget appears in draw and judgement at all
	public boolean receiveEvent = true; //Whether this widget receives events
	public boolean draw = false;
	
	protected ResourceLocation texture; //Texture to be automatically bind, if any
	protected int texWidth, texHeight; //Texture resolution, if specified
	
	protected WidgetCoord wcoord; //Calculated by LIGuiScreen for drawing
	
	double scale = 1.0;
	
	//Internal states
	List<Widget> subWidgets = new ArrayList<Widget>();
	Set<Widget> widgetToAdd = new HashSet<Widget>();
	boolean disposed;
	boolean iterating;
	
	public Widget(String id, Widget par, double x, double y) {
		this(id, par, x, y, 0, 0);
	}
	
	public Widget(String id, Widget par, double x, double y, double w, double h) {
		screen = par.getScreen();
		parent = par;
		area = new DrawArea(x, y, w, h);
		ID = id;
		par.addChild(this);
	}
	
	public Widget(String id, LIGui scr, double x, double y) {
		this(id, scr, x, y, 0, 0);
	}
	
	public Widget(String id, LIGui scr, double x, double y, double w, double h) {
		screen = scr;
		parent = null;
		area = new DrawArea(x, y, w, h);
		ID = id;
		scr.addWidget(this);
	}
	
	public Widget setTexMapping(double u, double v, double tw, double th) {
		area.setTexMapping(u, v, tw, th);
		draw = true;
		return this;
	}
	
	public Widget setTexture(ResourceLocation tex, int width, int height) {
		texture = tex;
		texWidth = width;
		texHeight = height;
		draw = true;
		return this;
	}
	
	/**
	 * Define the widget offset behavior. Only applies when father is the screen.
	 */
	public void setAlignStyle(Alignment align) {
		style = align;
	}
	
	/**
	 * Get the last widget parent, maybe null.
	 */
	public Widget getWidgetParent() {
		return parent;
	}
	
	/**
	 * Get the screen this widget belongs to.
	 */
	public LIGui getScreen() {
		return screen;
	}
	
	public List<Widget> getSubWidgets() {
		return subWidgets;
	}
	
	public void dispose() {
		disposed = true;
	}
	
	public boolean hasDisposed() {
		return disposed;
	}
	
	/**
	 * Return the drawing priority, typically 1-10. Widgets with higher drawing priority are bound to be rendered first, 
	 * while drawing order of the same priority is automatically assigned.
	 */
	public int getDrawPriority() {
		return 1;
	}
	
	public DrawArea getRawArea() {
		return area;
	}
	
	public DrawArea getArea() {
		return wcoord.da;
	}
	
	public void setScale(double s) {
		scale = s;
		screen.updateWidgetPos(this);
	}
	
	/**
	 * Draw the widget!
	 * @param mx mouse posX (Widget origin)
	 * @param my mouse posY (Widget origin)
	 * @param mouseHovering true if the mouse is on the widget
	 */
	public void draw(double mx, double my, boolean mouseHovering) {
		GL11.glEnable(GL11.GL_BLEND);
		if(texture != null) {
			RenderUtils.loadTexture(texture);
		}
		if(texWidth != 0 && texHeight != 0) {
			HudUtils.setTextureResolution(texWidth, texHeight);
		}
		GL11.glColor4d(1, 1, 1, 1);
		getArea().draw();
	}
	
	//---------EVENT RECEIVERS---------
	
	/**
	 * Called when the mouse button is clicked within the widget area.
	 * @param mx widget-coordinate mouseX
	 * @param my widget-coordinate mouseY
	 */
	public void onMouseDown(double mx, double my) {}
	
	/**
	 * Called when the mouse button is clicked within the widget area.
	 * @param mx widget-coordinate mouseX
	 * @param my widget-coordinate mouseY
	 */
	public void onMouseUp(double mx, double my) {}
	
	/**
	 * Called when the mouse peforms a 'drag' action(Starting in this widget's area)
	 * @param x1 widget-coordinate endX
	 * @param y1 widget-coordinate endY
	 */
	public void onMouseDrag(double x0, double y0) {}
	
	//---------INTERNAL----------
	/**
	 * Called when widget is created, usually not called by itself.
	 */
	protected void addChild(Widget child) {
		if(iterating) {
			this.widgetToAdd.add(child);
			return;
		}
		for(Widget w : subWidgets) {
			if(w.ID.equals(child.ID)) {
				throw new RuntimeException("Widget ID collision: " + w.ID);
			}
		}
		subWidgets.add(child);
		screen.addSubWidget(child);
		Collections.sort(subWidgets);
	}
	
	final void checkUpdate() {
		for(Widget w : widgetToAdd) {
			this.addChild(w);
		}
		widgetToAdd.clear();
	}
	
	@Override
	public int compareTo(Widget o) {
		return this.zOrder > o.zOrder ? 1 : this.zOrder == o.zOrder ? 0 : -1;
	}
	
	@Override
	public boolean equals(Object w) {
		if(!(w instanceof Widget))
			return false;
		return ((Widget)w).ID.equals(ID);
	}
	
	@Override
	public int hashCode() {
		return ID.hashCode();
	}

	@Override
	public Iterator<Widget> iterator() {
		return subWidgets.iterator();
	}
	
	@Override
	public String toString() {
		return "[WID:" + ID + "]";
	}
	
}
