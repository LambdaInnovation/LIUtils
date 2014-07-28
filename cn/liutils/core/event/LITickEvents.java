/**
 * 
 */
package cn.liutils.core.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import cn.liutils.api.entity.EntityPlayerRender;
import cn.liutils.core.LIUtilsMod;
import cn.liutils.core.debug.Debug_MovingProcessor;
import cn.liutils.core.debug.KeyMoving;
import cn.liutils.core.debug.command.Command_SetMode;
import cn.liutils.core.proxy.LIClientProxy;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

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
			if(LIUtilsMod.DEBUG)
				doDebug(player);
			playerTick(player);
			
			LIClientProxy.keyProcess.tickStart();
		} else {
			LIClientProxy.keyProcess.tickEnd();
		}
	}
	
	private void playerTick(EntityPlayer player) {
		World world = player.worldObj;
		if(helper == null) {
			helper = new EntityPlayerRender(player, player.worldObj);
			world.spawnEntityInWorld(helper);
		}
	}
	
	private void doDebug(EntityPlayer player) {
		ItemStack item = player.getCurrentEquippedItem();
		if(item != null) {
			IItemRenderer render = MinecraftForgeClient.getItemRenderer(item, ItemRenderType.EQUIPPED_FIRST_PERSON);
			if(render != null) {
				Debug_MovingProcessor proc = KeyMoving.getProcessor(render);
				Command_SetMode.setActiveProcessor(player, proc);
			} else Command_SetMode.setActiveProcessor(player, null);
		} else Command_SetMode.setActiveProcessor(player, null);
	}
}
