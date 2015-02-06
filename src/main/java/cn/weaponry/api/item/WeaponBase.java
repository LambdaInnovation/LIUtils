/**
 * 
 */
package cn.weaponry.api.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEventHandler;
import cn.annoreg.mc.RegEventHandler.Bus;
import cn.weaponry.api.info.InfManager;
import cn.weaponry.api.info.InfWeapon;
import cn.weaponry.api.state.WeaponState;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeathFolD
 *
 */
@RegistrationClass
public abstract class WeaponBase extends ItemSword {

	public WeaponBase() {
		super(ToolMaterial.EMERALD);
	}
	
	public abstract WeaponState getInitialState();
	
	/**
	 * Called when player has switched current item to this weapon.
	 * @param inf weapon information
	 */
	public void onActivated(InfWeapon inf) {}
	
	//---Internal process
	@Override
	public void onUpdate(ItemStack stack, World world, Entity ent, int slot, boolean hold) {
		if(hold && (ent instanceof EntityPlayer)) {
			EntityPlayer player = (EntityPlayer) ent;
			player.isSwingInProgress = false;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public boolean isFull3D() {
        return true;
    }
	
	@Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
    	return stack;
    }
    
	@Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
    	return true;
    }
    
	@Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase elb, EntityLivingBase elb2) {  
    	return true;
    }
	
	@RegEventHandler(Bus.Forge)
	public static class Event {
		@SubscribeEvent
		public void onInteract(EntityInteractEvent event) {
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if(stack != null && (stack.getItem() instanceof WeaponBase)) {
				InfWeapon info = InfManager.getInfo(event.entityPlayer);
				if(info.active() && info.getCurState().doesCancelSwing(info)) {
					event.setCanceled(true);
				}
			}
		}
	}

}
 