/**
 * 
 */
package cn.liutils.template;

import cn.liutils.api.key.IKeyHandler;
import cn.liutils.api.key.LIKeyProcess;
import cn.liutils.api.render.IPlayerRenderHook;
import cn.liutils.core.entity.EntityPlayerHook;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * All sorts of client registry functions.
 * @author WeAthFolD
 */
@SideOnly(Side.CLIENT)
public class LIClientRegistry {

	public static void addKey(String s, int key, boolean isRep, IKeyHandler process) {
		LIKeyProcess.instance.addKey(s, key, isRep, process);
	}
	
	
}
