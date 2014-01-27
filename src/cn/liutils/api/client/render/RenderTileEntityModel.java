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

import java.util.Random;

import cn.liutils.api.client.model.ITileEntityModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

/**
 * @author WeAthFolD
 *
 */
public class RenderTileEntityModel extends TileEntitySpecialRenderer {

	private ITileEntityModel model;
	protected static Random rng = new Random();
	
	/**
	 * 
	 */
	public RenderTileEntityModel(ITileEntityModel mo) {
		model = mo;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer#renderTileEntityAt(net.minecraft.tileentity.TileEntity, double, double, double, float)
	 */
	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
			double d2, float f) {
		model.render(tileentity, d0, d1, d2, 0, 0.0625F, 1.0F);
	}

}
