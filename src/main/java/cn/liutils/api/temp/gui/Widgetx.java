/**
 * 
 */
package cn.liutils.api.temp.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.gui.Widget;
import cn.liutils.api.temp.gui.LIGui.WidgetNode;


/**
 * @author WeathFolD
 *
 */
public class Widgetx {
	
	public enum AlignStyle { LEFT, CENTER };
	
	public AlignStyle alignStyle = AlignStyle.LEFT; //Align style. Only applicable when 
	
	public double posX, posY; // relative position to its first parent
	public double width, height; // size
	public double scale = 1.0;
	
	public boolean 
		doesDraw = true, 
		doesListenKey = true;
	public boolean disposed = false;
	
	protected DrawObject drawer;

	LIGui gui;
	Widgetx lastParent;
	
	WidgetNode node;
	
	public Widgetx() {}
	
	public boolean isWidgetParent() {
		return lastParent != null;
	}
	
	public Widgetx getWidgetParent() {
		return lastParent;
	}
	
	public LIGui getGui() {
		return gui;
	}
	
	public void addWidget(Widgetx child) {
	}
	
	/**
	 * @param mx mouse X coordinate, transformed to widget coord
	 * @param my mouse Y coordinate, transformed to widget coord
	 * @param hovering is the mouse on the widget?
	 */
	public void draw(double mx, double my, boolean hovering) {
		
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
	
	/**
	 * Get the relative drawing priority
	 * If two widgets share the same parent, the one with higher priority will be drawn first.
	 */
	public int getDrawPriority() {
		return 1;
	}
	
	//Control Events
	public void onMouseDrag(double x, double y) {}
	
	public void onMouseDown(double x, double y) {}

}
