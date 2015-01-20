/**
 * 
 */
package cn.liutils.api.render.piece.property;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.piece.Piece;

/**
 * Transformation property designed for 2D drawings, will not transform the billboard to the center of the origin.
 * @author WeathFolD
 */
public class Transform2D extends PieceProperty {

	double tx, ty; //Transform coordinates
	double rotation; //Rotation angle around Z axis
	
	public Transform2D(Piece p) {
		super(p);
	}
	
	public Transform2D(Piece p, double tx, double ty) {
		super(p);
		this.tx = tx;
		this.ty = ty;
	}

	@Override
	public EnumSet<EventType> getHandledEvents() {
		return EnumSet.of(EventType.PRE_TESSELLATION);
	}

	@Override
	public void onEvent(EventType type) {
		double ox = piece.width / 2, oy = piece.height / 2;
		GL11.glTranslated(tx, ty, 0);
		
		GL11.glTranslated(ox, oy, 0);
		GL11.glRotated(rotation, 0, 1, 0);
		GL11.glTranslated(-ox, -oy, 0);
	}

}
