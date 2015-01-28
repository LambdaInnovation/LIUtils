/**
 * 
 */
package cn.liutils.api.draw.tess;

import java.util.EnumSet;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * A most simple rectangle drawer that takes rect's width and height, then draw it in
 * (0, 0, 0) -> (0, height, width) with a offset (tx, ty, tz)
 * @author WeathFolD
 */
public class Rect extends TessBase {

	public double width, height;
	
	public double tx, ty, tz;
	
	public RectMapping map = new RectMapping(); //Texture mapping data.

	public Rect() {
		super("simple_rect");
	}
	
	public Rect(double w, double h) {
		super("simple_rect");
		setSize(w, h);
	}
	
	public void setCentered() {
		tz = -width / 2;
		ty = -height / 2;
	}
	
	public void setSize(double w, double h) {
		width = w;
		height = h;
	}
	
	public void setOffset(double tx, double ty, double tz) {
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
	}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.DO_TESS);
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		Tessellator t = Tessellator.instance;
		GL11.glTranslated(tx, ty, tz); //built in offset, usually in order to move it to center
		t.startDrawingQuads(); {
			obj.post(EventType.IN_TESS);
			t.addVertexWithUV(0, 0, 	 0, 	map.u0, map.v1);
			t.addVertexWithUV(0, 0,  	 width, map.u1, map.v1);
			t.addVertexWithUV(0, height, width, map.u1, map.v0);
			t.addVertexWithUV(0, height, 0, 	map.u0, map.v0);
		} t.draw();
	}

}
