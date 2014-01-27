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

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.util.Motion3D;

/**
 * @author WeAthFolD
 *
 */
public class RenderModelProjectile extends Render {

	private ModelBase model;
	
	protected ResourceLocation TEXTURE_PATH;
	
	protected float offsetX, offsetY, offsetZ;
	protected float scale;
	
	public RenderModelProjectile(ModelBase mdl, String texturePath) {
		TEXTURE_PATH = new ResourceLocation(texturePath);
		model = mdl;
	}
	
	public RenderModelProjectile(ModelBase mdl, ResourceLocation texturePath) {
		TEXTURE_PATH = texturePath;
		model = mdl;
	}
	
	public RenderModelProjectile setOffset(float x, float y, float z) {
		offsetX = x;
		offsetY = y;
		offsetZ = z;
		return this;
	}
	
	public RenderModelProjectile setScale(float s) {
		scale = s;
		return this;
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4,
			double par6, float par8, float par9) {
		Entity gren = par1Entity;
		Motion3D motion = new Motion3D(gren);
		
		GL11.glPushMatrix();
		
		bindTexture(TEXTURE_PATH);
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);
		GL11.glRotatef(180.0F - gren.rotationYaw, 0.0F, -1.0F, 0.0F); // 左右旋转
		GL11.glRotatef(gren.rotationPitch, 1.0F, 0.0F, 0.0F); // 上下旋转
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		model.render(par1Entity, (float)par2, (float)par4, (float)par6, par8, par9, 0.0625F);
		
		GL11.glPopMatrix();
		
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}


}
