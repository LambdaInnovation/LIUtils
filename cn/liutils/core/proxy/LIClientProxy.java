/**
 * 
 */
package cn.liutils.core.proxy;

import org.lwjgl.input.Keyboard;

import cn.liutils.api.client.render.RenderEmptyBlock;
import cn.liutils.api.client.render.RenderPlayerHelper;
import cn.liutils.api.client.render.RenderTrail;
import cn.liutils.api.entity.EntityPlayerRender;
import cn.liutils.api.entity.EntityTrailFX;
import cn.liutils.core.LIUtilsMod;
import cn.liutils.core.client.register.LIKeyProcess;
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
}
