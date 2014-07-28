package cn.liutils.api.client.render;

import java.util.Random;

import cn.liutils.api.client.model.ITileEntityModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderTileEntityModel extends TileEntitySpecialRenderer {

	private ITileEntityModel model;
	protected static Random rng = new Random();
	
	public RenderTileEntityModel(ITileEntityModel mo) {
		model = mo;
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
			double d2, float f) {
		model.render(tileentity, d0, d1, d2, 0, 0.0625F, 1.0F);
	}

}
