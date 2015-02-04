/**
 * 
 */
package cn.weaponry.api.client.render;

import cn.weaponry.api.info.InfWeapon;

/**
 * @author WeathFolD
 *
 */
public abstract class RenderEffect {

	public RenderEffect() {}
	
	public void startEffect(InfWeapon inf) {}

	public abstract void beforeDraw(String part, InfWeapon inf, long passed);

}
