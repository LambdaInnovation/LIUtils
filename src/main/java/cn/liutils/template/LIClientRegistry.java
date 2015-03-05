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
