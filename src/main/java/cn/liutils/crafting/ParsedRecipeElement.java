package cn.liutils.crafting;

import net.minecraftforge.oredict.OreDictionary;

/**
 * In a recipe file, use "NAME#DATA*AMOUNT" to specify an element.
 * @author EAirPeter
 */
public class ParsedRecipeElement {

	public String name = null;
	public int data = OreDictionary.WILDCARD_VALUE;
	public int amount = 1;

	@Override
	public String toString() {
		return "(" + name + "," + data + "," + amount + ")";
	}
	
}
