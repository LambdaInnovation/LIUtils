/**
 * 
 */
package cn.liutils.core.proxy;

import cn.liutils.api.EntityManipHandler;
import cn.liutils.api.player.ControlManager;
import cn.liutils.core.player.ControlHandler;
import cn.liutils.util.GenericUtils;
import net.minecraft.command.CommandHandler;

/**
 * @author WeAthFolD
 *
 */
public class LICommonProxy {

	public void init() {
		EntityManipHandler.init();
		ControlHandler.init();
	}
	
	public void preInit() {}
	
	public void postInit() {}
	
	public void cmdInit(CommandHandler handler) {}
	
}
