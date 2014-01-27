/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

/**
 * @author WeAthFolD
 *
 */
public class EntityUtils {

	public static int getItemSlotByStack(ItemStack item, EntityPlayer player) {
		InventoryPlayer inv = player.inventory;
		for(int i = 0; i < inv.mainInventory.length; i++) {
			ItemStack is = inv.mainInventory[i];
			if(is != null && item == is)
				return i;
		}
		return -1;
	}
	
	public static double getDistanceSqFlat(Entity e1, Entity e2) {
		double x1 = e1.posX - e2.posX, x2 = e1.posZ - e2.posZ;
		return x1 * x1 + x2 * x2;
	}
	
	public static MovingObjectPosition rayTraceLook(Entity e, float distance) {
		Motion3D mo = new Motion3D(e, true);
		Vec3 vec1 = mo.asVec3(e.worldObj), vec32 = mo.move(distance).asVec3(e.worldObj);
		return e.worldObj.clip(vec1, vec32);
	}

}
