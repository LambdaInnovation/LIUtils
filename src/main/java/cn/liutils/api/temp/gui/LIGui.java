package cn.liutils.api.temp.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import cn.liutils.api.gui.Widget;
import cn.liutils.api.temp.gui.Widgetx.AlignStyle;

/**
 * @author WeathFolD
 *
 */
public class LIGui implements Iterable<LIGui.WidgetNode> {
	
	double width, height; //Only useful when calculating 'CENTER' align preference

	private List<WidgetNode> widgets = new LinkedList();
	
	//If we are currently going through widgets list.
	boolean iterating = false;
	//Nodes that were added last frame during iteration to be added at next frame's beginning.
	private List<WidgetNode> nodesToAdd = new LinkedList(); 
	//Counter for assigning ZOrder.
	private Map<Integer, Integer> zOrderProg = new HashMap();

	public LIGui() {}
	
	public LIGui(double width, double height) {
		this.width = width;
		this.height = height;
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
			for(WidgetNode node : widgets) {
				if(node.widget.alignStyle == AlignStyle.CENTER)
					updateNode(node);
			}
		}
	}
	
	//---Widget handling
	/**
	 * Add the constructed Widget into the LIGui.
	 * @param widget
	 */
	public void addWidget(Widgetx widget) {
		if(widget.gui != null && widget.gui != this) {
			throw new RuntimeException("Fatal: Trying to add widget " + widget + " into multiple GUIs");
		}
		widget.gui = this;
		initWidget(widget);
		
		if(!iterating) {
			widgets.add(widget.node);
			Collections.sort(widgets);
		} else nodesToAdd.add(widget.node);
	}
	
	public void addSubWidget(Widgetx parent, Widgetx sub) {
		if(sub.getWidgetParent() != null && sub.getWidgetParent() != parent) {
			throw new RuntimeException("Fatal: Trying to add widget " + sub + " into multiple GUIs");
		}
		sub.gui = this;
		sub.lastParent = parent;
		initWidget(sub);
		
		if(!iterating) {
			parent.node.sub.add(sub.node); //.....
			Collections.sort(parent.node.sub);
		} else parent.node.toAdd.add(sub.node);
	}
	
	//---Events
	/**
	 * Go down the hierarchy tree and draw each widget node.
	 */
	public void draw(double mx, double my) {
		frameUpdate();
		iterating = true;
		drawTraverse(mx, my, null, this, getTopNode(mx, my));
		iterating = false;
	}
	
	static final long DRAG_TIME_TOLE = 100;
	long lastStartTime;
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
    	if(btn == 0) {
    		long time = Minecraft.getSystemTime();
        	if(Math.abs(time - dt - lastStartTime) > DRAG_TIME_TOLE) {
        		lastStartTime = time;
        		draggingNode = getTopNode(mx, my);
        		if(draggingNode == null)
        			return;
        		xOffset = mx - draggingNode.x;
        		yOffset = my - draggingNode.y;
        	}
        	if(draggingNode != null)
        		draggingNode.widget.onMouseDrag(
        			(mx - draggingNode.x - xOffset) / draggingNode.scale, 
        			(my - draggingNode.y - yOffset) / draggingNode.scale);
    	}
    }
    
    /**
	 * Standard GUI class callback.
	 * @param mx
	 * @param my
	 * @param btn the mouse button ID.
	 */
	public void mouseClicked(int mx, int my, int bid) {
		if(bid == 0) {
			WidgetNode node = getTopNode(mx, my);
			if(node != null) {
				node.widget.onMouseDown((mx - node.x) / node.scale, (my - node.y) / node.scale);
			}
		}
	}
	
	//---Internal Processing
	/**
	 * Recalculate node's absolute position and offset, provided that its parent's data is correct.
	 */
	void updateNode(WidgetNode node) {
		if(node.widget.isWidgetParent()) {
			WidgetNode parent = node.parent();
			node.scale = parent.scale * node.widget.scale;
			node.x = parent.x + node.x * node.scale;
			node.y = parent.y + node.y * node.scale;
		} else {
			node.scale = node.widget.scale;

			double x0 = 0, y0 = 0;
			switch(node.widget.alignStyle) {
			case CENTER:
				x0 = (width - node.widget.width * node.scale) / 2;
				y0 = (height - node.widget.height * node.scale) / 2;
				break;
			case LEFT:
				x0 = node.widget.posX * node.scale;
				y0 = node.widget.posY * node.scale;
				break;
			}
			node.x = Math.max(0, x0);
			node.y = Math.max(0, y0);
		}
	}
	
	void initWidget(Widgetx widget) {
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
		if(!this.nodesToAdd.isEmpty()) {
			widgets.addAll(nodesToAdd);
			nodesToAdd.clear();
			Collections.sort(widgets);
		}
	}
	
	private void updateTraverse(WidgetNode cur, Iterable<WidgetNode> set) {
		if(cur != null) {
			if(cur.toAdd != null && !cur.toAdd.isEmpty()) {
				cur.sub.addAll(cur.toAdd);
				cur.toAdd.clear();
				Collections.sort(cur.sub);
			}
		}
		Iterator<WidgetNode> iter = set.iterator();
		while(iter.hasNext()) {
			WidgetNode node = iter.next();
			if(node.widget.disposed) {
				iter.remove();
			} else {
				updateTraverse(node, node.getSubNodes());
			}
		}
	}
	
	private void updateOrder(Widgetx widget) {
		//Assign z order
		int prio = widget.getDrawPriority();
		int prog = zOrderProg.get(prio);
		widget.node.zOrder = prio * 100 + prog;
		zOrderProg.put(prio, (prog + 1) % 100);
	}
	
	private WidgetNode getTopNode(double x, double y) {
		return gtnTraverse(x, y, null, this);
	}
	
	private void drawTraverse(double mx, double my, WidgetNode cur, Iterable<WidgetNode> set, WidgetNode top) {
		if(cur != null && cur.widget.doesDraw) {
			cur.widget.draw((mx - cur.x) / cur.scale, (my - cur.y) / cur.scale, cur == top);
		}
		
		if(cur == null || cur.widget.doesDraw) {
			cur.iterating = true;
			Iterator<WidgetNode> iter = set.iterator();
			while(iter.hasNext()) {
				WidgetNode wn = iter.next();
				drawTraverse(mx, my, wn, wn.getSubNodes(), top);
			}
			cur.iterating = false;
		}
	}
	
	private WidgetNode gtnTraverse(double x, double y, WidgetNode node, Iterable<WidgetNode> set) {
		WidgetNode res = null;
		if(node != null && node.pointWithin(x, y)) {
			res = node;
		}
		
		WidgetNode next = null;
		for(WidgetNode wn : set) {
			WidgetNode tmp = gtnTraverse(x, y, wn, wn.getSubNodes());
			if(tmp != null)
				next = tmp;
		}
		return next == null ? res : next;
	}
	
	public class WidgetNode implements Comparable<WidgetNode>, Iterable<WidgetNode> {
		public final Widgetx widget;
		
		public double x, y;
		public double scale;
		public int zOrder;
		
		List<WidgetNode> sub;
		
		//Design same to LIGui main class
		boolean iterating = false;
		List<WidgetNode> toAdd;
		
		public WidgetNode(Widgetx wig) {
			widget = wig;
		}
		
		public boolean hasChild() {
			return sub != null && !sub.isEmpty();
		}
		
		public List<WidgetNode> getSubNodes() {
			return sub == null ? dummy : sub;
		}
		
		public WidgetNode parent() {
			Widgetx wx = widget.getWidgetParent();
			return wx == null ? null : wx.node;
		}
		
		public boolean pointWithin(double mx, double my) {
			double x1 = x + widget.width * scale, y1 = y + widget.height * scale;
			return (x <= mx && mx <= x1) && (y <= my && my <= y1);
		}

		@Override
		public int compareTo(WidgetNode other) {
			return zOrder - other.zOrder;
		}

		@Override
		public Iterator<WidgetNode> iterator() {
			return sub == null ? null : sub.iterator();
		}
		
	}

	@Override
	public Iterator<WidgetNode> iterator() {
		return widgets.iterator();
	}

	private static List<WidgetNode> dummy = new ArrayList();
}
