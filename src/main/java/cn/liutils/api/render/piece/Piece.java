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
 * Basic piece class. Handles simple drawing at (0, 0, 0)->(width, height, 0)
 * A piece's drawing effect depends largely on its <code>PieceProperty</code> sub instances.
 * You can combine different properties to bind texture, assign color and transformations, etc.
 * @author WeathFolD
 */
public class Piece implements IDrawable {
	public double width, height;
	public double u0, v0, u1, v1;
	
	private Map<EventType, Set<PieceProperty>> props = new HashMap();
	
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
	}
	
	private void onEvent(EventType event) {
		for(PieceProperty pp : gset(event)) {
			pp.onEvent(event);
		}
	}
	
	public void setSize(double w, double h) {
		width = w;
		height = h;
	}
	
	public void setMapAndSize(double u, double v, double width, double height) {
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
		t.addVertexWithUV(0, 0, 0, u0, v1);
		t.addVertexWithUV(width, 0, 0, u1, v1);
		t.addVertexWithUV(width, height, 0, u1, v0);
		t.addVertexWithUV(0, height, 0, u0, v0);
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
