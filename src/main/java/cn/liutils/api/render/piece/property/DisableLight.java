/**
 * 
 */
package cn.liutils.api.render.piece.property;

import java.util.EnumSet;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.piece.Piece;

/**
 * @author WeathFolD
 *
 */
public class DisableLight extends PieceProperty {

	public DisableLight(Piece piece) {
		super(piece);
	}

	@Override
	public EnumSet<EventType> getHandledEvents() {
		return EnumSet.of(EventType.PRE_DRAW, EventType.PRE_TESSELLATION, EventType.ON_TESSELLATION, EventType.POST_DRAW);
	}

	@Override
	public void onEvent(EventType type) {
		if(type == EventType.PRE_DRAW) {
			GL11.glDisable(GL11.GL_LIGHTING);
		} else if(type == EventType.PRE_TESSELLATION) {
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		} else if(type == EventType.ON_TESSELLATION) {
			Tessellator.instance.setBrightness(15728880);
		} else {
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}

}
