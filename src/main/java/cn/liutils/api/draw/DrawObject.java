/**
 * 
 */
package cn.liutils.api.draw;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

/**
 * The most basic class of LIUtils drawing API. this class uses Component 
 * pattern and holds together many <code>DrawHandler</code>s and call them to
 * perform different action at different stages. You can combine multiple different
 * DrawHandlers to get a great variety of drawing effect.
 * @author WeathFolD
 */
public class DrawObject {
	
	public enum EventType {
		PRE_DRAW, /* Before going into modelview matrix. */
		PRE_TRANSFORM, POST_TRANSFORM,  /* Different transform stages. */
		PRE_TESS, /* Right before tessellation starts. */
		DO_TESS, /* Tessellator callings are expected to be done in here. */
		POST_TESS, /* Right after tessellation ends. */
		POST_DRAW, /* After leaving modelview matrix. */
		
		IN_TESS /* To be called by any DrawHandler that handles tessellating. */
	};
	
	protected Map<EventType, List<DrawHandler> > table = new HashMap();
	protected Map<String, DrawHandler> nameTable = new HashMap();
	
	public DrawObject() {}
	
	public DrawObject(Collection<? extends DrawHandler> handlers) {
		for(DrawHandler dh : handlers) {
			addHandler(dh);
		}
	}

	public void draw() {
		post(EventType.PRE_DRAW);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix(); {
			post(EventType.PRE_TRANSFORM);
			post(EventType.POST_TRANSFORM);
			post(EventType.PRE_TESS);
			post(EventType.DO_TESS);
			post(EventType.POST_TESS);
		} GL11.glPopMatrix();
		post(EventType.POST_DRAW);
	}
	
	public void addHandlers(DrawHandler... dhs) {
		for(DrawHandler dh : dhs) {
			addHandler(dh);
		}
	}
	
	public void addHandler(DrawHandler dh) {
		if(nameTable.containsKey(dh.getID())) {
			throw new RuntimeException("Duplicate ID: " + dh.getID());
		}
		nameTable.put(dh.getID(), dh);
		for(EventType et : dh.getEvents()) {
			getEventList(et).add(dh);
		}
	}
	
	public DrawHandler getHandler(String id) {
		return nameTable.get(id);
	}
	
	public void removeHandler(DrawHandler dh) { //Here we assume DH with same ID are equal.
		removeHandler(dh.getID());
	}
	
	public void removeHandler(String id) {
		if(nameTable.containsKey(id)) {
			DrawHandler toRemove = nameTable.get(id);
			for(EventType et : toRemove.getEvents()) {
				getEventList(et).remove(toRemove);
			}
			nameTable.remove(id);
		}
	}
	
	private List<DrawHandler> getEventList(EventType et) {
		List<DrawHandler> res = table.get(et);
		if(res == null) {
			res = new LinkedList();
			table.put(et, res);
		}
		return res;
	}
	
	public final void post(EventType et) {
		List<DrawHandler> res = table.get(et);
		if(res != null) {
			for(DrawHandler dh : res) {
				if(dh.enabled)
					dh.onEvent(et, this);
			}
		}
	}
	
}
