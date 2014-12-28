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
	
	final EntityLivingBase user;
	protected final RayDamageApplier applier;
	
	public RayDamageFactory(EntityLivingBase elb, int dmg) {
		this(elb, new RayDamageApplier(elb, dmg));
	}

	public RayDamageFactory(EntityLivingBase elb, RayDamageApplier rda) {
		super(elb.worldObj);
		user = elb;
		applier = rda;
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
		
		public Periodic(EntityLivingBase elb, float dmg, int rate, int lifeTime) {
			this(elb, new RayDamageApplier(elb, dmg), rate, lifeTime);
		}
		
		public Periodic(EntityLivingBase elb, RayDamageApplier rda, int rate, int lifeTime) {
			super(elb, rda);
			this.rate = rate;
			this.lifeTime = lifeTime;
		}
		
		public Periodic(EntityLivingBase elb, float dmg, int rate) {
			this(elb, dmg, rate, -1);
		}
		
		public Periodic(EntityLivingBase elb, RayDamageApplier rda, int rate) {
			this(elb, rda, rate, -1);
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

		public Timed(EntityLivingBase elb, RayDamageApplier rda) {
			this(elb, rda, 0);
		}
		
		public Timed(EntityLivingBase elb, float dmg) {
			this(elb, dmg, 0);
		}
		
		public Timed(EntityLivingBase elb, float dmg, int tick) {
			this(elb, new RayDamageApplier(elb, dmg), tick);
		}
		
		public Timed(EntityLivingBase elb, RayDamageApplier rda, int tick) {
			super(elb, rda);
			waitTime = tick;
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
