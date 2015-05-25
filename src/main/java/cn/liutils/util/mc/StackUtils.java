package cn.liutils.util.mc;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StackUtils {

	/**
	 * Return whether two stack's item instance and data are equal.
	 */
	public static boolean isStackDataEqual(ItemStack s1, ItemStack s2) {
		if(s1.getItem() != s1.getItem())
			return false;
		NBTTagCompound tag1 = s1.getTagCompound(), tag2 = s2.getTagCompound();
		if(tag1 == null || tag2 == null) {
			return tag1 == null && tag2 == null;
		}
		
		return tag1.equals(tag2);
	}
	
	public static NBTTagCompound loadTag(ItemStack stack) {
		NBTTagCompound ret = stack.stackTagCompound;
		if(ret == null)
			ret = stack.stackTagCompound = new NBTTagCompound();
		return ret;
	}
	
}
