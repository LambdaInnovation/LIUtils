/**
 * 
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
