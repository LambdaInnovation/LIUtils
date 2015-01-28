/**
 * 
 */
package cn.liutils.api.gui.widget;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.MathHelper;
import cn.liutils.api.gui.LIGui;
import cn.liutils.api.gui.LIGui.WidgetNode;
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
	
	private DragBar bar;
	List<WidgetNode> aliveNodes = new ArrayList(); //real list
	
	String ID;

	/**
	 * @param id
	 * @param par
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public ListVertical(String ID, double x, double y, double w, double h) {
		super(x, y, w, h);
		this.ID = ID;
	}
	
	public void setDragBar(DragBar db) {
		bar = db;
	}
	
	@Override
	protected void onAdded() {
		//add mouse wheel handlings
		final LIGui gui = getGui();
		final WidgetNode node = getNode();
		
		gui.addKeyHandler("scr_up_" + ID, LIKeyProcess.MWHEELUP, false, new IKeyHandler() {
			@Override
			public void onKeyDown(int keyCode, boolean tickEnd) {
				if(gui.isVisible(ListVertical.this) && node.pointWithin(gui.mouseX, gui.mouseY))
					progressLast();
			}
			@Override public void onKeyUp(int keyCode, boolean tickEnd) {}
			@Override public void onKeyTick(int keyCode, boolean tickEnd) {}
		});
		
		gui.addKeyHandler("scr_dn_" + ID, LIKeyProcess.MWHEELDOWN, false, new IKeyHandler() {
			@Override
			public void onKeyDown(int keyCode, boolean tickEnd) {
				if(gui.isVisible(ListVertical.this) && node.pointWithin(gui.mouseX, gui.mouseY)) {
					progressNext();
				}
			}
			@Override public void onKeyUp(int keyCode, boolean tickEnd) {}
			@Override public void onKeyTick(int keyCode, boolean tickEnd) {}
		});
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
		List<WidgetNode> all = this.getSubNodes();
		aliveNodes.clear();
		
		for(WidgetNode node : all) {
			node.widget.doesDraw = false;
		}
		
		int n = getMaxShow() + progress;
		for(int i = progress; i < n && i < aliveNodes.size(); ++i) {
			WidgetNode w = aliveNodes.get(i);
			w.widget.posX = 0;
			w.widget.posY = (i - progress) * perHeight;
			w.widget.doesDraw = true;
			aliveNodes.add(w);
			w.widget.updatePos();
		}
	}
	
	public void draw(double a, double b, boolean c) {}
	
	private int getMaxShow() {
		return perHeight == 0 ? 0 : MathHelper.floor_double(height / perHeight);
	}
	
	public void setPerHeight(double d) {
		perHeight = d;
	}
	
	@Override
	public void addWidget(Widget child) {
		perHeight = Math.max(perHeight, child.height * child.scale);
		maxProgress = Math.max(0, aliveNodes.size() - getMaxShow());
		super.addWidget(child);
		updateList();
	}
	
	//---------EVENT LISTENER------------
	public void onProgressChanged() {
		if(bar != null) {
			bar.setProgress(this.progress / (double)this.maxProgress);
		}
	}
	
}
