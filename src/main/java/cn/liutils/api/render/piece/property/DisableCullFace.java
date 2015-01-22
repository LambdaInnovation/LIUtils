/**
 * 
 */
package cn.liutils.api.render.piece.property;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.piece.Piece;

/**
 * @author WeathFolD
 *
 */
public class DisableCullFace extends PieceProperty {

	public DisableCullFace(Piece piece) {
		super(piece);
	}

	@Override
	public EnumSet<EventType> getHandledEvents() {
		return EnumSet.of(EventType.PRE_DRAW, EventType.POST_DRAW);
	}

	@Override
	public void onEvent(EventType type) {
		if(type == EventType.PRE_DRAW) {
			GL11.glDisable(GL11.GL_CULL_FACE);
		} else {
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}

}
