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
package cn.weaponry.api.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEntity;
import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.motion.CollisionCheck;
import cn.liutils.api.entityx.motion.VelocityUpdate;
import cn.liutils.util.space.Motion3D;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Default bullet entity.
 * @author WeathFolD
 */
@RegistrationClass
@RegEntity
//@RegEntity.HasRender
public class EntityBullet extends EntityX {
	
	
	
	float damage;
	EntityPlayer thrower; //Not synced
	
	public EntityBullet(EntityPlayer _thrower, float dmg, int scatter) {
		super(_thrower.worldObj);
		damage = dmg;
		thrower = _thrower;
		new Motion3D(thrower, scatter, true).applyToEntity(this);
		this.setHeading(motionX, motionY, motionZ, getBulletSpeed());
		setup();
	}
	
	public EntityBullet(EntityPlayer _thrower, float dmg) {
		this(_thrower, dmg, 0);
	}

	@SideOnly(Side.CLIENT)
	public EntityBullet(World world) {
		super(world);
		setup();
	}
	
	public EntityBullet setSelector(IEntitySelector sel) {
		return this;
	}
	
	public void onCollision(MovingObjectPosition res) {
		switch(res.typeOfHit) {
		case ENTITY:
			res.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(thrower), damage);
			break;
		case BLOCK:
			break;
		default:
			break;
		}
		setDead();
	}
	
	protected double getBulletSpeed() {
		return 5.0D;
	}
	
	private void setup() {
		addDaemonHandler(new VelocityUpdate(this));
		addDaemonHandler(new CollisionCheck(this) {
			@Override
			protected void onCollided(MovingObjectPosition res) {
				onCollision(res);
			}
		});
	}

}
