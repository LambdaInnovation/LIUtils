package cn.liutils.crafting;

import java.lang.reflect.Constructor;

import cn.liutils.core.LIUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author EAirPeter
 */
public class ShapelessOreRegistry implements IRecipeRegistry {

	public static final ShapelessOreRegistry INSTANCE = new ShapelessOreRegistry();
	private static Constructor<ShapelessOreRecipe> ctor = null;
	
	static {
		try {
			ctor = ShapelessOreRecipe.class.getConstructor(ItemStack.class, Object[].class);
		}
		catch(Throwable e) {
			throw new RuntimeException("Failed to get the constructor of class \"ShapelessOreRecipe\"", e);
		}
	}
	
	@Override
	public void register(String type, RecipeElement output, RecipeElement[] input, int width, int height) {
		try {
			ItemStack isOutput = OreDictionary.getOres(output.name).get(0);
			isOutput.setItemDamage(output.data);
			isOutput.stackSize = output.amount;
			Object[] recipe = new Object[input.length];
			for (int i = 0; i < input.length; ++i)
				recipe[i] = input[i].name;
			GameRegistry.addRecipe(ctor.newInstance(new Object[] {isOutput, recipe}));
		} catch (Throwable e) {
			LIUtils.log.error("Failed to register a recipe", e);
		}
	}

	private ShapelessOreRegistry() {
	}
	
}
