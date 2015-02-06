/**
 * 
 */
package cn.weaponry.api.action.impl;

import java.lang.reflect.Constructor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import cn.weaponry.api.action.Action;
import cn.weaponry.api.info.InfWeapon;

/**
 * Single shooting action. Spawn a bullet and then immediately stops.
 * @author WeathFolD
 */
public abstract class ActionShoot extends Action {
	
	Class<? extends Entity> entClass;
	
	public float dmg;

	public ActionShoot(float _dmg) {
		dmg = _dmg;
	}
	
	public void onActionBegin(InfWeapon inf, int life) {
		if(consumeAmmo(inf)) {
			inf.player.worldObj.spawnEntityInWorld(spawnEntity(inf));
		}
	}
	
	public boolean onActionTick(InfWeapon inf, int passed, int life) {
		return true;
	}
	
	protected Entity spawnEntity(InfWeapon inf) {
		Entity ret = null;
		try {
			Constructor<? extends Entity> ctor = entClass.getConstructor(EntityPlayer.class, Float.TYPE);
			ret = ctor.newInstance(inf.player, dmg);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	protected abstract boolean consumeAmmo(InfWeapon inf);

}
