/**
 * 
 */
package cn.liutils.api.entityx;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cn.liutils.api.entityx.motion.CollisionCheck;
import cn.liutils.api.entityx.motion.VelocityUpdate;
import cn.liutils.util.DebugUtils;

/**
 * Enhanced entity motion mechanism. Allow us to handle entity position&motion in
 * a object-oriented way. use different combination of MotionHandler to produce different
 * effects.
 * @author WeathFolD
 */
public abstract class EntityX extends Entity {
	
	private MotionHandler curMotion;
	private Map<String, MotionHandler> daemonHandlers = new HashMap();
	
	protected boolean updated = false;
	public boolean handleClient = true;

	public EntityX(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {}
	
	@Override
	public void onUpdate() {
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
	}
	
	public boolean removeDaemonHandler(String ID) {
		for(MotionHandler mh : daemonHandlers.values()) {
			if(mh.getID().equals(ID)) {
				daemonHandlers.remove(mh);
				return true;
			}
		}
		return false;
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
		//TODO: Naive algorithm, consider HashMap?
		return daemonHandlers.containsKey(str);
	}
	
	@Override
	/**
	 * In EntityX, use setPos(x, y, z) instead.
	 */
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
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
        this.prevRotationPitch = this.rotationPitch = -(float)(Math.atan2(dy, (double)f3) * 180.0D / Math.PI);
    }

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1) { }

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1) { }
	
	protected final Vec3 createVec(double x, double y, double z) {
		return worldObj.getWorldVec3Pool().getVecFromPool(x, y, z);
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
