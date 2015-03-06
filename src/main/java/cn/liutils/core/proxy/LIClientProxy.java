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
package cn.liutils.core.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import cn.liutils.core.LIUtils;
import cn.liutils.core.client.render.RenderTrail;
import cn.liutils.core.event.LIClientEvents;
import cn.liutils.core.player.MouseHelperX;
import cn.liutils.template.client.render.block.RenderEmptyBlock;
import cn.liutils.template.client.render.entity.RenderCrossedProjectile;
import cn.liutils.template.entity.EntityBulletFX;
import cn.liutils.template.entity.EntityTrailFX;
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
		
		RenderingRegistry.registerEntityRenderingHandler(EntityTrailFX.class, new RenderTrail());
		
	}
	
	@Override
	public void preInit() {
		super.preInit();
		MinecraftForge.EVENT_BUS.register(new LIClientEvents());
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
