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
package cn.weaponry.core;

import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.annoreg.core.RegistrationManager;
import cn.annoreg.core.RegistrationMod;
import cn.annoreg.mc.RegMessageHandler;
import cn.liutils.core.LIUtils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * @author WeathFolD
 *
 */
@Mod(modid = "weaponry", name = "LIUtils-Weaponry", version = LIUtils.VERSION)
@RegistrationMod(pkg = "cn.weaponry.", res = "weaponry", prefix = "mw_")
public class Weaponry {
	
	public static final String DEPENDENCY = "required-after:weaponry@" + LIUtils.VERSION;
	
	private static final String NET_CHANNEL = "weaponry";
	
	@Instance("weaponry")
	public static Weaponry instance;
	
	public static Logger log = LogManager.getLogger("Weaponry");
	
	public static Configuration config;
	
	@RegMessageHandler.WrapperInstance
	public static SimpleNetworkWrapper network = NetworkRegistry.INSTANCE.newSimpleChannel(NET_CHANNEL);
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		RegistrationManager.INSTANCE.registerAll(this, "PreInit");
		
		RegistrationManager.INSTANCE.registerAll(this, LIUtils.REGISTER_TYPE_CONFIGURABLE);
		RegistrationManager.INSTANCE.registerAll(this, LIUtils.REGISTER_TYPE_KEYHANDLER);
		RegistrationManager.INSTANCE.registerAll(this, LIUtils.REGISTER_TYPE_RENDER_HOOK);
	}
	
	@EventHandler
	public void init(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		RegistrationManager.INSTANCE.registerAll(this, "Init");
	}
	
	@EventHandler
	public void postInit(FMLPreInitializationEvent event) {
		RegistrationManager.INSTANCE.registerAll(this, "PostInit");
	}
	
	@EventHandler()
	public void serverStarting(FMLServerStartingEvent event) {
        RegistrationManager.INSTANCE.registerAll(this, "StartServer");
	}

}
