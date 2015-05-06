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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import cn.liutils.core.LIUtils;
import cn.liutils.loading.Loader.ObjectNamespace;

/**
 * @author WeAthFolD
 *
 */
class DefaultRules {

	static class UnlName extends ItemLoadRule {
		@Override
		public void load(Item item, ObjectNamespace ns, String name) {
			String unlName = ns.getString("unlName");
			if(unlName != null)
				item.setUnlocalizedName(unlName);
		}
	}
	
	static class Texture extends ItemLoadRule {

		@Override
		public void load(Item item, ObjectNamespace ns, String name) {
			String texName = ns.getString("textureName");
			if(texName != null)
				item.setTextureName(texName);
		}
		
	}
	
	static class CCT extends ItemLoadRule {

		@Override
		public void load(Item item, ObjectNamespace ns, String name) {
			String cctName = ns.getString("creativeTab");
			if(cctName != null) {
				try {
					int dot = cctName.lastIndexOf('.');
					Class c = Class.forName(cctName.substring(0, dot));
					CreativeTabs cct = (CreativeTabs) c.getField(cctName.substring(dot + 1)).get(null);
					if(cct != null) {
						item.setCreativeTab(cct);
					} else {
						throw new RuntimeException();
					}
				} catch(Exception e) {
					LIUtils.log.error("Didn't find CreativeTab " + cctName);
				}
			}
		}
		
	}
	
	static class MaxDamage extends ItemLoadRule {

		@Override
		public void load(Item item, ObjectNamespace ns, String name) {
			try {
				int md = ns.getInt("maxDamage");
				item.setMaxDamage(md);
			} catch(Exception e) {}
		}
		
	}
	
	static class MaxSS extends ItemLoadRule {

		@Override
		public void load(Item item, ObjectNamespace ns, String name) {
			try {
				int md = ns.getInt("maxStackSize");
				item.setMaxStackSize(md);
			} catch(Exception e) {}
		}
		
	}
	
	static class Full3D extends ItemLoadRule {

		@Override
		public void load(Item item, ObjectNamespace ns, String name) {
			try {
				boolean b = ns.getBoolean("full3D");
				if(b)
					item.setFull3D();
			} catch(Exception e) {}
		}
		
	}
	
}
