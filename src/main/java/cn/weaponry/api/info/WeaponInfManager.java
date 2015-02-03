/**
 * 
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
public class WeaponInfManager {
	
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
