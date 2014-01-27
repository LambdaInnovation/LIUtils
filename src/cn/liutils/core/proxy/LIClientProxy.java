/**
 * 
 */
package cn.liutils.core.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraftforge.common.MinecraftForge;
import cn.liutils.api.client.render.RenderEntityBlock;
import cn.liutils.api.client.render.RenderPlayerHelper;
import cn.liutils.api.client.render.RenderTrail;
import cn.liutils.api.debug.Debug_ProcessorModel;
import cn.liutils.api.debug.KeyMoving;
import cn.liutils.api.entity.EntityBlock;
import cn.liutils.api.entity.EntityPlayerRenderHelper;
import cn.liutils.api.entity.EntityTrailFX;
import cn.liutils.core.LIUtilsMod;
import cn.liutils.core.client.register.LIClientTickHandler;
import cn.liutils.core.client.register.LIKeyProcess;
import cn.liutils.core.client.register.LISoundRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * @author WeAthFolD
 *
 */
public class LIClientProxy extends LICommonProxy {

	public static LIClientTickHandler clientTickHandler = new LIClientTickHandler();
	
	@Override
	public void init() {
		RenderingRegistry.registerEntityRenderingHandler(EntityTrailFX.class, new RenderTrail());
	}
	
	@Override
	public void preInit() {
		
		MinecraftForge.EVENT_BUS.register(new LISoundRegistry());
		TickRegistry.registerTickHandler(clientTickHandler, Side.CLIENT);
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayerRenderHelper.class, new RenderPlayerHelper());
		RenderingRegistry.registerEntityRenderingHandler(EntityBlock.class, new RenderEntityBlock());
		
		if(LIUtilsMod.DEBUG) {
			KeyMoving key = new KeyMoving();
			KeyMoving.addProcess(new Debug_ProcessorModel());
			LIKeyProcess.addKey("debug_up", Keyboard.KEY_UP, true, key);
			LIKeyProcess.addKey("debug_down", Keyboard.KEY_DOWN, true, key);
			LIKeyProcess.addKey("debug_left", Keyboard.KEY_LEFT, true, key);
			LIKeyProcess.addKey("debug_right", Keyboard.KEY_RIGHT, true, key);
			LIKeyProcess.addKey("debug_forward", Keyboard.KEY_NUMPAD8, true, key);
			LIKeyProcess.addKey("debug_back", Keyboard.KEY_NUMPAD2, true, key);
		}
	}
	
	@Override
	public void postInit() {
		TickRegistry.registerTickHandler(new LIKeyProcess(), Side.CLIENT);
	}
}
