package cn.liutils.api.player.lock;

import org.lwjgl.input.Mouse;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.MouseEvent;
import cn.liutils.api.player.MouseHelperX;
import cn.liutils.api.player.lock.LockBase.LockType;

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
