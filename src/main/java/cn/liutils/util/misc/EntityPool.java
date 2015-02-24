/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.util.misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cn.liutils.core.event.eventhandler.LIFMLGameEventDispatcher;
import cn.liutils.core.event.eventhandler.LIHandler;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;

/**
 * Simple entity pool for reusing. This is usually used for temp entities like particle effects.
 * @author WeathFolD
 */
public abstract class EntityPool<T extends Entity> {
	
	static final int MAX_POOL_SIZE = 200;
	
	HashMap<World, WorldPool> pools = new HashMap();
	
	Trigger trigger = new Trigger();

	public EntityPool() {
		trigger.setDead();
	}
	
	public void activate() {
		trigger.setAlive();
		LIFMLGameEventDispatcher.INSTANCE.registerWorldTick(trigger);
	}
	
	public void deactivate() {
		trigger.setDead();
	}
	
	public boolean isActive() {
		return trigger.isDead();
	}
	
	public T getEntity(World world) {
		T ent = getPool(world).get();
		resetEntity(ent);
		return ent;
	}
	
	private WorldPool getPool(World world) {
		WorldPool ret = pools.get(world);
		if(ret == null) {
			ret = new WorldPool(world);
			pools.put(world, ret);
		}
		return ret;
	}
	
	public abstract T createEntity(World world);
	
	public abstract void resetEntity(T ent);
	
	private class WorldPool {
		Set<T> aliveEntities = new HashSet(),
				deadEntities = new HashSet();
		int poolSize = 0;
		
		public final World world;
		
		public WorldPool(World _world) {
			world = _world;
		}
		
		/**
		 * The added entity must be alive.
		 */
		private void add(T ent) {
			aliveEntities.add(ent);
		}
		
		/**
		 * Get a previously used entity. its death flag will be automatically restored.
		 */
		public T get() {
			if(deadEntities.isEmpty()) {
				T ret = createEntity(world);
				add(ret);
				return ret;
			}
			Iterator<T> iter = deadEntities.iterator();
			T ret = iter.next();
			ret.isDead = false;
			aliveEntities.add(ret);
			return ret;
		}
		
		public void onTick() {
			Iterator<T> iter = aliveEntities.iterator();
			while(iter.hasNext()) {
				T ent = iter.next();
				if(ent.isDead) {
					if(deadEntities.size() < MAX_POOL_SIZE) {
						deadEntities.add(ent);
					}
					iter.remove();
				}
			}
		}
		
	}
	
	private class Trigger extends LIHandler<WorldTickEvent> {

		@Override
		protected boolean onEvent(WorldTickEvent event) {
			if(event.phase == Phase.END) {
				WorldPool wp = pools.get(event.world);
				if(wp != null)
					wp.onTick();
			}
			return true;
		}
		
	}

}
