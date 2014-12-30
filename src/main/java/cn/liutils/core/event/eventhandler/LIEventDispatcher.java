package cn.liutils.core.event.eventhandler;

import java.util.HashSet;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.MouseInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

public final class LIEventDispatcher {
	
	private static LIEventDispatcher INSTANCE = null;
	
	private LIEventDispatcher() {
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public static LIEventDispatcher instance() {
		if (INSTANCE == null)
			INSTANCE = new LIEventDispatcher();
		return INSTANCE;
	}

	public final HashSet<LIIHandler> setRenderTick = new HashSet<LIIHandler>();

	@SubscribeEvent
	void onRenderTick(RenderTickEvent event) {
		for (LIIHandler handler : setRenderTick)
			handler.onEvent(event);
	}

	public final HashSet<LIIHandler> setClientTick = new HashSet<LIIHandler>();

	@SubscribeEvent
	void onClientTick(ClientTickEvent event) {
		for (LIIHandler handler : setClientTick)
			handler.onEvent(event);
	}

	public final HashSet<LIIHandler> setServerTick = new HashSet<LIIHandler>();

	@SubscribeEvent
	void onServerTick(ServerTickEvent event) {
		for (LIIHandler handler : setServerTick)
			handler.onEvent(event);
	}

	public final HashSet<LIIHandler> setWorldTick = new HashSet<LIIHandler>();

	@SubscribeEvent
	void onWorldTick(WorldTickEvent event) {
		for (LIIHandler handler : setWorldTick)
			handler.onEvent(event);
	}

	public final HashSet<LIIHandler> setPlayerTick = new HashSet<LIIHandler>();

	@SubscribeEvent
	void onPlayerTick(PlayerTickEvent event) {
		for (LIIHandler handler : setPlayerTick)
			handler.onEvent(event);
	}

	public final HashSet<LIIHandler> setMouseInput = new HashSet<LIIHandler>();

	@SubscribeEvent
	void onMouseInput(MouseInputEvent event) {
		for (LIIHandler handler : setMouseInput)
			handler.onEvent(event);
	}

	public final HashSet<LIIHandler> setKeyInput = new HashSet<LIIHandler>();

	@SubscribeEvent
	void onKeyInput(KeyInputEvent event) {
		for (LIIHandler handler : setKeyInput)
			handler.onEvent(event);
	}

}
