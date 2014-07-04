/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.client.render;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.entity.EntityPlayerRenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * @author WeAthFolD
 */
public class RenderPlayerHelper extends Render {

	
	
	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.entity.Render#doRender(net.minecraft.entity.Entity, double, double, double, float, float)
	 */
	@Override
	public void doRender(Entity entity, double d0, double d1, double d2,
			float f, float f1) {
		EntityPlayerRenderHelper ap = (EntityPlayerRenderHelper) entity;
		for(PlayerRenderHelper p : EntityPlayerRenderHelper.activeHelpers) {
			GL11.glPushMatrix();
			GL11.glTranslated(d0, d1, d2);
			GL11.glRotatef(ap.rotationYaw, 0.0F, -1.0F, 0.0F);
			p.renderBody(ap.player, ap.worldObj);
			GL11.glTranslatef(0.0F, 0.5F, 0.0F);
			p.renderHead(ap.player, ap.worldObj);
			GL11.glPopMatrix();
		}
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.entity.Render#getEntityTexture(net.minecraft.entity.Entity)
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

}
