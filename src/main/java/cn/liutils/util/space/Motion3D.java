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
package cn.liutils.util.space;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.util.DebugUtils;

/**
 * A entity that has its position and motion properties. used for all kinds of spacial calculations.
 * @author WeAthFolD
 */
public class Motion3D {

	private static final Random RNG = new Random();
	public double motionX, motionY, motionZ;
	public double posX, posY, posZ;
	public static final double OFFSET_SCALE = 0.5D;

	public Motion3D(double pX, double pY, double pZ, double moX, double moY, double moZ) {
		posX = pX;
		posY = pY;
		posZ = pZ;

		motionX = moX;
		motionY = moY;
		motionZ = moZ;
	}

	public Motion3D(Vec3 posVec, double moX, double moY, double moZ) {
		this(posVec.xCoord, posVec.yCoord, posVec.zCoord, moX, moY, moZ);
	}

	public Motion3D(Motion3D another) {
		this();
		setFrom(another);
	}
	
	public Motion3D() {
		this(0, 0, 0, 0, 0, 0);
	}
	
	@Override
	public Motion3D clone() {
		return new Motion3D(this);
	}
	
	public void setFrom(Motion3D ano) {
		posX = ano.posX;
		posY = ano.posY;
		posZ = ano.posZ;
		motionX = ano.motionX;
		motionY = ano.motionY;
		motionZ = ano.motionZ;
	}
	
	/**
	 * Create an Motion3D based on its position and moving direction.
	 */
	public Motion3D(Entity ent) {
		this(ent, 0, false);
	}

	/**
	 * @see cn.liutils.api.util.Motion3D(Entity, int, boolean)
	 * @param ent entity
	 * @param dirFlag false:use moving direction; true:use head-looking direction
	 */
	public Motion3D(Entity ent, boolean dirFlag) {
		this(ent, 0, dirFlag);
	}

	/**
	 * Create an Motion3D from an entity, selectible offset.
	 * @param entity
	 * @param offset direction offset
	 * @param dirFlag false:use moving direction; true:use head-looking direction
	 */
	public Motion3D(Entity entity, int offset, boolean dirFlag) {
		init(entity, offset, dirFlag);
	}
	
	/**
	 * Reset the pos and motion data to the data of entity passed in
	 * @param entity
	 * @param offset
	 * @param dirFlag true: use head facing direction; flase: use motion
	 */
	public void init(Entity entity, int offset, boolean dirFlag) {
		this.posX = entity.posX;
		this.posY = entity.posY + entity.getEyeHeight();
		this.posZ = entity.posZ;
//		if(!entity.worldObj.isRemote && entity instanceof EntityPlayer) {
//			posY += 1.6;
//		}

		if (dirFlag) {
			float var3 = 1.0F, var4 = 0.0F;
			
			float rotationYaw = entity.getRotationYawHead() + 2 * (RNG.nextFloat() - 0.5F) * offset;
			float rotationPitch = entity.rotationPitch + (RNG.nextFloat() - 0.5F) * offset;
			//System.out.println(rotationYaw + " " + rotationPitch);
			this.motionX = -MathHelper.sin(rotationYaw / 180.0F
					* (float) Math.PI)
					* MathHelper.cos(rotationPitch / 180.0F
							* (float) Math.PI) * var3;
			this.motionZ = MathHelper.cos(rotationYaw / 180.0F
					* (float) Math.PI)
					* MathHelper.cos(rotationPitch / 180.0F
							* (float) Math.PI) * var3;
			this.motionY = -MathHelper.sin((rotationPitch + var4)
					/ 180.0F * (float) Math.PI)
					* var3;
			
			
		} else {
			motionX = entity.motionX;
			motionY = entity.motionY;
			motionZ = entity.motionZ;
			setMotionOffset(offset);
		}
		this.normalize();
	}
	
	public Motion3D setPosition(double x, double y, double z) {
		posX = x;
		posY = y;
		posZ = z;
		return this;
	}
	
	public void update(Entity entity, boolean dirFlag) {
		this.posX = entity.posX;
		this.posY = entity.posY + entity.getEyeHeight();
		this.posZ = entity.posZ;

		if (dirFlag) {
			calcMotionByRotation(entity.rotationYaw, entity.rotationPitch);
		} else {
			motionX = entity.motionX;
			motionY = entity.motionY;
			motionZ = entity.motionZ;
		}
	}
	
