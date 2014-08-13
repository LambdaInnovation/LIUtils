/**
 * 
 */
package cn.liutils.core.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cn.liutils.api.entity.EntityPlayerRender;
import cn.liutils.api.util.PlayerPositionLock;
import cn.liutils.core.proxy.LIClientProxy;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

/**
 * @author Administrator
 *
 */
public class LITickEvents {
	public EntityPlayerRender helper;

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if(event.phase == Phase.START) {
			Minecraft mc = Minecraft.getMinecraft();
			EntityPlayer player = mc.thePlayer;
			if(player == null) return;
			playerTick(player);
			
			LIClientProxy.keyProcess.tickStart();
		} else {
			LIClientProxy.keyProcess.tickEnd();
		}
	}
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if(event.phase == Phase.START) {
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if(event.phase == Phase.START) {
			PlayerPositionLock.onTick(event.player);
		}
	}
	
	private void playerTick(EntityPlayer player) {
		World world = player.worldObj;
		if(helper == null) {
			helper = new EntityPlayerRender(player, player.worldObj);
			world.spawnEntityInWorld(helper);
		}
		
	}
}
