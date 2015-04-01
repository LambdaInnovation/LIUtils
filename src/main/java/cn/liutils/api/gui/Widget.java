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
package cn.liutils.api.gui;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;
import cn.liutils.api.draw.prop.AssignTexture;
import cn.liutils.api.draw.tess.GUIRect;
import cn.liutils.api.gui.LIGui.WidgetNode;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;


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
	
	public Widget initTexDraw(ResourceLocation tex, double u, double v) {
	    return initTexDraw(tex, u, v, width, height);
	}
	
	/**
	 * Init a built-in default drawer that draws the texture to the widget area.
	 * tex can be null, which means we don't explicitly bind texture
	 */
	public Widget initTexDraw(ResourceLocation tex, double u, double v, double tw, double th) {
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
		return this;
	}
	
	public void addSetTexture(ResourceLocation tex) {
		assert(tex != null && drawer != null);
		DrawHandler dh = drawer.getHandler("texture");
		if(dh != null) {
			((AssignTexture)dh).set(tex);
		}
		else drawer.addHandler(new AssignTexture(tex));
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
	
	//Sandbox utils
	/**
	 * Should be called at this widget's draw() function.
	 */
	protected void drawBlackout() {
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GLU.gluOrtho2D(1, 0, 1, 0);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		
		GL11.glColor4d(0, 0, 0, 0.6);
		HudUtils.setZLevel(-1);
		HudUtils.drawModalRect(0, 0, 1, 1);
		
		GL11.glPopMatrix();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		RenderUtils.bindIdentity();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

}
