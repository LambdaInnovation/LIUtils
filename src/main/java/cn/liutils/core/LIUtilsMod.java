
package cn.liutils.core;

import net.minecraft.command.CommandHandler;
import net.minecraft.entity.Entity;

import org.apache.logging.log4j.Logger;

import cn.liutils.core.energy.EnergyNet;
import cn.liutils.core.proxy.LICommonProxy;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

/**
 * LIUtils is a core support mod, written by Lambda Innovation.
 * It's contents involve rendering, key listening, debugging and other many stuffs.
 * #cn.liutils.api is what user should invoke. It contains all the main functions.
 * #cn.liutils.core is core registration module, and should not be modified or invoked normally.
 * @author WeAthFolD
 */
@Mod(modid = "LIutils", name = "LIUtils", version = LIUtilsMod.VERSION)
public class LIUtilsMod {
	
	/**
	 * Version Number.
	 */
	public static final String VERSION = "1.7.2.000dev";
	
	/**
	 * Does open debug mode. turn to false when compiling.
	 */
	public static final boolean DEBUG = true;
	
	/**
	 * The mod dependency. put this in your mod's dependency if you want to use LIUtils.
	 */
	public static final String DEPENDENCY = "required-after:LIutils@" + VERSION;
	
	@Instance("LIutils")
	public static LIUtilsMod instance;
	
	@SidedProxy(serverSide = "cn.liutils.core.proxy.LICommonProxy", clientSide = "cn.liutils.core.proxy.LIClientProxy")
	public static LICommonProxy proxy;
	
	public static Logger log = FMLLog.getLogger();
	
	public static boolean ic2Exists = false;
	
	@EventHandler()
	public void preInit(FMLPreInitializationEvent event) {

		log.info("Starting LIUtils " + VERSION);
		log.info("Copyright (c) Lambda Innovation, 2013");
		log.info("http://www.lambdacraft.cn");
		
		try {
			Class.forName("ic2.core.IC2");
			ic2Exists = true;
		} catch(Exception e) {}
		
		if(DEBUG)
			log.info("IC2 internal network state: " + ic2Exists);
		
		if(!ic2Exists)
			EnergyNet.initialize();
		
		proxy.preInit();
	}
	
	@EventHandler()
	public void init(FMLInitializationEvent Init) {
		proxy.init();
	}
	
	public void registerEntity(Class<? extends Entity> cl, String name, int id) {
		registerEntity(cl, name, id, 32, 3, true);
	}
	
	private void registerEntity(Class<? extends Entity> cl, String name, int id, int trackRange, int freq, boolean updateVel) {
		EntityRegistry.registerModEntity(cl, name, id, instance, trackRange, freq, updateVel);
	}
	
	@EventHandler()
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
	
	@EventHandler()
	public void serverStarting(FMLServerStartingEvent event) {
		CommandHandler cm = (CommandHandler) event.getServer().getCommandManager();
		proxy.cmdInit(cm);
	}
	
}
