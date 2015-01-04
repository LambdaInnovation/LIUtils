package cn.liutils.api.render.model;

import net.minecraft.item.ItemStack;

/**
 * Interface for model applied on itemRenderer.
 * @author WeathFolD
 */
public interface IItemModel {

	public void render(ItemStack is, float scale, float f);
	public void setRotationAngles(ItemStack is, double posX, double posY, double posZ, float f);
	
}

