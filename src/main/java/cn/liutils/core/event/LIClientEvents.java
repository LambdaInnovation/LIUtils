/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.core.event;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.gui.AuxGui;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 *
 */
@SideOnly(Side.CLIENT)
public class LIClientEvents {

	private static Set<AuxGui> auxGuiList = new HashSet<AuxGui>();
	
	public static void registerAuxGui(AuxGui gui) {
		auxGuiList.add(gui);
	}
	
	@SubscribeEvent	
	public void drawHudEvent(RenderGameOverlayEvent event) {
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		 if(event.type == ElementType.CROSSHAIRS) {
			 for(AuxGui gui : auxGuiList) {
				 if(gui.isOpen()) gui.draw(event.resolution);
			 }
		 }
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
}
