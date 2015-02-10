/**
 * 
 */
package cn.liutils.api.gui;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;
import cn.liutils.api.draw.prop.AssignTexture;
import cn.liutils.api.draw.tess.GUIRect;
import cn.liutils.api.gui.LIGui.WidgetNode;


/**
 * @author WeathFolD
 *
 */
public class Widget {
	
	public enum AlignStyle { LEFT, CENTER };
	
	public AlignStyle alignStyle = AlignStyle.LEFT; //Align style. Only applicable when 
	
	public double posX, posY; // relative position to its first parent
	public double width, height; // size
	public double scale = 1.0;
	
	public boolean 
		doesDraw = true, 
		doesListenKey = true;
	public boolean disposed = false;
	
	public DrawObject drawer;

	LIGui gui;
	Widget lastParent;
	
	WidgetNode node;
	
	public Widget() {}
	
	public Widget(double x, double y, double w, double h) {
		this.setPos(x, y);
		this.setSize(w, h);
	}
	
	public Widget(double w, double h) {
		this.setSize(w, h);
	}
	
	/**
	 * Called when added into a GUI.
	 */
	protected void onAdded() {
		
	}
	
	protected WidgetNode getNode() {
		return node;
	}
	
	public boolean initialized() {
		return gui != null;
	}
	
	public boolean isWidgetParent() {
		return lastParent != null;
	}
	
	public Widget getWidgetParent() {
		return lastParent;
	}
	
	public LIGui getGui() {
		return gui;
	}
	
	public void addWidget(Widget child) {
		gui.addSubWidget(this, child);
	}
	
	public void addWidgets(Widget ...wigs) {
		for(Widget w : wigs) {
			addWidget(w);
		}
	}
	
	public void setSize(double w, double h) {
		width = w;
		height = h;
	}
	
	public void setPos(double x, double y) {
		posX = x;
		posY = y;
	}
	
	protected List<WidgetNode> getSubNodes() {
		return node.getSubNodes();
	}
	
	/**
	 * Announce to LIGui to let it update this widget's position.
	 */
	public final void updatePos() {
		gui.updateNode(node);
	}
	
	public void setDrawer(DrawObject dor) {
		drawer = dor;
	}
	
	public void dispose() {
		disposed = true;
	}
	
	/**
	 * Get the relative drawing priority
	 * If two widgets share the same parent, the one with higher priority will be drawn first.
	 */
	public int getDrawPriority() {
		return 1;
	}
	
	//Draw Event
	/**
	 * @param mx mouse X coordinate, transformed to widget coord
	 * @param my mouse Y coordinate, transformed to widget coord
	 * @param hovering is the mouse on the widget?
	 */
	public void draw(double mx, double my, boolean hovering) {
		if(drawer != null) {
			drawer.draw();
		}
	}
	
	/**
	 * Init a built-in default drawer that draws the texture to the widget area.
	 * tex can be null, which means we don't explicitly bind texture
	 */
	public void initTexDraw(ResourceLocation tex, double u, double v, double tw, double th) {
		this.drawer = new DrawObject();
		final GUIRect rect = new GUIRect(width, height, u, v, tw, th);
		drawer.addHandler(rect);
		drawer.addHandler(new DrawHandler() { //Make the size consistent
			@Override public EnumSet<EventType> getEvents() {
				return EnumSet.of(EventType.PRE_TESS);
			}
			@Override public String getID() {
				return "size_adjust";
			}
			@Override
			public void onEvent(EventType event, DrawObject obj) {
				rect.setSize(width, height);
			}
		});
		if(tex != null) {
			drawer.addHandler(new AssignTexture(tex));
		}
	}
	
	public void addSetTexture(ResourceLocation tex) {
		assert(tex != null && drawer != null);
		DrawHandler dh = drawer.getHandler("texture");
		if(dh != null) {
			((AssignTexture)dh).set(tex);
		}
		else drawer.addHandler(new AssignTexture(tex));
	}
	
	public void setTexResolution(double w, double h) {
		DrawHandler dh = drawer.getHandler("texture");
		if(dh != null) {
			((AssignTexture)dh).setResolution(w, h);
		} else {
			AssignTexture adh = new AssignTexture(null);
			drawer.addHandler(adh);
			adh.setResolution(w, h);
		}
	}
	
	//Control Events
	/**
	 * Called when the mouse peforms a 'drag' action(Starting in this widget's area)
	 * @param x1 widget-coordinate endX
	 * @param y1 widget-coordinate endY
	 */
	public void onMouseDrag(double x, double y) {}
	
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
	 * Handle the key input. This will only be called when doesNeedFocus() returns true and this widget gains focus.
	 */
	public void handleKeyInput(char ch, int kid) {}
	
	/**
	 * Return whether this widget can be focused and receive keyboard input or not.
	 */
	public boolean doesNeedFocus() {
		return false;
	}
	
	public final boolean isFocused() {
		return getGui().focus == this.node;
	}

}
