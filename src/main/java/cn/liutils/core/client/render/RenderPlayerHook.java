package cn.liutils.core.client.render;

import java.util.Iterator;
import java.util.Set;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.liutils.api.render.IPlayerRenderHook;
import cn.liutils.core.entity.EntityPlayerHook;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

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