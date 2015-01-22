/**
 * 
 */
package cn.liutils.api.render.piece;

import net.minecraft.client.renderer.Tessellator;
import cn.liutils.api.render.piece.property.PieceProperty.EventType;
import cn.liutils.util.HudUtils;

/**
 * @author WeathFolD
 */
public class Piece2D extends Piece {

	public Piece2D() {}

	public Piece2D(double w, double h, double u0, double v0, double u1,
			double v1) {
		super(w, h, u0, v0, u1, v1);
	}

	public Piece2D(double w, double h) {
		super(w, h);
	}
	
	protected void doTessellation() {
		HudUtils.drawRect(0, 0, u0, v0, 2 * hWidth, 2 * hHeight, u1 - u0, v1 - v0);
	}

}
