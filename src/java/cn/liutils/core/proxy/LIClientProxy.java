/**
 * 
 */
package cn.liutils.core.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.command.CommandHandler;
import cn.liutils.api.client.render.RenderEmptyBlock;
import cn.liutils.api.entity.EntityTrailFX;
import cn.liutils.core.LIUtilsMod;
import cn.liutils.core.client.register.LIKeyProcess;
import cn.liutils.core.client.render.RenderPlayerHelper;
import cn.liutils.core.client.render.RenderTrail;
import cn.liutils.core.debug.CommandModifier;
import cn.liutils.core.debug.KeyModifier;
import cn.liutils.core.debug.KeyShowInfo;
import cn.liutils.core.entity.EntityPlayerRender;
import cn.liutils.core.event.LIEventListener;
import cn.liutils.core.event.LITickEvents;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;

/**
 * @author WeAthFolD
 *
 */
public class LIClientProxy extends LICommonProxy {

	public static LIEventListener clientTickHandler = new LIEventListener();
	public static LIKeyProcess keyProcess;
	
	@Override
	public void init() {
		super.init();
		RenderingRegistry.registerBlockHandler(new RenderEmptyBlock());
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayerRender.class, new RenderPlayerHelper());
		RenderingRegistry.registerEntityRenderingHandler(EntityTrailFX.class, new RenderTrail());
		if(LIUtilsMod.DEBUG) {
			LIKeyProcess.addKey("a1", Keyboard.KEY_NUMPAD8, false, new KeyModifier(0, true));
			LIKeyProcess.addKey("a2", Keyboard.KEY_NUMPAD2, false, new KeyModifier(0, false));
			LIKeyProcess.addKey("a3", Keyboard.KEY_NUMPAD4, false, new KeyModifier(1, true));
			LIKeyProcess.addKey("a4", Keyboard.KEY_NUMPAD6, false, new KeyModifier(1, false));
			LIKeyProcess.addKey("a5", Keyboard.KEY_NUMPAD7, false, new KeyModifier(2, true));
			LIKeyProcess.addKey("a6", Keyboard.KEY_NUMPAD9, false, new KeyModifier(2, false));
			LIKeyProcess.addKey("a7", Keyboard.KEY_NUMPAD5, false, new KeyShowInfo());
		}
	}
	
	@Override
	public void preInit() {
		super.preInit();
		FMLCommonHandler.instance().bus().register(new LITickEvents());
	}
	
	@Override
	public void postInit() {
		super.postInit();
		keyProcess = new LIKeyProcess();
	}
	
	@Override
	public void cmdInit(CommandHandler handler) {
		if(LIUtilsMod.DEBUG) {
			handler.registerCommand(new CommandModifier());
		}
	}
}
