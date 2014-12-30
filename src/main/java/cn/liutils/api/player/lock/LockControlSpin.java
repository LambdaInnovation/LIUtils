package cn.liutils.api.player.lock;

import org.lwjgl.input.Mouse;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
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
	public void onEvent(Event event) {
		if (((RenderTickEvent) event).phase == Phase.START) {
			MouseHelperX.lock();
			Mouse.setCursorPosition(x, y);
			return;
		}
		incorrect(event);
	}
	
	@Override
	public void register() {
		lied.setRenderTick.add(this);
	}
	
	@Override
	public void unregister() {
		lied.setRenderTick.remove(this);
		MouseHelperX.unlock();
	}
}
