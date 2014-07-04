package cn.liutils.api.debug;

import net.minecraftforge.client.IItemRenderer;
import cn.liutils.api.debug.KeyMoving.EnumKey;

public interface Debug_MovingProcessor <T extends IItemRenderer>{

	boolean isProcessAvailable(IItemRenderer render, int mode);
	String doProcess(T render, EnumKey key, int mode, int tickTime);
	String getDescription(int mode);
	
}
