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
package cn.liutils.api.entityx;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import cn.liutils.api.entityx.motion.ApplyPos;
import cn.liutils.api.entityx.motion.FollowEntity;

/**
 * @author WeathFolD
 *
 */
public class FakeEntity extends EntityX {
	
	public FakeEntity(World world, double x, double y, double z) {
		super(world);
		this.clearDaemonHandlers();
		this.addDaemonHandler(new ApplyPos(this, x, y, z));
	}
	
	public FakeEntity(Entity target) {
		super(target.worldObj);
		this.clearDaemonHandlers();
		setPosition(target.posX, target.posY, target.posZ);
		this.addDaemonHandler(new FollowEntity(this, target));
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return false;
	}

}
