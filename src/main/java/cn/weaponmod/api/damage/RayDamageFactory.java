/**
 * 
 */
package cn.weaponmod.api.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * An entity that does nothing but continiously generate RayDamageApplier.
 * @author WeathFolD
 */
public abstract class RayDamageFactory extends Entity {
	
	EntityLivingBase user;

	public RayDamageFactory(EntityLivingBase elb) {
		super(elb.worldObj);
		user = elb;
	}
	
	public abstract void onUpdate();

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1) {}
	
	public static class Periodic extends RayDamageFactory {
		
		final int lifeTime;
		final int rate;
		final RayDamageApplier applier;
		
		public Periodic(EntityLivingBase elb, RayDamageApplier rpa, int rate, int lifeTime) {
			super(elb);
			this.rate = rate;
			this.lifeTime = lifeTime;
			this.applier = rpa;
		}
		
		public Periodic(EntityLivingBase elb, RayDamageApplier rpa, int rate) {
			this(elb, rpa, rate, -1);
		}

		@Override
		public void onUpdate() {
			if(lifeTime > 0 && ticksExisted > lifeTime) {
				setDead();
				return;
			}
			if(ticksExisted % rate == 0) {
				applier.resetMotion(user);
				applier.attempt();
			}
			
			++ticksExisted;
		}
		
	}
	
	public static class Timed extends RayDamageFactory {
		
		final int waitTime;
		final RayDamageApplier applier;

		public Timed(EntityLivingBase elb, RayDamageApplier rpa) {
			this(elb, rpa, 0);
		}
		
		public Timed(EntityLivingBase elb, RayDamageApplier rpa, int tick) {
			super(elb);
			waitTime = tick;
			applier = rpa;
		}

		@Override
		public void onUpdate() {
			if(ticksExisted >= waitTime) {
				applier.resetMotion(user);
				applier.attempt();
				setDead();
			}
			++ticksExisted;
		}
		
	}

}
