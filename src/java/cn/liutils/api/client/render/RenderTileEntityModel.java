package cn.liutils.api.client.render;

import java.util.Random;

import org.lwjgl.opengl.GL11;

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
		GL11.glScalef(0.0625F, 0.0625F, 0.0625F);
		GL11.glTranslated(d0, d1, d2);
		model.render(tileentity, 0F, 0F);
	}

}
