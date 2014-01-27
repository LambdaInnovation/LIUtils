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

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cn.liutils.api.client.util.RenderUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 渲染单个Icon为一个实体的实用类。
 */
@SideOnly(Side.CLIENT)
public class RenderIcon extends Render {
	
	protected ResourceLocation icon;
	private boolean renderBlend = false;
	protected float alpha = 1.0F;
	private float size = 0.5F;
	protected boolean enableDepth = true;
	protected boolean hasLight = false;

	public RenderIcon(ResourceLocation ic) {
		icon = ic;
	}

	public RenderIcon setBlend(float a) {
		renderBlend = true;
		alpha = a;
		return this;
	}
	
	public RenderIcon setSize(float s) {
		size = s;
		if(size <= 0.0F)
			size = 1.0F;
		return this;
	}
	
	public RenderIcon setHasLight(boolean b) {
		hasLight = b;
		return this;
	}
	
	public RenderIcon setEnableDepth(boolean b) {
		enableDepth = b;
		return this;
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity par1Entity, double par2, double par4,
			double par6, float par8, float par9) {
		if (icon != null) {
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			if(!enableDepth) 
				GL11.glDisable(GL11.GL_DEPTH_TEST);
			if(!hasLight)
				GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glTranslatef((float) par2, (float) par4, (float) par6);
			GL11.glScalef(size, size, size);
			RenderUtils.loadTexture(icon);
			
			Tessellator tessellator = Tessellator.instance;
			this.func_77026_a(tessellator);
			
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}

	private void func_77026_a(Tessellator tessllator) {
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F,
				0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		if(!hasLight) 
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		tessllator.startDrawingQuads();
		if(!hasLight) 
			tessllator.setBrightness(15728880);
		tessllator.setColorRGBA_F(1.0F, 1.0F, 1.0F, alpha);
		tessllator.setNormal(0.0F, 1.0F, 0.0F);
		tessllator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, 0, 0);
		tessllator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, 0, 1);
		tessllator.addVertexWithUV(f4 - f5, f4 - f6, 0.0D, 1, 1);
		tessllator.addVertexWithUV(0.0F - f5, f4 - f6, 0.0D, 1, 0);
		tessllator.draw();
	}


	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO 自动生成的方法存根
		return icon;
	}
}
