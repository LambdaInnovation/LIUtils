/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.block;

import cn.liutils.api.util.GenericUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @author WeAthFolD
 *
 */
public class LIBlockBase extends Block {
	
	/**
	 * @param par1
	 * @param material
	 */
	public LIBlockBase(int par1, Material material) {
		super(par1, material);
	}
	
	public LIBlockBase(int id, Material mat, String unlName) {
		this(id, mat);
		this.setUnlocalizedName(unlName);
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
