/**
 * 
 */
package cn.liutils.core.proxy;

import cn.liutils.api.util.EntityManipHandler;
import net.minecraft.command.CommandHandler;

/**
 * @author Administrator
 *
 */
public class LICommonProxy {

	public void init() {
		EntityManipHandler.init();
	}
	
	public void preInit() {}
	
	public void postInit() {}
	
	public void cmdInit(CommandHandler handler) {}
	
}
