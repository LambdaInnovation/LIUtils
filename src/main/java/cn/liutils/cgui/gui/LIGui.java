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
package cn.liutils.cgui.gui;

import java.util.Iterator;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.api.key.LIKeyProcess.Trigger;
import cn.liutils.cgui.gui.event.DragEvent;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.GainFocusEvent;
import cn.liutils.cgui.gui.event.KeyEvent;
import cn.liutils.cgui.gui.event.LostFocusEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.event.RefreshEvent;
import cn.liutils.cgui.gui.property.PropBasic;
import cn.liutils.cgui.gui.property.PropBasic.AlignStyle;
import cn.liutils.core.event.eventhandler.LIFMLGameEventDispatcher;
import cn.liutils.util.HudUtils;
import cn.liutils.util.RenderUtils;

/**
 * @author WeathFolD
 */
public class LIGui extends WidgetContainer {
	
	double width, height; //Only useful when calculating 'CENTER' align preference
	
	//Absolute mouse position.
	public double mouseX, mouseY;
	
	LIKeyProcess keyProcess;
	LIKeyProcess.Trigger trigger;
	
	Widget focus; //last input focus

	public LIGui() {}
	
	public LIGui(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	public void dispose() {
		if(trigger != null) trigger.setDead();
	}
	
	/**
	 * Called when screen is being resized.
	 * @param w new width
	 * @param h new height
	 */
	public void resize(double w, double h) {
		boolean diff = width != w || height != h;
		
		this.width = w;
		this.height = h;
		
		if(diff) {
			for(Widget widget : this) {
				if(widget.propBasic().align == AlignStyle.CENTER)
					widget.dirty = true;
			}
		}
	}
	
	//---Event callback---
	/**
	 * Go down the hierarchy tree and draw each widget node.
	 */
	public void draw(double mx, double my) {
		frameUpdate();
		updateMouse(mx, my);
		//System.out.println("draw " + this);
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
		drawTraverse(mx, my, null, this, getTopWidget(mx, my));
		
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	static final long DRAG_TIME_TOLE = 100;
	long lastStartTime, lastDragTime;
	Widget draggingNode;
	double xOffset, yOffset;
	/**
	 * Standard GUI class callback.
	 * @param mx
	 * @param my
	 * @param btn the mouse button ID.
	 * @param dt how long is this button being pressed(ms)
	 */
    public boolean mouseClickMove(int mx, int my, int btn, long dt) {
    	updateMouse(mx, my);
    	if(btn == 0) {
    		long time = Minecraft.getSystemTime();
        	if(Math.abs(time - dt - lastStartTime) > DRAG_TIME_TOLE) {
        		lastStartTime = time;
        		draggingNode = getTopWidget(mx, my);
        		if(draggingNode == null)
        			return false;
        		xOffset = mx - draggingNode.x;
        		yOffset = my - draggingNode.y;
        	}
        	if(draggingNode != null) {
        	    lastDragTime = time;
        		draggingNode.postEvent(new DragEvent());
        		return true;
        	}
    	}
    	return false;
    }
    
    /**
	 * Standard GUI mouseClicked callback.
	 * @param mx
	 * @param my
	 * @param btn the mouse button ID.
	 * @return if any action was performed on a widget.
	 */
	public boolean mouseClicked(int mx, int my, int bid) {
		updateMouse(mx, my);
		if(bid == 0) {
			Widget node = getTopWidget(mx, my);
			System.out.println(node);
			if(node != null) {
				if(node.propBasic().needFocus) {
					gainFocus(node);
					System.out.println("focused " + node);
				} else {
					removeFocus();
				}
				node.postEvent(new MouseDownEvent((mx - node.x) / node.scale, (my - node.y) / node.scale));
				//TODO: Need further filtering
				return true;
			} else {
				removeFocus();
			}
		}
		return false;
	}
	
	public void removeFocus() {
		if(focus != null) {
			focus.postEvent(new LostFocusEvent());
			focus = null;
		}
	}
	
	/**
	 * Gain a widget's focus with force.
	 */
	public void gainFocus(Widget node) {
		if(node == focus)
			return;
		if(focus != null) {
			removeFocus();
		}
		focus = node;
		focus.postEvent(new GainFocusEvent());
	}
	
	public void keyTyped(char ch, int key) {
		if(focus != null) {
			focus.postEvent(new KeyEvent(ch, key));
		}
	}
    
    //---Helper Methods---
	public Widget getTopWidget(double x, double y) {
		return gtnTraverse(x, y, null, this);
	}
	
    public void updateDragWidget() {
    	if(draggingNode != null) {
    		moveWidgetToAbsPos(draggingNode, mouseX - xOffset, mouseY - yOffset);
    	}
    }
    
    /**
     * Inverse calculation. Move this widget to the ABSOLUTE window position (tx, ty).
     * Note that the widget's position may be further changed because of its parent widget's position change.
     */
    public void moveWidgetToAbsPos(Widget w, double tx, double ty) {
    	PropBasic p = w.propBasic();
		w.x = tx;
		w.y = ty;
		//Reversed calc to update propWidget.
		if(w.isWidgetParent()) {
			Widget pw = w.parent;
			p.x = (w.x - pw.x) / w.scale;
			p.y = (w.y - pw.y) / w.scale;
		} else {
			double x0, y0;
			if(p.align == AlignStyle.CENTER) {
				x0 = width / 2;
				y0 = height / 2;
			} else {
				x0 = y0 = 0;
			}
			p.x = (w.x - x0) / w.scale;
			p.y = (w.y - y0) / w.scale;
		}
		w.dirty = true;
    }
    
    public Widget getDraggingWidget() {
        return Math.abs(Minecraft.getSystemTime() - lastDragTime) > DRAG_TIME_TOLE || draggingNode == null ? null : draggingNode;
    }
	
	public Widget getFocus() {
		return focus;
	}
	
	//---Key Handling
    /**
     * Add a key event listener within the lifetime of the GUI.
     */
    public void addKeyHandler(String name, int keyCode, boolean isRep, IKeyHandler ikh) {
    	if(keyProcess == null) { //lazy loading
    		keyProcess = new LIKeyProcess();
    		keyProcess.mouseOverride = false;
    		trigger = new Trigger(keyProcess);
    		LIFMLGameEventDispatcher.INSTANCE.registerClientTick(trigger);
    	}
    	keyProcess.addKey(name, keyCode, isRep, ikh);
    }
	
	//---Internal Processing
	private void updateWidget(Widget widget) {
		widget.gui = this;
		
		PropBasic p = widget.propBasic();
		if(widget.isWidgetParent()) {
			Widget parent = widget.parent;
			widget.scale = parent.scale * p.scale;
			widget.x = parent.x + p.x * widget.scale;
			widget.y = parent.y + p.y * widget.scale;
		} else {
			widget.scale = p.scale;

			double x0 = 0, y0 = 0;
			switch(p.align) {
			case CENTER:
				x0 = (width - p.width * widget.scale) / 2;
				y0 = (height - p.height * widget.scale) / 2;
				break;
			case LEFT:
				x0 = p.x * widget.scale;
				y0 = p.y * widget.scale;
				break;
			}
			widget.x = Math.max(0, x0);
			widget.y = Math.max(0, y0);
		}
		widget.dirty = false;
		
		//Check sub widgets
		for(Widget w : widget) {
			updateWidget(w);
		}
	}
	
	/**
	 * Generic checking.
	 */
	private void frameUpdate() {
		updateTraverse(null, this);
		this.update();
	}
	
	private void updateTraverse(Widget cur, WidgetContainer set) {
		if(cur != null) {
			if(cur.dirty) {
				cur.postEvent(new RefreshEvent());
				this.updateWidget(cur);
				System.out.println("Ref " + cur);
			}
		}
		
		Iterator<Widget> iter = set.iterator();
		while(iter.hasNext()) {
			Widget widget = iter.next();
			if(!widget.disposed) {
				updateTraverse(widget, widget);
				widget.update();
			}
		}
	}
	
	private void updateMouse(double mx, double my) {
		this.mouseX = mx;
		this.mouseY = my;
	}
	
	private void drawTraverse(double mx, double my, Widget cur, WidgetContainer set, Widget top) {
		if(cur != null && cur.propBasic().doesDraw) {
			GL11.glPushMatrix();
			GL11.glTranslated(cur.x, cur.y, 0);
			GL11.glScaled(cur.scale, cur.scale, 1);
			GL11.glColor4d(1, 1, 1, 1); //Force restore color for any widget
			cur.postEvent(new DrawEvent((mx - cur.x) / cur.scale, (my - cur.y) / cur.scale, cur == top));
			//System.out.println("drawing " + cur);
			GL11.glPopMatrix();
		}
		
		if(cur == null || cur.propBasic().doesDraw) {
			Iterator<Widget> iter = set.iterator();
			while(iter.hasNext()) {
				Widget wn = iter.next();
				drawTraverse(mx, my, wn, wn, top);
			}
		}
	}
	
	private Widget gtnTraverse(double x, double y, Widget node, WidgetContainer set) {
		Widget res = null;
		boolean sub = node == null || (node.propBasic().doesDraw && node.propBasic().doesListenKey);
		if(sub && node != null && node.isPointWithin(x, y)) {
			res = node;
		}
		
		if(!sub) return res;
		
		Widget next = null;
		for(Widget wn : set) {
			Widget tmp = gtnTraverse(x, y, wn, wn);
			if(tmp != null)
				next = tmp;
		}
		return next == null ? res : next;
	}

	@Override
	void onWidgetAdded(String name, Widget w) {
		w.gui = this;
		updateWidget(w);
	}
	
	public static void drawBlackout() {
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
