package cn.liutils.core.player;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cn.liutils.api.player.ControlData;
import cn.liutils.api.util.GenericUtils;
import cn.liutils.core.LIUtils;
import cn.liutils.core.event.eventhandler.LIEventDispatcher;
import cn.liutils.core.event.eventhandler.LIIHandler;
import cn.liutils.core.proxy.LIClientProps;

/**
 * 
 * @author Violet
 *
 */
public class ControlHandler implements LIIHandler {
private static ControlHandler INSTANCE = null;
	
	private ControlHandler() {	
	}

	private HashMap<String, ControlData> map = new HashMap<String, ControlData>();
	
	//TODO use Dispatcher
	public static void init() {
		if (INSTANCE == null)
			INSTANCE = new ControlHandler();
		FMLCommonHandler.instance().bus().register(INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		LIUtils.netHandler.registerMessage(MsgControlSyncAll.Handler.class, MsgControlSyncAll.class, LIClientProps.DISC_CONTROL_SYNCALL, Side.CLIENT);
		LIEventDispatcher lied = LIEventDispatcher.instance();
		lied.setClientTick.add(INSTANCE);
		lied.setServerTick.add(INSTANCE);
	}
	
	public void onClientTick(ClientTickEvent event) {
		if (event.phase == Phase.START && Minecraft.getMinecraft().thePlayer != null && !Minecraft.getMinecraft().isGamePaused())
			ControlData.get(Minecraft.getMinecraft().thePlayer).tick();
	}
	
	private int ticker = 0;
	
	public void onServerTick(ServerTickEvent event) {
		List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		if (event.phase == Phase.START) {
			for (EntityPlayerMP player : players)
				ControlData.get(player).tick();
		}
		else {
			if (ticker == 0) {
				for (EntityPlayerMP player : players)
					ControlData.get(player).tickSync();
				ticker = 600;
			}
			else {
				for (EntityPlayerMP player : players)
					ControlData.get(player).tickSync();
				--ticker;
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer) {
			if (ControlData.get(event.entity) == null)
				event.entity.registerExtendedProperties(ControlData.IDENTIFIER, new ControlData());
		}
	}

	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		ControlData data = ControlData.get(event.player);
		if (data == null)
			((EntityPlayerMP) event.player).playerNetServerHandler.kickPlayerFromServer("INVALID SITUATION");
		data.syncAll();
	}
	
	@Override
	public void onEvent(Event event) {
		if (event instanceof ServerTickEvent)
			onServerTick((ServerTickEvent) event);
		else if (event instanceof ClientTickEvent)
			onClientTick((ClientTickEvent) event);
	}
}
