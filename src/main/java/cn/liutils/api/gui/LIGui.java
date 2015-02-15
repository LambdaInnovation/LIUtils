package cn.liutils.api.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cn.liutils.api.gui.Widget.AlignStyle;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.api.key.LIKeyProcess.Trigger;
import cn.liutils.core.event.eventhandler.LIFMLGameEventDispatcher;
import cn.liutils.util.DebugUtils;

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
	
	public double mouseX, mouseY;
	
	LIKeyProcess keyProcess;
	LIKeyProcess.Trigger trigger;
	
	WidgetNode focus; //last input focus

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
	public void addWidget(Widget widget) {
		if(widget.gui != null && widget.gui != this) {
			throw new RuntimeException("Fatal: Trying to add widget " + widget + " into multiple GUIs");
		}
		widget.gui = this;
		initWidget(widget);
		
		if(!iterating) {
			widgets.add(widget.node);
			Collections.sort(widgets);
		} else nodesToAdd.add(widget.node);
		widget.onAdded();
	}
	
	public void addSubWidget(Widget parent, Widget sub) {
		if(sub.getWidgetParent() != null && sub.getWidgetParent() != parent) {
			throw new RuntimeException("Fatal: Trying to add widget " + sub + " into multiple GUIs");
		}
		sub.gui = this;
		sub.lastParent = parent;
		initWidget(sub);
		
		if(!iterating) {
			parent.node.addSubNode(sub.node); //.....
			Collections.sort(parent.node.sub);
		} else parent.node.toAdd.add(sub.node);
		sub.onAdded();
	}
	
	public boolean isVisible(Widget wig) {
		do {
			if(!wig.doesDraw)
				return false;
		} while((wig = wig.getWidgetParent()) != null);
		return true;
	}
	
	//---Events
	/**
	 * Go down the hierarchy tree and draw each widget node.
	 */
	public void draw(double mx, double my) {
		frameUpdate();
		updateMouse(mx, my);
		iterating = true;
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
		drawTraverse(mx, my, null, this, getTopNode(mx, my));
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		iterating = false;
	}
	
	public void dispose() {
		if(trigger != null) trigger.setDead();
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
		updateMouse(mx, my);
		if(bid == 0) {
			WidgetNode node = getTopNode(mx, my);
			if(node != null) {
				if(node.widget.doesNeedFocus()) {
					focus = node;
				} else {
					focus = null;
				}
				node.widget.onMouseDown((mx - node.x) / node.scale, (my - node.y) / node.scale);
			} else {
				focus = null;
			}
		}
	}
	
	protected void keyTyped(char ch, int key) {
		if(focus != null) {
			focus.widget.handleKeyInput(ch, key);
		}
	}
	
	/**
	 * Quick alias for playing sound
	 * @param src
	 * @param volume
	 */
	public void playSound(ResourceLocation src, float volume) {
		Minecraft.getMinecraft().getSoundHandler().playSound(
			PositionedSoundRecord.func_147674_a(src, volume));
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
    		System.out.println("Loaded key handler");
    	}
    	keyProcess.addKey(name, keyCode, isRep, ikh);
    }
	
	//---Internal Processing
	/**
	 * Recalculate node's absolute position and offset, provided that its parent's data is correct.
	 */
	void updateNode(WidgetNode node) {
		if(node.widget.isWidgetParent()) {
			WidgetNode parent = node.parent();
			node.scale = parent.scale * node.widget.scale;
			node.x = parent.x + node.widget.posX * node.scale;
			node.y = parent.y + node.widget.posY * node.scale;
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
		
		for(WidgetNode wn : node) {
			updateNode(wn);
		}
	}
	
	void initWidget(Widget widget) {
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
		updateTraverse(null, this);
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
				updateTraverse(node, node);
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
	
	private void drawTraverse(double mx, double my, WidgetNode cur, Iterable<WidgetNode> set, WidgetNode top) {
		if(cur != null && cur.widget.doesDraw) {
			GL11.glPushMatrix();
			GL11.glTranslated(cur.x, cur.y, 0);
			GL11.glScaled(cur.scale, cur.scale, 1);
			//System.out.println(cur.widget + " " + DebugUtils.formatArray(cur.x, cur.y, cur.scale));
			cur.widget.draw((mx - cur.x) / cur.scale, (my - cur.y) / cur.scale, cur == top);
			GL11.glPopMatrix();
		}
		
		if(cur == null || cur.widget.doesDraw) {
			if(cur != null)
				cur.iterating = true;
			Iterator<WidgetNode> iter = set.iterator();
			while(iter.hasNext()) {
				WidgetNode wn = iter.next();
				drawTraverse(mx, my, wn, wn, top);
			}
			if(cur != null)
				cur.iterating = false;
		}
	}
	
	private WidgetNode gtnTraverse(double x, double y, WidgetNode node, Iterable<WidgetNode> set) {
		WidgetNode res = null;
		boolean sub = node == null || (node.widget.doesDraw && node.widget.doesListenKey);
		if(sub && node != null && node.pointWithin(x, y)) {
			res = node;
		}
		
		if(!sub) return res;
		
		WidgetNode next = null;
		for(WidgetNode wn : set) {
			WidgetNode tmp = gtnTraverse(x, y, wn, wn);
			if(tmp != null)
				next = tmp;
		}
		return next == null ? res : next;
	}
	
	public class WidgetNode implements Comparable<WidgetNode>, Iterable<WidgetNode> {
		public final Widget widget;
		
		public double x, y;
		public double scale;
		public int zOrder;
		
		private List<WidgetNode> sub = new LinkedList();
		
		//Design same to LIGui main class
		boolean iterating = false;
		List<WidgetNode> toAdd = new LinkedList();
		
		public WidgetNode(Widget wig) {
			widget = wig;
		}
		
		public boolean hasChild() {
			return sub != null && !sub.isEmpty();
		}
		
		public void addSubNode(WidgetNode wn) {
			sub.add(wn);
		}
		
		public List<WidgetNode> getSubNodes() {
			return sub;
		}
		
		public WidgetNode parent() {
			Widget wx = widget.getWidgetParent();
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
			return sub.iterator();
		}
		
	}

	@Override
	public Iterator<WidgetNode> iterator() {
		return widgets.iterator();
	}
}
