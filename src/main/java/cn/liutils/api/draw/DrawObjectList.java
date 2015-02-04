/**
 * 
 */
package cn.liutils.api.draw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject.EventType;

/**
 * delegate events to a subset of DrawObjects.
 * This enables us to set the DrawObject generic properties at once, and then do the seperate rendering.
 * @author WeathFolD
 */
public class DrawObjectList extends DrawObject {
	
	List<DrawObject> objs = new ArrayList<DrawObject>();

	public DrawObjectList() {}
	
	public DrawObjectList(Collection<? extends DrawHandler> handlers) {
		super(handlers);
	}
	
	public DrawObjectList(DrawObject... dos) {
		objs.addAll(Arrays.asList(dos));
	}
	
	public DrawObjectList(Collection<? extends DrawHandler> handlers, DrawObject... dos) {
		super(handlers);
		objs.addAll(Arrays.asList(dos));
	}
	
	public void draw() {
		postAll(EventType.PRE_DRAW);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix(); {
			postAll(EventType.PRE_TRANSFORM);
			postAll(EventType.POST_TRANSFORM);
			postAll(EventType.PRE_TESS);
			postAll(EventType.DO_TESS);
			postAll(EventType.POST_TESS);
		} GL11.glPopMatrix();
		postAll(EventType.POST_DRAW);
	}
	
	private void postAll(EventType event) {
		super.post(event);
		for(DrawObject dd : objs) {
			dd.post(event);
		}
	}

}