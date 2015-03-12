/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.template.entity;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.util.space.Motion3D;

/**
 * @author WeAthFolD
 */
public class EntityBulletFX extends EntityThrowable {

	protected Motion3D motion;
	protected float damage;
	public boolean renderFromLeft = false;
	protected IEntitySelector selector = null;
	
	protected int lifeTime = 50;
	
	public EntityBulletFX setRenderFromLeft() {
		renderFromLeft = true;
		return this;
	}
	
	public EntityBulletFX(World par1World, EntityLivingBase par2EntityLiving, float dmg) {
		this(par1World, par2EntityLiving, dmg, 0);
	}
	
	
	public EntityBulletFX(World par1World, EntityLivingBase ent, float dmg, float scatterRadius) {
		super(par1World, ent);
		initPosition(ent, (int) scatterRadius);
		this.damage = dmg;
	}
	
	protected void initPosition(EntityLivingBase ent) {
		initPosition(ent, 0);
	}
	
	protected void initPosition(EntityLivingBase ent, int scatter) {
		this.motion = new Motion3D(ent, scatter, true);
		motion.applyToEntity(this);
		this.setThrowableHeading(motionX, motionY, motionZ, func_70182_d(), 1.0F);
		this.rotationYaw = ent.rotationYawHead;
	}
	
	public EntityBulletFX(World par1World, EntityLivingBase par2EntityLiving, float dmg, boolean rev) {
		this(par1World, par2EntityLiving, dmg);
		if(rev) setRenderFromLeft();
	}
	
	
	public EntityBulletFX(World par1World, Entity ent, Entity target, float dmg, float oriHgt, float targHgt) {
		super(par1World);
		double x0 = ent.posX + ent.width * 0.5,
				y0 = ent.posY + oriHgt,
				z0 = ent.posZ + ent.width * 0.5;
		double x1 = target.posX + target.width * 0.5,
				y1 = target.posY + targHgt,
				z1 = target.posZ + target.width * 0.5;
		
		
		motionX = x1 - x0;
		motionY = y1 - y0;
		motionZ = z1 - z0;
		double d = Math.sqrt(motionX * motionX + motionY + motionY + motionZ * motionZ);
		motionX /= d;
		motionY /= d;
		motionZ /= d;
		float travel = 1.0F;
		setPosition(x0 + motionX * travel, y0 + motionY * travel, z0 + motionZ * travel);
		this.setThrowableHeading(motionX, motionY, motionZ, func_70182_d(), 1.0F);
		damage = dmg;
		motion = new Motion3D(this);
	}
	
	public EntityBulletFX(World par1World, EntityLivingBase ent, Entity target, float dmg) {
		super(par1World, ent);
		motionX = target.posX  - ent.posX;
		motionY = (target.posY + target.height / 2.0) - (ent.posY + ent.height / 2.0);
		motionZ = target.posZ - ent.posZ;
		double d = Math.sqrt(motionX * motionX + motionY + motionY + motionZ * motionZ);
		motionX /= d;
		motionY /= d;
		motionZ /= d;
		setPosition(ent.posX + motionX * 1.0, ent.posY + ent.height / 2.0 + motionY * 1.0, ent.posZ + motionZ * 1.0);
		this.setThrowableHeading(motionX, motionY, motionZ, func_70182_d(), 10.0F);
		damage = dmg;
		motion = new Motion3D(this);
	}
	
	public EntityBulletFX(World world, Vec3 begin, Vec3 motion, float dmg) {
		super(world);
		setPosition(begin.xCoord, begin.yCoord, begin.zCoord);
		setThrowableHeading(motion.xCoord, motion.yCoord, motion.zCoord, func_70182_d(), 1.0F);
		this.motion = new Motion3D(this);
		damage = dmg;
	}
	
	public EntityBulletFX(World world) {
		super(world);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if(worldObj.isRemote) {
			double par1 = motionX, par5 = motionZ, par3 = motionY, f3 = Math.sqrt(par1 * par1 + par5 + par5);
	        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
	        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, f3) * 180.0D / Math.PI);
		}
		if (ticksExisted > lifeTime)
			this.setDead();
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}

	@Override
	protected void onImpact(MovingObjectPosition par1) {
		this.setDead();
	}

	protected void doBlockCollision(MovingObjectPosition result) {}

	protected void doEntityCollision(MovingObjectPosition result) {}
	
	public float getBulletDamage(int mode) {
		return damage;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected float func_70182_d() {
		return 3F;
	}


}
