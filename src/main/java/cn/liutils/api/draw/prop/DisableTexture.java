/**
 * 
 */
package cn.liutils.api.draw.prop;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * @author WeathFolD
 *
 */
public class DisableTexture extends DrawHandler {
	
	private static DisableTexture INSTANCE;
	
	public static DrawHandler instance() {
		if(INSTANCE == null) {
			INSTANCE = new DisableTexture();
		}
		return INSTANCE;
	}

	private DisableTexture() {}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.PRE_DRAW, EventType.POST_DRAW);
	}

	@Override
	public String getID() {
		return "disable_tex";
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		switch(event) {
		case PRE_DRAW:
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			break;
		case POST_DRAW:
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			break;
		default:
		}
	}

}
