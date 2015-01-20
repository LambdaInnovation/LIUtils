/**
 * 
 */
package cn.liutils.api.render.piece.property;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.piece.Piece;

/**
 * @author WeathFolD
 */
public class Transform extends PieceProperty {
	
	public double tx, ty, tz; //offset
	public double yaw, pitch, roll; //rotation
	
	public Transform(Piece piece) {
		super(piece);
	}

	@Override
	public EnumSet<EventType> getHandledEvents() {
		return EnumSet.of(EventType.PRE_TRANSFORM);
	}

	@Override
	public void onEvent(EventType type) {
		prepare();
		double ox = piece.width / 2, oy = piece.height / 2;
		GL11.glTranslated(tx, ty, tz);
		GL11.glTranslated(ox, oy, 0);
		GL11.glRotated(yaw, 0, 1, 0);
		GL11.glRotated(pitch, 0, 0, 1);
		GL11.glRotated(roll, 1, 0, 0);
		GL11.glTranslated(-ox, -oy, 0);
	}
	
	/**
	 * Update the transform state, if needed
	 */
	protected void prepare() {}

}
