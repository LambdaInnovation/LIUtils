package cn.liutils.api.potion;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

/**
 * This class defines the behavior of a potion that can only be applied to a player.
 * @author acaly
 *
 */
public abstract class CustomPlayerPotion extends Potion {
	
	public CustomPlayerPotion(boolean isBad, int color) {
		super(PotionRegistry.getFreeId(), isBad, color);
	}

	public abstract void onStart(EntityPlayer player);
	public abstract void onFinish(EntityPlayer player);
	public abstract void onTick(EntityPlayer player);
}
