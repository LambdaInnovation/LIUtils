/**
 * 
 */
package cn.liutils.core.proxy;

import cn.liutils.api.player.ControlManager;
import cn.liutils.api.util.EntityManipHandler;
import cn.liutils.api.util.GenericUtils;
import cn.liutils.core.player.ControlHandler;
import net.minecraft.command.CommandHandler;

/**
 * @author Administrator
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
