/**
 * 
 */
package cn.liutils.api.render.piece.property;

import java.util.EnumSet;

import cn.liutils.api.render.piece.Piece;
import cn.liutils.util.RenderUtils;
import net.minecraft.util.ResourceLocation;

/**
 * @author WeathFolD
 */
public class AssignTexture extends PieceProperty {
	
	protected ResourceLocation texture;

	public AssignTexture(Piece piece) {
		this(piece, null);
	}
	
	public AssignTexture(Piece piece, ResourceLocation res) {
		super(piece);
		texture = res;
	}
	
	public void setTexture(ResourceLocation tex) {
		texture = tex;
	}

	@Override
	public EnumSet<EventType> getHandledEvents() {
		return EnumSet.of(EventType.PRE_TESSELLATION);
	}

	@Override
	public void onEvent(EventType type) {
		RenderUtils.loadTexture(getTexture());
	}
	
	/**
	 * In case of overriding to get varying textures
	 */
	protected ResourceLocation getTexture() {
		return texture;
	}

}
