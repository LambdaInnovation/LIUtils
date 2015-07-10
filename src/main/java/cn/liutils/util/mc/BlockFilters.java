package cn.liutils.util.mc;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class BlockFilters {
	
	/**
	 * This filter filters NO blocks, so it collides with water or grass blocks.
	 */
	public static final IBlockFilter filNothing = new IBlockFilter() {

		@Override
		public boolean accepts(World world, int x, int y, int z, Block block) {
			return block != Blocks.air;
		}
		
	},
	
	filNormal = new IBlockFilter() {

		@Override
		public boolean accepts(World world, int x, int y, int z, Block block) {
			Block b = world.getBlock(x, y, z);
			return b.getCollisionBoundingBoxFromPool(world, x, y, z) != null && 
					b.canCollideCheck(world.getBlockMetadata(x, y , z), false);
		}
		
	};
	
	

}
