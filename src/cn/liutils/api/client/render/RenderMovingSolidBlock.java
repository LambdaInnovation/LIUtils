package cn.liutils.api.client.render;

import cn.liutils.api.entity.EntityMovingSolidBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.src.FMLRenderAccessLibrary;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderMovingSolidBlock extends Render {

	private final RenderBlocks renderBlocks = new RenderBlocks();

	public RenderMovingSolidBlock() {
		this.shadowSize = 5.0f;
	}

	@Override
	public void doRender(Entity var1, double x, double y, double z,
			float pitch, float yaw) {
		EntityMovingSolidBlock e = (EntityMovingSolidBlock) var1;
		renderBlocks.setRenderBoundsFromBlock(e.block);
		renderBlocks.renderBlockSandFalling(e.block, e.worldObj,
				(int)x, (int)y, (int)z, (int)0);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity var1) {
		return null;
	}

}
