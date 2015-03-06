/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.core.event;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.liutils.api.render.IPlayerRenderHook;
import cn.liutils.core.LIUtils;
import cn.liutils.core.energy.EnergyNet;
import cn.liutils.core.entity.EntityPlayerHook;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * @author WeAthFolD
 */
@RegistrationClass
@RegEventHandler({Bus.FML, Bus.Forge})
public class LITickEvents {

	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event) {
		World world = event.world;
		if(!LIUtils.ic2Exists)
			EnergyNet.onTick(world);
	}
	
}
