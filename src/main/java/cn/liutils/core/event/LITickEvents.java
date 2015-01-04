/**
 * 
 */
package cn.liutils.core.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cn.liutils.core.LIUtils;
import cn.liutils.core.energy.EnergyNet;
import cn.liutils.core.entity.EntityPlayerDaemon;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * @author WeAthFolD
 */
public class LITickEvents {
	public EntityPlayerDaemon helper;

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if(event.phase == Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.thePlayer;
			if(player == null) return;
			playerTick(player);
		} else {
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		World world = event.world;
		if(!LIUtils.ic2Exists)
			EnergyNet.onTick(world);
	}
	
	private void playerTick(EntityPlayer player) {
		World world = player.worldObj;
		if(helper == null) {
			helper = new EntityPlayerDaemon(player, player.worldObj);
			world.spawnEntityInWorld(helper);
		}
		
	}
}
