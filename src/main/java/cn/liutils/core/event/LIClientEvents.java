/**
 * 
 */
package cn.liutils.core.event;

import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
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
		 if(event.type == ElementType.CROSSHAIRS) {
			 for(AuxGui gui : auxGuiList) {
				 if(gui.isOpen()) gui.draw(event.resolution);
			 }
		 }
	}
	
}
