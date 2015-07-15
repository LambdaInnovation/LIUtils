package cn.liutils.crafting;

import net.minecraft.item.ItemStack;

/**
 * @author EAirPeter
 */
public interface IRecipeRegistry {

	public void register(String type, ItemStack output, Object[] input, int width, int height);
	
}
