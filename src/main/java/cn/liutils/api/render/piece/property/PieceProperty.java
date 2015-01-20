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
	
	public PieceProperty(Piece piece) {
		this.piece = piece;
	}

	public enum EventType {
		PRE_DRAW,
		PRE_TRANSFORM,
		PRE_TESSELLATION,
		ON_TESSELLATION,
		POST_DRAW
	};
	
	public abstract EnumSet<EventType> getHandledEvents();
	
	public abstract void onEvent(EventType type);
}
