/**
 * Code by Lambda Innovation, 2013.
 */
package cn.liutils.api.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author WeAthFolD
 *
 */
@SideOnly(Side.CLIENT)
public interface PlayerRenderHelper {
	/**
	 * 用于确定当前的辅助渲染是否被激活。
	 * 每Tick都会调用，不要进行太多运算。
	 * @param player
	 * @param world
	 * @return
	 */
	boolean isActivated(EntityPlayer player, World world);
	
	/**
	 * 方向自己调，反正帮你做好旋转了……
	 * @param player
	 * @param world
	 */
	void renderHead(EntityPlayer player, World world);
	
	void renderBody(EntityPlayer player, World world);
}
