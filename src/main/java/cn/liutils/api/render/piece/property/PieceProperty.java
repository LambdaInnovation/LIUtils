/**
 * 
 */
package cn.liutils.api.render.piece.property;

import java.util.EnumSet;

import cn.liutils.api.render.piece.Piece;

/**
 * @author WeathFolD
 *
 */
public abstract class PieceProperty {
	
	public final Piece piece;
	public boolean enabled = true;
	
	public PieceProperty(Piece piece) {
		this.piece = piece;
		piece.addProperty(this);
	}

	public enum EventType {
		PRE_DRAW,
		PRE_TRANSFORM,
		PRE_TESSELLATION,
		ON_TESSELLATION,
		POST_DRAW
	};
	
	public void setEnabled(boolean b) {
		enabled = b;
	}
	
	public abstract EnumSet<EventType> getHandledEvents();
	
	public abstract void onEvent(EventType type);
}
