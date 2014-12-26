/**
 * 
 */
package cn.weaponmod.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/**
 * Straightforward hit entity
 * @author WeathFolD
 */
public class DmgSimple extends Damage {
	
	float damage;

	public DmgSimple(DamageSource ds, float dmg) {
		super(ds);
		damage = dmg;
	}

	@Override
	public float getDamage(Entity ent) {
		return damage;
	}

}
