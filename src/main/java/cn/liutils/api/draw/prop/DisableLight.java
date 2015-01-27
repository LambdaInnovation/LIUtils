/**
 * 
 */
package cn.liutils.api.draw.prop;

import java.util.EnumSet;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.draw.DrawHandler;
import cn.liutils.api.draw.DrawObject;
import cn.liutils.api.draw.DrawObject.EventType;

/**
 * Disable light rendering.
 * @author WeathFolD
 */
public class DisableLight extends DrawHandler {
	
	private static DisableLight INSTANCE;
	
	public static DisableLight instance() {
		if(INSTANCE == null) {
			INSTANCE = new DisableLight();
		}
		return INSTANCE;
	}

	private DisableLight() {}

	@Override
	public EnumSet<EventType> getEvents() {
		return EnumSet.of(EventType.PRE_DRAW, EventType.PRE_TESS, EventType.IN_TESS, EventType.POST_DRAW);
	}

	@Override
	public String getID() {
		return "disable_light";
	}

	@Override
	public void onEvent(EventType event, DrawObject obj) {
		switch(event) {
		case PRE_DRAW:
			GL11.glDisable(GL11.GL_LIGHTING);
			break;
		case PRE_TESS:
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
			break;
		case IN_TESS:
			Tessellator.instance.setBrightness(15728880);
			break;
		case POST_DRAW:
			GL11.glEnable(GL11.GL_LIGHTING);
			break;
		default:
		}
	}

}
