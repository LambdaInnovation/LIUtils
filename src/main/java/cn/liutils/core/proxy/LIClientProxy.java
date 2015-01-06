/**
 * 
 */
package cn.liutils.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import cn.liutils.core.LIUtils;
import cn.liutils.core.client.render.RenderPlayerHook;
import cn.liutils.core.client.render.RenderTrail;
import cn.liutils.core.entity.EntityPlayerHook;
import cn.liutils.core.event.LIClientEvents;
import cn.liutils.core.player.MouseHelperX;
import cn.liutils.template.client.render.block.RenderEmptyBlock;
import cn.liutils.template.client.render.entity.RenderCrossedProjectile;
import cn.liutils.template.entity.EntityBulletFX;
import cn.liutils.template.entity.EntityTrailFX;
import cn.liutils.util.render.TextUtils;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * @author WeAthFolD
 *
 */
public class LIClientProxy extends LICommonProxy {
	
	@Override
	public void init() {
		super.init();
		RenderingRegistry.registerBlockHandler(new RenderEmptyBlock());
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBulletFX.class, 
				new RenderCrossedProjectile(0.45, 0.03, 1F, 0.96F, 0.722F) {
			@Override
			public void doRender(Entity entity, double par2, double par4,
					double par6, float par8, float par9) {
				EntityBulletFX bullet = (EntityBulletFX) entity;
				fpOffsetZ = bullet.renderFromLeft ? 0.2 : -0.2;
				tpOffsetZ = bullet.renderFromLeft ? 0.4 : -0.4;
				super.doRender(entity, par2, par4, par6, par8, par9);
			}
		}
		.setIgnoreLight(true));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityPlayerHook.class, new RenderPlayerHook());
		RenderingRegistry.registerEntityRenderingHandler(EntityTrailFX.class, new RenderTrail());
		
	}
	
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new LIClientEvents());
		TextUtils.init();
	}
	
	@Override
	public void postInit() {
		super.postInit();
		Minecraft.getMinecraft().mouseHelper = new MouseHelperX();
	}
	
	@Override
	public void cmdInit(CommandHandler handler) {
		if(LIUtils.DEBUG) {
		}
	}
}