	public Motion3D calcMotionByRotation(float yaw, float pitch) {
		float var3 = 1.0f;
		this.motionX = -MathHelper.sin(yaw / 180.0F
				* (float) Math.PI)
				* MathHelper.cos(yaw / 180.0F
						* (float) Math.PI) * var3;
		this.motionZ = MathHelper.cos(yaw / 180.0F
				* (float) Math.PI)
				* MathHelper.cos(pitch / 180.0F
						* (float) Math.PI) * var3;
		this.motionY = -MathHelper.sin((pitch)
				/ 180.0F * (float) Math.PI)
				* var3;
		this.normalize();
		return this;
	}

	
	/**
	 * Add a 3-dimension randomized offset for the motion vec.
	 * @param par1
	 * @return
	 */
	public Motion3D setMotionOffset(double par1) {
		this.motionX += (RNG.nextDouble() - .5) * par1 * OFFSET_SCALE;
		this.motionY += (RNG.nextDouble() - .5) * par1 * OFFSET_SCALE;
		this.motionZ += (RNG.nextDouble() - .5) * par1 * OFFSET_SCALE;
		return this;
	}
	
	/*
	 * @param mo
	 * @param e
	 */
	public void applyToEntity(Entity e) {
		e.setPosition(this.posX, this.posY, this.posZ);
		normalize();
		e.motionX = this.motionX;
		e.motionY = this.motionY;
		e.motionZ = this.motionZ;
		double tmp = Math.sqrt(motionX * motionX + motionZ * motionZ);
		e.prevRotationYaw = e.rotationYaw = -(float)(Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
        e.prevRotationPitch = e.rotationPitch = -(float)(Math.atan2(motionY, tmp) * 180.0D / Math.PI);
	}
	
	/**
	 * Move this motion3D towards motion vec direction. Didn't copy the class.
	 * @param step
	 * @return this
	 */
	public Motion3D move(double step) {
		posX += motionX * step;
		posY += motionY * step;
		posZ += motionZ * step;
		return this;
	}
	
	/**
	 * Normalize the motion vector.
	 * @return this
	 */
	public Motion3D normalize() {
		double z = Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ);
		motionX /= z;
		motionY /= z;
		motionZ /= z;
		return this;
	}
	
	public double distanceTo(double x, double y, double z) {
		double a = x - posX, b = y - posY, c = z - posZ;
		return Math.sqrt(a * a + b * b + c * c);
	}

	/**
	 * 获取以本身和另外一个MotionXYZ的pos为顶点的碰撞箱。
	 * @param another
	 * @return
	 */
	public final AxisAlignedBB getBoundingBox(Motion3D another) {
		double minX, minY, minZ, maxX, maxY, maxZ;
		if (another.posX > this.posX) {
			minX = this.posX;
			maxX = another.posX;
		} else {
			minX = another.posX;
			maxX = this.posX;
		}
		if (another.posY > this.posY) {
			minY = this.posY;
			maxY = another.posY;
		} else {
			minY = another.posY;
			maxY = this.posY;
		}
		if (another.posZ > this.posZ) {
			minZ = this.posZ;
			maxZ = another.posZ;
		} else {
			minZ = another.posZ;
			maxZ = this.posZ;
		}
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * get position vector
	 * @param world
	 * @return
	 */
	public Vec3 getPosVec(World world) {
		return Vec3.createVectorHelper(posX, posY, posZ);
	}
	
	public Vec3 getMotionVec(World world) {
		return Vec3.createVectorHelper(motionX, motionY, motionZ);
	}
	
	public MovingObjectPosition applyRaytrace(World world) {
		return applyRaytrace(world, 100.0);
	}
	
	public MovingObjectPosition applyRaytrace(World world, double maxdist) {
		Vec3 dir = this.getMotionVec(world).normalize();
		Vec3 pos = this.getPosVec(world);
		Vec3 to = pos.addVector(dir.xCoord * maxdist, dir.yCoord * maxdist, dir.zCoord * maxdist);
		return world.rayTraceBlocks(pos, to);
	}
	
	@Override
	public String toString() {
		return "[ Motion3D POS" + DebugUtils.formatArray(posX, posY, posZ) + "MOTION" + DebugUtils.formatArray(motionX, motionY, motionZ) + " ]";
	}

}
