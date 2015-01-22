/**
 * 
 */
package cn.liutils.api.render.piece.property;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.piece.Piece;

/**
 * Transformation property designed for 2D drawings.
 * @author WeathFolD
 */
public class Transform2D extends PieceProperty {

	public double tx, ty; //Transform coordinates
	public double rotation; //Rotation angle around Z axis
	public double pivotX, pivotY;
	
	public Transform2D(Piece p) {
		super(p);
	}
	
	public Transform2D(Piece p, double tx, double ty) {
		super(p);
		this.tx = tx;
		this.ty = ty;
	}
	
	public void setPivotPoint(double x, double y) {
		pivotX = x;
		pivotY = y;
	}
	
	@Override
	public EnumSet<EventType> getHandledEvents() {
		return EnumSet.of(EventType.PRE_TESSELLATION);
	}

	@Override
	public void onEvent(EventType type) {
		GL11.glTranslated(tx, ty, 0);
		GL11.glTranslated(pivotX, pivotY, 0);
		GL11.glRotated(rotation, 0, 0, 1);
		GL11.glTranslated(-pivotX, -pivotY, 0);
	}

}
