package cn.liutils.crafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author EAirPeter
 */
public class ShapedOreRegistry implements IRecipeRegistry {
	
	public static final ShapedOreRegistry INSTANCE  = new ShapedOreRegistry();
	
	@Override
	public void register(String type, RecipeElement output, RecipeElement[] input, int width, int height) {
		boolean mirrored = !type.equals("shaped_s");
		ItemStack isOutput = OreDictionary.getOres(output.name).get(0);
		isOutput.setItemDamage(output.data);
		isOutput.stackSize = output.amount;
		int pairs = 0;
		for (RecipeElement elem : input)
			if (elem != null)
				++pairs;
		Object[] recipe = new Object[height + pairs * 2];
		int index = 0;
		int _i = height;
		for (int y = 0; y < height; ++y) {
			String spec = new String();
			for (int x = 0; x < width; ++x, ++index)
				if (input[index] != null) {
					spec += (char) (index + 'A');
					recipe[_i++] = Character.valueOf((char) (index + 'A'));
					recipe[_i++] = input[index].name;
				}
				else
					spec += ' ';
			recipe[y] = spec;
		}
		GameRegistry.addRecipe(new ShapedOreRecipe(isOutput, mirrored, recipe));
	}
	
	private ShapedOreRegistry() {
	}
	
}
