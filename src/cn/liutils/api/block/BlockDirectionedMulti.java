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
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author WeAthFolD
 *
 */
public abstract class BlockDirectionedMulti extends Block implements ITileEntityProvider {
	
	public static class SubBlockPos {
		int offX, offY, offZ; //in origin rotation
		int id;
		
		public SubBlockPos(int x, int y, int z, int id) {
			offX = x;
			offY = y;
			offZ = z;
			this.id = id;
		}
		
		public void destroyMe(World wrld, int x, int y, int z) {
			wrld.setBlockToAir(x + offX, y + offZ, z + offZ);
		}
		
		public void setMe(World world, int x, int y, int z, int meta, Block block) {
			world.setBlock(x + offX, y + offY, z + offZ, block, meta, 0x03);
		}
		
		public boolean meThere(World wrld, int x, int y, int z, Block comparison) {
			return wrld.getBlock(x + offX, y + offY,  z + offZ) == comparison;
		}
	}
	 
	protected List<SubBlockPos> subBlocks = new ArrayList<SubBlockPos>();

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
        
        ForgeDirection dir = getFacingDirection(metadata);
        Iterator<SubBlockPos> iter = subBlocks.iterator();
        iter.next();
        
        while(iter.hasNext()) {
        	SubBlockPos pos = iter.next();
        	SubBlockPos pos2 = applyRotation(pos, dir.ordinal());
        	pos2.setMe(world, x, y, z, metadata + pos2.id << 2, this);
        }
        world.setBlockMetadataWithNotify(x, y, z, metadata, 0x03);
        System.out.println("OnBlockPlacedBy finished " + world.isRemote);
    }
	
	private boolean originExists(World world, int x, int y, int z, int meta) {
		int[] coord = getOrigin(world, x, y, z, meta);
		Block block = world.getBlock(coord[0], coord[1], coord[2]);
		int meta2  = world.getBlockMetadata(coord[0], coord[1], coord[2]);
		return block == this && meta2 >> 2 == 0;
	}
	
	private int[] getOrigin(World world, int x, int y, int z, int meta) {
		SubBlockPos pos2 = applyRotation(subBlocks.get(meta >> 2),
				getFacingDirection(meta).ordinal());
		return new int[] { x - pos2.offX, y - pos2.offY, z - pos2.offZ };
	}
	
    @Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
    	super.onNeighborBlockChange(world, x, y, z, block);
    	int meta = world.getBlockMetadata(x, y, z);
    	
    	/**
    	if(meta >> 2 == 0) { //The origin block
    		ForgeDirection dir = getFacingDirection(meta);
    		System.out.println("MeJudging " + world.isRemote);
    		
    		Iterator<SubBlockPos> iter = subBlocks.iterator();
    		iter.next();
    		while(iter.hasNext()) {
    			SubBlockPos pos2 = applyRotation(iter.next(), dir.ordinal());
    			if(!pos2.meThere(world, x, y, z, this)) {
    				System.out.println("ID " + pos2.id + "not there, removing me");
    				clearAll(world, x, y, z, dir.ordinal());
    				world.setBlockToAir(x, y, z);
    				return;
    			}
    		}
    	}**/
    }
    
    @SideOnly(Side.CLIENT)
    public abstract Vec3 getRenderOffset();
    
    @SideOnly(Side.CLIENT)
    public Vec3 getOffsetRotated(int dir) {
    	Vec3 pos = getRenderOffset();
    	if(dir == 3) 
			return Vec3.createVectorHelper(pos.xCoord, pos.yCoord, pos.zCoord);
		if(dir == 4)
			return Vec3.createVectorHelper(pos.zCoord, pos.yCoord, pos.xCoord);
		if(dir == 2)
			return Vec3.createVectorHelper(0, pos.yCoord, pos.zCoord);
		return Vec3.createVectorHelper(pos.zCoord, pos.yCoord, 0);
    }
    
	private SubBlockPos applyRotation(SubBlockPos pos, int dir) {
		if(dir == 3) 
			return new SubBlockPos(pos.offX, pos.offY, pos.offZ, pos.id);
		if(dir == 4)
			return new SubBlockPos(-pos.offZ, pos.offY, pos.offX, pos.id);
		if(dir == 2)
			return new SubBlockPos(-pos.offX, pos.offY, -pos.offZ, pos.id);
		return new SubBlockPos(pos.offZ, pos.offY, -pos.offX, pos.id);
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
	
	/**
	 * Add subBlocks to the list.
	 * Be reminded that offset is relative to the MODELVIEW coord. (Z+ as front)
	 */
	public abstract void addSubBlocks(List<SubBlockPos> list);
	
}
