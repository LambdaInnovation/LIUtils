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
package cn.liutils.api.util;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * @author WeAthFolD
 * 
 */
public class Motion3D {

	private static final Random RNG = new Random();
	public double motionX, motionY, motionZ;
	public double posX, posY, posZ;
	public static final double OFFSET_SCALE = 0.5D;

	public Motion3D(double pX, double pY, double pZ, double moX, double moY,
			double moZ) {
		posX = pX;
		posY = pY;
		posZ = pZ;

		motionX = moX;
		motionY = moY;
		motionZ = moZ;
	}

	public Motion3D(Vec3 par0, double moX, double moY, double moZ) {
		this(par0.xCoord, par0.yCoord, par0.zCoord, moX, moY, moZ);
	}

	public Motion3D(Motion3D a) {
		this(a.posX, a.posY, a.posZ, a.motionX, a.motionY, a.motionZ);
	}
	
	public Motion3D(Entity ent) {
		this(ent, 0, false);
	}

	public Motion3D(Entity ent, boolean ahr) {
		this(ent, 0, ahr);
	}

	public Motion3D(Entity entity, int offset, boolean asHeadRotation) {
		this.posX = entity.posX;
		this.posY = entity.posY + entity.getEyeHeight();
		this.posZ = entity.posZ;

		if (asHeadRotation) {
			float var3 = 1.0F, var4 = 0.0F;
			this.motionX = -MathHelper.sin(entity.rotationYaw / 180.0F
					* (float) Math.PI)
					* MathHelper.cos(entity.rotationPitch / 180.0F
							* (float) Math.PI) * var3;
			this.motionZ = MathHelper.cos(entity.rotationYaw / 180.0F
					* (float) Math.PI)
					* MathHelper.cos(entity.rotationPitch / 180.0F
							* (float) Math.PI) * var3;
			this.motionY = -MathHelper.sin((entity.rotationPitch + var4)
					/ 180.0F * (float) Math.PI)
					* var3;
		} else {
			motionX = entity.motionX;
			motionY = entity.motionY;
			motionZ = entity.motionZ;
		}
		setMotionOffset(offset);
	}

	public Motion3D setMotionOffset(double par1) {
		this.motionX += (RNG.nextDouble() - 1) * par1 * OFFSET_SCALE;
		this.motionY += (RNG.nextDouble() - 1) * par1 * OFFSET_SCALE;
		this.motionZ += (RNG.nextDouble() - 1) * par1 * OFFSET_SCALE;
		return this;
	}
	
	public static void setMotionToEntity(Motion3D mo, Entity e) {
		e.setPosition(mo.posX, mo.posY, mo.posZ);
		e.motionX = mo.motionX;
		e.motionY = mo.motionY;
		e.motionZ = mo.motionZ;
	}
	
	/**
	 * 
	 * @param scale
	 * @return
	 */
	public Motion3D move(double scale) {
		posX += motionX * scale;
		posY += motionY * scale;
		posZ += motionZ * scale;
		return this;
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
	
	public Vec3 asVec3(World world) {
		return world.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
	}
	
	@Override
	public String toString() {
		return "[ Motion3D POS" + DebugUtils.formatArray(posX, posY, posZ) + "MOTION" + DebugUtils.formatArray(motionX, motionY, motionZ) + " ]";
	}

}
