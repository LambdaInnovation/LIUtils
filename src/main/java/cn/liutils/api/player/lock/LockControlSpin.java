package cn.liutils.api.player.lock;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.input.Mouse;

import cn.liutils.core.player.MouseHelperX;

public class LockControlSpin extends LockBase {

	public static final LockType TYPE = LockType.CONTROL_SPIN;
	
	private int x, y;
	
	public LockControlSpin(ByteBuf buf) {
		super(TYPE, buf);
		x = Mouse.getX();
		y = Mouse.getY();
	}

	public LockControlSpin(int ticks, EntityPlayer player) {
		super(TYPE, ticks);
		x = Mouse.getX();
		y = Mouse.getY();
	}
	
	@Override
	public void onTick(EntityPlayer player) {
		MouseHelperX.lock();
		Mouse.setCursorPosition(x, y);
	}
	
}
