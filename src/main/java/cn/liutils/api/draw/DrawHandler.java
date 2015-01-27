/**
 * 
 */
package cn.liutils.api.draw;

import java.util.EnumSet;

import cn.liutils.api.draw.DrawObject.EventType;

/**
 * Component of a <code>DrawObject</code>. Each do some action at a specific render stage.
 * @author WeathFolD
 */
public abstract class DrawHandler {

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
