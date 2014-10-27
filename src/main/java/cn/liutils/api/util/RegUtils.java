package cn.liutils.api.util;

import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.GameRegistry;

public class RegUtils {

	public static Item reg(Class<? extends Item> itemClass, String id) {
		try {
			Item it = itemClass.getConstructor().newInstance();
			GameRegistry.registerItem(it, id);
			return it;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Item[] reg(Class<? extends Item> itemClass, int n, String id) {
		Item[] res = new Item[n];
		try {
			for(int i = 0; i < n; ++i) {
				Item it = itemClass.getConstructor(Integer.TYPE).newInstance(i);
				GameRegistry.registerItem(it, id + i);
				res[i] = it;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
