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
package cn.liutils.api.entityx.motion;

import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 *
 */
public class GravityApply extends MotionHandler {
	
	public static final double DEFAULT_GRAVITY_VALUE = 0.08;
	
	public double gravity;

	public GravityApply(EntityX ent) {
		this(ent, DEFAULT_GRAVITY_VALUE);
	}
	
	public GravityApply(EntityX ent, double grav) {
		super(ent);
		gravity = grav;
	}
	
	@Override
	public int getPriority() {
		return 1007;
	}

	@Override
	public void onCreated() {}

	@Override
	public void onUpdate() {
		if(!entity.onGround) {
			this.entity.motionY -= gravity;
		}
	}

	@Override
	public String getID() {
		return "gravity";
	}

}
