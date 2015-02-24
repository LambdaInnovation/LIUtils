/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.client.render.entity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * Most simple model renderer.
 * @author WeAthFolD
 */
public class RenderModel extends Render {

	protected ModelBase model;
	protected float modelScale;
	protected ResourceLocation texture;
	protected float offsetX = 0.0F, offsetY = 0.0F, offsetZ = 0.0F;

	public RenderModel(ModelBase m, ResourceLocation texturePath, float scale) {
		model = m;
		modelScale = scale;
		texture = texturePath;
	}
	
	public RenderModel setOffset(float x, float y, float z) {
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		return this;
	}

	@Override
	public void doRender(Entity entity, double par2, double par4, double par6,
			float par8, float par9) {

		GL11.glPushMatrix(); {
			bindTexture(texture);
			GL11.glTranslatef((float) par2, (float) par4 + 2 * entity.height, (float) par6);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
			GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(-1.0F, -1.0F, 1.0F);
			this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F * modelScale);
		} GL11.glPopMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}



}
