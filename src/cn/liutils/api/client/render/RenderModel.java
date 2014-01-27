/** 
 * Copyright (c) LambdaCraft Modding Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.half-life.cn/
 * 
 * LambdaCraft is open-source. It is distributed under the terms of the
 * LambdaCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * LambdaCraft是完全开源的。它的发布遵从《LambdaCraft开源协议》。你允许阅读，修改以及调试运行
 * 源代码， 然而你不允许将源代码以另外任何的方式发布，除非你得到了版权所有者的许可。
 */
package cn.liutils.api.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

/**
 * 简单成翔的模型渲染器。
 * 
 * @author WeAthFolD
 */
public class RenderModel extends Render {

	private ModelBase model;
	private float modelScale;
	private ResourceLocation texture;

	/**
	 * 
	 */
	public RenderModel(ModelBase m, ResourceLocation texturePath, float scale) {
		model = m;
		modelScale = scale;
		texture = texturePath;
	}

	@Override
	public void doRender(Entity entity, double par2, double par4, double par6,
			float par8, float par9) {

		GL11.glPushMatrix();
		//Load Texture
		bindTexture(texture);
		GL11.glTranslatef((float) par2, (float) par4 + 2 * entity.height,
				(float) par6);
		GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F,
				0.0625F * modelScale);
		GL11.glPopMatrix();

	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}



}
