package cn.liutils.core.debug;

import net.minecraftforge.client.IItemRenderer;
import cn.liutils.core.debug.KeyMoving.EnumKey;

public interface Debug_MovingProcessor <T extends IItemRenderer>{

	boolean isProcessAvailable(IItemRenderer render, int mode);
	String doProcess(T render, EnumKey key, int mode, int tickTime);
	String getDescription(int mode);
	
}
