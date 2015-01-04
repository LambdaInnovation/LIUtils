/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.core.entity;

import java.util.HashSet;
import java.util.Set;

import cn.liutils.api.render.IPlayerRenderListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * A entity that always in the position of a player entity, in aid of player additional rendering.
 * @author WeAthFolD
 */
public class EntityPlayerDaemon extends Entity {

	public EntityPlayer player;
	
	private static Set<IPlayerRenderListener> helpers = new HashSet();
	public Set<IPlayerRenderListener> activeHelpers = new HashSet();
	
	/**
	 */
	public EntityPlayerDaemon(EntityPlayer p, World world) {
		super(world);
		player = p;
		setPositionAndRotation(p.posX, p.posY, p.posZ, p.rotationYaw, p.rotationPitch);
	}
	
    @Override
	public void onUpdate()
    {
    	setPositionAndRotation(player.posX, player.posY, player.posZ, player.renderYawOffset, player.rotationPitch);
		for(IPlayerRenderListener p : helpers) {
			if(p.isActivated(player, worldObj)) {
				activeHelpers.add(p);
			} else activeHelpers.remove(p);
		}
    }
    
    public static boolean hasActivate(EntityPlayer player) {
    	for(IPlayerRenderListener p : helpers)
    		if(p.isActivated(player, player.worldObj)) return true;
    	return false;
    }
    
    public static void addRenderHelper(IPlayerRenderListener helper) {
    	helpers.add(helper);
    }

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

}
