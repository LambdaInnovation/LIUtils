/**
 * 
 */
package cn.liutils.api.render.piece;

import net.minecraft.client.renderer.Tessellator;
import cn.liutils.api.render.piece.property.DisableCullFace;
import cn.liutils.api.render.piece.property.PieceProperty.EventType;


/**
 * Draws crossed square rather than one square.
 * @author WeathFolD
 */
public class PieceCrossed extends Piece {

	public PieceCrossed() {
		super();
		init();
	}

	public PieceCrossed(double w, double h, double u0, double v0, double u1,
			double v1) {
		super(w, h, u0, v0, u1, v1);
		init();
	}

	public PieceCrossed(double w, double h) {
		super(w, h);
		init();
	}
	
	private void init() {
		new DisableCullFace(this);
	}
	
	@Override
	protected void doTessellation() {
		Tessellator t = Tessellator.instance;
		t.startDrawingQuads();
		onEvent(EventType.ON_TESSELLATION);
		t.addVertexWithUV(0, hHeight,  -hWidth, u0, v0);
		t.addVertexWithUV(0, -hHeight, -hWidth, u0, v1);
		t.addVertexWithUV(0, -hHeight,  hWidth, u1, v1);
		t.addVertexWithUV(0, hHeight,   hWidth, u1, v0);
		
		t.addVertexWithUV(hHeight,  0,  -hWidth, u0, v0);
		t.addVertexWithUV(-hHeight, 0,  -hWidth, u0, v1);
		t.addVertexWithUV(-hHeight, 0,  hWidth,  u1, v1);
		t.addVertexWithUV(hHeight,  0,  hWidth,  u1, v0);
		t.draw();
	}
	

}
