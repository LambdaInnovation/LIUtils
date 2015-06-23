package cn.liutils.util.mc;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public class BlockFilters {
	
	/**
	 * This filter filters NO blocks, so it collides with water or grass blocks.
	 */
	public static final IBlockFilter filNothing = new IBlockFilter() {

		@Override
		public boolean accepts(World world, int x, int y, int z) {
			return true;
		}
		
	},
	
	filNormal = new IBlockFilter() {

		@Override
		public boolean accepts(World world, int x, int y, int z) {
			Block b = world.getBlock(x, y, z);
			return b.getCollisionBoundingBoxFromPool(world, x, y, z) != null;
		}
		
	};
	
	

}
