/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
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

	public DisableLight() {}

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
