/**
 * 
 */
package cn.liutils.api.draw.tess;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * @author WeathFolD
 *
 */
public class GUIRect extends Rect {
	
	double umul = 1, vmul = 1;

	public GUIRect() {}

	public GUIRect(double w, double h) {
		super(w, h);
	}
	
	public void setResolution(double w, double h) {
		umul = 1 / w;
		vmul = 1 / h;
	}
	
	@Override
	public void onEvent(EventType event, DrawObject obj) {
		Tessellator t = Tessellator.instance;
		GL11.glTranslated(tx, ty, tz); //built in offset, usually in order to move it to center
		t.startDrawingQuads(); {
			obj.post(EventType.IN_TESS);
			t.addVertexWithUV(0, 	 0, 	 0, map.u0 * umul, map.v0 * umul);
			t.addVertexWithUV(0, 	 height, 0, map.u0 * umul, map.v1 * umul);
			t.addVertexWithUV(width, height, 0, map.u1 * umul, map.v1 * umul);
			t.addVertexWithUV(width, 0, 	 0, map.u1 * umul, map.v0 * umul);
		} t.draw();
	}

}
