/** 
 * Copyright (c) Lambda Innovation Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.cn/
 * 
 * The mod is open-source. It is distributed under the terms of the
 * Lambda Innovation Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * 本Mod是完全开源的，你允许参考、使用、引用其中的任何代码段，但不允许将其用于商业用途，在引用的时候，必须注明原作者。
 */
package cn.liutils.api.entity;

import net.minecraft.block.Block;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.api.util.Motion3D;

/**
 * @author WeAthFolD
 *
 */
public class EntityBullet extends EntityThrowable {

	protected Motion3D motion;
	protected float damage;
	public boolean renderFromLeft = false;
	protected IEntitySelector selector = null;
	
	public EntityBullet setRenderFromLeft() {
		renderFromLeft = true;
		return this;
	}
	
	public EntityBullet(World par1World, EntityLivingBase par2EntityLiving, float dmg) {
		this(par1World, par2EntityLiving, dmg, 0);
	}
	
	
	public EntityBullet(World par1World, EntityLivingBase par2EntityLiving, float dmg, float scatterRadius) {
		super(par1World, par2EntityLiving);
		float d1 = 0, d2 = 0;
		if(scatterRadius > 0) {
			d1 = scatterRadius * (rand.nextFloat() * 2 - 1F);
			d2 = scatterRadius * (rand.nextFloat() * 2 - 1F);
		}
		this.rotationYaw = par2EntityLiving.rotationYawHead + d1;
		this.rotationPitch += d2;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 0.4F;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI) * 0.4F;
        this.motionY = MathHelper.sin((this.rotationPitch + this.func_70183_g()) / 180.0F * (float)Math.PI) * 0.4F;
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), 1.0F);
		this.motion = new Motion3D(this);
		this.damage = dmg;
	}
	
	public EntityBullet(World par1World, EntityLivingBase par2EntityLiving, float dmg, boolean rev) {
		this(par1World, par2EntityLiving, dmg);
		if(rev) setRenderFromLeft();
	}
	
	
	public EntityBullet(World par1World, Entity ent, Entity target, float dmg) {
		super(par1World);
		motionX = target.posX  - ent.posX;
		motionY = (target.posY + target.height / 2.0) - (ent.posY + ent.height / 2.0);
		motionZ = target.posZ - ent.posZ;
		double d = Math.sqrt(motionX * motionX + motionY + motionY + motionZ * motionZ);
		motionX /= d;
		motionY /= d;
		motionZ /= d;
		setPosition(ent.posX + motionX * 1.0, ent.posY + ent.height / 2.0, ent.posZ + motionZ * 1.0);
		this.setThrowableHeading(motionX, motionY, motionZ, func_70182_d(), 1.0F);
		damage = dmg;
		motion = new Motion3D(this);
	}
	
	public EntityBullet(World world, Vec3 begin, Vec3 motion, float dmg) {
		super(world);
		setPosition(begin.xCoord, begin.yCoord, begin.zCoord);
		setThrowableHeading(motion.xCoord, motion.yCoord, motion.zCoord, func_70182_d(), 1.0F);
		this.motion = new Motion3D(this);
		damage = dmg;
	}
	
	public EntityBullet setEntitySelector(IEntitySelector sel) {
		selector = sel;
		return this;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (ticksExisted > 50)
			this.setDead();
	}

	@Override
	protected void entityInit() {
	}

	public EntityBullet(World world) {
		super(world);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.0F;
	}

	@Override
	protected void onImpact(MovingObjectPosition par1) {
		switch (par1.typeOfHit) {
		case BLOCK:
			doBlockCollision(par1);
			break;
		case ENTITY:
			doEntityCollision(par1);
			break;
		default:
			break;
		}
	}

	protected void doBlockCollision(MovingObjectPosition result) {
		  //Block block = worldObj.getBlock(result.blockX, result.blockY, result.blockZ);
		//if(!worldObj.isRemote && (block == Blocks.glass || block == Blocks.glass_pane)) {
		//	worldObj.destroyBlock(result.blockX, result.blockY, result.blockZ, false);
		//	worldObj.destroyBlockInWorldPartially(p_147443_1_, p_147443_2_, p_147443_3_, p_147443_4_, p_147443_5_);
		//}
		//TODO:子弹的选择性方块摧毁？
		this.setDead();
	}

	protected void doEntityCollision(MovingObjectPosition result) {
		if (selector != null && !selector.isEntityApplicable(result.entityHit))
			return;
		result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(getThrower()), damage);
		result.entityHit.hurtResistantTime = -1;
	}

	public float getBulletDamage(int mode) {
		return damage;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected float func_70182_d() {
		return worldObj.isRemote ? 3.0F : 15.0F;
	}


}
