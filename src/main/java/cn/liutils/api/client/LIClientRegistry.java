/**
 * 
 */
package cn.liutils.api.client;

import cn.liutils.api.client.key.IKeyHandler;
import cn.liutils.api.client.render.PlayerRenderHandler;
import cn.liutils.core.client.register.LIKeyProcess;
import cn.liutils.core.entity.EntityPlayerRender;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 客户端的各种注册辅助。
 * @author WeAthFolD
 *
 */
@SideOnly(Side.CLIENT)
public class LIClientRegistry {

	public static void addKey(String s, int key, boolean isRep, IKeyHandler process) {
		LIKeyProcess.addKey(s, key, isRep, process);
	}
	
	public static void addPlayerRenderingHelper(PlayerRenderHandler helper) {
		EntityPlayerRender.addRenderHelper(helper);
	}
	
	
}
