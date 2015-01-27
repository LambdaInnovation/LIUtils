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
	
	public Transform(Piece piece, double x, double y, double z, double yaw, double pitch, double roll) {
		super(piece);
		setOffset(x, y, z);
		setRotation(yaw, pitch, roll);
	}
	
	public void setOffset(double tx, double ty, double tz) {
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
	}
	
	public void setRotation(double yaw, double pitch, double roll) {
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
	}
	
	public void setOrient(double x0, double y0, double z0, double x1, double y1, double z1) {
		double dx = x1 - x0, dy = y1 - y0, dz = z1 - z0;
		double sqxz = dx * dx + dz * dz;
		double len = Math.sqrt(sqxz + dy * dy);
		
		this.yaw = Math.atan2(dx, dz) * 180 / Math.PI;
		this.pitch = Math.atan2(dy, Math.sqrt(sqxz)) * 180 / Math.PI;
		this.piece.hWidth = len / 2;
		this.tx = x0 + dx / 2;
		this.ty = y0 + dy / 2;
		this.tz = z0 + dz / 2;
	}

	@Override
	public EnumSet<EventType> getHandledEvents() {
		return EnumSet.of(EventType.PRE_TRANSFORM);
	}

	@Override
	public void onEvent(EventType type) {
		prepare();
		GL11.glTranslated(tx, ty, tz);
		GL11.glRotated(yaw, 0, 1, 0);
		GL11.glRotated(pitch, 0, 0, 1);
		GL11.glRotated(roll, 1, 0, 0);
	}
	
	/**
	 * Update the transform state, if needed
	 */
	protected void prepare() {}

	@Override
	public String getID() {
		return "transform";
	}

}
