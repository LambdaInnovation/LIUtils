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
package cn.liutils.core.client.render;

import java.util.Set;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.IPlayerRenderHook;
import cn.liutils.core.entity.EntityPlayerHook;

public class RenderPlayerHook extends Render {

	@Override
	public void doRender(Entity ent, double x, double y,
			double z, float wtf, float var9) {
		EntityPlayerHook hook = (EntityPlayerHook) ent;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		traverse(hook, hook.blend ? EntityPlayerHook.helpers_al : EntityPlayerHook.helpers_op);
		GL11.glPopMatrix();
	}
	
	private void traverse(EntityPlayerHook ent, Set<IPlayerRenderHook> hooks) {
		for(IPlayerRenderHook hook : hooks) {
			GL11.glPushMatrix();
			GL11.glRotatef(ent.rotationYaw, 0.0F, -1.0F, 0.0F);
			GL11.glPushMatrix(); {
				GL11.glTranslated(0, -1.67, 0);
				hook.renderBody(ent.player, ent.worldObj);
			} GL11.glPopMatrix();
			GL11.glTranslatef(0.0F, 0.5F, 0.0F);
			hook.renderHead(ent.player, ent.worldObj);
			GL11.glPopMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return null;
	}
	
}