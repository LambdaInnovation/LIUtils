/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Automatically attached on all the player and renders whenever player model is rendered.
 * Used for rendering custom stuffs.(Typically in 3rd person)
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
public interface IPlayerRenderListener {
	
	/**
	 * Get whether the render process is activated or not
	 * @param player
	 * @param world
	 * @return
	 */
	boolean isActivated(EntityPlayer player, World world);
	
	void renderHead(EntityPlayer player, World world);
	
	void renderBody(EntityPlayer player, World world);
}
