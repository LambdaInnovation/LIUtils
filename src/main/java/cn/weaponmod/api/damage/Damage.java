/**
 * 
 */
package cn.weaponmod.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

/**
 * Interface representing damage information.
 * @author WeathFolD
 */
public abstract class Damage {
	
	protected DamageSource source;
	
	public Damage(DamageSource ds) {
		source = ds;
	}
	
	protected abstract float getDamage(Entity ent);
	
	public void damageEntity(Entity ent) {
		ent.attackEntityFrom(source, getDamage(ent));
	}
}
