/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.lambdacraft.cn/
 *
 * LIUtils is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * LIUtils是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.key;


/**
 * Handler for a LIKeyProcess registered key.
 * @see LIKeyProcess
 * @author WeAthFolD
 */
public interface IKeyHandler {

	/**
	 * Called when key is pressed down.
	 */
	public void onKeyDown(int keyCode, boolean tickEnd);

	/**
	 * Called when key is released.
	 */
	public void onKeyUp(int keyCode, boolean tickEnd);
	
	public void onKeyTick(int keyCode, boolean tickEnd);
}
