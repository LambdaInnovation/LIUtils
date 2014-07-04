/** 
 * Copyright (c) LambdaCraft Modding Team, 2013
 * 版权许可：LambdaCraft 制作小组， 2013.
 * http://lambdacraft.half-life.cn/
 * 
 * LambdaCraft is open-source. It is distributed under the terms of the
 * LambdaCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 *
 * LambdaCraft是完全开源的。它的发布遵从《LambdaCraft开源协议》。你允许阅读，修改以及调试运行
 * 源代码， 然而你不允许将源代码以另外任何的方式发布，除非你得到了版权所有者的许可。
 */
package cn.liutils.api.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * 不进行实体碰撞的简单投射类，在EntitySatchel中被使用。
 * 
 * @author WeAthFolD
 * 
 */
public abstract class EntityProjectile extends Entity {

	private EntityPlayer thrower;

	public EntityProjectile(World par1World, EntityPlayer par2EntityLiving) {
		super(par1World);
		this.thrower = par2EntityLiving;
		this.setSize(0.25F, 0.25F);
		this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY
				+ par2EntityLiving.getEyeHeight(), par2EntityLiving.posZ,
				par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
		this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		this.posY -= 0.10000000149011612D;
		this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
		this.setPosition(this.posX, this.posY, this.posZ);
		this.yOffset = 0.0F;
		float f = 0.4F;
		this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F
				* (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI)
				* f;
		this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F
				* (float) Math.PI)
				* MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI)
				* f;
		this.motionY = -MathHelper.sin((this.rotationPitch + this
				.getMotionYOffset()) / 180.0F * (float) Math.PI)
				* f;
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ,
				this.getHeadingVelocity(), 1.0F);
	}

	public EntityProjectile(World world) {
		super(world);
	}

	protected abstract float getHeadingVelocity();

	protected abstract float getMotionYOffset();

	protected abstract float getGravityVelocity();

	protected abstract void onCollide(MovingObjectPosition result);

	/**
	 * Similar to setArrowHeading, it's point the throwable entity to a x, y, z
	 * direction.
	 */
	public void setThrowableHeading(double par1, double par3, double par5,
			float par7, float par8) {
		float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5
				* par5);
		par1 /= f2;
		par3 /= f2;
		par5 /= f2;
		par1 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
		par3 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
		par5 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
		par1 *= par7;
		par3 *= par7;
		par5 *= par7;
		this.motionX = par1;
		this.motionY = par3;
		this.motionZ = par5;
		float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
		this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1,
				par5) * 180.0D / Math.PI);
		this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3,
				f3) * 180.0D / Math.PI);
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (worldObj.isRemote)
			return;
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (!this.onGround)
			this.motionY -= getGravityVelocity();
		Vec3 pre = Vec3.createVectorHelper(posX, posY, posZ), after;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
		after = Vec3.createVectorHelper(posX, posY, posZ);

		MovingObjectPosition result = worldObj.clip(pre, after);
		if (result != null && result.typeOfHit == EnumMovingObjectType.TILE) {
			this.onCollide(result);
		}

		this.motionX *= 0.99D;
		this.motionY *= 0.99D;
		this.motionZ *= 0.99D;

		if (this.onGround) {
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
			this.motionY *= -0.5D;
		}
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return !this.isDead;
	}

	public EntityPlayer getThrower() {
		return thrower;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		if (worldObj.isRemote) {
			this.setDead();
			return;
		}
		posX = nbt.getDouble("posX");
		posY = nbt.getDouble("posY");
		posZ = nbt.getDouble("posZ");
		motionX = nbt.getDouble("motionX");
		motionY = nbt.getDouble("motionY");
		motionZ = nbt.getDouble("motionZ");
		EntityPlayer th = worldObj.getPlayerEntityByName(nbt
				.getString("thrower"));
		if (th != null)
			this.thrower = th;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		if (worldObj.isRemote)
			return;
		nbt.setDouble("posX", posX);
		nbt.setDouble("posY", posY);
		nbt.setDouble("posZ", posZ);
		nbt.setDouble("motionX", motionX);
		nbt.setDouble("motionY", motionY);
		nbt.setDouble("motionZ", motionZ);
		if (thrower != null)
			nbt.setString("thrower", getThrower().getEntityName());
	}

}
