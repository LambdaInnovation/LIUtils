package cn.liutils.core.client.render;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.IPlayerRenderHook;
import cn.liutils.core.entity.EntityPlayerHook;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderPlayerHook extends Render {

	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		EntityPlayerHook ent = (EntityPlayerHook) entity;
		for(IPlayerRenderHook handler : ent.activeHelpers) {
			GL11.glPushMatrix();
			GL11.glTranslated(d0, d1, d2);
			GL11.glRotatef(ent.rotationYaw, 0.0F, -1.0F, 0.0F);
			GL11.glPushMatrix(); {
				GL11.glTranslated(0, -1.67, 0);
				handler.renderBody(ent.player, ent.worldObj);
			} GL11.glPopMatrix();
			GL11.glTranslatef(0.0F, 0.5F, 0.0F);
			handler.renderHead(ent.player, ent.worldObj);
			GL11.glPopMatrix();
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}