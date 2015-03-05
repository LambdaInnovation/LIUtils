/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
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
		//ControlHandler.init();
	}
	
	public void preInit() {}
	
	public void postInit() {}
	
	public void cmdInit(CommandHandler handler) {}
	
}
