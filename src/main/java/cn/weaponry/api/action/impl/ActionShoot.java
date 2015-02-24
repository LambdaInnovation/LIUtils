/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.weaponry.api.action.impl;

import java.lang.reflect.Constructor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import cn.weaponry.api.action.Action;
import cn.weaponry.api.entity.EntityBullet;
import cn.weaponry.api.info.InfWeapon;

/**
 * Single shooting action. Spawn a bullet and then immediately stops.
 * @author WeathFolD
 */
public abstract class ActionShoot extends Action {
	
	Class<? extends Entity> entClass = EntityBullet.class;
	
	public float dmg;

	public ActionShoot(float _dmg) {
		dmg = _dmg;
	}
	
	public void onActionBegin(InfWeapon inf, int life) {
		if(consumeAmmo(inf)) {
			if(!inf.player.worldObj.isRemote)
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
