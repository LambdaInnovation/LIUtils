package cn.liutils.template.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cn.liutils.util.EntityUtils;

/**
 * A Item class that spawns a given kind of mob.
 * @author WeathFolD
 */
public class LIMobSpawner extends Item {

	protected Class<? extends EntityLiving> entClass = EntityPig.class;
	
	public LIMobSpawner() { }
	
	public LIMobSpawner(Class<? extends EntityLiving> entityClass) {
		entClass = entityClass;
	}

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer par3EntityPlayer)
    {
    	if(!world.isRemote) {
			EntityUtils.spawnCreature(world, entClass, par3EntityPlayer);
			if (!(par3EntityPlayer.capabilities.isCreativeMode)) {
				par1ItemStack.stackSize--;
			}
		}
        return par1ItemStack;
    }
    
    public Class<? extends EntityLiving> getEntityClass() {
    	return entClass;
    }
	
	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 10;
	}
}
