package cn.liutils.core.player;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegSubmoduleInit;
import cn.liutils.api.player.ControlData;
import cn.liutils.core.LIUtils;
import cn.liutils.core.proxy.LIClientProps;

/**
 * 
 * @author Violet
 *
 */
@RegistrationClass
@RegSubmoduleInit(side = RegSubmoduleInit.Side.CLIENT_ONLY)
@SideOnly(Side.CLIENT)
public class ControlHandler {
private static ControlHandler INSTANCE = null;
	
	private ControlHandler() {	
	}

	private HashMap<String, ControlData> map = new HashMap<String, ControlData>();
	
	public static void init() {
		if (INSTANCE == null) {
			INSTANCE = new ControlHandler();
			FMLCommonHandler.instance().bus().register(INSTANCE);
			MinecraftForge.EVENT_BUS.register(INSTANCE);
			//LIUtils.netHandler.registerMessage(MsgControlSyncAll.Handler.class, MsgControlSyncAll.class, LIClientProps.DISC_CONTROL_SYNC, Side.CLIENT);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.START && Minecraft.getMinecraft().thePlayer != null && !Minecraft.getMinecraft().isGamePaused())
			ControlData.get(Minecraft.getMinecraft().thePlayer).tick();
	}
	
	private long lastSyncAll = 0;
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onServerTick(ServerTickEvent event) {
		List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		if (event.phase == Phase.START) {
			for (EntityPlayerMP player : players)
				ControlData.get(player).tick();
		}
		else {
			long now = MinecraftServer.getSystemTimeMillis();
			if (now > lastSyncAll + 20000) {
				lastSyncAll = now;
				for (EntityPlayerMP player : players)
					ControlData.get(player).syncAll();
			}
			else {
				for (EntityPlayerMP player : players)
					ControlData.get(player).tickSync();
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			if (ControlData.get(event.entity) == null)
				event.entity.registerExtendedProperties(ControlData.IDENTIFIER, new ControlData((EntityPlayer) event.entity));
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		ControlData data = ControlData.get(event.player);
		if (data == null)
			((EntityPlayerMP) event.player).playerNetServerHandler.kickPlayerFromServer("INVALID SITUATION");
		data.syncAll();
	}
	
}
