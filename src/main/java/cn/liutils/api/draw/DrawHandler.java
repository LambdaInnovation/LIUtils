/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.draw;

import java.util.EnumSet;

import cn.liutils.api.draw.DrawObject.EventType;

/**
 * Component of a <code>DrawObject</code>. Each do some action at a specific render stage.
 * @author WeathFolD
 */
public abstract class DrawHandler {
	
	public boolean enabled = true;

	public DrawHandler() {}
	
	public abstract EnumSet<EventType> getEvents();
	
	public abstract String getID();
	
	public abstract void onEvent(EventType event, DrawObject obj);
	
	@Override
	public final boolean equals(Object obj) {
		if(!(obj instanceof DrawHandler)) 
			return false;
		return ((DrawHandler) obj).getID().equals(this.getID());
	}

}
