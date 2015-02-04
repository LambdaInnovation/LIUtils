/**
 * 
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

	public static final String VERSION = "1.0.0";
	
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
