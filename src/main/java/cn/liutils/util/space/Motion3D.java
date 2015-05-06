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
	public double vx, vy, vz; //Velocity
	public double px, py, pz; //Position
	public static final double OFFSET_SCALE = 0.5D;

	public Motion3D(double pX, double pY, double pZ, double moX, double moY, double moZ) {
		px = pX;
		py = pY;
		pz = pZ;

		vx = moX;
		vy = moY;
		vz = moZ;
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
		px = ano.px;
		py = ano.py;
		pz = ano.pz;
		vx = ano.vx;
		vy = ano.vy;
		vz = ano.vz;
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
		this.px = entity.posX;
		this.py = entity.posY + entity.getEyeHeight();
		this.pz = entity.posZ;
//		if(!entity.worldObj.isRemote && entity instanceof EntityPlayer) {
//			posY += 1.6;
//		}

		if (dirFlag) {
			float var3 = 1.0F, var4 = 0.0F;
			
			float rotationYaw = entity.getRotationYawHead() + 2 * (RNG.nextFloat() - 0.5F) * offset;
			float rotationPitch = entity.rotationPitch + (RNG.nextFloat() - 0.5F) * offset;
			//System.out.println(rotationYaw + " " + rotationPitch);
			this.vx = -MathHelper.sin(rotationYaw / 180.0F
					* (float) Math.PI)
					* MathHelper.cos(rotationPitch / 180.0F
							* (float) Math.PI) * var3;
			this.vz = MathHelper.cos(rotationYaw / 180.0F
					* (float) Math.PI)
					* MathHelper.cos(rotationPitch / 180.0F
							* (float) Math.PI) * var3;
			this.vy = -MathHelper.sin((rotationPitch + var4)
					/ 180.0F * (float) Math.PI)
					* var3;
			
			
		} else {
			vx = entity.motionX;
			vy = entity.motionY;
			vz = entity.motionZ;
			setMotionOffset(offset);
		}
		this.normalize();
	}
	
	public Motion3D setPosition(double x, double y, double z) {
		px = x;
		py = y;
		pz = z;
		return this;
	}
	
	public Motion3D multiplyMotionBy(double d) {
		vx *= d;
		vy *= d;
		vz *= d;
		return this;
	}
	
	public void update(Entity entity, boolean dirFlag) {
		this.px = entity.posX;
		this.py = entity.posY + entity.getEyeHeight();
		this.pz = entity.posZ;

		if (dirFlag) {
			calcMotionByRotation(entity.rotationYaw, entity.rotationPitch);
		} else {
			vx = entity.motionX;
			vy = entity.motionY;
			vz = entity.motionZ;
		}
	}
	
	public Motion3D calcMotionByRotation(float yaw, float pitch) {
		float var3 = 1.0f;
		this.vx = -MathHelper.sin(yaw / 180.0F
				* (float) Math.PI)
				* MathHelper.cos(yaw / 180.0F
						* (float) Math.PI) * var3;
		this.vz = MathHelper.cos(yaw / 180.0F
				* (float) Math.PI)
				* MathHelper.cos(pitch / 180.0F
						* (float) Math.PI) * var3;
		this.vy = -MathHelper.sin((pitch)
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
		this.vx += (RNG.nextDouble() - .5) * par1 * OFFSET_SCALE;
		this.vy += (RNG.nextDouble() - .5) * par1 * OFFSET_SCALE;
		this.vz += (RNG.nextDouble() - .5) * par1 * OFFSET_SCALE;
		return this;
	}
	
	/*
	 * @param mo
	 * @param e
	 */
	public void applyToEntity(Entity e) {
		e.setPosition(this.px, this.py, this.pz);
		//normalize();
		e.motionX = this.vx;
		e.motionY = this.vy;
		e.motionZ = this.vz;
		double tmp = Math.sqrt(vx * vx + vz * vz);
		e.prevRotationYaw = e.rotationYaw = -(float)(Math.atan2(vx, vz) * 180.0D / Math.PI);
        e.prevRotationPitch = e.rotationPitch = -(float)(Math.atan2(vy, tmp) * 180.0D / Math.PI);
	}
	
	/**
	 * Move this motion3D towards motion vec direction. Didn't copy the class.
	 * @param step
	 * @return this
	 */
	public Motion3D move(double step) {
		px += vx * step;
		py += vy * step;
		pz += vz * step;
		return this;
	}
	
	/**
	 * Normalize the motion vector.
	 * @return this
	 */
	public Motion3D normalize() {
		double z = Math.sqrt(vx * vx + vy * vy + vz * vz);
		vx /= z;
		vy /= z;
		vz /= z;
		return this;
	}
	
	public double distanceTo(double x, double y, double z) {
		double a = x - px, b = y - py, c = z - pz;
		return Math.sqrt(a * a + b * b + c * c);
	}

	/**
	 * 获取以本身和另外一个MotionXYZ的pos为顶点的碰撞箱。
	 * @param another
	 * @return
	 */
	public final AxisAlignedBB getBoundingBox(Motion3D another) {
		double minX, minY, minZ, maxX, maxY, maxZ;
		if (another.px > this.px) {
			minX = this.px;
			maxX = another.px;
		} else {
			minX = another.px;
			maxX = this.px;
		}
		if (another.py > this.py) {
			minY = this.py;
			maxY = another.py;
		} else {
			minY = another.py;
			maxY = this.py;
		}
		if (another.pz > this.pz) {
			minZ = this.pz;
			maxZ = another.pz;
		} else {
			minZ = another.pz;
			maxZ = this.pz;
		}
		return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * get position vector
	 * @param world
	 * @return
	 */
	public Vec3 getPosVec(World world) {
		return Vec3.createVectorHelper(px, py, pz);
	}
	
	public Vec3 getMotionVec(World world) {
		return Vec3.createVectorHelper(vx, vy, vz);
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
		return "[ Motion3D POS" + DebugUtils.formatArray(px, py, pz) + "MOTION" + DebugUtils.formatArray(vx, vy, vz) + " ]";
	}

}
