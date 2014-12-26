/**
 * 
 */
package cn.weaponmod.api.damage;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.api.util.GenericUtils;
import cn.liutils.api.util.Motion3D;

/**
 * @author WeathFolD
 *
 */
public class RayDamageApplier {
	
	public static final double DEFAULT_MAX_DISTANCE = 100;
	protected double maxDistance = DEFAULT_MAX_DISTANCE;
	protected final World world;
	protected Motion3D motion;
	protected Damage damage;
	protected IEntitySelector selector;
	protected List<Entity> excl = new ArrayList<Entity>();
	
	public RayDamageApplier(EntityLivingBase elb, Damage dmg) {
		this(elb, dmg, DEFAULT_MAX_DISTANCE);
	}
	
	public RayDamageApplier(EntityLivingBase elb, float dmg) {
		this(elb, new DmgSimple(DamageSource.causeMobDamage(elb), dmg), DEFAULT_MAX_DISTANCE);
	}
	
	/**
	 * Init a applier with the pos and facing direction of entity elb
	 * @param elb The entity
	 * @param dmg
	 * @param md Max trace distance
	 */
	public RayDamageApplier(EntityLivingBase elb, Damage dmg, double md) {
		this(elb.worldObj, new Motion3D(elb, true), dmg, md);
		excl.add(elb);
	}
	
	public RayDamageApplier(EntityLivingBase elb, int dmg, double md) {
		this(elb, new DmgSimple(DamageSource.causeMobDamage(elb), dmg), md);
	}
	
	public RayDamageApplier(World world, Motion3D mo, Damage dmg) {
		this(world, mo, dmg, DEFAULT_MAX_DISTANCE);
	}

	/**
	 * Init a applier with given world and motion vector.
	 * @param world
	 * @param mo
	 * @param dmg
	 * @param md Max trace distance
	 */
	public RayDamageApplier(World world, Motion3D mo, Damage dmg, double md) {
		this.world = world;
		resetMotion(mo);
		resetDamage(dmg);
		setMaxDist(md);
	}
	
	public void resetMotion(EntityLivingBase elb) {
		if(motion == null) {
			motion = new Motion3D(elb, true).clone();
		} else {
			motion.init(elb, 0, true); //No new instances
		}
		motion.normalize();
	}
	
	public void resetMotion(Motion3D mo) {
		if(motion == null) {
			motion = mo.clone(); //Duplicate
		} else {
			motion.setFrom(mo); //No new instances
		}
		motion.normalize();
	}
	
	public void resetDamage(Damage dmg) {
		damage = dmg;
	}
	
	public RayDamageApplier setMaxDist(double d) {
		maxDistance = d;
		return this;
	}
	
	public RayDamageApplier setSelector(IEntitySelector ies) {
		selector = ies;
		return this;
	}
	
	public RayDamageApplier exclude(Entity... excl) {
		for(Entity e : excl) {
			this.excl.add(e);
		}
		return this;
	}
	
	public boolean attempt() {
		Vec3 v1 = motion.getPosVec(world),
			 v2 = motion.getPosVec(world);
		Vec3 mv = motion.getMotionVec(world);
		v2 = v2.addVector(mv.xCoord * maxDistance, mv.yCoord * maxDistance, mv.zCoord * maxDistance);
		System.out.println(v1);
		System.out.println(v2);
		MovingObjectPosition mop = GenericUtils.rayTraceBlocksAndEntities(selector, world, v1, v2, this.excl.toArray(new Entity[] {}));
		System.out.println("Hit " + mop.typeOfHit + " ");
		if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY) {
			damage.damageEntity(mop.entityHit);
		}
		return false;
	}

}
