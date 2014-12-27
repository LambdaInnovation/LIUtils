package cn.liutils.api.player;

import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cn.liutils.api.player.lock.LockBase.LockType;
import cn.liutils.api.util.GenericUtils;
import cn.liutils.core.LIUtils;
import cn.liutils.core.proxy.LIClientProps;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;

/**
 * @author EAirPeter
 */
public class ControlManager {
	private static ControlManager INSTANCE = null;
	
	private ControlManager() {	
	}

	private HashMap<String, ControlData> map = new HashMap<String, ControlData>();
	
	public static void init() {
		if (INSTANCE == null)
			INSTANCE = new ControlManager();
		FMLCommonHandler.instance().bus().register(INSTANCE);
		MinecraftForge.EVENT_BUS.register(INSTANCE);
		LIUtils.netHandler.registerMessage(MsgControlSyncAll.Handler.class, MsgControlSyncAll.class, LIClientProps.DISC_CONTROL_SYNCALL, Side.CLIENT);
	}
	
	/**
	 * Set lock state(s)
	 * @param ticks Ticks to unlock the state and negative for infinite
	 */
	public static void lockSet(EntityPlayer player, LockType type, int ticks) {
		ControlData.get(player).lockSet(type, ticks);
	}
	
	/**
	 * Modify lock state(s)
	 * @param ticks Positive for increase while negative for decrease
	 */
	public static void lockModify(EntityPlayer player, LockType type, int ticks) {
		ControlData.get(player).lockModify(type, ticks);
	}

	/**
	 * Unlock something
	 */
	public static void lockCancel(EntityPlayer player, LockType type) {
		ControlData.get(player).lockCancel(type);
	}
	
	@SubscribeEvent
	public void onMouse(MouseEvent event) {
		if (GenericUtils.isPlayerInGame() && Minecraft.getMinecraft().inGameHasFocus)
			ControlData.get(Minecraft.getMinecraft().thePlayer).onMouse(event);
	}
	
	@SubscribeEvent
	public void onKeyboard(KeyInputEvent event) {
		if (GenericUtils.isPlayerInGame() && Minecraft.getMinecraft().inGameHasFocus)
			ControlData.get(Minecraft.getMinecraft().thePlayer).onKeyboard();
	}
	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if (Minecraft.getMinecraft().thePlayer != null && !Minecraft.getMinecraft().isGamePaused()) {
			if (event.phase == Phase.START)
				ControlData.get(Minecraft.getMinecraft().thePlayer).tick();
			else
				ControlData.get(Minecraft.getMinecraft().thePlayer).onTick();
		}
	}
	
	private int ticker = 0;
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		List<EntityPlayerMP> players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		if (event.phase == Phase.START) {
			for (EntityPlayerMP player : players)
				ControlData.get(player).tick();
		}
		else {
			for (EntityPlayerMP player : players)
				ControlData.get(player).onTick();
			if (ticker == 0) {
				ticker = 600;
			}
			else
				--ticker;
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
}
