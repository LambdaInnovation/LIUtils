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
package cn.liutils.api.entityx;

import net.minecraft.util.Vec3;

/**
 * Generic interface handling entity motion.
 * @author WeathFolD
 */
public abstract class MotionHandler<T extends EntityX> implements Comparable<MotionHandler> {
	
	protected final T entity;
	public boolean alive = true;
	
	public MotionHandler(T ent) {
		this.entity = ent;
	}
	
	/**
	 * Called when the MotionHandler is constructed and added into world.
	 */
	public void onCreated() {}
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
		return Vec3.createVectorHelper(x, y, z);
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
