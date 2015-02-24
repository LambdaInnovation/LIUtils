/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.util.space;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 * Utility class providing position and the block instance information.
 * @author WeAthFolD
 */
public class BlockPos {

	public int x, y, z;
	public Block block;
	
	public BlockPos(int par1, int par2, int par3) {
		x = par1;
		y = par2;
		z = par3;
	}

	public BlockPos(int par1, int par2, int par3, Block bID) {
		this(par1, par2, par3);
		block = bID;
	}
	
	public BlockPos(TileEntity te) {
		this(te.xCoord, te.yCoord, te.zCoord, te.blockType);
	}

	/**
	 * Judegs only position.
	 */
	@Override
	public boolean equals(Object b) {
		if (b == null || !(b instanceof BlockPos))
			return false;
		BlockPos bp = (BlockPos) b;
		return (x == bp.x && y == bp.y && z == bp.z);
	}
	
	@Override
	public int hashCode() {
		return x << 8 + y << 4 + z;
	}

	public BlockPos copy() {
		return new BlockPos(x, y, z, block);
	}

}
