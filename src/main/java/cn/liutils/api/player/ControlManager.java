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
 * 
 * @author Violet
 *
 */
public class ControlManager {

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
	
	private ControlManager() {	
	}
	
}
