/**
 * 
 */
package cn.liutils.core.proxy;

import net.minecraftforge.common.MinecraftForge;
import cn.liutils.core.event.LIEventListener;

/**
 * @author Administrator
 *
 */
public class LICommonProxy {

	public void init() {
		MinecraftForge.EVENT_BUS.register(new LIEventListener());
	}
	
	public void preInit() {}
	
	public void postInit() {}
	
}
