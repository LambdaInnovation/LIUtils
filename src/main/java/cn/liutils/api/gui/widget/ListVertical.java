/**
 * 
 */
package cn.liutils.api.gui.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.util.MathHelper;
import cn.liutils.api.gui.DrawArea;
import cn.liutils.api.gui.LIGui;
import cn.liutils.api.gui.Widget;
import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;

/**
 * @author WeathFolD
 *
 */
public class ListVertical extends Widget {
	
	int progress;
	int maxProgress;
	double perHeight;
	List<Widget> widgets = new ArrayList<Widget>(); //real list
	private DragBar bar;

	/**
	 * @param id
	 * @param par
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public ListVertical(String id, Widget par, double x, double y, double w,
			double h) {
		super(id, par, x, y, w, h);
		setup();
	}

	public ListVertical(String id, LIGui scr, double x, double y, double w,
			double h) {
		super(id, scr, x, y, w, h);
		setup();
	}
	
	public void setDragBar(DragBar db) {
		bar = db;
	}
	
	private void setup() {
		//add mouse wheel handlings
		screen.addKeyHandler("scr_up_" + ID, LIKeyProcess.MWHEELUP, false, new IKeyHandler() {
			@Override
			public void onKeyDown(int keyCode, boolean tickEnd) {
				if(screen.isVisible(ListVertical.this) && wcoord.coordWithin(screen.mouseX, screen.mouseY))
					progressLast();
			}
			@Override public void onKeyUp(int keyCode, boolean tickEnd) {}
			@Override public void onKeyTick(int keyCode, boolean tickEnd) {}
		});
		
		screen.addKeyHandler("scr_dn_" + ID, LIKeyProcess.MWHEELDOWN, false, new IKeyHandler() {
			@Override
			public void onKeyDown(int keyCode, boolean tickEnd) {
				if(screen.isVisible(ListVertical.this) && wcoord.coordWithin(screen.mouseX, screen.mouseY)) {
					progressNext();
				}
			}
			@Override public void onKeyUp(int keyCode, boolean tickEnd) {}
			@Override public void onKeyTick(int keyCode, boolean tickEnd) {}
		});
		
		this.draw = true;
	}
	
	public void addElements(Widget ...ws) {
		widgets.addAll(Arrays.asList(ws));
	}
	
	public void progressNext() {
		setProgress(progress + 1);
	}
	
	public void progressLast() {
		setProgress(progress - 1);
	}
	
	public void setProgress(int prog) {
		int np = Math.max(0, Math.min(maxProgress, prog));
		boolean changed = np != progress;
		progress = np;
		if(changed) {
			onProgressChanged();
		}
		updateList();
	}
	
	public int getProgress() {
		return progress;
	}
	
	public int getMaxProgress() {
		return maxProgress;
	}
	
	public double getRelativeProgress() {
		return (double)progress / maxProgress;
	}
	
	public void setByRelativeProgress(double d) {
		d *= maxProgress;
		setProgress((int) d);
	}
	
	private void updateList() {
		List<Widget> handles = this.getSubWidgets();
		handles.clear();
		int n = getMaxShow() + progress;
		for(int i = progress; i < n && i < widgets.size(); ++i) {
			Widget w = widgets.get(i);
			DrawArea da = w.getArea();
			da.x = 0;
			da.y = (i - progress) * perHeight;
			w.visible = true;
			handles.add(w);
			screen.updateWidgetPos(w);
		}
	}
	
	public void draw(double a, double b, boolean c) {}
	
	private int getMaxShow() {
		return perHeight == 0 ? 0 : MathHelper.floor_double(getArea().height / perHeight);
	}
	
	public void setPerHeight(double d) {
		perHeight = d;
	}
	
	@Override
	protected void addChild(Widget child) {
		if(widgets.contains(child)) {
			throw new RuntimeException("Widget ID collision: " + child.ID);
		}
		widgets.add(child);
		screen.addSubWidget(child);
		perHeight = Math.max(perHeight, child.getArea().height);
		maxProgress = Math.max(0, widgets.size() - getMaxShow());
		updateList();
	}
	
	//---------EVENT LISTENER------------
	public void onProgressChanged() {
		if(bar != null) {
			bar.setProgress(this.progress / (double)this.maxProgress);
		}
	}
	
}
