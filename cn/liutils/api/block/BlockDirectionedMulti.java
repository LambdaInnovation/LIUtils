/**
 * Copyright (C) Lambda-Innovation, 2013-2014
 * This code is open-source. Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 */
package cn.liutils.api.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.liutils.core.proxy.LIClientProps;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author WeAthFolD
 *
 */
public abstract class BlockDirectionedMulti extends Block implements ITileEntityProvider {
	
	public static class SubBlockPos {
		public final int offX, offY, offZ; //in origin rotation
		public final int id;
		
		public SubBlockPos(int x, int y, int z, int id) {
			offX = x;
			offY = y;
			offZ = z;
			this.id = id;
		}
		
		public void destroyMe(World wrld, int x, int y, int z) {
			System.out.println("[" + (x + offX) + " " + (y + offZ) + " " + (z + offZ) + "]");
			throw new RuntimeException();
			//wrld.setBlockToAir(x + offX, y + offZ, z + offZ);
		}
		
		public void setMe(World world, int x, int y, int z, int meta, Block block) {
			world.setBlock(x + offX, y + offY, z + offZ, block, meta, 0x03);
		}
		
		public boolean meThere(World wrld, int x, int y, int z, Block comparison) {
			return wrld.getBlock(x + offX, y + offY,  z + offZ) == comparison;
		}
	}
	 
	public List<SubBlockPos> subBlocks = new ArrayList();
	
	protected boolean useRotation = true;

	public BlockDirectionedMulti(Material mat) {
		super(mat);
		subBlocks.add(new SubBlockPos(0, 0, 0, 0));
		addSubBlocks(subBlocks);
	}
	
	private static final int[] dirMap = { 2, 5, 3, 4};
	public static ForgeDirection getFacingDirection(int metadata) {
		return ForgeDirection.values()[dirMap[metadata & 0x03]];
	}
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack)
    {
        int l = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int metadata = l;
        
        int dir = getFacingDirection(metadata).ordinal();
        SubBlockPos arr[] = new SubBlockPos[subBlocks.size()];
        for(int i = 1; i < subBlocks.size(); ++i) {
        	SubBlockPos bp = subBlocks.get(i);
        	SubBlockPos bp2 = this.applyRotation(bp, dir);
        	/*Block block = world.getBlock(x + bp2.offX, y + bp2.offY, z + bp2.offZ);
        	if(!block.isReplaceable(world, x + bp2.offX, y + bp2.offY, z + bp2.offZ)) {
        		this.dropBlockAsItem(world, x, y, z, new ItemStack(this));
        		world.setBlockToAir(x, y, z);
        		return;
        	}*/
        	arr[i] = bp2;
        }
        
        for(int i = 1; i < subBlocks.size(); ++i) {
        	arr[i].setMe(world, x, y, z, metadata | (arr[i].id << 2), this);
        }
        world.setBlockMetadataWithNotify(x, y, z, metadata, 0x03);
    }
	
	private boolean originExists(World world, int x, int y, int z, int meta) {
		int[] coord = getOrigin(world, x, y, z, meta);
		Block block = world.getBlock(coord[0], coord[1], coord[2]);
		int meta2  = world.getBlockMetadata(coord[0], coord[1], coord[2]);
		return block == this || meta2 >> 2 == 0;
	}
	
	protected int[] getOrigin(World world, int x, int y, int z, int meta) {
		SubBlockPos pos2 = applyRotation(subBlocks.get(meta >> 2),
				getFacingDirection(meta).ordinal());
		return new int[] { x - pos2.offX, y - pos2.offY, z - pos2.offZ };
	}
	
    @Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
    	super.onNeighborBlockChange(world, x, y, z, block);
    }
    
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
    	System.out.println("BreakBLock " + world.isRemote);
    	metadata = this.getMetadata(world, x, y, z);
    	if(metadata == -1) metadata = world.getBlockMetadata(x, y, z);
    	super.breakBlock(world, x, y, z, block, metadata);
    	int[] crds = this.getOrigin(world, x, y, z, metadata);
    	/*
    	{
    		x = crds[0];
    		y = crds[1];
    		z = crds[2];
    		int dir = this.getFacingDirection(metadata).ordinal();
    		for(SubBlockPos bp : this.subBlocks) {
    			SubBlockPos bp2 = this.applyRotation(bp, dir);
    			bp2.destroyMe(world, x, y, z);
    		}
    	}
    	*/
    }
    
    @SideOnly(Side.CLIENT)
    public abstract Vec3 getRenderOffset();
    
    @SideOnly(Side.CLIENT)
    public Vec3 getOffsetRotated(int dir) {
    	if(!useRotation) return getRenderOffset();
    	Vec3 pos = getRenderOffset();
    	if(dir == 3) 
			return Vec3.createVectorHelper(pos.xCoord, pos.yCoord, pos.zCoord);
		if(dir == 4)
			return Vec3.createVectorHelper(pos.zCoord, pos.yCoord, pos.xCoord);
		if(dir == 2)
			return Vec3.createVectorHelper(0, pos.yCoord, pos.zCoord);
		return Vec3.createVectorHelper(pos.zCoord, pos.yCoord, 0);
    }
    
	public SubBlockPos applyRotation(SubBlockPos pos, int dir) {
		if(dir == 3) 
			return new SubBlockPos(pos.offX, pos.offY, pos.offZ, pos.id);
		if(dir == 4)
			return new SubBlockPos(-pos.offZ, pos.offY, pos.offX, pos.id);
		if(dir == 2)
			return new SubBlockPos(-pos.offX, pos.offY, -pos.offZ, pos.id);
		return new SubBlockPos(pos.offZ, pos.offY, -pos.offX, pos.id);
	}
	
	protected int[] offset(int x, int y, int z, int meta, int id) {
		SubBlockPos pos2 = applyRotation(subBlocks.get(id), getFacingDirection(meta).ordinal());
		return new int[] {
				x + pos2.offX,
				y + pos2.offY,
				z + pos2.offZ
		};
	}
	
	private void clearAll(World world, int x, int y, int z, int dir) {
		Iterator<SubBlockPos> iter = subBlocks.iterator();
		iter.next();
		while(iter.hasNext()) {
			SubBlockPos pos0 = applyRotation(iter.next(), dir);
			pos0.destroyMe(world, x, y, z);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderType() {
		return LIClientProps.RENDER_TYPE_EMPTY;
	}
	
	@Override
    public boolean isOpaqueCube()
    {
		return false;
    }
	
	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }
	
	public int getMetadata(IBlockAccess world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof IMetadataProvider) {
			return ((IMetadataProvider)te).getMetadata();
		}
		return world.getBlockMetadata(x, y, z);
	}
	
	/**
	 * Add subBlocks to the list.
	 * Be reminded that offset is relative to the MODELVIEW coord. (Z+ as front)
	 */
	public abstract void addSubBlocks(List<SubBlockPos> list);
	
}
