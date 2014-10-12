/**
 * Copyright (C) Lambda-Innovation, 2013-2014
 * This code is open-source. Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 */
package cn.liutils.api.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.block.BlockDirectionedMulti;
import cn.liutils.api.client.model.ITileEntityModel;
import cn.liutils.api.client.util.RenderUtils;

/**
 * @author WeAthFolD
 *
 */
public class RenderTileModelSided extends TileEntitySpecialRenderer {
	
	protected ITileEntityModel theModel;
	protected float scale = 0.0625F;
	protected double offX, offY, offZ;
	protected ResourceLocation texture;

	/**
	 * 
	 */
	public RenderTileModelSided(ITileEntityModel mdl) {
		theModel = mdl;
	}
	
	public RenderTileModelSided setScale(float f) {
		scale = f;
		return this;
	}
	
	public RenderTileModelSided setOffset(double x, double y, double z) {
		offX = x;
		offY = y;
		offZ = z;
		return this;
	}
	
	public RenderTileModelSided setModelTexture(ResourceLocation t) {
		texture = t;
		return this;
	}
	
	public ResourceLocation getTexture() {
		return texture;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#renderTileEntityAt(net.minecraft.tileentity.TileEntity, double, double, double, float)
	 */
	@Override
	public void renderTileEntityAt(TileEntity var1, double var2, double var4,
			double var6, float var8) {
		int meta = var1.getBlockMetadata();
		Vec3 rotate = ((BlockDirectionedMulti)var1.getBlockType()).getOffsetRotated(BlockDirectionedMulti.getFacingDirection(meta).ordinal());
		GL11.glPushMatrix(); {
			GL11.glTranslated(var2 + offX + rotate.xCoord, var4 + offY + rotate.yCoord, var6 + offZ + rotate.zCoord);
			renderAtOrigin(var1);
		} GL11.glPopMatrix();
	}
	
	protected final float rotations[] = { 180, 90, 0, -90 };
	protected void renderAtOrigin(TileEntity te) {
		if(te.getBlockMetadata() >> 2 != 0) return;
		int meta = te.getBlockMetadata();
		ResourceLocation tex = getTexture();
		if(tex != null) RenderUtils.loadTexture(tex);
		GL11.glPushMatrix(); {
			GL11.glRotatef(rotations[meta], 0F, 1F, 0F);
			GL11.glScalef(scale, scale, scale);
			theModel.render(te, 0F, 0F);
		} GL11.glPopMatrix();
	}

}
