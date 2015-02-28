/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * AcademyCraft is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * AcademyCraft是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.gui.widget;

import cn.liutils.api.gui.Widget;

/**
 * Vertical list with element focus.
 * @author WeathFolD
 */
public class FocusedVList extends ListVertical {
	
	int focus = 0;

	public FocusedVList(String ID, double x, double y, double w, double h) {
		super(ID, x, y, w, h);
	}
	
	public void progressNext() {
		setFocus(focus + 1);
	}
	
	public void progressLast() {
		setFocus(focus - 1);
	}
	
	public void setFocus(int i) {
		focus = Math.max(0, Math.min(getSubNodes().size() - 1, i));
		if(focus < progress) {
			setProgress(focus);
		} else if(focus + getMaxShow() >= progress){
			setProgress(focus - this.getMaxShow() + 1);
		}
	}
	
	public int getFocus() {
		return focus;
	}
	
	public Widget getFocusedElement() {
		return this.getSubNodes().get(focus).widget;
	}

}
