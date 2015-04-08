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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.api.key.LIKeyProcess.Trigger;
import cn.liutils.cgui.gui.event.DragEvent;
import cn.liutils.cgui.gui.event.DrawEvent;
import cn.liutils.cgui.gui.event.KeyEvent;
import cn.liutils.cgui.gui.event.MouseDownEvent;
import cn.liutils.cgui.gui.property.PropWidget;
import cn.liutils.cgui.gui.property.PropWidget.AlignStyle;
import cn.liutils.core.event.eventhandler.LIFMLGameEventDispatcher;

/**
 * @author WeathFolD
 *
 */
public class LIGui implements Iterable<Widget> {
	
	double width, height; //Only useful when calculating 'CENTER' align preference

	private List<Widget> widgets = new LinkedList();
	
	//Counter for assigning ZOrder.
	private Map<Integer, Integer> zOrderProg = new HashMap();
	
	//Absolute mouse position.
	public double mouseX, mouseY;
	
	LIKeyProcess keyProcess;
	LIKeyProcess.Trigger trigger;
	
	WidgetNode focus; //last input focus

	public LIGui() {}
	
	public LIGui(double width, double height) {
		this.width = width;
		this.height = height;
	}
	
	protected void clear() {
		dispose();
		widgets.clear();
		zOrderProg.clear();
		focus = null;
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
			for(Widget widget : widgets) {
				if(widget.propWidget().align == AlignStyle.CENTER)
					updateNode(widget.node);
			}
		}
	}
	
	//---Widget handling
	/**
	 * Add the constructed Widget into the LIGui.
	 * @param widget
	 */
	public void addWidget(Widget widget) {
		if(widget.gui != null && widget.gui != this) {
			throw new RuntimeException("Fatal: Trying to add widget " + widget + " into multiple GUIs");
		}
		widget.gui = this;
		updateWidget(widget);
		
		widgets.add(widget);
		Collections.sort(widgets);
		widget.onAdded();
	}
	
	//---Events
	/**
	 * Go down the hierarchy tree and draw each widget node.
	 */
	public void draw(double mx, double my) {
		frameUpdate();
		updateMouse(mx, my);
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
		drawTraverse(mx, my, null, this, getTopNode(mx, my));
		
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	public void dispose() {
		if(trigger != null) trigger.setDead();
	}
	
	static final long DRAG_TIME_TOLE = 100;
	long lastStartTime, lastDragTime;
	WidgetNode draggingNode;
	double xOffset, yOffset;
	/**
	 * Standard GUI class callback.
	 * @param mx
	 * @param my
	 * @param btn the mouse button ID.
	 * @param dt how long is this button being pressed(ms)
	 */
    public void mouseClickMove(int mx, int my, int btn, long dt) {
    	updateMouse(mx, my);
    	if(btn == 0) {
    		long time = Minecraft.getSystemTime();
        	if(Math.abs(time - dt - lastStartTime) > DRAG_TIME_TOLE) {
        		lastStartTime = time;
        		draggingNode = getTopNode(mx, my);
        		//System.out.println(draggingNode);
        		if(draggingNode == null)
        			return;
        		xOffset = mx - draggingNode.x;
        		yOffset = my - draggingNode.y;
        	}
        	if(draggingNode != null) {
        		final double PIXEL_PER_MS = 5;
        		long dtt = time - lastDragTime;
        		
        	    lastDragTime = time;
        	    
        	    double dx = (mx - draggingNode.x - xOffset) / draggingNode.scale,
        	    	dy = (my - draggingNode.y - yOffset) / draggingNode.scale;
        		draggingNode.widget.postEvent(new DragEvent(
        				Math.signum(dx) * Math.min(dtt * PIXEL_PER_MS, Math.abs(dx)), 
        				Math.signum(dy) * Math.min(dtt * PIXEL_PER_MS, Math.abs(dy))));
        	}
    	}
    }
    
    public void updateDragWidget() {
    	if(draggingNode != null) {
    		Widget w = draggingNode.widget;
    		PropWidget p = draggingNode.widget.propWidget();
    		draggingNode.x = mouseX - xOffset;
    		draggingNode.y = mouseY - yOffset;
    		//Reversed calc. TODO: Maybe need to wrap this up
    		if(w.isWidgetParent()) {
    			Widget pw = w.parent;
    			WidgetNode pn = pw.node;
    			p.x = (draggingNode.x - pn.x) / draggingNode.scale;
    			p.y = (draggingNode.y - pn.y) / draggingNode.scale;
    		} else {
    			double x0, y0;
    			if(p.align == AlignStyle.CENTER) {
    				x0 = width / 2;
    				y0 = height / 2;
    			} else {
    				x0 = y0 = 0;
    			}
    			p.x = (draggingNode.x - x0) / draggingNode.scale;
    			p.y = (draggingNode.y - y0) / draggingNode.scale;
    		}
    		draggingNode.widget.dirty = true;
    	}
    }
    
    public Widget getDraggingWidget() {
        return Math.abs(Minecraft.getSystemTime() - lastDragTime) > DRAG_TIME_TOLE || draggingNode == null ? null : draggingNode.widget;
    }
    
    /**
	 * Standard GUI class callback.
	 * @param mx
	 * @param my
	 * @param btn the mouse button ID.
	 */
	public void mouseClicked(int mx, int my, int bid) {
		updateMouse(mx, my);
		if(bid == 0) {
			WidgetNode node = getTopNode(mx, my);
			if(node != null) {
				if(node.widget.doesNeedFocus()) {
					focus = node;
				} else {
					focus = null;
				}
				node.widget.postEvent(new MouseDownEvent((mx - node.x) / node.scale, (my - node.y) / node.scale));
			} else {
				focus = null;
			}
		}
	}
	
	public void keyTyped(char ch, int key) {
		if(focus != null) {
			focus.widget.postEvent(new KeyEvent(ch, key));
		}
	}
	
	public Widget getFocus() {
		return focus == null ? null : focus.widget;
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
	/**
	 * Recalculate node's absolute position and offset, provided that its parent's data is correct.
	 * Also double-check all its sub widgets' data.
	 */
	private void updateNode(WidgetNode node) {
		PropWidget p = node.widget.propWidget();
		if(node.widget.isWidgetParent()) {
			WidgetNode parent = node.parent();
			node.scale = parent.scale * p.scale;
			node.x = parent.x + p.x * node.scale;
			node.y = parent.y + p.y * node.scale;
		} else {
			node.scale = p.scale;

			double x0 = 0, y0 = 0;
			switch(p.align) {
			case CENTER:
				x0 = (width - p.width * node.scale) / 2;
				y0 = (height - p.height * node.scale) / 2;
				break;
			case LEFT:
				x0 = p.x * node.scale;
				y0 = p.y * node.scale;
				break;
			}
			node.x = Math.max(0, x0);
			node.y = Math.max(0, y0);
		}
		node.widget.dirty = false;
		
		//Check sub widgets
		for(Widget w : node.widget.subWidgets) {
			System.out.println("updateWidget " + w);
			updateWidget(w);
		}
	}
	
	private void updateWidget(Widget widget) {
		if(widget.node == null) {
			widget.node = new WidgetNode(widget);
			updateOrder(widget);
		}
		updateNode(widget.node);
	}
	
	/**
	 * Generic checking.
	 */
	private void frameUpdate() {
		updateTraverse(null, this);
	}
	
	private void updateTraverse(WidgetNode cur, Iterable<Widget> set) {
		if(cur != null) {
			if(cur.widget.dirty) {
				this.updateNode(cur);
			}
		}
		
		Iterator<Widget> iter = set.iterator();
		while(iter.hasNext()) {
			Widget widget = iter.next();
			if(widget.disposed) {
				iter.remove();
			} else {
				updateTraverse(widget.node, widget.node);
			}
		}
	}
	
	private void updateOrder(Widget widget) {
		//Assign z order
		int prio = widget.getDrawPriority();
		Integer prog = zOrderProg.get(prio);
		if(prog == null) prog = 0;
		widget.node.zOrder = prio * 100 + prog;
		zOrderProg.put(prio, (prog + 1) % 100);
	}
	
	private void updateMouse(double mx, double my) {
		this.mouseX = mx;
		this.mouseY = my;
	}
	
	public WidgetNode getTopNode(double x, double y) {
		return gtnTraverse(x, y, null, this);
	}
	
	private void drawTraverse(double mx, double my, WidgetNode cur, Iterable<Widget> set, WidgetNode top) {
		//System.out.println("drawTrav " + cur);
		
		if(cur != null && cur.widget.doesDraw) {
			GL11.glPushMatrix();
			GL11.glTranslated(cur.x, cur.y, 0);
			GL11.glScaled(cur.scale, cur.scale, 1);
			GL11.glColor4d(1, 1, 1, 1); //Force restore color for any widget
			cur.widget.postEvent(new DrawEvent((mx - cur.x) / cur.scale, (my - cur.y) / cur.scale, cur == top));
			GL11.glPopMatrix();
		}
		
		if(cur == null || cur.widget.doesDraw) {
			Iterator<Widget> iter = set.iterator();
			while(iter.hasNext()) {
				Widget wn = iter.next();
				drawTraverse(mx, my, wn.node, wn.node, top);
			}
		}
	}
	
	private WidgetNode gtnTraverse(double x, double y, WidgetNode node, Iterable<Widget> set) {
		WidgetNode res = null;
		boolean sub = node == null || (node.widget.doesDraw && node.widget.doesListenKey);
		if(sub && node != null && node.pointWithin(x, y)) {
			res = node;
		}
		
		if(!sub) return res;
		
		WidgetNode next = null;
		for(Widget wn : set) {
			WidgetNode tmp = gtnTraverse(x, y, wn.node, wn.node);
			if(tmp != null)
				next = tmp;
		}
		return next == null ? res : next;
	}
	
	public class WidgetNode implements Comparable<WidgetNode>, Iterable<Widget> {
		public final Widget widget;
		
		public double x, y;
		public double scale;
		public int zOrder;

		public WidgetNode(Widget wig) {
			widget = wig;
		}
		
		public WidgetNode parent() {
			Widget wx = widget.getWidgetParent();
			return wx == null ? null : wx.node;
		}
		
		public boolean pointWithin(double mx, double my) {
			double x1 = x + widget.propWidget().width * scale, y1 = y + widget.propWidget().height * scale;
			return (x <= mx && mx <= x1) && (y <= my && my <= y1);
		}

		@Override
		public int compareTo(WidgetNode other) {
			return zOrder - other.zOrder;
		}

		@Override
		public Iterator<Widget> iterator() {
			return widget.subWidgets.iterator();
		}
		
	}

	@Override
	public Iterator<Widget> iterator() {
		return widgets.iterator();
	}
}
