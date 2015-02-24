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
package cn.weaponry.api.info;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

/**
 * @author WeathFolD
 */
@RegistrationClass
@RegEventHandler(Bus.FML)
public class InfManager {
	
	private static Map<EntityPlayer, InfWeapon> 
		table_client = new HashMap(),
		table_server = new HashMap();

	public static InfWeapon getInfo(EntityPlayer player) {
		Map<EntityPlayer, InfWeapon> table = player.worldObj.isRemote ? table_client : table_server;
		InfWeapon ret = table.get(player);
		if(ret == null) {
			ret = new InfWeapon(player);
			table.put(player, ret);
		}
		return ret;
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		getInfo(event.player).onUpdate();
	}

}
