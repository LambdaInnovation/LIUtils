/**
 * 
 */
package cn.liutils.template.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cn.liutils.template.block.BlockDirectionalMulti;
import cn.liutils.template.block.BlockDirectionalMulti.SubBlockPos;

/**
 * ItemBlock handler of BlockDirectionalMulti.
 * @author WeathFolD
 */
public class ItemBlockDirMulti extends ItemBlock {
	
	final BlockDirectionalMulti bdm;

	public ItemBlockDirMulti(Block block) {
		super(block);
		bdm = (BlockDirectionalMulti) block;
	}
	
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float a, float b, float c)
    {
        Block before = world.getBlock(x, y, z);

        if (before == Blocks.snow_layer && (world.getBlockMetadata(x, y, z) & 7) < 1)
        {
            side = 1;
        }
        else if (before != Blocks.vine && before != Blocks.tallgrass && before != Blocks.deadbush && !before.isReplaceable(world, x, y, z))
        {
            if (side == 0)
            {
                --y;
            }

            if (side == 1)
            {
                ++y;
            }

            if (side == 2)
            {
                --z;
            }

            if (side == 3)
            {
                ++z;
            }

            if (side == 4)
            {
                --x;
            }

            if (side == 5)
            {
                ++x;
            }
        }

        if (stack.stackSize == 0) {
            return false;
        }
        else if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }
        else if (y == 255 && this.field_150939_a.getMaterial().isSolid()) {
            return false;
        }
        else if (world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, side, player, stack))  {
        	//Inject: check if satisfies multiblock structure
        	int l = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        	for(SubBlockPos sbp : bdm.getSubRotated(l)) {
        		int nx = sbp.offX + x, ny = sbp.offY + y, nz = sbp.offZ + z;
        		if(!this.isBlockIgnorable(world, nx, ny, nz)) {
        			return false;
        		}
        	}
        	
            int i1 = this.getMetadata(stack.getItemDamage());
            int j1 = this.field_150939_a.onBlockPlaced(world, x, y, z, side, a, b, c, i1);

            if (placeBlockAt(stack, player, world, x, y, z, side, a, b, c, j1))
            {
                world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
                --stack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }
    
    private boolean isBlockIgnorable(World world, int x, int y, int z) {
    	Block before = world.getBlock(x, y, z);
    	return 
    		before == Blocks.vine || 
    		before == Blocks.tallgrass || 
    		before == Blocks.deadbush || 
    		before.isReplaceable(world, x, y, z);
    }

}
