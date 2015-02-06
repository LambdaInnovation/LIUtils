/**
 * 
 */
package cn.weaponry.template.classic;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cn.liutils.util.GenericUtils;
import cn.weaponry.api.item.WeaponBase;
import cn.weaponry.api.state.WeaponState;

/**
 * Old half-life/quake style weapon. Greedy-fill reloading preference,
 * and simple one-field-ammo-value handling method.
 * @author WeathFolD
 */
public abstract class WeaponClassic extends WeaponBase {
	
	private final int maxAmmo;

	public WeaponClassic(int _maxAmmo) {
		maxAmmo = _maxAmmo;
	}
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs cct, List list) {
    	ItemStack res = new ItemStack(item, 1, 0);
    	setAmmo(res, getMaxAmmo());
        list.add(res);
    }
	
	public int getAmmo(ItemStack stack) {
		return GenericUtils.loadCompound(stack).getShort("ammo");
	}
	
	public void setAmmo(ItemStack stack, int n) {
		n = Math.max(0, Math.min(n, maxAmmo)); //wrap
		GenericUtils.loadCompound(stack).setShort("ammo", (short) n);
	}
	
	public int getMaxAmmo() {
		return maxAmmo;
	}

	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean wtf) {
		String str = getAmmo(stack) + "/" + getMaxAmmo();
		list.add(str);
	}

}
