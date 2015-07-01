package cn.liutils.crafting;

/**
 * @author EAirPeter
 */
public interface IRecipeRegistry {

	public void register(RecipeElement output, RecipeElement[] input, int width, int height);
	
}
