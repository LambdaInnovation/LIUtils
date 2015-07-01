package cn.liutils.crafting;

/**
 * @author EAirPeter
 */
public interface IRecipeRegistry {

	public void register(String type, RecipeElement output, RecipeElement[] input, int width, int height);
	
}
