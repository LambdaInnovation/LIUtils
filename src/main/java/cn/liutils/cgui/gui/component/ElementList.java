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
package cn.liutils.cgui.gui.component;

import java.util.ArrayList;
import java.util.List;

import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.event.FrameEvent;
import cn.liutils.cgui.gui.event.FrameEvent.FrameEventHandler;
import cn.liutils.cgui.gui.event.GuiEvent;
import cn.liutils.cgui.gui.event.GuiEventHandler;

/**
 * Component that can hold widgets itself and display them as a list. Only Widgets fully in the area will be shown.
 * Currently you can only setup the list before it was first drawn.
 * @author WeAthFolD
 */
public class ElementList extends Component {
	
	List<Widget> subWidgets = new ArrayList();
	
	public double spacing = 0.0;
	
	double elementHt;
	
	int progress, maxProgress, showPerPage;
	
	private boolean loaded = false;

	public ElementList() {
		super("ElementList");
		
		this.addEventHandler(new FrameEventHandler() {
			@Override
			public void handleEvent(Widget w, FrameEvent event) {
				if(!loaded) {
					loaded = true;
					setup(w);
					updateList();
					for(Widget ww : subWidgets) {
						w.addWidget(ww);
					}
				}
			}
		});
	}
	
	public static ElementList get(Widget w) {
		return w.getComponent("ElementList");
	}
	
	public int getProgress() {
		return progress;
	}
	
	public int getMaxProgress() {
		return maxProgress;
	}
	
	public void disposeAll() {
		for(Widget w : subWidgets)
			w.disposed = true;
		subWidgets.clear();
		loaded = false;
		progress = maxProgress = showPerPage = 0;
	}
	
	public void setProgress(Widget w, int prog) {
		if(progress == prog)
			return;
		progress = prog;
		if(progress < 0) progress = 0;
		if(progress > maxProgress) progress = maxProgress;
		updateList();
		
	}
	
	public static class ProgressChangedEvent implements GuiEvent  {}
	
	public abstract static class ProgressChangeHandler extends GuiEventHandler<ProgressChangedEvent> {

		public ProgressChangeHandler() {
			super(ProgressChangedEvent.class);
		}
		
	}
	
	private void setup(Widget w) {
		if(elementHt == 0) {
			maxProgress = 0;
			return;
		}
		double ht = w.transform.height;
		showPerPage = (int) Math.max(1, ht / (elementHt + spacing));
		maxProgress = Math.max(0, subWidgets.size() - showPerPage);
	}
	
	private void updateList() {
		for(Widget w : subWidgets) {
			w.transform.doesDraw = false;
		}
		for(int i = progress; i < progress + showPerPage && i < subWidgets.size(); ++i) {
			int in = i - progress;
			Widget w = subWidgets.get(i);
			w.transform.doesDraw = true;
			w.transform.x = 0;
			w.transform.y = in * (elementHt + spacing);
			w.dirty = true;
		}
	}
	
	public void progressNext(Widget w) { 
		++progress;
		if(progress > maxProgress) progress = maxProgress;
		updateList();
		w.postEvent(new ProgressChangedEvent());
	}
	
	public void progressLast(Widget w) {
		--progress;
		if(progress < 0) progress = 0;
		updateList();
		w.postEvent(new ProgressChangedEvent());
	}
	
	public void addWidget(Widget w) {
		if(loaded) return;
		w.needCopy = false;
		elementHt = Math.max(elementHt, w.transform.height);
		subWidgets.add(w);
	}
	
	public ElementList copy() {
		ElementList el = (ElementList) super.copy();
		for(Widget w : subWidgets) {
			el.addWidget(w);
		}
		return el;
	}

}
