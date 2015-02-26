/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.core.control;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.weaponry.api.info.InfManager;
import cn.weaponry.core.Weaponry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * @author WeathFolD
 *
 */
@RegistrationClass
public class ControlManager {
	
	@RegEventHandler(Bus.FML)
	public static ControlManager instance = new ControlManager();

	public static final int
		BEAT_RATE = 300,
		BEAT_TIMEOUT = 1000;
	
	private static class PlayerState {
		public KeyState[] stateArr = {
			new KeyState(),
			new KeyState(),
			new KeyState()
		};
	}
	
	private static class KeyState {
		public boolean down;
		public long beatTime; //delta time since last heart beat sync.
	}
	
	public Map<EntityPlayer, PlayerState> 
		table_client = new HashMap(),
		table_server = new HashMap();
	
	public void onKeyState(EntityPlayer player, int id, boolean b) {
		KeyState ks = getFor(player).stateArr[id];
		//Does additional validating.
		if(b) {
			if(!ks.down) {
				ks.down = true;
				InfManager.getInfo(player).handleRawInput(id, true);
			}
			ks.beatTime = Minecraft.getSystemTime();
		} else {
			if(ks.down) {
				ks.down = false;
				InfManager.getInfo(player).handleRawInput(id, false);
			}
		}
	}
	
	public void onKeyTick(EntityPlayer player, int id) {
		KeyState ks = getFor(player).stateArr[id];
		if(ks.down) {
			ks.beatTime = Minecraft.getSystemTime();
		}
	}
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		PlayerState state = getFor(player);
		for(int i = 0; i < 3; ++i) {
			KeyState ks = state.stateArr[i];
			if(ks.down) {
				long dt = Minecraft.getSystemTime() - ks.beatTime;
				if(dt > BEAT_TIMEOUT) {
					Weaponry.log.error("Cancelled control of " + player.getCommandSenderName() + " because of timeout.");
					InfManager.getInfo(player).handleRawInput(i, false);
					ks.down = false;
				} else {
					InfManager.getInfo(player).handleKeyTick(i);
				}
			}
		}
	}
	
	PlayerState getFor(EntityPlayer player) {
		Map<EntityPlayer, PlayerState> table = player.worldObj.isRemote ? table_client : table_server;
		PlayerState ret = table.get(player);
		if(ret == null) {
			ret = new PlayerState();
			table.put(player, ret);
		}
		return ret;
	}

}
