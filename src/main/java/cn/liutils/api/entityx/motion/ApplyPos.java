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

import cn.liutils.api.entityx.EntityX;
import cn.liutils.api.entityx.MotionHandler;

/**
 * @author WeathFolD
 *
 */
public class ApplyPos<T extends EntityX> extends MotionHandler<T> {
	
	double posX, posY, posZ;

	public ApplyPos(T ent, double x, double y, double z) {
		super(ent);
		setPos(x, y, z);
	}

	@Override
	public void onCreated() {}

	@Override
	public void onUpdate() {
		entity.posX = posX;
		entity.posY = posY;
		entity.posZ = posZ;
	}
	
	public void setPos(double px, double py, double pz) {
		posX = px;
		posY = py;
		posZ = pz;
	}

	@Override
	public String getID() {
		return "setpos";
	}

}
