/**
 * 
 */
package cn.liutils.api.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.util.DebugUtils;
import cn.liutils.api.util.GenericUtils;

/**
 * Handler of Widget and event receiver of Minecraft GUIs.
 * In charge of renderering and event delegation job.
 * @author WeathFolD
 */
public class LIGui implements Iterable<Widget> {

	private long TIME_TOLERANCE = 100L;
	enum Alignment { TOPLEFT, CENTER };
	
	Alignment style = Alignment.CENTER;
	double posX, posY;
	
	protected List<Widget> widgets = new ArrayList<Widget>();
	private Map<Integer, Integer> zOrderProg = new HashMap<Integer, Integer>();
	
	private int width, height;
	protected final GuiScreen parent;
	
	public LIGui(GuiScreen screen, double x, double y) {
		setAlignStyle(Alignment.CENTER);
		posX = x;
		posY = y;
		parent = screen;
	}
	
	public LIGui(GuiScreen screen) {
		style = Alignment.TOPLEFT;
		parent = screen;
	}
	
	/**
	 * Event delegation from MCGUI
	 */
	public void drawElements(int mx, int my) {
		drawAndTraverse(mx, my, null, this);
	}
	
	private void drawAndTraverse(double mx, double my, Widget w, Iterable<Widget> it) {
		update();
		//draw
		if(w != null && w.visible) {
			GL11.glPushMatrix(); {
				GL11.glTranslated(w.wcoord.absX, w.wcoord.absY, 0);
				w.draw(mx, my, w.wcoord.coordWithin(mx, my));
			} GL11.glPopMatrix();
		}
		if(w != null && !w.visible)
			return;
		for(Widget sub : it)
			drawAndTraverse(mx, my, sub, sub);
	}
	
	public void mouseClicked(int mx, int my, int bid) {
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
		int zo = -1;
		Widget res = null;
		if(wc != null && wc.receiveEvent && wc.wcoord.coordWithin(mx, my)) {
			zo = wc.zOrder;
			res = wc;
		}
		if(wc != null && !wc.visible) 
			return res;
		for(Widget t : con) { //Recurse
			Widget w = getTopmostElement(mx, my, t, t.getSubWidgets());
			if(w != null && w.receiveEvent && w.wcoord.coordWithin(mx, my) && w.zOrder > zo) {
				zo = w.zOrder;
				res = w;
			}
		}
		return res;
	}
    
	long lastStartTime;
	Widget curDragging;
    protected void mouseClickMove(int mx, int my, int btn, long dt) {
    	if(btn == 0) {
    		update();
    		long time = Minecraft.getSystemTime();
        	if(Math.abs(time - dt - lastStartTime) > TIME_TOLERANCE) {
        		lastStartTime = time;
        		curDragging = getTopmostElement(mx, my);
        	}
        	if(curDragging != null)
        		curDragging.onMouseDrag(mx - curDragging.wcoord.absX, my - curDragging.wcoord.absY);
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
			if(style == Alignment.CENTER) {
				double hw = width * 0.5, hh = height * 0.5;
				cw.absX = hw - posX * 0.5 + w.area.x;
				cw.absY = hh - posY * 0.5 + w.area.y;
			} else {
				cw.absX = posX + w.area.x;
				cw.absY = posY + w.area.y;
			}
		} else { //Widget as parent
			GenericUtils.assertObj(wp.wcoord);
			cw.absX = wp.wcoord.absX + w.area.x;
			cw.absY = wp.wcoord.absY + w.area.y;
		}
		w.wcoord = cw;
		return cw;
	}
	
	public WidgetCoord getUppermostElement(double x, double y) {
		return gueTraverse(x, y, null, this, -1);
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
	
	private WidgetCoord gueTraverse(double x, double y, Widget w, Iterable<Widget> iter, int max) {
		WidgetCoord res = null;
		if(w != null && w.visible && w.receiveEvent && w.wcoord.coordWithin(x, y) && w.zOrder > max) {
			max = w.zOrder;
			res = w.wcoord;
		}
		for(Widget s : iter) {
			WidgetCoord tmp = gueTraverse(x, y, s, s, max);
			if(tmp != null) {
				max = tmp.wig.zOrder;
				res = tmp;
			}
		}
		return res;
	}
	
	/**
	 * Define the widget offset behavior.
	 */
	public void setAlignStyle(Alignment align) {
		style = align;
	}
	
	protected List<Widget> getWidgets() {
		return widgets;
	}
	
	/**
	 * Do not call this function the widget automatically does the job
	 */
	protected void addWidget(Widget c) {
		if(widgets.contains(c)) {
			throw new RuntimeException("ID Collision: " + c.ID);
		}
		calcWidget(c);
		widgets.add(c);
		assignZOrder(c);
		Collections.sort(widgets);
	}
	
	protected void addSubWidget(Widget c) {
		calcWidget(c);
		assignZOrder(c);
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
	}

	@Override
	public Iterator<Widget> iterator() {
		return widgets.iterator();
	}
	
}
