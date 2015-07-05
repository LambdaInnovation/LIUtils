/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cn.annoreg.core.Registrant;
import cn.annoreg.mc.RegEventHandler;
import cn.liutils.api.event.OpenAuxGuiEvent;
import cn.liutils.util.client.RenderUtils;
import cn.liutils.util.helper.GameTimer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 *
 */
@SideOnly(Side.CLIENT)
@Registrant
public class AuxGuiHandler {

	@RegEventHandler
	public static AuxGuiHandler instance = new AuxGuiHandler();
	
	private AuxGuiHandler() {}
	
	private static List<AuxGui> auxGuiList = new ArrayList<AuxGui>();
	
	public static void register(AuxGui gui) {
		auxGuiList.add(gui);
		MinecraftForge.EVENT_BUS.post(new OpenAuxGuiEvent(gui));
		gui.onAdded();
	}
	
	@SubscribeEvent	
	public void drawHudEvent(RenderGameOverlayEvent event) {
		if(Minecraft.getMinecraft().thePlayer.isDead) {
			Iterator<AuxGui> iter = auxGuiList.iterator();
			while(iter.hasNext()) {
				AuxGui gui = iter.next();
				if(!gui.isConsistent()) {
					gui.onDisposed();
					iter.remove();
				}
			}
			return;
		}
		
		GL11.glDepthFunc(GL11.GL_ALWAYS);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		RenderUtils.pushTextureState();
		
		if(event.type == ElementType.EXPERIENCE) {
			Iterator<AuxGui> iter = auxGuiList.iterator();
			while(iter.hasNext()) {
				AuxGui gui = iter.next();
				if(gui.isDisposed()) {
					
					gui.onDisposed();
					iter.remove();
					gui.lastFrameActive = false;
					
				} else {
					
					if(!gui.lastFrameActive)
						gui.lastActivateTime = GameTimer.getTime();
					gui.draw(event.resolution);
					gui.lastFrameActive = true;
					
				}
			}
		}
		
		RenderUtils.popTextureState();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent event) {
		if(!Minecraft.getMinecraft().isGamePaused()) {
			Iterator<AuxGui> iter = auxGuiList.iterator();
			while(iter.hasNext()) {
				AuxGui gui = iter.next();
				if(!gui.isDisposed() && gui.requireTicking) {
					if(!gui.lastFrameActive)
						gui.lastActivateTime = GameTimer.getTime();
					gui.tick();
					gui.lastFrameActive = true;
				}
			}
		}
	}
	
	public static boolean hasForegroundGui() {
		for(AuxGui ag : auxGuiList) {
			if(!ag.isDisposed() && ag.isForeground())
				return true;
		}
		return false;
	}
	
}
