package cn.liutils.crafting;

import java.io.File;
import java.util.HashMap;

import cn.liutils.core.LIUtils;

/**
 * @author EAirPeter
 */
public class RecipeRegistry {
	
	public static final RecipeRegistry INSTANCE = new RecipeRegistry();
	
	/**
	 * Assign a registry to the type given
	 * @param type The type
	 * @param registry The registry
	 */
	public void registerRecipeType(String type, IRecipeRegistry registry) {
		if (map.containsKey(type))
			throw new IllegalArgumentException("Recipe type \"" + type + "\" exists");
		map.put(type, registry);
	}
	
	/**
	 * Add all recipes from a file. The recipe's type must be registered before.
	 * @param path The path of the file containing recipes
	 */
	public void addRecipeFromFile(String path) {
		addRecipeFromFile(new File(path));
	}
	
	/**
	 * Add all recipes from a file. The recipe's type must be registered before.
	 * @param file The file containing recipes
	 */
	public void addRecipeFromFile(File file) {
		RecipeParser parser = null;
		try {
			parser = new RecipeParser(file);
		}
		catch (Throwable e) {
			LIUtils.log.error("Failed to load recipes from file: " + file, e);
		}
		finally {
			parser.close();
		}
	}
	
	/**
	 * Add all recipes from a string. The recipe's type must be registered before.
	 * @param recipes The string specifying recipes
	 */
	public void addRecipeFromString(String recipes) {
		RecipeParser parser = null;
		try {
			parser = new RecipeParser(recipes);
		}
		catch (Throwable e) {
			LIUtils.log.error("Failed to load recipes from String: " + recipes, e);
		}
		finally {
			parser.close();
		}
	}
	
	private void addRecipe(RecipeParser parser) {
		while (parser.parseNext()) {
			IRecipeRegistry registry = map.get(parser.getType());
			if (registry != null)
				registry.register(parser.getOutput(), parser.getInput(), parser.getWidth(), parser.getHeight());
			else
				LIUtils.log.error("Failed to register a recipe because the type \"" + parser.getType() + "\" doesn't have its registry");
		}
	}
	
	private HashMap<String, IRecipeRegistry> map = new HashMap<String, IRecipeRegistry>();
	
	private RecipeRegistry() {
	}
	
}
