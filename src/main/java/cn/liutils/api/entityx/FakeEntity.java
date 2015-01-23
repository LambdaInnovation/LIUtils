/**
 * 
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
		this.addDaemonHandler(new FollowEntity(this, target));
	}
	
	@Override
	public boolean shouldRenderInPass(int pass) {
		return false;
	}

}
