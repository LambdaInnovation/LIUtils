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
package cn.liutils.api.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.IModelCustom;

/**
 * Simple wrapper of IModelCustom.
 * @author WeAthFolD
 *
 */
public class TileEntityModelCustom implements ITileEntityModel {
	
	IModelCustom theModel;

	/**
	 * 
	 */
	public TileEntityModelCustom(IModelCustom model) {
		theModel = model;
	}

	/* (non-Javadoc)
	 * @see cn.liutils.api.client.model.ITileEntityModel#render(net.minecraft.tileentity.TileEntity, double, double, double, float, float, float)
	 */
	@Override
	public void render(TileEntity is, double x, double y, double z, float f1,
			float scale, float f) {
		GL11.glTranslated(x, y, z);
		theModel.renderAll();
	}

}
