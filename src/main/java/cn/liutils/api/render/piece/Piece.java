/**
 * 
 */
package cn.liutils.api.render.piece;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.IDrawable;
import cn.liutils.api.render.piece.property.PieceProperty;
import cn.liutils.api.render.piece.property.PieceProperty.EventType;

/**
 * Basic piece class. Handles simple drawing at origin(0, 0, 0) With the billboard centered on it, facing Z+ direction.
 * A piece's drawing effect depends largely on its <code>PieceProperty</code> sub instances.
 * You can combine different properties to bind texture, assign color and transformations, etc.
 * @author WeathFolD
 */
public class Piece implements IDrawable {
	public double hWidth, hHeight;
	public double u0, v0, u1, v1;
	
	private Map<EventType, Set<PieceProperty>> props = new HashMap();
	private Map<String, PieceProperty> index = new HashMap();
	
	public Piece() {}
	
	public Piece(double w, double h, double u0, double v0, double u1, double v1) {
		setSize(w, h);
		setMapping(u0, v0, u1, v1);
	}
	
	public Piece(double w, double h) {
		this(w, h, 0, 0, 1, 1);
	}
	
	public void addProperty(PieceProperty prop) {
		for(EventType et : prop.getHandledEvents()) {
			gset(et).add(prop);
		}
		index.put(prop.getID(), prop);
	}
	
	/**
	 * A bit costly, don't use it all the time
	 */
	public void removeProperty(String ID) {
		PieceProperty pp = index.get(ID);
		if(pp == null) return;
		for(EventType et : pp.getHandledEvents()) {
			gset(et).remove(pp);
		}
	}
	
	public PieceProperty getProperty(String name) {
		return index.get(name);
	}
	
	protected void onEvent(EventType event) {
		for(PieceProperty pp : gset(event)) {
			if(pp.enabled)
				pp.onEvent(event);
		}
	}
	
	public void setSize(double w, double h) {
		hWidth = w / 2;
		hHeight = h / 2;
	}
	
	public void setMappingSized(double u, double v, double width, double height) {
		setMapping(u, v, u + width, v + height);
	}
	
	public void setMapping(double u0, double v0, double u1, double v1) {
		this.u0 = u0;
		this.v0 = v0;
		this.u1 = u1;
		this.v1 = v1;
	}

	@Override
	public void draw() {
		onEvent(EventType.PRE_DRAW);
		GL11.glPushMatrix(); {
			onEvent(EventType.PRE_TRANSFORM);
			onEvent(EventType.PRE_TESSELLATION);
			doTessellation();
		} GL11.glPopMatrix();
		onEvent(EventType.POST_DRAW);
	}
	
	protected void doTessellation() {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		onEvent(EventType.ON_TESSELLATION);
		t.addVertexWithUV(0, hHeight,  -hWidth, u0, v0);
		t.addVertexWithUV(0, -hHeight, -hWidth, u0, v1);
		t.addVertexWithUV(0, -hHeight,  hWidth, u1, v1);
		t.addVertexWithUV(0, hHeight,   hWidth, u1, v0);
		t.draw();
	}
	
	private Set<PieceProperty> gset(EventType et) {
		Set<PieceProperty> res = props.get(et);
		if(res == null) {
			res = new HashSet<PieceProperty>();
			props.put(et, res);
		}
		return res;
	}
}
