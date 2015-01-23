/**
 * 
 */
package cn.liutils.util.space;

import net.minecraft.block.Block;

/**
 * @author WeathFolD
 *
 */
public interface IBlockFilter {
	public static IBlockFilter always = new IBlockFilter() {
		@Override
		public boolean accepts(Block block) {
			return true;
		}
	};
	
	boolean accepts(Block block);
}
