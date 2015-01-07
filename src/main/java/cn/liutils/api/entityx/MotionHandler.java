/**
 * 
 */
package cn.liutils.api.entityx;

import net.minecraft.util.Vec3;

/**
 * Generic interface handling entity motion.
 * If you want the motion handler to be re-created on reloading(which is the most case),
 * you must keep the default constructor in the MotionHandler.
 * @author WeathFolD
 */
public abstract class MotionHandler<T extends EntityX> implements Comparable<MotionHandler> {
	
	protected final T entity;
	public boolean alive = true;
	
	public MotionHandler(T ent) {
		this.entity = ent;
	}
	
	/**
	 * Called when entity is first updated.
	 */
	public abstract void onSpawnedInWorld();
	public abstract void onUpdate();
	/**
	 * Get the identifier of this MotionHandler. Within an entity,
	 * the ID is supposed to be mutual exclusive.
	 */
	public abstract String getID();
	
	/**
	 * Return the priority value of this handler,
	 * Handlers with higher priority will be executed first each tick update.
	 * You are supposed not to return value bigger than 100.
	 * (They are keeped for internal updates)
	 * @return the priority value
	 */
	public int getPriority() {
		return 0;
	}
	
	@Override
	public int hashCode() {
		return getID().hashCode();
	}
	
	protected Vec3 createVector(double x, double y, double z) {
		return entity.worldObj.getWorldVec3Pool().getVecFromPool(x, y, z);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof MotionHandler)) {
			return false;
		}
		MotionHandler<?> mh = (MotionHandler<?>) obj;
		return mh.entity == entity && getID().equals(mh.getID());
	}
	
	@Override
	public int compareTo(MotionHandler mh) {
		int mp = getPriority(), np = mh.getPriority();
		return mp > np ? 1 : (mp == np ? 0 : -1);
	}
	
}
