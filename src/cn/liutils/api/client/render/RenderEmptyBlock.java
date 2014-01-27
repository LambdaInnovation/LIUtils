/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.client.render;

import cn.liutils.core.proxy.LIGeneralProps;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * @author WeAthFolD
 *
 */
public class RenderEmptyBlock implements ISimpleBlockRenderingHandler {

	/**
	 * 
	 */
	public RenderEmptyBlock() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#renderInventoryBlock(net.minecraft.block.Block, int, int, net.minecraft.client.renderer.RenderBlocks)
	 */
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#renderWorldBlock(net.minecraft.world.IBlockAccess, int, int, int, net.minecraft.block.Block, int, net.minecraft.client.renderer.RenderBlocks)
	 */
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	/* (non-Javadoc)
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#shouldRender3DInInventory()
	 */
	@Override
	public boolean shouldRender3DInInventory() {
		return false;
	}

	/* (non-Javadoc)
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#getRenderId()
	 */
	@Override
	public int getRenderId() {
		return LIGeneralProps.RENDER_TYPE_EMPTY;
	}

}
