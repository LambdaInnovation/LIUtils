/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under 
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.liutils.api.entityx.motion;

import net.minecraft.util.Vec3;
import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 *
 */
public class ConstVelocity extends MotionHandler {
	
	public double u, v, w;
	public double velocity;
	
	public ConstVelocity(EntityX ent) {
		this(ent, 0, 0, 1, 0);
	}

	public ConstVelocity(EntityX ent, double u, double v, double w, double vel) {
		super(ent);
		this.u = u;
		this.v = v;
		this.w = w;
		normalize();
		this.velocity = vel;
	}

	@Override
	public void onCreated() {}

	@Override
	public void onUpdate() {
		entity.motionX = u * velocity;
		entity.motionY = v * velocity;
		entity.motionZ = w * velocity;
	}
	
	private void normalize() {
		double d = Math.sqrt(u * u + v * v + w * w);
		u /= d;
		v /= d;
		w /= d;
	}

	@Override
	public String getID() {
		return "const_vel";
	}

}
