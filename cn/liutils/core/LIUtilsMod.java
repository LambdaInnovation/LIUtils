/**
 * 
 */
package cn.liutils.core;

import net.minecraft.command.CommandHandler;
import net.minecraft.entity.Entity;
import org.apache.logging.log4j.Logger;

import cn.liutils.api.debug.command.Command_GetRenderInf;
import cn.liutils.api.debug.command.Command_SetMode;
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
 * LIUtils mod主加载模块
 * @author WeAthFolD
 */
@Mod(modid = "LIutils", name = "LIUtils", version = LIUtilsMod.VERSION)
public class LIUtilsMod {
	
	public static final String VERSION = "1.3.0";
	
	public static final String DEPENDENCY = "required-after:LIutils@" + VERSION;
	
	public static final boolean DEBUG = false; //璇峰湪缂栬瘧鏃惰缃负false
	
	@Instance("LIutils")
	public static LIUtilsMod instance;
	
	@SidedProxy(serverSide = "cn.liutils.core.proxy.LICommonProxy", clientSide = "cn.liutils.core.proxy.LIClientProxy")
	public static LICommonProxy proxy;
	
	public static Logger log = FMLLog.getLogger();
	
	/**
	 * 棰勫姞杞斤紙璁剧疆銆佷笘鐣岀敓鎴愩�娉ㄥ唽Event锛�
	 * @param event
	 */
	@EventHandler()
	public void preInit(FMLPreInitializationEvent event) {

		log.info("Starting LIUtils " + VERSION);
		log.info("Copyright (c) Lambda Innovation, 2013");
		log.info("http://www.lambdacraft.cn");
		
		proxy.preInit();
	}
	
	/**
	 * 鍔犺浇锛堟柟鍧椼�鐗╁搧銆佺綉缁滃鐞嗐�鍏朵粬)
	 * 
	 * @param Init
	 */
	@EventHandler()
	public void init(FMLInitializationEvent Init) {
		proxy.init();
	}
	
	private void registerEntity(Class<? extends Entity> cl, String name, int id) {
		registerEntity(cl, name, id, 32, 3, true);
	}
	
	private void registerEntity(Class<? extends Entity> cl, String name, int id, int trackRange, int freq, boolean updateVel) {
		EntityRegistry.registerModEntity(cl, name, id, instance, trackRange, freq, updateVel);
	}
	
	/**
	 * 鍔犺浇鍚庯紙淇濆瓨璁剧疆锛�
	 * 
	 * @param event
	 */
	@EventHandler()
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
	
	/**
	 * 鏈嶅姟鍣ㄥ姞杞斤紙娉ㄥ唽鎸囦护锛�
	 * 
	 * @param event
	 */
	@EventHandler()
	public void serverStarting(FMLServerStartingEvent event) {
		CommandHandler cm = (CommandHandler) event.getServer().getCommandManager();
		if(DEBUG) {
			cm.registerCommand(new Command_SetMode());
			cm.registerCommand(new Command_GetRenderInf());
		}	
	}
	
}
