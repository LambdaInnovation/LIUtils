/**
 * 
 */
package cn.weaponry.api.client.render;

import cn.liutils.api.draw.DrawObject.EventType;
import cn.weaponry.api.info.InfWeapon;

/**
 * @author WeathFolD
 *
 */
public abstract class RenderEffect {

	public RenderEffect() {}
	
	public void startEffect(InfWeapon inf) {}

	public abstract void onEvent(EventType event, String part, InfWeapon inf, long passed);

}
