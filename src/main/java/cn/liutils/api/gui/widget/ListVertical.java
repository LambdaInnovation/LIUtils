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
 */
public class ListVertical extends Widget {
	
	int progress;
	int maxProgress;
	double perHeight;
	double yMargin = 0;
	
	private DragBar bar;
	List<WidgetNode> aliveNodes = new ArrayList(); //real list
	
	String ID;

	public ListVertical(String ID, double x, double y, double w, double h) {
		super(x, y, w, h);
		this.ID = ID;
	}
	
	public ListVertical setYMargin(double d) {
	    yMargin = d;
	    return this;
	}
	
	public ListVertical setDragBar(DragBar db) {
		bar = db;
		return this;
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
		setProgress(progress + 1, true);
	}
	
	public void progressLast() {
		setProgress(progress - 1, true);
	}
	
	public void setProgress(int prog, boolean notify) {
		int np = Math.max(0, Math.min(maxProgress, prog));
		boolean changed = np != progress;
		progress = np;
		if(changed && notify) {
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
		return maxProgress == 0 ? 0 : (double)progress / maxProgress;
	}
	
	public void setByRelativeProgress(double d) {
		d *= maxProgress;
		setProgress((int) d, true);
	}
	
	private void updateList() {
		List<WidgetNode> all = this.getSubNodes();
		aliveNodes.clear();
		
		for(WidgetNode node : all) {
			node.widget.doesDraw = false;
		}
		
		int n = getMaxShow() + progress;
		for(int i = progress; i < n && i < all.size(); ++i) {
			WidgetNode w = all.get(i);
			w.widget.posX = 0;
			w.widget.posY = (i - progress) * getRealHeight();
			w.widget.doesDraw = true;
			aliveNodes.add(w);
			w.widget.updatePos();
		}
	}
	
	@Override
	public void draw(double a, double b, boolean c) {
	    //check drag bar
	    if(bar != null) {
	        int lastProgress = progress;
	        setProgress((int) (this.maxProgress * bar.getProgress()), false);
	    }
	}
	
	protected int getMaxShow() {
		return perHeight == 0 ? 0 : MathHelper.floor_double(height / getRealHeight());
	}
	
	public void setPerHeight(double d) {
		perHeight = d;
	}
	
	private double getRealHeight() {
	    return perHeight + yMargin;
	}
	
	@Override
	public void addWidget(Widget child) {
		perHeight = Math.max(perHeight, child.height * child.scale);
		maxProgress = Math.max(0, getSubNodes().size() - getMaxShow() + 1);
		super.addWidget(child);
		updateList();
	}
	
	//---------EVENT LISTENER------------
	public void onProgressChanged() {
		if(bar != null && this.maxProgress != 0) { //Prevent dividing by 0.
			bar.setProgress(this.progress / (double)this.maxProgress);
		}
	}
	
}
