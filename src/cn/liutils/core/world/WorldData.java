package cn.liutils.core.world;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.world.World;
import cn.liutils.core.energy.EnergyNet;

public class WorldData {
	private static Map<World, WorldData> mapping = new WeakHashMap<World, WorldData>();

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
