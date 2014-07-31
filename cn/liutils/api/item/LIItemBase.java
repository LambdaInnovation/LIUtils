/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.item;

import cn.liutils.api.util.GenericUtils;
import net.minecraft.item.Item;

/**
 * @author WeAthFolD
 *
 */
public class LIItemBase extends Item {

	/**
	 * @param id
	 */
	public LIItemBase(int id) {
		super(id);
	}
	
	/**
	 * 同时设置图标路径和unlocalizedName的方便函数。注意记得使用标准namespace规范。
	 * @param name
	 */
	public void setIAndU(String name) {
		String realName =  GenericUtils.splitString(name, false);
		setUnlocalizedName(realName);
		setTextureName(name);
	}

}
