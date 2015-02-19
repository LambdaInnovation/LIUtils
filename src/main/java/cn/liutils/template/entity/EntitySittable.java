/**
 * Copyright (C) Lambda-Innovation, 2013-2014
 * This code is open-source. Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 */
package cn.liutils.template.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.annoreg.core.RegistrationClass;
import cn.annoreg.mc.RegEntity;

/**
 * TileSittable的支持实体~
 * 
 * @author WeAthFolD
 */
@RegistrationClass
@RegEntity
public class EntitySittable extends Entity {
	/**
	 * Marking interface.
	 */
	public static interface ISittable {}
	
	public EntityPlayer mountedPlayer;
	Vec3 startPt;
	int bx, by, bz;

	public EntitySittable(World wrld, float x, float y, float z, int bx,
			int by, int bz) {
		super(wrld);
		setPosition(x, y, z);
		setSize(0.01F, 0.01F);
		startPt = Vec3.createVectorHelper(x, y, z);
		this.bx = bx;
		this.by = by;
		this.bz = bz;
	}

	public EntitySittable(World wrld) {
		super(wrld);
		setSize(0.01F, 0.01F);
	}

	public void mount(EntityPlayer player) {
		player.mountEntity(this);
		mountedPlayer = player;
	}

	public void disMount() {
		if (mountedPlayer != null) {
			mountedPlayer.mountEntity((Entity) null);
			mountedPlayer = null;
		}
	}

	public boolean isMounted() {
		return mountedPlayer != null;
	}

	@Override
	public void onUpdate() {
		if(mountedPlayer != null && mountedPlayer.isDead) {
			mountedPlayer = null;
		}
		
		if (!worldObj.isRemote) {
			TileEntity te = worldObj.getTileEntity(bx, by, bz);
			if (te == null || !(te instanceof ISittable)) {
				setDead();
				return;
			}
			if (startPt != null) {
				posX = startPt.xCoord;
				posY = startPt.yCoord;
				posZ = startPt.zCoord;
			} else {
				setDead();
			}
		}
	}

	@Override
	protected void entityInit() {
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		setDead();
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
	}
}