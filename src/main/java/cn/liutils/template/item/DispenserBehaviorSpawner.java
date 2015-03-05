/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.item;

import cn.liutils.util.EntityUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;


/**
 * The dispenser behavior for a LIMobSpawner. Remember to register it for your MobSpawner! >w>
 * @author WeAthFolD
 */
public class DispenserBehaviorSpawner extends BehaviorDefaultDispenseItem {
	
	public static DispenserBehaviorSpawner INSTANCE = new DispenserBehaviorSpawner();
	
	/**
	 * Prevents instanting
	 */
	private DispenserBehaviorSpawner() {}
	
    /**
     * Dispense the specified stack, play the dispense sound and spawn particles.
     */
    @Override
	public ItemStack dispenseStack(IBlockSource blockSource, ItemStack stack)
    {
        EnumFacing enumfacing = BlockDispenser.func_149937_b(blockSource.getBlockMetadata());
        double d0 = blockSource.getX() + enumfacing.getFrontOffsetX();
        double d1 = blockSource.getYInt() + 0.2F;
        double d2 = blockSource.getZ() + enumfacing.getFrontOffsetZ();
        Entity entity = EntityUtils.spawnCreature(blockSource.getWorld(), null, ((LIMobSpawner)stack.getItem()).getEntityClass()
        		, d0, d1, d2);

        if (entity instanceof EntityLivingBase && stack.hasDisplayName())
        {
            ((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
        }

        stack.splitStack(1);
        return stack;
    }
    
}
