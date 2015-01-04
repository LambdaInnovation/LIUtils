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
import cn.liutils.api.player.state.StateBase.StateType;
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
 * 
 * @author Violet
 *
 */
public class ControlManager {

	/**
	 * Set state
	 * @param ticks Ticks to cancel the state and negative for infinite
	 */
	public static void stateSet(EntityPlayer player, StateType type, int ticks) {
		ControlData.get(player).stateSet(type, ticks);
	}
	
	/**
	 * Modify state
	 * @param ticks Positive for increase while negative for decrease
	 */
	public static void stateModify(EntityPlayer player, StateType type, int ticks) {
		ControlData.get(player).stateModify(type, ticks);
	}

	/**
	 * Cancel some state
	 */
	public static void stateCancel(EntityPlayer player, StateType type) {
		ControlData.get(player).stateCancel(type);
	}
	
	private ControlManager() {	
	}
	
}
