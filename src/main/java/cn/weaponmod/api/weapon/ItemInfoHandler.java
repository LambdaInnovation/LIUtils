package cn.weaponmod.api.weapon;

import cn.weaponmod.api.WMInformation;
import cn.weaponmod.api.information.InfWeapon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemInfoHandler extends Item {
	
	public ItemInfoHandler() {
		super();
	}
	
	/**
	 * Automatically loads the weapon information
	 * {@link cn.weaponmod.api.WMInformation#register}
	 * @return created weapon information
	 */
	@Deprecated
	public final InfWeapon loadInformation(ItemStack stack, EntityPlayer player) {
		return WMInformation.getInformation(player);
	}
	
	public final InfWeapon loadInformation(EntityPlayer player) {
		return WMInformation.getInformation(player);
	}

}
