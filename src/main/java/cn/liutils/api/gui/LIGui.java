/**
 * 
 */
package cn.liutils.api.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.gui.Widget.Alignment;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.api.key.LIKeyProcess.Trigger;
import cn.liutils.core.event.eventhandler.LIFMLGameEventDispatcher;
import cn.liutils.util.DebugUtils;
import cn.liutils.util.GenericUtils;

/**
 * Handler of Widget and event receiver of Minecraft GUIs.
 * In charge of renderering and event delegation job.
 * A LIKeyProcess class has wrapped in for special key and mouse listening purpose.
 * @author WeathFolD
 */
public class LIGui implements Iterable<Widget> {

	private long TIME_TOLERANCE = 100L;
	
	protected List<Widget> widgets = new ArrayList<Widget>();
	protected Set<Widget> toAdd = new HashSet<Widget>();
	private Map<Integer, Integer> zOrderProg = new HashMap<Integer, Integer>();
	
	private int width, height;
	protected final GuiScreen parent;
	
	LIKeyProcess keyProcess;
	LIKeyProcess.Trigger trigger;
	
	public double mouseX, mouseY;
	
	boolean iterating;
	
	public LIGui(GuiScreen screen) {
		parent = screen;
	}
	
	/**
	 * Event delegation from MCGUI
	 */
	public void drawElements(int mx, int my) {
		for(Widget w : toAdd) {
			this.addWidget(w);
		}
		toAdd.clear();
		checkTraverse(null, this);
		
		mouseX = mx;
		mouseY = my;
		iterating = true;
		//System.out.println("--------");
		drawAndTraverse(mx, my, null, this);
		//System.out.println("--------");
		iterating = false;
	}
	
	private void checkTraverse(Widget w, Iterable<Widget> it) {
		if(w != null) 
			w.checkUpdate();
		for(Widget w2 : it) {
			checkTraverse(w2, w2.getSubWidgets());
		}
	}
	
