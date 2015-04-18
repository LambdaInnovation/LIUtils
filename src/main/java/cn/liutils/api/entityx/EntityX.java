/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 譛ｬ菴懷刀迚域揀逕ｱLambda Innovation謇�怏縲�
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 譛ｬ鬘ｹ逶ｮ譏ｯ荳�ｸｪ蠑�ｺ宣｡ｹ逶ｮ�御ｸ秘�蠕ｪGNU騾夂畑蜈ｬ蜈ｱ謗域揀蜊剰ｮｮ縲�
 * 蝨ｨ驕ｵ辣ｧ隸･蜊剰ｮｮ逧�ュ蜀ｵ荳具ｼ梧お蜿ｯ莉･閾ｪ逕ｱ莨�眺蜥御ｿｮ謾ｹ縲�
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.entityx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

/**
 * Enhanced entity motion mechanism. Allow us to handle entity position&motion in
 * a object-oriented way. use different combination of MotionHandler to produce different
 * effects.
 * @author WeathFolD
 */
public abstract class EntityX extends Entity {
	
	Map<Integer, List<EntityCallback>> events = new HashMap();
	
	private MotionHandler curMotion;
	private Map<String, MotionHandler> daemonHandlers = new HashMap();
	
	protected boolean updated = false;
	public boolean handleClient = true;

	public EntityX(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {}
	
	public void execAfter(int ticks, EntityCallback bc) {
		List<EntityCallback> res = events.get(ticks);
		if(res == null) {
			res = new ArrayList();
			events.put(ticks + ticksExisted, res);
		}
		res.add(bc);
	}
	
	@Override
	public void onUpdate() {
		//super.onUpdate();
		boolean doesUpdate = doesUpdate();
		//System.out.println("Updating " + this);
		
		if(!updated) {
			updated = true;
		}
		//++ticksExisted;
		
		//vec copy
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		
		if(doesUpdate) {
			updateAll(onUpdate);
		}
		
		List<EntityCallback> res = events.get(ticksExisted);
		if(res != null) {
			for(EntityCallback cb : res)
				cb.execute(this);
		}
	}
	
	public boolean removeDaemonHandler(String ID) {
		MotionHandler rem = daemonHandlers.remove(ID);
		return rem != null;
	}
	
	public void clearDaemonHandlers() {
		daemonHandlers.clear();
	}
	
	public void addDaemonHandler(MotionHandler mh) {
		if(hasMotionHandler(mh.getID())) {
			throw new RuntimeException("ID Collision: trying to add [" + 
				mh.getID() + "] into " + this);
		}
		daemonHandlers.put(mh.getID(), mh);
		mh.onCreated();
	}
	
	public MotionHandler getDaemonHandler(String id) {
		return daemonHandlers.get(id);
	}
	
	public void setCurMotion(MotionHandler mh) {
		curMotion = mh;
	}
	
	public boolean hasMotionHandler(String str) {
		return daemonHandlers.containsKey(str);
	}
	
    public void setHeading(double dx, double dy, double dz, double vel) {
        float f2 = MathHelper.sqrt_double(dx * dx + dy * dy + dz * dz);
        dx /= f2;
        dy /= f2;
        dz /= f2;
        dx *= vel;
        dy *= vel;
        dz *= vel;
        this.motionX = dx;
        this.motionY = dy;
        this.motionZ = dz;
        float f3 = MathHelper.sqrt_double(dx * dx + dz * dz);
        this.prevRotationYaw = this.rotationYaw = -(float)(Math.atan2(dx, dz) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = -(float)(Math.atan2(dy, f3) * 180.0D / Math.PI);
    }

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1) { }

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1) { }
	
	protected final Vec3 createVec(double x, double y, double z) {
		return Vec3.createVectorHelper(x, y, z);
	}
	
	protected final Vec3 createVec() {
		return createVec(0, 0, 0);
	}
	
	protected final void copyVec(Vec3 dst, Vec3 src) {
		dst.xCoord = src.xCoord;
		dst.yCoord = src.yCoord;
		dst.zCoord = src.zCoord;
	}
	
	protected boolean doesUpdate() {
		return !worldObj.isRemote || handleClient;
	}
	
	private void updateAll(Callback sth) {
		updateAll(sth, null);
	}
	
	private void updateAll(Callback cb, MotionHandler exclusion) {
		if(curMotion != null)  {
			if(!curMotion.alive) {
				curMotion = null;
			} else {
				cb.invoke(curMotion);
			}
		}
		Iterator<Map.Entry<String, MotionHandler>> iter = daemonHandlers.entrySet().iterator();
		synchronized(this) {
			while(iter.hasNext()) {
				MotionHandler mh = iter.next().getValue();
				if(!mh.alive) {
					iter.remove();
					continue;
				}
				if(mh != exclusion) {
					cb.invoke(mh);
				}
			}
		}
	}
	
	public static interface EntityCallback<T extends EntityX> {
		void execute(T ent);
	}
	
	private static interface Callback {
		void invoke(MotionHandler mo);
	}
	
	private static Callback onSpawned = new Callback() {
		@Override public void invoke(MotionHandler mo) {
			mo.onCreated();
		}
	};
	private static Callback onUpdate = new Callback() {
		@Override public void invoke(MotionHandler mo) {
			//System.out.println("OnUpdate " + mo.getID());
			mo.onUpdate();
		}
	};

}
