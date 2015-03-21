/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cn.liutils.core.proxy.LIClientProps;
import cn.liutils.util.GenericUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A class of block that can be placed to player's facing direction, also it contains multiblock structure. <br/>
 * This block depends <strong>on only its main block(meta 0)</strong> to render, and you ought to provide the
 * offset for render (of each direction maybe) by functions provided <code>abstract</code>. <br/><br/>
 * You should define the sub blocks in your Ctor, 
 * using {@link #addSubBlock(int, int, int)} <br/>
 * It is usual (almost all the time) that we use <code>TileEntityRenderer</code> and disable normal renderers.
 * Render template is provided via <code>RenderTileDirMulti</code>,
 * a corresponding model-renderer implementation is <code>RenderDirMultiModelled</code>. </br>
 * Because MC use bytes to store metadata we often find the value overflows. Thus,
 * A <code>IMetadataProvider</code> interface is used upon block's TileEntity to override metadata.
 * You can use <code>TileDirectionalMulti</code> for fast impl.<br/>
 * You should use <code>ItemBlockDirMulti</code> as your block instance's ItemBlock.
 * BTW, Z+ is always parallel to the player placing direction~
 * @author WeAthFolD
 * @see cn.liutils.template.block.TileDirectionalMulti
 * @see cn.liutils.template.client.render.block.RenderTileDirMulti
 * @see cn.liutils.template.client.render.block.RenderDirMultiModelled
 * @see cn.liutils.template.block.IMetadataProvider
 * @see cn.liutils.template.block.TileDirectionalMulti
 * @see cn.liutils.template.item.ItemBlockDirMulti
 */
public abstract class BlockDirectionalMulti extends BlockContainer {
	
	/**
	 * A list for all the subblocks. Automatically stores id0
	 */
	private List<SubBlockPos> subBlocks = new ArrayList();
	
	/**
	 * Determine if we use built-in offset-rotation for the renderer.
	 */
	protected boolean useRotation = true;

	public BlockDirectionalMulti(Material mat) {
		super(mat);
		addSubBlock(0, 0, 0);
	}
	
    @SideOnly(Side.CLIENT)
    /**
     * Get the render offset for front-view direction(Z+). Rotations will be automatically applied for others.
     * Sometimes you have to use getOffsetRotated(int) for better control.
     */
    public abstract Vec3 getRenderOffset();
    
    @SideOnly(Side.CLIENT)
    /**
     * Return the resulting offset vec for direction value dir. you can override this to get better control.
     * @param dir
     * @return
     */
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
	
    /**
     * Get the block facing direction via its metadata.
     */
	public static ForgeDirection getFacingDirection(int metadata) {
		return ForgeDirection.values()[dirMap[metadata & 0x03]];
	}
	
	/**
	 * Return an array of size 3 containg the origin coordinate of the block.
	 */
	public int[] getOrigin(World world, int x, int y, int z, int meta) {
		SubBlockPos pos2 = applyRotation(getSubBlockByMeta(meta),
				getFacingDirection(meta).ordinal());
		if(pos2 == null)
			return null;
		return new int[] { x - pos2.offX, y - pos2.offY, z - pos2.offZ };
	}
	
	/**
	 * Rotate the specific subBlock to a certain direction and return a instance representing the result.
	 * <br/>Dosen't change the <code>subBlocks</code> list.
	 */
	public SubBlockPos applyRotation(SubBlockPos pos, int dir) {
		if(pos == null)
			return null;
		if(dir == 3) 
			return new SubBlockPos(pos.offX, pos.offY, pos.offZ, pos.id);
		if(dir == 4)
			return new SubBlockPos(-pos.offZ, pos.offY, pos.offX, pos.id);
		if(dir == 2)
			return new SubBlockPos(-pos.offX, pos.offY, -pos.offZ, pos.id);
		return new SubBlockPos(pos.offZ, pos.offY, -pos.offX, pos.id);
	}
	
	/**
	 * Get an array of 3 ints representing the subblock #id's coordinates, where the id is implied by meta.
	 * @param x
	 * @param y
	 * @param z
	 * @param meta
	 * @return
	 */
	protected int[] offset(int x, int y, int z, int meta) {
		SubBlockPos pos2 = applyRotation(subBlocks.get(meta >> 2), getFacingDirection(meta).ordinal());
		return new int[] {
				x + pos2.offX,
				y + pos2.offY,
				z + pos2.offZ
		};
	}
	
	public int getMetadata(IBlockAccess world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof IMetadataProvider) {
			return ((IMetadataProvider)te).getMetadata();
		}
		return world.getBlockMetadata(x, y, z);
	}
	
	public void setMetadata(World world, int x, int y, int z, int meta) {
		TileEntity te = world.getTileEntity(x, y, z);
		if(te != null && te instanceof IMetadataProvider) {
			((IMetadataProvider)te).setMetadata(meta);
			return;
		}
		world.setBlockMetadataWithNotify(x, y, z, meta, 0x03);
	}
	
	/**
	 * Define a SubBlock.
	 * Note that we don't process overlap situations, therefore you might wanna keep it safe yourself.
	 */
	protected void addSubBlock(int ox, int oy, int oz) {
		SubBlockPos res = new SubBlockPos(ox, oy, oz, subBlocks.size());
		subBlocks.add(res);
	}
	
	/**
	 * Get a SubBlock instance by its id.
	 */
	protected SubBlockPos getSubBlock(int id) {
		return GenericUtils.safeFetchFrom(subBlocks, id);
	}
	
	/**
	 * Get a SubBlock instance by a metadata of a block.
	 */
	protected SubBlockPos getSubBlockByMeta(int meta) {
		return getSubBlock(meta >> 2);
	}
	
	/**
	 * Get a block's subBlock id by its metadata.
	 */
	protected int getSubBlockIdByMeta(int meta) {
		return meta >> 2;
	}
	
	protected List<SubBlockPos> getSubBlocks() {
		return subBlocks;
	}
	
	public List<SubBlockPos> getSubRotated(int dir) {
		List<SubBlockPos> ret = new ArrayList();
		for(SubBlockPos sbp : subBlocks) {
			ret.add(this.applyRotation(sbp, getFacingDirection(dir).ordinal()));
		}
		return ret;
	}
	
	//---------------------Internal Impl.---------------------------
	private static final int[] dirMap = { 2, 5, 3, 4 };
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack)
    {
        int l = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int metadata = l;
        world.setBlockMetadataWithNotify(x, y, z, metadata, 0x03);
       
        int dir = getFacingDirection(metadata).ordinal();
        SubBlockPos arr[] = new SubBlockPos[subBlocks.size()];
        for(int i = 1; i < subBlocks.size(); ++i) {
        	SubBlockPos bp = subBlocks.get(i);
        	SubBlockPos bp2 = this.applyRotation(bp, dir);
        	Block block = world.getBlock(x + bp2.offX, y + bp2.offY, z + bp2.offZ);
        	//Drop if unable to place. This normally won't happen, just for safety check.
        	if(!block.isReplaceable(world, x + bp2.offX, y + bp2.offY, z + bp2.offZ)) {
        		if(placer instanceof EntityPlayer) 
        			if(!((EntityPlayer) placer).capabilities.isCreativeMode)
        				this.dropBlockAsItem(world, x, y, z, new ItemStack(this));
        		world.setBlockToAir(x, y, z);
        		return;
        	}
        	arr[i] = bp2;
        }
        
        for(int i = 1; i < subBlocks.size(); ++i) {
        	arr[i].setMe(world, x, y, z, metadata | (arr[i].id << 2), this);
        	System.out.println(world.getTileEntity(arr[i].offX + x, arr[i].offY + y, arr[i].offZ + z));
        }
        
        System.out.println(world.getTileEntity(x, y, z) + " " + world.isRemote);
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
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
    {
    	TileEntity te = world.getTileEntity(x, y, z);
    	int[] crds = this.getOrigin(world, x, y, z, metadata);
    	if(crds != null) {
    		x = crds[0];
    		y = crds[1];
    		z = crds[2];
    		int dir = BlockDirectionalMulti.getFacingDirection(metadata).ordinal();
    		for(SubBlockPos bp : this.subBlocks) {
    			SubBlockPos bp2 = this.applyRotation(bp, dir);
    			Block inst = world.getBlock(x + bp2.offX, y + bp2.offY, z + bp2.offZ);
    			if(inst == this) //safe check in order not to destroy other blocks
    				bp2.destroyMe(world, x, y, z);
    		}
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
	
	public TileEntity getOriginTileEntity(World world, int x, int y, int z, int meta) {
		int[] coords = this.getOrigin(world, x, y, z, meta);
		if(coords == null)
			return null;
		TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
		return te;
	}
	
	public class SubBlockPos {
		public final int offX, offY, offZ; //in origin rotation
		public final int id;
		
		public SubBlockPos(int x, int y, int z, int id) {
			offX = x;
			offY = y;
			offZ = z;
			this.id = id;
		}
		
		public void destroyMe(World wrld, int x, int y, int z) {
			wrld.removeTileEntity(x + offX, y + offY, z + offZ);
			wrld.setBlockToAir(x + offX, y + offY, z + offZ);
		}
		
		public void setMe(World world, int x, int y, int z, int meta, Block block) {
			world.setBlock(x + offX, y + offY, z + offZ, block, meta, 0x03);
			setMetadata(world, x + offX, y + offY, z + offZ, meta);
		}
		
		public boolean meThere(World wrld, int x, int y, int z) {
			return wrld.getBlock(x + offX, y + offY,  z + offZ) == BlockDirectionalMulti.this;
		}
	}
	
	@Override 
    public int getMobilityFlag()
    {
        return 1;
    }
}
