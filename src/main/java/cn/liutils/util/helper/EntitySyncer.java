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
package cn.liutils.util.helper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.DataWatcher.WatchableObject;
import net.minecraft.entity.Entity;
import cn.liutils.api.annotation.Experimental;
import cn.liutils.util.generic.RegistryUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * A helper to help syncing fields within entity, which gets rid of the ANNOYING 
 * registering proccess. Supports all the type that is supported by DataWatcher. <br>
 * You should delegate the init() method within entityInit(), and update() method within onUpdate().
 * <br> The direction is always server -> client.
 * <br> The registered fields should be symmetric in two sides so that we can track the ID correctly.
 * @author WeAthFolD
 */
@Experimental
public class EntitySyncer {
	
	public enum SyncType {
		/**
		 * This field is only synchronized on startup.
		 */
		ONCE,
		
		/**
		 * This field is synchronized every tick when entity is alive.
		 */
		RUNTIME
	}
	
	public @interface Synchronized {
		
		SyncType syncType() default SyncType.RUNTIME;
		
	}
	
	BiMap<Integer, Class<?>> typeMap = HashBiMap.create();
	
	final Entity entity;
	final DataWatcher dataWatcher;
	final HashMap<Integer, WatchableObject> watchedObjects;
	
	final Map<Integer, SyncInstance> watched;
	
	public EntitySyncer(Entity ent) {
		entity = ent;
		dataWatcher = RegistryUtils.getFieldInstance(Entity.class, ent, "dataWatcher", "field_70180_af");
		watchedObjects = RegistryUtils.getFieldInstance(DataWatcher.class, dataWatcher, 
				"watchedObjects", "field_75695_b");
		watched = new HashMap();
		
	}
	
	interface Test {
		void call(Object o);
	}
	
	/**
	 * Delegated when the entity enters entityInit().
	 */
	public void init() {
		for(Field f : entity.getClass().getDeclaredFields()) {
			if(f.isAnnotationPresent(Synchronized.class)) {
				int id = nextID();
				watched.put(id, new SyncInstance(id, f));
			}
		}
	}
	
	/**
	 * Delegated during entity onUpdate() tick.
	 */
	public void update() {
	
	}
	
	private int nextID() {
		for(int i = 0;; ++i) {
			if(!watchedObjects.containsValue(i))
				return i;
		}
	}
	
	private class SyncInstance {
		
		public SyncInstance(int id, Field f) {
			
		}
		
		
	}

}
