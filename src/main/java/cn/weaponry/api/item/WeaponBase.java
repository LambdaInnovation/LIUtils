/**
 * 
 */
package cn.weaponry.api.item;

import cn.weaponry.api.state.WeaponState;
import net.minecraft.item.ItemSword;

/**
 * @author WeathFolD
 *
 */
public abstract class WeaponBase extends ItemSword {

	public WeaponBase() {
		super(ToolMaterial.EMERALD);
	}
	
	public abstract WeaponState getInitialState();
	
	//TODO: Interrupt original events

}
