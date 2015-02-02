/**
 * 
 */
package cn.liutils.util.space;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * @author WeathFolD
 *
 */
public interface IBlockFilter {
	public static IBlockFilter always = new IBlockFilter() {
		@Override
		public boolean accepts(World world, Block block, int x, int y, int z) {
			return true;
		}
	};
	
	boolean accepts(World world, Block block, int x, int y, int z);
}
