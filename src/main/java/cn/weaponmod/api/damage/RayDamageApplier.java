/**
 * 
 */
package cn.weaponmod.api.damage;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
	protected Entity[] exclusions;
	
	public RayDamageApplier(EntityLivingBase elb, Damage dmg) {
		this(elb, dmg, DEFAULT_MAX_DISTANCE);
	}
	
	/**
	 * Init a applier with the pos and facing direction of entity elb
	 * @param elb The entity
	 * @param dmg
	 * @param md Max trace distance
	 */
	public RayDamageApplier(EntityLivingBase elb, Damage dmg, double md) {
		this(elb.worldObj, new Motion3D(elb, true), dmg, md);
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
		exclusions = excl;
		return this;
	}
	
	public boolean attempt() {
		Vec3 v1 = motion.getPosVec(world),
			 v2 = motion.getPosVec(world);
		Vec3 mv = motion.getMotionVec(world);
		v2 = v2.addVector(v2.xCoord, v2.yCoord, v2.zCoord);
		MovingObjectPosition mop = GenericUtils.rayTraceBlocksAndEntities(selector, world, v1, v2, exclusions);
		if(mop != null && mop.typeOfHit == MovingObjectType.ENTITY) {
			damage.damageEntity(mop.entityHit);
		}
		return false;
	}

}