	private void drawAndTraverse(double mx, double my, Widget w, Iterable<Widget> it) {
		update();
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		//draw
		if(w != null && w.visible && w.draw) {
			GL11.glPushMatrix(); {
				GL11.glTranslated(w.wcoord.absX, w.wcoord.absY, 0);
				w.draw(mx - w.wcoord.absX, my - w.wcoord.absY, w == this.getTopmostElement(mx, my)); //QUESTION: Maybe we can optimize this?
			} GL11.glPopMatrix();
		}
		if(w != null && !w.visible)
			return;
		Iterator<Widget> iter = it.iterator();
		while(iter.hasNext()) {
			Widget sub = iter.next();
			sub.iterating = true;
			//System.out.println((w == null ? "parent": sub.ID) + " {");
			drawAndTraverse(mx, my, sub, sub);
			//System.out.println("}");
			sub.iterating = false;
			//Check if disposed and remove
			if(sub.disposed) {
				iter.remove();
			}
		}
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	public void mouseClicked(int mx, int my, int bid) {
		mouseX = mx;
		mouseY = my;
		update();
		if(bid == 0) {
			Widget w = getTopmostElement(mx, my);
			if(w != null) {
				w.onMouseDown(mx - w.wcoord.absX, my - w.wcoord.absY);
			}
		}
	}
	
	private Widget getTopmostElement(double mx, double my) {
		return getTopmostElement(mx, my, null, this);
	}
	
	private Widget getTopmostElement(double mx, double my, Widget wc, Iterable<Widget> con) {
		Widget res = null;
		if(wc != null && wc.visible && wc.receiveEvent && wc.wcoord.coordWithin(mx, my)) {
			res = wc;
		}
		if(wc != null && !wc.visible) 
			return res;
		for(Widget t : con) { //Recurse
			Widget w = getTopmostElement(mx, my, t, t.getSubWidgets());
			//The array is sorted in zOrder increase so naturally we replace the res
			if(w != null && w.receiveEvent && w.wcoord.coordWithin(mx, my)) {
				res = w;
			}
		}
		return res;
	}
    
	long lastStartTime;
	Widget curDragging;
	double xOffset, yOffset;
    public void mouseClickMove(int mx, int my, int btn, long dt) {
    	mouseX = mx;
		mouseY = my;
    	if(btn == 0) {
    		update();
    		long time = Minecraft.getSystemTime();
        	if(Math.abs(time - dt - lastStartTime) > TIME_TOLERANCE) {
        		lastStartTime = time;
        		curDragging = getTopmostElement(mx, my);
        		if(curDragging == null)
        			return;
        		xOffset = mx - curDragging.wcoord.absX;
        		yOffset = my - curDragging.wcoord.absY;
        	}
        	if(curDragging != null)
        		curDragging.onMouseDrag(mx - curDragging.wcoord.absX - xOffset, my - curDragging.wcoord.absY - yOffset);
    	}
    }
    
    private void update() {
    	if(width != parent.width || height != parent.height) {
    		width = parent.width;
    		height = parent.height;
    		resizeTraverse(null, this);
    	}
    }
	
	protected WidgetCoord calcWidget(Widget w) {
		WidgetCoord cw = new WidgetCoord(w, 0, 0);
		Widget wp = w.getWidgetParent();
		if(wp == null) { //Screen as parent
			if(w.style == Alignment.CENTER) {
				double hw = width * 0.5, hh = height * 0.5;
				cw.absX = hw - w.area.width * 0.5 + w.area.x;
				cw.absY = hh - w.area.height * 0.5 + w.area.y;
			} else {
				cw.absX = w.area.x;
				cw.absY = w.area.y;
			}
		} else { //Widget as parent
			GenericUtils.assertObj(wp.wcoord);
			cw.absX = wp.wcoord.absX + w.area.x;
			cw.absY = wp.wcoord.absY + w.area.y;
		}
		w.wcoord = cw;
		return cw;
	}
	
	private void resizeTraverse(Widget w, Iterable<Widget> iter) {
		if(w != null) {
			calcWidget(w);
		}
		for(Widget s : iter) {
			resizeTraverse(s, s.getSubWidgets());
		}
	}
	
	public void updateWidgetPos(Widget wg) {
		resizeTraverse(wg, wg.getSubWidgets());
	}
	
	protected List<Widget> getWidgets() {
		return widgets;
	}
	
	/**
	 * Do NOT call this function. The widget automatically does the job
	 */
	final void addWidget(Widget c) {
		if(widgets.contains(c)) {
			throw new RuntimeException("ID Collision: " + c.ID);
		}
		if(iterating) {
			toAdd.add(c);
			return;
		}
		calcWidget(c);
		widgets.add(c);
		assignZOrder(c);
		Collections.sort(widgets);
	}
	
	public void addSubWidget(Widget c) {
		calcWidget(c);
		assignZOrder(c);
		Collections.sort(c.subWidgets);
	}
	
	public boolean isVisible(Widget w) {
		Widget cur = w;
		while(cur.getWidgetParent() != null) {
			//Iterate self and all the parents to check
			if(!cur.visible)
				return false;
			cur = cur.getWidgetParent();
		}
		return cur.visible;
	}
	
	public void removeWidget(Widget c) {
		widgets.remove(c);
	}
	
	private void assignZOrder(Widget c) {
		int prio = c.getDrawPriority();
		Integer i = zOrderProg.get(prio);
		if(i == null)
			i = 0;
		c.zOrder = prio * 100 + i; //Need guarantee: no more than 100 widgets per priority.(Definetly isnt it? wwwwww)a
		zOrderProg.put(prio, i + 1);
	}
	
	/**
	 * Baked position data, updated when widget explicitly requires or new widget is added.
	 * @author WeathFolD
	 */
    public class WidgetCoord implements Comparable {
		public Widget wig;
		public double absX, absY;
		public WidgetCoord(Widget w, double ax, double ay) {
			wig = w;
			absX = ax;
			absY = ay;
		}
		
		public boolean coordWithin(double x, double y) {
			return (absX <= x && x <= absX + wig.area.width) && (absY <= y && y <= absY + wig.area.height);
		}
		
		@Override
		public int compareTo(Object o) {
			if(!(o instanceof WidgetCoord)) return -1;
			return wig.compareTo(((WidgetCoord)o).wig);
		}
		
		@Override
		public String toString() {
			return "[WC " + DebugUtils.formatArray(absX, absY, wig.area.width, wig.area.height) + "]";
		}
	}
    
    /**
     * Called when this gui is not anymore useful(closed).
     */
    public void onDispose() {
    	if(trigger != null) { //remove KeyHandler from tickEvent
    		trigger.setDead();
    	}
    }
    
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

	@Override
	public Iterator<Widget> iterator() {
		return widgets.iterator();
	}
	
}
