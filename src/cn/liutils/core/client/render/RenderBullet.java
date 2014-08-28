/** 
 * Copyright (c) Lambda Innovation Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.cn/
 * 
 * The mod is open-source. It is distributed under the terms of the
 * Lambda Innovation Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * 本Mod是完全开源的，你允许参考、使用、引用其中的任何代码段，但不允许将其用于商业用途，在引用的时候，必须注明原作者。
 */
package cn.liutils.core.client.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.client.util.RenderUtils;
import cn.liutils.api.entity.EntityBullet;
import cn.liutils.api.util.Motion3D;

/**
 * @author WeAthFolD
 *
 */
public class RenderBullet extends Render {

	protected double LENGTH;
	protected double HEIGHT;
	protected String TEXTURE_PATH;
	protected boolean renderTexture = true;
	protected float colorR, colorG, colorB;
	protected boolean ignoreLight = false;
	
	public RenderBullet(double l, double h, String texturePath) {
		LENGTH = l;
		HEIGHT = h;
		TEXTURE_PATH = texturePath;
	}
	
	public RenderBullet(double l, double h, float a, float b, float c) {
		LENGTH = l;
		HEIGHT = h;
		setColor3f(a, b, c);
	}
	
	public RenderBullet setColor3f(float a, float b, float c) {
		renderTexture = false;
		colorR = a;
		colorG = b;
		colorB = c;
		return this;
	}
	
	public RenderBullet setIgnoreLight(boolean b) {
		ignoreLight = b;
		return this;
	}

	@Override
	public void doRender(Entity entity, double par2, double par4,
			double par6, float par8, float par9) {
		new Motion3D(entity);
		Tessellator tessellator = Tessellator.instance;

		GL11.glPushMatrix();

		Vec3 v1 = newV3(0, HEIGHT, 0), v2 = newV3(0, -HEIGHT, 0), v3 = newV3(
				LENGTH, -HEIGHT, 0), v4 = newV3(LENGTH, HEIGHT, 0),

		v5 = newV3(0, 0, -HEIGHT), v6 = newV3(0, 0, HEIGHT), v7 = newV3(LENGTH,
				0, HEIGHT), v8 = newV3(LENGTH, 0, -HEIGHT);

		GL11.glTranslatef((float) par2, (float) par4, (float) par6);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		if(renderTexture)
			RenderUtils.loadTexture(TEXTURE_PATH);
		else {
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(colorR, colorG, colorB);
		}
		
		if(ignoreLight)
			GL11.glDisable(GL11.GL_LIGHTING);
		
		GL11.glRotatef(270.0F - entity.rotationYaw, 0.0F, -1.0F, 0.0F); // 左右旋转
		GL11.glRotatef(entity.rotationPitch, 0.0F, 0.0F, -1.0F); // 上下旋转
		if(entity instanceof EntityBullet) {
			if(((EntityBullet)entity).renderFromLeft) {
				GL11.glTranslatef(0.0F, 0.0F, 0.3F);
			}
		}
		
		if(ignoreLight) 
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		tessellator.startDrawingQuads();
		if(ignoreLight) 
			tessellator.setBrightness(15728880);
		
		RenderUtils.addVertex(v1, 0, 1);
		RenderUtils.addVertex(v2, 1, 1);
		RenderUtils.addVertex(v3, 1, 0);
		RenderUtils.addVertex(v4, 0, 0);
		
		RenderUtils.addVertex(v5, 0, 1);
		RenderUtils.addVertex(v6, 1, 1);
		RenderUtils.addVertex(v7, 1, 0);
		RenderUtils.addVertex(v8, 0, 0);
		
		tessellator.draw(); 
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public static Vec3 newV3(double x, double y, double z) {
		return Vec3.createVectorHelper(x, y, z);
	}



	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		// TODO 自动生成的方法存根
		return null;
	}
}
