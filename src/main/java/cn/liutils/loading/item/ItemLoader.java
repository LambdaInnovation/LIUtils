/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under  
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.loading.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cn.liutils.core.LIUtils;
import cn.liutils.loading.Loader;

import com.google.gson.JsonObject;

import cpw.mods.fml.common.registry.GameRegistry;
import cn.liutils.loading.item.DefaultRules.*;

/**
 * Primitive info:
 * @itemType The Item class to construct. It must contain a void constructor.
 * 
 * Built-in rules:
 * @unlName
 * @textureName
 * @maxDamage
 * @maxStackSize
 * @creativeTab Field path. will use refelection at runtime to locate it.
 * @full3D
 */
public class ItemLoader<T extends Item> extends Loader<T> {
	
	static ItemLoadRule builtIns[] = {
		new UnlName(),
		new Texture(),
		new CCT(),
		new MaxDamage(),
		new MaxSS(),
		new Full3D()
	};
	
	public final List<ItemLoadRule> additionalRules = new ArrayList<ItemLoadRule>();

	@Override
	public T load(String name, JsonObject object) {
		try {
			String itemType = this.getString(name, "itemType");
			Item item;
			if(itemType == null) {
				item = new Item();
			} else {
				item = (Item) Class.forName(itemType).newInstance();
			}
			
			checkRuleset(item, builtIns, name);
			checkRuleset(item, additionalRules, name);
			if(item instanceof ItemLoadRuleProvider) {
				checkRuleset(item, ((ItemLoadRuleProvider)item).getRules(), name);
			}
			
			GameRegistry.registerItem(item, name);
			return (T) item;
		} catch(Exception e) {
			LIUtils.log.error("An error occured loading Item " + name);
			return null;
		}
	}
	
	private void checkRuleset(Item item, Iterable<ItemLoadRule> iterable, String name) throws Exception {
		for(ItemLoadRule rule : iterable) {
			if(rule.applyFor(item, this, name)) {
				rule.load(item, this, name);
			}
		}
	}
	
	private void checkRuleset(Item item, ItemLoadRule[] iterable, String name) throws Exception {
		for(ItemLoadRule rule : iterable) {
			if(rule.applyFor(item, this, name)) {
				rule.load(item, this, name);
			}
		}
	}

}
