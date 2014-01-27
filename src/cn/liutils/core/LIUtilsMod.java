/**
 * 
 */
package cn.liutils.core;

import java.util.logging.Logger;

import net.minecraft.command.CommandHandler;
import net.minecraft.entity.Entity;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.EntityRegistry;
import cn.liutils.api.debug.command.Command_GetRenderInf;
import cn.liutils.api.debug.command.Command_SetMode;
import cn.liutils.api.entity.EntityBlock;
import cn.liutils.core.proxy.LICommonProxy;
import cn.liutils.core.proxy.LIGeneralProps;

/**
 * LIUtils mod的主注册类。
 * @author WeAthFolD
 */
@Mod(modid = "LIutils", name = "LIUtils", version = LIUtilsMod.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class LIUtilsMod {
	
	public static final String VERSION = "1.2.0";
	
	public static final String DEPENDENCY = "required-after:LIutils@" + VERSION;
	
	public static final boolean DEBUG = false; //请在编译时设置为false
	
	@Instance("LIutils")
	public static LIUtilsMod instance;
	
	@SidedProxy(serverSide = "cn.liutils.core.proxy.LICommonProxy", clientSide = "cn.liutils.core.proxy.LIClientProxy")
	public static LICommonProxy proxy;
	
	public static Logger log = Logger.getLogger("LIUtils");
	
	/**
	 * 预加载（设置、世界生成、注册Event）
	 * @param event
	 */
	@EventHandler()
	public void preInit(FMLPreInitializationEvent event) {

		log.setParent(FMLLog.getLogger());
		log.info("Starting LIUtils " + VERSION);
		log.info("Copyright (c) Lambda Innovation, 2013");
		log.info("http://www.lambdacraft.cn");
		
		proxy.preInit();
	}
	
	/**
	 * 加载（方块、物品、网络处理、其他)
	 * 
	 * @param Init
	 */
	@EventHandler()
	public void init(FMLInitializationEvent Init) {
		proxy.init();
		
		registerEntity(EntityBlock.class, "entity_block", LIGeneralProps.ENT_ID_BLOCK);
	}
	
	private void registerEntity(Class<? extends Entity> cl, String name, int id) {
		registerEntity(cl, name, id, 32, 3, true);
	}
	
	private void registerEntity(Class<? extends Entity> cl, String name, int id, int trackRange, int freq, boolean updateVel) {
		EntityRegistry.registerModEntity(cl, name, id, instance, trackRange, freq, updateVel);
	}
	
	/**
	 * 加载后（保存设置）
	 * 
	 * @param event
	 */
	@EventHandler()
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
	
	/**
	 * 服务器加载（注册指令）
	 * 
	 * @param event
	 */
	@EventHandler()
	public void serverStarting(FMLServerStartingEvent event) {
		CommandHandler cm = (CommandHandler) event.getServer().getCommandManager();
		cm.registerCommand(new Command_SetMode());
		cm.registerCommand(new Command_GetRenderInf());
	}
	
}
