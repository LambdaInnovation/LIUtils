package cn.liutils.template.client.render.block;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import cn.liutils.api.render.model.ITileEntityModel;
import cn.liutils.util.RenderUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityModel extends TileEntitySpecialRenderer {

	private ITileEntityModel model;
	private ResourceLocation texture;
	protected static Random RNG = new Random();
	protected double yOffset = 0F;
	protected boolean reverse = true;
	protected double scale = 0.0625F;
	
	public RenderTileEntityModel(ITileEntityModel mo, ResourceLocation tex) {
		model = mo;
		texture = tex;
	}
	
	public RenderTileEntityModel setReverse(boolean b) {
		reverse = b;
		return this;
	}
	
	public RenderTileEntityModel setYOffset(double f) {
		yOffset = f;
		return this;
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1,
			double d2, float f) {
		GL11.glPushMatrix(); {
			GL11.glTranslated(d0 + .5, d1 + yOffset + (reverse ? 0 : 1.5), d2 + .5);
			if(reverse) {
				GL11.glScaled(-scale, -scale, scale);
			} else {
				GL11.glScaled(scale, scale, scale);
			}
			RenderUtils.loadTexture(texture);
			model.render(tileentity, 0F, 0F);
		} GL11.glPopMatrix();
	}

}
