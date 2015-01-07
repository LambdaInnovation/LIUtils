/**
 * 
 */
package cn.liutils.api.entityx;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
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
	private Queue<MotionHandler> daemonHandlers = new PriorityQueue<MotionHandler>();
	
	protected boolean updated = false;
	public boolean handleClient = false;

	public EntityX(World world) {
		super(world);
		addDaemonHandler(new VelocityUpdate(this));
		addDaemonHandler(new CollisionCheck(this));
	}

	@Override
	protected void entityInit() {}
	
	@Override
	public void onUpdate() {
		boolean doesUpdate = doesUpdate();
		//System.out.println("Updating " + this);
		
		if(!updated) {
			updated = true;
			if(doesUpdate)
				updateAll(onSpawned);
		}
		++ticksExisted;
		
		//vec copy
		this.lastTickPosX = this.posX;
		this.lastTickPosY = this.posY;
		this.lastTickPosZ = this.posZ;
		
		if(doesUpdate) {
			updateAll(onUpdate);
		}
	}
	
	public boolean removeDaemonHandler(String ID) {
		for(MotionHandler mh : daemonHandlers) {
			if(mh.getID().equals(ID)) {
				daemonHandlers.remove(mh);
				return true;
			}
		}
		return false;
	}
	
	public void addDaemonHandler(MotionHandler mh) {
		if(hasMotionHandler(mh.getID())) {
			throw new RuntimeException("ID Collision: trying to add [" + 
				mh.getID() + "] into " + this);
		}
		daemonHandlers.add(mh);
	}
	
	public void setCurMotion(MotionHandler mh) {
		curMotion = mh;
	}
	
	public boolean hasMotionHandler(String str) {
		//TODO: Naive algorithm, consider HashMap?
		if(curMotion != null && curMotion.getID().equals(str))
			return true;
		for(MotionHandler mh : daemonHandlers) {
			if(mh.getID().equals(str))
				return true;
		}
		return false;
	}
	
	@Override
	/**
	 * In EntityX, use setPos(x, y, z) instead.
	 */
	public void setPosition(double x, double y, double z) {
		super.setPosition(x, y, z);
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
	
	private void updateAll(DoSth sth) {
		updateAll(sth, null);
	}
	
	private void updateAll(DoSth sth, MotionHandler exclusion) {
		if(curMotion != null)  {
			if(!curMotion.alive) {
				curMotion = null;
			} else {
				sth.dosth(curMotion);
			}
		}
		Iterator<MotionHandler> iter = daemonHandlers.iterator();
		while(iter.hasNext()) {
			MotionHandler mh = iter.next();
			if(!mh.alive) {
				iter.remove();
				continue;
			}
			if(mh != exclusion) {
				sth.dosth(mh);
			}
		}
	}
	
	private static interface DoSth {
		void dosth(MotionHandler mo);
	}
	
	private static DoSth onSpawned = new DoSth() {
		@Override public void dosth(MotionHandler mo) {
			mo.onSpawnedInWorld();
		}
	};
	private static DoSth onUpdate = new DoSth() {
		@Override public void dosth(MotionHandler mo) {
			//System.out.println("OnUpdate " + mo.getID());
			mo.onUpdate();
		}
	};

}
