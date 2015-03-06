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
package cn.liutils.core.energy;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.world.World;

public class WorldData {
	private static Map<World, WorldData> mapping = new WeakHashMap();

	/*
	 * public Queue<ITickCallback> singleTickCallbacks = new ArrayDeque();
	 * public Set<ITickCallback> continuousTickCallbacks = new HashSet(); public
	 * boolean continuousTickCallbacksInUse = false; public List<ITickCallback>
	 * continuousTickCallbacksToAdd = new ArrayList(); public
	 * List<ITickCallback> continuousTickCallbacksToRemove = new ArrayList();
	 */

	public EnergyNet energyNet = new EnergyNet();

	// public Set<NetworkManager.TileEntityField> networkedFieldsToUpdate = new
	// HashSet();
	public int ticksLeftToNetworkUpdate = 2;

	public static WorldData get(World world) {
		if (world == null)
			throw new IllegalArgumentException("world is null");

		WorldData rew = mapping.get(world);

		if (rew == null) {
			rew = new WorldData();
			mapping.put(world, rew);
		}
		return rew;
	}

	public static void onWorldUnload(World world) {
		mapping.remove(world);
	}
}
