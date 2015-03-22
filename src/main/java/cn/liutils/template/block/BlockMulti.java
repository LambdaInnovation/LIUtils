/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.block;

import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cn.liutils.core.LIUtils;
import cn.liutils.core.proxy.LIClientProps;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 */
public abstract class BlockMulti extends BlockContainer {
	
	List<SubBlockPos> subList = new ArrayList();
	List<SubBlockPos>[] buffer;
	
	@SideOnly(Side.CLIENT)
	double[][] rotCenters;
	
	boolean init = false;
	
	public class SubBlockPos {
		public final int dx, dy, dz;
		public SubBlockPos(int _dx, int _dy, int _dz) {
			dx = _dx;
			dy = _dy;
			dz = _dz;
		}
	}

	public BlockMulti(Material p_i45386_1_) {
		super(p_i45386_1_);
		addSubBlock(0, 0, 0);
		initSubBlock();
		finishInit();
	}
	
	//SubBlock definition API
	/**
	 * ONLY add sub blocks in this method (Called via super Ctor)
	 */
	public abstract void initSubBlock();
	
	public void addSubBlock(int dx, int dy, int dz) {
		if(init) {
			throw new RuntimeException("Trying to add a sub block after block init finished");
		}
		subList.add(new SubBlockPos(dx, dy, dz));
	}
	
	/**
	 * Accept a int[][3] array, add all the array element as a single subPos inside the list.
	 */
	public void addSubBlock(int[][] data) {
		for(int[] s : data) {
			addSubBlock(s[0], s[1], s[2]);
		}
	}
	
	private void finishInit() {
		//Pre-init rotated position offset list.
		buffer = new ArrayList[6];
		
		for(int i = 2; i <= 5; ++i) {
			ForgeDirection dir = ForgeDirection.values()[i];
			buffer[i] = new ArrayList();
			for(SubBlockPos s : subList) {
				buffer[i].add(rotate(s, dir));
			}
		}
		
		if(FMLCommonHandler.instance().getSide().isClient()) {
			double[] arr = getRotCenter();
			rotCenters = new double[][] {
					{}, {},
					{arr[0],  arr[1], arr[2]},
					{-arr[0], arr[1], -arr[2]},
					{arr[2],  arr[1], -arr[0]},
					{-arr[2], arr[1], arr[0]}
			};
		}
		//Finished, set the flag and encapsulate the instance.
		init = true;
	}
	
	//Rotation API
	//Some lookup tables
	private static final ForgeDirection[] rotMap = { NORTH, EAST, SOUTH, WEST }; //-Z, +X, +Z, -X
	private static final double[] drMap = {
		0, 0, 
		180, 0, -90, 90
	};
	private static final double[][] offsetMap = {
		{0, 0}, //placeholder
		{0, 0}, //placeholder
		{0, 0},
		{1, 1},
		{0, 1},
		{1, 0}
	};
	
	public double[] getPivotOffset(InfoBlockMulti info) {
		return getPivotOffset(info.dir);
	}
	
	public ForgeDirection getRotation(int l) {
		return rotMap[l];
	}
	
	/**
	 * Get the whole structure's (minX, minZ) point coord, in [dir = 0] (a.k.a: facing z-) point of view.
	 * @param side
	 * @return
	 */
	public double[] getPivotOffset(ForgeDirection dir) {
		return offsetMap[dir.ordinal()];
	}
	
	@SideOnly(Side.CLIENT)
	public abstract double[] getRotCenter();
	
	@SideOnly(Side.CLIENT)
	public double[] getOffsetRotated(ForgeDirection dir) {
		double arr[] = getRotCenter();
		switch(dir) {
		
		default:
			throw new RuntimeException("Invalid rotate direction");
		}
	}
	
	//Placement API
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
    	if(world.isRemote)
    		return;
    	
    	int l = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        ForgeDirection dir = rotMap[l];
        
    	//Set the origin block.
        TileEntity te = world.getTileEntity(x, y, z);
    	((IMultiTile)te).setBlockInfo(new InfoBlockMulti(te, dir, 0));
    	
        List<SubBlockPos> rotatedList = buffer[dir.ordinal()];
        //Check done in ItemBlockMulti, brutely replace here.
        for(int i = 1; i < rotatedList.size(); ++i) {
        	SubBlockPos sub = rotatedList.get(i);
        	int nx = x + sub.dx, ny = y + sub.dy, nz = z + sub.dz;
        	world.setBlock(nx, ny, nz, this);
        	te = world.getTileEntity(nx, ny, nz);
        	((IMultiTile)te).setBlockInfo(new InfoBlockMulti(te, dir, i));
        }
    }
	
    @Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)  {
    	if(world.isRemote)
    		return;
    	
    	TileEntity te = world.getTileEntity(x, y, z);
    	if(!(te instanceof IMultiTile)) {
    		LIUtils.log.error("Didn't find correct tile when breaking a BlockMulti.");
    		return;
    	}
    	InfoBlockMulti info = ((IMultiTile)te).getBlockInfo();
    	int[] origin = getOrigin(te);
    	if(origin == null)
    		return;

    	List<SubBlockPos> rotatedList = buffer[info.dir.ordinal()];
    	for(SubBlockPos pos : rotatedList) {
    		world.setBlockToAir(origin[0] + pos.dx, origin[1] + pos.dy, origin[2] + pos.dz);
    	}
    }
    
    //A series of getOrigin funcs.
    
    public int[] getOrigin(World world, int x, int y, int z) {
    	return getOrigin(world.getTileEntity(x, y, z));
    }
    
    public int[] getOrigin(TileEntity te) {
    	TileEntity ret = getOriginTile(te);
    	return ret == null ? null : new int[] { ret.xCoord, ret.yCoord, ret.zCoord };
    }
    
    public TileEntity getOriginTile(World world, int x, int y, int z) {
    	TileEntity now = world.getTileEntity(x, y, z);
    	return getOriginTile(now);
    }
    
    public TileEntity getOriginTile(TileEntity now) {
    	if(!(now instanceof IMultiTile)) {
    		return null;
    	}
    	InfoBlockMulti info = ((IMultiTile)now).getBlockInfo();
    	if(info == null || !info.isLoaded())
    		return null;
    	SubBlockPos sbp = buffer[info.dir.ordinal()].get(info.subID);
    	TileEntity ret = validate(now.getWorldObj().getTileEntity(now.xCoord - sbp.dx, now.yCoord - sbp.dy, now.zCoord - sbp.dz));
    	return ret;
    }
	
	//Internal
	private SubBlockPos rotate(SubBlockPos s, ForgeDirection dir) {
		switch(dir) {
		case EAST:
			return new SubBlockPos(-s.dz, s.dy, s.dx);
		case WEST:
			return new SubBlockPos(s.dz, s.dy, -s.dx);
		case SOUTH:
			return new SubBlockPos(-s.dx, s.dy, -s.dz);
		case NORTH:
			return new SubBlockPos(s.dx, s.dy, s.dz);
		default:
			throw new RuntimeException("Invalid rotate direction");
		}
	}
	
	double getRotation(InfoBlockMulti info) {
		return drMap[info.dir.ordinal()];
	}
	
	private TileEntity validate(TileEntity te) {
		return te instanceof IMultiTile ? te : null;
	}
	
	@Override
    public boolean isOpaqueCube() {
        return false;
    }
    
	@Override
    public int getRenderType() {
        return LIClientProps.RENDER_TYPE_EMPTY;
    }
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
}
