/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.entity;

import java.util.HashSet;
import java.util.Set;

import cn.liutils.api.client.render.PlayerRenderHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * 用来辅助另一个Entity渲染的辅助Entity。
 * @author WeAthFolD
 *
 */
public class EntityPlayerRenderHelper extends Entity {

	public EntityPlayer player;
	public float rotationYawHead = 0.0F;
	
	private static Set<PlayerRenderHelper> helpers = new HashSet();
	public static Set<PlayerRenderHelper> activeHelpers = new HashSet();
	
	/**
	 */
	public EntityPlayerRenderHelper(EntityPlayer p, World world) {
		super(world);
		player = p;
		setPositionAndRotation(p.posX, p.posY, p.posZ, p.rotationYaw, p.rotationPitch);
		rotationYawHead = p.rotationYawHead;
	}
	
    @Override
	public void onUpdate()
    {
    	setPositionAndRotation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
		rotationYawHead = player.rotationYawHead;
		for(PlayerRenderHelper p : helpers) {
			if(p.isActivated(player, worldObj)) {
				activeHelpers.add(p);
			} else activeHelpers.remove(p);
		}
    }
    
    public static boolean hasActivate(EntityPlayer player) {
    	for(PlayerRenderHelper p : helpers)
    		if(p.isActivated(player, player.worldObj)) return true;
    	return false;
    }
    
    public static void addRenderHelper(PlayerRenderHelper helper) {
    	helpers.add(helper);
    }

	@Override
	protected void entityInit() {}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {}

}
