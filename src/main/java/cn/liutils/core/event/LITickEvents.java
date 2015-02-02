/**
 * 
 */
package cn.liutils.core.event;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.liutils.api.render.IPlayerRenderHook;
import cn.liutils.core.LIUtils;
import cn.liutils.core.energy.EnergyNet;
import cn.liutils.core.entity.EntityPlayerHook;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * @author WeAthFolD
 */
@RegistrationClass
@RegEventHandler({Bus.FML, Bus.Forge})
public class LITickEvents {

	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		World world = event.world;
		if(!LIUtils.ic2Exists)
			EnergyNet.onTick(world);
	}
	
}
